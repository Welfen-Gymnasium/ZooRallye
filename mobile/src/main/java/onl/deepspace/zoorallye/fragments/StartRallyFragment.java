package onl.deepspace.zoorallye.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Tools;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStartRallyListener} interface
 * to handle interaction events.
 * Use the {@link StartRallyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartRallyFragment extends Fragment {

    private OnStartRallyListener mListener;

    public StartRallyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartRallyFragment.
     */
    public static StartRallyFragment newInstance(String param1, String param2) {
        StartRallyFragment fragment = new StartRallyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_rally, container, false);

        final Button startRally = (Button) view.findViewById(R.id.start_rally);
        startRally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRally();
            }
        });

        return view;
    }

    private void startRally() {
        final int questionsCount = Const.RALLY_QUESTION_COUNT;
        final int maxScore = questionsCount * Const.SCORE_AVERAGE;
        int currentMaxScore = 0;
        JSONObject questions = Tools.getQuestions(getContext());
        if (questions == null) {
            // TODO: 25.04.2016 Fetch questions
            return;
        }
        try {
            JSONArray checkbox = questions.getJSONArray(Const.QUESTION_TYPE_CHECKBOX);
            JSONArray radio = questions.getJSONArray(Const.QUESTION_TYPE_RADIO);
            JSONArray seekbar = questions.getJSONArray(Const.QUESTION_TYPE_SEEKBAR);
            JSONArray sort = questions.getJSONArray(Const.QUESTION_TYPE_SORT);
            JSONArray text = questions.getJSONArray(Const.QUESTION_TYPE_TEXT);
            JSONArray trueFalse = questions.getJSONArray(Const.QUESTION_TYPE_TRUE_FALSE);

            // TODO: 25.04.2016 Algorithm for selecting random questions for the rally
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStartRallyListener) {
            mListener = (OnStartRallyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartRallyListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStartRallyListener {
        void onStartRally(Bundle bundle);
    }
}
