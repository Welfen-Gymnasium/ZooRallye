package onl.deepspace.zoorallye.questions;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import onl.deepspace.zoorallye.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionCommunication} interface
 * to handle interaction events.
 * Use the {@link AnswerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private static final String ARG_USER = "userAnswer";
    private static final String ARG_CORRECT = "correctAnswer";
    private static final String ARG_SCORES = "scores";

    private View mView;
    private QuestionCommunication mCommunicator;

    private String mQuestion;
    private String mUser;
    private String mCorrect;
    private String mScores;

    public AnswerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question The question the user answered.
     * @param userAnswer The answer the user gave.
     * @param correctAnswer The correct answer to the question.
     * @param scores The scores the user got for his answer.
     * @return A new instance of fragment AnswerFragment.
     */
    public static AnswerFragment newInstance(String question, String userAnswer, String correctAnswer, String scores) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putString(ARG_USER, userAnswer);
        args.putString(ARG_CORRECT, correctAnswer);
        args.putString(ARG_SCORES, scores);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mQuestion = args.getString(ARG_QUESTION);
            mUser = args.getString(ARG_USER);
            mCorrect = args.getString(ARG_CORRECT);
            mScores = args.getString(ARG_SCORES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_answer, container, false);

        // Init communication with activity
        try {
            mCommunicator = (QuestionCommunication) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement QuestionCommunication");
        }

        // Set TextViews with infos about the previous answered question
        TextView question = (TextView) mView.findViewById(R.id.answer_question);
        question.setText(mQuestion);

        TextView user = (TextView) mView.findViewById(R.id.answer_user);
        user.setText(mUser);

        TextView correct = (TextView) mView.findViewById(R.id.answer_correct);
        correct.setText(mCorrect);

        TextView scores = (TextView) mView.findViewById(R.id.answer_scores);
        scores.setText(mScores);

        // Set on click listener for finishing question
        ImageView forward = (ImageView) mView.findViewById(R.id.answer_forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommunicator.finishQuestion();
            }
        });

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCommunicator = null;
    }
}
