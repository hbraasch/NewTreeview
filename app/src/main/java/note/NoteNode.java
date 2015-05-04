package note;

import shared.Node;

/**
 * Created by HeinrichWork on 26/04/2015.
 */
public class NoteNode extends Node<NoteNode> {

    public NoteNode(String strDescription)
    {
        setDescription(strDescription);
    }

    private String strUuid;

    public String getUuid() {
        return strUuid;
    }

    public void setUuid(String strUuid) {
        this.strUuid = strUuid;
    }

}
