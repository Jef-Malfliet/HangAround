package com.malfliet.hangaround.activity.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfliet.hangaround.databinding.ListItemEditParticipantBinding
import com.malfliet.hangaround.domain.Participant
import com.malfliet.hangaround.domain.Person

class ParticipantAddListAdapter(
    private val editClickListener: EditClickListener,
    private val deleteClickListener: DeleteClickListener
) :
    ListAdapter<Participant, ParticipantAddListAdapter.ViewHolder>(ParticipantDiffCallback()) {

    var persons: List<Person> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val participant = getItem(position)
        holder.bind(participant, persons, editClickListener, deleteClickListener)
    }

    class ViewHolder private constructor(val binding: ListItemEditParticipantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            participant: Participant,
            persons: List<Person>,
            editClickListener: EditClickListener,
            deleteClickListener: DeleteClickListener
        ) {
            binding.participant = participant
            binding.textViewName.text =
                persons.find { person -> person.id == participant.personId }?.name ?: "..."
            binding.editClickListener = editClickListener
            binding.deleteClickListener = deleteClickListener
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

class EditClickListener(val clickListener: (participantPersonId: String) -> Unit) {
    fun onClick(participant: Participant) = clickListener(participant.personId)
}

class DeleteClickListener(val clickListener: (participantPersonId: String) -> Unit) {
    fun onClick(participant: Participant) = clickListener(participant.personId)
}

class ParticipantDiffCallback : DiffUtil.ItemCallback<Participant>() {
    override fun areItemsTheSame(oldItem: Participant, newItem: Participant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Participant, newItem: Participant): Boolean {
        return oldItem == newItem
    }
}