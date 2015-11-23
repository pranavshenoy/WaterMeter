package com.slateandpencil.watermeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class add_tank extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        switch (item.getItemId()){
            case R.id.save:
                Toast.makeText(add_tank.this, "Added New Tank", Toast.LENGTH_SHORT).show();
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
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
    }

}
