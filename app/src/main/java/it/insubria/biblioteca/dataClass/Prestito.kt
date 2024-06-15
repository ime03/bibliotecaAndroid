package it.insubria.biblioteca.dataClass

data class Prestito(var IdPrestito:String?=null, var idArticolo:String?=null, var IdUtente:String?=null, var dataInizio:String ?= null, var dataScadenza:String ?=null, var dataRestituzione:String ?=null)
