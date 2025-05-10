package com.example.thecoffeeapp.data

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val type: RecipeType
) {
    enum class RecipeType {
        DRINK,
        SNACK
    }

    companion object {
        fun getSampleRecipes(): List<Recipe> = listOf(
            Recipe(
                1,
                "Classic Cappuccino",
                "A perfect blend of espresso, steamed milk, and milk foam",
                "https://images.pexels.com/photos/312418/pexels-photo-312418.jpeg",
                listOf(
                    "1 shot espresso",
                    "4 oz steamed milk",
                    "2 oz milk foam"
                ),
                listOf(
                    "Pull a shot of espresso",
                    "Steam and froth the milk",
                    "Pour steamed milk over espresso",
                    "Top with milk foam"
                ),
                RecipeType.DRINK
            ),
            Recipe(
                2,
                "Coffee Chocolate Cookies",
                "Delicious cookies with a coffee twist",
                "https://images.pexels.com/photos/230325/pexels-photo-230325.jpeg",
                listOf(
                    "2 cups flour",
                    "1/2 cup cocoa powder",
                    "2 tbsp instant coffee",
                    "1 cup butter",
                    "1 cup sugar",
                    "2 eggs"
                ),
                listOf(
                    "Mix dry ingredients",
                    "Cream butter and sugar",
                    "Combine all ingredients",
                    "Bake at 350°F for 12 minutes"
                ),
                RecipeType.SNACK
            ),
            Recipe(
                3,
                "Iced Caramel Latte",
                "Sweet and refreshing iced coffee drink",
                "https://images.pexels.com/photos/1193335/pexels-photo-1193335.jpeg",
                listOf(
                    "2 shots espresso",
                    "1 cup cold milk",
                    "2 tbsp caramel syrup",
                    "Ice cubes"
                ),
                listOf(
                    "Brew espresso shots",
                    "Let espresso cool",
                    "Fill glass with ice",
                    "Add caramel syrup",
                    "Pour in espresso and milk"
                ),
                RecipeType.DRINK
            )
        )
    }
}
