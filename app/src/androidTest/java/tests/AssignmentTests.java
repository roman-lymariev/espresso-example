package tests;

import android.content.pm.ActivityInfo;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.common.collect.Ordering;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.com.mobileassignment.views.MainActivity;
import pages.MainPage;
import pages.MapPage;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.StringStartsWith.startsWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AssignmentTests {
    MainPage mainPage = new MainPage();
    MapPage mapPage = new MapPage();

    private static final List<String> SEARCH_STRINGS = Arrays.asList(
            "odes", "Al A", "Yorkshire, US", "A Coruna, ES", "Aba, HU"
    );

    @Rule
    public ActivityTestRule<MainActivity> atRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void waitUntilAppIsLoaded() {
        mainPage.waitUntilViewIslLoaded(atRule);
    }

    @Test
    public void tc0_appCanBeOpened() {
        mainPage.isOpened();
    }

    @Test
    public void tc1_checkInitialCityListSorting_NameOnly() {
        assertThat("Default city list is ordered by name",
                Ordering.natural().isOrdered(mainPage.getCurrentCitiesList_nameOnly(atRule)),
                is(equalTo(true))
        );
    }

    @Test
    public void tc1_checkInitialCityListSorting_DisplayedText() {
        assertThat("Default city list is ordered by name and country",
                Ordering.natural().isOrdered(mainPage.getCurrentCitiesList_nameAndCountry(atRule)),
                is(equalTo(true))
        );
    }

    @Test
    public void tc2_checkSearchResultsSorting() {
        mainPage.typeIntoSearch(SEARCH_STRINGS.get(0));

        assertThat("Search results list is ordered by name",
                Ordering.natural().isOrdered(mainPage.getCurrentCitiesList_nameOnly(atRule)),
                is(equalTo(true))
        );
    }

    @Test
    public void tc3_checkDefaultSortingAfterClearingSearch() {
        String searchString = SEARCH_STRINGS.get(1);

        mainPage.typeIntoSearch(searchString)
                .closeKeyboard()
                .clearSearch();

        assertThat("Search results list is ordered by name",
                Ordering.natural().isOrdered(mainPage.getCurrentCitiesList_nameOnly(atRule)),
                is(equalTo(true))
        );
    }

    @Test
    public void tc4_checkCityListIsScrollable() {
        String searchString = SEARCH_STRINGS.get(4);

        assertFalse("City row should not be displayed yet: " + searchString,
                mainPage.isCityDisplayedInList(searchString));
        mainPage.scrollListDown();
        assertTrue("City row should be scrolled into view: " + searchString,
                mainPage.isCityDisplayedInList(searchString));
    }

    @Test
    public void tc5_checkFilteredListIsScrollable() {
        String city = SEARCH_STRINGS.get(2);

        mainPage.typeIntoSearch(city.substring(0, 4))
                .closeKeyboard();

        assertFalse("City row should not be displayed yet: " + city,
                mainPage.isCityDisplayedInList(city));

        mainPage.scrollListDown();
        assertTrue("City row should be scrolled into view: " + city,
                mainPage.isCityDisplayedInList(city));
    }

    @Test
    public void tc6_checkDynamicFiltering() {
        String searchString = SEARCH_STRINGS.get(2);
        String firstPart = searchString.substring(0, 3);

        mainPage.typeIntoSearch(firstPart)
                .closeKeyboard()
                .waitForSearchResults(atRule);

        assertThat("All displayed cities start with " + firstPart,
                mainPage.getCurrentCitiesList_nameAndCountry(atRule), everyItem(startsWith(firstPart))
        );

        mainPage.typeIntoSearch(searchString.substring(4))
                .closeKeyboard()
                .waitForSearchResults(atRule);

        assertThat("All displayed cities start with " + searchString,
                mainPage.getCurrentCitiesList_nameAndCountry(atRule), everyItem(startsWith(searchString))
        );
    }

    @Test
    public void tc8_checkMapCanBeOpened() {
        String city = SEARCH_STRINGS.get(3);

        mainPage.tapCityToOpen(city);
        mapPage.isOpened();
    }

    @Test
    public void tc11_12_checkBackButtonNavigation() {
        tc8_checkMapCanBeOpened();

        //go from Map to Main
        Espresso.pressBack();
        mainPage.isOpened();

        //close the app
        Espresso.pressBackUnconditionally();
        assertTrue(atRule.getActivity().isDestroyed());
    }

    @Test
    public void tc13_checkDeviceOrientationChange_Map() {
        tc8_checkMapCanBeOpened();

        atRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mapPage.isOpened();
    }

    @Test
    public void tc13_checkDeviceOrientationChange_Main() {
        atRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mainPage.isOpened();
    }

    @After
    public void finishActivity() {
        if (atRule.getActivity() != null) {
            atRule.getActivity().finish();
        }
    }
}
