package note;

import java.util.ArrayList;

/**
 * Created by HeinrichWork on 26/04/2015.
 */
public class NoteNode extends NoteNodeBase<NoteNode> {

    public NoteNode(String strDescription)
    {
        setDescription(strDescription);
    }

    @Override
    public ArrayList<NoteNode> getChildNodes() {
        return children;
    }

    @Override
    public NoteNode getParent() {
        return parent;
    }

    public void addChildNode(NoteNode childNode) {
        children.add(childNode);
        childNode.setParent(this);
    }
}
