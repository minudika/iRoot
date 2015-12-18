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
      //s  allLocations=getResources().getStringArray(R.array.allLocations);



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
                populateEndLocaitonSpinner(getRootLocations(startLocation));
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

    public ArrayList<String> getRootLocations(String startLocation){
        ArrayList<String>list1=new ArrayList<String>();
        ArrayList<String>list2=new ArrayList<String>();
        ArrayList locations=new ArrayList<String>();

        for(String s:root1){
            list1.add(s);
        }
        for(String s:root2){
            list2.add(s);
        }

        if(list1.indexOf(startLocation)!=-1){
            int index=list1.indexOf(startLocation);
            for(int i=index+1;i<list1.size();i++){
                locations.add(list1.get(i));
            }
            return locations;
        }

        if(list2.indexOf(startLocation)!=-1){
            int index=list2.indexOf(startLocation);
            for(int i=index+1;i<list2.size();i++){
                locations.add(list2.get(i));
            }
            return locations;
        }

        return locations;
    }
}
