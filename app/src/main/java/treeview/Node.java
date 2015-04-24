package treeview;

import java.util.ArrayList;

/**
 * All trees using the TreeIterator tool, their nodes must to implement this interface
 */

public abstract class Node<T> {
    public abstract ArrayList<T> getChildNodes();
    public abstract T getParent();
}
