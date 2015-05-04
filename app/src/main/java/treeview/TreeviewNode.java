package treeview;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import shared.Node;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class TreeviewNode<T> extends Node<TreeviewNode<T>> {

    static int UNIQUE_ID = 0;

    private int intUniqueId = ++UNIQUE_ID;
    private Treeview treeview;
    private T tag;



    public TreeviewNode(Treeview treeview, String strDescription, Integer intIconId, boolean boolIsSelected, boolean boolIsChecked, boolean boolIsHidden, boolean boolIsNew, boolean boolIsDeleted, Drawable drawableMediaPreviewImage, T tag) {
        this.treeview = treeview;
        setIconId(intIconId);
        super.setSelected(boolIsSelected); // Do not trigger listener event
        super.setHidden(boolIsHidden); // Do not trigger listener event
        super.setChecked(boolIsChecked); // Do not trigger listener event
        setNew(boolIsNew);
        setDirty(false);
        setDeleted(boolIsDeleted);
        setDescription(strDescription);
        setExpansionState(Treeview.EnumTreenodeExpansionState.EMPTY);
        setMediaPreviewImage(drawableMediaPreviewImage);
        setTag(tag);
    }


    // Getter Setters

    @Override
    public void setHidden(boolean boolIsHidden) {
        super.setHidden(boolIsHidden);
        if (treeview.getOnHideCheckChangedListener() != null) {
            treeview.getOnHideCheckChangedListener().onChange(boolIsHidden, this);
        }
    }

    @Override
    public void setChecked(boolean boolIsChecked) {
        super.setChecked(boolIsChecked);
        if (treeview.getOnCheckChangedListener() != null) {
            treeview.getOnCheckChangedListener().onChange(boolIsChecked, this);
        }
    }

    @Override
    public void setSelected(boolean boolIsSelected) {
        super.setSelected(boolIsSelected);
        // Indicate to user
        if (treeview.getOnSelectionChangedListener() != null) {
            treeview.getOnSelectionChangedListener().onChange(boolIsSelected, this );
        }
    }

    public int getUniqueId() {
        return intUniqueId;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(T tag) {
        this.tag = tag;
    }

    // Methods
    private boolean boolIteratorHelper;

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

    public boolean isAnyChildrenHidden() {
        boolIteratorHelper = false;
        TreeIterator<TreeviewNode<T>> treeIterator = new TreeIterator<>(this.getChildNodes());
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
        TreeIterator<TreeviewNode<T>> treeIterator = new TreeIterator<>(this.getChildNodes());
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
        TreeIterator<TreeviewNode<T>> treeIterator = new TreeIterator<>(this.getChildNodes());
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
        TreeIterator<TreeviewNode<T>> treeIterator = new TreeIterator<>(this.getChildNodes());
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
        TreeIterator<TreeviewNode<T>> treeIterator = new TreeIterator<>(this.getChildNodes());
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
        TreeIterator<TreeviewNode<T>> treeIterator = new TreeIterator<>(this.getChildNodes());
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
        TreeIterator<TreeviewNode<T>> treeIterator = new TreeIterator<>(this.getChildNodes());
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


