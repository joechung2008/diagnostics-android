package com.github.joechung2008.diagnostics_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class KeyValueFragment : Fragment() {

    private var data: Map<String, String> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val keys = arguments?.getStringArrayList(ARG_KEYS)
        val values = arguments?.getStringArrayList(ARG_VALUES)
        if (!keys.isNullOrEmpty() && values != null && keys.size == values.size) {
            val map = keys.indices.associate { i -> keys[i] to values[i] }
            data = map
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_key_value, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.key_value_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = KeyValueAdapter(data)
    }

    companion object {
        private const val ARG_KEYS = "keys"
        private const val ARG_VALUES = "values"

        fun newInstance(data: Map<String, String>) =
            KeyValueFragment().apply {
                arguments = Bundle().apply {
                    val keys = ArrayList<String>(data.keys)
                    val values = ArrayList<String>(data.values)
                    putStringArrayList(ARG_KEYS, keys)
                    putStringArrayList(ARG_VALUES, values)
                }
            }
    }
}
