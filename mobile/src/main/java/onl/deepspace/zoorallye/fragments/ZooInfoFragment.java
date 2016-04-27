package onl.deepspace.zoorallye.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import onl.deepspace.zoorallye.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZooInfoFragment extends Fragment {


    public ZooInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zoo_info, container, false);

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

    private void openUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

}
