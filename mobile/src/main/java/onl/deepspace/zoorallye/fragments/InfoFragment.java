package onl.deepspace.zoorallye.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Tools;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private static final String ARG_RALLY_ACTIVE = "rallyActive";
    private static final String ARG_SCORE = "score";
    private static final String ARG_ANSWERED_QUESTIONS = "answeredQuestions";

    private InfoFragmentCommunication mCommunicator;

    private boolean mRallyActive;
    private int mScore;
    private int mAnsweredQuestions;

    private TextView mScoreValue;
    private TextView mAnsweredValue;
    private TextView mRemainingValue;

    private AlertDialog mDialog;

    public static InfoFragment newInstance(boolean rallyActive, int score, int answeredQuestions) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_RALLY_ACTIVE, rallyActive);
        args.putInt(ARG_SCORE, score);
        args.putInt(ARG_ANSWERED_QUESTIONS, answeredQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mRallyActive = args.getBoolean(ARG_RALLY_ACTIVE, false);
            mScore = args.getInt(ARG_SCORE, 0);
            mAnsweredQuestions = args.getInt(ARG_ANSWERED_QUESTIONS, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        View stopRally = view.findViewById(R.id.fragment_zoo_info_stop_rally);

        View currentScore = view.findViewById(R.id.fragment_zoo_info_current_score);
        mScoreValue = (TextView) view
                .findViewById(R.id.fragment_zoo_info_current_score_value);

        View answeredQuestions = view.findViewById(R.id.fragment_zoo_info_answered_questions);
        mAnsweredValue = (TextView) view
                .findViewById(R.id.fragment_zoo_info_answered_questions_value);

        View remainingQuestion = view.findViewById(R.id.fragment_zoo_info_remaining_questions);
        mRemainingValue = (TextView) view
                .findViewById(R.id.fragment_zoo_info_remaining_questions_value);

        if (savedInstanceState != null) {
            mRallyActive = savedInstanceState.getBoolean(ARG_RALLY_ACTIVE);
            mScore = savedInstanceState.getInt(ARG_SCORE);
            mAnsweredQuestions = savedInstanceState.getInt(ARG_ANSWERED_QUESTIONS);
        }

        if (mRallyActive) {
            stopRally.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopRally();
                }
            });
            updateTextViews(mScore, mAnsweredQuestions);
        } else {
            stopRally.setVisibility(View.GONE);
            currentScore.setVisibility(View.GONE);
            answeredQuestions.setVisibility(View.GONE);
            remainingQuestion.setVisibility(View.GONE);
        }

        view.findViewById(R.id.fragment_zoo_info_opening_hours).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("http://www.zoo-augsburg.de/service/oeffnungszeiten/");
            }
        });

        view.findViewById(R.id.fragment_zoo_info_entrance_fee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("http://www.zoo-augsburg.de/service/eintrittspreise/");
            }
        });

        view.findViewById(R.id.fragment_zoo_info_arrival).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("http://www.zoo-augsburg.de/service/anreise/");
            }
        });

        view.findViewById(R.id.fragment_zoo_info_restaurants).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("http://www.zoo-augsburg.de/service/gastronomie/");
            }
        });

        return view;
    }

    private void stopRally() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.stop_rally));
        builder.setMessage(getString(R.string.really_stop_rally));
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.stop_rally_yes,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogConfirm();
            }
        });

        builder.setNegativeButton(R.string.stop_rally_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogDismiss();
            }
        });
        mDialog = builder.show();
    }

    private void dialogDismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void dialogConfirm() {
        if (mDialog != null) {
            mCommunicator.onStopRally();
            mDialog.dismiss();
        }
    }

    public void setScore(int score) {
        mScore = score;
        updateTextViews(mScore, mAnsweredQuestions);
    }

    public void setAnsweredQuestions(int count) {
        mAnsweredQuestions = count;
        updateTextViews(mScore, mAnsweredQuestions);
    }

    private void updateTextViews(int score, int answeredQuestions) {
        Locale locale = getResources().getConfiguration().locale;
        JSONObject questions = Tools.getQuestions(getContext());
        int questionCount = 0;
        try {
            questionCount = questions.getJSONArray(Const.QuestionsAPI_SLIDER).length() +
                            questions.getJSONArray(Const.QuestionsAPI_RADIO).length() +
                    questions.getJSONArray(Const.QuestionsAPI_CHECKBOX).length() +
                    questions.getJSONArray(Const.QuestionsAPI_SORT).length() +
                    questions.getJSONArray(Const.QuestionsAPI_TRUE_FALSE).length() +
                    questions.getJSONArray(Const.QuestionsAPI_TEXT).length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (mScoreValue != null)
            mScoreValue.setText(String.format(locale, "%d", score));
        if (mAnsweredValue != null)
            mAnsweredValue.setText(String.format(locale, "%d", answeredQuestions));
        if (mRemainingValue != null)
            mRemainingValue.setText(String.format(locale, "%d",
                     questionCount - answeredQuestions));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_RALLY_ACTIVE, mRallyActive);
        outState.putInt(ARG_SCORE, mScore);
        outState.putInt(ARG_ANSWERED_QUESTIONS, mAnsweredQuestions);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCommunicator = (InfoFragmentCommunication) context;
        } catch (ClassCastException e) {
            throw new IllegalStateException(context.getClass().getName() +
                    " must implement InforFragmentCommunication");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommunicator = null;
    }

    private void openUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public interface InfoFragmentCommunication {
        void onStopRally();
    }
}
