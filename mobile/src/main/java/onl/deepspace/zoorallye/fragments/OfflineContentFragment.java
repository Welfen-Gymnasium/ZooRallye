package onl.deepspace.zoorallye.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;

/**
 * A fragment representing a list of Items.
 */
public class OfflineContentFragment extends Fragment implements
        MyOfflineContentRecyclerViewAdapter.OfflineItemCommunication{

    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OfflineContentFragment() {
    }

    @SuppressWarnings("unused")
    public static OfflineContentFragment newInstance() {
        return new OfflineContentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offlinecontent_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            ArrayList<OfflineItem> list = new ArrayList<>();
            list.add(new OfflineItem("questions", "Fragen", false));
            list.add(new OfflineItem("4P1shyVmM4", "Zoo Augsburg", true));
            RecyclerView.Adapter adapter = new MyOfflineContentRecyclerViewAdapter(list, this);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onItemClick(OfflineItem item) {
        Log.d(Const.LOGTAG, item.toString());
        // TODO: 19.04.2016 Fetch data from Server
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
