package onl.deepspace.zoorallye.questions;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionCommunication} interface
 * to handle interaction events.
 * Use the {@link TextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private static final String ARG_ANSWER = "answer";
    private static final String ARG_IMAGE = "image";

    private View mView;
    private QuestionCommunication mCommunicator;

    private String mQuestion;
    private String mAnswer;
    private String mImage;

    public TextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question The question.
     * @param answer The correct answer.
     * @return A new instance of fragment TextFragment.
     */
    public static TextFragment newInstance(String question, String answer, String image) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putString(ARG_ANSWER, answer);
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
            mAnswer = args.getString(ARG_ANSWER);
            mImage = args.getString(ARG_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragement_text, container, false);

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

        // Init text fragment
        TextView question = (TextView) mView.findViewById(R.id.question_text);
        question.setText(mQuestion);

        final Button recline = (Button) mView.findViewById(R.id.recline_text);
        recline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclineQuestion();
            }
        });

        final Button submit = (Button) mView.findViewById(R.id.submit_text);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer();
            }
        });

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCommunicator = null;
    }


    private void submitAnswer() {
        if (mCommunicator != null) {
            EditText userAnswerTextView = (EditText) mView.findViewById(R.id.question_text_answer);
            String userAnswer = userAnswerTextView.getText().toString();

            // TODO: 31.03.2016 Maybe add a better way to detect whether user answer was correct
            userAnswer = userAnswer.trim();
            boolean isCorrect = userAnswer.equalsIgnoreCase(mAnswer);

            mCommunicator.submitText(userAnswer, isCorrect);
        } else {
            Log.e(Const.LOGTAG,"mCommunicator is null: Did you implement QuestionCommunication to your activity?");
        }
    }

    private void reclineQuestion() {
        mCommunicator.reclineQuestion();
    }
}
