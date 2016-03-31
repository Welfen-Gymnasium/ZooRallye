package onl.deepspace.zoorallye.QuestionFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionCommunication} interface
 * to handle interaction events.
 * Use the {@link RadioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadioFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private static final String ARG_ANSWER = "answer";
    private static final String ARG_FALSE_ANSWERS = "falseAnswers";

    private View mView;
    private QuestionCommunication mCommunicator;

    private String mQuestion;
    private String mAnswer;
    private ArrayList<String> mAllAnswers;

    public RadioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question The question.
     * @param answer The correct answer.
     * @param falseAnswers All false answers.
     * @return A new instance of fragment RadioFragment.
     */
    public static RadioFragment newInstance(String question, String answer, ArrayList<String> falseAnswers) {
        RadioFragment fragment = new RadioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putString(ARG_ANSWER, answer);
        args.putStringArrayList(ARG_FALSE_ANSWERS, falseAnswers);
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
            ArrayList<String> falseAnswers = args.getStringArrayList(ARG_FALSE_ANSWERS);
            mAllAnswers = shuffleAnswers(mAnswer, falseAnswers);
        }
    }

    private ArrayList<String> shuffleAnswers(String answer, ArrayList<String> falseAnswers) {
        ArrayList<String> allAnswers = new ArrayList<>(falseAnswers);
        allAnswers.add(answer);
        Collections.shuffle(allAnswers);
        return allAnswers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_radio, container, false);

        // Init communication with activity
        try {
            mCommunicator = (QuestionCommunication) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement QuestionCommunication");
        }

        // Init radio fragment
        TextView question = (TextView) mView.findViewById(R.id.question_radio);
        question.setText(mQuestion);

        RadioGroup radioGroup = (RadioGroup) mView.findViewById(R.id.container_radio);

        for (int i = 0; i < mAllAnswers.size(); i++) {
            RadioButton button = (RadioButton) inflater.inflate(R.layout.radio_view, radioGroup);
            String answer = mAllAnswers.get(i);
            button.setHint(answer);
            radioGroup.addView(button);
        }

        final Button recline = (Button) mView.findViewById(R.id.recline_radio);
        recline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclineQuestion();
            }
        });

        final Button submit = (Button) mView.findViewById(R.id.submit_radio);
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
            RadioGroup radioGroup = (RadioGroup) mView.findViewById(R.id.container_radio);
            RadioButton checkedButton = getCheckedRadioButton(radioGroup);
            String userAnswer = (String) checkedButton.getHint();
            boolean isCorrect = userAnswer.equals(mAnswer);
            mCommunicator.submitRadio(userAnswer, isCorrect);
        } else {
            Log.e(Const.LOGTAG,"mCommunicator is null: Did you implement QuestionCommunication to your activity?");
        }
    }

    private RadioButton getCheckedRadioButton(RadioGroup radioGroup) {
        RadioButton checkedRadioButton = null;
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) radioGroup.getChildAt(i);
            if(button.isChecked()) {
                if (checkedRadioButton == null) checkedRadioButton = button;
                else Log.w(Const.LOGTAG, "There are two radio buttons checked at once");
            }
        }
        return checkedRadioButton;
    }

    private void reclineQuestion() {
        mCommunicator.reclineQuestion();
    }
}
