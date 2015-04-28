package treeview;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.treeapps.newtreeview.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class Treeview extends LinearLayout {




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
    private ArrayList<TreeviewNode> childNodes = new ArrayList<TreeviewNode>();

    private ArrayList<ListViewListItem> listViewListItems;
    private TreeviewArrayAdapter arrayAdapter;
    private TreeAdapter treeAdapter;
    private ListView listView;
    private int intTreeNodeLayoutId;
    private boolean boolIsMultiSelectable;
    private boolean boolIsCheckboxLongClickEnabled;
    private ArrayList<TreeviewNode> selectedTreeviewNodes;
    private int intTextSizeInSp;

    private boolean boolHiddenSelectionIsActive;
    private boolean boolIsHiddenActive;


    private boolean boolIsCheckList;
    private boolean boolIsCheckedItemsMadeHidden;

    protected int intMaxIndentLevel;
    protected int intSelectColor;
    protected int intIndentRadiusInDp;



    public Treeview(Context context) {
        super(context);
        this.listView = new ListView(getContext());
        addView(this.listView);
    }



    public Treeview(Context context, AttributeSet attrs) {
        super(context, attrs);
        getStyledAttributes(context,attrs);
        this.listView = new ListView(getContext());
        addView(this.listView);
    }

    public Treeview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getStyledAttributes(context,attrs);
        this.listView = new ListView(getContext());
        addView(this.listView);
    }

    public Treeview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        getStyledAttributes(context,attrs);
        this.listView = new ListView(getContext());
        addView(this.listView);
    }



    public void getStyledAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.Treeview, 0, 0);
        try {
            intTextSizeInSp = a.getInteger(R.styleable.Treeview_textsize, 0);
        } finally {
            a.recycle();
        }
    }

    public void setParameters(int intTreeNodeLayoutId, Map<Integer,Drawable> iconImages, int intMaxIndentLevel, int intSelectColor, int intTextSizeInSp, int intIndentRadiusInDp) {

        // General initialisation
        this.intTreeNodeLayoutId = intTreeNodeLayoutId;
        this.iconImages = iconImages;
        this.intMaxIndentLevel = intMaxIndentLevel;
        this.intSelectColor = intSelectColor;
        this.intIndentRadiusInDp = intIndentRadiusInDp;
        if (this.intTextSizeInSp == 0) this.intTextSizeInSp = intTextSizeInSp;

        listViewListItems = new ArrayList<ListViewListItem> ();
        arrayAdapter = new TreeviewArrayAdapter(getContext(),intTreeNodeLayoutId, listViewListItems);
        listView.setAdapter(arrayAdapter);
        listView.setBackgroundColor(getResources().getColor(R.color.white_background));

        boolHiddenSelectionIsActive = false;
        boolIsHiddenActive = false;

        boolIsCheckList = false;
        boolIsCheckedItemsMadeHidden = false;

    }


    public void setAdapter(TreeAdapter treeAdapter) {

        // Adapter
        this.treeAdapter = treeAdapter;
        treeAdapter.setTreeview(this);
        childNodes = treeAdapter.adapt();
        invalidate();
    }


    // Getter Setters

    public Map<Integer, Drawable> getIconImages() {
        return iconImages;
    }

    public void setIconImages(Map<Integer, Drawable> iconImages) {
        this.iconImages = iconImages;
    }

    public ArrayList<TreeviewNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(ArrayList<TreeviewNode> childNodes) {
        this.childNodes = childNodes;
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

    public boolean isCheckboxLongClickEnabled() {
        return boolIsCheckboxLongClickEnabled;
    }

    public void setIsCheckboxLongClickEnabled(boolean boolIsCheckboxLongClickEnabled) {
        this.boolIsCheckboxLongClickEnabled = boolIsCheckboxLongClickEnabled;
    }

    // Methods

    public Treeview getTreeview() {
        return this;
    }



    public void addNode(TreeviewNode treeviewNode) {
        treeviewNode.setParent(null);
        getChildNodes().add(treeviewNode);
    }

    public void removeNode(final TreeviewNode removeTreeviewNode) {
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(getChildNodes());
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

    public TreeviewNode getSelectedFirst() {
        if (selectedTreeviewNodes != null) {
            if (selectedTreeviewNodes.size() != 0) {
                    return selectedTreeviewNodes.get(0);
            }
        }
        return null;
    }

    public ArrayList<TreeviewNode> getSelected() {
        return selectedTreeviewNodes;
    }

    private ArrayList<ListViewListItem> generateListItems() {
        final ArrayList<ListViewListItem> listViewListItemsNew = new ArrayList<ListViewListItem>();

        // Iterate through tree
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(getChildNodes());
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
                Treeview treeview = getTreeview();
                ListViewListItem listItem = new ListViewListItem(treeview, treeviewNode, intLevel, null, null, EnumTreenodeNewItemType.OLD);



                // Fill in the _boolFolderHasHiddenItems property. If any child with hidden items, return true
                if ((treeviewNode.getExpansionState() == EnumTreenodeExpansionState.COLLAPSED)
                        || (treeviewNode.getExpansionState() == EnumTreenodeExpansionState.EXPANDED)) {
                    listItem.setHasHiddenItems(treeviewNode.isAnyChildrenHidden());
                }
                // Fill in the NewItem property
                listItem.setEnumNewItemType(EnumTreenodeNewItemType.OLD);
                // If topmost item, determine if any children are new
                if (treeviewNode.getParent() == null) {
                    if (treeviewNode.isNew()) {
                        // Topmost item, new
                        if (treeviewNode.hasNewChildren()) {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.NEW_AND_ROOT_PARENT_OF_NEW);
                        } else {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.NEW);
                        }
                    } else {
                        // Topmost item, old
                        if (treeviewNode.hasNewChildren()) {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.ROOT_PARENT_OF_NEW);
                        }
                    }
                } else {
                    // Leave item
                    if (treeviewNode.isNew()) {
                        // Leave item, new
                        if (treeviewNode.hasNewChildren()) {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.NEW_AND_PARENT_OF_NEW);
                        } else {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.NEW);
                        }
                    } else {
                        // Leave item, old
                        if (treeviewNode.hasNewChildren()) {
                            listItem.setEnumNewItemType(EnumTreenodeNewItemType.PARENT_OF_NEW);
                        }
                    }
                }

                // Finally add to collection to make it visible
                listViewListItemsNew.add(listItem);

                // Collapse Expand handling
                if (treeviewNode.getExpansionState() == EnumTreenodeExpansionState.COLLAPSED) {
                    // Do not display children of this leave
                    return true;   // Signal to iterator to leave this branch
                } else {
                    return false; // Signal to iterator to carry on into branch
                }
            }
        });

        // Run through listViewListItemsNew again to determine their relationship with each other for drawing purposes
        ListViewListItem aboveListItem = null;
        ListViewListItem thisListItem = null;
        ListViewListItem belowListItem = null;
        for (int i = 0; i < listViewListItemsNew.size(); i++) {
            thisListItem = (ListViewListItem) listViewListItemsNew.get(i);
            if (i == 0) {
                aboveListItem = (ListViewListItem) listViewListItemsNew.get(i);
            } else {
                aboveListItem = (ListViewListItem) listViewListItemsNew.get(i - 1);
            }
            if (i >= listViewListItemsNew.size() - 1) {
                belowListItem = (ListViewListItem) listViewListItemsNew.get(i);
            } else {
                belowListItem = (ListViewListItem) listViewListItemsNew.get(i + 1);
            }

            if (aboveListItem.getLevel() == thisListItem.getLevel()) {
                thisListItem.setEnumAboveRelation(EnumTreenodeListItemLevelRelation.SAME);
            } else if (aboveListItem.getLevel() < thisListItem.getLevel()) {
                thisListItem.setEnumAboveRelation(EnumTreenodeListItemLevelRelation.LOWER);
            } else {
                thisListItem.setEnumAboveRelation(EnumTreenodeListItemLevelRelation.HIGHER);
            }

            if (belowListItem.getLevel() == thisListItem.getLevel()) {
                thisListItem.setEnumBelowRelation(EnumTreenodeListItemLevelRelation.SAME);
            } else if (belowListItem.getLevel() < thisListItem.getLevel()) {
                thisListItem.setEnumBelowRelation(EnumTreenodeListItemLevelRelation.LOWER);
            } else {
                thisListItem.setEnumBelowRelation(EnumTreenodeListItemLevelRelation.HIGHER);
            }
        }
        if (listViewListItemsNew.size() != 0) {
            listViewListItemsNew.get(0).setEnumAboveRelation(EnumTreenodeListItemLevelRelation.HIGHER);
            listViewListItemsNew.get(listViewListItemsNew.size() - 1).setEnumBelowRelation(EnumTreenodeListItemLevelRelation.HIGHER);
        }

        // Done
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
        layers[0] = treeview.getIconImageDrawable(treeviewNode.getIconId());

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

    public void setIsChanged(final boolean boolIsDirty) {
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(getChildNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                treeviewNode.setDirty(boolIsDirty);
                return false;
            }
        });
    }

    private boolean boolItteration;
    public boolean isChanged() {
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(getChildNodes());
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

        private final int DP_2_IN_PX = TreeviewUtils.dpToPx(getContext(),2);
        private final int DP_5_IN_PX = TreeviewUtils.dpToPx(getContext(),5);

        private int intResourceId;
        private  List<ListViewListItem> listViewListItems;
        private int fltMaxClickDistance;

        // Form objects
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

            int sdk = android.os.Build.VERSION.SDK_INT;
            RelativeLayout relativeView;
            final ListViewListItem listViewListItem = getItem(position);
            final Treeview treeview = listViewListItem.getTreeview();
            final TreeviewNode treeviewNode = listViewListItem.getTreeviewNode();

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
            ImageView iconImageView = (ImageView) relativeView.findViewById(R.id.treenode_icon);
            iconImageView.setTag(listViewListItem);
            iconImageView.setOnClickListener(new IconImageOnClickListener());
            iconImageView.setImageDrawable(treeview.generateIconImageDrawable(listViewListItem));

            // Setup checkbox
            // Checklist or Hide activities
            final CheckBox checkbox = (CheckBox) relativeView.findViewById(R.id.treenode_checkbox);
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)checkbox.getLayoutParams();
                params.setMargins(0, 0, DP_5_IN_PX, 0); //substitute parameters for left, top, right, bottom
                checkbox.setLayoutParams(params);
            }
            setCheckboxVisibilityBasedOnSettings(checkbox, treeview.isCheckList(), treeview.isHiddenSelectionActive());
            if ( treeview.isHiddenSelectionActive() == false) {
                // Hide selection is inactive
                if (treeview.isCheckList()) {
                    // Checklist activities
                    // Set items depending on checklist type note
                    if (treeview.isCheckboxLongClickEnabled() == false) {
                        checkbox.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick (View view) {
                                handleCheckBoxTriggerEvent((CheckBox) view, listViewListItem);
                            }
                        });
                    } else {
                        checkbox.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick (View view) {
                                ((CheckBox) view).setChecked(!((CheckBox) view).isChecked()); // Need to revert the selection if it gets triggered
                                Toast.makeText(getContext(), "Long click to check or uncheck", Toast.LENGTH_SHORT).show();
                                invalidate();
                            }
                        });
                        checkbox.setLongClickable(true);
                        checkbox.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                ((CheckBox) view).setChecked(!((CheckBox) view).isChecked()); // LongClick logic is reverse to OnClick, need to invert setting before calling next method
                                handleCheckBoxTriggerEvent((CheckBox) view, listViewListItem);
                                invalidate();
                                return true;
                            }
                        });
                    }

                    if (treeviewNode.isChecked()) {
                        checkbox.setChecked(true);
                    } else {
                        checkbox.setChecked(false);
                    }

                    // End of checklist activities
                }

            } else {
                // Hide selection is active
                checkbox.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (((CheckBox) view).isChecked()) {
                            treeviewNode.setChildrenHidden(true);
                        } else {
                            treeviewNode.setChildrenHidden(false);
                        }
                    }
                });
                if (treeviewNode.isHidden()) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }

            }


            // Setup media preview image
            ImageView mediaPreviewImageView = (ImageView) relativeView.findViewById(R.id.treenode_media_preview);
            mediaPreviewImageView.setTag(listViewListItem);
            mediaPreviewImageView.setOnClickListener(new MediaPreviewImageOnClickListener());

            // Draw content of preview image if resource has an preview image
            Drawable drawable = listViewListItem.getTreeviewNode().getDrawableMediaPreviewImage();
            if (drawable == null) {
                mediaPreviewImageView.setVisibility(View.GONE);
            } else {
                mediaPreviewImageView.setVisibility(View.VISIBLE);
                mediaPreviewImageView.setImageDrawable(drawable);
            }


            Rect rectPreviewImageSizeInPx = determinePreviewImageSizeInPx(mediaPreviewImageView);
            fltMaxClickDistance = TreeviewUtils.pxToDp(getContext(),rectPreviewImageSizeInPx.height()/10);

            // Setup description (must be last because its custom and need sizes from already defined components
            IndentableTextView textViewDescription = (IndentableTextView) relativeView.findViewById(R.id.treenode_description);
            textViewDescription.setTag(listViewListItem);
            textViewDescription.setProperties(listViewListItem, rectPreviewImageSizeInPx.height(), rectPreviewImageSizeInPx.width(), intMaxIndentLevel, intSelectColor, intTextSizeInSp, intIndentRadiusInDp);
            textViewDescription.setOnLongClickListener(new TextViewDescriptionOnLongClickListener());

            // Done
            return relativeView;
        }

        private void handleCheckBoxTriggerEvent(CheckBox checkBox, ListViewListItem listViewListItem) {
            final TreeviewNode treeviewNode = listViewListItem.getTreeviewNode();
            if (checkBox.isChecked()) {
                // Checked
                if (treeviewNode.getExpansionState() != EnumTreenodeExpansionState.EMPTY) {
                    // Not empty
                    if (treeviewNode.isAnyChildrenChecked() == false) {
                        // If checkbox is on a parent, ask user if
                        // he wants all children checked
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("All items below will be checked also. Do you want to proceed?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                treeviewNode.setChildrenChecked(true);
                                invalidate();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                treeviewNode.setChecked(false);
                                dialog.cancel();
                                invalidate();

                            }
                        });
                        builder.show();
                    }
                }
                // If checkbox is on a child, just carry on checking it
                treeviewNode.setChecked(true);
                invalidate();
            } else {
                // Unchecked
                if (treeviewNode.getExpansionState() != EnumTreenodeExpansionState.EMPTY) {
                    // Not empty
                    if (treeviewNode.getChildNodes().isEmpty() == false) {
                        // If checkbox is on a parent, ask user if he wants all children unchecked
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("All items below will be unchecked also. Do you want to proceed?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                treeviewNode.setChildrenChecked(false);
                                invalidate();
                            }
                        });
                        builder.setNeutralButton("Unchecked only", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                treeviewNode.setChecked(false);
                                invalidate();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                treeviewNode.setChecked(true);
                                dialog.cancel();
                                invalidate();
                            }
                        });
                        builder.show();
                    } else {
                        treeviewNode.setChildrenChecked(false);
                        invalidate();
                    }
                }

                // If checkbox is on a child, just carry on un-checking it
                treeviewNode.setChecked(false);
                // Uncheck all parents because if any child unchecked, no parent can be set
                treeviewNode.setParentsChecked(false);

                invalidate();;
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void setCheckboxVisibilityBasedOnSettings(CheckBox checkbox, boolean isCheckList, boolean isHiddenSelectionActive)  {
            int sdk = android.os.Build.VERSION.SDK_INT;
            Context context = getContext();
            // Override by other activities
            checkbox.setVisibility(View.GONE);
            if (isHiddenSelectionActive) {
                checkbox.setVisibility(View.VISIBLE);
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    checkbox.setButtonDrawable(R.drawable.custom_check_box_red);
                } else {
                    checkbox.setBackground(context.getResources().getDrawable( R.drawable.custom_check_box_red));
                }
                return;
            }
            if (isCheckList) {
                checkbox.setVisibility(View.VISIBLE);
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    checkbox.setButtonDrawable(R.drawable.custom_check_box_black);
                } else {
                    checkbox.setBackground(context.getResources().getDrawable( R.drawable.custom_check_box_black));
                }
            }
        }

        private Rect determinePreviewImageSizeInPx(ImageView imageView) {
            // Sizing of the custom list item
            if (imageView.getVisibility() == View.VISIBLE) {
                ViewGroup.MarginLayoutParams vlp = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                int intThumbnailWidth = imageView.getLayoutParams().width;
                int intThumbnailHeight = imageView.getLayoutParams().height + vlp.leftMargin + vlp.rightMargin;
                return new Rect(0,0,intThumbnailWidth, intThumbnailHeight);
            } else {
                return new Rect(0,0,0,0);
            }

        }

        private class IconImageOnClickListener implements View.OnClickListener {


            @Override
            public void onClick(View v) {

                ImageView myImageView = (ImageView) v.findViewById(R.id.treenode_icon);
                ListViewListItem listItemClicked = (ListViewListItem) myImageView.getTag();
                Treeview treeview = listItemClicked.getTreeview();
                TreeviewNode treeviewNodeClicked = listItemClicked.getTreeviewNode();

                if (treeviewNodeClicked.getExpansionState() == EnumTreenodeExpansionState.COLLAPSED) {
                    treeviewNodeClicked.setExpansionState(EnumTreenodeExpansionState.EXPANDED);
                    treeview.setIsChanged(true);
                } else if (treeviewNodeClicked.getExpansionState() == EnumTreenodeExpansionState.EXPANDED) {
                    treeviewNodeClicked.setExpansionState(EnumTreenodeExpansionState.COLLAPSED);
                    treeview.setIsChanged(true);
                } else {
                    return;
                }
                // Update changes
                treeview.invalidate();
            }
        }

        private class MediaPreviewImageOnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {

            }
        }

        private class TextViewDescriptionOnLongClickListener implements OnLongClickListener {
            @Override
            public boolean onLongClick(View v) {
                // Toggle selection
                TextView myTextView = (TextView) v.findViewById(R.id.treenode_description);
                ListViewListItem objListItem = (ListViewListItem) myTextView.getTag();
                TreeviewNode objNewSelectedTreeNode = objListItem.getTreeviewNode();
                objNewSelectedTreeNode.toggleSelection();

                // Refresh view
                getTreeview().invalidate();
                return true;
            }
        }
    }




}
