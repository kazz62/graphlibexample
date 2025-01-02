Projet de slack day montrant différents graphiques faisables avec la librairie graphique [VICO](https://github.com/patrykandpatrick/vico).

Pendant l'implémentation, j'ai remarqué qu'elle ne propose pas de graphiques horizontaux tels que nous les utilisons dans la partie commerce de l'application Cube Instore.

La seule solution proposée sur le [Q&A](https://github.com/patrykandpatrick/vico/discussions/983) est d'utiliser un Modifier pour effectuer une rotation du composable. Cependant, cela pose de nouveaux problèmes quand à l'affichage des différentes légendes.

On pourrait peut-être contourner le soucis en faisant notre propre composant personnalisé pour ce type de chart. Cependant, on perdrat de vue l'objectif d'avoir une lib qui gère l'ensemble des graphes et qui est maintenue constamment.

Cette feature a déjà été proposée à l'équipe en charge de la librairie, mais le sujet n'est pas encore d'actualité d'après [cette partie](https://github.com/patrykandpatrick/vico/discussions/379) de leur Q&A.
