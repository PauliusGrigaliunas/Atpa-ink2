package com.nex3z.tfmobilemnist;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.fingerpaintview.FingerPaintView;

import butterknife.ButterKnife;

public class DrawFragment extends Fragment {
    private static final String LOG_TAG = DrawFragment.class.getSimpleName();
    private FingerPaintView mFpvPaint;
    private TextView mTvPrediction;
    private TextView mTvProbability;
    private TextView mTvTimeCost;
    private Button clearButton, detectButton;

    ImageView imageView;
    private DatabaseHelper mydb;
    private Button NamBarBtnVar;
    private Toolbar toolbar;
    private Bitmap inverted;

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
        mydb = new DatabaseHelper(getActivity());
        clearButton = (Button) view.findViewById(R.id.btn_clear2);
        detectButton = (Button) view.findViewById(R.id.btn_detect);
        mFpvPaint = view.findViewById(R.id.fpv_paint2);
        mTvPrediction = view.findViewById(R.id.tv_prediction);
        mTvProbability = view.findViewById(R.id.tv_probability);
        mTvTimeCost = view.findViewById(R.id.tv_timecost);
        NamBarBtnVar = new Button(getActivity());


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
            inverted = ImageUtil.invert(image);
            Result result = mClassifier.classify(inverted);
            renderResult(result);
            }
        });

        changeToolBar();

        saveObject();
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.removeView(NamBarBtnVar);
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



    private void changeToolBar(){

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        toolbar.setSubtitle("Task info");
        NamBarBtnVar.setText("save");
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity=Gravity.END;
        toolbar.addView(NamBarBtnVar, layoutParams);
    }


    private void saveObject() {
        NamBarBtnVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClassifier == null) {
                    Log.e(LOG_TAG, "onDetectClick(): Classifier is not initialized");

                    return;
                } else if (mFpvPaint.isEmpty()) {
                    //Toast.makeText(getActivity(), R.string.please_write_a_digit, Toast.LENGTH_SHORT).show();
                    Snackbar.make(v, "Picture was't insered", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                else{
                    ImageObject image = new ImageObject(inverted,
                            Integer.parseInt(mTvPrediction.getText().toString()),
                            Double.parseDouble(mTvProbability.getText().toString()),
                            mTvTimeCost.getText().toString());

                    if (mydb.insertTaskData(image)) {
                        Snackbar.make(v, "Picture was saved", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else
                        Snackbar.make(v, "Picture was't saved", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                }

            }
        });
    }

}
