package onl.deepspace.zoorallye.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Question;

/**
 * A fragment representing a list of Items.
 *
 * Created by Dennis on 25.04.2016.
 */
public class BeaconsOverlayFragment extends Fragment {

    public BeaconsOverlayFragment() {
    }

    @SuppressWarnings("unused")
    public static BeaconsOverlayFragment newInstance(ArrayList<String> animals, ArrayList<Question> questions) {
        return new BeaconsOverlayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beacons_overlay, container, false);
        // TODO: 25.04.2016 Change values dynamically with data of real beacon 
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
