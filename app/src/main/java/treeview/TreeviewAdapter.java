package treeview;

import java.util.ArrayList;

import note.Node;

/**
 * Created by HeinrichWork on 25/04/2015.
 */
public abstract class TreeviewAdapter<T extends Node<T>> {

    Treeview treeview;
    ArrayList<T> sourceChildNodes;

    protected TreeviewAdapter(ArrayList<T> sourceChildNodes) {
        this.sourceChildNodes = sourceChildNodes;
    }

    public abstract TreeviewNode convertSourceToTreeNode(T sourceNode);

    public ArrayList<TreeviewNode> adapt() {
        ArrayList<TreeviewNode> rootTreenodes = new ArrayList<>();
        for (T sourceChildNode : sourceChildNodes) {
            TreeviewNode treeviewNode = convertSourceToTreeNode(sourceChildNode);
            treeviewNode.setParent(null);
            rootTreenodes.add(treeviewNode);
            TouchTreeNodesRecursively(treeviewNode, sourceChildNode);
        }
        rootTreenodes = correctExpansionStatus(rootTreenodes);
        return rootTreenodes;
    }

    private ArrayList<TreeviewNode> correctExpansionStatus(ArrayList<TreeviewNode> rootTreenodes)  {
        TreeIterator<TreeviewNode> iterator = new TreeIterator<TreeviewNode>(rootTreenodes);
        iterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treenode, int intLevel) {
                if (treenode.getExpansionState() == Treeview.EnumTreenodeExpansionState.EMPTY) {
                    if (!treenode.getChildNodes().isEmpty()) {
                        treenode.setExpansionState(Treeview.EnumTreenodeExpansionState.EXPANDED);
                    }
                }
                return false;
            }
        });
        return rootTreenodes;
    }

    private void TouchTreeNodesRecursively(TreeviewNode treeviewNode, T sourceNode) {
        treeviewNode.setChildTreeviewNodes(new ArrayList<TreeviewNode>());
        for(T sourceChildNode : sourceNode.getChildNodes()) {
            TreeviewNode treeviewNodeNew = convertSourceToTreeNode(sourceChildNode);
            treeviewNodeNew.setParent(treeviewNode);
            treeviewNode.getChildNodes().add(treeviewNodeNew);
            TouchTreeNodesRecursively(treeviewNodeNew, sourceChildNode);
        }
    }

    public void notifyDataSetChanged() {
        this.treeview.setChildNodes(adapt());
        this.treeview.invalidate();
    }

    public void setTreeview(Treeview treeview) {
        this.treeview = treeview;
    }

    public ArrayList<T> getSourceChildNodes() {
        return sourceChildNodes;
    }

    public void setSourceChildNodes(ArrayList<T> sourceChildNodes) {
        this.sourceChildNodes = sourceChildNodes;
    }
}
