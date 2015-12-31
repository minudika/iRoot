package wso2.org.iroot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wso2.org.utils.XMLParser;


public class MainWindow extends Activity {
    Spinner spinnerStartLocation;
    Spinner spinnerEndLocation;
    Button btnSubmit;
    TimePicker timePicker;
    Context context = this;
    String[] root1;
    String[] root2;
    String[] allLocations;
    String[] busStopIds;
    String[] rootDirecion;
    String[] rootIds;
    String[] results;
    ArrayList<String> allBusStops;
    String stringFrom, stringTo;
    XMLParser xmlParser;


    HttpClient client;
    HttpPost post;
    HttpPost post2;

    //----------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        spinnerStartLocation = (Spinner) findViewById(R.id.spinner_from);
        spinnerEndLocation = (Spinner) findViewById(R.id.spinner_to);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        root1 = getResources().getStringArray(R.array.root1);
        root2 = getResources().getStringArray(R.array.root2);
        allLocations = getResources().getStringArray(R.array.busStopNames);
        busStopIds = getResources().getStringArray(R.array.busStopIds);
        rootDirecion = getResources().getStringArray(R.array.rootDirection);
        rootIds = getResources().getStringArray(R.array.rootIds);

        populateAllLocationList();
        populateStartLocaionSpinner(allBusStops);


        client = new DefaultHttpClient();
        //String url="https://10.100.4.177:9443/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint/";
        //http://10.100.5.59:9783/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint/
        String url = "http://192.168.43.8:9763/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint/";
        post = new HttpPost(url);
        post2 = new HttpPost(url);
        final Intent intent = new Intent(context, DisplayResults.class);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // populateSpinner();
                //pickTime();
                //startActivity(new Intent(context,MainWindow.class));
                new login().execute("");
                Bundle bundle = new Bundle();
                bundle.putStringArray("array", results);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        spinnerStartLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                String startLocation = parent.getItemAtPosition(position).toString();
                stringFrom = startLocation;
                populateEndLocaitonSpinner(getEndLocations(startLocation));
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void populateEndLocaitonSpinner(ArrayList<String> arrayList) {

        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, arrayList);
        // APP CURRENTLY CRASHING HERE
        spinnerEndLocation.setAdapter(adp);
        //Set listener Called when the item is selected in spinner
        spinnerEndLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                String endLocation = parent.getItemAtPosition(position).toString();
                stringTo = endLocation;

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void populateStartLocaionSpinner(ArrayList<String> arrayList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, arrayList);
        // APP CURRENTLY CRASHING HERE
        spinnerEndLocation.setAdapter(adp);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void pickTime() {
        long time = timePicker.getDrawingTime();
        int hour = timePicker.getCurrentHour();
        int minutes = timePicker.getCurrentMinute();
        Toast.makeText(getApplicationContext(), Integer.toString(hour) + " : " + Integer.toString(minutes), Toast.LENGTH_LONG).show();

    }

    public ArrayList<String> getEndLocations(String startLocation) {
        ArrayList<String> list = new ArrayList<String>();
        //ArrayList<String>list2=new ArrayList<String>();
        ArrayList endLocations = new ArrayList<String>();

        int index = allBusStops.indexOf(startLocation);
        String rootId = rootIds[index];
        String rootDir = rootDirecion[index];


        if (rootDir.equals("1")) {
            for (int i = index + 1; i < allLocations.length; i++) {
                endLocations.add(allLocations[i]);
            }
        } else {

            for (int i = index - 1; i >= 0; i--) {
                endLocations.add(allLocations[i]);
            }
        }
        return endLocations;
    }

    private void populateAllLocationList() {
        allBusStops = new ArrayList<String>();
        for (String str : allLocations) {
            allBusStops.add(str);
        }
    }

    private class login extends AsyncTask<String, Void, String[]> {

        ProgressDialog dialog = ProgressDialog.show(context, "", "Retrieving results, Please wait...");

        @Override
        protected String[] doInBackground(String... params) {
            Log.i("thread", "Doing Something...");

            try {

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();

                pairs.add(new BasicNameValuePair("from", stringFrom));
                pairs.add(new BasicNameValuePair("to", stringTo));

                post.setHeader("Accept-Encoding", "gzip,deflate");
                post.setHeader("Content-Type", "text/xml;charset=UTF-8");
                post.setHeader("SOAPAction", "urn:putMessage");


                post2.setHeader("Accept-Encoding", "gzip,deflate");
                post2.setHeader("Content-Type", "text/xml;charset=UTF-8");
                post2.setHeader("SOAPAction", "urn:getLastMessage");


                StringEntity stringEntity = new StringEntity("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:app=\"http://app.android.wso2.com\">" +
                        "   <soapenv:Header/>" +
                        "   <soapenv:Body>" +
                        "   <app:putMessage>" +
                        "  <app:message>minudika</app:message>" +
                        "   </app:putMessage>" +
                        "   </soapenv:Body>" +
                        "   </soapenv:Envelope>", "UTF-8");
                stringEntity.setContentType("text/xml");


                StringEntity stringEntity2 = new StringEntity("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:app=\"http://app.android.wso2.com\">" +
                        "   <soapenv:Header/>" +
                        "   <soapenv:Body>" +
                        "      <app:getLastMessage/>" +
                        "   </soapenv:Body>" +
                        "</soapenv:Envelope>", "UTF-8");
                stringEntity.setContentType("text/xml");


                post.setEntity(stringEntity);
                post2.setEntity(stringEntity2);


                client.execute(post);
                HttpResponse response = client.execute(post2);
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity e = response.getEntity();
                    String data = EntityUtils.toString(e);
                    xmlParser = new XMLParser(data);
                    results = xmlParser.getData();
                    //JSONObject last = new JSONObject(data);
                    dialog.dismiss();
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }


}
