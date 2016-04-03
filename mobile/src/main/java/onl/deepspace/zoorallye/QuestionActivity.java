package onl.deepspace.zoorallye;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.questions.AnswerFragment;
import onl.deepspace.zoorallye.questions.CheckboxFragment;
import onl.deepspace.zoorallye.questions.QuestionCommunication;
import onl.deepspace.zoorallye.questions.RadioFragment;
import onl.deepspace.zoorallye.questions.SeekbarFragment;
import onl.deepspace.zoorallye.questions.SortFragment;
import onl.deepspace.zoorallye.questions.TextFragment;
import onl.deepspace.zoorallye.questions.TrueFalseFragment;

public class QuestionActivity extends AppCompatActivity implements QuestionCommunication {

    private String mQuestion;
    private String mType;
    private Bundle mBundle;
    private String mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        mQuestion = intent.getStringExtra(Const.INTENT_QUESTION);
        mType = intent.getStringExtra(Const.INTENT_QUESTION_TYPE);
        mBundle = intent.getBundleExtra(Const.INTENT_QUESTION_BUNDLE);
        mImage = intent.getStringExtra(Const.INTENT_QUESTION_IMAGE);

        setupQuestionFragment();
    }

    @Override
    public void submitSort(ArrayList<String> userAnswer, boolean isCorrect) {
        // TODO: 30.03.2016 Save and show user answer
        @SuppressWarnings("ConstantConditions")
        String correctAnswer = mBundle.getStringArrayList(Const.QUESTIONS_ANSWERS).toString();
        String scores = "100";
        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, userAnswer.toString(), correctAnswer, scores);
        openFragment(fragment);
    }

    @Override
    public void submitSeekbar(float userAnswer, float offset) {
        // TODO: 30.03.2016 Save and show user answer
        String correctAnswer = Float.toString(mBundle.getFloat(Const.QUESTIONS_ANSWER));
        String scores = "100";
        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, Float.toString(userAnswer), correctAnswer, scores);
        openFragment(fragment);
    }

    @Override
    public void submitRadio(String userAnswer, boolean isCorrect) {
        // TODO: 30.03.2016 Save and show user answer
        String correctAnswer = mBundle.getString(Const.QUESTIONS_ANSWER);
        String scores = "100";
        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, userAnswer, correctAnswer, scores);
        openFragment(fragment);
    }

    @Override
    public void submitCheckbox(ArrayList<String> userAnswer, float percentCorrect) {
        // TODO: 30.03.2016 Save and show user answer
        @SuppressWarnings("ConstantConditions")
        String correctAnswer = mBundle.getStringArrayList(Const.QUESTIONS_ANSWERS).toString();
        String scores = "100";
        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, userAnswer.toString(), correctAnswer, scores);
        openFragment(fragment);
    }

    @Override
    public void submitText(String userAnswer, boolean isCorrect) {
        // TODO: 30.03.2016 Save and show user answer
        @SuppressWarnings("ConstantConditions")
        String correctAnswer = mBundle.getString(Const.QUESTIONS_ANSWER);
        String scores = "100";
        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, userAnswer, correctAnswer, scores);
        openFragment(fragment);
    }

    @Override
    public void submitTrueFalse(boolean userAnswer, boolean isCorrect) {
        // TODO: 30.03.2016 Save and show user answer
        @SuppressWarnings("ConstantConditions")
        boolean correctAnswer = mBundle.getBoolean(Const.QUESTIONS_ANSWER);

        String correct = correctAnswer ? getString(R.string.answer_true) : getString(R.string.answer_false);
        String user = userAnswer ? getString(R.string.answer_true) : getString(R.string.answer_false);
        String scores = "100";

        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, user, correct, scores);
        openFragment(fragment);
    }

    @Override
    public void reclineQuestion() {
        // TODO: 30.03.2016 Recline question
    }

    @Override
    public void finishQuestion() {

    }

    private void setupQuestionFragment() {
        Fragment fragment;
        String answer;
        ArrayList<String> answers;
        ArrayList<String> falseAnswers;
        switch (mType) {
            case Const.QUESTION_TYPE_SORT:
                answers = mBundle.getStringArrayList(Const.QUESTIONS_ANSWERS);
                fragment = SortFragment.newInstance(mQuestion, answers, mImage);
                break;
            case Const.QUESTION_TYPE_SEEKBAR:
                float min = mBundle.getFloat(Const.QUESTIONS_MIN);
                float max = mBundle.getFloat(Const.QUESTIONS_MAX);
                float step = mBundle.getFloat(Const.QUESTIONS_STEP);
                float fAnswer = mBundle.getFloat(Const.QUESTIONS_ANSWER);
                fragment = SeekbarFragment.newInstance(mQuestion, min, max, step, fAnswer, mImage);
                break;
            case Const.QUESTION_TYPE_RADIO:
                answer = mBundle.getString(Const.QUESTIONS_ANSWER);
                falseAnswers = mBundle.getStringArrayList(Const.QUESTIONS_FALSE_ANSWERS);
                fragment = RadioFragment.newInstance(mQuestion, answer, falseAnswers, mImage);
                break;
            case Const.QUESTION_TYPE_CHECKBOX:
                answers = mBundle.getStringArrayList(Const.QUESTIONS_ANSWERS);
                falseAnswers = mBundle.getStringArrayList(Const.QUESTIONS_FALSE_ANSWERS);
                fragment = CheckboxFragment.newInstance(mQuestion, answers, falseAnswers, mImage);
                break;
            case Const.QUESTION_TYPE_TEXT:
                answer = mBundle.getString(Const.QUESTIONS_ANSWER);
                fragment = TextFragment.newInstance(mQuestion, answer, mImage);
                break;
            case Const.QUESTION_TYPE_TRUE_FALSE:
                boolean bAnswer = mBundle.getBoolean(Const.QUESTIONS_ANSWER);
                fragment = TrueFalseFragment.newInstance(mQuestion, bAnswer, mImage);
                break;
            default:
                throw new IllegalStateException("Type: " + mType + " not found.");
        }
        openFragment(fragment);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.attach(fragment);
        transaction.commit();
    }
}
