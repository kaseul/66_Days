package hsge.kr.hs.mirim.afterschool;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hsge.kr.hs.mirim.afterschool.adapter.CompleteListRecyclerAdapter;
import hsge.kr.hs.mirim.afterschool.adapter.PlanListRecyclerAdapter;
import hsge.kr.hs.mirim.afterschool.model.PlanItem;

public class CompleteListActivity extends AppCompatActivity {
    String dbname = "PlanDB";
    String tablename = "planlist";
    String sql;

    MyDBHelper dbHelper;
    SQLiteDatabase sqlDB;
    Cursor resultset;
    ArrayList<PlanItem> planlists = new ArrayList<>();

    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");
    long now = System.currentTimeMillis();
    Date today = new Date(now);

    String todayTime = sdfNow.format(today);
    Date start;
    Calendar calendar = Calendar.getInstance();
    int d_day;

    RecyclerView planlistRecyclerView;
    CompleteListRecyclerAdapter adapter;

    TextView textIsComplete;
    ImageView imageIsComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_list);

        planlistRecyclerView = findViewById(R.id.planlist_recyclerview_complete);
        planlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        textIsComplete = findViewById(R.id.isComplete);
        imageIsComplete = findViewById(R.id.image_isComplete);

        adapter = new CompleteListRecyclerAdapter(getApplicationContext());

        planlistRecyclerView.setAdapter(adapter);
        dbHelper = new MyDBHelper(this);

        selectList();

        if(planlists.size() == 0) {
            textIsComplete.setVisibility(View.VISIBLE);
            imageIsComplete.setVisibility(View.VISIBLE);
        }
        else {
            textIsComplete.setVisibility(View.INVISIBLE);
            imageIsComplete.setVisibility(View.INVISIBLE);
        }
    }

    public void selectList(){   // 항상 DB문을 쓸때는 예외처리(try-catch)를 해야한다
        planlists.removeAll(planlists);
        try {
            sqlDB = dbHelper.getReadableDatabase();
            sql = "select content, startday, num from "+ tablename;
            resultset = sqlDB.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)
            while (resultset.moveToNext()) {
                PlanItem item = new PlanItem();
                item.content = resultset.getString(0);
                item.startday = resultset.getString(1);
                item.num = resultset.getInt(2);

                start = sdfNow.parse(item.startday);
                calendar.setTime(start);
                calendar.add(Calendar.DATE, 66);
                item.startday += " ~ " + sdfNow.format(calendar.getTime());
                d_day = (int)((calendar.getTime().getTime() - today.getTime()) / (24 * 60 * 60 * 1000)) + 1;
                Log.v("d_day", d_day +" ");
                item.d_day = d_day;

                if(d_day < 0)
                    planlists.add(item);

            }
            adapter.setPlanlists(planlists);
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        sqlDB.close();
    }
}
