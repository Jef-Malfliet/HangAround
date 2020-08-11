package com.malfliet.hangaround.activity.list

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfliet.hangaround.databinding.ListItemActivityBinding
import com.malfliet.hangaround.domain.Activity
import com.malfliet.hangaround.domain.Person

class ActivityListAdapter(
    private val clickListener: ActivityListener,
    val sharedPreferences: SharedPreferences
) :
    ListAdapter<Activity, ActivityListAdapter.ViewHolder>(
        ActivityDiffCallback()
    ) {

    var persons: List<Person>? = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = getItem(position)
        holder.bind(activity, persons!!, clickListener, sharedPreferences)
    }

    class ViewHolder private constructor(val binding: ListItemActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            persons: List<Person>,
            clickListener: ActivityListener,
            sharedPreferences: SharedPreferences
        ) {
            binding.activity = activity
            val person = persons.find { person -> person.id == activity.owner }
            binding.ownerName = if (person?.id!! == sharedPreferences.getString("personId", "")!!) {
                "You"
            } else {
                person.name
            }
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemActivityBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class ActivityListener(val clickListener: (activityId: String) -> Unit) {
    fun onClick(activity: Activity) = clickListener(activity.id!!)
}

class ActivityDiffCallback : DiffUtil.ItemCallback<Activity>() {
    override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
        return oldItem == newItem
    }
}