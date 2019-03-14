package hsge.kr.hs.mirim.afterschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hsge.kr.hs.mirim.afterschool.CalendarTestActivity;
import hsge.kr.hs.mirim.afterschool.R;
import hsge.kr.hs.mirim.afterschool.model.PlanItem;

public class CompleteListRecyclerAdapter extends RecyclerView.Adapter<CompleteListRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PlanItem> planlists = new ArrayList<>();

    public CompleteListRecyclerAdapter(Context c) {
        this.context = c;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.completelist_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.content.setText(planlists.get(position).content);
        holder.day.setText(planlists.get(position).startday);
    }

    @Override
    public int getItemCount() {
        return planlists.size();
    }

    public void setPlanlists(ArrayList<PlanItem> arr) {
        this.planlists = arr;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView prize;
        public TextView content;
        public TextView day;
        public View planlistView;

        public ViewHolder(View v) {
            super(v);
            planlistView = v;

            prize =  v.findViewById(R.id.prize);
            content = v.findViewById(R.id.content);
            day = v.findViewById(R.id.day);
        }
    }
}
