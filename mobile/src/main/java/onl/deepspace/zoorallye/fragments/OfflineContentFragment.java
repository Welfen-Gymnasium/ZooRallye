package onl.deepspace.zoorallye.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.services.DataFetcher;
import onl.deepspace.zoorallye.lib.OfflineContentRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 */
public class OfflineContentFragment extends Fragment implements
        OfflineContentRecyclerViewAdapter.OfflineItemCommunication{

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

        // TODO NO-IMPORTANCE: 25.04.2016 complete full behavior

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
            RecyclerView.Adapter adapter = new OfflineContentRecyclerViewAdapter(list, this);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onItemClick(OfflineItem item) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), DataFetcher.class);

        // Send optional extras to Download IntentService
        String url = item.id.equals("questions") ? Const.QuestionsAPI : Const.ZOOS_API + item.id;
        intent.putExtra("url", url);
        intent.putExtra("requestId", item.id.hashCode());

        getContext().startService(intent);
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
