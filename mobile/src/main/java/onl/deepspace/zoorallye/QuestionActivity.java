package onl.deepspace.zoorallye;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import onl.deepspace.zoorallye.questions.QuestionCommunication;

public class QuestionActivity extends AppCompatActivity implements QuestionCommunication {
    // TODO: 31.03.2016 Implement way to change image in question fragments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }

    @Override
    public void submitSort(ArrayList<String> userAnswer, boolean isCorrect) {
        // TODO: 30.03.2016 Save and show user answer
        Toast.makeText(QuestionActivity.this, Boolean.toString(isCorrect), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void submitSeekbar(float userAnswer, float offset) {
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
