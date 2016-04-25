package onl.deepspace.zoorallye.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import onl.deepspace.zoorallye.R;

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
    // TODO: Rename and change types and number of parameters
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
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
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
