package com.treeapps.newtreeview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import note.Note;
import note.NoteNode;
import treeview.TreeviewAdapter;
import treeview.TreeviewNode;
import treeview.Treeview;
import utils.TreeUtils;


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
    Context context;
    Treeview treeview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        FragmentManager fragmentManager = getFragmentManager();
        sd = (SessionDataFragment) fragmentManager.findFragmentByTag("DATA");
        if (sd == null) {
            sd = new SessionDataFragment();
            fragmentManager.beginTransaction().add(sd, "DATA").commit();
        }


        if (savedInstanceState == null) {

            // Set up user data
            sd.iconImages = new HashMap<Integer, Drawable>();
            sd.iconImages.put(EnumIconImageId.EMPTY.getValue(), getResources().getDrawable(R.mipmap.item_empty));
            sd.iconImages.put(EnumIconImageId.COLLAPSED.getValue(), getResources().getDrawable(R.mipmap.item_collapsed));
            sd.iconImages.put(EnumIconImageId.EXPANDED.getValue(), getResources().getDrawable(R.mipmap.item_expanded));

            NoteNode noteNode1 = new NoteNode("One");
            NoteNode noteNode2 = new NoteNode("Two");
            NoteNode noteNode21 = new NoteNode("TwoOne");
            NoteNode noteNode22 = new NoteNode("TwoTwo");
            noteNode2.addChildNode(noteNode21);
            noteNode2.addChildNode(noteNode22);

            sd.note.getChildNodes().add(noteNode1);
            sd.note.getChildNodes().add(noteNode2);


            for (int i = 0; i < 20; i++) {
                NoteNode noteNode = new NoteNode("Node " + i);
                sd.note.getChildNodes().add(noteNode);
            }

        }
        
        // Setup components
        treeview = (Treeview) findViewById(R.id.treeview);
        treeview.setParameters(R.layout.treenode_item, sd.iconImages, 10, getResources().getColor(R.color.treeview_select_color),
                getResources().getInteger(R.integer.treeview_text_size_in_dp),
                getResources().getInteger(R.integer.treeview_indent_radius_in_dp));
        treeview.setOnSelectionChangedListener(new Treeview.OnSelectionChangedListener() {
            @Override
            public void onChange(boolean boolIsSelected, TreeviewNode treeviewNode) {
                NoteNode noteNode = (NoteNode) treeviewNode.getTag();
                noteNode.setSelected(boolIsSelected);
            }
        });
        treeview.setOnCheckChangedListener(new Treeview.OnCheckChangedListener() {
            @Override
            public void onChange(boolean boolIsChecked, TreeviewNode treeviewNode) {
                NoteNode noteNode = (NoteNode) treeviewNode.getTag();
                noteNode.setChecked(boolIsChecked);
            }
        });
        treeview.setOnHideCheckChangedListener(new Treeview.OnHideCheckChangedListener() {
            @Override
            public void onChange(boolean boolIsHidden, TreeviewNode treeviewNode) {
                NoteNode noteNode = (NoteNode) treeviewNode.getTag();
                noteNode.setHidden(boolIsHidden);
            }
        });

        TreeviewAdapter treeAdapter = new TreeviewAdapter<NoteNode>(sd.note.getChildNodes()) {
            @Override
            public TreeviewNode convertSourceToTreeNode(NoteNode sourceNode) {
                TreeviewNode treeviewNode = new TreeviewNode(treeview,
                        sourceNode.getDescription(),
                        sourceNode.isSelected(),
                        sourceNode.isChecked(),
                        sourceNode.isHidden(),
                        sourceNode.isNew(), sourceNode);
                return treeviewNode;
            }
        };
        treeview.setAdapter(treeAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {


        FragmentManager fragmentManager = getFragmentManager();
        sd = (SessionDataFragment) fragmentManager.findFragmentByTag("DATA");
        if (sd == null) {
            sd = new SessionDataFragment();
            fragmentManager.beginTransaction().add(sd, "DATA").commit();
        }
        super.onResume();

        treeview.invalidate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem toggleChecklist = menu.findItem(R.id.action_toggle_checklist);
        if (treeview.isCheckList()) {
            toggleChecklist.setTitle("Unselect checklist");
        } else {
            toggleChecklist.setTitle("Select checklist");
        }

        MenuItem toggleHideMode = menu.findItem(R.id.action_toggle_hidden_mode);
        if (treeview.isHiddenModeEnabled()) {
            toggleHideMode.setTitle("Unselect hide mode");
        } else {
            toggleHideMode.setTitle("Select hide mode");
        }

        MenuItem toggleHideSelectionActive = menu.findItem(R.id.action_toggle_hide_selection_active);
        if (treeview.isHiddenSelectionActive()) {
            toggleHideSelectionActive.setTitle("Unselect hide selection");
        } else {
            toggleHideSelectionActive.setTitle("Select hide selection");
        }

        MenuItem toggleMultiSelectable = menu.findItem(R.id.action_toggle_multi_selection);
        if (treeview.isMultiSelectable()) {
            toggleMultiSelectable.setTitle("Enable single selection");
        } else {
            toggleMultiSelectable.setTitle("Enable multi selection");
        }

        MenuItem toggleDescriptionLongClickEnabled = menu.findItem(R.id.action_toggle_descript_long_click_enabled);
        if (treeview.isDescriptionLongClickEnabled()) {
            toggleDescriptionLongClickEnabled.setTitle("Enable short-click selection");
        } else {
            toggleDescriptionLongClickEnabled.setTitle("Enable long-click selection");
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
        treeview.setCheckList(!treeview.isCheckList());
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuToggleHideModeClick(MenuItem item) {
        treeview.setHiddenModeEnabled(!treeview.isHiddenModeEnabled());
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuToggleHideHiddenClick(MenuItem item) {
        treeview.setHiddenSelectionIsActive(!treeview.isHiddenSelectionActive());
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuAddSameLevelClick(MenuItem item) {
        NoteNode noteNodeNew = new NoteNode("Brand new node " + new Date().toString());

        TreeviewNode treeviewNodeSelected = treeview.getSelectedFirst();

        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            NoteNode noteNodeParent = noteNodeSelected.getParent();
            if (noteNodeParent == null) {
                sd.note.addChildNode(noteNodeNew);
            } else {
                noteNodeParent.addChildNode(noteNodeNew);
            }
        }
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuAddNextLevelClick(MenuItem item) {
        NoteNode noteNodeNew = new NoteNode("Brand new node " + new Date().toString());

        TreeviewNode treeviewNodeSelected = treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                noteNodeSelected.addChildNode(noteNodeNew);
            }
        }
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuDeleteClick(MenuItem item)  {
        TreeviewNode treeviewNodeSelected = treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                noteNodeSelected.setDelete(true);
            }
        }
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
    }

    public void onMenuMoveUpClick(MenuItem item) {
        TreeviewNode treeviewNodeSelected = treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                NoteNode parentNode = noteNodeSelected.getParent();
                if (parentNode == null) {
                    int intCurrentPosition = sd.note.getChildNodes().indexOf(noteNodeSelected);
                    intCurrentPosition += 1;
                    ArrayList<NoteNode> nodes = treeview.getAdapter().getSourceChildNodes();
                    TreeUtils.reorderArrayList(nodes, noteNodeSelected, intCurrentPosition);
                } else {
                    int intCurrentPosition = noteNodeSelected.getParent().getChildNodes().indexOf(noteNodeSelected);
                    intCurrentPosition += 1;
                    ArrayList<NoteNode> oldNodes = noteNodeSelected.getParent().getChildNodes();
                    TreeUtils.reorderArrayList(oldNodes, noteNodeSelected, intCurrentPosition);
                }
            }
        }
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuMoveDownClick(MenuItem item) {
        TreeviewNode treeviewNodeSelected = treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                NoteNode parentNode = noteNodeSelected.getParent();
                if (parentNode == null) {
                    int intCurrentPosition = sd.note.getChildNodes().indexOf(noteNodeSelected);
                    intCurrentPosition -= 1;
                    ArrayList<NoteNode> nodes = treeview.getAdapter().getSourceChildNodes();
                    TreeUtils.reorderArrayList(nodes, noteNodeSelected, intCurrentPosition);
                } else {
                    int intCurrentPosition = noteNodeSelected.getParent().getChildNodes().indexOf(noteNodeSelected);
                    intCurrentPosition -= 1;
                    ArrayList<NoteNode> oldNodes = noteNodeSelected.getParent().getChildNodes();
                    TreeUtils.reorderArrayList(oldNodes, noteNodeSelected, intCurrentPosition);
                }
            }
        }
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuToggleNewnessClick(MenuItem menuItem) {
        TreeviewNode treeviewNodeSelected = treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                noteNodeSelected.setNew(!noteNodeSelected.isNew());
            }
        }
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
    }

    public void onMenuToggleMultiSelectionClick(MenuItem item) {
        treeview.setMultiSelectable(!treeview.isMultiSelectable());
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuToggleEnableSelectionLongClick(MenuItem item)  {
        treeview.setDescriptionLongClickEnabled(!treeview.isDescriptionLongClickEnabled());
        treeview.getAdapter().notifyDataSetChanged();
        treeview.invalidate();
        invalidateOptionsMenu();
    }
}
