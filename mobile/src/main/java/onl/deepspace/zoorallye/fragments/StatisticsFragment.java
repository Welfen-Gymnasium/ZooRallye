package onl.deepspace.zoorallye.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.activities.AppCompatAchievementActivity;

public class StatisticsFragment extends Fragment {

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        ImageButton allAchievements = (ImageButton) view.findViewById(R.id.statistics_achievements_button);
        allAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ((AppCompatAchievementActivity) getActivity()).displayAchievements();
                } catch (Exception e){
                    Log.e(Const.LOGTAG, e.getLocalizedMessage());
                }
            }
        });

        Button setLeaderBoard100 = (Button) view.findViewById(R.id.statistics_leaderboard_submit_100);
        setLeaderBoard100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ((AppCompatAchievementActivity) getActivity()).submitLeaderBoardScore(
                            getResources().getString(
                                    R.string.leaderboard_total_question_points_earned), 100);
                } catch (Exception e){
                    Log.e(Const.LOGTAG, e.getLocalizedMessage());
                }
            }
        });

        Button setLeaderBoard200 = (Button) view.findViewById(R.id.statistics_leaderboard_submit_200);
        setLeaderBoard200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ((AppCompatAchievementActivity) getActivity()).submitLeaderBoardScore(
                            getResources().getString(
                                    R.string.leaderboard_total_question_points_earned), 200);
                } catch (Exception e){
                    Log.e(Const.LOGTAG, e.getLocalizedMessage());
                }
            }
        });

        ImageButton displayLeaderBoard = (ImageButton) view.findViewById(R.id.statistics_leaderboard_show);
        displayLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ((AppCompatAchievementActivity) getActivity()).displayLeaderBoard(
                            getResources().getString(
                                    R.string.leaderboard_total_question_points_earned));
                } catch (Exception e){
                    Log.e(Const.LOGTAG, e.getLocalizedMessage());
                }
            }
        });

        return view;
    }
}
