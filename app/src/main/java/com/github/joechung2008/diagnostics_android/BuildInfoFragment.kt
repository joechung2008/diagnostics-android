package com.github.joechung2008.diagnostics_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.github.joechung2008.diagnostics_android.ui.MainViewModel
import kotlinx.coroutines.launch

class BuildInfoFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_build_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buildVersionTextView = view.findViewById<TextView>(R.id.build_version_text)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.diagnostics.collect { diagnostics ->
                diagnostics?.let {
                    buildVersionTextView.text = it.buildInfo.buildVersion
                }
            }
        }
    }
}