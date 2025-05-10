package com.example.thecoffeeapp.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.thecoffeeapp.BuildConfig
import com.example.thecoffeeapp.databinding.FragmentSettingsBinding
import com.example.thecoffeeapp.util.NotificationHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var notificationHelper: NotificationHelper? = null
    private var snackbar: Snackbar? = null

    private val _notificationsEnabled = MutableStateFlow(false)
    private val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupNotifications()
        setupSupportButton()
        setupVersionInfo()
        observeSettings()
    }

    private fun setupNotifications() {
        notificationHelper = NotificationHelper(requireContext())
        
        binding.dailyQuoteSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (isChecked) {
                    requestNotificationPermission()
                } else {
                    _notificationsEnabled.value = false
                    notificationHelper?.cancelNotification()
                }
            }
        }
    }

    private fun setupSupportButton() {
        binding.buyMeCoffeeButton.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://www.buymeacoffee.com/thaxam")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            } catch (e: Exception) {
                showError("Couldn't open support link")
            }
        }
    }

    private fun setupVersionInfo() {
        binding.versionText.text = "Version ${BuildConfig.VERSION_NAME}"
    }

    private fun observeSettings() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                notificationsEnabled.collect { enabled ->
                    binding.dailyQuoteSwitch.isChecked = enabled
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        // In a real app, this would use the Android permission system
        // For now, just show a message
        _notificationsEnabled.value = true
        showMessage("Notification permission granted")
    }

    private fun showMessage(message: String) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).apply { show() }
    }

    private fun showError(error: String) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.root,
            error,
            Snackbar.LENGTH_LONG
        ).apply { show() }
    }

    override fun onDestroyView() {
        // Clean up resources
        snackbar?.dismiss()
        snackbar = null
        notificationHelper = null
        
        // Clear binding
        _binding = null
        
        super.onDestroyView()
    }
}
