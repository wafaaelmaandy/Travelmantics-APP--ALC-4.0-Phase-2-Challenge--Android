package com.wafaaelm3andy.travelmantics.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wafaaelm3andy.travelmantics.Model.DealOfHoliday;
import com.wafaaelm3andy.travelmantics.R;
import com.wafaaelm3andy.travelmantics.utils.FirebaseUtil;

public class AddHolidayDealActivity extends AppCompatActivity {
    private static final int PICTURE_REQUEST = 42;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    EditText titleET ,detailsET , priceET ;
    ImageView dealImage ;
    private DealOfHoliday dealOfHoliday;
    String imgname , url ;
    Button insertbtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_holiday_deal);
        //FirebaseUtil.openfbRef("traveldeals",this);
        database =FirebaseUtil.database;
        myRef = FirebaseUtil.myRef;
        titleET =findViewById(R.id.deal_title_ET);
        detailsET=findViewById(R.id.deal_details_ET);
        priceET=findViewById(R.id.deal_price_ET);
        dealImage=findViewById(R.id.deal_image);
        insertbtn = findViewById(R.id.insert_img);
        Intent intent= getIntent();
        DealOfHoliday dealOfHoliday = intent.getParcelableExtra(getString(R.string.deal_object));
        if(dealOfHoliday==null){
            dealOfHoliday= new DealOfHoliday();}
        this.dealOfHoliday=dealOfHoliday;
        titleET.setText(dealOfHoliday.getTitle());
        detailsET.setText(dealOfHoliday.getDetails());
        priceET.setText(dealOfHoliday.getPrice());
        Glide.with(getApplicationContext()).load(dealOfHoliday.getImgurl()).into(dealImage);



    }

    public void insertPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICTURE_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.save_deals,menu);
        MenuItem delateItem = menu.findItem(R.id.delete_deal_menu);
        MenuItem saveItem = menu.findItem(R.id.save_deal_menu);

        if(FirebaseUtil.isAdmin){
            delateItem.setVisible(true);
            saveItem.setVisible(true);
            enableEditText(true);
            insertbtn.setEnabled(true);

        }else{
            delateItem.setVisible(false);
            saveItem.setVisible(false);
        enableEditText(false);
            insertbtn.setEnabled(false);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_deal_menu :
                saveDeal();
                Toast.makeText(AddHolidayDealActivity.this,"saved",Toast.LENGTH_LONG).show();
                clean();
                backToList();
                return  true ;
            case R.id.delete_deal_menu :
                deleteDeal();
                Toast.makeText(AddHolidayDealActivity.this,"deleted",Toast.LENGTH_LONG).show();
                 backToList();
                 return  true ;
                default:
                    return super.onOptionsItemSelected(item);


        }

    }

    private void clean() {
        titleET.setText("");
        detailsET.setText("");
        priceET.setText("");
        titleET.requestFocus();
    }

    private void saveDeal() {
        dealOfHoliday.setTitle( titleET.getText().toString());
        dealOfHoliday.setDetails(detailsET.getText().toString());
        dealOfHoliday.setPrice( priceET.getText().toString());
        if(dealOfHoliday.getId()==null){
        myRef.push().setValue(dealOfHoliday);}
        else{
        myRef.child(dealOfHoliday.getId()).setValue(dealOfHoliday);}


    }
    private void deleteDeal() {

        if(dealOfHoliday.getId()==null){
           Toast.makeText(this,"deal deosnot exist",Toast.LENGTH_LONG).show();
        return;}
        else{
            myRef.child(dealOfHoliday.getId()).removeValue();

            if(dealOfHoliday.getImgName()!=null&&dealOfHoliday.getImgName().isEmpty()==false){
                StorageReference storageReference = FirebaseUtil.storageReference.child(dealOfHoliday.getImgName());

                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddHolidayDealActivity.this,"Success",Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }

    }
    private void backToList(){
        startActivity(new Intent(this,UserActivity.class));
    }
    private  void enableEditText(boolean isEnabled){
        titleET.setEnabled(isEnabled);
        detailsET.setEnabled(isEnabled);
        priceET.setEnabled(isEnabled);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICTURE_REQUEST&&resultCode==RESULT_OK){
             Uri imgUri = data.getData();
             uploadFile(imgUri);
        }
    }
    //this method will upload the file
    private void uploadFile(final Uri imgUri ) {
        //if there is a file to upload
        if (imgUri != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference storageReference = FirebaseUtil.storageReference.child(imgUri.getLastPathSegment());
            final UploadTask uploadTask = storageReference.putFile(imgUri) ;
           imgname= uploadTask.getResult().getStorage().getPath();
            uploadTask .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                   // imgname =taskSnapshot.getStorage().getPath();

                    //if the upload is successfull
                    //hiding the progress dialog
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri  downloadUri = task.getResult();
                                url = downloadUri.toString();
                                dealOfHoliday.setImgurl(url);
                                dealOfHoliday.setImgName(imgname);
                                Glide.with(getApplicationContext()).load(downloadUri).into(dealImage);

                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });
                    progressDialog.dismiss();

                    //and displaying a success toast
                    Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });



        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(getApplicationContext(),"fail to upload", Toast.LENGTH_LONG).show();

        }
    }
}

