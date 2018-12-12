package com.example.avsanthoshkumar.locationuploader;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetpasswordActivity extends AppCompatActivity {
    loginDatabaseHelper ldb;
    private static Button getpassword;
    private String sPhoneGiven;
    View myLayout;
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);
        ldb = new loginDatabaseHelper(this);
        onClickGetPasswordButtonListner();
        LayoutInflater myInflator=getLayoutInflater();
        myLayout=myInflator.inflate(R.layout.customtoast,(ViewGroup)findViewById(R.id.toastlayout) );
        sentPI = PendingIntent.getBroadcast(ForgetpasswordActivity.this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(ForgetpasswordActivity.this, 0, new Intent(DELIVERED), 0);
    }

    @Override
    protected void onResume(){
        super.onResume();
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent successfully!", Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong and there's no way to tell what, why or how.
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure!", Toast.LENGTH_SHORT).show();
                        break;

                    //Your device simply has no cell reception. You're probably in the middle of
                    //nowhere, somewhere inside, underground, or up in space.
                    //Certainly away from any cell phone tower.
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service!", Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong in the SMS stack, while doing something with a protocol
                    //description unit (PDU) (most likely putting it together for transmission).
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;

                    //You switched your device into airplane mode, which tells your device exactly
                    //"turn all radios off" (cell, wifi, Bluetooth, NFC, ...).
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off!", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered!", Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        //register the BroadCastReceivers to listen for a specific broadcast
        //if they "hear" that broadcast, it will activate their onReceive() method
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }
    private void sendSMS(String phoneNumber, String message) {
        if (ContextCompat.checkSelfPermission(ForgetpasswordActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ForgetpasswordActivity.this, new String [] {Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
            if (ContextCompat.checkSelfPermission(ForgetpasswordActivity.this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(ForgetpasswordActivity.this, new String [] {Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            }
        }
        else {
            if (ContextCompat.checkSelfPermission(ForgetpasswordActivity.this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(ForgetpasswordActivity.this, new String [] {Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            }
        }
    }

    public void onClickGetPasswordButtonListner() {
        getpassword = findViewById(R.id.GetPassword);
        getpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText phonenumber=findViewById(R.id.PhoneNumber);
                sPhoneGiven = phonenumber.getText().toString();
                Cursor res = ldb.getAllData();
                res.getCount();
                Log.d("countis ",Integer.toString(res.getCount()));
                String phonen="";
                String spass="";
                while (res.moveToNext()) {
                    phonen= res.getString(3).toString();
                    spass=res.getString(2).toString();

                }

                if(phonen.equals(sPhoneGiven)){
                    sendSMS(phonen, "Hey your password is "+spass);
                    finish();
                }else{
                    TextView text = (TextView) myLayout.findViewById(R.id.text);
                    text.setText("Phone number doesn't match with records");
                    Toast myToast=new Toast(ForgetpasswordActivity.this);
                    myToast.setDuration(Toast.LENGTH_SHORT);
                    myToast.setView(myLayout);
                    myToast.show();
                }
            }
        });
    }
}