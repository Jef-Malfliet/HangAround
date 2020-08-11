package com.malfliet.hangaround.friends.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfliet.hangaround.databinding.ListItemAddFriendBinding
import com.malfliet.hangaround.databinding.ListItemFriendBinding
import com.malfliet.hangaround.domain.Person

class FriendsListAddAdapter(private val friendListener: FriendListener) :
    ListAdapter<Person, FriendsListAddAdapter.ViewHolder>(
        PersonDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = getItem(position)
        holder.bind(person, friendListener)
    }

    class ViewHolder private constructor(val binding: ListItemAddFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person, friendListener: FriendListener) {
            binding.person = person
            binding.clickListener = friendListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAddFriendBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class FriendListener(val clickListener: (personId: String) -> Unit) {
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