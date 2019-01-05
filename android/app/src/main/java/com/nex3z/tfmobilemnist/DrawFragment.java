package com.nex3z.tfmobilemnist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.fingerpaintview.FingerPaintView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawFragment extends Fragment {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FingerPaintView mFpvPaint;
    private TextView mTvPrediction;
    private TextView mTvProbability;
    private TextView mTvTimeCost;
    private Button clearButton, detectButton;

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

        final View view = inflater.inflate(R.layout.fragment_draw, container, false);
        clearButton = (Button) view.findViewById(R.id.btn_clear2);
        detectButton = (Button) view.findViewById(R.id.btn_detect);
        mFpvPaint = view.findViewById(R.id.fpv_paint2);
        mTvPrediction = view.findViewById(R.id.tv_prediction);
        mTvProbability = view.findViewById(R.id.tv_probability);
        mTvTimeCost = view.findViewById(R.id.tv_timecost);

        clearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mFpvPaint.clear();
                mTvPrediction.setText(R.string.empty);
                mTvProbability.setText(R.string.empty);
                mTvTimeCost.setText(R.string.empty);
            }
        });

        detectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mClassifier == null) {
                    Log.e(LOG_TAG, "onDetectClick(): Classifier is not initialized");
                    return;
                } else if (mFpvPaint.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.please_write_a_digit, Toast.LENGTH_SHORT).show();
                    return;
                }
            Bitmap image = mFpvPaint.exportToBitmap(
                    Classifier.DIM_IMG_SIZE_WIDTH, Classifier.DIM_IMG_SIZE_HEIGHT);
            Bitmap inverted = ImageUtil.invert(image);
            Result result = mClassifier.classify(inverted);
            renderResult(result);
            }
        });


        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mClassifier.close();
    }

    private void init() {
        try {
            mClassifier = new Classifier(getActivity());
        } catch (RuntimeException e) {
            Log.e(LOG_TAG, "Failed to create classifier.", e);
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void renderResult(Result result) {
        mTvPrediction.setText(String.valueOf(result.getNumber()));
        mTvProbability.setText(String.valueOf(result.getProbability()));
        mTvTimeCost.setText(String.format(getString(R.string.timecost_value),
                result.getTimeCost()));
    }

}
