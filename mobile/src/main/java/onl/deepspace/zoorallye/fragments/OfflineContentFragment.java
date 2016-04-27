package onl.deepspace.zoorallye.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import onl.deepspace.zoorallye.MainActivity;
import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Tools;
import onl.deepspace.zoorallye.helper.services.DataFetcher;
import onl.deepspace.zoorallye.lib.OfflineContentRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 */
public class OfflineContentFragment extends Fragment implements
        OfflineContentRecyclerViewAdapter.OfflineItemCommunication, DataFetcher.DownloadResultReceiver.Receiver{

    private int mColumnCount = 1;
    private View recycler;

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

        recycler = view.findViewById(R.id.offline_fragment_recycler);
        
        // Set the adapter
        if (recycler instanceof RecyclerView) {
            Context context = recycler.getContext();
            RecyclerView recyclerView = (RecyclerView) recycler;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            boolean zoosFetched = Tools.getZoos(getContext(), false) != null;
            boolean questionsFetched = Tools.getQuestions(getContext(), false) != null;

            ArrayList<OfflineItem> list = new ArrayList<>();
            list.add(new OfflineItem("questions", getResources().getString(R.string.questions), questionsFetched));
            list.add(new OfflineItem("4P1shyVmM4", "Zoo Augsburg", zoosFetched));
            RecyclerView.Adapter adapter = new OfflineContentRecyclerViewAdapter(list, this);
            recyclerView.setAdapter(adapter);
        }

        view.findViewById(R.id.fragment_offline_content_delete_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.deleteAppData(getContext());
                refreshFragment();
            }
        });

        return view;
    }

    @Override
    public void onItemClick(OfflineItem item) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), DataFetcher.class);
        DataFetcher.DownloadResultReceiver mReceiver;

        mReceiver = new DataFetcher.DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        // Send optional extras to Download IntentService
        String url = item.id.equals("questions") ? Const.QuestionsAPI : Const.ZOOS_API + item.id;
        intent.putExtra("url", url);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 2001);

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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        refreshFragment();
    }

    private void refreshFragment(){
        ((MainActivity) getActivity()).openFragment(((MainActivity) getActivity()).getFragmentByID(R.id.nav_offline));
    }
}
