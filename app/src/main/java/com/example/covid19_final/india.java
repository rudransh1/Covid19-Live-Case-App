package com.example.covid19_final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class india extends AppCompatActivity {

    Button button7,button8,button6;
    private static TextView textview18,textview25,textview26,textview27;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.india_activity);
        button6=findViewById(R.id.button6);
        button7=findViewById(R.id.button7);
        button8=findViewById(R.id.button8);
        textview18=findViewById(R.id.textView18);
        textview25=findViewById(R.id.textView25);
        textview26=findViewById(R.id.textView26);
        textview27=findViewById(R.id.textView27);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parsed process2 = new parsed();
                process2.execute();
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home();
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_loc();
            }
        });

    }
    private void home() {
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void my_loc() {
        Intent intent =new Intent(this, My_location.class);
        startActivity(intent);
    }
        public class parsed extends AsyncTask<Void,Void,Void> {

        //private ProgressDialog pDialog;
        String data ="";
        String dataParsed = "";
        String singleParsed ="";
        String x,u,v,w;
        String cit,stat;

        AlertDialog mydialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mydialog = new AlertDialog.Builder(india.this)
                    //.setTitle("Delete entry")
                    .setMessage("Getting Data ...")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.


                    // A null listener allows the button to dismiss the dialog and take no further action.
                    //.setNegativeButton(android.R.string.no, null)
                    .setCancelable(true)
                    .setIcon(android.R.drawable.btn_star)
                    .show();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.covid19india.org/data.json");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line="";
                int j=0;

                while((line=bufferedReader.readLine()) != null){
                    data = data + line;
                    j++;
                    //Log.d("this_tag", String.valueOf(j));
                }


                JSONObject JA=new JSONObject(data);
                JSONArray sw=JA.getJSONArray("statewise");

                JSONObject me=(JSONObject) sw.get(0);
                x=me.getString("confirmed");
                u=me.getString("active");
                v=me.getString("recovered");
                w=me.getString("deaths");


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mydialog.dismiss();
            india.textview18.setText(this.x);
            india.textview25.setText(this.u);
            india.textview26.setText(this.v);
            india.textview27.setText(this.w);

        }

    }
}
