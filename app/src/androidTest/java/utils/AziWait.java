package utils;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;

import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.azimolabs.conditionwatcher.Instruction;


public class AziWait {
    private static int DEFAULT_TIMEOUT = 3000;
    private static int DEFAULT_WATCH_INTERVAL = 500;

    public static void waitUntilVisible(View view) {
        waitForViewCondition(view, View.VISIBLE);
    }

    public static void waitUntilInvisible(View view) {
        waitForViewCondition(view, View.INVISIBLE);
    }

    public static void waitUntilGone(View view) {
        waitForViewCondition(view, View.GONE);
    }

    private static void waitForViewCondition(final View view, final int expectedViewCondition) {
        try {
            ConditionWatcher.setTimeoutLimit(DEFAULT_TIMEOUT);
            ConditionWatcher.setWatchInterval(DEFAULT_WATCH_INTERVAL);
            ConditionWatcher.waitForCondition(new Instruction() {
                                                  @Override
                                                  public String getDescription() {
                                                      return String.format("Wait until view %s visibility is %s",
                                                              view.toString(),
                                                              expectedViewCondition
                                                      );
                                                  }

                                                  @Override
                                                  public boolean checkCondition() {
                                                      try {
                                                          return (view.getVisibility() == expectedViewCondition);
                                                      } catch (NoMatchingViewException ex) {
                                                          return false;
                                                      }
                                                  }
                                              }
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void waitForCondition(final ViewInteraction interaction, final ViewAssertion assertion) {
        try {
            ConditionWatcher.setTimeoutLimit(DEFAULT_TIMEOUT);
            ConditionWatcher.setWatchInterval(DEFAULT_WATCH_INTERVAL);
            ConditionWatcher.waitForCondition(new Instruction() {
                                                  @Override
                                                  public String getDescription() {
                                                      return String.format("Wait until ViewInteraction %s is %s",
                                                              interaction.toString(),
                                                              assertion.toString()
                                                      );
                                                  }

                                                  @Override
                                                  public boolean checkCondition() {
                                                      try {
                                                          interaction.check(assertion);
                                                          return true;
                                                      } catch (Exception ex) {
                                                          return false;
                                                      }
                                                  }
                                              }
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
