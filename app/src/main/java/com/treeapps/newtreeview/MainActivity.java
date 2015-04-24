package com.treeapps.newtreeview;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import note.NoteNode;
import treeview.TreeviewNode;
import treeview.Treeview;
import treeview.TreeviewUtils;


public class MainActivity extends ActionBarActivity {


    public static enum EnumIconImageId {
        EMPTY(0), COLLAPSED(1), EXPANDED(2);
        final int numTab;
        private EnumIconImageId(int num) {
            this.numTab = num;
        }
        public int getValue() {
            return this.numTab;
        }
    }

    Treeview treeview;
    Map<Integer,Drawable> iconImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Set up values
            iconImages = new HashMap<Integer,Drawable>();
            iconImages.put(EnumIconImageId.EMPTY.getValue(), getResources().getDrawable(R.mipmap.item_empty));
            iconImages.put(EnumIconImageId.COLLAPSED.getValue(), getResources().getDrawable(R.mipmap.item_collapsed));
            iconImages.put(EnumIconImageId.EXPANDED.getValue(), getResources().getDrawable(R.mipmap.item_expanded));

            // Setup components
            ListView treeviewListView = (ListView) findViewById(R.id.treeview_listview);
            treeview = new Treeview(this, treeviewListView, R.layout.treenode_item, iconImages);
            TreeviewNode treeviewNode1 = new TreeviewNode("One");
            TreeviewNode treeviewNode2 = new TreeviewNode("Two");
            TreeviewNode treeviewNode21 = new TreeviewNode("TwoOne");
            TreeviewNode treeviewNode22 = new TreeviewNode("TwoTwo");
            treeviewNode2.addNode(treeviewNode21);
            treeviewNode2.addNode(treeviewNode22);
            treeview.addNode(treeviewNode1);
            treeview.addNode(treeviewNode2);
            treeview.setIconImages(iconImages);
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

    public void onMenuTestClick(MenuItem item) {
        TreeviewNode treeviewNode = treeview.getSelected();
        if (treeviewNode != null) {
            treeviewNode.toggleExpansionState();
        }
    }
}
