package treeview;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class TreeviewNode implements NodeBase<TreeviewNode> {



    // Item row data
    private Integer intIconId;
    private boolean boolIsNew;
    private  boolean boolIsHidden;
    private boolean boolIsChecked;
    private String strDescription;
    private Drawable drawableMediaPreviewImage;

    // Item folding data
    private Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState;
    private boolean boolIsSelected;

    // Behaviour
    private boolean boolIsDirty;
    private boolean boolIsDeleted;



    // Items parent and childNoteNodes
    private TreeviewNode parent;
    private ArrayList<TreeviewNode> childTreeviewNodes = new ArrayList<TreeviewNode>();

    // Application specific, not to be serialised
    private transient Treeview treeview;
    private transient Object tag;

    public TreeviewNode(Treeview treeview, String strDescription, Integer intIconId, boolean boolIsSelected, boolean boolIsChecked, boolean boolIsHidden, boolean boolIsNew, boolean boolIsDeleted, Drawable drawableMediaPreviewImage, Object tag) {
        this.treeview = treeview;
        this.intIconId = intIconId;
        this.boolIsNew = false;
        this.boolIsSelected = boolIsSelected;
        this.boolIsHidden = boolIsHidden;
        this.boolIsNew = boolIsNew;
        this.boolIsChecked = boolIsChecked;
        this.boolIsDirty = false;
        this.boolIsDeleted = boolIsDeleted;
        this.strDescription = strDescription;
        this.drawableMediaPreviewImage = null;
        this.enumTreenodeExpansionState = Treeview.EnumTreenodeExpansionState.EMPTY;
        this.drawableMediaPreviewImage = drawableMediaPreviewImage;
        this.tag = tag;
    }

    public TreeviewNode(Treeview treeview, String strDescription, Integer intIconId, boolean boolIsNew, boolean boolIsSelected, boolean boolIsChecked, boolean boolIsHidden, Drawable drawableMediaPreviewImage, boolean boolIsDirty, boolean boolIsDeleted, Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState) {
        this.treeview = treeview;
        this.intIconId = intIconId;
        this.boolIsNew = boolIsNew;
        this.boolIsSelected = boolIsSelected;
        this.boolIsHidden = boolIsHidden;
        this.boolIsNew = boolIsNew;
        this.boolIsChecked = boolIsChecked;
        this.boolIsDirty = boolIsDirty;
        this.boolIsDeleted = boolIsDeleted;
        this.strDescription = strDescription;
        this.drawableMediaPreviewImage = drawableMediaPreviewImage;
        this.enumTreenodeExpansionState = enumTreenodeExpansionState;
    }

    // Getter Setters

    public Integer getIconId() {
        return intIconId;
    }

    public void setEmptyIconId(Integer intEmptyIconId) {
        this.intIconId = intEmptyIconId;
    }

    public boolean isDirty() {
        return boolIsDirty;
    }

    public void setDirty(boolean boolIsDirty) {
        this.boolIsDirty = boolIsDirty;
    }

    public boolean isDeleted() {
        return boolIsDeleted;
    }

    public void setDeleted(boolean boolIsDeleted) {
        this.boolIsDeleted = boolIsDeleted;
    }

    public boolean isNew() {
        return boolIsNew;
    }

    public void setNew(boolean boolIsNew) {
        this.boolIsNew = boolIsNew;
    }

    public boolean isHidden() {
        return boolIsHidden;
    }

    public void setHidden(boolean boolIsHidden) {
        this.boolIsHidden = boolIsHidden;
        if (treeview.getOnHideCheckChangedListener() != null) {
            treeview.getOnHideCheckChangedListener().onChange(boolIsHidden, this);
        }
    }

    public boolean isChecked() {
        return boolIsChecked;
    }

    public void setChecked(boolean boolIsChecked) {
        this.boolIsChecked = boolIsChecked;
        if (treeview.getOnCheckChangedListener() != null) {
            treeview.getOnCheckChangedListener().onChange(boolIsChecked, this);
        }
    }

    public String getDescription() {
        return strDescription;
    }

    public void setDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public Drawable getDrawableMediaPreviewImage() {
        return drawableMediaPreviewImage;
    }

    public void setDrawableMediaPreviewImage(Drawable drawableMediaPreviewImage) {
        this.drawableMediaPreviewImage = drawableMediaPreviewImage;
    }

    public Treeview.EnumTreenodeExpansionState getExpansionState() {
        return enumTreenodeExpansionState;
    }

    public void setExpansionState(Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState) {
        this.enumTreenodeExpansionState = enumTreenodeExpansionState;
    }

    public ArrayList<TreeviewNode> getChildNodes() {
        return childTreeviewNodes;
    }

    public void setChildTreeviewNodes(ArrayList<TreeviewNode> childTreeviewNodes) {
        this.childTreeviewNodes = childTreeviewNodes;
    }

    public TreeviewNode getParent() {
        return parent;
    }

    public void setParent(TreeviewNode parent) {
        this.parent = parent;
    }

    public boolean isSelected() {
        return boolIsSelected;
    }

    public void setSelected(boolean boolIsSelected) {
        this.boolIsSelected = boolIsSelected;
        // Indicate to user
        if (treeview.getOnSelectionChangedListener() != null) {
            treeview.getOnSelectionChangedListener().onChange(boolIsSelected, this );
        }
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    // Methods
    public void addChildNode(TreeviewNode treeviewNode) {
        treeviewNode.parent = this;
        getChildNodes().add(treeviewNode);
    }

    public void toggleExpansionState() {
        Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState = getExpansionState();
        switch (enumTreenodeExpansionState) {
            case COLLAPSED:
                setExpansionState(Treeview.EnumTreenodeExpansionState.EXPANDED);
                break;
            case EXPANDED:
                setExpansionState(Treeview.EnumTreenodeExpansionState.COLLAPSED);
                break;
            case EMPTY:
                break;
        }
    }

    private boolean boolIteratorHelper;
    public boolean isAnyChildrenHidden() {
        boolIteratorHelper = false;
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(this.getChildNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                if (treeviewNode.isHidden()) {
                    boolIteratorHelper = true;
                    return true; // Exit immediately
                }
                return false;
            }
        });
        return boolIteratorHelper;
    }

    public boolean hasNewChildren() {
        boolIteratorHelper = false;
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(this.getChildNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                if (treeviewNode.isNew()) {
                    boolIteratorHelper = true;
                    return true; // Exit immediately
                }
                return false;
            }
        });
        return boolIteratorHelper;
    }


    public void toggleSelection() {
        setSelected(!isSelected());
    }

    public void setSelfAndChildrenHidden(final boolean boolIsHidden)  {
        // Set itself
        this.setHidden(boolIsHidden);

        // Set childNoteNodes
        boolIteratorHelper = false;
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(this.getChildNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                treeviewNode.setHidden(boolIsHidden);
                return false;
            }
        });
    }

    public void setChildrenChecked(final boolean boolIsChecked)  {
        // Set itself
        this.setChecked(boolIsChecked);
        // Set childNoteNodes
        boolIteratorHelper = false;
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(this.getChildNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                treeviewNode.setChecked(boolIsChecked);
                return false;
            }
        });
    }

    public boolean isAnyChildrenChecked() {
        boolIteratorHelper = false;
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(this.getChildNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                if (treeviewNode.isChecked()) {
                    boolIteratorHelper = true;
                    return true; // Stop immediately
                }
                return false;
            }
        });
        return boolIteratorHelper;
    }

    public boolean isAllChildrenChecked() {
        boolIteratorHelper = true;
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(this.getChildNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                if (!treeviewNode.isChecked()) {
                    boolIteratorHelper = false;
                    return true; // Stop immediately
                }
                return true;
            }
        });
        return boolIteratorHelper;
    }

    public void setParentsChecked(final boolean boolIsChecked)  {
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(this.getChildNodes());
        treeIterator.execute(this, new TreeIterator.OnTouchAllParentNodesListener<TreeviewNode>() {
            @Override
            public boolean onParentNode(TreeviewNode treenode) {
                if (treenode != null) {
                    treenode.setChecked(boolIsChecked);
                }
                return false;
            }
        });
    }
}


