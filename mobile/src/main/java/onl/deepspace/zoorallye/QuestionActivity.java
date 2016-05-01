package onl.deepspace.zoorallye;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.Transition;
import android.util.Log;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Question;
import onl.deepspace.zoorallye.helper.Tools;
import onl.deepspace.zoorallye.questions.AnswerFragment;
import onl.deepspace.zoorallye.questions.CheckboxFragment;
import onl.deepspace.zoorallye.questions.QuestionCommunication;
import onl.deepspace.zoorallye.questions.RadioFragment;
import onl.deepspace.zoorallye.questions.SeekbarFragment;
import onl.deepspace.zoorallye.questions.SortFragment;
import onl.deepspace.zoorallye.questions.TextFragment;
import onl.deepspace.zoorallye.questions.TrueFalseFragment;

public class QuestionActivity extends AppCompatActivity implements QuestionCommunication {

    private static final String ARG_ANSWERED = "answered";
    private static final String ARG_USER_ANSWER = "userAnswer";
    private static final String ARG_CORRECT_ANSWER = "correctAnswer";
    private static final String ARG_SCORE = "score";

    private Question mQuestionObject;
    private String mQuestion;
    private String mQuestionId;
    private String mType;
    private JSONObject mValues;
    private String mImage;
    private Fragment mActiveFragment;

    private boolean mAnswered;
    private String mUserAnswer;
    private String mCorrectAnswer;
    private int mScore;

    private NumberFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Locale locale = getResources().getConfiguration().locale;
        format = NumberFormat.getInstance(locale);

        Intent intent = getIntent();
        mQuestionObject = intent.getParcelableExtra(Const.QUESTION);
        mQuestion = mQuestionObject.getQuestion();
        mQuestionId = mQuestionObject.getId();
        mType = mQuestionObject.getType();
        mValues = mQuestionObject.getValue();
        mImage = mQuestionObject.getImage();

        if (savedInstanceState != null) {
            mAnswered = savedInstanceState.getBoolean(ARG_ANSWERED, false);
            mUserAnswer = savedInstanceState.getString(ARG_USER_ANSWER, "");
            mCorrectAnswer = savedInstanceState.getString(ARG_CORRECT_ANSWER, "");
            mScore = savedInstanceState.getInt(ARG_SCORE, 0);
        }

