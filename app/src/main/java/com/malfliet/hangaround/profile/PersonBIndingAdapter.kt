package com.malfliet.hangaround.profile

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.malfliet.hangaround.domain.Person

@BindingAdapter("personEmailString")
fun TextView.setPersonEmail(person: Person?) {
    person?.let {
        text = person.email
    }
}

@BindingAdapter("personNameString")
fun TextView.setPersonName(person: Person?) {
    person?.let {
        text = person.name
    }
}

@BindingAdapter("personNameInputString")
fun EditText.setActivityName(person: Person?) {
    person?.let {
        this.setText(person.name)
    }
}
