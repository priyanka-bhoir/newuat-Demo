package com.priyanka.newuat_demo.SubModule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.priyanka.newuat_demo.R;

import java.util.ArrayList;

public class AddressPicker extends AppCompatActivity {

    TextInputEditText streettxt,areatxt,citytxt,statetxt,countrytxt,postalcodetxt;
    AutoCompleteTextView addresstypetxt;
    Button submit;
    String street,area,city,state,country,postalcode,addresstype,key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_picker);

        Intent i=getIntent();
        key=i.getStringExtra("key");

        streettxt=findViewById(R.id.street);
        areatxt=findViewById(R.id.area);
        citytxt=findViewById(R.id.city);
        statetxt=findViewById(R.id.state);
        countrytxt=findViewById(R.id.contry);
        postalcodetxt=findViewById(R.id.postalCode);

        addresstypetxt=findViewById(R.id.addresstype);


        ArrayList<String> arrayList =new ArrayList<>();
        arrayList.add("Office");
        arrayList.add("Home");
        arrayList.add("Other");
        ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        addresstypetxt.setThreshold(1);
        addresstypetxt.setAdapter(arrayAdapter);
        submit=findViewById(R.id.saveButton);

        submit.setOnClickListener(v -> {
            street=streettxt.getText().toString();
            area=areatxt.getText().toString();
            city=citytxt.getText().toString();
            state=statetxt.getText().toString();
            country=countrytxt.getText().toString();
            postalcode=postalcodetxt.getText().toString();
            addresstype=addresstypetxt.getText().toString();

//            Intent i=new Intent();
            i.putExtra("street",street);
            i.putExtra("area",area);
            i.putExtra("city",city);
            i.putExtra("state",state);
            i.putExtra("country",country);
            i.putExtra("postalcode",postalcode);
            i.putExtra("addresstype",addresstype);
            i.putExtra("key",key);
            setResult(Activity.RESULT_OK,i);
            finish();
        });


    }
}