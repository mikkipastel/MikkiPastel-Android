package com.mikkipastel.blog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mikkipastel.blog.R

class AboutAppFragment : DialogFragment() {

    companion object {
        fun newInstance() = AboutAppFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_aboutapp, container, false)
    }
}
