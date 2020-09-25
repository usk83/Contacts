package io.github.usk83.contacts.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDatabaseDao {
    @Insert
    fun insert(contact: ContactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts: List<ContactEntity>)

    @Update
    fun update(contact: ContactEntity)

    @Query("SELECT * FROM contacts WHERE name = :name COLLATE NOCASE")
    fun getContactByName(name: String): ContactEntity?

    @Query("SELECT * FROM contacts")
    fun getAllContacts() : LiveData<List<ContactEntity>>

    @Query("DELETE FROM contacts")
    fun clear()
}
