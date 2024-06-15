package it.insubria.biblioteca.dataClass

import android.os.Parcel
import android.os.Parcelable

data class PrestitoFilm(var IdPrestito:String?=null, var IdArticolo:String?=null, var IdUtente:String?=null, var dataInizio:String ?= null, var dataScadenza:String ?=null, var dataRestituzione:String?=null, var ID:String ?=null, var disponibilità:Int?=null, var titolo:String?=null, var copertina:String ?=null,  var dataInserimento:String?=null, var attori:String?=null, var annoUscita:Int?=null, var regista:String?=null, var trama:String?=null, var genere:String?=null):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<PrestitoFilm> {
        override fun createFromParcel(parcel: Parcel): PrestitoFilm {
            return PrestitoFilm(parcel)
        }

        override fun newArray(size: Int): Array<PrestitoFilm?> {
            return arrayOfNulls(size)
        }
    }
}
