package io.github.usk83.contacts.ui.add

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.usk83.contacts.database.ContactDatabase
import io.github.usk83.contacts.domain.Contact
import io.github.usk83.contacts.repository.ContactsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddContactViewModel(application: Application) : ViewModel() {
    private val contactsRepository: ContactsRepository = ContactsRepository(ContactDatabase.getInstance(application))

    val name = MutableLiveData<String>()
    val cell = MutableLiveData<String>()

    private val _navigateToContacts = MutableLiveData<Boolean?>()
    val navigateToContacts: LiveData<Boolean?>
        get() = _navigateToContacts

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    fun onCancelClicked() {
        _navigateToContacts.value = true
    }

    fun onSaveClicked() {
        coroutineScope.launch {
            val contact = Contact(name.value!!, cell.value!!)
            contactsRepository.saveContact(contact)
            _navigateToContacts.value = true
        }
    }

    fun onContactsNavigated() {
        _navigateToContacts.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddContactViewModel(application) as T
        }
    }

}
