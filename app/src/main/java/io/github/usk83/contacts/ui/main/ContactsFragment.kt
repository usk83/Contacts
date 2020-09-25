package io.github.usk83.contacts.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.usk83.contacts.R
import io.github.usk83.contacts.databinding.FragmentContactsBinding
import io.github.usk83.contacts.domain.Contact

class ContactsFragment : Fragment() {

    private val viewModel: ContactsViewModel by lazy {
        val activity = requireNotNull(activity) { "Cannot initialize ContactsViewModel" }
        ViewModelProvider(this, ContactsViewModel.Factory(activity.application)).get(
            ContactsViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContactsBinding.inflate(inflater)
        val adapter = ContactsAdapter()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.contactList.adapter = adapter

        viewModel.contacts.observe(this, Observer {
            it.apply {
                adapter.submitContacts(it)
            }
        })

        binding.addFab.setOnClickListener {
            this.findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.refreshContactsData()
            }
            R.id.action_clear -> {
                viewModel.clearContactsData()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

}
