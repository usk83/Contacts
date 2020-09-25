package io.github.usk83.contacts.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.usk83.contacts.domain.Contact

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val id: String = "",
    val gender: String?,
    val name: String,
    val location: String?,
    val email: String?,
    val cell: String
)

fun List<ContactEntity>.asDomainModel(): List<Contact> {
    return map {
        Contact(
            id = it.id,
            gender = it.gender,
            name = it.name,
            location = it.location,
            email = it.email,
            cell = it.cell
        )
    }
}
