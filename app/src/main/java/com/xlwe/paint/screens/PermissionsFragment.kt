package com.xlwe.paint.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xlwe.paint.core.OpeningAppSettings
import com.xlwe.paint.core.PermissionRequest
import com.xlwe.paint.databinding.FragmentPermissionsBinding

class PermissionsFragment : BaseFragment() {
    private var _binding: FragmentPermissionsBinding? = null
    private val binding: FragmentPermissionsBinding
        get() = _binding!!

    private lateinit var openingAppSettings: OpeningAppSettings
    private lateinit var permissionRequest: PermissionRequest
    private var screenSettings = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is PermissionRequest)
            permissionRequest = context

        if (context is OpeningAppSettings)
            openingAppSettings = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun parseParams() {
        val args = requireArguments()
        screenSettings = args.getString(SCREEN_SETTINGS) ?: ""

        if (isSettingsScreen(SETTINGS_CANCEL_START)) {
            binding.text.visibility = View.VISIBLE
            binding.btnPermission.visibility = View.VISIBLE

            binding.btnPermission.setOnClickListener {
                permissionRequest.request()
            }
        }

        if (isSettingsScreen(SETTINGS_FULL_CANCEL_START)) {
            binding.text.visibility = View.VISIBLE
            binding.btnPermission.visibility = View.VISIBLE

            binding.text.text = "Вы навсегда отклонили разрешения, без которых я не могу работать"
            binding.btnPermission.text = "Открыть настройки"
            binding.btnPermission.setOnClickListener {
                openingAppSettings.openSettings()
            }
        }
    }

    override fun isSettingsScreen(screen: String) = screenSettings == screen

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val SCREEN_SETTINGS = "screen_settings"
        private const val SETTINGS_CANCEL_START = "screen_cancel_start"
        private const val SETTINGS_FULL_CANCEL_START = "screen_full_cancel_start"

        fun newInstanceCancelStart() =
            PermissionsFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_SETTINGS, SETTINGS_CANCEL_START)
                }
            }

        fun newInstanceFullCancelStart() =
            PermissionsFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_SETTINGS, SETTINGS_FULL_CANCEL_START)
                }
            }
    }
}