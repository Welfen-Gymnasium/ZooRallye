package onl.deepspace.zoorallye.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Question;
import onl.deepspace.zoorallye.helper.Strings;

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

    private ViewGroup mQuestionList;

    private BeaconOverlayListener mCommunicator;

    public BeaconsOverlayFragment() {
    }

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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beacons_overlay, container, false);

        TextView beaconType = (TextView) view.findViewById(R.id.beacon_type);
        beaconType.setText(Strings.getBeaconType(getContext(), mBeaconType));

        ViewGroup animalList = (ViewGroup) view.findViewById(R.id.animal_list);

        animalList.removeViewsInLayout(1, animalList.getChildCount() - 1);

        for (String animal : mAnimals) {
            TextView animalItem = (TextView) inflater
                    .inflate(R.layout.beacon_overlay_animal_item, animalList, false);
            animalItem.setText(Strings.getAnimal(getContext(), animal));
            animalList.addView(animalItem);
        }

        mQuestionList = (ViewGroup) view.findViewById(R.id.question_list);
        mQuestionList.removeViewsInLayout(1, mQuestionList.getChildCount() - 1);

        if(mQuestions != null){
            for (Question question : mQuestions) {
                ViewGroup questionItem = (ViewGroup) inflater
                        .inflate(R.layout.beacon_overlay_question_item, mQuestionList, false);

                TextView questionType = (TextView) questionItem.findViewById(R.id.question_type);
                questionType.setText(Strings.getType(getContext(), question.getType()));

                TextView questionAnimal = (TextView) questionItem.findViewById(R.id.question_animal);
                String animal = question.getAnimal();
                if (animal != null) questionAnimal.setText(Strings.getAnimal(getContext(), animal));

                TextView questionScore = (TextView) questionItem.findViewById(R.id.question_score);
                questionScore.setText(question.getScore() +
                        getContext().getString(R.string.points_suffix));

                ImageView questionState = (ImageView) questionItem.findViewById(R.id.question_state);
                int state = question.getState();
                int iconId, colorId = 0;
                switch (state) {
                    case Question.STATE_UNKNOWN:
                        iconId = R.drawable.ic_unkown_circle;
                        break;
                    case Question.STATE_CORRECT:
                        iconId = R.drawable.ic_check_circle;
                        colorId = getResources().getColor(R.color.answerTrue);
                        break;
                    case Question.STATE_WRONG:
                        iconId = R.drawable.ic_cross_circle;
                        colorId = getResources().getColor(R.color.answerFalse);
                        break;
                    default:
                        iconId = 0;
                        colorId = 0;
                }
                if (iconId != 0) questionState.setImageResource(iconId);
                if (colorId != 0) questionItem.setBackgroundColor(colorId);

                questionItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        questionClicked(v);
                    }
                });

                mQuestionList.addView(questionItem);
            }
        }

        ImageView hideOverlay = (ImageView) view.findViewById(R.id.hide_beacon_overlay);
        hideOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommunicator.hideBeaconOverlay();
            }
        });

        return view;
    }

    private void questionClicked(View view) {
        int index = getIndexForView(mQuestionList, view) - 1;
        if (index >= 0 && index < mQuestions.size()) {
            Question question = mQuestions.get(index);
            mCommunicator.showQuestion(question);
        } else {
            Log.e(Const.LOGTAG, "Didn't found view");
        }
    }

    private int getIndexForView(ViewGroup group, View view) {
        for (int i = 0; i < group.getChildCount(); i++) {
            if(view == group.getChildAt(i)) return i;
        }
        return -1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BeaconOverlayListener) {
            mCommunicator = (BeaconOverlayListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BeaconOverlayListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDestroyView();
        mCommunicator = null;
    }

    public interface BeaconOverlayListener {
        void hideBeaconOverlay();
        void showQuestion(Question question);
    }

}
