package com.example.vinyl


import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.Matchers.allOf

@LargeTest
@RunWith(AndroidJUnit4::class)
class AlbumsScreenInstrumentedTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private fun goToAlbumsScreen() {
        // Search the Albums Option in the NavBar and click it
        onView(
            allOf(
                withId(R.id.navigation_albums), withContentDescription(R.string.title_albums),
                childAtPosition(childAtPosition(withId(R.id.nav_view), 0), 1), isDisplayed()
            )
        ).perform(click())
        // Wait a moment until the Fragment is fully presented
        Thread.sleep(2000)
    }

    @Test
    fun testAlbumScreenTitle() {
        goToAlbumsScreen()
        // Check whether the Title of the Fragment is present and has the text Albums (coming from the strings.xml file)
        onView(
            allOf(
                withId(R.id.textViewAlbumsTitle), withText(R.string.title_albums),
                withParent(withParent(withId(R.id.nav_host_fragment_activity_main))),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))

        // Check whether the Title of the Activity has the text Albums (coming from the strings.xml file)
        onView(allOf(
            withText(R.string.title_albums),
            withParent(allOf(withId(R.id.action_bar), withParent(withId(R.id.action_bar_container)))),
            isDisplayed())
        ).check(matches(isDisplayed()))
    }


    @Test
    fun testAlbumScreenNewAlbumCard() {
        goToAlbumsScreen()
        // Check whether the Input Field with text "Search" is displayed
        onView(
            allOf(
                withId(R.id.txtAlbumsFilter),
                withParent(withParent(withId(R.id.outlinedTextFieldAlbums))),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))
            .check(matches(withHint(R.string.search)))

        // Check whether the card is displaying the invitation to add a new album text
        onView(withId(R.id.add_new_album_card_legend))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.add_new_album_legend)))

        // Check whether the card is displaying the invitation to add a new album button
        onView(withId(R.id.add_new_album_card_btn))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.add_new_album)))
    }

    @Test
    fun testAlbumScreenAlbumsList() {
        goToAlbumsScreen()
        Thread.sleep(4000)
        // Check whether the list is rendered
        onView(withId(R.id.albums_rv))
            .check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
