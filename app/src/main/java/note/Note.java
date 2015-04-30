package note;

import java.util.ArrayList;

/**
 * Created by HeinrichWork on 25/04/2015.
 */
public class Note {

    private ArrayList<NoteNode> childNoteNodes = new ArrayList<>();

    public ArrayList<NoteNode> getChildNodes() {
        return childNoteNodes;
    }

    public void setChildNodes(ArrayList<NoteNode> noteNodes) {
        this.childNoteNodes = noteNodes;
    }

    public void setNoteNodes(ArrayList<NoteNode> noteNodes) {
        this.childNoteNodes = noteNodes;
    }

    public void addChildNode(NoteNode childNode) {
        childNoteNodes.add(childNode);
        childNode.setParent(null);
        childNode.setNew(true);
    }
}
