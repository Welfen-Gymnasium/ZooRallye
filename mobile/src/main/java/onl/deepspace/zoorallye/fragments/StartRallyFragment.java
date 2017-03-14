package onl.deepspace.zoorallye.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Question;
import onl.deepspace.zoorallye.helper.Tools;
import onl.deepspace.zoorallye.helper.services.DataFetcher;
import onl.deepspace.zoorallye.helper.services.DataFetcher.DownloadResultReceiver;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStartRallyListener} interface
 * to handle interaction events.
 * Use the {@link StartRallyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartRallyFragment extends Fragment {//} implements DataFetcher.DownloadResultReceiver.Receiver{

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

        view.findViewById(R.id.start_rally).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRally();
            }
        });

        return view;
    }

    private void startRally() {
        final int questionsCount = Const.RALLY_QUESTION_COUNT;
        // final int maxScore = questionsCount * Const.SCORE_AVERAGE;
        // int currentMaxScore = 0;
        JSONObject questions = Tools.getQuestions(getContext());
        /*if (questions == null) {
            Log.d(Const.LOGTAG, "Get Questions");
            DataFetcher.DownloadResultReceiver mReceiver;

            mReceiver = new DownloadResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), DataFetcher.class);

            // Send optional extras to Download IntentService
            intent.putExtra("url", "http://api.deepspace.onl/zoorallye/questions");
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("requestId", 101);

            getActivity().startService(intent);
            return;
        }*/
        try {
            JSONArray checkbox = questions.getJSONArray(Const.QUESTION_TYPE_CHECKBOX);
            JSONArray radio = questions.getJSONArray(Const.QUESTION_TYPE_RADIO);
            JSONArray seekbar = questions.getJSONArray(Const.QUESTION_TYPE_SEEKBAR);
            JSONArray sort = questions.getJSONArray(Const.QUESTION_TYPE_SORT);
            JSONArray text = questions.getJSONArray(Const.QUESTION_TYPE_TEXT);
            JSONArray trueFalse = questions.getJSONArray(Const.QUESTION_TYPE_TRUE_FALSE);

            ArrayList<Question> allQuestions = new ArrayList<>();
            allQuestions = addToArrayList(allQuestions, checkbox, Const.QUESTION_TYPE_CHECKBOX);
            allQuestions = addToArrayList(allQuestions, radio, Const.QUESTION_TYPE_RADIO);
            allQuestions = addToArrayList(allQuestions, seekbar, Const.QUESTION_TYPE_SEEKBAR);
            allQuestions = addToArrayList(allQuestions, sort, Const.QUESTION_TYPE_SORT);
            allQuestions = addToArrayList(allQuestions, text, Const.QUESTION_TYPE_TEXT);
            allQuestions = addToArrayList(allQuestions, trueFalse, Const.QUESTION_TYPE_TRUE_FALSE);

            Collections.shuffle(allQuestions);
            // TODO NO-IMPORTANCE: 26.04.2016 make questions equally valuable
            Log.d(Const.LOGTAG, String.valueOf(allQuestions));

            ArrayList<Question> rallyQuestions;
            try {
                rallyQuestions = allQuestions;
            } catch (IndexOutOfBoundsException e) {
                Log.e(Const.LOGTAG, "Too less questions fetched");
                rallyQuestions = allQuestions;
            }

            mListener.onStartRally(rallyQuestions);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
            //Maybe wrong JSON saved, delete it for new fetch
            getContext().deleteFile(Tools.QUESTIONS_DB);
        } catch (NullPointerException e){
            Log.w(Const.LOGTAG, "Questions not fetched resulted in " + e.getMessage());
        }

    }

    private ArrayList<Question> addToArrayList(
            ArrayList<Question> array, JSONArray jsonArray, String type)
            throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            array.add(new Question(type, jsonArray.getJSONObject(i)));
        }
        return array;
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

    /*@Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case DataFetcher.STATUS_RUNNING:
                break;
            case DataFetcher.STATUS_FINISHED:
                // Handle results
                String result = resultData.getString(DataFetcher.RESULT);
                Log.i(Const.LOGTAG, "New Questions received, saving them! " + result);

                startRally();

                break;
            case DataFetcher.STATUS_ERROR:
                // Handle the error
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Log.e(Const.LOGTAG, error);
                break;
        }
    }*/

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
        void onStartRally(ArrayList<Question> questions);
    }
}
