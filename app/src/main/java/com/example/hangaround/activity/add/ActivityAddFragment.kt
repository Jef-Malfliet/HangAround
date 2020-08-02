package com.example.hangaround.activity.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.hangaround.MainActivity
import com.example.hangaround.R
import com.example.hangaround.databinding.FragmentAddActivityBinding
import java.util.*

class ActivityAddFragment : Fragment() {

    private lateinit var binding: FragmentAddActivityBinding
    private lateinit var viewModel: ActivityAddViewModel
    private lateinit var listAdapter: ParticipantAddListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_activity, container, false)

        val args = arguments?.let { ActivityAddFragmentArgs.fromBundle(it) }

        val application = requireNotNull(activity).application
        viewModel = ViewModelProviders.of(
            this,
            ActivityAddViewModel.Factory(application, args!!.activityId)
        )
            .get(ActivityAddViewModel::class.java)

        listAdapter = ParticipantAddListAdapter()
        binding.rvParticipants.adapter = listAdapter

        setupNavigation()

        (requireActivity() as MainActivity).supportActionBar!!.title = "New Activity"

        setupDatePickers()

        viewModel.getActivity()
        viewModel.getParticipants()

        return binding.root
    }

    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()
        var startYear = calendar.get(Calendar.YEAR)
        var startMonth = calendar.get(Calendar.MONTH)
        var startDay = calendar.get(Calendar.DAY_OF_MONTH)
        var startHour = calendar.get(Calendar.HOUR_OF_DAY)
        var startMinute = calendar.get(Calendar.MINUTE)
        var endYear = calendar.get(Calendar.YEAR)
        var endMonth = calendar.get(Calendar.MONTH)
        var endDay = calendar.get(Calendar.DAY_OF_MONTH)
        var endHour = calendar.get(Calendar.HOUR_OF_DAY)
        var endMinute = calendar.get(Calendar.MINUTE)

        val startDateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker?, newYear: Int, newMonth: Int, newDay: Int ->
                val dateString = String.format("%d-%d-%d", newDay, newMonth + 1, newYear)
                binding.textViewStartDate.text = dateString
                startYear = newYear
                startMonth = newMonth
                startDay = newDay
            }

        binding.linearLayoutStartDate.setOnClickListener {
            val dateDialog = DatePickerDialog(
                requireContext(),
                R.style.Theme_AppCompat_Light_Dialog,
                startDateSetListener,
                startYear,
                startMonth,
                startDay
            )

            dateDialog.show()
        }

        val endDateSetListener =
            DatePickerDialog.OnDateSetListener { _, newYear, newMonth, newDay ->
                val dateString = String.format("%d-%d-%d", newDay, newMonth + 1, newYear)
                binding.textViewEndDate.text = dateString
                endYear = newYear
                endMonth = newMonth
                endDay = newDay
            }

        binding.linearLayoutEndDate.setOnClickListener {
            val dateDialog = DatePickerDialog(
                requireContext(),
                R.style.Theme_AppCompat_Light_Dialog,
                endDateSetListener,
                endYear,
                endMonth,
                endDay
            )

            dateDialog.show()
        }

        val startTimeListener = TimePickerDialog.OnTimeSetListener() { view, newHour, newMinute ->
            val timeString =
                String.format("%d:%s%d", newHour, if (newMinute < 10) "0" else "", newMinute)
            binding.textViewStartTime.text = timeString
            startHour = newHour
            startMinute = newMinute
        }

        binding.linearLayoutStartTime.setOnClickListener {
            val startTimePicker =
                TimePickerDialog(
                    requireContext(),
                    R.style.Theme_AppCompat_Light_Dialog,
                    startTimeListener,
                    startHour,
                    startMinute,
                    true
                )
            startTimePicker.show()
        }

        val endTimeListener = TimePickerDialog.OnTimeSetListener() { view, newHour, newMinute ->
            val timeString =
                String.format("%d:%s%d", newHour, if (newMinute < 10) "0" else "", newMinute)
            binding.textViewEndTime.text = timeString
            endHour = newHour
            endMinute = newMinute
        }

        binding.linearLayoutEndTime.setOnClickListener {
            val endTimePicker =
                TimePickerDialog(
                    requireContext(),
                    R.style.Theme_AppCompat_Light_Dialog,
                    endTimeListener,
                    endHour,
                    endMinute,
                    true
                )
            endTimePicker.show()
        }
    }

    private fun setupNavigation() {
        val backButtonCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    view!!.findNavController()
                        .navigate(ActivityAddFragmentDirections.actionActivityAddFragmentToActivityListFragment())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, backButtonCallback)
    }

    override fun onStart() {
        super.onStart()

        viewModel.activity.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.activity = it
                listAdapter.submitList(it.participants)
            }
        })

        viewModel.persons.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.persons = it
            }
        })
    }
}