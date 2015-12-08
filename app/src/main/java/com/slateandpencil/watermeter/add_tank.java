package com.slateandpencil.watermeter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class add_tank extends AppCompatActivity {

    File database;
    EditText tank_name,ip_addr,port_no;
    SwitchCompat notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database=this.getDatabasePath("watermeter");
        tank_name=(EditText)findViewById(R.id.input_name);;
        ip_addr=(EditText)findViewById(R.id.input_ip);;
        port_no=(EditText)findViewById(R.id.input_port);
        notification=(SwitchCompat)findViewById(R.id.Switch);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        SQLiteDatabase sb;
        switch (item.getItemId()){
            case R.id.save:
                if(!database.exists()){
                   sb=openOrCreateDatabase("watermeter", MODE_PRIVATE, null);
                    sb.execSQL("CREATE TABLE IF NOT EXISTS `tank` (\n" +
                            "  `num` number(10),\n" +
                            "  `name` varchar(100),\n" +
                            "  `ip` varchar(100) ,\n" +
                            "  `port` varchar(100),\n" +
                            "  `enable` number(1)\n" +
                            ");");
                }
                else {
                    sb=openOrCreateDatabase("watermeter", MODE_PRIVATE, null);
                }
                String name=tank_name.getText().toString();
                String ip=ip_addr.getText().toString();
                String port=port_no.getText().toString();
                int enable;
                int count=0;
                Cursor resultSet=null;
                if(notification.isChecked())
                    enable=1;
                else
                    enable=0;
                resultSet=sb.rawQuery("Select count(*) from  tank", null);
                while (resultSet.moveToNext()) {
                    count = Integer.parseInt(resultSet.getString(0));
                }
                sb.execSQL("INSERT INTO 'tank' ('num','name','ip','port','enable') VALUES\n"+
                "('"+(count+1)+"','"+name+"','"+ip+"','"+port+"','"+enable+"');");

                Toast.makeText(add_tank.this, "Added New Tank with no", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(add_tank.this, "Changes Discarded", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(add_tank.this, "Changes Discarded", Toast.LENGTH_SHORT).show();

        }
       /* intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
        return true;//return super.onOptionsSelected(MenuItem item);



    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Toast.makeText(add_tank.this, "Changes Discarded", Toast.LENGTH_SHORT).show();
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
    }

}
