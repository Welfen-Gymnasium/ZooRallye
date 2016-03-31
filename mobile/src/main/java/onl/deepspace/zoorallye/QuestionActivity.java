package onl.deepspace.zoorallye;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import onl.deepspace.zoorallye.QuestionFragments.QuestionCommunication;
import onl.deepspace.zoorallye.QuestionFragments.SortFragment;

public class QuestionActivity extends AppCompatActivity implements QuestionCommunication {
    // TODO: 31.03.2016 Implement way to change image in question fragments
    private static final String SORT_TAG = "sortTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        ArrayList<String> answers = new ArrayList<>();
        answers.add("Test 1");
        answers.add("Test 2");
        answers.add("Test 3");

        SortFragment fragment = SortFragment.newInstance("Question?", answers);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.question_root, fragment, SORT_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void submitSort(ArrayList<String> userAnswer, boolean isCorrect) {
        // TODO: 30.03.2016 Save and show user answer
        Toast.makeText(QuestionActivity.this, Boolean.toString(isCorrect), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void submitSeekbar(int userAnswer, int offset) {
        // TODO: 30.03.2016 Save and show user answer
    }

    @Override
    public void submitRadio(String userAnswer, boolean isCorrect) {
        // TODO: 30.03.2016 Save and show user answer
    }

    @Override
    public void submitCheckbox(ArrayList<String> userAnswer, float percentCorrect) {
        // TODO: 30.03.2016 Save and show user answer
    }

    @Override
    public void submitText(String userAnswer, boolean isCorrect) {
        // TODO: 30.03.2016 Save and show user answer
    }

    @Override
    public void submitTrueFalse(boolean userAnswer, boolean isCorrect) {
        // TODO: 30.03.2016 Save and show user answer
    }

    @Override
    public void reclineQuestion() {
        // TODO: 30.03.2016 Recline question
    }
}
