package com.nex3z.tfmobilemnist;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageAnalysisFragment extends Fragment {

    private int id;
    private ImageView imageView;
    private DatabaseHelper mydb;
    private ImageObject imageObject;
    private TextView textView;
    Cursor cursor;

    public ImageAnalysisFragment() {
    }
    Toolbar toolbar;
    Button NamBarBtnVar;
    private Classifier mClassifier;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mClassifier = new Classifier(getActivity());
        View view  = inflater.inflate(R.layout.fragment_image_analysis, container, false);
        mydb =  new DatabaseHelper(getActivity());
        id = getArguments().getInt("data");

        imageView = (ImageView) view.findViewById(R.id.imageView3);
        textView = (TextView) view.findViewById(R.id.textView2);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        NamBarBtnVar = new Button(getActivity());

        cursor = mydb.getImageById(id);
        cursor.moveToFirst();
        byte[] byteArray = cursor.getBlob(1);
        imageObject = new ImageObject(BitmapFactory.decodeByteArray(byteArray,0 , byteArray.length), cursor.getInt(2), cursor.getDouble(3), cursor.getString(4));

        imageView.setImageBitmap(imageObject.getImage());
        textView.setText(cursor.getString(2));

        changeToolBar();
        deleteObject();
        return view;
    }
    public void onDestroyView(){
        toolbar.removeView(NamBarBtnVar);
        super.onDestroyView();
        toolbar.removeView(NamBarBtnVar);
    }
    @Override
    public void onDestroy(){
        toolbar.removeView(NamBarBtnVar);
        super.onDestroy();
        toolbar.removeView(NamBarBtnVar);
}
    private void changeToolBar(){

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        NamBarBtnVar.setText("Delete");
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity=Gravity.END;
        toolbar.addView(NamBarBtnVar, layoutParams);
    }
    private void deleteObject() {
        NamBarBtnVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDeleted = mydb.deleteByID(cursor.getInt(0));

                if (isDeleted == true) {
                    Toast.makeText(getActivity(), "Data deleted", Toast.LENGTH_LONG).show();
                    goBack();
                } else {
                    Toast.makeText(getActivity(), "Data not deleted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goBack() {
        toolbar.removeView(NamBarBtnVar);
        Fragment fragment = new ListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_change, fragment);
        ft.commit();

    }


}
