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
    private ArrayList<Treenode> childTreenodes = new ArrayList<Treenode>();

    private Context context;
    private ArrayList<ListViewListItem> listViewListItems;
    private TreeviewArrayAdapter arrayAdapter;
    private ListView listView;
    private int intTreeNodeLayoutId;
    private boolean boolIsMultiSelectable;
    ArrayList<Treenode> selectedTreenodes;

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

    public ArrayList<Treenode> getChildTreenodes() {
        return childTreenodes;
    }

    public void setChildTreenodes(ArrayList<Treenode> childTreenodes) {
        this.childTreenodes = childTreenodes;
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

    public void addNode(Treenode treenode) {
        treenode.setParent(null);
        getChildTreenodes().add(treenode);
    }

    public void removeNode(final Treenode removeTreenode) {
        TreeviewIterator treeviewIterator = new TreeviewIterator(getChildTreenodes());
        treeviewIterator.execute(new TreeviewIterator.OnTouchAllNodesListener() {
            @Override
            public boolean onNode(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel) {
                if (treenode.equals(removeTreenode)) {
                    parentArrayList.remove(treenode);
                    return true;
                }
                return false;
            }
        });
    }

    public Treenode getSelected() {
        if (selectedTreenodes != null) {
            if (selectedTreenodes.size() != 0) {
                    return selectedTreenodes.get(0);
            }
        }
        return null;
    }

    public ArrayList<Treenode> getAllSelected() {
        return selectedTreenodes;
    }

    private ArrayList<ListViewListItem> generateListItems() {
        final ArrayList<ListViewListItem> listViewListItemsNew = new ArrayList<ListViewListItem>();

        TreeviewIterator treeviewIterator = new TreeviewIterator(getChildTreenodes());
        treeviewIterator.execute(new TreeviewIterator.OnTouchAllNodesListener() {
            @Override
            public boolean onNode(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel) {

                    // All items in this list will be displayed. To hide, do not put them in this list
                    // Determine firstly if item needs to be displayed

                    // Evaluate deleted first
                    if (treenode.isDeleted())
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
                            if (treenode.isHidden() == false) {
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
                            if (treenode.isChecked() == false) {
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

                    ListViewListItem listItem = new ListViewListItem(getTreeview(), treenode, intLevel, null, null,EnumTreenodeNewItemType.OLD);

                    // Fill in the _boolFolderHasHiddenItems property. If any child with hidden items, return true
                    if ((objListItem.getItemType() == EnumFoldingType.FOLDER_COLLAPSED)
                            || (objListItem.getItemType() == EnumFoldingType.FOLDER_EXPANDED)) {
                        objListItem.setFolderHasHiddenItems(IsAnyHiddenItems(objTreeNode));
                    }
                    // Fill in the NewItem property
                    objListItem.intNewItemType = EnumNewItemType.OLD;
                    // If topmost item, determine if any children are new
                    if (getParentTreeNode(objTreeNode) == null) {
                        if (objTreeNode.boolIsNew) {
                            // Topmost item, new
                            if (IsChildItemNewItem(objTreeNode)) {
                                objListItem.intNewItemType = EnumNewItemType.NEW_AND_ROOT_PARENT_OF_NEW;
                            } else {
                                objListItem.intNewItemType = EnumNewItemType.NEW;
                            }
                        } else {
                            // Topmost item, old
                            if (IsChildItemNewItem(objTreeNode)) {
                                objListItem.intNewItemType = EnumNewItemType.ROOT_PARENT_OF_NEW;
                            }
                        }
                    } else {
                        // Leave item
                        if (objTreeNode.boolIsNew) {
                            // Leave item, new
                            if (IsChildItemNewItem(objTreeNode)) {
                                objListItem.intNewItemType = EnumNewItemType.NEW_AND_PARENT_OF_NEW;
                            } else {
                                objListItem.intNewItemType = EnumNewItemType.NEW;
                            }
                        } else {
                            // Leave item, old
                            if (IsChildItemNewItem(objTreeNode)) {
                                objListItem.intNewItemType = EnumNewItemType.PARENT_OF_NEW;
                            }
                        }
                    }

                    objListItems.add(objListItem);

                    if (objTreeNode.enumItemType == EnumFoldingType.FOLDER_COLLAPSED)
                        return objListItems;

                    for (clsTreeNode objChildTreeNode : objTreeNode.objChildren) {
                        objListItems = getListItemsRecursively(objChildTreeNode, intLevel, objListItems);
                    }
                }
                return false;

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
        Treenode treenode = listViewListItem.getTreenode();
        EnumTreenodeNewItemType enumNewItemType = listViewListItem.getEnumNewItemType();
        EnumTreenodeExpansionState enumExpansionState = treenode.getExpansionState();
        int intLayerCount = 1;
        boolean boolIsNew = treenode.isNew();
        boolean boolIsHidden = treenode.isHidden();
        if (boolIsHidden) intLayerCount +=1;
        if (boolIsNew) intLayerCount +=1;

        Drawable[] layers = new Drawable[intLayerCount+1];
        // Main image
        layers[0] = treeview.getIconImageDrawable(treenode.getEmptyIconId());

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
        TreeviewIterator treeviewIterator = new TreeviewIterator(getChildTreenodes());
        treeviewIterator.execute(new TreeviewIterator.OnTouchAllNodesListener() {
            @Override
            public boolean onNode(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel) {
                treenode.setDirty(boolIsDirty);
                return false;
            }
        });
    }

    private boolean boolItteration;
    private boolean isDirty() {
        TreeviewIterator treeviewIterator = new TreeviewIterator(getChildTreenodes());
        treeviewIterator.execute(new TreeviewIterator.OnTouchAllNodesListener() {
            @Override
            public boolean onNode(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel) {
                if (treenode.isDirty())  {
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
        private Treenode treenode;
        private int intLevel;
        private EnumTreenodeListItemLevelRelation enumAboveRelation, enumBelowRelation;
        private EnumTreenodeNewItemType enumNewItemType;



        // Getters Setters

        public Treenode getTreenode() {
            return treenode;
        }

        public void setTreenode(Treenode treenode) {
            this.treenode = treenode;
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

        ListViewListItem(Treeview treeview, Treenode treenode, int intLevel, EnumTreenodeListItemLevelRelation enumAboveRelation, EnumTreenodeListItemLevelRelation enumBelowRelation, EnumTreenodeNewItemType enumTreenodeNewItemType) {
            this.treeview = treeview;
            this.treenode = treenode;
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
            Treenode treenode = listViewListItem.getTreenode();

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
            textViewDescription.setText(listViewListItem.getLevel() + ": " + treenode.getDescription());

            // Setup media preview image
            return relativeView;
        }

        private class IconImageOnClickListener implements View.OnClickListener {


            @Override
            public void onClick(View v) {

                ListViewListItem listItemClicked = (ListViewListItem) iconImageView.getTag();
                Treeview treeview = listItemClicked.getTreeview();
                Treenode treenodeClicked = listItemClicked.getTreenode();

                if (treenodeClicked.getExpansionState() == EnumTreenodeExpansionState.COLLAPSED) {
                    treenodeClicked.setExpansionState(EnumTreenodeExpansionState.EXPANDED);
                    treeview.setIsDirty(true);
                } else if (treenodeClicked.getExpansionState() == EnumTreenodeExpansionState.EXPANDED) {
                    treenodeClicked.setExpansionState(EnumTreenodeExpansionState.COLLAPSED);
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
