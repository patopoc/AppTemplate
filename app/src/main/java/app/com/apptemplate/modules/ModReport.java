package app.com.apptemplate.modules;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import app.com.apptemplate.R;
import app.com.apptemplate.interfaces.RedirectInterface;
import app.com.apptemplate.utils.DataBaseHelper;
import app.com.apptemplate.utils.SessionControl;
import app.com.apptemplate.utils.TimeHelper;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the *
 * Use the {@link app.com.apptemplate.modules.ModReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModReport extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RedirectInterface mRedirectListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModReport newInstance(String param1, String param2) {
        ModReport fragment = new ModReport();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ModReport() {
        // Required empty public constructor
    }

    private SessionControl sessionControl;
    private Cursor cursorData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionControl= new SessionControl(getActivity(),false);
        int modPosition=0;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            modPosition= getArguments().getInt("modPosition");
        }

        if(sessionControl.isRequireSession()){
            if(!sessionControl.checkSessionData()){
                mRedirectListener.redirectToLogin(modPosition);
            }
        }

        DataBaseHelper dbh= new DataBaseHelper(getActivity());
        try {
            dbh.createDataBase();
            SQLiteDatabase database= dbh.getReadableDatabase();
            cursorData=database.rawQuery("select date from smiles",null);

        }catch(Exception e){
            Log.e("WidgetProvider",e.getMessage());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_mod_report, container, false);

        BarChart barChart= (BarChart) view.findViewById(R.id.chart);
        ArrayList<Integer> data= getCountBy(cursorData,"dd/MM/yyyy");
        ArrayList<BarEntry> dataEntries= new ArrayList<BarEntry>();
        ArrayList<String> xLabels= new ArrayList<>();

        for (int i=0; i < data.size(); i++){
            BarEntry entry= new BarEntry(data.get(i),i);
            dataEntries.add(entry);
            xLabels.add(i+"");
        }

        BarDataSet barDataSet= new BarDataSet(dataEntries,"Los Datos");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<BarDataSet> barDataSets= new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);
        BarData barData= new BarData(xLabels,barDataSets);
        barChart.setData(barData);
        barChart.animateY(500, Easing.EasingOption.EaseInQuad);
        barChart.invalidate();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mRedirectListener != null) {
            //mRedirectListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mRedirectListener = (RedirectInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRedirectListener = null;
    }

    private ArrayList<Integer> getCountBy(Cursor cursor,String orderByFormat){
        ArrayList<String> searchedDates= new ArrayList<String>();
        ArrayList<String> unsortedDates= new ArrayList<>();
        ArrayList<Integer> groupedCount= new ArrayList<>();

        while(cursor.moveToNext()){
            unsortedDates.add(TimeHelper.getDate(cursor.getLong(0),orderByFormat));
        }
        cursor.close();

        for(String currDate : unsortedDates){
            boolean skipDate=false;
            //check if date was already searched
            for(String sd : searchedDates){
                if(currDate.equals(sd)){
                    skipDate=true;
                }
            }
            if(skipDate)
                continue;
            int currCount=0;
            for(String date : unsortedDates){
                if(date.equals(currDate))
                    currCount ++;
            }
            groupedCount.add(currCount);
            searchedDates.add(currDate);
        }

        return groupedCount;
    }

}
