package com.malfliet.hangaround.friends.add

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.malfliet.hangaround.MainActivity
import com.malfliet.hangaround.R
import com.malfliet.hangaround.activity.detail.ActivityDetailFragmentDirections
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
        actionbar.title = "Add Friend"

        listAdapter = FriendsListAddAdapter(FriendListener {
            viewmodel.addFriend(it)
            binding.editTextSearch.setText("")
            listAdapter.submitList(emptyList())
        })
        binding.rvNewFriends.adapter = listAdapter

        binding.imageButtonSearch.setOnClickListener {
            hideKeyboardFrom(context!!, view!!)
            val nameString = binding.editTextSearch.text.toString()
            viewmodel.getPersonsWithNameLike(nameString)
        }

        setupNavigation()

        return binding.root
    }

    private fun setupNavigation() {
        val backButtonCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController()
                        .navigate(FriendAddFragmentDirections.actionFriendAddFragmentToFriends())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, backButtonCallback)
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

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}