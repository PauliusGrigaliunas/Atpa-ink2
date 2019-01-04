package com.nex3z.tfmobilemnist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.fingerpaintview.FingerPaintView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawFragment extends Fragment {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.fpv_paint) FingerPaintView mFpvPaint;
    @BindView(R.id.tv_prediction) TextView mTvPrediction;
    @BindView(R.id.tv_probability) TextView mTvProbability;
    @BindView(R.id.tv_timecost) TextView mTvTimeCost;

    private Classifier mClassifier;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ButterKnife.bind(getActivity());
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_draw, container, false);
    }


    private void init() {
        try {
            mClassifier = new Classifier(getActivity());
        } catch (RuntimeException e) {
            Log.e(LOG_TAG, "Failed to create classifier.", e);
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
