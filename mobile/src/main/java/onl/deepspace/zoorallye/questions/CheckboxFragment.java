package onl.deepspace.zoorallye.questions;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Use the {@link CheckboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckboxFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private static final String ARG_ANSWERS = "answers";
    private static final String ARG_FALSE_ANSWERS = "falseAnswers";
    private static final String ARG_IMAGE = "image";

    private View mView;
    private QuestionCommunication mCommunicator;

    private String mQuestion;
    private ArrayList<String> mAnswers;
    private ArrayList<String> mAllAnswers;
    private String mImage;

    public CheckboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question The question.
     * @param answers All correct answers.
     * @param falseAnswers All false answers
     * @return A new instance of fragment CheckboxFragment.
     */
    public static CheckboxFragment newInstance(String question, ArrayList<String> answers,
                                               ArrayList<String> falseAnswers, String image) {
        CheckboxFragment fragment = new CheckboxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putStringArrayList(ARG_ANSWERS, answers);
        args.putStringArrayList(ARG_FALSE_ANSWERS, falseAnswers);
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
            mAnswers = args.getStringArrayList(ARG_ANSWERS);
            ArrayList<String> falseAnswers = args.getStringArrayList(ARG_FALSE_ANSWERS);
            mAllAnswers = shuffleAnswers(mAnswers, falseAnswers);
            mImage = args.getString(ARG_IMAGE);
        }
    }

    private ArrayList<String> shuffleAnswers(ArrayList<String> answers, ArrayList<String> falseAnswers) {
        ArrayList<String> shuffledAnswers = new ArrayList<>(falseAnswers);
        shuffledAnswers.addAll(answers);
        Collections.shuffle(shuffledAnswers);
        return shuffledAnswers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragement_checkbox, container, false);

        //Init communication with activity
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

        // Init checkbox fragment
        TextView question = (TextView) mView.findViewById(R.id.question_checkbox);
        question.setText(mQuestion);

        LinearLayout checkboxGroup = (LinearLayout) mView.findViewById(R.id.container_checkbox);

        for (int i = 0; i < mAllAnswers.size(); i++) {
            CheckBox checkBox = (CheckBox) inflater.inflate(R.layout.checkbox_view, checkboxGroup);
            checkBox.setHint(mAllAnswers.get(i));
            checkboxGroup.addView(checkBox);
        }

        final Button recline = (Button) mView.findViewById(R.id.recline_checkbox);
        recline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclineQuestion();
            }
        });

        final Button submit = (Button) mView.findViewById(R.id.submit_checkbox);
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
            LinearLayout checkboxGroup = (LinearLayout) mView.findViewById(R.id.container_radio);
            int correctItems = mAnswers.size();
            int correctUserItems = 0;

            ArrayList<String> userAnswer = getUserAnswer(checkboxGroup);
            for (int i = 0; i < userAnswer.size(); i++) {
                if (isCorrectAnswer(userAnswer.get(i))) correctUserItems++;
            }

            float percentCorrect = 100 / correctItems * correctUserItems;

            mCommunicator.submitCheckbox(userAnswer, percentCorrect);
        } else {
            Log.e(Const.LOGTAG,"mCommunicator is null: Did you implement QuestionCommunication to your activity?");
        }
    }

    private ArrayList<String> getUserAnswer(ViewGroup group) {
        ArrayList<String> userAnswer = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) group.getChildAt(i);
            if(checkBox.isChecked()) userAnswer.add((String) checkBox.getHint());
        }
        return userAnswer;
    }

    private boolean isCorrectAnswer(String userAnswer) {
        for (int i = 0; i < mAnswers.size(); i++) {
            if(userAnswer.equals(mAllAnswers.get(i))) return true;
        }
        return false;
    }

    private void reclineQuestion() {
        mCommunicator.reclineQuestion();
    }
}
