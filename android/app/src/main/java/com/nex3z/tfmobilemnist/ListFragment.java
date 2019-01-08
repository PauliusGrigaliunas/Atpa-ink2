package com.nex3z.tfmobilemnist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class ListFragment extends Fragment {
    private DatabaseHelper mydb;
    private ListView listView;
    private ImageView imageView;
    private FloatingActionButton fab;

    private ArrayList arrayList;
    private HashMap<String, String> hmap;

    private TextView textView;

    int flags = R.drawable.ic_menu_camera;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        mydb = new DatabaseHelper(getActivity());
        showList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showList();
    }

    private void showList() {

        arrayList = new ArrayList<HashMap<String, String>>();
        try {

            Cursor c = mydb.showAll();


            while (c.moveToNext()) {
                hmap = new HashMap<String, String>();
                hmap.put("id", c.getString(0));
                hmap.put("prediction", " prediction: " + c.getString(2));
                hmap.put("probability", " prob: " + c.getString(3));
                hmap.put("image", Integer.toString(flags));
               /* byte[] byteArray = c.getBlob(3);
                Drawable did = new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length).toString());
                hmap.put("points", did.toString());*/


                arrayList.add(hmap);

                //imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());

        }

        String from[] = {"id", "prediction", "image", "probability"};
        int to[] = {R.id.idText, R.id.taskText, R.id.imageView2, R.id.probability};

        SimpleAdapter adapter = new SimpleAdapter(
                getContext(),
                arrayList,
                R.layout.list_row,
                from, to);

        listView.setAdapter(adapter);
        selectItemFromList();
    }


    private void selectItemFromList() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap element = (HashMap) arrayList.get(position);

                int name = Integer.parseInt(element.get("id").toString());

                Bundle bundle = new Bundle();
                bundle.putInt("data", name);

                Fragment fragment = new ImageAnalysisFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_change, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}