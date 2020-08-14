package com.malfliet.hangaround.activity.add

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.malfliet.hangaround.MainActivity
import com.malfliet.hangaround.R
import com.malfliet.hangaround.databinding.FragmentAddActivityBinding
import com.malfliet.hangaround.domain.Participant
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.database.asDomainModel
import kotlinx.android.synthetic.main.fragment_add_activity.*
import java.util.*

class ActivityAddFragment : Fragment() {

    private lateinit var binding: FragmentAddActivityBinding
    private lateinit var viewModel: ActivityAddViewModel
    private lateinit var listAdapter: ParticipantAddListAdapter
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var args: ActivityAddFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_activity, container, false)

        args = arguments?.let { ActivityAddFragmentArgs.fromBundle(it) }!!

        (requireActivity() as MainActivity).supportActionBar!!.title =
            if (args.activityId == null) {
                "New Activity"
            } else {
                ""
            }

        val application = requireNotNull(activity).application
        viewModel = ViewModelProviders.of(
            this,
            ActivityAddViewModel.Factory(application, args.activityId)
        )
            .get(ActivityAddViewModel::class.java)

        setupListAdapter(inflater)

        setupNavigation()

        setupDatePickers()

        spinnerAdapter = ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item
        )

        setupParticipantDialog(inflater)

        setHasOptionsMenu(true)

        viewModel.getActivity()
        viewModel.getParticipants()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_add_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            if (checkAllInputsFilledIn() && checkInputsValid() && checkDatesAfterToday()) {
                val succeeded = if (args.activityId == null) {
                    createActivity()
                } else {
                    updateActivity()
                }
                if (succeeded) {
                    findNavController()
                        .navigate(ActivityAddFragmentDirections.actionActivityAddFragmentToActivityListFragment())
                }
            } else {
                binding.textviewError.visibility = View.VISIBLE

            }
            return true
        } else {
            return false
        }
    }

    private fun setupListAdapter(inflater: LayoutInflater) {
        val editClickListener = EditClickListener { participantPersonId ->
            val role =
                viewModel.participants.value!!.find { participant -> participant.personId == participantPersonId }?.role
            val person =
                viewModel.persons.value!!.find { person -> person.id == participantPersonId }
            val participants =
                viewModel.persons.value!!.filter { p -> p.id != viewModel.personId }
                    .filter { p ->
                        !viewModel.participants.value!!.map { part -> part.personId }.contains(p.id)
                    }.toMutableList()
            participants.add(person!!)
            val participantNames = participants.map { p -> p.name }.toMutableList()
            spinnerAdapter.clear()
            spinnerAdapter.addAll(participantNames)
            buildParticipantDialog(inflater, person, role, participants)
        }

        val deleteClickListener = DeleteClickListener { participantPersonId ->
            val deleteParticipant =
                viewModel.participants.value!!.find { participant -> participant.personId == participantPersonId }
            viewModel.participants.value!!.remove(deleteParticipant)
            viewModel.participants.notifyObserver()

        }
        listAdapter = ParticipantAddListAdapter(editClickListener, deleteClickListener)
        binding.rvParticipants.adapter = listAdapter
    }

    private fun setupParticipantDialog(inflater: LayoutInflater) {
        binding.floatingActionButton.setOnClickListener {
            hideKeyboardFrom(context!!, view!!)
            val persons =
                viewModel.persons.value!!.filter { person -> person.id != viewModel.personId }
                    .filter { person ->
                        !viewModel.participants.value!!.map { part -> part.personId }
                            .contains(person.id)
                    }
            buildParticipantDialog(inflater, null, null, persons)
        }
    }

    private fun buildParticipantDialog(
        inflater: LayoutInflater,
        person: Person?,
        role: String?,
        persons: List<Person>
    ) {
        val dialogLayout = inflater.inflate(R.layout.dialog_add_participant, null)
        val spinner = (dialogLayout.findViewById(R.id.spinner_friend) as Spinner)
        val editTextRole = (dialogLayout.findViewById(R.id.input_role) as EditText)
        val builder = AlertDialog.Builder(requireContext())
        if (role != null) {
            editTextRole.setText(role, TextView.BufferType.EDITABLE)
        }
        var newParticipant: Person? = null
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //moet van ergens anders zoeken, lijst meegeven
                    val selectedPerson = persons[position]
                    newParticipant = selectedPerson
                }
            }
        spinner.adapter = spinnerAdapter
        if (person != null) {
            newParticipant = person
            val index = spinner.adapter.count - 1
            spinner.setSelection(index)
        }
        builder.setView(dialogLayout)
            .setPositiveButton(R.string.save, null)
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })
        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.colorPrimary))
            val yesButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            yesButton.setTextColor(resources.getColor(R.color.colorPrimary))
            yesButton.setOnClickListener {
                if (editTextRole.text.toString().trim() != "") {
                    if (person != null) {
                        val oldParticipant =
                            viewModel.participants.value!!.find { participant -> participant.personId == person.id }
                        viewModel.participants.value!!.remove(oldParticipant)
                        viewModel.participants.value!!.add(
                            Participant(
                                oldParticipant?.id,
                                editTextRole.text.toString(),
                                oldParticipant?.personId!!
                            )
                        )
                    } else {
                        val oldParticipantsList = viewModel.participants.value!!.toMutableList()
                        oldParticipantsList.add(
                            Participant(
                                null,
                                editTextRole.text.toString(),
                                newParticipant!!.id!!
                            )
                        )
                        viewModel.participants.postValue(oldParticipantsList)
                    }
                    viewModel.participants.notifyObserver()
                    dialog.dismiss()
                    view!!.clearFocus();
                }
            }
        }
        dialog.show()
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
                val dateString = String.format(
                    "%d-%s%d-%s%d",
                    newYear,
                    if (newMonth + 1 < 10) "0" else "",
                    newMonth + 1,
                    if (newDay < 10) "0" else "",
                    newDay
                )
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
                val dateString = String.format(
                    "%d-%s%d-%s%d",
                    newYear,
                    if (newMonth + 1 < 10) "0" else "",
                    newMonth + 1,
                    if (newDay < 10) "0" else "",
                    newDay
                )
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

        val startTimeListener = TimePickerDialog.OnTimeSetListener() { _, newHour, newMinute ->
            val timeString =
                String.format(
                    "%s%d:%s%d",
                    if (newHour < 10) "0" else "",
                    newHour,
                    if (newMinute < 10) "0" else "",
                    newMinute
                )
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

        val endTimeListener = TimePickerDialog.OnTimeSetListener() { _, newHour, newMinute ->
            val timeString =
                String.format(
                    "%s%d:%s%d",
                    if (newHour < 10) "0" else "",
                    newHour,
                    if (newMinute < 10) "0" else "",
                    newMinute
                )
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

        binding.rvParticipants
    }

    private fun setupNavigation() {
        val backButtonCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController()
                        .navigate(ActivityAddFragmentDirections.actionActivityAddFragmentToActivityListFragment())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, backButtonCallback)
    }

    override fun onStart() {
        super.onStart()

        viewModel.activityToEdit?.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.activity = it.asDomainModel()
                viewModel.participants.value = it.participants as MutableList<Participant>
                listAdapter.submitList(it.participants.sortedBy { participant -> participant.role })
            }
        })

        viewModel.participants.observe(viewLifecycleOwner, Observer {
            it?.let {
                val tempList = mutableListOf<Participant>()
                tempList.addAll(it)
                listAdapter.submitList(tempList.sortedBy { participant -> participant.role })
                viewModel.persons.value?.let {
                    val possibleParticipantNames =
                        viewModel.persons.value!!.filter { person -> person.id != viewModel.personId }
                            .filter { person ->
                                !tempList.map { part -> part.personId }.contains(person.id)
                            }
                            .map { person -> person.name }
                    spinnerAdapter.clear()
                    spinnerAdapter.addAll(possibleParticipantNames)
                    manageFloatingButton(possibleParticipantNames)
                }
            }
        })

        viewModel.persons.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.persons = it
                spinnerAdapter.clear()
                val participantIds =
                    viewModel.participants.value!!.map { person -> person.personId }
                val possibleParticipantNames =
                    it.filter { person -> person.id != viewModel.personId }
                        .filter { person -> !participantIds.contains(person.id) }
                        .map { person -> person.name }
                spinnerAdapter.clear()
                spinnerAdapter.addAll(possibleParticipantNames)
                manageFloatingButton(possibleParticipantNames)
            }
        })
    }

    private fun manageFloatingButton(possibleParticipantNames: List<String>) {
        if (possibleParticipantNames.isNotEmpty()) {
            binding.floatingActionButton.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            binding.floatingActionButton.isEnabled = true
            binding.floatingActionButton.isClickable = true
        } else {
            binding.floatingActionButton.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.grey))
            binding.floatingActionButton.isEnabled = false
            binding.floatingActionButton.isClickable = false
        }
    }

    private fun updateActivity(): Boolean {
        return viewModel.updateActivity(
            args.activityId!!,
            input_name.text.toString(),
            input_description.text.toString(),
            input_location.text.toString(),
            textView_start_date.text.toString(),
            textView_start_time.text.toString(),
            textView_end_date.text.toString(),
            textView_end_time.text.toString()
        )
    }

    private fun createActivity(): Boolean {
        return viewModel.createActivity(
            input_name.text.toString(),
            input_description.text.toString(),
            input_location.text.toString(),
            textView_start_date.text.toString(),
            textView_start_time.text.toString(),
            textView_end_date.text.toString(),
            textView_end_time.text.toString()
        )
    }

    private fun checkDatesAfterToday(): Boolean {
        val c = Calendar.getInstance()
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[Calendar.MILLISECOND] = 0
        val today = c.time
        val datesAfterToday = stringToDateTime(
            binding.textViewStartDate.text.toString(),
            binding.textViewStartTime.text.toString()
        ).after(today) && stringToDateTime(
            binding.textViewEndDate.text.toString(),
            binding.textViewEndTime.text.toString()
        ).after(today)

        if (!datesAfterToday) {
            binding.textviewError.text =
                resources.getString(R.string.textview_dates_after_today_label)
        }
        return datesAfterToday
    }

    private fun checkInputsValid(): Boolean {
        val datesValid = stringToDateTime(
            binding.textViewStartDate.text.toString(),
            binding.textViewStartTime.text.toString()
        ).before(
            stringToDateTime(
                binding.textViewEndDate.text.toString(),
                binding.textViewEndTime.text.toString()
            )
        )

        val participantsAdded = viewModel.participants.value!!.count() > 0

        if (!datesValid) {
            binding.textviewError.text =
                resources.getString(R.string.textview_start_date_before_end_date_label)
        }
        if (!participantsAdded) {
            binding.textviewError.text =
                resources.getString(R.string.textview_participant_not_empty)
        }
        return datesValid && participantsAdded
    }

    private fun stringToDateTime(dateString: String, timeString: String): Date {
        var c = Calendar.getInstance()
        val datePieces = dateString.split("-").map { piece -> piece.toInt() }.toList()
        val timePieces = timeString.split(":").map { piece -> piece.toInt() }.toList()
        c.set(datePieces[0], datePieces[1] - 1, datePieces[2], timePieces[0], timePieces[1])
        return c.time
    }

    private fun checkAllInputsFilledIn(): Boolean {
        val filledIn = binding.inputName.text.toString() != "" &&
                binding.inputLocation.text.toString() != "" &&
                binding.inputDescription.text.toString() != "" &&
                binding.textViewStartDate.text.toString() != resources.getString(R.string.textview_select_date_label) &&
                binding.textViewStartTime.text.toString() != resources.getString(R.string.textview_select_time_label) &&
                binding.textViewEndDate.text.toString() != resources.getString(R.string.textview_select_date_label) &&
                binding.textViewEndTime.text.toString() != resources.getString(R.string.textview_select_time_label)
        if (!filledIn) {
            binding.textviewError.text =
                resources.getString(R.string.textview_every_field_must_be_filled_in_label)
        }
        return filledIn

    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}