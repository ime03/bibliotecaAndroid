package it.insubria.biblioteca

data class Book(
    var isbn:String?=null, var titolo: String?=null, var autore: String?=null, var genere: String?=null, var disponibilità: Int, var copertina:String?=null
)
