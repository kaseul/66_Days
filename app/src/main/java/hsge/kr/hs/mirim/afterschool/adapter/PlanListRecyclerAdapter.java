package hsge.kr.hs.mirim.afterschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import hsge.kr.hs.mirim.afterschool.CalendarTestActivity;
import hsge.kr.hs.mirim.afterschool.PlanActivityGroup;
import hsge.kr.hs.mirim.afterschool.R;
import hsge.kr.hs.mirim.afterschool.model.PlanItem;

import static hsge.kr.hs.mirim.afterschool.PlanActivityGroup.id_Calendar;

public class PlanListRecyclerAdapter extends RecyclerView.Adapter<PlanListRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PlanItem> planlists = new ArrayList<>();
    private OnItemLongClickListener longClickListener;

    public PlanListRecyclerAdapter(Context c, OnItemLongClickListener longClickListener) {
        this.context = c;
        this.longClickListener = longClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.planlist_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.d_day.setText("D - " + (planlists.get(position).d_day == 0 ? "Day" : planlists.get(position).d_day));
        holder.content.setText(planlists.get(position).content);
        holder.day.setText(planlists.get(position).startday);
        holder.progressBar.setProgress(66 - planlists.get(position).d_day);

        holder.planlistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Intent intent = new Intent(context, CalendarTestActivity.class);
                intent.putExtra("num", planlists.get(position).num);
                intent.putExtra("d_day", planlists.get(position).d_day);
                View view = PlanActivityGroup.planActivityGroup.getLocalActivityManager().startActivity(id_Calendar, intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                PlanActivityGroup.planActivityGroup.replaceView(view);
            }
        });

        holder.planlistView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onItemLongClick(v, position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return planlists.size();
    }

    public void setPlanlists(ArrayList<PlanItem> arr) {
        this.planlists = arr;
        notifyDataSetChanged();
    }

    public ArrayList<PlanItem> getPlanlists() {
        return planlists;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView d_day;
        public TextView content;
        public TextView day;
        public ProgressBar progressBar;
        public View planlistView;

        public ViewHolder(View v) {
            super(v);
            planlistView = v;

            d_day =  v.findViewById(R.id.d_day);
            content = v.findViewById(R.id.content);
            day = v.findViewById(R.id.day);
            progressBar = v.findViewById(R.id.progress_bar);
        }
    }
}
