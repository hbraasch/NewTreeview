package treeview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.treeapps.newtreeview.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class Treeview {

    public enum EnumTreenodeExpansionState {
        EXPANDED, COLLAPSED, EMPTY
    }

    public static enum EnumTreenodeListItemLevelRelation {
        SAME, HIGHER, LOWER;
    }

    private Map<Integer,Drawable> iconImages = new HashMap<>();
    private ArrayList<Treenode> childTreenodes = new ArrayList<Treenode>();

    private Context context;
    private ArrayList<ListViewListItem> listViewListItems;
    private TreeviewArrayAdapter arrayAdapter;
    private ListView listView;
    private int intTreeNodeLayoutId;



    public Treeview(Context context, ListView listView, int intTreeNodeLayoutId, Map<Integer,Drawable> iconImages) {
        this.context = context;
        this.listView = listView;
        this.intTreeNodeLayoutId = intTreeNodeLayoutId;
        this.iconImages = iconImages;

        listViewListItems = new ArrayList<ListViewListItem> ();
        arrayAdapter = new TreeviewArrayAdapter(context,intTreeNodeLayoutId, listViewListItems);
        listView.setAdapter(arrayAdapter);
    }

    // Getter Setters

    public Map<Integer, Drawable> getIconImages() {
        return iconImages;
    }

    public void setIconImages(Map<Integer, Drawable> iconImages) {
        this.iconImages = iconImages;
    }

    public ArrayList<Treenode> getChildTreenodes() {
        return childTreenodes;
    }

    public void setChildTreenodes(ArrayList<Treenode> childTreenodes) {
        this.childTreenodes = childTreenodes;
    }

    // Methods

    public void addNode(Treenode treenode) {
        getChildTreenodes().add(treenode);
    }

    public void removeNode(final Treenode removeTreenode) {
        TreeviewIterator treeviewIterator = new TreeviewIterator(getChildTreenodes());
        treeviewIterator.execute(new TreeviewIterator.OnNodeTouchedListener() {
            @Override
            public boolean onNode(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel) {
                if (treenode.equals(removeTreenode)) {
                    parentArrayList.remove(treenode);
                    return true;
                }
                return false;
            }
        });
    }

    private ArrayList<ListViewListItem> generateListItems() {
        final ArrayList<ListViewListItem> listViewListItemsNew = new ArrayList<ListViewListItem>();

        TreeviewIterator treeviewIterator = new TreeviewIterator(getChildTreenodes());
        treeviewIterator.execute(new TreeviewIterator.OnNodeTouchedListener() {
            @Override
            public boolean onNode(ArrayList<Treenode> parentArrayList, Treenode treenode, int intLevel) {
                ListViewListItem listViewListItem = new ListViewListItem(treenode, intLevel, EnumTreenodeListItemLevelRelation.LOWER,EnumTreenodeListItemLevelRelation.LOWER);
                listViewListItemsNew.add(listViewListItem);
                return false;
            }
        });
        return listViewListItemsNew;
    }

    /**
     * Redraw
     */
    public void invalidate() {
        listViewListItems = generateListItems();
        arrayAdapter.clear();
        arrayAdapter.addAll(listViewListItems);
        arrayAdapter.notifyDataSetChanged();
    }



    // Helper classes
    class ListViewListItem {

        private Treenode treenode;
        private int intLevel;
        private EnumTreenodeListItemLevelRelation enumAboveRelation, enumBelowRelation;

        // Getters Setters


        public Treenode getTreenode() {
            return treenode;
        }

        public void setTreenode(Treenode treenode) {
            this.treenode = treenode;
        }

        public int getLevel() {
            return intLevel;
        }

        public void setLevel(int intLevel) {
            this.intLevel = intLevel;
        }

        public EnumTreenodeListItemLevelRelation getEnumAboveRelation() {
            return enumAboveRelation;
        }

        public void setEnumAboveRelation(EnumTreenodeListItemLevelRelation enumAboveRelation) {
            this.enumAboveRelation = enumAboveRelation;
        }

        public EnumTreenodeListItemLevelRelation getEnumBelowRelation() {
            return enumBelowRelation;
        }

        public void setEnumBelowRelation(EnumTreenodeListItemLevelRelation enumBelowRelation) {
            this.enumBelowRelation = enumBelowRelation;
        }

        ListViewListItem(Treenode treenode, int intLevel, EnumTreenodeListItemLevelRelation enumAboveRelation, EnumTreenodeListItemLevelRelation enumBelowRelation) {
            this.treenode = treenode;
            this.intLevel = intLevel;
            this.enumAboveRelation = enumAboveRelation;
            this.enumBelowRelation = enumBelowRelation;
        }
    }

    class TreeviewArrayAdapter extends ArrayAdapter<ListViewListItem> {

        private int intResourceId;
        private  List<ListViewListItem> listViewListItems;

        public TreeviewArrayAdapter(Context context, int intResourceId, List<ListViewListItem> listViewListItems) {
            super(context, intResourceId, listViewListItems);
            this.intResourceId = intResourceId;
            this.listViewListItems = listViewListItems;
        }

        public int getResourceId() {
            return intResourceId;
        }

        public void setResourceId(int intResourceId) {
            this.intResourceId = intResourceId;
        }

        public List<ListViewListItem> getListViewListItems() {
            return listViewListItems;
        }

        public void setListViewListItems(List<ListViewListItem> listViewListItems) {
            this.listViewListItems = listViewListItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RelativeLayout relativeView;
            ListViewListItem listViewListItem = getItem(position);

            if (convertView == null) {
                relativeView = new RelativeLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater layoutInflater;
                layoutInflater = (LayoutInflater) getContext().getSystemService(inflater);
                layoutInflater.inflate(intResourceId, relativeView, true);
            } else {
                relativeView = (RelativeLayout) convertView;
            }

            IndentableTextView textViewDescription = (IndentableTextView) relativeView.findViewById(R.id.treenode_description);
            textViewDescription.setText(listViewListItem.getLevel() + ": " + listViewListItem.getTreenode().getDescription());
            return relativeView;
        }
    }
}
