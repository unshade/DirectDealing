# TelecomNancy DirectDealing

Services et objets entre particuliers.

## Prérequis

- Java JDK 17
- Gradle 8.2 et plus

## Installation

Clonez ce dépôt sur votre machine locale en utilisant :

Via HTTPS :
```bash
git clone https://gitlab.telecomnancy.univ-lorraine.fr/pcd2k24/codingweek-15.git
```

Via SSH :
```bash
git clone git@gitlab.telecomnancy.univ-lorraine.fr:pcd2k24/codingweek-15.git
```

## Configuration de l'Environnement

Assurez-vous que Java et Gradle sont correctement installés sur votre système. Vous pouvez vérifier cela en exécutant :

```bash
java -version
gradle -version
```

## Compilation et Exécution

Pour compiler le projet, exécutez :

```bash
gradle build
```

Pour exécuter l'application, utilisez :

```bash
gradle run
```

## Exécution des Tests

Pour lancer les tests, utilisez la commande suivante :

```bash
gradle test
```

## Database Seeding

Remplissez la base de donnée à l'aide de données de test avec la tâche `seedDatabase` :

```bash
gradle seedDatabase
```

## Exécution avec Initialisation Fraîche

Pour exécuter l'application avec une initialisation fraîche de la base de données, utilisez la tâche `freshRun` :

```bash
gradle freshRun
```

## Dépendances

Ce projet utilise diverses dépendances comme Hibernate, JavaFX, Lombok, etc., qui sont définies dans le fichier `build.gradle`.

## Packaging avec JLink

Ce projet utilise le plugin JLink pour créer une image de l'application. Pour créer une image de l'application, utilisez :

```bash
gradle jlink
```

L'image de l'application se trouvera dans `build/distributions`.