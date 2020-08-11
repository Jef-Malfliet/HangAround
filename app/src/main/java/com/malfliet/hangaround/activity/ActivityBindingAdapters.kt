package com.malfliet.hangaround.activity

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.malfliet.hangaround.domain.Activity
import com.malfliet.hangaround.domain.Participant

@BindingAdapter("activityNameString")
fun TextView.setActivityName(activity: Activity?) {
    activity?.let {
        text = activity.name
    }
}

@BindingAdapter("activityStartDateString")
fun TextView.setActivityStartDate(activity: Activity?) {
    activity?.let {
        val pieces = activity.startDate.split("T")
        text = String.format(
            "%s at %s",
            pieces[0],
            pieces[1].removeRange(pieces[1].length - 8, pieces[1].length)
        )
    }
}

@BindingAdapter("activityEndDateString")
fun TextView.setActivityEndDate(activity: Activity?) {
    activity?.let {
        val pieces = activity.endDate.split("T")
        text = String.format(
            "%s at %s",
            pieces[0],
            pieces[1].removeRange(pieces[1].length - 8, pieces[1].length)
        )
    }
}

@BindingAdapter("activityDescriptionString")
fun TextView.setActivityDescription(activity: Activity?) {
    activity?.let {
        text = activity.description
    }
}

@BindingAdapter("activityPlaceString")
fun TextView.setActivityPlace(activity: Activity?) {
    activity?.let {
        text = activity.place
    }
}

@BindingAdapter("participantRoleString")
fun TextView.setParticipantRole(participant: Participant?) {
    participant?.let {
        text = participant.role
    }
}

@BindingAdapter("activityNameInputString")
fun EditText.setActivityName(activity: Activity?) {
    activity?.let {
        this.setText(activity.name)
    }
}

@BindingAdapter("activityDescriptionInputString")
fun EditText.setActivityDescription(activity: Activity?) {
    activity?.let {
        this.setText(activity.description)
    }
}

@BindingAdapter("activityLocationInputString")
fun EditText.setActivityLocation(activity: Activity?) {
    activity?.let {
        this.setText(activity.place)
    }
}

@BindingAdapter("activityStartDateInputString")
fun TextView.setActivityStartDateInput(activity: Activity?) {
    activity?.let {
        val pieces = activity.startDate.split("T")
        text = pieces[0]
    }
}

@BindingAdapter("activityStartTimeInputString")
fun TextView.setActivityStartTimeInput(activity: Activity?) {
    activity?.let {
        val pieces = activity.startDate.split("T")
        text = pieces[1].removeRange(pieces[1].length - 8, pieces[1].length)
    }
}

@BindingAdapter("activityEndDateInputString")
fun TextView.setActivityEndDateInput(activity: Activity?) {
    activity?.let {
        val pieces = activity.endDate.split("T")
        text = pieces[0]
    }
}

@BindingAdapter("activityEndTimeInputString")
fun TextView.setActivityEndTimeInput(activity: Activity?) {
    activity?.let {
        val pieces = activity.endDate.split("T")
        text = pieces[1].removeRange(pieces[1].length - 8, pieces[1].length)
    }
}
