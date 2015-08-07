PJI - DownloadPDF
=================

Auteur(s)
---------

- Quentin Baert

Description
-----------

Le projet consiste à extraire des PDF des comptes rendus intégraux de l'Assemblée Nationale française des données concernant les scrutins qui y sont effectués. Les informations récupérées seront ensuite nettoyées, et organisées dans une ou plusieurs bases de données.

Strucure
--------

Les sources sont organisées selon le schéma d'un projet SBT (Scala Build Tool), c'est à dire :

```
build.sbt
src
├── main
│   ├── java
│   ├── resources
│   └── scala
│       ├── download
│       │   ├── PDFDownloader.scala
│       │   └── URLManager.scala
│       ├── pdftotext
│       │   └── PDFConverter.scala
│       ├── textanalysis
│       │   ├── CSVBuilder.scala
│       │   ├── VoteBuilder.scala
│       │   ├── VoteFilter.scala
│       │   └── VoteSeparator.scala
│       ├── utils
│       │   └── PatternDictionnary.scala
│       └── vote
│           ├── Vote.scala
│           ├── VoteDecision.scala
│           └── Voter.scala
└── test
    ├── java
    ├── resources
    └── scala
```

Étapes de réalisation
---------------------

## Récupération des PDF

Les sources des classes permettant de télécharger les PDF des comptes rendus intégraux de l'Assemblée Nationale se trouvent dans le package `download`.

### Procédé utilisé

Les pages d'indexes de toutes les legislatures (http://archives.assemblee-nationale.fr/X/index.asp, avec X compris entre 1 et 11) sont filtrées afin d'en tirer les URLs des pages de sessions. Ces pages de sessions sont filtrées à leur tour afin d'obtenir les URLs des PDF.

Une fois la liste de toutes les URLs établie, les PDF correspondant à chacune d'entre elles sont téléchargés et organisés localement selon leur legislature, années et session.

Il est possible de récupérer l'intégralité en une fois. Toutefois, la possibilité de télécharger les PDF par paquet de 100 a été implémentée afin de rester "courtois" et éviter de surcharger les serveurs contenant les fichiers.

## Convertion des PDF en fichiers texte

Les sources des classes permettant de convertir les PDF des comptes rendus intégraux en fichiers textes se trouvent dans le package `pdftotext`.

### Procédé utilisé

Les PDF précédemment téléchargés ont été convertis en fichiers texte grâce à la libraire *PDFBox* (https://pdfbox.apache.org/).

Pour cela, tous les dossiers localement crées à l'étape précédente sont parcourus et convertis un par un.  
Une arborescence similaire est créee : les fichiers textes sont donc toujours organisés selon leur législature, années et session.

## Filtrage des fichiers qui contiennent des scrutins

La classe `VoteFilter` permet de filtrer les fichiers contenant un ou plusieurs scrutins. Cette classe se trouve dans le package `textAnalysis`.

### Procédé utilisé

Les fichiers contenant un ou plusieurs scrutins terminent tous par une section "Annexes au procès verbal". Sous forme de fichiers textes, les données sont facilement explorables. Il a donc suffit d'écrire une expression régulière qui trouve les fichiers qui contiennent une telle section.

Les fichiers repérés comme contenant des scrutins sont ensuite copiés et réorganisés localement, toujours selon leurs législature, années et session.

## Modélisation objet d'un scrutin

Pour des raisons pratiques, et parcequ'il est plus facile de manipuler des objets plutôt que du texte brut, les scrutins sont convertis en objets avant d'être manipulés.

Le package `vote` contient l'ensemble des classes qui permettent la représentation objet d'un scrutin.

En résumé :  
    - la classe `Voter` représente un votant  
    - les objets `VoteDecision` représentent les différents décisions que peut prendre un votant : pour, contre, abstention, non-votant  
    - la classe `Vote` représente un scrutin avec toutes les informations intéréssantes qui le concernent

## Isolation des différents scrutins présents dans un fichier

Les fichiers marqués l'ont été comme contenant un ou plusieurs scrutins. Une première étape avant de modéliser ces scrutins est de les isoler.

La classe `VoteSeparator` se charge de séparer les différents scrutins d'un fichier.

### Procédé utilisé

Les différents scrutins sont toujours annoncés de la même manière dans les fichiers : chacun d'eux commence par la ligne `SCRUTIN N°X` avec X le numéros du scrutin. Une expréssion régulière est donc écrite afin de détecter chacunes des occurences de cette ligne. Il suffit ensuite de séparer le texte selon les indices de ces lignes.

## Modélisation du texte représentant un scrutin en objet `Vote`

L'étape précédente permet d'isoler les portions de texte qui contiennent un scrutin. La prochaine étape est donc de construire des objet `Vote` à partir du texte brut.

Pour cela, de nombreuses expréssions régulières ont été écrites afin d'isoler les différentes informations du scrutins (numéro, sujet, groupes, etc). Ces expréssions régulières se trouvent dans la classe utilitaire `PatternDictionnary` du package `util`.

### Procédé utilisé

Afin d'obtenir un objet `Vote` à partir du texte d'un scrutin, les différents expréssions régulières sont appliqués les unes après les autres afin de trouver les informations intéréssantes. C'est également à cette étape que le texte est nettoyé afin d'obtenir les données les plus propres possible.

## Convertion d'un fichier en CSV

L'objet `CSVBuilder` du package `textanalysis` permet de convertir un fichier texte contenant un scrutin en fichier CSV.

La méthode `CSVBuilder.buildCSVFileFrom` permet de convertir un fichier texte en CSV, on lui fournit le chemin du fichier texte et la legilsature correspondante.

La méthode `CSVBuilder.buildAllCSVFor` permet de convertir l'ensemble des fichiers textes d'une législature en fichiers CSV, on lui fournit le numéro de la législature.

Utilisation
-----------

L'execution des différentes fonctionnalités necessite Scala Build Tool (http://www.scala-sbt.org/).

## Récupération des PDF

* Pour récupérer l'ensemble des PDF en une seule fois

```
$ sbt
> compile
> console
scala> PDFDownloader.downloadAll
```

* Pour récupérer le n-ième paquet de 100 PDF

```
$ sbt
> compile
> console
scala> PDFDownloader downloadGroupNb X
```

* Pour récupérer les PDF de la n-ième législature

```
$ sbt
> compile
> console
scala> PDFDownloader downloadLeg Y
```

## Conversion des PDF en fichiers texte

```
$ sbt
> compile
> console
scala> PDFConverter.convertAll
```

## Filtrage des fichiers contenant un ou plusieurs scrutins

```
sbt
> compile
> console
scala> VoteFilter.filterAllTxtFiles
```

## Convertion de l'ensemble des fichiers d'une législature

```
sbt
> compile
> console
scala> CSVBuilder buildAllCSVFor Z
```

Où :  
- `$` est l'invite de commande du shell  
- `>` est l'invite de commande de sbt  
- `scala>` est l'invite de commande de l'interpréteur Scala  
- `X` est le nombre correspondant au Xième paquet de 100 PDF à télécharger  
- `Y` est le numéro de la législature dont on souhaite télécharger les PDF
- `Z` est le numéro de la législature dont on souhaite obtenir les CSV
