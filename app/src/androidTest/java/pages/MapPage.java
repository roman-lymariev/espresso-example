package pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

public class MapPage {
    private static final String CONTENT_DESCR = "Google Map";

    public void isOpened() {
        onView(withContentDescription(CONTENT_DESCR)).check(matches(isDisplayed()));
    }
}
