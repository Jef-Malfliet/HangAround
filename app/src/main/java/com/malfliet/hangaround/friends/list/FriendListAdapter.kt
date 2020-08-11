package com.malfliet.hangaround.friends.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfliet.hangaround.databinding.ListItemFriendBinding
import com.malfliet.hangaround.domain.Person

class FriendsListAdapter(private val personListener: PersonListener) :
    ListAdapter<Person, FriendsListAdapter.ViewHolder>(
        PersonDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = getItem(position)
        holder.bind(person, personListener)
    }

    class ViewHolder private constructor(val binding: ListItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person, personListener: PersonListener) {
            binding.person = person
            binding.clickListener = personListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFriendBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class PersonListener(val clickListener: (personId: String) -> Unit) {
    fun onClick(person: Person) = clickListener(person.id!!)
}

class PersonDiffCallback : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }
}