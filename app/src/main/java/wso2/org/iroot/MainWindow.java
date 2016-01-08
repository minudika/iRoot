package wso2.org.iroot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import wso2.org.utils.XMLParser;


public class MainWindow extends Activity {
    Spinner spinnerStartLocation;
    Spinner spinnerEndLocation;
    Spinner spinnerFrom,spinnerTo;
    Button btnSubmit,btnPickTime;
    TimePicker timePicker;
    AutoCompleteTextView autoCompleteTextView_from,autoCompleteTextView_to;
    TextView txtViewDisplayTime;
    Context context = this;
    String[] root_1;
    String[] root_2;
    String[] root25_1,root25_2,root29_1,root29_2,root38_1,root38_2;
    String[] allLocations;
    String[] busStopIds;
    String[] rootDirecion;
    String[] rootIds;
    String[] results;
    String currentTime;
    ArrayList<String> allBusStops;
    Calendar calendar;

    ArrayList<String> root1;
    ArrayList<String> root2;
    ArrayList<String> root1_25;
    ArrayList<String> root1_29;
    ArrayList<String> root1_38;
    ArrayList<String> root2_25;
    ArrayList<String> root2_29;
    ArrayList<String> root2_38;
    String stringFrom,stringTo;
    XMLParser xmlParser;




    HttpClient client;
    HttpPost post;
    HttpPost post2;

    //----------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        autoCompleteTextView_from=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView_from);
        spinnerTo = (Spinner) findViewById(R.id.spinner_to);
        spinnerFrom = (Spinner) findViewById(R.id.spinner_from);
        btnPickTime=(Button)findViewById(R.id.btnPickTime);
        txtViewDisplayTime = (TextView)findViewById(R.id.textView_displayTime);
        btnSubmit = (Button) findViewById(R.id.button_submit);

        root_1 = getResources().getStringArray(R.array.root_1);
        root_2 = getResources().getStringArray(R.array.root_2);
        allLocations = getResources().getStringArray(R.array.busStopNames);
        busStopIds = getResources().getStringArray(R.array.busStopIds);
        rootDirecion = getResources().getStringArray(R.array.rootDirection);
        rootIds = getResources().getStringArray(R.array.rootIds);

        calendar=Calendar.getInstance();
        setFromResources();
        populateAllLocationList();

        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        currentTime = setTime(hour,minute);




        client = new DefaultHttpClient();
        //String url="https://10.100.4.177:9443/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint/";
        //http://10.100.5.59:9783/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint/
        //String url = "http://10.0.2.2:9763/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint/";
        String url = "http://ec2-52-77-236-192.ap-southeast-1.compute.amazonaws.com:9763/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint";
        String url2 ="http://ec2-52-77-236-192.ap-southeast-1.compute.amazonaws.com:9763/endpoints/MobileRequestReceiver";
        post = new HttpPost(url2);
        post2 = new HttpPost(url);


        spinnerTo.setEnabled(false);

        // Get a reference to the AutoCompleteTextView in the layout

