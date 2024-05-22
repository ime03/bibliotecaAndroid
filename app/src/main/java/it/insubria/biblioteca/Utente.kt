package it.insubria.biblioteca

import java.util.Date

data class Utente(
    var id:String?=null,
    var nome:String?=null,
    var cognome:String?=null,
    var cf:String?=null,
    var dataNascita:String?=null,
    var ruolo: String?=null
)
