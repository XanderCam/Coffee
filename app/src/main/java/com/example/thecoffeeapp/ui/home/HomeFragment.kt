package com.example.thecoffeeapp.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.thecoffeeapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackgroundImage()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupBackgroundImage() {
        // Load background image with proper error handling
        Glide.with(requireContext())
            .load("https://creator.nightcafe.studio/creation/iZK3qbeqDEaVQ4TVl3j6/a-warm-cup-of-coffee-with-visible-hot-steam-hot-creamy-bubbles-on-the-surface-of-the-coffee-bokeh-ba")
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(android.R.drawable.ic_menu_gallery)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    e?.printStackTrace()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(binding.backgroundImage)
    }

    private fun setupClickListeners() {
        binding.addCupButton.setOnClickListener {
            viewModel.addCupOfCoffee()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe today's count
                viewModel.todaysCupCount.observe(viewLifecycleOwner) { count ->
                    binding.todayCount.text = count.toString()
                }

                // Observe yesterday's count
                viewModel.yesterdaysCupCount.observe(viewLifecycleOwner) { count ->
                    binding.yesterdayCount.text = count.toString()
                }

                // Observe daily quote
                viewModel.dailyQuote.observe(viewLifecycleOwner) { quote ->
                    binding.dailyQuote.text = quote
                }

                // Observe taunt messages
                viewModel.showTaunt.observe(viewLifecycleOwner) { taunt ->
                    snackbar?.dismiss()
                    taunt?.let {
                        snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                            .setAction("Dismiss") { viewModel.clearTaunt() }
                        snackbar?.show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        snackbar?.dismiss()
        snackbar = null
        _binding = null
        super.onDestroyView()
    }
}
