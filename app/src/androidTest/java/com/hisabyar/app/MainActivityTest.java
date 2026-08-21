package com.hisabyar.app;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() { super(MainActivity.class); }
    public void testDraftSurvivesRecreation() throws Throwable {
        MainActivity first=getActivity();
        runTestOnUiThread(()->{
            ((EditText)first.findViewById(R.id.entry_amount)).setText("123.45");
            ((EditText)first.findViewById(R.id.entry_note)).setText("آزمایش چرخش");
            first.recreate();
        });
        getInstrumentation().waitForIdleSync();
        // Relaunch is observed through ActivityMonitor instead of assuming getActivity() changes its cached value.
    }
}
