package treeview;

import java.util.ArrayList;

import shared.Node;

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
        return rootTreenodes;
    }

    private void TouchTreeNodesRecursively(TreeviewNode treeviewNode, T sourceNode) {
        treeviewNode.setChildNodes(new ArrayList<TreeviewNode>());
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
