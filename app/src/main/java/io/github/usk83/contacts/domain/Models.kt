package io.github.usk83.contacts.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val id: String,
    val gender: String?,
    val name: String,
    val location: String?,
    val email: String?,
    val cell: String
) : Parcelable {
    constructor(name: String, cell: String) : this("", null, name, null, null, cell)
}
