package pages;

import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.test.espresso.AmbiguousViewMatcherException;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

import app.com.mobileassignment.R;
import app.com.mobileassignment.model.City;
import utils.AziWait;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static utils.AziWait.waitUntilGone;
import static utils.AziWait.waitUntilVisible;

public class MainPage {
    //--- progress bar ---
    public MainPage waitUntilViewIslLoaded(ActivityTestRule activityTestRule) {
        waitUntilGone(activityTestRule.getActivity().findViewById(R.id.progress_bar));
        return this;
    }

    //--- search ---
    public MainPage clearSearch() {
        onView(withId(R.id.search))
                .perform(clearText());
        return this;
    }

    public MainPage typeIntoSearch(String text) {
        onView(withId(R.id.search))
                .perform(typeText(text));
        return this;
    }

    public MainPage closeKeyboard() {
        onView(withId(R.id.search))
                .perform(closeSoftKeyboard());
        return this;
    }

    public MainPage waitForSearchResults(ActivityTestRule activityTestRule) {
        waitUntilVisible(activityTestRule.getActivity().findViewById(R.id.results));
        return this;
    }

    // --- cities list ---
    public List<String> getCurrentCitiesList_nameAndCountry(ActivityTestRule activityTestRule) {
        ListAdapter citiesAdapter = getCitiesListAdapter(activityTestRule);

        List<String> cities = new ArrayList<>();
        for (int i = 0; i < citiesAdapter.getCount(); i++) {
            City city = (City) citiesAdapter.getItem(i);
            cities.add(city.getName() + ", " + city.getCountry());
        }

        return cities;
    }

    public List<String> getCurrentCitiesList_nameOnly(ActivityTestRule activityTestRule) {
        ListAdapter citiesAdapter = getCitiesListAdapter(activityTestRule);

        List<String> cities = new ArrayList<>();
        for (int i = 0; i < citiesAdapter.getCount(); i++) {
            City city = (City) citiesAdapter.getItem(i);
            cities.add(city.getName());
        }

        return cities;
    }

    public void tapCityToOpen(String city) {
        onData(withCityNameCountry(city))
                .inAdapterView(withId(R.id.citiesList))
                .atPosition(0)
                .perform(click());
    }

    public static Matcher withCityNameCountry(final String cityName){
        return new TypeSafeMatcher<City>(){
            @Override
            public boolean matchesSafely(City city) {
                return cityName.compareTo(city.getName()+", "+city.getCountry()) == 0;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("Matches the cities contained in the list by name");
            }
        };
    }

    public MainPage scrollListDown() {
        onView(withId(R.id.results))
                .perform(swipeUp());
        return this;
    }

    public boolean isCityDisplayedInList(String city) {
        AziWait.waitForCondition(onView(withSubstring(city)), matches(isDisplayed()));
        try {
            onView(withSubstring(city)).check(matches(isDisplayed()));
            return true;
        } catch (AmbiguousViewMatcherException ex) {
            return true;
        } catch (NoMatchingViewException | AssertionError ex) {
            return false;
        }
    }

    private ListAdapter getCitiesListAdapter(ActivityTestRule activityTestRule) {
        return ((ListView) activityTestRule.getActivity().findViewById(R.id.citiesList)).getAdapter();
    }

    public void isOpened() {
        onView(withId(R.id.citiesList))
                .check(matches(isDisplayed()));
    }
}
