package com.example.hangaround.activity

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.hangaround.domain.Activity
import com.example.hangaround.domain.Participant

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