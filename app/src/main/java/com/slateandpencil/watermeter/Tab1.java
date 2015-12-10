package com.slateandpencil.watermeter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import static android.R.layout.simple_list_item_1;
import static android.database.sqlite.SQLiteDatabase.openDatabase;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by Akhil on 21-11-2015.
 */
public class Tab1 extends Fragment {
    ListView lv;
    SQLiteDatabase sb = openOrCreateDatabase("watermeter",MainActivity.MODE_PRIVATE,null);
    Cursor resultSet=null;
    public Tab1()
    {

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Boolean flag=false;
        TextView t=(TextView)getView().findViewById(R.id.empty_list);
        lv = (ListView) getView().findViewById(R.id.listView);
        ArrayList<String> x= new ArrayList<String>();

        resultSet=sb.rawQuery("Select name from tank;",null);
        while(resultSet.moveToNext()){
            x.add(resultSet.getString(0));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), simple_list_item_1, x);
        if(flag)
            t.setText("");
        else
            t.setText("Nothing to show");

        lv.setAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab1, container, false);
    }
}
