package note;

import android.graphics.drawable.Drawable;

/**
 * Created by HeinrichWork on 26/04/2015.
 */
public class NoteNode extends Node<NoteNode> {

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
