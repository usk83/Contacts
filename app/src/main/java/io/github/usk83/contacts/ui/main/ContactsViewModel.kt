package io.github.usk83.contacts.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.usk83.contacts.database.ContactDatabase.Companion.getInstance
import io.github.usk83.contacts.domain.Contact
import io.github.usk83.contacts.repository.ContactsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class ContactsViewModel(application: Application) : ViewModel() {

    private val contactsRepository: ContactsRepository = ContactsRepository(getInstance(application))

    val contacts: LiveData<List<Contact>> = contactsRepository.contacts

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Main )

    fun refreshContactsData() {
        coroutineScope.launch {
            try {
                contactsRepository.refreshContacts()
            } catch (e: Exception) {
                Log.e("ContactsViewModel", "Fatal: ${e.message}")
            }
        }
    }

    fun clearContactsData() {
        coroutineScope.launch {
            contactsRepository.clearContacts()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ContactsViewModel(application) as T
        }
    }

}
