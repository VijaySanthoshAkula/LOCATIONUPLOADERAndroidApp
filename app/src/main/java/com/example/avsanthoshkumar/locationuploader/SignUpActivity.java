package com.example.avsanthoshkumar.locationuploader;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private static Button SignUp;
    private String sEmail,sPassword,sPhoneNumber,sIdNumber,sRoomNumber,sName;
    List<String> details;
    HashMap<String, String> result = new HashMap<>();
    int Size;
    View myLayout;
    private ProgressBar spinner;
    loginDatabaseHelper myDb;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Student");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        myDb = new loginDatabaseHelper(this);
        onClickSignUpButtonListner();
        LayoutInflater myInflator=getLayoutInflater();
        myLayout=myInflator.inflate(R.layout.customtoast,(ViewGroup)findViewById(R.id.toastlayout) );
    }

    public void onClickSignUpButtonListner(){
        SignUp=findViewById(R.id.SignUp);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.EmailId);
                sEmail = et.getText().toString();
                EditText et2 =(EditText) findViewById(R.id.Password);
                sPassword = et2.getText().toString();
                EditText et3 =(EditText) findViewById(R.id.PhoneNumber);
                sPhoneNumber = et3.getText().toString();
                EditText et4 =(EditText) findViewById(R.id.IdNumber);
                sIdNumber = et4.getText().toString();
                EditText et5 =(EditText) findViewById(R.id.Name);
                sName = et5.getText().toString();
                EditText et6 =(EditText) findViewById(R.id.RoomNumber);
                sRoomNumber = et6.getText().toString();
                details=new ArrayList<String>();
                if(!(sEmail.equals(""))) {
                    details.add(sEmail);
                    result.put("Email",sEmail);
                }
                if(!(sPassword.equals(""))) {
                    details.add(sPassword);
                }
                if(!(sPhoneNumber.equals(""))) {
                    details.add(sPhoneNumber);
                    result.put("ContactNo",sPhoneNumber);
                }
                if(!(sIdNumber.equals(""))) {
                    details.add(sIdNumber.toUpperCase());
                    result.put("IDNumber",sIdNumber.toUpperCase());
                }
                if(!(sRoomNumber.equals(""))) {
                    details.add(sRoomNumber);
                    result.put("RoomNo",sRoomNumber);
                }
                if(!(sName.equals(""))) {
                    details.add(sName);
                    result.put("Name",sName);
                }
                Size=details.size();
                if(!(details.size()==6)){
                    TextView text = (TextView) myLayout.findViewById(R.id.text);
                    text.setText("Please insert all the fields");
                    Toast myToast=new Toast(SignUpActivity.this);
                    myToast.setDuration(Toast.LENGTH_LONG);
                    myToast.setView(myLayout);
                    myToast.show();
                }else{
                    AddData();

                    finish();
                }
            }
        });

    }

    public void AddData() {
        boolean isInserted=false;
        Cursor res = myDb.getAllData();
        int count=res.getCount();
        if (count != 0) {
            myDb.deleteAllData();
        }
        spinner.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {

            @Override
            public void run() {

                addEntry();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setVisibility(View.GONE);
                        TextView text = (TextView) myLayout.findViewById(R.id.text);
                        text.setText("Sign Up Completed");
                        Toast myToast=new Toast(SignUpActivity.this);
                        myToast.setDuration(Toast.LENGTH_LONG);
                        myToast.setView(myLayout);
                        myToast.show();
                    }
                });

            }
        }).start();

    }
    private void addEntry() {
        Log.d("mail+pass+phone", details.get(0)+details.get(1)+details.get(2)+details.get(3));
        boolean bval=myDb.insertData(details.get(0),details.get(1),details.get(2),details.get(3));
        result.put("lat","0.0");
        result.put("lng","0.0");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        result.put("time",dateFormat.format(date));
        result.put("outside","no");
        myRef.child(details.get(3)).setValue(result);
    }
}
