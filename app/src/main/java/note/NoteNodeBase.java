package note;

import java.util.ArrayList;

import treeview.NodeBase;

/**
 * Created by HeinrichWork on 26/04/2015.
 */
public abstract class NoteNodeBase<T> implements NodeBase<T> {
    private String strUuid;
    private String strDescription;
    private boolean boolIsSelected;
    private boolean boolIsChecked;
    private boolean boolIsHidden;
    private boolean boolIsNew;
    private boolean boolIsDeleted;

    public T parent;
    public ArrayList<T> childNoteNodes = new ArrayList<T>();

    public String getUuid() {
        return strUuid;
    }

    public void setUuid(String strUuid) {
        this.strUuid = strUuid;
    }

    public ArrayList<T> getChildNodes() {
        return childNoteNodes;
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parentNode) {
        parent = parentNode;
    }

    public void setChildNodes(ArrayList<T> childnodes) {
        childNoteNodes = childnodes;
    }

    public String getDescription() {
        return strDescription;
    }

    public void setDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public void setSelected(boolean boolIsSelected) {
        this.boolIsSelected = boolIsSelected;
    }

    public boolean isSelected() {
        return boolIsSelected;
    }

    public boolean isChecked() {
        return boolIsChecked;
    }

    public void setChecked(boolean boolIsChecked) {
        this.boolIsChecked = boolIsChecked;
    }

    public boolean isHidden() {
        return boolIsHidden;
    }

    public void setHidden(boolean boolIsHidden) {
        this.boolIsHidden = boolIsHidden;
    }

    public boolean isNew() {
        return boolIsNew;
    }

    public void setNew(boolean boolIsNew) {
        this.boolIsNew = boolIsNew;
    }

    public void setDelete(boolean boolIsDeleted) {
        this.boolIsDeleted = boolIsDeleted;
    }

    public boolean isDeleted() {
        return boolIsDeleted;
    }
}
