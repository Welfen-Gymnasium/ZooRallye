package onl.deepspace.zoorallye.questions;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionCommunication} interface
 * to handle interaction events.
 * Use the {@link SeekbarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("unused")
public class SeekbarFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private static final String ARG_MIN = "min";
    private static final String ARG_MAX = "max";
    private static final String ARG_STEP = "step";
    private static final String ARG_ANSWER = "answer";
    private static final String ARG_IMAGE = "image";

    private View mView;
    private QuestionCommunication mCommunicator;

    private String mQuestion;
    private double mMin;
    private double mMax;
    private double mStep;
    private double mAnswer;
    private String mImage;

    public SeekbarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question The question.
     * @param min The minimum the seekbar can be.
     * @param max The maximum the seekbar can be.
     * @param step The step size in which the seekbar should increase.
     * @param answer The correct answer.
     * @return A new instance of fragment SeekbarFragment.
     */
    public static SeekbarFragment newInstance(String question, double min, double max,
                                              double step, double answer, String image) {
        SeekbarFragment fragment = new SeekbarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putDouble(ARG_MIN, min);
        args.putDouble(ARG_MAX, max);
        args.putDouble(ARG_STEP, step);
        args.putDouble(ARG_ANSWER, answer);
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
            mMin = args.getDouble(ARG_MIN);
            mMax = args.getDouble(ARG_MAX);
            mStep = args.getDouble(ARG_STEP);
            mAnswer = args.getDouble(ARG_ANSWER);
            mImage = args.getString(ARG_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_seekbar, container, false);

        // Init communication with activity
        try {
            mCommunicator = (QuestionCommunication) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement QuestionCommunication");
        }

        // Setup image
        ImageView image = (ImageView) mView.findViewById(R.id.question_image);
        Resources res = getResources();
        int id = 0;
        if (mImage != null)
            id = res.getIdentifier(mImage, "drawable", getContext().getPackageName());
        if (id == 0)
            id = res.getIdentifier("img_drawer", "drawable", getContext().getPackageName());
        image.setImageResource(id);

        // Init seekbar fragment
        TextView question = (TextView) mView.findViewById(R.id.question);
        question.setText(mQuestion);

        // TODO: 30.03.2016 Animation for sliding in to position
        final CustomSeekBar customSeekBar = (CustomSeekBar) mView.findViewById(R.id.seekBar);
        customSeekBar.setFloatRange(mMin, mMax, mStep);

        Locale locale = getResources().getConfiguration().locale;
        final NumberFormat format = NumberFormat.getInstance(locale);

        final TextView label = (TextView) mView.findViewById(R.id.label_seekbar);
        label.setText(format.format(customSeekBar.getFloatProgress()));

        customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                label.setText(format.format(customSeekBar.getFloatProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        TextView min = (TextView) mView.findViewById(R.id.seekbar_min);
        min.setText(format.format(mMin));

        TextView max = (TextView) mView.findViewById(R.id.seekbar_max);
        max.setText(format.format(mMax));

        final Button recline = (Button) mView.findViewById(R.id.recline_seekbar);
        recline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclineQuestion();
            }
        });

        final Button submit = (Button) mView.findViewById(R.id.submit_seekbar);
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
            CustomSeekBar seekbar = (CustomSeekBar) mView.findViewById(R.id.seekBar);
            double userAnswer = seekbar.getFloatProgress();
            double offset = Math.abs(userAnswer - mAnswer);
            mCommunicator.submitSeekbar(userAnswer, offset);
        } else {
            Log.e(Const.LOGTAG,"mCommunicator is null: Did you implement QuestionCommunication to your activity?");
        }
    }

    private void reclineQuestion() {
        mCommunicator.reclineQuestion();
    }
}
