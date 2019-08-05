package com.wafaaelm3andy.travelmantics.utils;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wafaaelm3andy.travelmantics.Model.DealOfHoliday;
import com.wafaaelm3andy.travelmantics.ui.AddHolidayDealActivity;
import com.wafaaelm3andy.travelmantics.ui.UserActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    private static final int RC_SIGN_IN = 123;
    public static FirebaseDatabase database  ;
    public static  DatabaseReference myRef ;
    public static FirebaseAuth firebaseAuth ;
    public static FirebaseAuth.AuthStateListener authStateListener ;
    public static FirebaseStorage firebaseStorage ;
    public static StorageReference storageReference ;


    private  static  FirebaseUtil firebaseUtil;
    private static UserActivity caller ;
    public static ArrayList <DealOfHoliday>dealOfHolidays ;
    public  static  boolean isAdmin ;

    private FirebaseUtil(){}
    public static void  openfbRef(String ref,UserActivity callerActivity){

        if(firebaseUtil==null){
            firebaseUtil=new FirebaseUtil();
            database = FirebaseDatabase.getInstance();
            firebaseAuth= FirebaseAuth.getInstance();
            caller=callerActivity ;
            authStateListener= new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser()== null){
                    FirebaseUtil.signin();}
                    String uid = firebaseAuth.getUid();
                    checkAdmin(uid);
                    Toast.makeText(caller.getBaseContext(),"Welcome Back !",Toast.LENGTH_LONG).show();
                    connectToStorage();

                }
            };
        }
        dealOfHolidays= new ArrayList<DealOfHoliday>();
        myRef = database.getReference().child(ref);

    }

    private static void checkAdmin(String uid) {
        FirebaseUtil.isAdmin=false ;
        DatabaseReference reference = database.getReference().child("administrators").child(uid);
        ChildEventListener childEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin=true ;
                caller.showMenu();
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
        reference.addChildEventListener(childEventListener);
    }

    public  static void attachListener(){
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    public  static void deattachListener(){
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    public  static void signin(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
               );

// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }
    public static void connectToStorage(){
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference().child("deals_pictures");
    }
}
