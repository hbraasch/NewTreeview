package note;

import java.util.ArrayList;

import treeview.NodeBase;

/**
 * Created by HeinrichWork on 26/04/2015.
 */
public abstract class NoteNodeBase<T> implements NodeBase<T> {
    private String strDescription;
    public T parent;
    public ArrayList<T> children = new ArrayList<T>();

    public String getDescription() {
        return strDescription;
    }

    public void setDescription(String strDescription) {
        this.strDescription = strDescription;
    }

}
