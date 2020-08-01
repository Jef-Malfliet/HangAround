package com.example.hangaround.activity.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.hangaround.MainActivity
import com.example.hangaround.R
import com.example.hangaround.databinding.FragmentActivityDetailBinding


class ActivityDetailFragment : Fragment() {

    private lateinit var viewModel: ActivityDetailViewModel
    private lateinit var binding: FragmentActivityDetailBinding
    private lateinit var listAdapter: ParticipantListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_activity_detail, container, false)

        var args = arguments?.let { ActivityDetailFragmentArgs.fromBundle(it) }

        val application = requireNotNull(activity).application
        viewModel = ViewModelProviders.of(
            this,
            ActivityDetailViewModel.Factory(application, args!!.activityId)
        )
            .get(ActivityDetailViewModel::class.java)

        listAdapter = ParticipantListAdapter(viewModel.activity)
        binding.rvParticipants.adapter = listAdapter

        val backButtonCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    view!!.findNavController()
                        .navigate(ActivityDetailFragmentDirections.actionActivityDetailFragmentToActivityListFragment())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, backButtonCallback)

        viewModel.getParticipants()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.persons.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it)
            }
        })

        viewModel.activity.observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as MainActivity).supportActionBar!!.title = it.name
                binding.activity = it
            }
        })
    }
}