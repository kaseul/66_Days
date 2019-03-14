package hsge.kr.hs.mirim.afterschool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import hsge.kr.hs.mirim.afterschool.model.PlanItem;

import static hsge.kr.hs.mirim.afterschool.PlanListActivity.planlists;

public class MainActivity extends AppCompatActivity {
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = planlists.size() - 1; i >= 0; i--) {
            if(!planlists.get(i).today)
                alert(null, planlists.get(i).content, i);
        }
    }

    public void alert(View v, String content, final int index){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setPositiveButton("달성했어요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "잘하셨어요!",Toast.LENGTH_SHORT).show();

                PlanItem item = new PlanItem();
                item.content = planlists.get(index).content;
                item.d_day = planlists.get(index).d_day;
                item.num = planlists.get(index).num;
                item.startday = planlists.get(index).startday;
                item.today = true;
                Log.v("", index + "");
                planlists.set(index, item);

                if(++check == planlists.size())
                    finish();
            }
        });
        alert.setNegativeButton("실패했어요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "내일은 꼭!",Toast.LENGTH_SHORT).show();
                if(++check == planlists.size())
                    finish();
            }
        });

        alert.setTitle("66");
        alert.setIcon(R.drawable.icon);
        alert.setMessage("오늘의 목표 : " + content + " 달성하셨나요?");
        alert.show();
    }

}
