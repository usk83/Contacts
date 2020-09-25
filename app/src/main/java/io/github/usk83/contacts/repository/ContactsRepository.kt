package io.github.usk83.contacts.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.github.usk83.contacts.database.ContactDatabase
import io.github.usk83.contacts.database.ContactEntity
import io.github.usk83.contacts.database.asDomainModel
import io.github.usk83.contacts.domain.Contact
import io.github.usk83.contacts.network.ContactsApi
import io.github.usk83.contacts.network.asDatabaseModel
import io.github.usk83.contacts.network.asDomainModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.util.*

class ContactsRepository(private val database: ContactDatabase) {

    val contacts: LiveData<List<Contact>> = Transformations.map(database.contactDatabaseDao.getAllContacts()) {
        it.asDomainModel()
    }

    suspend fun refreshContacts() {
        withContext(IO) {
            database.contactDatabaseDao.clear()
            val properties = ContactsApi.retrofitService.getProperties(24)
            database.contactDatabaseDao.insertAll(properties.asDatabaseModel())
        }
    }

    suspend fun clearContacts() {
        withContext(IO) {
            database.contactDatabaseDao.clear()
        }
    }

    suspend fun saveContact(contact: Contact) {
        var id = contact.id
        if (id.isBlank()) {
            id = UUID.randomUUID().toString()
        }
        withContext(IO) {
            val entity = ContactEntity(
                id = id,
                gender = contact.gender,
                name = contact.name,
                location = contact.location,
                email = contact.email,
                cell = contact.cell)
            database.contactDatabaseDao.insert(entity)
        }
    }

}
