package io.github.usk83.contacts.ui.add

import android.content.Context

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.usk83.contacts.R
import io.github.usk83.contacts.databinding.FragmentAddContactBinding
import io.github.usk83.contacts.ui.main.ContactsAdapter
import io.github.usk83.contacts.util.normalizeString


class AddContactFragment : Fragment() {

    private val viewModel: AddContactViewModel by lazy {
        val activity = requireNotNull(activity) { "Cannot initialize AddContactViewModel" }
        ViewModelProvider(this, AddContactViewModel.Factory(activity.application)).get(
            AddContactViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddContactBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToContacts.observe(this, Observer { navigateToContacts ->
            if (navigateToContacts != null && navigateToContacts) {
                this.findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
                viewModel.onContactsNavigated()
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            }
        })

        binding.nameEditText.doAfterTextChanged {
            binding.saveButton.isEnabled =
                isSaveButtonEnabled(binding.nameEditText.text.toString(), binding.cellEditText.text.toString())
        }

        binding.cellEditText.doAfterTextChanged {
            binding.saveButton.isEnabled =
                isSaveButtonEnabled(binding.nameEditText.text.toString(), binding.cellEditText.text.toString())
        }

        return binding.root
    }

    private fun isSaveButtonEnabled(nameText: String, cellText: String): Boolean {
        val name = normalizeString(nameText)
        val cell = normalizeString(cellText)
        return name.split(" ").size == 2 && cell.length == 10 && cell.isDigitsOnly()
    }

}
