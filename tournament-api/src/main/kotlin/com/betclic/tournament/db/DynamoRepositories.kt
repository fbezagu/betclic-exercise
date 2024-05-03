package com.betclic.tournament.db

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import aws.sdk.kotlin.services.dynamodb.waiters.waitUntilTableExists
import aws.smithy.kotlin.runtime.net.url.Url
import com.betclic.tournament.domain.PlayerRepository
import com.betclic.tournament.domain.Player
import com.betclic.tournament.domain.Repositories
import kotlinx.coroutines.runBlocking
import java.util.*

class DynamoRepositories : Repositories() {
    override fun players(): PlayerRepository = DynamoPlayerRepository()
    override fun init() {
        DynamoPlayerRepository().init()
    }
}

private const val PLAYERS_TABLE_NAME = "Players"

class DynamoPlayerRepository : PlayerRepository {
    fun init() {
        if (!tableExists()) {
            println("Table players doesn't exist ---> CREATE")
            createTable()
        }
    }

    /*
     Query functions
     */

    override fun all(): List<Player> {
        val request = ScanRequest { tableName = PLAYERS_TABLE_NAME }

        getClient().use { ddb ->
            val result = mutableListOf<Player>()
            runBlocking {
                val response = ddb.scan(request)
                response.items?.forEach { item -> result.add(playerFromItem(item)) }
            }
            return result
        }
    }

    override fun getByNickname(nickname: String): Player? {
        return all().find { it.nickname == nickname }
    }

    override fun getById(id: String): Player? {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["Id"] = AttributeValue.S(id)
        val request = GetItemRequest {
            key = keyToGet
            tableName = PLAYERS_TABLE_NAME
        }
        getClient().use { ddb ->
            var result: Player?
            runBlocking {
                val returnedItem = ddb.getItem(request)
                result = if (returnedItem.item == null) {
                    null
                } else {
                    playerFromItem(returnedItem.item!!)
                }
            }
            return result
        }
    }

    override fun countWithScoreHigherThan(score: Int): Int {
        return all().filter { it.score > score }.size
    }

    override fun allSortedByScore(): List<Player> {
        return all().sortedByDescending { it.score }
    }

    override val count: Int
        get() {
            getClient().use { ddb ->
                val result: Int
                runBlocking {
                    val response = ddb.scan(ScanRequest {
                        tableName = PLAYERS_TABLE_NAME
                        select = Select.fromValue("COUNT")
                    })
                    result = response.count
                }
                return result
            }
        }

    /*
    Update data functions
     */

    override fun add(player: Player) : Player{
        val itemValues = mutableMapOf<String, AttributeValue>()
        val playerWithId = player.copy(id = UUID.randomUUID().toString())
        itemValues["Id"] = AttributeValue.S(playerWithId.id)
        itemValues["Nickname"] = AttributeValue.S(playerWithId.nickname)
        itemValues["Score"] = AttributeValue.N(playerWithId.score.toString())

        val request = PutItemRequest {
            tableName = PLAYERS_TABLE_NAME
            item = itemValues
        }

        getClient().use { ddb -> runBlocking { ddb.putItem(request) } }
        return playerWithId
    }

    override fun update(player: Player) {
        val itemKey = mutableMapOf<String, AttributeValue>()
        itemKey["Id"] = AttributeValue.S(player.id)
        val updatedValues = mutableMapOf<String, AttributeValueUpdate>()
        updatedValues["Score"] = AttributeValueUpdate {
            value = AttributeValue.N(player.score.toString())
            action = AttributeAction.Put
        }
        updatedValues["Nickname"] = AttributeValueUpdate {
            value = AttributeValue.S(player.nickname)
            action = AttributeAction.Put
        }

        val request = UpdateItemRequest {
            tableName = PLAYERS_TABLE_NAME
            key = itemKey
            attributeUpdates = updatedValues
        }

        getClient().use { ddb -> runBlocking { ddb.updateItem(request) } }
    }

    override fun clear() {
        dropTable()
        createTable()
    }

    /*
     Table manipulation functions
     */

    private fun createTable() {
        val attDef = AttributeDefinition {
            attributeName = "Id"
            attributeType = ScalarAttributeType.S
        }

        val keySchemaVal = KeySchemaElement {
            attributeName = "Id"
            keyType = KeyType.Hash
        }

        val provisionedVal = ProvisionedThroughput {
            readCapacityUnits = 1
            writeCapacityUnits = 1
        }

        val request = CreateTableRequest {
            attributeDefinitions = listOf(attDef)
            keySchema = listOf(keySchemaVal)
            provisionedThroughput = provisionedVal
            tableName = PLAYERS_TABLE_NAME
        }

        getClient().use { ddb ->
            runBlocking {
                val response = ddb.createTable(request)
                ddb.waitUntilTableExists { tableName = PLAYERS_TABLE_NAME }
                response.tableDescription!!.tableArn.toString()
            }
        }
    }

    private fun dropTable() {
        getClient().use { ddb ->
            runBlocking {
                ddb.deleteTable(DeleteTableRequest {
                    tableName = PLAYERS_TABLE_NAME
                })
            }
        }
    }

    private fun tableExists(): Boolean {
        val request = DescribeTableRequest { tableName = PLAYERS_TABLE_NAME }

        getClient().use { ddb ->
            var result: Boolean
            runBlocking {
                try {
                    ddb.describeTable(request)
                    result = true
                } catch (e: ResourceNotFoundException) {
                    result = false
                }
            }
            return result
        }
    }


    /*
    Utility functions
     */
    private fun getClient(): DynamoDbClient {
        return DynamoDbClient {
            region = "eu-west-3"
            endpointUrl = Url.parse("http://localhost:4566")
        }
    }

    private fun playerFromItem(item: Map<String, AttributeValue>): Player {
        val id = item["Id"]?.asS() ?: ""
        val nickname = item["Nickname"]?.asS() ?: ""
        val score = item["Score"]?.asN()?.toInt() ?: 0
        return Player(nickname, id, score)
    }
}
