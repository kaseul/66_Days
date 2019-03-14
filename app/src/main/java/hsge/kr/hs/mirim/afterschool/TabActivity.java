package hsge.kr.hs.mirim.afterschool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class TabActivity extends android.app.TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        final TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.getTabWidget().setDividerDrawable(null);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("",getResources().getDrawable(R.drawable.tab01)).setContent(new Intent(this, PlanActivityGroup.class)));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.tab02)).setContent(new Intent(this, CompleteListActivity.class)));
    }
}
