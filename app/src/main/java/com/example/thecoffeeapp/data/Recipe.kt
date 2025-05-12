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
            // Drinks
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
                "Cold Brew Coffee",
                "Smooth and less acidic cold brewed coffee",
                "https://images.pexels.com/photos/2615323/pexels-photo-2615323.jpeg",
                listOf(
                    "1 cup coarse ground coffee",
                    "4 cups cold water",
                    "Optional: milk or cream"
                ),
                listOf(
                    "Combine coffee and water",
                    "Steep for 12-24 hours in fridge",
                    "Strain through coffee filter",
                    "Serve over ice"
                ),
                RecipeType.DRINK
            ),
            Recipe(
                3,
                "Mocha Frappuccino",
                "Blended coffee drink with chocolate",
                "https://images.pexels.com/photos/1193335/pexels-photo-1193335.jpeg",
                listOf(
                    "2 shots espresso",
                    "1 cup milk",
                    "3 tbsp chocolate syrup",
                    "2 cups ice",
                    "Whipped cream"
                ),
                listOf(
                    "Blend espresso, milk, chocolate, and ice",
                    "Pour into glass",
                    "Top with whipped cream",
                    "Drizzle with chocolate"
                ),
                RecipeType.DRINK
            ),
            // Snacks
            Recipe(
                4,
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
                5,
                "Tiramisu",
                "Classic Italian coffee-flavored dessert",
                "https://images.pexels.com/photos/6341882/pexels-photo-6341882.jpeg",
                listOf(
                    "Ladyfingers",
                    "Strong brewed coffee",
                    "Mascarpone cheese",
                    "Heavy cream",
                    "Sugar",
                    "Cocoa powder"
                ),
                listOf(
                    "Brew coffee and let cool",
                    "Mix mascarpone and cream",
                    "Dip ladyfingers in coffee",
                    "Layer with cream mixture",
                    "Dust with cocoa powder"
                ),
                RecipeType.SNACK
            ),
            Recipe(
                6,
                "Coffee Brownies",
                "Rich chocolate brownies with coffee kick",
                "https://images.pexels.com/photos/45202/brownies-chocolate-dessert-sweet-45202.jpeg",
                listOf(
                    "2 cups sugar",
                    "1 cup butter",
                    "4 eggs",
                    "2 tsp vanilla",
                    "1 cup cocoa",
                    "1 cup flour",
                    "2 tbsp instant coffee"
                ),
                listOf(
                    "Mix wet ingredients",
                    "Combine dry ingredients",
                    "Mix all together",
                    "Bake at 350°F for 30-35 minutes"
                ),
                RecipeType.SNACK
            )
        )
    }
}
