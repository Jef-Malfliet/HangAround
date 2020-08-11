package com.malfliet.hangaround.activity.detail

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.malfliet.hangaround.MainActivity
import com.malfliet.hangaround.R
import com.malfliet.hangaround.databinding.FragmentActivityDetailBinding
import com.malfliet.hangaround.persistence.database.asDomainModel


class ActivityDetailFragment : Fragment() {

    private lateinit var viewModel: ActivityDetailViewModel
    private lateinit var binding: FragmentActivityDetailBinding
    private lateinit var listAdapter: ParticipantListAdapter
    private lateinit var args: ActivityDetailFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_activity_detail, container, false)

        args = arguments?.let { ActivityDetailFragmentArgs.fromBundle(it) }!!

        val application = requireNotNull(activity).application
        viewModel = ViewModelProviders.of(
            this,
            ActivityDetailViewModel.Factory(application, args!!.activityId)
        )
            .get(ActivityDetailViewModel::class.java)

        listAdapter = ParticipantListAdapter()
        binding.rvParticipants.adapter = listAdapter

        setupNavigation(args.activityId)

        setHasOptionsMenu(true)

        viewModel.getParticipants()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                findNavController().navigate(
                    ActivityDetailFragmentDirections.actionActivityDetailFragmentToActivityAddFragment()
                        .setActivityId(args.activityId)
                )
                return true
            }
            R.id.delete -> {
                viewModel.deleteActivity(args.activityId)
                findNavController().navigateUp()
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun setupNavigation(activityId: String) {
        val backButtonCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController()
                        .navigate(ActivityDetailFragmentDirections.actionActivityDetailFragmentToActivityListFragment())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, backButtonCallback)
    }

    override fun onStart() {
        super.onStart()

        viewModel.persons.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.persons = it
            }
        })

        viewModel.activity.observe(viewLifecycleOwner, Observer {
            it?.let {
                (requireActivity() as MainActivity).supportActionBar!!.title =
                    it.name
                binding.activity = it.asDomainModel()
                listAdapter.submitList(it.participants.sortedBy { participant -> participant.role })

                if (viewModel.personId != it.owner) {
                    setMenuVisibility(false)
                }
            }
        })
    }
}