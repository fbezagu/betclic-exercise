# Instructions

> Les scripts suivants fonctionnent uniquement sous Linux x64 pour l’instant.

Pour démarrer l’API, aller dans le dossier racine et lancer :

    ./run.sh

Pour arrêter (notamment localstack) :

    ./stop.sh

# Description de l’API

L’API fournit les ressources suivantes :

| Ressource             | URI          | GET                                                                         | PUT                             | POST                     | DELETE                               | 
|-----------------------|--------------|-----------------------------------------------------------------------------|---------------------------------|--------------------------|--------------------------------------|
| Classement du tournoi | /rankings    | Liste des joueurs triée par score                                           | -                               | -                        | -                                    |
| Joueurs               | /players     | Liste des joueurs non triée                                                 | -                               | Ajoute un nouveau joueur | Supprime tous les joueurs du tournoi |
| Un joueur             | /player/{id} | Représentation d’un joueur avec son score et son classement dans le tournoi | Met à jour le score d’un joueur | -                        | -                                    |       

Toutes les représentations (envoyées et retournées) sont au format JSON suivant :

```
{
  "id": "abcd38"
  "nickname": "Rémi",
  "score": 25,
  "rank": 2
}
```

# Reste à faire avant mise en production

- Authentification
- Suppression du rank dans les requêtes qui ne le renseignent pas
- BackOffice d’administration
- Repository DynamoDB
    - Get par nickname plus performant
    - allSortedByScore plus performant, en utilisant un sort natif de dynamo
    - countWithScoreHigherThan plus performant, en utilisant un filtre + count natifs de dynamo
    - Tester les inconsistances de types de données
- Script de démarrage
    - Gérer MacOS et Windows
- Créer un Dockerfile (ou autre conteneur)
- Mettre en place la CI/CD (Jenkinsfile, etc)
- Configurer DynamoDB sur AWS et pointer dessus
- Configurer les credentials AWS avec IAM et les injecter