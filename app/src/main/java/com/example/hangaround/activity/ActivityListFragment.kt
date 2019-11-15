package com.example.hangaround.activity


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.hangaround.R
import com.example.hangaround.databinding.FragmentActivityListBinding

/**
 * A simple [Fragment] subclass.
 */
class ActivityListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val binding: FragmentActivityListBinding = DataBindingUtil.inflate( inflater, R.layout.fragment_activity_list, container, false)

        return binding.root
    }


}
