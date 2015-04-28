package com.treeapps.newtreeview;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import note.Note;
import note.NoteNode;
import treeview.TreeAdapter;
import treeview.TreeviewNode;
import treeview.Treeview;


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

    Note note = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            // Set up test values
            iconImages = new HashMap<Integer,Drawable>();
            iconImages.put(EnumIconImageId.EMPTY.getValue(), getResources().getDrawable(R.mipmap.item_empty));
            iconImages.put(EnumIconImageId.COLLAPSED.getValue(), getResources().getDrawable(R.mipmap.item_collapsed));
            iconImages.put(EnumIconImageId.EXPANDED.getValue(), getResources().getDrawable(R.mipmap.item_expanded));

            NoteNode noteNode1 = new NoteNode("One");
            NoteNode noteNode2 = new NoteNode("Two");
            NoteNode noteNode21 = new NoteNode("TwoOne");
            NoteNode noteNode22 = new NoteNode("TwoTwo");
            noteNode2.getChildNodes().add(noteNode21);
            noteNode2.getChildNodes().add(noteNode22);

            note.getChildNodes().add(noteNode1);
            note.getChildNodes().add(noteNode2);


            for (int i = 0; i < 20; i++) {
                NoteNode noteNode = new NoteNode("Node " + i);
                note.getChildNodes().add(noteNode);
            }


            // Setup components
            treeview = (Treeview) findViewById(R.id.treeview);
            treeview.setParameters(R.layout.treenode_item, iconImages, 10, getResources().getColor(R.color.treeview_select_color),
                    getResources().getInteger(R.integer.treeview_text_size_in_dp),
                    getResources().getInteger(R.integer.treeview_indent_radius_in_dp));
            treeview.setIsCheckList(true);
            TreeAdapter treeAdapter = new TreeAdapter<NoteNode>(note.getChildNodes()) {
                @Override
                public TreeviewNode convertSourceToTreeNode(NoteNode sourceNode) {
                    return new TreeviewNode(sourceNode.getDescription()) ;
                }
            };
            treeview.setAdapter(treeAdapter);
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
        TreeviewNode treeviewNode = treeview.getSelectedFirst();
        if (treeviewNode != null) {
            treeviewNode.toggleExpansionState();
        }
    }
}
