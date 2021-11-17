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
import android.util.Log;
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
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private EditText number;
    private EditText message;
    private EditText count;
    private Button contacts;
    public Button send;
    private static final int CONTACT_PICKER_REQUEST = 222;
    List<ContactResult> results = new ArrayList<>();

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
                        Manifest.permission.READ_CONTACTS
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!results.isEmpty()) {
                    for(int j=0; j<results.size();j++) {
                        for (int i = 0; i < parseInt(count.getText().toString()); i++) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(results.get(j).getPhoneNumbers().get(0).getNumber(), null, message.getText().toString(), null, null);
                            Toast.makeText(MainActivity.this, "Sent: " + (i + 1) + " messages", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                Toast.makeText(MainActivity.this,"Messages sent successfully!",Toast.LENGTH_LONG).show();
            }

        });
        contacts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context//Optional - default: MultiContactPicker.Azure
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                        .handleColor(ContextCompat.getColor(MainActivity.this, R.color.azureColorAccent)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.azureColorAccent)) //Optional - default: Azure Blue
                        .bubbleTextColor(Color.WHITE) //Optional - default: White
                        .setTitleText("Select Contacts") //Optional - default: Select Contacts//Optional - will pre-select contacts of your choice. String... or List<ContactResult>
                        .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                        .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                        .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in,
                                android.R.anim.fade_out) //Optional - default: No animation overrides
                        .showPickerForResult(CONTACT_PICKER_REQUEST);

            }
        }
        );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                results = MultiContactPicker.obtainResult(data);
                StringBuilder names = new StringBuilder(results.get(0).getDisplayName());
                for(int j=0; j<results.size();j++)
                {
                    if(j!=0)
                    names.append(",").append(results.get(j).getDisplayName());
                }
                number.setText(names);
                number.setClickable(false);
                Log.d("MyTag", results.get(0).getDisplayName());
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }
}