package com.nex3z.tfmobilemnist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAnalysisFragment extends Fragment {

    private int id;
    private TextView textView;
    private ImageView imageView;
    private DatabaseHelper mydb;

    public ImageAnalysisFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_image_analysis, container, false);
        mydb =  new DatabaseHelper(getActivity());
        id = getArguments().getInt("data");
        textView = (TextView) view.findViewById(R.id.textView2);
        imageView = (ImageView) view.findViewById(R.id.imageView3);

        Cursor cursor = mydb.getImageById(id);
        cursor.moveToFirst();

        textView.setText(cursor.getString(2));
        byte[] byteArray = cursor.getBlob(1);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(byteArray,0 , byteArray.length));

        return view;
    }


}
