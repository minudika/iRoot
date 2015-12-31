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

/*import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;*/

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
    ArrayList<String> allBusStops;
    String stringFrom, stringTo;

    //--------------------------------
    private final String NAMESPACE = "http://www.w3schools.com/webservices/";
    private final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
    private final String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
    private final String METHOD_NAME = "CelsiusToFahrenheit";
    private String TAG = "MINUDIKA";
    private static String celcius;
    private static String fahren;
    //--------------------------------

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // populateSpinner();
                //pickTime();
                //startActivity(new Intent(context,MainWindow.class));
                new login().execute("");
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
        /*ArrayList<String> arrayList=new ArrayList<String>();

        arrayList.add("Moratuwa");
        arrayList.add("Mount Lavenia");
        arrayList.add("Dehiwala");
        arrayList.add("Wellawatte");
        arrayList.add("Bambalapitiya");
*/
        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, arrayList);
        // APP CURRENTLY CRASHING HERE
        spinnerEndLocation.setAdapter(adp);
        //Set listener Called when the item is selected in spinner
        spinnerEndLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                String endLocation = parent.getItemAtPosition(position).toString();
                stringTo = endLocation;
                // Toast.makeText(parent.getContext(), city, Toast.LENGTH_LONG).show();
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


        /*for (int i = 0; i < allLocations.length; i++) {
            if (rootIds[i].equals(rootId) && !allLocations[i].equals(startLocation)) {
                endLocations.add(allLocations[i]);
            }
        }*/

        if(rootDir.equals("1")){
            for(int i=index+1; i<allLocations.length; i++){
                endLocations.add(allLocations[i]);
            }
        }
        else{

            for(int i=index-1; i>=0; i--){
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

   /* private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            // getFahrenheit(celcius);
            getResponse();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //tv.setText(fahren + "° F");
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            //tv.setText("Calculating...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }
    }*/

    /*public void getResponse() {
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        //Set Name
        celsiusPI.setName("Celsius");
        //Set Value
        //   celsiusPI.setValue(celsius);
        //Set dataType
        celsiusPI.setType(double.class);
        //Add the property to request object
        request.addProperty(celsiusPI);
        //Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            //Invole web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            //Assign it to fahren static variable
            fahren = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private class login extends AsyncTask<String, Void, JSONObject> {

        ProgressDialog dialog = ProgressDialog.show(context, "", "Retrieving results, Please wait...");

        @Override
        protected JSONObject doInBackground(String... params) {
            Log.i("thread", "Doing Something...");

            try {

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();

                pairs.add(new BasicNameValuePair("from", stringFrom));
                pairs.add(new BasicNameValuePair("to", stringTo));

                post.setHeader("Accept-Encoding", "gzip,deflate");
                post.setHeader("Content-Type", "text/xml;charset=UTF-8");
                post.setHeader("SOAPAction", "urn:putMessage");
             //  post.setHeader("Content-Length","309");
//                post.setHeader("Host","10.100.4.177:9443");
//                post.setHeader("User-Agent","Apache-HttpClient/4.1.1 (java 1.5)");

                post2.setHeader("Accept-Encoding", "gzip,deflate");
                post2.setHeader("Content-Type", "text/xml;charset=UTF-8");
                post2.setHeader("SOAPAction", "urn:getLastMessage");
            //    post.setHeader("Content-Length","222");

              /* StringEntity stringEntity = new StringEntity("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:app=\"http://app.android.wso2.com\">" +
                        "   <soapenv:Header/>" +
                        "   <soapenv:Body>" +
                        "      <app:getLastMessage/>" +
                        "   </soapenv:Body>" +
                        "</soapenv:Envelope>", "UTF-8");
                stringEntity.setContentType("text/xml");*/

                StringEntity stringEntity = new StringEntity("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:app=\"http://app.android.wso2.com\">"+
                "   <soapenv:Header/>"+
                "   <soapenv:Body>"+
                "   <app:putMessage>"+
                "  <app:message>minudika</app:message>"+
                "   </app:putMessage>"+
                "   </soapenv:Body>"+
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

            //post.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response1 = client.execute(post);
                HttpResponse response = client.execute(post2);
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity e = response.getEntity();
                    String data = EntityUtils.toString(e);
                    JSONObject last = new JSONObject(data);
                    dialog.dismiss();
                    return last;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            //dialog.dismiss();
            Log.i("thread", "Started...");
            dialog.show();
        }

        protected void onPostExecute(JSONObject result) {
            Log.i("thread", "Done...");
            String from;
            String to;
            try {
                from = result.getString("from");
                to = result.getString("to");

                if (dialog != null) {
                    dialog.dismiss();
                } else {
                }


                //   tv.setText("Login Successful...");

                Bundle newbundle = new Bundle();
                newbundle.putString("from", from);
                newbundle.putString("to", to);

                Intent myIntent = new Intent(context, DisplayResults.class);
                myIntent.putExtras(newbundle);

                startActivity(myIntent);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
