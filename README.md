PJI - DownloadPDF
=================

Auteur
------

- Quentin Baert

Description
-----------

Le projet consiste à extraire des PDF des comptes rendus intégraux de l'Assemblée Nationale française des données concernant les scrutins qui y sont effectués. Les informations récupérées seront ensuite nettoyées, et organisées dans une ou plusieurs bases de données.

## Récupération des PDFs

Les sources des classes permettant de télécharger les PDFs des comptes rendus intégraux de l'Assemblée Nationale se trouvent dans le package `download`.

### Procédé utilisé

Les pages d'indexes de toutes les legislatures (http://archives.assemblee-nationale.fr/X/index.asp, avec X compris entre 1 et 11) sont filtrées afin d'en tirer les URLs des pages de sessions. Ces pages de sessions sont filtrées à leur tour afin d'obtenir les URLs des PDFs.

Une fois la liste de toutes les URLs établie, les PDFs correspondant à chacune d'entre elles sont téléchargés et organisés localement selon leur legislature, années et session.

Il est possible de récupérer l'intégralité en une fois. Toutefois, la possibilité de télécharger les PDFs par paquet de 100 a été implémenté afin de rester "courtois" et éviter de surcharger les serveurs contenant les fichiers.

## Convertion des PDFs en fichiers texte

Les sources des classes permettant de convertir les PDFs des comptes rendus intégraux en fichiers texte se trouvent dans le package `pdftotext`.

### Procédé utilisé

Les PDFs précédemment téléchargés ont été convertis en fichiers texte grâce à la libraire *PDFBox* (https://pdfbox.apache.org/).

Pour cela, tous les dossiers localement crées à l'étape précédente sont parcourus et convertis un par un.  
Une arborescence similaire est créee : les fichiers texte sont donc toujours organisés selon leur legislature, années et session.

## Filtrage des fichiers contenant des scrutins

La classe `VoteFilter` permet de filtrer les fichiers contenant un ou plusieurs scrutins. Cette classe se trouve dans le package `textAnalysis`.

### Procédé utilisé

Les fichiers contenant un ou plusieurs scrutins terminent tous par une section "Annexes au procès verbal". Sous forme de fichiers textes, les données sont facilement explorable. Il a donc suffit d'écrire une expression régulière qui trouve les fichiers qui contiennent une telle section.

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
│       ├── textAnalysis
│       │   ├── Vote.scala
│       │   ├── VoteFilter.scala
│       │   └── VoteSeparator.scala
└── test
    ├── java
    ├── resources
    └── scala
```

Utilisation
-----------

L'execution des différentes fonctionnalités necessite Scala Build Tool (http://www.scala-sbt.org/).

## Récupération des PDFs

* Pour récupérer l'ensemble des PDFs en une seule fois

```
$ sbt
> compile
> console
scala> PDFDownloader.downloadAll
```

* Pour récupérer le n-ième paquet de 100 PDFs

```
$ sbt
> compile
> console
scala> PDFDownloader downloadGroupNb X
```

## Conversion des PDFs en fichiers texte

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

Où :  
- '$' est l'invite de commande du shell  
- '>' est l'invite de commande de sbt  
- 'scala>' est l'invite de commande de l'interpréteur Scala
- 'X' est le nombre correspondant au Xième paquet de 100 PDFs à télécharger  
