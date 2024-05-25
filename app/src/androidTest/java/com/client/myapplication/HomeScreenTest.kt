package com.client.myapplication

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.client.myapplication.domain.model.RateDto
import com.client.myapplication.ui.screens.HomeScreen
import com.client.myapplication.ui.screens.MainViewState
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun circularProgressIndicatorExits_whenScreenIsLoading() {
        composeTestRule.setContent {
            HomeScreen(
                rates = MainViewState.Loading
            )
        }
        composeTestRule.onNodeWithTag("loading")
            .assertExists()
    }

    @Test
    fun listsExist_whenScreenIsLoaded() {
        composeTestRule.setContent {
            HomeScreen(
                rates = MainViewState.Success(
                    plnRates = listOf(
                        RateDto("USD", 0.25),
                        RateDto("EUR", 0.22),
                        RateDto("GBP", 0.19)
                    ),
                    euroRates = listOf(
                        RateDto("USD", 1.15),
                        RateDto("PLN", 4.55),
                        RateDto("GBP", 0.87)
                    )
                )
            )
        }
        composeTestRule.onNodeWithTag("list1").assertExists()
        composeTestRule.onNodeWithTag("list2").assertExists()
    }

    @Test
    fun errorViewExists_whenErrorState() {
        composeTestRule.setContent {
            HomeScreen(
                rates = MainViewState.Error("Error message")
            )
        }
        composeTestRule.onNodeWithTag("error")
            .assertExists()
    }
}