package treeview;

import java.util.ArrayList;

/**
 * All trees using the TreeIterator tool, their nodes must to implement this interface
 */

public interface NodeBase<T> {
     
    public ArrayList<T> getChildNodes();
    public  T getParent();
}
