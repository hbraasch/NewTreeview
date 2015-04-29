package note;

import java.util.ArrayList;

/**
 * Created by HeinrichWork on 25/04/2015.
 */
public class Note {

    private ArrayList<NoteNode> noteNodes = new ArrayList<>();

    public ArrayList<NoteNode> getChildNodes() {
        return noteNodes;
    }

    public void setChildNodes(ArrayList<NoteNode> noteNodes) {
        this.noteNodes = noteNodes;
    }

    public void setNoteNodes(ArrayList<NoteNode> noteNodes) {
        this.noteNodes = noteNodes;
    }
}
