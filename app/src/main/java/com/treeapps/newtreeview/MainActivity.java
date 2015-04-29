package com.treeapps.newtreeview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
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

    public static class SessionDataFragment extends Fragment {

        Context context;

        public Treeview treeview;
        public Map<Integer, Drawable> iconImages;

        public Note note = new Note();

        public void SessionDataFragment() {
        }

        // This method is only called once for this fragment
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // retain this fragment
            setRetainInstance(true);
        }
    }

    SessionDataFragment sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        sd = (SessionDataFragment) fragmentManager.findFragmentByTag("DATA");
        if (sd == null) {
            sd = new SessionDataFragment();
            fragmentManager.beginTransaction().add(sd, "DATA").commit();
        }
        sd.context = this;

        if (savedInstanceState == null) {

            // Set up test values
            sd.iconImages = new HashMap<Integer, Drawable>();
            sd.iconImages.put(EnumIconImageId.EMPTY.getValue(), getResources().getDrawable(R.mipmap.item_empty));
            sd.iconImages.put(EnumIconImageId.COLLAPSED.getValue(), getResources().getDrawable(R.mipmap.item_collapsed));
            sd.iconImages.put(EnumIconImageId.EXPANDED.getValue(), getResources().getDrawable(R.mipmap.item_expanded));

            NoteNode noteNode1 = new NoteNode("One");
            NoteNode noteNode2 = new NoteNode("Two");
            NoteNode noteNode21 = new NoteNode("TwoOne");
            NoteNode noteNode22 = new NoteNode("TwoTwo");
            noteNode2.getChildNodes().add(noteNode21);
            noteNode2.getChildNodes().add(noteNode22);

            sd.note.getChildNodes().add(noteNode1);
            sd.note.getChildNodes().add(noteNode2);


            for (int i = 0; i < 20; i++) {
                NoteNode noteNode = new NoteNode("Node " + i);
                sd.note.getChildNodes().add(noteNode);
            }


            // Setup components
            sd.treeview = (Treeview) findViewById(R.id.treeview);
            sd.treeview.setParameters(R.layout.treenode_item, sd.iconImages, 10, getResources().getColor(R.color.treeview_select_color),
                    getResources().getInteger(R.integer.treeview_text_size_in_dp),
                    getResources().getInteger(R.integer.treeview_indent_radius_in_dp));
            sd.treeview.setOnSelectionChangedListener(new Treeview.OnSelectionChangedListener() {
                @Override
                public void onChange(boolean boolIsSelected, TreeviewNode treeviewNode) {
                    NoteNode noteNode = (NoteNode) treeviewNode.getTag();
                    noteNode.setSelected(boolIsSelected);
                }
            });
            sd.treeview.setOnCheckChangedListener(new Treeview.OnCheckChangedListener() {
                @Override
                public void onChange(boolean boolIsChecked, TreeviewNode treeviewNode) {
                    NoteNode noteNode = (NoteNode) treeviewNode.getTag();
                    noteNode.setChecked(boolIsChecked);
                }
            });
            sd.treeview.setOnHideCheckChangedListener(new Treeview.OnHideCheckChangedListener() {
                @Override
                public void onChange(boolean boolIsHidden, TreeviewNode treeviewNode) {
                    NoteNode noteNode = (NoteNode) treeviewNode.getTag();
                    noteNode.setHidden(boolIsHidden);
                }
            });

            TreeAdapter treeAdapter = new TreeAdapter<NoteNode>(sd.note.getChildNodes()) {
                @Override
                public TreeviewNode convertSourceToTreeNode(NoteNode sourceNode) {
                    TreeviewNode treeviewNode = new TreeviewNode(sd.treeview,
                            sourceNode.getDescription(),
                            sourceNode.isSelected(),
                            sourceNode.isChecked(),
                            sourceNode.isHidden(),
                            sourceNode);
                    return treeviewNode;
                }
            };
            sd.treeview.setAdapter(treeAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fragmentManager = getFragmentManager();
        sd = (SessionDataFragment) fragmentManager.findFragmentByTag("DATA");
        if (sd == null) {
            sd = new SessionDataFragment();
            fragmentManager.beginTransaction().add(sd, "DATA").commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem toggleChecklist = menu.findItem(R.id.action_toggle_checklist);
        if (sd.treeview.isCheckList()) {
            toggleChecklist.setTitle("Unselect checklist");
        } else {
            toggleChecklist.setTitle("Select checklist");
        }

        MenuItem toggleHideMode = menu.findItem(R.id.action_toggle_hidden_mode);
        if (sd.treeview.isHiddenModeEnabled()) {
            toggleHideMode.setTitle("Unselect hide mode");
        } else {
            toggleHideMode.setTitle("Select hide mode");
        }

        MenuItem toggleHideSelectionActive = menu.findItem(R.id.action_toggle_hide_selection_active);
        if (sd.treeview.isHiddenSelectionActive()) {
            toggleHideSelectionActive.setTitle("Unselect hide selection");
        } else {
            toggleHideSelectionActive.setTitle("Select hide selection");
        }
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

    }

    public void onMenuToggleChecklistClick(MenuItem item) {
        sd.treeview.setCheckList(!sd.treeview.isCheckList());
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuToggleHideModeClick(MenuItem item) {
        sd.treeview.setHiddenModeEnabled(!sd.treeview.isHiddenModeEnabled());
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuToggleHideHiddenClick(MenuItem item) {
        sd.treeview.setHiddenSelectionIsActive(!sd.treeview.isHiddenSelectionActive());
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuAddSameLevelClick(MenuItem item) {

    }

    public void onMenuAddNextLevelClick(MenuItem item) {
        NoteNode noteNodeNew = new NoteNode("Brand new node " + new Date().toString());

        TreeviewNode treeviewNodeSelected = sd.treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                noteNodeSelected.getChildNodes().add(noteNodeNew);
            }
        }
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuDeleteClick(MenuItem item) {
    }
}
