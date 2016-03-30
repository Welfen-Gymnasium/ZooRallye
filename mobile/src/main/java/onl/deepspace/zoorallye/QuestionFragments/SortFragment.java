package onl.deepspace.zoorallye.QuestionFragments;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import onl.deepspace.zoorallye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SortFragment extends ListFragment {
    // the fragment initialization parameters
    private static final String ARGS_QUESTION = "question";
    private static final String ARGS_ANSWERS = "answers";

    private ArrayAdapter<String> adapter;
    private String mQuestion;
    private ArrayList<String> mAnswers;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if(from != to) {
                String item = adapter.getItem(from);
                adapter.remove(item);
                adapter.insert(item, to);
            }
        }
    };

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            adapter.remove(adapter.getItem(which));
        }
    };

    protected int getLayout() {
        return R.layout.fragment_sort;
    }

    protected int getItemLayout() {
        return R.layout.sort_view;
    }

    private DragSortListView mDslv;
    private DragSortController mController;

    public int dragStartMode = DragSortController.ON_DOWN;

    public SortFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SortFragment.
     */
    public static SortFragment newInstance(String question, ArrayList<String> answers) {
        SortFragment fragment = new SortFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_QUESTION, question);
        args.putStringArrayList(ARGS_ANSWERS, answers);
        fragment.setArguments(args);
        return fragment;
    }

    public DragSortController getController() {
        return mController;
    }

    public void setListAdapter() {
        adapter = new ArrayAdapter<>(getActivity(), getItemLayout(), R.id.sort_item_text, mAnswers);
        setListAdapter(adapter);
    }

    public DragSortController buildController(DragSortListView dlsv) {
        DragSortController controller = new DragSortController(dlsv);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(dragStartMode);
        return controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mQuestion = args.getString(ARGS_QUESTION);
            mAnswers = args.getStringArrayList(ARGS_ANSWERS);
        }
        setListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        mDslv = (DragSortListView) view.findViewById(android.R.id.list);

        mController = buildController(mDslv);
        mDslv.setFloatViewManager(mController);
        mDslv.setOnTouchListener(mController);
        mDslv.setDropListener(onDrop);
        mDslv.setDragEnabled(true);

        return view;
    }
}