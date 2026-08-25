package com.hisabyar.app;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() { super(MainActivity.class); }
    public void testDraftSurvivesRecreation() throws Throwable {
        MainActivity first=getActivity();
        ActivityMonitor monitor=getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);
        try {
            runTestOnUiThread(()->{
                ((EditText)first.findViewById(R.id.entry_amount)).setText("123.45");
                ((EditText)first.findViewById(R.id.entry_note)).setText("آزمایش چرخش");
                first.recreate();
            });
            MainActivity recreated=(MainActivity)getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
            assertNotNull("Activity must restart",recreated);
            getInstrumentation().waitForIdleSync();
            runTestOnUiThread(()->{
                assertEquals("123.45",((EditText)recreated.findViewById(R.id.entry_amount)).getText().toString());
                assertEquals("آزمایش چرخش",((EditText)recreated.findViewById(R.id.entry_note)).getText().toString());
            });
        } finally { getInstrumentation().removeMonitor(monitor); }
    }
}
