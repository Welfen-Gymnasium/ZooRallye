package onl.deepspace.zoorallye.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Question;

/**
 * A fragment representing a list of Items.
 *
 * Created by Dennis on 25.04.2016.
 */
public class BeaconsOverlayFragment extends Fragment {

    private static final String ARG_BEACON_TYPE = "beaconType";
    private static final String ARG_ANIMALS = "animals";
    private static final String ARG_QUESTIONS = "questions";

    private String mBeaconType;
    private ArrayList<String> mAnimals;
    private ArrayList<Question> mQuestions;

    public BeaconsOverlayFragment() {
    }

    @SuppressWarnings("unused")
    public static BeaconsOverlayFragment newInstance(String beaconType, ArrayList<String> animals,
                                                     ArrayList<Question> questions) {
        BeaconsOverlayFragment fragment = new BeaconsOverlayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BEACON_TYPE, beaconType);
        args.putStringArrayList(ARG_ANIMALS, animals);
        args.putParcelableArrayList(ARG_QUESTIONS, questions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mBeaconType = args.getString(ARG_BEACON_TYPE);
            mAnimals = args.getStringArrayList(ARG_ANIMALS);
            mQuestions = args.getParcelableArrayList(ARG_QUESTIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beacons_overlay, container, false);

        TextView beaconType = (TextView) view.findViewById(R.id.beacon_type);
        beaconType.setText(mBeaconType);

        ViewGroup animalList = (ViewGroup) view.findViewById(R.id.animal_list);

        animalList.removeViewsInLayout(1, animalList.getChildCount() - 1);

        for (String animal : mAnimals) {
            TextView animalItem = (TextView) inflater
                    .inflate(R.layout.beacon_overlay_animal_item, animalList);
            animalItem.setText(animal);
            animalList.addView(animalItem);
        }

        ViewGroup questionList = (ViewGroup) view.findViewById(R.id.question_list);

        for (Question question : mQuestions) {
            ViewGroup questionItem = (ViewGroup) inflater
                    .inflate(R.layout.beacon_overlay_question_item, questionList);

            TextView questionType = (TextView) questionItem.findViewById(R.id.question_type);
            questionType.setText(question.getType());

            TextView questionAnimal = (TextView) questionItem.findViewById(R.id.question_animal);
            String animal = question.getAnimal();
            if (animal != null) questionAnimal.setText(animal);

            TextView questionScore = (TextView) questionItem.findViewById(R.id.question_score);
            questionScore.setText(question.getScore());

            ImageView questionState = (ImageView) questionItem.findViewById(R.id.question_state);
            int state = question.getState();
            int iconId;
            switch (state) {
                case Question.STATE_UNKNOWN:
                    iconId = R.drawable.ic_unkown_circle;
                    break;
                case Question.STATE_CORRECT:
                    iconId = R.drawable.ic_check_circle;
                    break;
                case Question.STATE_WRONG:
                    iconId = R.drawable.ic_cross_circle;
                    break;
                default:
                    iconId = 0;
            }
            if (iconId != 0) questionState.setImageResource(iconId);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
