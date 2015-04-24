package treeview;

import java.util.ArrayList;

/**
 * Created by HeinrichWork on 4/04/2015.
 */
public class TreeIterator<T extends Node<T>> {

    private ArrayList<T> rootTreenodes;
    private OnTouchAllNodesListener onNodeTouchedListener;



    public interface OnTouchAllNodesListener<T> {
        /**
         * Traverse downwards through treeview, touching all child nodes, starting from the constructor presented childnode collection
         * @param parentArrayList parent collection containing this node
         * @param treenode the current node under inspection
         * @param intLevel indentation level (topmost level index = 0)
         * @return if true, itteration stops immediately. If false, itteration continue
         */
        public boolean onNode(ArrayList<T> parentArrayList, T treenode, int intLevel);
    }

    public interface OnTouchAllParentNodesListener<T> {
        /**
         * Traverse upwards through treeview, touching all parent nodes till root (treenode == null) is reached
         * @param treenode the current node under inspection
         * @return if true, itteration stops immediately. If false, itteration continue
         */
        public boolean onParentNode(T treenode);
    }




    public interface FindClause<T> {
        /**
         * User needs to generate the find condition inside this method
         * @param treenode
         * @return
         */
        public boolean isNodeFound(T treenode);
    }

    public TreeIterator(ArrayList<T> rootTreenodes) {
        this.rootTreenodes = rootTreenodes;
    }

    /**
     * Used to touch all nodes, when a callback returns true, stop
     * @param onTouchAllNodesListener
     */
    public void execute(OnTouchAllNodesListener onTouchAllNodesListener) {
        int intLevel;
        this.onNodeTouchedListener = onTouchAllNodesListener;
        for (T rootTreenode : rootTreenodes) {
            intLevel = 0;
            if(TouchTreeNodesRecursively(rootTreenodes, rootTreenode, intLevel)) return;
        }
    }

    /**
     * Used to traverse up the parent chain
     * @param startNode
     * @param onTouchAllParentNodesListener
     */
    public void execute(T startNode, OnTouchAllParentNodesListener onTouchAllParentNodesListener) {
        T parent = startNode.getParent();
        while (parent != null) {
            onTouchAllParentNodesListener.onParentNode(parent);
        }
        onTouchAllParentNodesListener.onParentNode(null);
    }

    /**
     * Used to touch all nodes, but when a callback returns true, stop traversing the current branch
     * @param onTouchAllNodesListener
     */
    public void executeWithBranchDepthControllable(OnTouchAllNodesListener onTouchAllNodesListener) {
        int intLevel;
        this.onNodeTouchedListener = onTouchAllNodesListener;
        for (T rootTreenode : rootTreenodes) {
            intLevel = 0;
            TouchTreeNodesRecursivelyWithBranchDeptControl(rootTreenodes, rootTreenode, intLevel);
        }
    }

    public ArrayList<T> Find(final FindClause findClause) {
        final ArrayList<T> foundTreeNodes = new ArrayList<T>();
        execute(new OnTouchAllNodesListener<T>() {
            @Override
            public boolean onNode(ArrayList<T> parentArrayList, T treenode, int intLevel) {
                if (findClause.isNodeFound(treenode)) {
                    foundTreeNodes.add(treenode);
                }
                return false;
            }
        });
        return foundTreeNodes;
    }


    private  boolean TouchTreeNodesRecursively(ArrayList<T> parentArrayList, T treenode, int intLevel) {
        intLevel += 1;
        if (ProcessTreeNode(parentArrayList, treenode, intLevel-1)) return true;
        for(T childTreenode : treenode.getChildNodes()) {
            if (TouchTreeNodesRecursively(treenode.getChildNodes(), childTreenode, intLevel)) return true;
        }
        return false;
    }

    private void TouchTreeNodesRecursivelyWithBranchDeptControl(ArrayList<T> parentArrayList, T treenode, int intLevel) {
        intLevel += 1;
        if (!ProcessTreeNode(parentArrayList, treenode, intLevel-1))  {
            // Only follow child nodes of previous ProcessTreeNode returned false
            for(T childTreenode : treenode.getChildNodes()) {
                TouchTreeNodesRecursively(treenode.getChildNodes(), childTreenode, intLevel);
            }
        }
    }

    /**
     * Get consumed when returning "true"
     * @param parentArrayList
     * @param treenode
     * @param intLevel
     * @return
     */
    public <T> boolean ProcessTreeNode(ArrayList<T> parentArrayList, T treenode, int intLevel) {
        return onNodeTouchedListener.onNode(parentArrayList, treenode, intLevel);
    }

}
