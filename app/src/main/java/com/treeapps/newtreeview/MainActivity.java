package com.treeapps.newtreeview;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Map;

import treeview.Treenode;
import treeview.Treeview;


public class MainActivity extends ActionBarActivity {

    Treeview treeview;
    Map<Integer,Drawable> iconImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            ListView treeviewListView = (ListView) findViewById(R.id.treeview_listview);
            treeview = new Treeview(this, treeviewListView, R.layout.treenode_item, iconImages);
            Treenode treenode1 = new Treenode("One");
            Treenode treenode2 = new Treenode("Two");
            Treenode treenode21 = new Treenode("TwoOne");
            Treenode treenode22 = new Treenode("TwoTwo");
            treenode2.addNode(treenode21);
            treenode2.addNode(treenode22);
            treeview.addNode(treenode1);
            treeview.addNode(treenode2);
            treeview.invalidate();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
