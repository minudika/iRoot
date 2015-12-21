package wso2.org.iroot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainWindow extends Activity {
    Spinner spinnerStartLocation;
    Spinner spinnerEndLocation;
    Button btnSubmit;
    TimePicker timePicker;
    Context context=this;
    String[]root1;
    String[] root2;
    String[] allLocations;
    String[] busStopIds;
    String[] rootDirecion;
    String[] rootIds;
    ArrayList<String>allBusStops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        spinnerStartLocation=(Spinner) findViewById(R.id.spinner_from);
        spinnerEndLocation=(Spinner) findViewById(R.id.spinner_to);
        btnSubmit=(Button) findViewById(R.id.btnSubmit);
        timePicker=(TimePicker)findViewById(R.id.timePicker);

        root1=getResources().getStringArray(R.array.root1);
        root2=getResources().getStringArray(R.array.root2);
        allLocations=getResources().getStringArray(R.array.busStopNames);
        busStopIds = getResources().getStringArray(R.array.busStopIds);
        rootDirecion = getResources().getStringArray(R.array.rootDirection);
        rootIds = getResources().getStringArray(R.array.rootIds);

        populateAllLocationList();
        populateStartLocaionSpinner(allBusStops);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // populateSpinner();
                pickTime();
            }
        });

        spinnerStartLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                String startLocation = parent.getItemAtPosition(position).toString();
                populateEndLocaitonSpinner(getEndLocations(startLocation));
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void populateEndLocaitonSpinner(ArrayList<String>arrayList){
        /*ArrayList<String> arrayList=new ArrayList<String>();

        arrayList.add("Moratuwa");
        arrayList.add("Mount Lavenia");
        arrayList.add("Dehiwala");
        arrayList.add("Wellawatte");
        arrayList.add("Bambalapitiya");
*/
        ArrayAdapter<String> adp = new ArrayAdapter<String> (context,android.R.layout.simple_spinner_dropdown_item,arrayList);
        // APP CURRENTLY CRASHING HERE
        spinnerEndLocation.setAdapter(adp);
        //Set listener Called when the item is selected in spinner
        spinnerEndLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                String city = parent.getItemAtPosition(position).toString();
               // Toast.makeText(parent.getContext(), city, Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


    }

    public void populateStartLocaionSpinner(ArrayList<String>arrayList){
        ArrayAdapter<String> adp = new ArrayAdapter<String> (context,android.R.layout.simple_spinner_dropdown_item,arrayList);
        // APP CURRENTLY CRASHING HERE
        spinnerEndLocation.setAdapter(adp);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void pickTime(){
        long time=timePicker.getDrawingTime();
        int hour=timePicker.getCurrentHour();
        int minutes=timePicker.getCurrentMinute();
        Toast.makeText(getApplicationContext(),Integer.toString(hour)+" : "+Integer.toString(minutes),Toast.LENGTH_LONG).show();

    }

    public ArrayList<String> getEndLocations(String startLocation){
        ArrayList<String> list=new ArrayList<String>();
        //ArrayList<String>list2=new ArrayList<String>();
        ArrayList endLocations=new ArrayList<String>();

        int index = allBusStops.indexOf(startLocation);
        String rootId = rootIds[index];

        for(int i=0; i<allLocations.length; i++){
            if(rootIds[i].equals(rootId) && !allLocations[i].equals(startLocation)){
                endLocations.add(allLocations[i]);
            }
        }
        return endLocations;
    }

    private void populateAllLocationList(){
        allBusStops = new ArrayList<String>();
        for(String str:allLocations){
            allBusStops.add(str);
        }
    }
}
