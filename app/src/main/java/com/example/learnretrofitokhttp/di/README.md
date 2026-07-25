Nous avons actuellement plusieurs objets qui doivent partager les mêmes instances :

``` kotlin
TokenStore ───────→ AuthInterceptor
    │
    └─────────────→ AuthRepository

DirectusApi ──────→ AuthRepository
    │
    └─────────────→ TestsRepository
```

Si nous créons deux DirectusNetworkClient, nous obtenons deux TokenStore différents :
- AuthRepository sauvegarde le token dans TokenStore A
- TestsRepository utilise un client lié à TokenStore B

Résultat : la requête getTests() n’est pas authentifiée
Le conteneur garantit que chaque objet n’est créé qu’une fois.
