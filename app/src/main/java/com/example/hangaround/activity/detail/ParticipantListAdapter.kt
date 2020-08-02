package com.example.hangaround.activity.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hangaround.databinding.ListItemParticipantBinding
import com.example.hangaround.domain.Activity
import com.example.hangaround.domain.Person

class ParticipantListAdapter(val activity: LiveData<Activity>) :
    ListAdapter<Person, ParticipantListAdapter.ViewHolder>(PersonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val participant = getItem(position)
        holder.bind(participant, activity)
    }

    class ViewHolder private constructor(val binding: ListItemParticipantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            person: Person, activity: LiveData<Activity>
        ) {
            binding.textViewRole.text =
                activity.value!!.participants.find { participant -> participant.personId == person.id }?.role
                    ?: "owner"
            binding.textViewName.text = person.name
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemParticipantBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class PersonDiffCallback : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }
}