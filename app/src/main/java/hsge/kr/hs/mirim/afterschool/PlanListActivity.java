package hsge.kr.hs.mirim.afterschool;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hsge.kr.hs.mirim.afterschool.adapter.PlanListRecyclerAdapter;
import hsge.kr.hs.mirim.afterschool.model.PlanItem;

public class PlanListActivity extends AppCompatActivity {
    String dbname = "PlanDB";
    String tablename = "planlist";
    String sql;
    FloatingActionButton addBtn;
    MyDBHelper dbHelper;
    SQLiteDatabase sqlDB;
    Cursor resultset;
    public static ArrayList<PlanItem> planlists = new ArrayList<>();

    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");
    long now = System.currentTimeMillis();
    Date today = new Date(now);

    String todayTime = sdfNow.format(today);
    Date start;
    Calendar calendar = Calendar.getInstance();
    int d_day;

    RecyclerView planlistRecyclerView;
    PlanListRecyclerAdapter adapter;

    String goal = "";
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);

        planlistRecyclerView = findViewById(R.id.planlist_recyclerview);
        planlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlanListRecyclerAdapter(PlanListActivity.this, new PlanListRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, final int position) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(getParent());
                confirm.setTitle("삭제하시겠습니까?");

                confirm.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteButtonClick(position);
                        selectButtonClick();
                    }
                });

                confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                });
                confirm.show();
            }
        });

        planlistRecyclerView.setAdapter(adapter);

        dbHelper = new MyDBHelper(this);
        addBtn = findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ListMake();
            }
        });

        selectButtonClick();
    }

    public void deleteButtonClick(int pos){
        sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("delete from " + tablename + " where num = " + planlists.get(pos).num);
        planlists.remove(pos);
        sqlDB.close();
        removecreateNotification(null);
    }

    public void selectButtonClick(){   // 항상 DB문을 쓸때는 예외처리(try-catch)를 해야한다
        planlists.removeAll(planlists);
        goal = "";
        try {
            sqlDB = dbHelper.getReadableDatabase();
            sql = "select content, startday, num from "+ tablename;
            resultset = sqlDB.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)
            while (resultset.moveToNext()) {
                PlanItem item = new PlanItem();
                item.content = resultset.getString(0);
                item.startday = resultset.getString(1);
                start = sdfNow.parse(item.startday);
                calendar.setTime(start);
                calendar.add(Calendar.DATE, 66);
                item.startday += " ~ " + sdfNow.format(calendar.getTime());
                d_day = (int)((calendar.getTime().getTime() - today.getTime()) / (24 * 60 * 60 * 1000)) + 1;
                Log.v("d_day", d_day +" ");
                item.d_day = d_day;
                item.num = resultset.getInt(2);
                if(d_day >= 0) {
                    planlists.add(item);
                    count++;
                    goal += (count) + "." + item.content + "\r\n";
                }
            }
            adapter.setPlanlists(planlists);
        } catch (Exception e) {
            Log.d("error", e.toString());
        }

        if(!goal.equals("")) {
            createNotification(null, goal);
        }

        sqlDB.close();
    }

    public void ListMake() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getParent());
        final EditText input = new EditText(this);

        alert.setTitle("목표 등록");
        alert.setMessage("66일동안 진행할 목표를 등록하세요!");

        input.setHint("ex)하루마다 영단어 10개 외우기");
        alert.setView(input);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                sqlDB = dbHelper.getWritableDatabase();
                //더미데이터
//                sqlDB.execSQL("insert into planlist (content, startday) values('" + "내 사과 물주기" + "','" + "2017/11/10" + "');");
//                sqlDB.execSQL("insert into planlist (content, startday) values('" + "30분씩 걷기" + "','" + "2018/9/10" + "');");
//                sqlDB.execSQL("insert into planlist (content, startday) values('" + "최소 4시간 이상 자기" + "','" + "2018/10/15" + "');");
//                sqlDB.execSQL("insert into planlist (content, startday) values('" + "영단어 10개 외우기" + "','" + "2018/11/10" + "');");
//                sqlDB.execSQL("insert into planlist (content, startday) values('" + "물고기 밥주기" + "','" + "2018/11/24" + "');");
//                sqlDB.execSQL("insert into planlist (content, startday) values('" + "엄마한테 전화하기" + "','" + "2018/11/30" + "');");
                sqlDB.execSQL("insert into planlist (content, startday) values('" + input.getText().toString() + "','" + todayTime + "');");
                sqlDB.close();
                selectButtonClick();
                Toast toast = Toast.makeText(getApplicationContext(), "목표가 등록되었습니다!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void alert(View v){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);

        alert.setPositiveButton("오늘 할 일을 했다", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "잘하셨어요!",Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("오늘 할 일을 못했다", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "내일은 꼭!",Toast.LENGTH_SHORT).show();
            }
        });

        alert.setTitle("66");
        alert.setIcon(R.drawable.icon);
        alert.setMessage("오늘도 잘 먹고 잘 자셨나요?");
        alert.show();
    }

    public void createNotification(View view, String content) {
        show(content);
    }

    private void show(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.icon); //알림 이미지
        builder.setContentTitle("66"); //알림 제목
        builder.setContentText("오늘의 목표 : " + content); //알림 내용
        builder.setOngoing(true); //알림 고정
        builder.setAutoCancel(false); //알림 터치 시 자동으로 삭제할 것인지

        Intent intent = new Intent(this, MainActivity.class); //먼지 모르겟는데 필수적인거같음
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), //알림 옆 큰 이미지
                R.drawable.icon);
        builder.setLargeIcon(largeIcon);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //먼지 모르겟는데 필수적인거같음
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }
        manager.notify(1, builder.build());
    }

    public void removecreateNotification(View view) { //알림 해제
        hide();
    }

    private void hide() {
        NotificationManagerCompat.from(this).cancel(1);
    }
}
