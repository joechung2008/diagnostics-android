package com.github.joechung2008.diagnostics_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.github.joechung2008.diagnostics_android.ui.MainViewModel
import kotlinx.coroutines.launch

class ServerInfoFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_server_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deploymentIdText = view.findViewById<TextView>(R.id.deployment_id_text)
        val hostnameText = view.findViewById<TextView>(R.id.hostname_text)
        val nodeVersionsLabel = view.findViewById<TextView>(R.id.node_versions_label)
        val nodeVersionsText = view.findViewById<TextView>(R.id.node_versions_text)
        val serverIdText = view.findViewById<TextView>(R.id.server_id_text)
        val uptimeLabel = view.findViewById<TextView>(R.id.uptime_label)
        val uptimeText = view.findViewById<TextView>(R.id.uptime_text)
        val totalSyncAllCountText = view.findViewById<TextView>(R.id.total_sync_all_count_text)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.diagnostics.collect { diagnostics ->
                diagnostics?.let {
                    val serverInfo = it.serverInfo
                    deploymentIdText.text = serverInfo.deploymentId
                    hostnameText.text = serverInfo.hostname
                    serverIdText.text = serverInfo.serverId
                    totalSyncAllCountText.text = serverInfo.extensionSync.totalSyncAllCount.toString()

                    serverInfo.nodeVersions.let { nodeVersions: String? ->
                        val isVisible = nodeVersions != null
                        nodeVersionsLabel.isVisible = isVisible
                        nodeVersionsText.isVisible = isVisible
                        nodeVersionsText.text = nodeVersions
                    }

                    serverInfo.uptime.let { uptime: Long? ->
                        val isVisible = uptime != null
                        uptimeLabel.isVisible = isVisible
                        uptimeText.isVisible = isVisible
                        uptimeText.text = uptime?.toString()
                    }
                }
            }
        }
    }
}