package com.wafaaelm3andy.travelmantics.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.wafaaelm3andy.travelmantics.Adapter.DealsListAdapter;
import com.wafaaelm3andy.travelmantics.Model.DealOfHoliday;
import com.wafaaelm3andy.travelmantics.R;
import com.wafaaelm3andy.travelmantics.utils.FirebaseUtil;

public class UserActivity extends AppCompatActivity  implements DealsListAdapter.ListItemClickListener{
RecyclerView dealsRV ;
DealsListAdapter listAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);



    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        DealOfHoliday dealOfHoliday = FirebaseUtil.dealOfHolidays.get(clickedItemIndex);
        Intent intent = new Intent(this ,AddHolidayDealActivity.class);
        intent.putExtra(getString(R.string.deal_object),dealOfHoliday);
        startActivity(intent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.deal_list_meue,menu);
        MenuItem insertitem = menu.findItem(R.id.open_add_deal);
        if(FirebaseUtil.isAdmin){
            insertitem.setVisible(true);
        }else{
            insertitem.setVisible(false);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.open_add_deal :
                startActivity(new Intent(this,AddHolidayDealActivity.class));
                return  true ;
            case R.id.logout :
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                FirebaseUtil.attachListener();
                            }
                        });
                FirebaseUtil.deattachListener();
                return  true ;
            default:
                return super.onOptionsItemSelected(item);


        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        FirebaseUtil.deattachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dealsRV=findViewById(R.id.dealsRV);
        listAdapter=new DealsListAdapter(R.layout.list_item,this,this,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        dealsRV.setLayoutManager(linearLayoutManager);
        dealsRV.setAdapter(listAdapter);
        FirebaseUtil.attachListener();

    }
    public void showMenu(){
        invalidateOptionsMenu();
    }
}
