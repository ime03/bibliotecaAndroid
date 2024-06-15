package it.insubria.biblioteca.dataClass

import android.os.Parcel
import android.os.Parcelable

data class PrestitoLibro(var IdPrestito:String?=null, var IdArticolo:String?=null, var IdUtente:String?=null, var dataInizio:String ?= null, var dataScadenza:String ?=null,var dataRestituzione:String?=null, var ID: String?=null, var isbn:String?=null, var titolo: String?=null, var autore: String?=null, var genere: String?=null, var disponibilità: Int?=null, var copertina:String?=null, var data:String?=null, var descrizione:String ?=null):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<PrestitoLibro> {
        override fun createFromParcel(parcel: Parcel): PrestitoLibro {
            return PrestitoLibro(parcel)
        }

        override fun newArray(size: Int): Array<PrestitoLibro?> {
            return arrayOfNulls(size)
        }
    }
}
