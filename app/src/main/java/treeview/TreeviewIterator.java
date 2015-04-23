package treeview;

import java.util.ArrayList;

/**
 * Created by HeinrichWork on 4/04/2015.
 */
public class TreeviewIterator {

    private ArrayList<Treenode> rootTreenodes;
    private OnNodeTouchedListener onNodeTouchedListener;

    public interface OnNodeTouchedListener {
        /**
         *
         * @param parentArrayList parent collection containing this node
         * @param treenode the current node under inspection
         * @param intLevel indentation level (topmost level index = 0)
         * @return if true, itteration stops immediately. If false, itteration continue
         */
        public boolean onNode(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel);
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

    public void execute(OnNodeTouchedListener onNodeTouchedListener) {
        int intLevel;
        this.onNodeTouchedListener = onNodeTouchedListener;
        for (Treenode rootTreenode : rootTreenodes) {
            intLevel = 0;
            TouchTreeNodesRecursively(rootTreenodes, rootTreenode, intLevel);
        }
    }

    public ArrayList<Treenode> Find(final FindClause findClause) {
        final ArrayList<Treenode> foundTreeNodes = new ArrayList<Treenode>();
        execute(new OnNodeTouchedListener() {
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
