package treeview;

import java.util.ArrayList;

/**
 * Created by HeinrichWork on 4/04/2015.
 */
public class TreeviewIterator {

    private ArrayList<Treenode> rootTreenodes;
    private OnTouchAllNodesListener onNodeTouchedListener;

    public interface OnTouchAllNodesListener {
        /**
         * Traverse downwards through treeview, touching all child nodes, starting from the constructor presented childnode collection
         * @param parentArrayList parent collection containing this node
         * @param treenode the current node under inspection
         * @param intLevel indentation level (topmost level index = 0)
         * @return if true, itteration stops immediately. If false, itteration continue
         */
        public boolean onNode(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel);
    }

    public interface OnTouchAllParentNodesListener {
        /**
         * Traverse upwards through treeview, touching all parent nodes till root (treenode == null) is reached
         * @param treenode the current node under inspection
         * @return if true, itteration stops immediately. If false, itteration continue
         */
        public boolean onParentNode(Treenode treenode);
    }




    public interface FindClause {
        /**
         * User needs to generate the find condition inside this method
         * @param treenode
         * @return
         */
        public boolean isNodeFound(Treenode treenode);
    }

    public TreeviewIterator(ArrayList<Treenode> rootTreenodes) {
        this.rootTreenodes = rootTreenodes;
    }

    public void execute(OnTouchAllNodesListener onTouchAllNodesListener) {
        int intLevel;
        this.onNodeTouchedListener = onTouchAllNodesListener;
        for (Treenode rootTreenode : rootTreenodes) {
            intLevel = 0;
            TouchTreeNodesRecursively(rootTreenodes, rootTreenode, intLevel);
        }
    }

    public void execute(Treenode startNode, OnTouchAllParentNodesListener onTouchAllParentNodesListener) {
        Treenode parent = startNode.getParent();
        while (parent != null) {
            onTouchAllParentNodesListener.onParentNode(parent);
        }
        onTouchAllParentNodesListener.onParentNode(null);
    }

    public ArrayList<Treenode> Find(final FindClause findClause) {
        final ArrayList<Treenode> foundTreeNodes = new ArrayList<Treenode>();
        execute(new OnTouchAllNodesListener() {
            @Override
            public boolean onNode(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel) {
                if (findClause.isNodeFound(treenode)) {
                    foundTreeNodes.add(treenode);
                }
                return false;
            }
        });
        return foundTreeNodes;
    }


    private boolean TouchTreeNodesRecursively(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel) {
        intLevel += 1;
        if (ProcessTreeNode(parentArrayList, treenode, intLevel-1)) return true;
        for(Treenode childTreenode : treenode.getChildTreenodes()) {
            if (TouchTreeNodesRecursively(treenode.getChildTreenodes(), childTreenode, intLevel)) return true;
        }
        return false;
    }

    /**
     * Get consumed when returning "true"
     * @param parentArrayList
     * @param objTreeNode
     * @param intLevel
     * @return
     */
    public boolean ProcessTreeNode(ArrayList<Treenode> parentArrayList, Treenode objTreeNode, int intLevel) {
        return onNodeTouchedListener.onNode(parentArrayList, objTreeNode, intLevel);
    }

}
