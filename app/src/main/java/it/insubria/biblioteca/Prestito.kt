package it.insubria.biblioteca

data class Prestito(var IdPrestito:String?=null, var IdLibro:String?=null, var IdUtente:String?=null, var dataInizio:String ?= null, var dataFine:String ?=null)
