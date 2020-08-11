package com.malfliet.hangaround.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.malfliet.hangaround.MainActivity
import com.malfliet.hangaround.R
import com.malfliet.hangaround.activity.detail.ActivityDetailFragmentDirections
import com.malfliet.hangaround.databinding.FragmentProfileBinding
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.login.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewmodel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        val application = requireNotNull(activity).application
        viewmodel = ViewModelProviders.of(this, ProfileViewModel.Factory(application))
            .get(ProfileViewModel::class.java)

        setHasOptionsMenu(true)

        binding.buttonEditName.setOnClickListener {
            hideKeyboardFrom(context!!, view!!)
            val person =
                viewmodel.persons.value!!.find { person -> person.id == viewmodel.personId }!!
            person.name = binding.editTextName.text.toString()
            viewmodel.updatePerson(person)
        }

        val actionbar = (requireActivity() as MainActivity).supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(false)
        actionbar.title = "Profile"

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        viewmodel.getPerson(sharedPreferences.getString("personId", "")!!)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.logout) {
            viewmodel.logout()

            val loginActivity = Intent(activity, LoginActivity::class.java)
            loginActivity.putExtra("CLEAR_CREDENTIALS", true)
            startActivity(loginActivity)

            (activity as MainActivity).finish()
            true
        } else {
            false
        }
    }

    override fun onStart() {
        super.onStart()

        viewmodel.persons.observe(this, Observer {
            it?.let {
                binding.person = it.find { person -> person.id == viewmodel.personId }
            }
        })
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}