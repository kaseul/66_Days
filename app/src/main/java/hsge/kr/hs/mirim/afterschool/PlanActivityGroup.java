package hsge.kr.hs.mirim.afterschool;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Stack;

public class PlanActivityGroup extends ActivityGroup {
    public static final String id_Plan = "Plan";
    public static final String id_Calendar = "Calendar";

    public static PlanActivityGroup planActivityGroup;
    private ArrayList<View> history;

    private LocalActivityManager localActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        history = new ArrayList<>();
        planActivityGroup = this;
        localActivityManager = getLocalActivityManager();

        Intent intent = new Intent(PlanActivityGroup.this, PlanListActivity.class);
        View view = localActivityManager.startActivity(id_Plan, intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
        replaceView(view);
    }

    public void replaceView(View view) {
        history.add(view);
        setContentView(view);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(history.size() > 0) {
                history.remove(history.size() - 1);
                if(history.size() == 0)
                    return false;
                else
                    setContentView(history.get(history.size() - 1));
            } else {
                return false;
            }
        }
        return true;
    }
}
