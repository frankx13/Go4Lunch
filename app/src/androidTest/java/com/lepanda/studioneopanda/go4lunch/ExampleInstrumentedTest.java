package com.lepanda.studioneopanda.go4lunch;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.lepanda.studioneopanda.go4lunch", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<CentralActivity> mActivityRule = new ActivityTestRule<>(CentralActivity.class);

    @Before
    public void setUp() {
        CentralActivity mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @Test
    public void swipePage() { // we check that the swipe on the screen is working
        onView(withId(R.id.viewpager_id))
                .check(matches(isDisplayed()));

        onView(withId(R.id.viewpager_id))
                .perform(swipeLeft());
    }

    @Test
    public void clickPage() { // we check that the click on a tab is working
        onView(withId(R.id.viewpager_id))
                .check(matches(isDisplayed()));

        onView(withId(R.id.viewpager_id))
                .perform(click());
    }
}
