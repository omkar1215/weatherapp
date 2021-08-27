package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WheaterActivity extends AppCompatActivity {

    EditText cityname;
    TextView tpc,tpf,latitude,longitude;
    Button submit;
    private final String url= "https://api.openweathermap.org/data/2.5/weather?q=";
    DecimalFormat df=new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheater);

        cityname=(EditText)findViewById(R.id.editTextTextPersonName4);
        tpc=(TextView)findViewById(R.id.textView6);
        tpf=(TextView)findViewById(R.id.textView7);
        latitude=(TextView)findViewById(R.id.textView8);
        longitude=(TextView)findViewById(R.id.textView9);
        submit=(Button)findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempurl="";
                String namecity=cityname.getText().toString();
                if(namecity.isEmpty()){
                    cityname.setError("Enter City Name");
                }else {
                    tempurl = url+namecity+"&appid=f540251d76f44e1850e1887ef93095ce";
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           // Log.d("response",response);
                            String output = "";
                            try {
                                JSONObject jsonObjectResponse = new JSONObject(response);
                                JSONObject jsonCurrent=jsonObjectResponse.getJSONObject("main");
                                double tmpcc=jsonCurrent.getDouble("temp");
                                JSONObject jsonLocation=jsonObjectResponse.getJSONObject("coord");
                                double lat=jsonLocation.getDouble("lat");
                                double lon=jsonLocation.getDouble("lon");
                                String tmppc=String.valueOf(tmpcc);
                                double data=(9/5)*tmpcc+32;
                                String tmppf=String.valueOf(data);
                                String latt=String.valueOf(lat);
                                String lonn=String.valueOf(lon);
                                tpc.setText("Temperature in Centigrade : "+tmppc+"C");
                                tpf.setText("Temperature in Fahrenheit : "+tmppf+"F");
                                latitude.setText("Latitude : "+latt);
                                longitude.setText("Longitude : "+lonn);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(WheaterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });

    }
}