         // Get the string array
        String[] allLocations = getResources().getStringArray(R.array.busStopNames);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter_from =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allLocations);
        //autoCompleteTextView_from.setAdapter(adapter_from);
        spinnerFrom.setAdapter(adapter_from);



        autoCompleteTextView_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(),"done",Toast.LENGTH_LONG).show();
                stringFrom = autoCompleteTextView_from.getText().toString();
                btnSubmit.setEnabled(false);
                populateDestinationList();

            }
        });

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringFrom = spinnerFrom.getItemAtPosition(position).toString();
                btnSubmit.setEnabled(false);
                populateDestinationList();
                View v = spinnerFrom.getSelectedView();
                ((TextView) v).setTextColor(-1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringTo = spinnerTo.getItemAtPosition(position).toString();
                View v = spinnerTo.getSelectedView();
                ((TextView) v).setTextColor(-1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Task().execute("");
            }
        });


        btnPickTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker

                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context,TimePickerDialog.THEME_HOLO_DARK, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        currentTime = setTime(hourOfDay,minute);

                    }
                },hour,minute,false);

                mTimePicker.setTitle("Select Time");

                mTimePicker.show();
            }
        });


    }






    private String setTime(int hourOfDay,int minute){
        String h="";
        String m="";
        String amPm="";
        if(hourOfDay <12){
            h=Integer.toString(hourOfDay);
            m=Integer.toString(minute);
            h=(h.length()<2)?"0"+h :h;
            m=(m.length()<2)?"0"+m :m;
            amPm="AM";
        }
        else{
            h=Integer.toString(hourOfDay-12);
            m=Integer.toString(minute);
            h=(h.length()<2)?"0"+h :h;
            m=(m.length()<2)?"0"+m :m;
            amPm="PM";
        }
        txtViewDisplayTime.setText("  "+h+":"+m+" "+amPm);
        return (h+":"+m+" "+amPm);
    }



    private boolean populateDestinationList(){

        List list = getEndLocations(stringFrom);
        if(list.size() != 0) {
            ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, getEndLocations(stringFrom));
            spinnerTo.setEnabled(true);
            btnSubmit.setEnabled(true);
            spinnerTo.setAdapter(adp);
            return true;
        }
        else{
            new AlertDialog.Builder(context)
                    .setMessage("No more bus stops on the route!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
            spinnerTo.setEnabled(false);
            autoCompleteTextView_from.setText("");
            btnSubmit.setEnabled(false);
            return false;
        }
    }



    public ArrayList<String> getEndLocations(String startLocation) {

        ArrayList endLocations = new ArrayList<String>();


        int index1,index2;

        int rootId=0;

        if(root1_25.indexOf(startLocation) != -1 || root2_25.indexOf(startLocation) != -1){
            rootId = 25;
            index1 = root1_25.indexOf(startLocation);
            index2 = root2_25.indexOf(startLocation);
        }
        else if(root1_29.indexOf(startLocation) != -1 || root2_29.indexOf(startLocation) != -1){
            rootId = 29;
            index1 = root1_29.indexOf(startLocation);
            index2 = root2_29.indexOf(startLocation);
        }
        else{
            rootId = 38;
            index1 = root1_38.indexOf(startLocation);
            index2 = root1_38.indexOf(startLocation);
        }

        if(index1!=-1){
            switch(rootId){
                case 25:
                    for (int i = index1 + 1; i < root25_1.length; i++) {
                        if(endLocations.indexOf(root25_1[i])==-1) {
                            endLocations.add(root25_1[i]);
                        }
                    }
                    break;
                case 29:
                    for (int i = index1 + 1; i < root29_1.length; i++) {
                        if(endLocations.indexOf(root29_1[i])==-1) {
                            endLocations.add(root29_1[i]);
                        }
                    }
                    break;
                case 38:
                    for (int i = index1 + 1; i < root38_1.length; i++) {
                        if(endLocations.indexOf(root38_1[i])==-1) {
                            endLocations.add(root38_1[i]);
                        }
                    }
                    break;
            }
        }

        if(index2!=-1){
            switch(rootId){
                case 25:
                    for (int i = index2 + 1; i < root25_2.length; i++) {
                        if(endLocations.indexOf(root25_2[i])==-1) {
                            endLocations.add(root25_2[i]);
                        }
                    }
                    break;
                case 29:
                    for (int i = index2 + 1; i < root29_2.length; i++) {
                        if(endLocations.indexOf(root29_2[i])==-1) {
                            endLocations.add(root29_2[i]);
                        }
                    }
                    break;
                case 38:
                    for (int i = index2 + 1; i < root38_2.length; i++) {
                        if(endLocations.indexOf(root38_2[i])==-1) {
                            endLocations.add(root38_2[i]);
                        }
                    }
                    break;
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

    private class Task extends AsyncTask<String, Void, String[]> {

        ProgressDialog dialog = ProgressDialog.show(context, "", "Retrieving results, Please wait...");

        @Override
        protected String[] doInBackground(String... params) {
            Log.i("thread", "Doing Something...");

            try {
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
                        "  <app:message>" +
                        " <from>"+stringFrom+"</from>"+
                        " <to>"+stringTo+"</to>"+
                        " <time>"+currentTime+"</time>"+
                        "</app:message>" +
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
                    dialog.dismiss();
                    return results;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return results;
        }

        protected void onPostExecute(String[] result) {
            Intent i=new Intent(context,DisplayResults.class);
            Bundle bundle = new Bundle();
            bundle.putString("from",stringFrom);
            bundle.putString("to",stringTo);
            bundle.putStringArray("results",result);
            i.putExtras(bundle);
            startActivity(i);
        }



    }

    private void setFromResources(){
        root_1 = getResources().getStringArray(R.array.root_1);
        root_2 = getResources().getStringArray(R.array.root_2);

        root25_1 = getResources().getStringArray(R.array.root25_1);
        root25_2 = getResources().getStringArray(R.array.root25_2);
        root29_1 = getResources().getStringArray(R.array.root29_1);
        root29_2 = getResources().getStringArray(R.array.root29_2);
        root38_1 = getResources().getStringArray(R.array.root38_1);
        root38_2 = getResources().getStringArray(R.array.root38_2);

        allLocations = getResources().getStringArray(R.array.busStopNames);
        busStopIds = getResources().getStringArray(R.array.busStopIds);
        rootDirecion = getResources().getStringArray(R.array.rootDirection);
        rootIds = getResources().getStringArray(R.array.rootIds);


        root1 = new ArrayList<String>();
        root1_25 = new ArrayList<String>();
        root1_29 = new ArrayList<String>();
        root1_38 = new ArrayList<String>();

        root2 = new ArrayList<String>();
        root2_25 = new ArrayList<String>();
        root2_29 = new ArrayList<String>();
        root2_38 = new ArrayList<String>();


        for(String s:root_1){
            root1.add(s);
        }
        for(String s:root_2){
            root2.add(s);
        }
        for(String s:root25_1){
            root1_25.add(s);
        }
        for(String s:root29_1){
            root1_29.add(s);
        }
        for(String s:root38_1){
            root1_38.add(s);
        }
        for(String s:root25_2){
            root2_25.add(s);
        }
        for(String s:root29_2){
            root2_29.add(s);
        }
        for(String s:root38_2){
            root2_38.add(s);
        }

    }


}
