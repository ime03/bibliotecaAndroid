package it.insubria.biblioteca

import android.os.Parcel
import android.os.Parcelable

data class PercorsoLettura(var ID:String?=null, var etàMinima:Int?=null, var etàMassima:Int?=null, var titolo:String?=null, var descrizione: String?=null, var dataInserimento:String?=null, var copertina:String?=null):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PercorsoLettura> {
        override fun createFromParcel(parcel: Parcel): PercorsoLettura {
            return PercorsoLettura(parcel)
        }

        override fun newArray(size: Int): Array<PercorsoLettura?> {
            return arrayOfNulls(size)
        }
    }
}

