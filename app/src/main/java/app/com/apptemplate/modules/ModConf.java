package app.com.apptemplate.modules;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.apptemplate.AppConf;
import app.com.apptemplate.AppMain;
import app.com.apptemplate.R;
import app.com.apptemplate.interfaces.RedirectInterface;
import app.com.apptemplate.utils.DataBaseHelper;
import app.com.apptemplate.utils.SessionControl;
import app.com.apptemplate.widgets.WidgetProvider;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the *
 * Use the {@link app.com.apptemplate.modules.ModConf#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModConf extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String TAG="";

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
    public static ModConf newInstance(String param1, String param2) {
        ModConf fragment = new ModConf();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ModConf() {
        // Required empty public constructor
    }

    int modPosition=0;

    private SessionControl sessionControl;
    TextView mCounterVal;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionControl= new SessionControl(getActivity(),false);

        if(AppConf.navigation == AppConf.NavigationType.NONE)
            setHasOptionsMenu(true);

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

    @Override
    public void onResume(){
        Log.d(TAG, "modPostion: " + modPosition);
        ((AppMain) getActivity()).onSectionAttached(modPosition);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_mod_conf, container, false);
        Button btnReset= (Button) view.findViewById(R.id.btn_reset_counter);
        Button btnSubtract= (Button) view.findViewById(R.id.btn_subtract_counter);
        mCounterVal= (TextView) view.findViewById(R.id.lbl_counter_val);
        mCounterVal.setText(getString(R.string.txt_counter_val_prefix)+ " "  + getCountFromDB());

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

        btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDB("id in(select id from smiles order by id desc limit 1)");
            }
        });

        return view;
    }

    private int getCountFromDB(){
        int count=0;
        DataBaseHelper dbh= new DataBaseHelper(getActivity());
        try {
            dbh.createDataBase();
            SQLiteDatabase database= dbh.getReadableDatabase();
            Cursor cursorData=database.rawQuery("select count(date) from smiles",null);
            cursorData.moveToFirst();
            count= cursorData.getInt(0);
            cursorData.close();

        }catch(Exception e){
            Log.e("WidgetProvider",e.getMessage());
        }
        return count;
    }

    public void showAlert(){
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.alert_message))
                .setCancelable(false)
                .setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFromDB(null);
                    }
                })
                .setNegativeButton(R.string.alert_no,null)
                .show();
    }


    private void deleteFromDB(String where){
        DataBaseHelper dbh= new DataBaseHelper(getActivity());
        int count=0;
        try {
            dbh.createDataBase();
            SQLiteDatabase database= dbh.getReadableDatabase();
            database.delete("smiles", where, null);
            Log.d("WidgetProvider","row deleted");
            Cursor cursor=database.rawQuery("select count(date) from smiles",null);
            cursor.moveToFirst();
            count= cursor.getInt(0);
            cursor.close();
            mCounterVal.setText(getString(R.string.txt_counter_val_prefix)+ " " + count);
            WidgetProvider.updateViews(getActivity(), count);

        }catch(Exception e){
            Log.e("WidgetProvider",e.getMessage());
        }
        dbh.close();
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

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.global, menu);

        if(AppConf.navigation == AppConf.NavigationType.NONE){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_global_settings) {
            Log.d(TAG, "clickeado Settings");
        }
        if(item.getItemId() == android.R.id.home){
            FragmentManager manager= getActivity().getSupportFragmentManager();
            FragmentTransaction transaction= manager.beginTransaction();
            transaction.remove(this);
            transaction.commit();
            manager.popBackStack();
        }
            return super.onOptionsItemSelected(item);
    }

}
