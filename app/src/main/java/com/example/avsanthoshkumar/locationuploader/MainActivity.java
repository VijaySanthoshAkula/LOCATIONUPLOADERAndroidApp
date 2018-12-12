package com.example.avsanthoshkumar.locationuploader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static Button login,signup,forgetPassword;
    private String sUsername;
    private String sPassword;
    View myLayout;
    loginDatabaseHelper ldb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ldb = new loginDatabaseHelper(this);
        onClickLoginButtonListner2();
        onClickSignUpButtonListener();
        onClickForgotPassword();
        LayoutInflater myInflator=getLayoutInflater();
        myLayout=myInflator.inflate(R.layout.customtoast,(ViewGroup)findViewById(R.id.toastlayout) );
    }
    public void onClickLoginButtonListner2(){
        login=findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username=findViewById(R.id.Username);
                EditText password=findViewById(R.id.Password);
                sUsername = username.getText().toString();
                sPassword=password.getText().toString();
                Cursor res = ldb.getAllData();
                res.getCount();
                Log.d("countis ",Integer.toString(res.getCount()));
                if(!(sUsername.equals("")||(sPassword.equals("")))) {
                    String suname = "";
                    String spass = "";
                    while (res.moveToNext()) {
                        suname = res.getString(1).toString();
                        spass = res.getString(2).toString();

                    }
                    if (sUsername.equals(suname) && (sPassword.equals(spass))) {

                        Intent intent = new Intent("com.example.avsanthoshkumar.locationuploader.UploadGpsActivity");
                        startActivity(intent);
                    } else {
                        TextView text = (TextView) myLayout.findViewById(R.id.text);
                        text.setText("Username or password incorrect");
                        Toast myToast = new Toast(MainActivity.this);
                        myToast.setDuration(Toast.LENGTH_LONG);
                        myToast.setView(myLayout);
                        myToast.show();
                    }
                }else{
                    TextView text = (TextView) myLayout.findViewById(R.id.text);
                    text.setText("Username or password incorrect");
                    Toast myToast = new Toast(MainActivity.this);
                    myToast.setDuration(Toast.LENGTH_LONG);
                    myToast.setView(myLayout);
                    myToast.show();
                }
            }
        });
    }

    public void onClickForgotPassword(){
        forgetPassword=findViewById(R.id.ForgotPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.avsanthoshkumar.locationuploader.ForgotpasswordActivity");
                startActivity(intent);
            }
        });
    }

    public void onClickSignUpButtonListener(){
        signup=findViewById(R.id.SignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("com.example.avsanthoshkumar.locationuploader.SignUpActivity");
                startActivity(intent);
            }
        });
    }
}
