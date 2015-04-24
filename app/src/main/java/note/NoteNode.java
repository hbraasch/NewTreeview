package note;

import java.util.ArrayList;

import treeview.Node;

/**
 * Created by HeinrichWork on 25/04/2015.
 */
public class NoteNode extends Node<NoteNode> {

    // Node unique key
    public String strTreenodeUuid;

    @Override
    public ArrayList<NoteNode> getChildNodes() {
        return null;
    }

    @Override
    public NoteNode getParent() {
        return null;
    }
}
