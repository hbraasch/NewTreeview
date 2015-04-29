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

    public T parent;
    public ArrayList<T> children = new ArrayList<T>();

    public String getUuid() {
        return strUuid;
    }

    public void setUuid(String strUuid) {
        this.strUuid = strUuid;
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
}
