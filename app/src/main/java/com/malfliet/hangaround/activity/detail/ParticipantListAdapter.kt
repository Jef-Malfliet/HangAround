package com.malfliet.hangaround.activity.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfliet.hangaround.databinding.ListItemParticipantBinding
import com.malfliet.hangaround.domain.Participant
import com.malfliet.hangaround.domain.Person

class ParticipantListAdapter() :
    ListAdapter<Participant, ParticipantListAdapter.ViewHolder>(PersonDiffCallback()) {

    var persons: List<Person> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val participant = getItem(position)
        holder.bind(participant, persons)
    }

    class ViewHolder private constructor(val binding: ListItemParticipantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            participant: Participant, persons: List<Person>
        ) {
            binding.textViewRole.text = participant.role
            binding.textViewName.text =
                persons.find { person -> person.id == participant.personId }?.name ?: "..."
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

class PersonDiffCallback : DiffUtil.ItemCallback<Participant>() {
    override fun areItemsTheSame(oldItem: Participant, newItem: Participant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Participant, newItem: Participant): Boolean {
        return oldItem == newItem
    }
}