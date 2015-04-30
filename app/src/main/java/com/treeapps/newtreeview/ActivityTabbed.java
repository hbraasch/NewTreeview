package com.treeapps.newtreeview;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import note.Note;
import note.NoteNode;
import treeview.Treeview;
import treeview.TreeviewAdapter;
import treeview.TreeviewNode;
import treeview.TreeUtils;
import utils.Utils;


@SuppressWarnings("ALL")
public class ActivityTabbed extends ActionBarActivity implements ActionBar.TabListener {


    private static final int INTENT_GET_IMAGE_RESOURCE = 1;

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

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    public static class SessionDataFragment extends android.app.Fragment {

        Context context;

        public Map<Integer, Drawable> iconImages;
        public Treeview treeview;

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
        setContentView(R.layout.activity_tabbed);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        sd = (SessionDataFragment) fragmentManager.findFragmentByTag("DATA");
        if (sd == null) {
            sd = new SessionDataFragment();
            fragmentManager.beginTransaction().add(sd, "DATA").commit();
        }
        sd.context = this;

        if(savedInstanceState == null) {
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


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    protected void onResume() {

        android.app.FragmentManager fragmentManager = getFragmentManager();
        sd = (SessionDataFragment) fragmentManager.findFragmentByTag("DATA");
        if (sd == null) {
            sd = new SessionDataFragment();
            fragmentManager.beginTransaction().add(sd, "DATA").commit();
        }
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_tabbed, menu);
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

        MenuItem toggleMultiSelectable = menu.findItem(R.id.action_toggle_multi_selection);
        if (sd.treeview.isMultiSelectable()) {
            toggleMultiSelectable.setTitle("Enable single selection");
        } else {
            toggleMultiSelectable.setTitle("Enable multi selection");
        }

        MenuItem toggleDescriptionLongClickEnabled = menu.findItem(R.id.action_toggle_descript_long_click_enabled);
        if (sd.treeview.isDescriptionLongClickEnabled()) {
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new OwnFragment();
                case 1:
                    return new ShareFragment();
                case 2:
                    return new ArchiveFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_own).toUpperCase(l);
                case 1:
                    return getString(R.string.title_fragment_share).toUpperCase(l);
                case 2:
                    return getString(R.string.title_fragment_archive).toUpperCase(l);
            }
            return null;
        }
    }

    public static class OwnFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public OwnFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_tabbed_own, container, false);


            // Setup components
            SessionDataFragment sd = ((ActivityTabbed)getActivity()).sd;
            final Treeview treeview = (Treeview) rootView.findViewById(R.id.treeview);
            sd.treeview = treeview;
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
                            EnumIconImageId.EMPTY.getValue(),
                            sourceNode.isSelected(),
                            sourceNode.isChecked(),
                            sourceNode.isHidden(),
                            sourceNode.isNew(),
                            sourceNode.isDeleted(),
                            sourceNode.getMediaPreviewImage(), sourceNode);
                    return treeviewNode;
                }
            };
            treeview.setAdapter(treeAdapter);







            return rootView;
        }
    }

    public static class ShareFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public ShareFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_tabbed_share, container, false);
            return rootView;
        }
    }

    public static class ArchiveFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public ArchiveFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_tabbed_archive, container, false);
            return rootView;
        }
    }

    // Intent responses

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTENT_GET_IMAGE_RESOURCE:
                    Uri objUri = data.getData();
                    if (objUri != null) {
                        Utils.generateThumbnailImage(this,objUri,new Utils.OnGenerateThumbnailImageCompleteListener() {
                            @Override
                            public void onComplete(boolean boolIsSuccess, String strErrorMessage, Drawable drawable) {
                                if (boolIsSuccess) {
                                    TreeviewNode treeviewNodeSelected = sd.treeview.getSelectedFirst();
                                    if (treeviewNodeSelected != null) {
                                        NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
                                        if (noteNodeSelected != null) {
                                            noteNodeSelected.setMediaPreviewImage(drawable);
                                            sd.treeview.getAdapter().notifyDataSetChanged();
                                            sd.treeview.invalidate();
                                        }
                                    }
                                } else {
                                    Toast.makeText(sd.context,strErrorMessage,Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    break;
            }
        }
    }


    // Menus

    public void onMenuTestClick(MenuItem item) {

    }

    public void onMenuToggleChecklistClick(MenuItem item) {
        sd. treeview.setCheckList(!sd.treeview.isCheckList());
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
        NoteNode noteNodeNew = new NoteNode("Brand new node " + new Date().toString());

        TreeviewNode treeviewNodeSelected = sd.treeview.getSelectedFirst();

        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            NoteNode noteNodeParent = noteNodeSelected.getParent();
            if (noteNodeParent == null) {
                sd.note.addChildNode(noteNodeNew);
            } else {
                noteNodeParent.addChildNode(noteNodeNew);
            }
        }
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuAddNextLevelClick(MenuItem item) {
        NoteNode noteNodeNew = new NoteNode("Brand new node " + new Date().toString());

        TreeviewNode treeviewNodeSelected = sd.treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                noteNodeSelected.addChildNode(noteNodeNew);
            }
        }
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuDeleteClick(MenuItem item)  {
        TreeviewNode treeviewNodeSelected = sd.treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                noteNodeSelected.setDelete(true);
            }
        }
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
    }

    public void onMenuMoveUpClick(MenuItem item) {
        TreeviewNode treeviewNodeSelected = sd.treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                NoteNode parentNode = noteNodeSelected.getParent();
                if (parentNode == null) {
                    int intCurrentPosition = sd.note.getChildNodes().indexOf(noteNodeSelected);
                    intCurrentPosition += 1;
                    ArrayList<NoteNode> nodes = sd.treeview.getAdapter().getSourceChildNodes();
                    TreeUtils.reorderArrayList(nodes, noteNodeSelected, intCurrentPosition);
                } else {
                    int intCurrentPosition = noteNodeSelected.getParent().getChildNodes().indexOf(noteNodeSelected);
                    intCurrentPosition += 1;
                    ArrayList<NoteNode> oldNodes = noteNodeSelected.getParent().getChildNodes();
                    TreeUtils.reorderArrayList(oldNodes, noteNodeSelected, intCurrentPosition);
                }
            }
        }
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuMoveDownClick(MenuItem item) {
        TreeviewNode treeviewNodeSelected = sd.treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                NoteNode parentNode = noteNodeSelected.getParent();
                if (parentNode == null) {
                    int intCurrentPosition = sd.note.getChildNodes().indexOf(noteNodeSelected);
                    intCurrentPosition -= 1;
                    ArrayList<NoteNode> nodes = sd.treeview.getAdapter().getSourceChildNodes();
                    TreeUtils.reorderArrayList(nodes, noteNodeSelected, intCurrentPosition);
                } else {
                    int intCurrentPosition = noteNodeSelected.getParent().getChildNodes().indexOf(noteNodeSelected);
                    intCurrentPosition -= 1;
                    ArrayList<NoteNode> oldNodes = noteNodeSelected.getParent().getChildNodes();
                    TreeUtils.reorderArrayList(oldNodes, noteNodeSelected, intCurrentPosition);
                }
            }
        }
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuToggleNewnessClick(MenuItem menuItem) {
        TreeviewNode treeviewNodeSelected = sd.treeview.getSelectedFirst();
        if (treeviewNodeSelected != null) {
            NoteNode noteNodeSelected = (NoteNode) treeviewNodeSelected.getTag();
            if (noteNodeSelected != null) {
                noteNodeSelected.setNew(!noteNodeSelected.isNew());
            }
        }
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
    }

    public void onMenuToggleMultiSelectionClick(MenuItem item) {
        sd.treeview.setMultiSelectable(!sd.treeview.isMultiSelectable());
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuToggleEnableSelectionLongClick(MenuItem item)  {
        sd.treeview.setDescriptionLongClickEnabled(!sd.treeview.isDescriptionLongClickEnabled());
        sd.treeview.getAdapter().notifyDataSetChanged();
        sd.treeview.invalidate();
        invalidateOptionsMenu();
    }

    public void onMenuAddImageClick(MenuItem item) {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
        imageIntent.setType("image/*");
        imageIntent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create the app chooser and add the image intent to it
        Intent chooser = new Intent(Intent.ACTION_CHOOSER, null);
        chooser.putExtra(Intent.EXTRA_INTENT, imageIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "Select Source of Image");


        // Start activity and provide ID of source (i.e. image)
        startActivityForResult(chooser, INTENT_GET_IMAGE_RESOURCE);
    }
}
