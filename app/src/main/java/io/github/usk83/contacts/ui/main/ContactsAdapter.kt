package io.github.usk83.contacts.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.usk83.contacts.R
import io.github.usk83.contacts.databinding.ListContactItemBinding
import io.github.usk83.contacts.domain.Contact
import kotlinx.android.synthetic.main.list_header.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsAdapter : ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback()) {
    private val adapterScope = CoroutineScope(Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> HeaderViewHolder.from(parent)
            1 -> ContactViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(getItem(position).id)
            }
            is ContactViewHolder -> {
                val contact = getItem(position) as DataItem.ContactItem
                holder.bind(contact.contact)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> 0
            is DataItem.ContactItem -> 1
        }
    }

    fun submitContacts(contacts: List<Contact>) {
        adapterScope.launch {
            val items = ArrayList<DataItem>()
            if (contacts.isNotEmpty()) {
                val sortedContacts = contacts.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name } )
                var headerChar = sortedContacts[0].name.first().toUpperCase()
                items.add(DataItem.Header(headerChar))
                for (item in sortedContacts) {
                    val char = item.name.first().toUpperCase()
                    if (char != headerChar) {
                        headerChar = char
                        items.add(DataItem.Header(headerChar))
                    }
                    items.add(DataItem.ContactItem(item))
                }
            }
            withContext(Main) {
                submitList(items)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id === newItem.id
        }
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    class HeaderViewHolder(private val headerView: View): RecyclerView.ViewHolder(headerView) {
        fun bind(index: String) {
            headerView.headerTextView.text = index
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_header, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }

    class ContactViewHolder(val binding: ListContactItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.contact = contact
        }
        companion object {
            fun from(parent: ViewGroup): ContactViewHolder {
                return ContactViewHolder(ListContactItemBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }
    }

}

sealed class DataItem {
    abstract val id: String

    data class Header(val index: Char) : DataItem() {
        override val id = index.toString()
    }

    data class ContactItem(val contact: Contact) : DataItem() {
        override val id = contact.id
    }
}
