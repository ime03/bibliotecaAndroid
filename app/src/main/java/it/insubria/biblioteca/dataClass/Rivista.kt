package it.insubria.biblioteca.dataClass

import android.os.Parcel
import android.os.Parcelable

data class Rivista(var ID: String?=null, var titolo: String?=null, var dataPubblicazione: String?=null, var genere: String?=null, var periodicità:String?=null, var disponibilità: Int?=null, var copertina:String?=null, var dataInserimento:String?=null, var descrizione:String ?=null) :
    Parcelable {
    constructor(parcel: Parcel) : this(
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
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Rivista> {
        override fun createFromParcel(parcel: Parcel): Rivista {
            return Rivista(parcel)
        }

        override fun newArray(size: Int): Array<Rivista?> {
            return arrayOfNulls(size)
        }
    }
}