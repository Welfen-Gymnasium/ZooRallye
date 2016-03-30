package onl.deepspace.zoorallye.QuestionFragments;

import java.util.ArrayList;

/**
 * Created by Dennis on 30.03.2016.
 */
public interface QuestionCommunication {
    void reclineQuestion();
    void submitSort(ArrayList<String> userAnswer, boolean isCorrect);
    void submitSeekbar(int userAnswer, int offset);
    void submitRadio(String userAnswer, boolean isCorrect);
    void submitCheckbox(ArrayList<String> userAnswer, float percentCorrect);
    void submitText(String userAnswer, boolean isCorrect);
    void submitTrueFalse(boolean userAnswer, boolean isCorrect);
}
