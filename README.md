# Description de l’API

L’API fournit les ressources suivantes :

| Ressource             | URI          | GET                                                                         | PUT                             | POST                     | DELETE                               | 
|-----------------------|--------------|-----------------------------------------------------------------------------|---------------------------------|--------------------------|--------------------------------------|
| Classement du tournoi | /rankings    | Liste des joueurs triée par score                                           | -                               | -                        | -                                    |
| Joueurs               | /players     | Liste des joueurs non triée                                                 | -                               | Ajoute un nouveau joueur | Supprime tous les joueurs du tournoi |
| Un joueur             | /player/{id} | Représentation d’un joueur avec son score et son classement dans le tournoi | Met à jour le score d’un joueur | -                        | -                                    |       

Toutes les représentations (envoyées et retournées) sont au format JSON. 

# Reste à faire
- Injection de dépendances avec Koin
- Script de démarrage
- Implémentation de la base de données
- Authentification
- Suppression du rank dans les requêtes qui ne le renseignent pas
- BackOffice d’administration