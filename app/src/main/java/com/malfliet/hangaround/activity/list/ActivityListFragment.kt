package com.malfliet.hangaround.activity.list


import android.os.Bundle
import android.preference.PreferenceManager
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
import com.malfliet.hangaround.databinding.FragmentActivityListBinding

class ActivityListFragment : Fragment() {

    private lateinit var viewModel: ActivityListViewModel
    private lateinit var binding: FragmentActivityListBinding
    private lateinit var listAdapter: ActivityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_activity_list, container, false)

        val application = requireNotNull(activity).application
        viewModel = ViewModelProviders.of(
            this,
            ActivityListViewModel.Factory(
                application
            )
        )
            .get(ActivityListViewModel::class.java)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        listAdapter = ActivityListAdapter(ActivityListener { activityId ->
            findNavController().navigate(
                ActivityListFragmentDirections.actionActivityListFragmentToActivityDetailFragment(
                    activityId
                )
            )
        }, sharedPreferences)
        binding.rvActivities.adapter = listAdapter

        (requireActivity() as MainActivity).supportActionBar!!.title = "Activities"

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(ActivityListFragmentDirections.actionActivityListFragmentToActivityAddFragment())
        }

        setHasOptionsMenu(false)

        viewModel.getActivitiesContainingPerson(sharedPreferences.getString("personId", "")!!)
        viewModel.getActivitiesByOwner(sharedPreferences.getString("personId", "")!!)
        viewModel.refreshPersons()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.activities.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it)
            }
        })

        viewModel.persons.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.persons = it
            }
        })
    }
}
