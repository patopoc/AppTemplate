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
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Cache;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.sql.Timestamp;
import java.util.ArrayList;

import app.com.apptemplate.AppMain;
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
    final String TAG="ModReport";

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
    int modPosition=0;

    private SessionControl sessionControl;
    private Cursor cursorData;
    private ArrayList<String> unsortedDatesString;
    private ArrayList<Long> unsortedDatesLong;
    private BarChart barChart;
    private ArrayList<String> xLabels;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionControl= new SessionControl(getActivity(),false);
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
            unsortedDatesLong= new ArrayList<>();
            SQLiteDatabase database= dbh.getReadableDatabase();
            cursorData=database.rawQuery("select date from smiles",null);
            while(cursorData.moveToNext()){
                unsortedDatesLong.add(cursorData.getLong(0));
                //unsortedDatesString.add(TimeHelper.getDate(cursorData.getLong(0),"dd/MM/yyyy"));
            }
            cursorData.close();

        }catch(Exception e){
            Log.e("WidgetProvider",e.getMessage());
        }

    }

    @Override
    public void onResume(){
        Log.d(TAG,"modPostion: "+modPosition);
        ((AppMain) getActivity()).onSectionAttached(modPosition);
        super.onResume();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_mod_report, container, false);

        barChart= (BarChart) view.findViewById(R.id.chart);
        Button btnDaily= (Button) view.findViewById(R.id.btn_daily);
        Button btnMonthly= (Button) view.findViewById(R.id.btn_monthly);
        final LinearLayout chartContainer= (LinearLayout) view.findViewById(R.id.char_container);
        final Button btnShowChart= (Button) view.findViewById(R.id.btn_show_chart);

        btnShowChart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(chartContainer.getVisibility() == View.GONE){
                    chartContainer.setVisibility(View.VISIBLE);
                    btnShowChart.setText(R.string.btn_hide_chart);
                }
                else if(chartContainer.getVisibility() == View.VISIBLE){
                    chartContainer.setVisibility(View.GONE);
                    btnShowChart.setText(R.string.btn_show_chart);
                }
            }
        });

        btnDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChartData("dd/MM/yyyy");
            }
        });

        btnMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChartData("MM/yyyy");
            }
        });

        setChartData("dd/MM/yyyy");
        return view;
    }

    private void setChartData(String orderFormat){
        unsortedDatesString=null;
        unsortedDatesString= new ArrayList<>();
        for(long dateStamp : unsortedDatesLong){
            unsortedDatesString.add(TimeHelper.getDate(dateStamp,orderFormat));
        }

        xLabels= new ArrayList<>();
        ArrayList<Integer> data= getGroupCount();

        ArrayList<BarEntry> dataEntries= new ArrayList<BarEntry>();


        for (int i=0; i < data.size(); i++){
            BarEntry entry= new BarEntry(data.get(i),i);
            dataEntries.add(entry);
            //xLabels.add(i+"");
        }

        BarDataSet barDataSet= new BarDataSet(dataEntries,"Los Datos");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<BarDataSet> barDataSets= new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);
        BarData barData= new BarData(xLabels,barDataSets);
        barChart.setData(barData);
        barChart.animateY(500, Easing.EasingOption.EaseInQuad);
        barChart.invalidate();
    }

    private ArrayList<String> getLabels(String orderFormat, int numLabels){
        ArrayList<String> labels= new ArrayList<>();

        return labels;
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

    private ArrayList<Integer> getGroupCount(){
        ArrayList<String> searchedDates= new ArrayList<String>();
        ArrayList<Integer> groupedCount= new ArrayList<>();
        long lastDate=0;

        for(int i=0; i< unsortedDatesString.size(); i++){
            //Log.d(TAG,unsortedDatesString.get(i));
            boolean skipDate=false;
            //check if date was already searched
            for(String sd : searchedDates){
                if(unsortedDatesString.get(i).equals(sd)){
                    skipDate=true;
                }
            }
            if(skipDate)
                continue;

            int dateDiffInt=0;
            /*if(unsortedDatesString.get(i).matches("\\d\\d/\\d\\d/\\d\\d\\d\\d") && lastDate != 0){
                //Log.d(TAG,"lastDate: "+lastDate+" || "+unsortedDatesString.get(i));
                long dateDiff=unsortedDatesLong.get(i) - lastDate;
                if(dateDiff < 0)
                    dateDiff *= -1;

                dateDiffInt= (int) (dateDiff / 86400000);
                //Log.d(TAG,"dateDiff "+dateDiffDays);
            }

            for(int j=0; j < dateDiffInt; j++){
                groupedCount.add(0);
            }*/

            int currCount=0;
            for(String date : unsortedDatesString){
                if(date.equals(unsortedDatesString.get(i)))
                    currCount ++;
            }
            groupedCount.add(currCount);
            searchedDates.add(unsortedDatesString.get(i));
            lastDate=unsortedDatesLong.get(i);
        }
        xLabels=searchedDates;
        return groupedCount;
    }
}
