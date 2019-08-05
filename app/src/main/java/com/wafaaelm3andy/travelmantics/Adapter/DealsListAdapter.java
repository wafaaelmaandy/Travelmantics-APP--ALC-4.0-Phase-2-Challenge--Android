package com.wafaaelm3andy.travelmantics.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wafaaelm3andy.travelmantics.Model.DealOfHoliday;
import com.wafaaelm3andy.travelmantics.R;
import com.wafaaelm3andy.travelmantics.ui.UserActivity;
import com.wafaaelm3andy.travelmantics.utils.FirebaseUtil;

import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class DealsListAdapter extends  RecyclerView.Adapter<DealsListAdapter.DealsViewHolder> {
    private FirebaseDatabase database ;
    private DatabaseReference myRef ;
    private ChildEventListener  childEventListener;
    private List<DealOfHoliday> dealOfHolidays;
    private int rowLayout;
    private Context context;

    public List<DealOfHoliday> getDepartment() {
        return dealOfHolidays;
    }

    public void setDealOfHolidays(List<DealOfHoliday> dealOfHolidays) {
        this.dealOfHolidays = this.dealOfHolidays;
        notifyDataSetChanged();
    }




    final  private DealsListAdapter.ListItemClickListener listItemClickListener  ;
    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    public DealsListAdapter(int rowLayout, UserActivity caller, Context context , DealsListAdapter.ListItemClickListener listener) {
        FirebaseUtil.openfbRef("traveldeals",caller);
        database =FirebaseUtil.database;
        myRef = FirebaseUtil.myRef;
        dealOfHolidays = FirebaseUtil.dealOfHolidays ;
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DealOfHoliday dealOfHoliday = dataSnapshot.getValue(DealOfHoliday.class);
                dealOfHoliday.setId(dataSnapshot.getKey());
                dealOfHolidays.add(dealOfHoliday);
                notifyItemInserted(dealOfHolidays.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef.addChildEventListener(childEventListener);
        this.rowLayout = rowLayout;
        this.context = context;
        listItemClickListener = listener ;


    }


    public class DealsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout HolidayDealLayout;
        TextView titleTV ,detailsTV,priceTV ;
        ImageView dealImg ;



        public DealsViewHolder(View v) {
            super(v);
            HolidayDealLayout = v.findViewById(R.id.deal_layout_list_item);
            titleTV=  v.findViewById(R.id.deal_title_tv);
            detailsTV=  v.findViewById(R.id.deal_details_tv);
            priceTV=  v.findViewById(R.id.deal_price_tv);
            dealImg=v.findViewById(R.id.deal_img);
            itemView.setOnClickListener(this);        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickedPosition);
        }
    }








    @Override
    public DealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new DealsViewHolder(view);
    }





    @Override
    public void onBindViewHolder(DealsViewHolder holder, final int position) {

      holder.titleTV.setText(dealOfHolidays.get(position).getTitle());
        holder.detailsTV.setText(dealOfHolidays.get(position).getDetails());
        holder.priceTV.setText(dealOfHolidays.get(position).getPrice());
        Glide.with(context).load(dealOfHolidays.get(position).getImgurl()).into(holder.dealImg);



    }

    @Override
    public int getItemCount() {
        return dealOfHolidays.size();
    }
}
