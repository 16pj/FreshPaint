package com.rpj.robin.freshpaint;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Listed extends AppCompatActivity {

    String res = "";

    String url_add = "http://robindustbin.comli.com/findimage.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        populatelist();
        listener();
    }

    private void listener() {
        ListView listView = (ListView)findViewById(R.id.listView);
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view;
                    String num = "You Selected " + textView.getText().toString();
                    Toast.makeText(Listed.this, num, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Listed.this, Show.class);
                    i.putExtra("url", "http://robindustbin.comli.com/images/" + textView.getText().toString());

                    startActivity(i);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void populatelist() {

        new Backgrounder().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresher) {
            populatelist();
            listener();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



public class Backgrounder extends AsyncTask<Void, Void, String> {


    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            java.net.URL url = new URL(url_add);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);

            InputStream inputStream1 = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream1, "iso-8859-1"));
            String result = "";
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            bufferedReader.close();
            inputStream1.close();
            httpURLConnection.disconnect();
            res = result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ListView listView = (ListView) findViewById(R.id.listView);
        String [] res1 = res.split(";");
        int i;
        for (i=0;i<res1.length;i++) {
            res += res1[i];
            res += "\n";
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Listed.this, R.layout.lister,res1);
        listView.setAdapter(adapter);

        Toast.makeText(Listed.this,"DONE!!",Toast.LENGTH_SHORT).show();
    }
}


}
