package com.example.thecoffeeapp.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thecoffeeapp.data.Recipe

class RecipesViewModel : ViewModel() {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _selectedRecipe = MutableLiveData<Recipe>()
    val selectedRecipe: LiveData<Recipe> = _selectedRecipe

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        // In a real app, this would come from a repository
        _recipes.value = Recipe.getSampleRecipes()
    }

    fun filterRecipes(type: Recipe.RecipeType? = null) {
        val allRecipes = Recipe.getSampleRecipes()
        _recipes.value = when (type) {
            null -> allRecipes
            else -> allRecipes.filter { it.type == type }
        }
    }

    fun selectRecipe(recipe: Recipe) {
        _selectedRecipe.value = recipe
    }
}
