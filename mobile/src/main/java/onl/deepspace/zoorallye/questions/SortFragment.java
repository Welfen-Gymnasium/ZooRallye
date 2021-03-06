package onl.deepspace.zoorallye.questions;

import android.support.v4.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.Collections;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;

/**
 * A simple {@link ListFragment} subclass.
 * Use the {@link SortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SortFragment extends ListFragment {
    // the fragment initialization parameters
    private static final String ARGS_QUESTION = "question";
    private static final String ARGS_ANSWERS = "answers";
    private static final String ARG_IMAGE = "image";

    private QuestionCommunication mCommunicator;

    private ArrayAdapter<String> adapter;
    private String mQuestion;
    private ArrayList<String> mAnswers;
    private String mImage;

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

    protected int getLayout() {
        return R.layout.fragment_sort;
    }

    protected int getItemLayout() {
        return R.layout.sort_view;
    }

    public int dragStartMode = DragSortController.ON_DOWN;

    public SortFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question The question.
     * @param answers All answers which have to be sorted.
     * @return A new instance of fragment SortFragment.
     */
    public static SortFragment newInstance(String question, ArrayList<String> answers, String image) {
        SortFragment fragment = new SortFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_QUESTION, question);
        args.putStringArrayList(ARGS_ANSWERS, answers);
        args.putString(ARG_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListAdapter() {
        ArrayList<String> shuffledAnswers = getShuffledAnswers();
        adapter = new ArrayAdapter<>(getActivity(), getItemLayout(), R.id.sort_item_text, shuffledAnswers);
        setListAdapter(adapter);
    }

    private ArrayList<String> getShuffledAnswers() {
        ArrayList<String> shuffledAnswers = new ArrayList<>(mAnswers);
        Collections.shuffle(shuffledAnswers);
        return shuffledAnswers;
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
            mImage = args.getString(ARG_IMAGE);
        }
        setListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(getLayout(), container, false);

        // Init communication with activity
        try {
            mCommunicator = (QuestionCommunication) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement QuestionCommunication");
        }

        // Setup image
        ImageView image = (ImageView) mView.findViewById(R.id.question_image);
        Resources res = getResources();
        int id = 0;
        if (mImage != null)
            id = res.getIdentifier(mImage, "drawable", getContext().getPackageName());
        if (id == 0)
            id = res.getIdentifier("img_drawer", "drawable", getContext().getPackageName());
        image.setImageResource(id);

        // Init sort fragment
        TextView question = (TextView) mView.findViewById(R.id.question);
        question.setText(mQuestion);

        final Button submit = (Button) mView.findViewById(R.id.submit_sort);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer();
            }
        });

        final Button recline = (Button) mView.findViewById(R.id.recline_sort);
        recline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclineQuestion();
            }
        });

        DragSortListView mDragSort = (DragSortListView) mView.findViewById(android.R.id.list);

        DragSortController mController = buildController(mDragSort);
        mDragSort.setFloatViewManager(mController);
        mDragSort.setOnTouchListener(mController);
        mDragSort.setDropListener(onDrop);
        mDragSort.setDragEnabled(true);

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCommunicator = null;
    }

    private void submitAnswer() {
        if(mCommunicator != null) {
            int correctItems = mAnswers.size();
            int correctUserItems = 0;

            ArrayList<String> userAnswer = getUserAnswer();
            for (int i = 0; i < userAnswer.size(); i++) {
                if(mAnswers.get(i).equals(userAnswer.get(i))) correctUserItems++;
            }

            float percentCorrect = (100 * correctUserItems) / correctItems;
            percentCorrect /= 100;

            mCommunicator.submitSort(userAnswer, percentCorrect);
        } else {
            Log.e(Const.LOGTAG, "mCommunicator is null: Did you implement QuestionCommunication to your activity?");
        }
    }

    private ArrayList<String> getUserAnswer() {
        ArrayList<String> userAnswer = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            userAnswer.add(adapter.getItem(i));
        }
        return userAnswer;
    }

    private void reclineQuestion() {
        mCommunicator.reclineQuestion();
    }
}