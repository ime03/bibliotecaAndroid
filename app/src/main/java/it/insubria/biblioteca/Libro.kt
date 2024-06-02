package it.insubria.biblioteca

import android.os.Parcel
import android.os.Parcelable

data class Libro(var autore : String ?= null,var titolo : String ?= null,var copertina: String? = null,var genere : String? = null, var disponibilita: Int = 0)
    : Parcelable {
        constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(autore)
            parcel.writeString(titolo)
            parcel.writeString(copertina)
            parcel.writeString(genere)
            parcel.writeInt(disponibilita)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Libro> {
            override fun createFromParcel(parcel: Parcel): Libro {
                return Libro(parcel)
            }

            override fun newArray(size: Int): Array<Libro?> {
                return arrayOfNulls(size)
            }
        }
}
