PJI - DownloadPDF
=================

Auteur
------

- Quentin Baert

Description
-----------

Ce programme permet de télécharger tous les comptes rendus intégraux de l'Assemblée Nationale sous forme de PDFs.  
Pour ce faire, les pages d'indexes de toutes les legislatures (http://archives.assemblee-nationale.fr/X/index.asp, avec X compris entre 1 et 11) sont filtrées afin d'en tirer les URLs des pages de sessions. Ces pages de sessions sont filtrées à leur tour afin d'obtenir les URLs des PDFs.

Une fois la liste de toutes les URLs établie, les PDFs correspondant à chacune d'entre elles sont téléchargés et organisés localement selon leur legislature, années et session.

Le programme, sous cette forme, permet uniquement de récupérer les PDFs par paquet de 100. Ceci pour rester "courtois" et éviter de surcharger les serveurs contenant les fichiers.

Strucure
--------

Les sources sont organisées selon le schéma d'un projet SBT (Scala Build Tool), c'est à dire :

```
|_ build.sbt  
|_ src  
	|_ main  
		|_ java  
		|_ resources  
		|_ scala  
			|_ download  
				|_ Main.scala  
				|_ PDFDownloader.scala  
				|_ URLManager.scala  
	|_ test  
```

Cependant, toutes les sources sont écrites en *Scala* et se trouvent dans le package **download**.

Utilisation
-----------

L'execution du programme necessite Scala Build Tool (http://www.scala-sbt.org/).

```
$ sbt
> compile
> run X
```

Où :  
- '$' est l'invite de commande du shell  
- '>' est l'invite de commande de sbt  
- 'X' est le nombre correspondant au Xième paquet de 100 PDFs à télécharger  
