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

    public void addChildNode(NoteNode childNode) {
        childNoteNodes.add(childNode);
        childNode.setParent(this);
        childNode.setNew(true);
    }
}
