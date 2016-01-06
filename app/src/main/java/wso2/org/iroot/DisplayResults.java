package wso2.org.iroot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class DisplayResults extends AppCompatActivity {

    TextView tView1,tView2,tView3,tView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tView1 = (TextView) findViewById(R.id.txtView_from);
        tView2 = (TextView) findViewById(R.id.txtView_to);
        tView3 = (TextView) findViewById(R.id.txtView_busNow);
        tView4 = (TextView) findViewById(R.id.txtView_busOneHr);

        Bundle b=getIntent().getExtras();

        String[] results=b.getStringArray("results");

        tView1.setText(results[0]);
        tView2.setText(results[1]);
       // tView3.setText(results[2]);

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
