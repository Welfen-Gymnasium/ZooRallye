package onl.deepspace.zoorallye.questions;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionCommunication} interface
 * to handle interaction events.
 * Use the {@link TrueFalseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("unused")
public class TrueFalseFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private static final String ARG_ANSWER = "answer";
    private static final String ARG_IMAGE = "image";

    private QuestionCommunication mCommunicator;

    private String mQuestion;
    private boolean mAnswer;
    private String mImage;

    public TrueFalseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question The question.
     * @param answer The correct answer.
     * @return A new instance of fragment TrueFalseFragment.
     */
    public static TrueFalseFragment newInstance(String question, boolean answer, String image) {
        TrueFalseFragment fragment = new TrueFalseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putBoolean(ARG_ANSWER, answer);
        args.putString(ARG_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mQuestion = args.getString(ARG_QUESTION);
            mAnswer = args.getBoolean(ARG_ANSWER);
            mImage = args.getString(ARG_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_true_false, container, false);

        // Init communication with activity
        try {
            mCommunicator = (QuestionCommunication) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement QuestionCommunication");
        }

        // Setup image
        ImageView image = (ImageView) mView.findViewById(R.id.question_image);
        if (mImage == null) image.setVisibility(View.GONE);
        else {
            Resources res = getResources();
            int resId = res.getIdentifier(mImage, "drawable", getActivity().getPackageName());
            image.setImageResource(resId);
        }

        // Init radio fragment
        TextView question = (TextView) mView.findViewById(R.id.question_true_false);
        question.setText(mQuestion);

        final Button recline = (Button) mView.findViewById(R.id.recline_true_false);
        recline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclineQuestion();
            }
        });

        final Button answerTrue = (Button) mView.findViewById(R.id.answer_true);
        answerTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer(true);
            }
        });

        final Button answerFalse = (Button) mView.findViewById(R.id.answer_false);
        answerFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer(false);
            }
        });

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCommunicator = null;
    }

    private void submitAnswer(boolean userAnswer) {
        if (mCommunicator != null) {
            boolean isCorrect = userAnswer == mAnswer;
            mCommunicator.submitTrueFalse(userAnswer, isCorrect);
        } else {
            Log.e(Const.LOGTAG,"mCommunicator is null: Did you implement QuestionCommunication to your activity?");
        }
    }

    private void reclineQuestion() {
        mCommunicator.reclineQuestion();
    }
}
