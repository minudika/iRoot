package wso2.org.iroot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.transform.Result;

import wso2.org.utils.XMLParser;


public class MainWindow extends Activity {
    Spinner spinnerStartLocation;
    Spinner spinnerEndLocation;
    Spinner spinner;
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

       /* spinnerStartLocation = (Spinner) findViewById(R.id.spinner_from);
        spinnerEndLocation = (Spinner) findViewById(R.id.spinner_to);*/
       /* btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnPickTime = (Button) findViewById(R.id.btnPickTime);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        txtViewDisplayTime = (TextView) findViewById(R.id.txtView_displayTime);
        autoCompleteTextView_to=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView_to);*/
        autoCompleteTextView_from=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView_from);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnPickTime=(Button)findViewById(R.id.btnPickTime);
        txtViewDisplayTime = (TextView)findViewById(R.id.textView_displayTime);
        btnSubmit = (Button) findViewById(R.id.button_submit);
        //timePicker.setIs24HourView(true);
        root_1 = getResources().getStringArray(R.array.root_1);
        root_2 = getResources().getStringArray(R.array.root_2);
        allLocations = getResources().getStringArray(R.array.busStopNames);
        busStopIds = getResources().getStringArray(R.array.busStopIds);
        rootDirecion = getResources().getStringArray(R.array.rootDirection);
        rootIds = getResources().getStringArray(R.array.rootIds);

        calendar=Calendar.getInstance();
        setFromResources();
        populateAllLocationList();
        //populateStartLocaionSpinner(allBusStops);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        setTime(hour,minute);
        //setup();


        client = new DefaultHttpClient();
        //String url="https://10.100.4.177:9443/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint/";
        //http://10.100.5.59:9783/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint/
        //String url = "http://10.0.2.2:9763/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint/";
        String url = "http://ec2-52-77-236-192.ap-southeast-1.compute.amazonaws.com:9763/services/AndroidAppService.AndroidAppServiceHttpSoap11Endpoint";
        String url2 ="http://ec2-52-77-236-192.ap-southeast-1.compute.amazonaws.com:9763/endpoints/MobileRequestReceiver";
        post = new HttpPost(url2);
        post2 = new HttpPost(url);
        final Intent intent = new Intent(context, DisplayResults.class);
       // autoCompleteTextView_to.setEnabled(false);
        spinner.setEnabled(false);

        // Get a reference to the AutoCompleteTextView in the layout

         // Get the string array
        String[] allLocations = getResources().getStringArray(R.array.busStopNames);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter_from =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allLocations);
        autoCompleteTextView_from.setAdapter(adapter_from);


        autoCompleteTextView_from.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // performSearch();
                    if (validateStartLocations(stringFrom)) {

                    }
                    return true;
                }
                return false;
            }
        });

        autoCompleteTextView_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(),"done",Toast.LENGTH_LONG).show();
                stringFrom = autoCompleteTextView_from.getText().toString();
                populateDestinationList();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringTo = spinner.getItemAtPosition(position).toString();
                View v = spinner.getSelectedView();
                ((TextView) v).setTextColor(-1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // populateSpinner();
                //pickTime();
                //startActivity(new Intent(context,MainWindow.class));
                new login().execute("");

                /*Bundle bundle = new Bundle();
                bundle.putStringArray("array", results);
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });

       /* spinnerStartLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                String startLocation = parent.getItemAtPosition(position).toString();
                stringFrom = startLocation;
                populateEndLocaitonSpinner(getEndLocations(startLocation));
                View v = spinnerStartLocation.getSelectedView();
                ((TextView) v).setTextColor(-1);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //Toast.makeText(getBaseContext(), Integer.toString(hourOfDay), Toast.LENGTH_LONG).show();
                setup();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
            }
        });*/


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
                        setTime(hourOfDay,minute);

                    }
                },hour,minute,false);

                mTimePicker.setTitle("Select Time");

                mTimePicker.show();
            }
        });


    }

    private boolean validateStartLocations(String s){
        if(root1.indexOf(s) == -1 && root2.indexOf(s) == -1){
            return false;
        }
        return true;
    }

    private boolean validateEndLocations(String s,ArrayList arrayList){
        if(arrayList.indexOf(s)==-1){
            return false;
        }
        return true;
    }


    private void setTime(int hourOfDay,int minute){
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
    }

    public void setup(){
        Resources system = Resources.getSystem();

        // This is the internal id of the EditText used in NumberPicker (hack)
        int mNumberPickerInputId =
                system.getIdentifier("numberpicker_input", "id", "android");



        final int hourSpinnerId =
                system.getIdentifier("hour", "id", "android");
        View hourSpinner = timePicker.findViewById(hourSpinnerId);
        if (hourSpinner != null) {
            setNumberPickerTextColor(hourSpinner, Color.WHITE, mNumberPickerInputId);
           // changeSpinnerTextColor((Spinner) hourSpinner, -1);
        }

        final int minSpinnerId =
                system.getIdentifier("minute", "id", "android");
        View minSpinner = timePicker.findViewById(minSpinnerId);
        if (minSpinner != null) {
            setNumberPickerTextColor(minSpinner, Color.WHITE,mNumberPickerInputId);
            //changeSpinnerTextColor((Spinner)minSpinner,-1);
        }

        final int amPmSpinnerId =
                system.getIdentifier("amPm", "id", "android");
        View amPmSpinner = timePicker.findViewById(amPmSpinnerId);
        if (amPmSpinner != null) {
            setNumberPickerTextColor(amPmSpinner, Color.WHITE, mNumberPickerInputId);
           // changeSpinnerTextColor((Spinner) amPmSpinner, -1);
        }
    }
    private void setNumberPickerTextColor(View spinner, int color,int id) {
        TextView input = (TextView) spinner.findViewById(id);
        //TextView input = (TextView) ((Spinner)spinner).getSelectedView();
        input.setTextColor(color);
    }

    private void changeSpinnerTextColor(Spinner spinner,int color){
        View v = spinner.getSelectedView();
        ((TextView) v).setTextColor(color);
    }

    private void populateDestinationList(){
        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, getEndLocations(stringFrom));

        spinner.setEnabled(true);
        spinner.setAdapter(adp);
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
                View v= spinnerEndLocation.getSelectedView();
                ((TextView)v).setTextColor(-1);

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

    /*public ArrayList<String> getEndLocations(String startLocation) {
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
    }*/

    public ArrayList<String> getEndLocations(String startLocation) {
        ArrayList<String> list = new ArrayList<String>();
        //ArrayList<String>list2=new ArrayList<String>();
        ArrayList endLocations = new ArrayList<String>();

        //int index = allBusStops.indexOf(startLocation);
        //int index1 = root1.indexOf(startLocation);
        // int index2 = root2.indexOf(startLocation);
        int index1,index2;

        int index1_1,index2_1;
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
                        endLocations.add(root25_1[i]);
                    }
                    break;
                case 29:
                    for (int i = index1 + 1; i < root29_1.length; i++) {
                        endLocations.add(root29_1[i]);
                    }
                    break;
                case 38:
                    for (int i = index1 + 1; i < root38_1.length; i++) {
                        endLocations.add(root38_1[i]);
                    }
                    break;
            }
        }

        if(index2!=-1){
            switch(rootId){
                case 25:
                    for (int i = index2 + 1; i < root25_2.length; i++) {
                        endLocations.add(root25_2[i]);
                    }
                    break;
                case 29:
                    for (int i = index2 + 1; i < root29_2.length; i++) {
                        endLocations.add(root29_2[i]);
                    }
                    break;
                case 38:
                    for (int i = index2 + 1; i < root38_2.length; i++) {
                        endLocations.add(root38_2[i]);
                    }
                    break;
            }
        }


        /*if(index2!=-1){
            for (int i = index2 - 1; i >= 0; i--) {
                endLocations.add(root_2[i]);
            }
        }

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
        }*/
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
                        "  <app:message>" +
                        " <from>"+stringFrom+"</from>"+
                        " <to>"+stringTo+"</to>"+
                        " <time>12</time>"+
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
