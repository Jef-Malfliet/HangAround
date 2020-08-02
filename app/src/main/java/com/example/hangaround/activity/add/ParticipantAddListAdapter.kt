package com.example.hangaround.activity.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hangaround.databinding.ListItemEditParticipantBinding
import com.example.hangaround.domain.Participant
import com.example.hangaround.domain.Person

class ParticipantAddListAdapter() :
    ListAdapter<Participant, ParticipantAddListAdapter.ViewHolder>(ParticipantDiffCallback()) {

    var persons: List<Person> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val participant = getItem(position)
        holder.bind(participant, persons)
    }

    class ViewHolder private constructor(val binding: ListItemEditParticipantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            participant: Participant, persons: List<Person>
        ) {
            binding.textViewRole.text = participant.role
            binding.textViewName.text =
                persons.find { person -> person.id == participant.id }?.name ?: "..."
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemEditParticipantBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class ParticipantDiffCallback : DiffUtil.ItemCallback<Participant>() {
    override fun areItemsTheSame(oldItem: Participant, newItem: Participant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Participant, newItem: Participant): Boolean {
        return oldItem == newItem
    }
}