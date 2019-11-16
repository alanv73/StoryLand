package edu.southhills.storyland;

import android.content.res.Configuration;
import android.util.Log;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testPortraitMode() {
        TextView tvOrientation = mActivity.findViewById(R.id.tvOrientation);
        String actualOrientation = tvOrientation.getText().toString();
        if(mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Assert.assertEquals("Landscape Orientation", actualOrientation);
        } else {
            Assert.assertEquals("Portrait Orientation", actualOrientation);
        }
    }

    @Test
    public void testUserRatingPref() {
        String user = "testUser";
        String rating = "testRating";
        mActivity.saveRatingToPref(user, rating);

        Assert.assertEquals(mActivity.getRatingFromPref(user), rating);
    }

    @After
    public void tearDown() throws Exception {

        mActivity.deleteSharedPreferences("testUser");
        mActivity = null;
    }
}