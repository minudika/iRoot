package wso2.org.iroot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DisplayResults extends AppCompatActivity {

    TextView tView1,tView2,tView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tView1 = (TextView) findViewById(R.id.textView4);
        tView2 = (TextView) findViewById(R.id.textView5);
        tView3 = (TextView) findViewById(R.id.textView6);

        Bundle b=getIntent().getExtras();

        String[] results=b.getStringArray("results");

        tView1.setText(results[0]);
        //tView2.setText(str2);
       // tView3.setText(str3);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
