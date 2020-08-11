package com.malfliet.hangaround.friends.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.malfliet.hangaround.MainActivity
import com.malfliet.hangaround.R
import com.malfliet.hangaround.activity.list.ActivityListFragmentDirections
import com.malfliet.hangaround.databinding.FragmentFriendsListBinding

class FriendListFragment : Fragment() {

    private lateinit var binding: FragmentFriendsListBinding
    private lateinit var listAdapter: FriendsListAdapter
    private lateinit var viewmodel: FriendListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_friends_list, container, false)

        val application = requireNotNull(activity).application
        viewmodel = ViewModelProviders.of(
            this,
            FriendListViewModel.Factory(application)
        ).get(FriendListViewModel::class.java)

        val actionbar = (requireActivity() as MainActivity).supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(false)
        actionbar.title = "Friends"

        listAdapter = FriendsListAdapter(PersonListener{
            viewmodel.removeFriend(it)
        })
        binding.rvFriends.adapter = listAdapter

        binding.imageButtonSearch.setOnClickListener {
            val searchString = binding.editTextSearch.text.toString()
            val loggedInPerson =
                viewmodel.persons.value!!.find { person -> person.id == viewmodel.personId }
            val friends = viewmodel.persons.value!!.filter { person ->
                loggedInPerson!!.friends.contains(person.id)
            }
            val filteredList = friends.filter { person ->
                person.name.contains(
                    searchString,
                    true
                )
            }
            listAdapter.submitList(filteredList)
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(FriendListFragmentDirections.actionFriendsToFriendAddFragment())
        }

        viewmodel.getFriends()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewmodel.persons.observe(this, Observer {
            it?.let {
                val loggedInPerson = it.find { person -> person.id == viewmodel.personId }
                val friends = it.filter { person -> loggedInPerson!!.friends.contains(person.id) }
                listAdapter.submitList(friends)
            }
        })
    }
}