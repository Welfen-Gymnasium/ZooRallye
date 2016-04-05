package onl.deepspace.zoorallye.questions;

import java.util.ArrayList;

/**
 * Created by Dennis on 30.03.2016.
 *
 * Interface for Communication with calling activity and the questions fragments
 */
public interface QuestionCommunication {
    void reclineQuestion();
    void submitSort(ArrayList<String> userAnswer, float percentCorrect);
    void submitSeekbar(float userAnswer, float offset);
    void submitRadio(String userAnswer, boolean isCorrect);
    void submitCheckbox(ArrayList<String> userAnswer, float percentCorrect);
    void submitText(String userAnswer, boolean isCorrect);
    void submitTrueFalse(boolean userAnswer, boolean isCorrect);
    void finishQuestion();
}