        try {
            setupQuestionFragment();
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    @Override
    public void submitSort(ArrayList<String> userAnswer, float percentCorrect) {
        mQuestionObject
                .setState(percentCorrect == 1f ? Question.STATE_CORRECT : Question.STATE_WRONG);
        String correctAnswer = "";
        try {
            correctAnswer = mValues.getString(Const.QUESTIONS_ANSWERS);

        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
        int score = (int) (Const.SCORE_SORT * percentCorrect);

        mAnswered = true;
        mUserAnswer = userAnswer.toString();
        mCorrectAnswer = correctAnswer;
        mScore = score;

        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, mUserAnswer, mCorrectAnswer,
                Integer.toString(mScore));
        showAnswer(fragment);

        JSONObject answer = new JSONObject();
        try {
            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String visitId = "" + date + '.' + month + '.' + year + '.';

            answer.put(Const.QUESTION_VISIT_ID, visitId);
            answer.put(Const.QUESTION_TYPE, Const.QUESTION_TYPE_SORT);
            answer.put(Const.QUESTION_ID, mQuestionId);
            answer.put(Const.QUESTION_ANSWER, userAnswer.toString());
            answer.put(Const.QUESTION_SCORE, score);

            Tools.insertAnswer(this, answer);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    @Override
    public void submitSeekbar(double userAnswer, double offset) {
        mQuestionObject.setState(offset == 0d ? Question.STATE_CORRECT : Question.STATE_WRONG);
        double min = 0;
        double max = 0;
        double correctAnswer = 0;
        try {
            min = mValues.getDouble(Const.QUESTIONS_MIN);
            max = mValues.getDouble(Const.QUESTIONS_MAX);
            correctAnswer = mValues.getDouble(Const.QUESTIONS_ANSWER);

        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }

        double upperMax = max - correctAnswer;
        double lowerMax = correctAnswer - min;
        double maxDist = upperMax > lowerMax ? upperMax : lowerMax;

        int step = (int) (Const.SCORE_SEEKBAR / maxDist);
        int score = (int) (Const.SCORE_SEEKBAR - (step * offset));

        mAnswered = true;
        mUserAnswer = format.format(userAnswer);
        mCorrectAnswer = format.format(correctAnswer);
        mScore = score;

        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, mUserAnswer, mCorrectAnswer,
                Integer.toString(mScore));
        showAnswer(fragment);

        JSONObject answer = new JSONObject();
        try {
            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String visitId = "" + date + '.' + month + '.' + year + '.';

            answer.put(Const.QUESTION_VISIT_ID, visitId);
            answer.put(Const.QUESTION_TYPE, Const.QUESTION_TYPE_SEEKBAR);
            answer.put(Const.QUESTION_ID, mQuestionId);
            answer.put(Const.QUESTION_ANSWER, format.format(userAnswer));
            answer.put(Const.QUESTION_SCORE, score);

            Tools.insertAnswer(this, answer);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    @Override
    public void submitRadio(String userAnswer, boolean isCorrect) {
        mQuestionObject.setState(isCorrect ? Question.STATE_CORRECT : Question.STATE_WRONG);
        String correctAnswer = "";
        try {
            correctAnswer = mValues.getString(Const.QUESTIONS_ANSWER);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
        int score = isCorrect ? Const.SCORE_RADIO : 0;
        mAnswered = true;
        mUserAnswer = userAnswer;
        mCorrectAnswer = correctAnswer;
        mScore = score;

        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, mUserAnswer, mCorrectAnswer,
                Integer.toString(mScore));
        showAnswer(fragment);

        JSONObject answer = new JSONObject();
        try {
            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String visitId = "" + date + '.' + month + '.' + year + '.';

            answer.put(Const.QUESTION_VISIT_ID, visitId);
            answer.put(Const.QUESTION_TYPE, Const.QUESTION_TYPE_RADIO);
            answer.put(Const.QUESTION_ID, mQuestionId);
            answer.put(Const.QUESTION_ANSWER, userAnswer);
            answer.put(Const.QUESTION_SCORE, score);

            Tools.insertAnswer(this, answer);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    @Override
    public void submitCheckbox(ArrayList<String> userAnswer, float percentCorrect) {
        mQuestionObject
                .setState(percentCorrect == 1f ? Question.STATE_CORRECT : Question.STATE_WRONG);
        String correctAnswer = "";
        try {
            correctAnswer = mValues.getString(Const.QUESTIONS_ANSWERS);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }

        int score = (int) (Const.SCORE_CHECKBOX * percentCorrect);

        mAnswered = true;
        mUserAnswer = userAnswer.toString();
        mCorrectAnswer = correctAnswer;
        mScore = score;

        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, mUserAnswer, mCorrectAnswer,
                Integer.toString(mScore));
        showAnswer(fragment);

        JSONObject answer = new JSONObject();
        try {
            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String visitId = "" + date + '.' + month + '.' + year + '.';

            answer.put(Const.QUESTION_VISIT_ID, visitId);
            answer.put(Const.QUESTION_TYPE, Const.QUESTION_TYPE_CHECKBOX);
            answer.put(Const.QUESTION_ID, mQuestionId);
            answer.put(Const.QUESTION_ANSWER, userAnswer.toString());
            answer.put(Const.QUESTION_SCORE, score);

            Tools.insertAnswer(this, answer);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    @Override
    public void submitText(String userAnswer, boolean isCorrect) {
        mQuestionObject.setState(isCorrect ? Question.STATE_CORRECT : Question.STATE_WRONG);
        String correctAnswer = "";
        try {
            correctAnswer = mValues.getString(Const.QUESTIONS_ANSWER);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }

        int score = isCorrect ? Const.SCORE_TEXT : 0;

        mAnswered = true;
        mUserAnswer = userAnswer;
        mCorrectAnswer = correctAnswer;
        mScore = score;

        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, mUserAnswer, mCorrectAnswer,
                Integer.toString(mScore));
        showAnswer(fragment);

        JSONObject answer = new JSONObject();
        try {
            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String visitId = "" + date + '.' + month + '.' + year + '.';

            answer.put(Const.QUESTION_VISIT_ID, visitId);
            answer.put(Const.QUESTION_TYPE, Const.QUESTION_TYPE_TEXT);
            answer.put(Const.QUESTION_ID, mQuestionId);
            answer.put(Const.QUESTION_ANSWER, userAnswer);
            answer.put(Const.QUESTION_SCORE, score);

            Tools.insertAnswer(this, answer);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    @Override
    public void submitTrueFalse(boolean userAnswer, boolean isCorrect) {
        mQuestionObject.setState(isCorrect ? Question.STATE_CORRECT : Question.STATE_WRONG);
        boolean correctAnswer = false;
        try {
            correctAnswer = mValues.getBoolean(Const.QUESTIONS_ANSWER);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }

        String correct = correctAnswer ? getString(R.string.answer_true) : getString(R.string.answer_false);
        String user = userAnswer ? getString(R.string.answer_true) : getString(R.string.answer_false);
        int score = isCorrect ? Const.SCORE_TRUE_FALSE : 0;

        mAnswered = true;
        mUserAnswer = user;
        mCorrectAnswer = correct;
        mScore = score;

        AnswerFragment fragment = AnswerFragment.newInstance(mQuestion, mUserAnswer, mCorrectAnswer,
                Integer.toString(mScore));
        showAnswer(fragment);

        JSONObject answer = new JSONObject();
        try {
            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String visitId = "" + date + '.' + month + '.' + year + '.';

            answer.put(Const.QUESTION_VISIT_ID, visitId);
            answer.put(Const.QUESTION_TYPE, Const.QUESTION_TYPE_TRUE_FALSE);
            answer.put(Const.QUESTION_ID, mQuestionId);
            answer.put(Const.QUESTION_ANSWER, Boolean.toString(userAnswer));
            answer.put(Const.QUESTION_SCORE, score);

            Tools.insertAnswer(this, answer);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    @Override
    public void reclineQuestion() {
        finish();
    }

    @Override
    public void finishQuestion() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Const.QUESTION, mQuestionObject);
        resultIntent.putExtra(Const.QUESTION_SCORE, mScore);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void showAnswer(AnswerFragment fragment) {
        ViewGroup root = (ViewGroup) findViewById(R.id.question_root);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Scene answer = Scene.getSceneForLayout(root, R.layout.fragment_answer, this);
            Transition mAutoTransition = new AutoTransition();
            // TransitionManager.go(answer, mAutoTransition);
        }
        openFragment(fragment);
    }

    private void setupQuestionFragment() throws JSONException {
        Fragment fragment;
        if(mAnswered) {
            fragment = AnswerFragment.newInstance(mQuestion, mUserAnswer, mCorrectAnswer,
                    Integer.toString(mScore));
        } else {
            String answer;
            ArrayList<String> answers;
            ArrayList<String> falseAnswers;
            switch (mType) {
                case Const.QUESTION_TYPE_SORT:
                    answers = Tools.string2ArrayList(mValues.getString(Const.QUESTIONS_ANSWERS));
                    fragment = SortFragment.newInstance(mQuestion, answers, mImage);
                    break;
                case Const.QUESTION_TYPE_SEEKBAR:
                    double min = mValues.getDouble(Const.QUESTIONS_MIN);
                    double max = mValues.getDouble(Const.QUESTIONS_MAX);
                    double step = mValues.getDouble(Const.QUESTIONS_STEP);
                    double fAnswer = mValues.getDouble(Const.QUESTIONS_ANSWER);
                    fragment = SeekbarFragment.newInstance(mQuestion, min, max, step, fAnswer, mImage);
                    break;
                case Const.QUESTION_TYPE_RADIO:
                    answer = mValues.getString(Const.QUESTIONS_ANSWER);
                    falseAnswers =
                            Tools.string2ArrayList(mValues.getString(Const.QUESTIONS_FALSE_ANSWERS));
                    fragment = RadioFragment.newInstance(mQuestion, answer, falseAnswers, mImage);
                    break;
                case Const.QUESTION_TYPE_CHECKBOX:
                    answers = Tools.string2ArrayList(mValues.getString(Const.QUESTIONS_ANSWERS));
                    falseAnswers =
                            Tools.string2ArrayList(mValues.getString(Const.QUESTIONS_FALSE_ANSWERS));
                    fragment = CheckboxFragment.newInstance(mQuestion, answers, falseAnswers, mImage);
                    break;
                case Const.QUESTION_TYPE_TEXT:
                    answer = mValues.getString(Const.QUESTIONS_ANSWER);
                    fragment = TextFragment.newInstance(mQuestion, answer, mImage);
                    break;
                case Const.QUESTION_TYPE_TRUE_FALSE:
                    boolean bAnswer = mValues.getBoolean(Const.QUESTIONS_ANSWER);
                    fragment = TrueFalseFragment.newInstance(mQuestion, bAnswer, mImage);
                    break;
                default:
                    throw new IllegalStateException("Type: " + mType + " not found.");
            }
        }
        openFragment(fragment);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(mActiveFragment != null)
            transaction.remove(mActiveFragment);
        transaction.add(R.id.question_root, fragment);
        transaction.commit();
        mActiveFragment = fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(ARG_ANSWERED, mAnswered);
        outState.putString(ARG_USER_ANSWER, mUserAnswer);
        outState.putString(ARG_CORRECT_ANSWER, mCorrectAnswer);
        outState.putInt(ARG_SCORE, mScore);
    }
}
