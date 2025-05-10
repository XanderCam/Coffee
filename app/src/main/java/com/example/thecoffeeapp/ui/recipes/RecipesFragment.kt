package com.example.thecoffeeapp.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecoffeeapp.data.Recipe
import com.example.thecoffeeapp.databinding.FragmentRecipesBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipesViewModel by viewModels()
    private var snackbar: Snackbar? = null
    private var recipesAdapter: RecipesAdapter? = null
    private var layoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupTabLayout()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(context)
        recipesAdapter = RecipesAdapter { recipe ->
            showRecipeDetails(recipe)
        }

        binding.recipesRecyclerView.apply {
            layoutManager = this@RecipesFragment.layoutManager
            adapter = recipesAdapter
            setHasFixedSize(true) // Optimization if items have fixed size
            
            // Add recycling optimization
            recycledViewPool.setMaxRecycledViews(0, 10)
            
            // Add scroll optimization
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        recyclerView.invalidateItemDecorations()
                    }
                }
            })
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.apply {
            addTab(newTab().setText("All"))
            addTab(newTab().setText("Drinks"))
            addTab(newTab().setText("Snacks"))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> viewModel.filterRecipes(null)
                        1 -> viewModel.filterRecipes(Recipe.RecipeType.DRINK)
                        2 -> viewModel.filterRecipes(Recipe.RecipeType.SNACK)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
                    recipesAdapter?.submitList(recipes)
                }
            }
        }
    }

    private fun showRecipeDetails(recipe: Recipe) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.root,
            "Selected: ${recipe.title}\nIngredients: ${recipe.ingredients.size}",
            Snackbar.LENGTH_LONG
        ).apply { show() }
    }

    override fun onDestroyView() {
        // Clean up resources
        snackbar?.dismiss()
        snackbar = null
        
        // Clear RecyclerView
        binding.recipesRecyclerView.adapter = null
        recipesAdapter = null
        layoutManager = null
        
        // Clear binding
        _binding = null
        
        super.onDestroyView()
    }
}
