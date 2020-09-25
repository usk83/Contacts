package io.github.usk83.contacts.network

import com.squareup.moshi.Json
import io.github.usk83.contacts.database.ContactEntity
import io.github.usk83.contacts.domain.Contact
import java.util.*

data class ContactsProperties(
    @Json(name = "results")
    val contacts: List<ContactProperty>
)

data class ContactProperty(
    @Json(name="")
    val id: String = UUID.randomUUID().toString(),
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val cell: String
)

data class Name(val first: String, val last: String) {
    val fullName: String
        get() = "$first $last"
}

data class Location(
    val street: Street,
    val city: String,
    @Json(name="state")
    val province: String,
    @Json(name="postcode")
    val postCode: String) {
    val fullAddress: String
        get() = "${street.fullStreet} $city, $province $postCode"
}

data class Street(val number: Double, val name: String) {
    val fullStreet: String
        get() = "$number $name"
}

fun ContactsProperties.asDomainModel(): List<Contact> {
    return contacts.map {
        Contact(
            id = it.id,
            gender = it.gender,
            name = it.name.fullName,
            location = it.location.fullAddress,
            email = it.email,
            cell = it.cell
        )
    }
}

fun ContactsProperties.asDatabaseModel(): List<ContactEntity> {
    return contacts.map {
        ContactEntity(
            id = it.id,
            gender = it.gender,
            name = it.name.fullName,
            location = it.location.fullAddress,
            email = it.email,
            cell = it.cell
        )
    }
}
