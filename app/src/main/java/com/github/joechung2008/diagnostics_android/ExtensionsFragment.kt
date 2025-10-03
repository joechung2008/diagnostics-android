package com.github.joechung2008.diagnostics_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.joechung2008.diagnostics_android.model.Extension
import com.github.joechung2008.diagnostics_android.model.ExtensionError
import com.github.joechung2008.diagnostics_android.model.ExtensionInfo
import com.github.joechung2008.diagnostics_android.ui.ExtensionsAdapter
import com.github.joechung2008.diagnostics_android.ui.MainViewModel
import kotlinx.coroutines.launch

class ExtensionsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_extensions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.extensions_recycler_view)
        val adapter = ExtensionsAdapter { extension ->
            navigateToDetail(extension)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.diagnostics.collect { diagnostics ->
                diagnostics?.let {
                    val sortedExtensions = it.extensions.values.sortedWith(compareBy(
                        { extension ->
                            when (extension) {
                                is ExtensionInfo -> 0
                                is ExtensionError -> 1
                                else -> 2
                            }
                        },
                        { extension ->
                            (extension as? ExtensionInfo)?.extensionName
                        }
                    ))
                    adapter.submitList(sortedExtensions)
                }
            }
        }
    }

    private fun navigateToDetail(extension: Extension) {
        val fragment = ExtensionDetailFragment.newInstance(extension)
        parentFragmentManager.beginTransaction()
            .replace(R.id.view_pager_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}