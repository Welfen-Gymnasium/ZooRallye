package onl.deepspace.zoorallye.fragments;

/**
 * Created by Dennis on 19.04.2016.
 *
 * An item representing an item in
 * {@link onl.deepspace.zoorallye.fragments.OfflineContentFragment}
 */
public class OfflineItem {
    public String id;
    public String name;
    public boolean offline;

    public OfflineItem(String id, String name, boolean offline) {
        this.id = id;
        this.name = name;
        this.offline = offline;
    }

    @Override
    public String toString() {
        return name;
    }
}