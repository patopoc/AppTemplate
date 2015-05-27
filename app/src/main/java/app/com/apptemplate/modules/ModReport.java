package app.com.apptemplate.modules;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import app.com.apptemplate.AppConf;
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
    //private ArrayList<String> unsortedDatesString;
    private ArrayList<Long> unsortedDatesLong;
    private BarChart barChart;
    private ArrayList<String> xLabels;

    ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionControl= new SessionControl(getActivity(),false);
        if(AppConf.navigation == AppConf.NavigationType.NONE) {
            setHasOptionsMenu(true);
            getActionBar().setDisplayHomeAsUpEnabled(false);
        }



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
    }

    private void getDataFromDB(){
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
        Log.d(TAG, "modPostion: " + modPosition);
        ((AppMain) getActivity()).onSectionAttached(modPosition);
        super.onResume();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_mod_report, container, false);
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        ConnectivityManager connectivityManager= (ConnectivityManager) getActivity()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null){
            mAdView.setVisibility(View.GONE);
        }
        else{
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
        getDataFromDB();

        barChart= (BarChart) view.findViewById(R.id.chart);
        Button btnDaily= (Button) view.findViewById(R.id.btn_daily);
        Button btnMonthly= (Button) view.findViewById(R.id.btn_monthly);
        final LinearLayout chartContainer= (LinearLayout) view.findViewById(R.id.char_container);
        final Button btnShowChart= (Button) view.findViewById(R.id.btn_show_chart);
        final ScrollView containerScroll= (ScrollView) view.findViewById(R.id.container_scrollable);

        btnShowChart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(chartContainer.getVisibility() == View.GONE){
                    chartContainer.setVisibility(View.VISIBLE);
                    btnShowChart.setText(R.string.btn_hide_chart);
                    containerScroll.post(new Runnable() {
                        @Override
                        public void run() {
                            containerScroll.scrollTo(0,chartContainer.getBottom());
                        }
                    });

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

        setSmileStatus(view, getCountDate(System.currentTimeMillis(),"dd/MM/yyyy"));
        TextView txtStdVal= (TextView) view.findViewById(R.id.txt_std_val);
        TextView txtStwVal= (TextView) view.findViewById(R.id.txt_stw_val);
        TextView txtStmVal= (TextView) view.findViewById(R.id.txt_stm_val);
        TextView txtAspdVal= (TextView) view.findViewById(R.id.txt_aspd_val);
        TextView txtAspwVal= (TextView) view.findViewById(R.id.txt_aspw_val);
        TextView txtAspmVal= (TextView) view.findViewById(R.id.txt_aspm_val);

        txtStdVal.setText(""+getCountDate(System.currentTimeMillis(),"dd/MM/yyyy"));
        txtStwVal.setText(""+getCountDate(System.currentTimeMillis(),"w/yyyy"));
        txtStmVal.setText(""+getCountDate(System.currentTimeMillis(),"MM/yyyy"));
        txtAspdVal.setText(""+getCountAvg("dd/MM/yyyy"));
        txtAspwVal.setText(""+getCountAvg("w/yyyy"));
        txtAspmVal.setText(""+getCountAvg("MM/yyyy"));

        return view;
    }

    private void setSmileStatus(View view, int smilesCount){
        ImageView imgStatus= (ImageView) view.findViewById(R.id.img_status);
        TextView recommendation= (TextView) view.findViewById(R.id.lbl_recommendation);
        Random randomRecom= new Random();
        if(smilesCount < 2){
            imgStatus.setImageResource(R.drawable.status0);
            String[] recom= getResources().getStringArray(R.array.status0);
            recommendation.setText(recom[randomRecom.nextInt(recom.length)]);
        }
        else if(smilesCount < 4){
            imgStatus.setImageResource(R.drawable.status1);
            String[] recom= getResources().getStringArray(R.array.status1);
            recommendation.setText(recom[randomRecom.nextInt(recom.length)]);
        }
        else if(smilesCount < 6){
            imgStatus.setImageResource(R.drawable.status2);
            String[] recom= getResources().getStringArray(R.array.status2);
            recommendation.setText(recom[randomRecom.nextInt(recom.length)]);
        }
        else if(smilesCount < 8){
            imgStatus.setImageResource(R.drawable.status3);
            String[] recom= getResources().getStringArray(R.array.status3);
            recommendation.setText(recom[randomRecom.nextInt(recom.length)]);
        }
        else if(smilesCount > 10){
            imgStatus.setImageResource(R.drawable.status4);
            String[] recom= getResources().getStringArray(R.array.status4);
            recommendation.setText(recom[randomRecom.nextInt(recom.length)]);
        }
    }

    private void setChartData(String orderFormat){
        //unsortedDatesString=null;
        ArrayList<String> unsortedDatesString= new ArrayList<>();
        for(long dateStamp : unsortedDatesLong){
            unsortedDatesString.add(TimeHelper.getDate(dateStamp,orderFormat));
        }

        xLabels= new ArrayList<>();
        ArrayList<Integer> data= getGroupCount(unsortedDatesString);

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

    private ArrayList<Integer> getGroupCount(ArrayList<String> unsortedDatesString){
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

    private int getCountDate(long timestamp, String format){
        int count=0;
        String currDate= TimeHelper.getDate(timestamp, format);
        for(long date : unsortedDatesLong){
            if(TimeHelper.getDate(date,format).equals(currDate)){
                count++;
            }
        }
        return count;
    }

    private int getCountAvg(String groupFormat){
        int avg=0;
        int totals=0;
        ArrayList<String> searchedDates= new ArrayList<String>();
        ArrayList<Integer> groupedCount= new ArrayList<>();
        ArrayList<String> unsortedDatesString= new ArrayList<>();

        for(long dateStamp : unsortedDatesLong){
            unsortedDatesString.add(TimeHelper.getDate(dateStamp,groupFormat));
        }

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

            int currCount=0;
            for(String date : unsortedDatesString){
                if(date.equals(unsortedDatesString.get(i)))
                    currCount ++;
            }
            groupedCount.add(currCount);
            searchedDates.add(unsortedDatesString.get(i));
        }
        for(Integer subTotal : groupedCount){
            totals+= subTotal;
        }
        if(groupedCount.size() > 0)
            avg= totals / groupedCount.size();

        return avg;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.app_main, menu);
        if(AppConf.navigation == AppConf.NavigationType.NONE)
            getActionBar().setDisplayHomeAsUpEnabled(false);

        setShareAction(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            Log.d(TAG, "clickeado Settings");
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    private void setShareAction(Menu menu) {
        MenuItem menuItem= menu.findItem(R.id.action_share);
        mShareActionProvider= (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        Log.d(TAG,"mShared Created");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        Log.d(TAG,"actionProvider: "+mShareActionProvider);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(sendIntent);
        }
    }
}
