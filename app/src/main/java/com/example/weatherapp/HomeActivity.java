package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    EditText name,phone,dob,add1,add2,pin;
    RadioGroup rg;
    RadioButton rb;
    TextView district,state;
    Button pincheck,register;
    private RequestQueue mRequestQueue;
    int year,month,day;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name=(EditText)findViewById(R.id.editTextTextPersonName);
        phone=(EditText)findViewById(R.id.editTextPhone);
        dob=(EditText)findViewById(R.id.editTextDate);
        add1=(EditText)findViewById(R.id.editTextTextPersonName2);
        add2=(EditText)findViewById(R.id.editTextTextPersonName3);
        pin=(EditText)findViewById(R.id.editTextTextPostalAddress);
        rg=(RadioGroup)findViewById(R.id.radiogroup);
        district=(TextView)findViewById(R.id.textView4);
        state=(TextView)findViewById(R.id.textView5);
        pincheck=(Button)findViewById(R.id.button2);
        register=(Button)findViewById(R.id.button3);

        mRequestQueue = Volley.newRequestQueue(HomeActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dob.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod(
                        "setShowSoftInputOnFocus"
                        , new Class[]{boolean.class});
                method.setAccessible(true);
                method.invoke(dob, false);
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        Calendar calendar= Calendar.getInstance();
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog= new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pininput=pin.getText().toString();
                pincheck.setEnabled(!pininput.isEmpty());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRequestQueue = Volley.newRequestQueue(HomeActivity.this);

        pincheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinn=pin.getText().toString();
                if(pinn.isEmpty()){
                    pin.setError("Please Enter Your PIN CODE");
                }else {
                    int count = 0;

                    for (int i = 0; i < pinn.length(); i++) {
                        if (pinn.charAt(i) != ' ')
                            count++;
                    }
                    if (count == 6) {
                        mRequestQueue.getCache().clear();
                        String url = "http://www.postalpincode.in/api/pincode/" + pinn;
                        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
                        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray postOfficeArray = response.getJSONArray("PostOffice");
                                    if (response.getString("Status").equals("Error")) {
                                        Toast.makeText(HomeActivity.this, "pin code is not valid", Toast.LENGTH_SHORT).show();
                                    } else {
                                        JSONObject obj = postOfficeArray.getJSONObject(0);
                                        String ddistrict = obj.getString("District");
                                        String sstate = obj.getString("State");
                                        district.setText("District : "+ddistrict);
                                        state.setText("State : "+sstate);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(HomeActivity.this, "Pin code is not valid."+e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(HomeActivity.this, "Pin code is not valid.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(objectRequest);

                    } else {
                        Toast.makeText(HomeActivity.this, "Enter Valid Pin Code", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee=name.getText().toString();
                String phonee=phone.getText().toString();
                String dobb=dob.getText().toString();
                String add11=add1.getText().toString();
                String districtt=district.getText().toString();
                String pinn=pin.getText().toString();
                int radioID=rg.getCheckedRadioButtonId();
                rb=findViewById(radioID);
                String PhonePattern = "^\\d{10}$";

                if(!phonee.matches(PhonePattern)){
                    phone.setError("Enter Valid Phone Number");
                }
                if(rb==null){
                    //String gender=rb.getText().toString();
                    Toast.makeText(HomeActivity.this, "Please Select Your Gender", Toast.LENGTH_SHORT).show();
                }
                if(namee.isEmpty()){
                    name.setError("Please Enter Your Name");
                }
                if(phonee.isEmpty()){
                    phone.setError("Please Enter Your Phone Number");
                }
                if(dobb.isEmpty()){
                    dob.setError("Please Select Your DOB");
                }
                if(add11.isEmpty()){
                    add1.setError("Please Enter Your Address");
                }
                if(pinn.isEmpty()){
                    pin.setError("Please Enter Your PIN CODE");
                }
                if(districtt.equals("District : (auto-populate)")){
                    Toast.makeText(HomeActivity.this, "Select District & State", Toast.LENGTH_SHORT).show();
                }
                if(phonee.matches(PhonePattern) && !(namee.isEmpty() && phonee.isEmpty() && dobb.isEmpty() && add11.isEmpty() && pinn.isEmpty() && rb==null && districtt.equals("District : (auto-populate)"))) {

                    Intent done=new Intent(HomeActivity.this,WheaterActivity.class);
                    startActivity(done);
                    Toast.makeText(HomeActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}