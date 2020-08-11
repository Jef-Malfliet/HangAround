package com.malfliet.hangaround.friends.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.malfliet.hangaround.MainActivity
import com.malfliet.hangaround.R
import com.malfliet.hangaround.databinding.FragmentAddFriendBinding
import com.malfliet.hangaround.domain.Person

class FriendAddFragment : Fragment() {

    private lateinit var binding: FragmentAddFriendBinding
    private lateinit var viewmodel: FriendAddViewModel
    private lateinit var listAdapter: FriendsListAddAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_friend, container, false)

        val application = requireNotNull(activity).application
        viewmodel = ViewModelProviders.of(
            this,
            FriendAddViewModel.Factory(application)
        ).get(FriendAddViewModel::class.java)

        val actionbar = (requireActivity() as MainActivity).supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(false)
        actionbar.title = "Add Friend"

        listAdapter = FriendsListAddAdapter(FriendListener {
            viewmodel.addFriend(it)
            binding.editTextSearch.setText("")
            listAdapter.submitList(emptyList())
        })
        binding.rvNewFriends.adapter = listAdapter

        binding.imageButtonSearch.setOnClickListener {
            val nameString = binding.editTextSearch.text.toString()
            viewmodel.getPersonsWithNameLike(nameString)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewmodel.persons.observe(this, Observer {
            it?.let {
                val nameString = binding.editTextSearch.text.toString()
                if (nameString.isNotEmpty()) {
                    val tempList = mutableListOf<Person>()
                    tempList.addAll(it)
                    listAdapter.submitList(tempList.filter { person -> person.id != viewmodel.personId }
                        .filter { person -> person.name.contains(nameString) })
                }
            }
        })
    }
}