package com.example.powerupquadcopter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import androidx.fragment.app.Fragment;

public class Tab_Tuning extends Fragment {

    final int YAW = 0;
    final int PITCH = 1;
    final int ROLL = 2;

    private OnFragmentInteractionListener mListener;

    private LineGraphSeries<DataPoint> yawSeries, pitchSeries, rollSeries;
    private TextView yawText, pitchText, rollText;

    private int yawX = 0;
    private int pitchX = 0;
    private int rollX = 0;

    public static Tab_Settings newInstance() {
        return new Tab_Settings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("Tuning", "OnCreate");
    }

    //  pass in YAW/PITCH/ROLL
    void plotData(int itemType, double value) {
        getActivity().runOnUiThread(() -> {
            switch(itemType) {
                case YAW:
                    yawSeries.appendData(new DataPoint(yawX++, value),
                            true, 100);
                    yawText.setText(String.format("%.1f", value));
                case PITCH:
                    pitchSeries.appendData(new DataPoint(pitchX++, value),
                            true, 100);
                    pitchText.setText(String.format("%.1f", value));
                case ROLL:
                    rollSeries.appendData(new DataPoint(rollX++, value),
                            true, 100);
                    rollText.setText(String.format("%.1f", value));
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_tuning, container, false);
        GraphView yawGraph = view.findViewById(R.id.yawGraph);
        GraphView pitchGraph = view.findViewById(R.id.pitchGraph);
        GraphView rollGraph = view.findViewById(R.id.rollGraph);
        yawText = view.findViewById(R.id.textYaw);
        pitchText = view.findViewById(R.id.textPitch);
        rollText = view.findViewById(R.id.textRoll);

        yawSeries = new LineGraphSeries<>();
        pitchSeries = new LineGraphSeries<>();
        rollSeries = new LineGraphSeries<>();

        Viewport yawViewport = yawGraph.getViewport();
        yawViewport.setYAxisBoundsManual(true);
        yawViewport.setMinY(-20);
        yawViewport.setMaxY(20);
        yawViewport.setScrollable(true);

        Viewport pitchViewport = pitchGraph.getViewport();
        pitchViewport.setYAxisBoundsManual(true);
        pitchViewport.setMinY(-20);
        pitchViewport.setMaxY(20);
        pitchViewport.setScrollable(true);

        Viewport rollViewport = rollGraph.getViewport();
        rollViewport.setYAxisBoundsManual(true);
        rollViewport.setMinY(-20);
        rollViewport.setMaxY(20);
        rollViewport.setScrollable(true);

        plotData(YAW, 0);
        plotData(PITCH, 0);
        plotData(ROLL, 0);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_tuning, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
