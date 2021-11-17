package com.example.automatemessage;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private EditText number;
    private EditText message;
    private EditText count;
    private Button contacts;
    public Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number = findViewById(R.id.number);
        message = findViewById(R.id.message);
        count = findViewById(R.id.count);
        contacts = findViewById(R.id.contacts);
        send = findViewById(R.id.sendButton);
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS,
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0 ;i<parseInt(count.getText().toString());i++)
                {
                SmsManager smsManager =(SmsManager)SmsManager.getDefault();
                smsManager.sendTextMessage(number.getText().toString(),null,message.getText().toString(),null,null);
                Toast.makeText(MainActivity.this, "Sent: "+(i+1)+" messages",Toast.LENGTH_SHORT).show();

            }
                Toast.makeText(MainActivity.this,"Messages sent successfully!",Toast.LENGTH_LONG).show();
            }

        });
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }
        );
    }
}