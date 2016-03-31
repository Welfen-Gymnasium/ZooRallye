package onl.deepspace.zoorallye.questions;

import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.Collections;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SortFragment extends ListFragment {
    // the fragment initialization parameters
    private static final String ARGS_QUESTION = "question";
    private static final String ARGS_ANSWERS = "answers";

    private QuestionCommunication mCommunicator;

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
    public static SortFragment newInstance(String question, ArrayList<String> answers) {
        SortFragment fragment = new SortFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_QUESTION, question);
        args.putStringArrayList(ARGS_ANSWERS, answers);
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

        // Init sort fragment
        TextView question = (TextView) mView.findViewById(R.id.question_sort);
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

        DragSortListView mDslv = (DragSortListView) mView.findViewById(android.R.id.list);

        DragSortController mController = buildController(mDslv);
        mDslv.setFloatViewManager(mController);
        mDslv.setOnTouchListener(mController);
        mDslv.setDropListener(onDrop);
        mDslv.setDragEnabled(true);

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCommunicator = null;
    }

    private void submitAnswer() {
        if(mCommunicator != null) {
            ArrayList<String> userAnswer = getUserAnswer();
            boolean isCorrect = checkAnswer(userAnswer);
            mCommunicator.submitSort(userAnswer, isCorrect);
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

    private boolean checkAnswer(ArrayList<String> userAnswer) {
        boolean answer = true;
        for (int i = 0; i < mAnswers.size(); i++) {
            if(!mAnswers.get(i).equals(userAnswer.get(i))) answer = false;
        }
        return answer;
    }

    private void reclineQuestion() {
        mCommunicator.reclineQuestion();
    }
}