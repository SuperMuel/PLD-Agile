# PLD-Agile

### Getting started

L'utilisation de [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/) est fortement recommandée pour le développement.

C'est gratuit pour les étudiants : https://www.jetbrains.com/community/education/#students

Le plus simple est de souscrire à [GitHub Student Developer Pack](https://education.github.com/pack?utm_source=github+jetbrains)
qui vous vérifiera votre statut étudiant, et vous permettra de bénéficier de plein d'offres pour les étudiants, comme
IntelliJ Idea ou Github Copilot gratuitement.

Le projet Java est géré par [Maven](https://maven.apache.org/). C'est ce qui gère les dépendances, et permet
à n'importe qui de faire tourner le projet rapidement sur n'importe quelle machine. Les paramètres du projet, comme les dépandences
sont inscrits dans [pom.xml](./pom.xml).

IntelliJ Idea vous aidera dans toutes les étapes pour faire tourner le projet sur votre machine. En cas de difficulités,
vous pourrez tenter d'effectuer manuellement les actions suivantes :
- [Installer Java JDK 21](https://www.oracle.com/fr/java/technologies/downloads/)
- [Installer Maven](https://maven.apache.org/install.html)

## ⚠️⚠️  Règles Git/Github : 

### Gestion des Branches
**Master/Main Branch** : La branche main (ou master) est la branche principale. Elle doit toujours contenir du code stable et prêt pour la production.

**Feature Branches** : Pour toute nouvelle fonctionnalité, correction de bug ou modification, créez une nouvelle branche à partir de main. Utilisez des noms de branches descriptifs, par exemple : feature/nouvelle-fonctionnalite, bugfix/correction-bug.

Creation et switch vers une nouvelle branche : `git checkout -b docs/add-documentation-for-app-launching`

**️Tests Avant de Pousser** : Exécutez tous les tests et assurez-vous qu'ils passent avant de pousser vos commits ou de créer une PR.

**Pas de Commit Direct sur main** : Ne poussez jamais directement sur la branche main. Utilisez des PR pour toutes les modifications.
