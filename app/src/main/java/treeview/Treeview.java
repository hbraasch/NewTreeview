package treeview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.treeapps.newtreeview.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class Treeview {



    public enum EnumTreenodeExpansionState {
        EXPANDED, COLLAPSED, EMPTY
    }

    public static enum EnumTreenodeListItemLevelRelation {
        SAME, HIGHER, LOWER;
    }

    public static enum EnumTreenodeNewItemType {
        OLD, NEW, NEW_AND_PARENT_OF_NEW, NEW_AND_ROOT_PARENT_OF_NEW, PARENT_OF_NEW, ROOT_PARENT_OF_NEW
    }

    private Map<Integer,Drawable> iconImages = new HashMap<>();
    private ArrayList<TreeviewNode> childTreeviewNodes = new ArrayList<TreeviewNode>();

    private Context context;
    private ArrayList<ListViewListItem> listViewListItems;
    private TreeviewArrayAdapter arrayAdapter;
    private ListView listView;
    private int intTreeNodeLayoutId;
    private boolean boolIsMultiSelectable;
    ArrayList<TreeviewNode> selectedTreeviewNodes;

    private boolean boolHiddenSelectionIsActive;
    private boolean boolIsHiddenActive;


    private boolean boolIsCheckList;
    private boolean boolIsCheckedItemsMadeHidden;


    public Treeview(Context context, ListView listView, int intTreeNodeLayoutId, Map<Integer,Drawable> iconImages) {
        this.context = context;
        this.listView = listView;
        this.intTreeNodeLayoutId = intTreeNodeLayoutId;
        this.iconImages = iconImages;

        listViewListItems = new ArrayList<ListViewListItem> ();
        arrayAdapter = new TreeviewArrayAdapter(context,intTreeNodeLayoutId, listViewListItems);
        listView.setAdapter(arrayAdapter);

        boolHiddenSelectionIsActive = false;
        boolIsHiddenActive = false;

        boolIsCheckList = false;
        boolIsCheckedItemsMadeHidden = false;
    }

    // Getter Setters


    public Context getContext() {
        return context;
    }

    public Map<Integer, Drawable> getIconImages() {
        return iconImages;
    }

    public void setIconImages(Map<Integer, Drawable> iconImages) {
        this.iconImages = iconImages;
    }

    public ArrayList<TreeviewNode> getChildTreeviewNodes() {
        return childTreeviewNodes;
    }

    public void setChildTreeviewNodes(ArrayList<TreeviewNode> childTreeviewNodes) {
        this.childTreeviewNodes = childTreeviewNodes;
    }

    public boolean getMultiSelectable() {
        return boolIsMultiSelectable;
    }

    public void setMultiSelectable(boolean boolIsMultiSelectable) {
        this.boolIsMultiSelectable = boolIsMultiSelectable;
    }

    public boolean isHiddenSelectionActive() {
        return boolHiddenSelectionIsActive;
    }

    public void setHiddenSelectionIsActive(boolean boolHiddenSelectionIsActive) {
        this.boolHiddenSelectionIsActive = boolHiddenSelectionIsActive;
    }

    public boolean isHiddenActive() {
        return boolIsHiddenActive;
    }

    public void setHiddenActive(boolean boolIsHiddenActive) {
        this.boolIsHiddenActive = boolIsHiddenActive;
    }

    public boolean isCheckList() {
        return boolIsCheckList;
    }

    public void setIsCheckList(boolean boolIsCheckList) {
        this.boolIsCheckList = boolIsCheckList;
    }

    public boolean isCheckedItemsMadeHidden() {
        return boolIsCheckedItemsMadeHidden;
    }

    public void setIsCheckedItemsMadeHidden(boolean boolIsCheckedItemsMadeHidden) {
        this.boolIsCheckedItemsMadeHidden = boolIsCheckedItemsMadeHidden;
    }



    // Methods

    public Treeview getTreeview() {
        return this;
    }

    public void addNode(TreeviewNode treeviewNode) {
        treeviewNode.setParent(null);
        getChildTreeviewNodes().add(treeviewNode);
    }

    public void removeNode(final TreeviewNode removeTreeviewNode) {
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(getChildTreeviewNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                if (treeviewNode.equals(removeTreeviewNode)) {
                    parentArrayList.remove(treeviewNode);
                    return true;
                }
                return false;
            }
        });
    }

    public TreeviewNode getSelected() {
        if (selectedTreeviewNodes != null) {
            if (selectedTreeviewNodes.size() != 0) {
                    return selectedTreeviewNodes.get(0);
            }
        }
        return null;
    }

    public ArrayList<TreeviewNode> getAllSelected() {
        return selectedTreeviewNodes;
    }

    private ArrayList<ListViewListItem> generateListItems() {
        final ArrayList<ListViewListItem> listViewListItemsNew = new ArrayList<ListViewListItem>();

        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(getChildTreeviewNodes());
        treeIterator.executeWithBranchDepthControllable(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {

                // All items in this list will be displayed. To hide, do not put them in this list
                // Determine firstly if item needs to be displayed

                // Evaluate deleted first
                if (treeviewNode.isDeleted())
                    return true;

                // Evaluate "hidden" next
                boolean boolDisplayItem = false;
                if (isHiddenSelectionActive()) {
                    // Busy selecting items to hide, all must display
                    boolDisplayItem = true;
                } else {
                    if (isHiddenActive() == false) {
                        // Hidden is not activated, so display all
                        boolDisplayItem = true;
                    } else {
                        // Hidden is activated, so display only those marked as not hidden
                        if (treeviewNode.isHidden() == false) {
                            boolDisplayItem = true;
                        }
                    }
                }

                if (boolDisplayItem == false) {
                    return true;
                }

                // Now evaluate the checklist component
                boolDisplayItem = false;
                // Now check what happens if it is a checklist
                if (isCheckList()) {
                    if (isCheckedItemsMadeHidden() == true) {
                        // System is set to hide all checked items, so see if checked item
                        if (treeviewNode.isChecked() == false) {
                            boolDisplayItem = true;
                        }
                    } else {
                        // System not set to hide checked items, display all
                        boolDisplayItem = true;
                    }
                } else {
                    // No checklist items, so display all
                    boolDisplayItem = true;
                }

                if (boolDisplayItem == false) {
                    return true;
                }

                // If to be displayed, add it to the list item collection
                intLevel += 1;
                Treeview treeview = getTreeview();
                ListViewListItem listItem = new ListViewListItem(treeview, treeviewNode, intLevel, null, null, EnumTreenodeNewItemType.OLD);

                // Fill in the _boolFolderHasHiddenItems property. If any child with hidden items, return true
                if ((treeviewNode.getExpansionState() == EnumTreenodeExpansionState.COLLAPSED)
                        || (treeviewNode.getExpansionState() == EnumTreenodeExpansionState.EXPANDED)) {
                    listItem.setHasHiddenItems(treeviewNode.hasHiddenChildItems());
                }
                // Fill in the NewItem property
                listItem.setEnumNewItemType(EnumTreenodeNewItemType.OLD);
                // If topmost item, determine if any children are new
                if (treeviewNode.getParent() == null) {
                    if (treeviewNode.isNew()) {
                        // Topmost item, new
                        if (treeviewNode.hasNewChildItems()) {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.NEW_AND_ROOT_PARENT_OF_NEW);
                        } else {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.NEW);
                        }
                    } else {
                        // Topmost item, old
                        if (treeviewNode.hasNewChildItems()) {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.ROOT_PARENT_OF_NEW);
                        }
                    }
                } else {
                    // Leave item
                    if (treeviewNode.isNew()) {
                        // Leave item, new
                        if (treeviewNode.hasNewChildItems()) {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.NEW_AND_PARENT_OF_NEW);
                        } else {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.NEW);
                        }
                    } else {
                        // Leave item, old
                        if (treeviewNode.hasNewChildItems()) {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.PARENT_OF_NEW);
                        }
                    }
                }

                // Finally add to collection to make it visible
                listViewListItemsNew.add(listItem);

                // Collapse Expand handling
                if (treeviewNode.getExpansionState() == EnumTreenodeExpansionState.COLLAPSED) {
                    // Do not display children of this leave
                    return false;   // Signal to iterator to leave this branch
                }
                return true;
            }


        });




        return listViewListItemsNew;
    }

    /**
     * Used to generate the treenode icon drawable
     * @param listViewListItem
     * @return
     */
    private Drawable generateIconImageDrawable(ListViewListItem listViewListItem) {
        Resources resources = getContext().getResources();
        Treeview treeview = listViewListItem.getTreeview();
        TreeviewNode treeviewNode = listViewListItem.getTreeviewNode();
        EnumTreenodeNewItemType enumNewItemType = listViewListItem.getEnumNewItemType();
        EnumTreenodeExpansionState enumExpansionState = treeviewNode.getExpansionState();
        int intLayerCount = 1;
        boolean boolIsNew = treeviewNode.isNew();
        boolean boolIsHidden = treeviewNode.isHidden();
        if (boolIsHidden) intLayerCount +=1;
        if (boolIsNew) intLayerCount +=1;

        Drawable[] layers = new Drawable[intLayerCount+1];
        // Main image
        layers[0] = treeview.getIconImageDrawable(treeviewNode.getEmptyIconId());

        // Layer for NEW display
        if (intLayerCount > 1) {

            if (intLayerCount ==  2) {
                if (boolIsHidden) layers[1] = resources.getDrawable(R.mipmap.icon_overlay_hidden);
                if (boolIsNew)  {
                    switch (enumNewItemType) {
                        case ROOT_PARENT_OF_NEW:
                        case PARENT_OF_NEW:
                            layers[1] = resources.getDrawable(R.mipmap.icon_overlay_new_yellow);
                            break;
                        case NEW_AND_ROOT_PARENT_OF_NEW:
                        case NEW_AND_PARENT_OF_NEW:
                            layers[1] = resources.getDrawable(R.mipmap.icon_overlay_new_redyellow);
                            break;
                        case NEW:
                            layers[1] = resources.getDrawable(R.mipmap.icon_overlay_new);
                            break;
                        default:
                            break;
                    }
                }
            } else {
                // intLayerCount = 3
                layers[1] = resources.getDrawable(R.mipmap.icon_overlay_hidden);
                layers[2] = resources.getDrawable(R.mipmap.icon_overlay_new);
            }
        }

        // Finally add expand collapse effects
        switch (enumExpansionState) {
            case EXPANDED:
                layers[intLayerCount] = resources.getDrawable(R.mipmap.item_expanded);
                break;
            case COLLAPSED:
                layers[intLayerCount] = resources.getDrawable(R.mipmap.item_collapsed);
                break;
            default:
                layers[intLayerCount] = resources.getDrawable(R.mipmap.item_empty);
                break;
        }

        return new LayerDrawable(layers);

    }

    private Drawable getIconImageDrawable(int intIconImageId) {
        if (getIconImages().containsKey(intIconImageId)) {
            return getIconImages().get(intIconImageId);
        }
        return null;
    }

    private void setIsDirty(final boolean boolIsDirty) {
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(getChildTreeviewNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                treeviewNode.setDirty(boolIsDirty);
                return false;
            }
        });
    }

    private boolean boolItteration;
    private boolean isDirty() {
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(getChildTreeviewNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                if (treeviewNode.isDirty())  {
                    boolItteration = true;
                    return true;
                }
                return false;
            }
        });
        return boolItteration;
    }



    // Events


    /**
     * Redraw
     */
    public void invalidate() {
        listViewListItems = generateListItems();
        arrayAdapter.clear();
        arrayAdapter.addAll(listViewListItems);
        arrayAdapter.notifyDataSetChanged();
    }



    // Helper classes

    /**
     * Contains data for ListView
     */
    class ListViewListItem {



        private Treeview treeview;
        private TreeviewNode treeviewNode;
        private int intLevel;
        private EnumTreenodeListItemLevelRelation enumAboveRelation, enumBelowRelation;
        private EnumTreenodeNewItemType enumNewItemType;
        private boolean boolHasHiddenItems;



        // Getters Setters

        public TreeviewNode getTreeviewNode() {
            return treeviewNode;
        }

        public void setTreeviewNode(TreeviewNode treeviewNode) {
            this.treeviewNode = treeviewNode;
        }

        public int getLevel() {
            return intLevel;
        }

        public void setLevel(int intLevel) {
            this.intLevel = intLevel;
        }

        public EnumTreenodeListItemLevelRelation getEnumAboveRelation() {
            return enumAboveRelation;
        }

        public void setEnumAboveRelation(EnumTreenodeListItemLevelRelation enumAboveRelation) {
            this.enumAboveRelation = enumAboveRelation;
        }

        public EnumTreenodeListItemLevelRelation getEnumBelowRelation() {
            return enumBelowRelation;
        }

        public void setEnumBelowRelation(EnumTreenodeListItemLevelRelation enumBelowRelation) {
            this.enumBelowRelation = enumBelowRelation;
        }

        public Treeview getTreeview() {
            return treeview;
        }

        public void setTreeview(Treeview treeview) {
            this.treeview = treeview;
        }

        public EnumTreenodeNewItemType getEnumNewItemType() {
            return enumNewItemType;
        }

        public void setEnumNewItemType(EnumTreenodeNewItemType enumNewItemType) {
            this.enumNewItemType = enumNewItemType;
        }

        public boolean hasHiddenItems() {
            return boolHasHiddenItems;
        }

        public void setHasHiddenItems(boolean boolHasHiddenItems) {
            this.boolHasHiddenItems = boolHasHiddenItems;
        }


        ListViewListItem(Treeview treeview, TreeviewNode treeviewNode, int intLevel, EnumTreenodeListItemLevelRelation enumAboveRelation, EnumTreenodeListItemLevelRelation enumBelowRelation, EnumTreenodeNewItemType enumTreenodeNewItemType) {
            this.treeview = treeview;
            this.treeviewNode = treeviewNode;
            this.intLevel = intLevel;
            this.enumAboveRelation = enumAboveRelation;
            this.enumBelowRelation = enumBelowRelation;
        }
    }

    /**
     * TreeviewArrayAdapter
     */
    class TreeviewArrayAdapter extends ArrayAdapter<ListViewListItem> {

        private int intResourceId;
        private  List<ListViewListItem> listViewListItems;

        // Form objects
        private ImageView iconImageView;
        private IndentableTextView textViewDescription;

        public TreeviewArrayAdapter(Context context, int intResourceId, List<ListViewListItem> listViewListItems) {
            super(context, intResourceId, listViewListItems);
            this.intResourceId = intResourceId;
            this.listViewListItems = listViewListItems;
        }

        public int getResourceId() {
            return intResourceId;
        }

        public void setResourceId(int intResourceId) {
            this.intResourceId = intResourceId;
        }

        public List<ListViewListItem> getListViewListItems() {
            return listViewListItems;
        }

        public void setListViewListItems(List<ListViewListItem> listViewListItems) {
            this.listViewListItems = listViewListItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RelativeLayout relativeView;
            ListViewListItem listViewListItem = getItem(position);
            Treeview treeview = listViewListItem.getTreeview();
            TreeviewNode treeviewNode = listViewListItem.getTreeviewNode();

            if (convertView == null) {
                relativeView = new RelativeLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater layoutInflater;
                layoutInflater = (LayoutInflater) getContext().getSystemService(inflater);
                layoutInflater.inflate(intResourceId, relativeView, true);
            } else {
                relativeView = (RelativeLayout) convertView;
            }

            // Setup icon
            iconImageView = (ImageView) relativeView.findViewById(R.id.treenode_icon);
            iconImageView.setTag(listViewListItem);
            iconImageView.setOnClickListener(new IconImageOnClickListener());
            iconImageView.setImageDrawable(treeview.generateIconImageDrawable(listViewListItem));

            // Setup checkbox

            // Setup description
            textViewDescription = (IndentableTextView) relativeView.findViewById(R.id.treenode_description);
            textViewDescription.setText(listViewListItem.getLevel() + ": " + treeviewNode.getDescription());

            // Setup media preview image
            return relativeView;
        }

        private class IconImageOnClickListener implements View.OnClickListener {


            @Override
            public void onClick(View v) {

                ListViewListItem listItemClicked = (ListViewListItem) iconImageView.getTag();
                Treeview treeview = listItemClicked.getTreeview();
                TreeviewNode treeviewNodeClicked = listItemClicked.getTreeviewNode();

                if (treeviewNodeClicked.getExpansionState() == EnumTreenodeExpansionState.COLLAPSED) {
                    treeviewNodeClicked.setExpansionState(EnumTreenodeExpansionState.EXPANDED);
                    treeview.setIsDirty(true);
                } else if (treeviewNodeClicked.getExpansionState() == EnumTreenodeExpansionState.EXPANDED) {
                    treeviewNodeClicked.setExpansionState(EnumTreenodeExpansionState.COLLAPSED);
                    treeview.setIsDirty(true);
                } else {
                    return;
                }
                // Update changes
                treeview.invalidate();
            }
        }
    }




}
