package app.com.apptemplate.modules;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.ArrayList;

import app.com.apptemplate.AppConf;
import app.com.apptemplate.AppMain;
import app.com.apptemplate.R;
import app.com.apptemplate.adapters.LugaresAdapter;
import app.com.apptemplate.adapters.PlacesAdapter;
import app.com.apptemplate.adapters.RecyclerAdapter;
import app.com.apptemplate.dummy.DummyData;
import app.com.apptemplate.interfaces.DataSetInterface;
import app.com.apptemplate.interfaces.RedirectInterface;
import app.com.apptemplate.utils.DataProvider;
import app.com.apptemplate.utils.LoadingDialog;
import app.com.apptemplate.utils.RecyclerItemClickListener;
import app.com.apptemplate.utils.SessionControl;
import app.com.apptemplate.utils.StringClass;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the *
 * Use the {@link app.com.apptemplate.modules.ModItemsMaster#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModItemsMaster extends Fragment implements DataSetInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG="ModItemsMaster";

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
    public static ModItemsMaster newInstance(String param1, String param2) {
        ModItemsMaster fragment = new ModItemsMaster();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ModItemsMaster() {
        // Required empty public constructor
    }
    int modPosition=0;

    private SessionControl sessionControl;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PlacesAdapter mAdapter;
    private LugaresAdapter mAdapter2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionControl= new SessionControl(getActivity(),false);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            modPosition= getArguments().getInt("modPosition");
        }

        if(sessionControl.isRequireSession()) {
            if (!sessionControl.checkSessionData()) {
                mRedirectListener.redirectToLogin(modPosition);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView= inflater.inflate(R.layout.fragment_mod_items_master, container, false);
        mRecycleView= (RecyclerView) fragmentView.findViewById(R.id.items_master);

        mLayoutManager= new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        mAdapter= new PlacesAdapter(new ArrayList<StringClass>() ,mRecycleView);
        mAdapter2= new LugaresAdapter(mRecycleView);

        LoadingDialog listLoading= LoadingDialog.newInstance(getActivity().getSupportFragmentManager());
        listLoading.setMessage(getResources().getString(R.string.loading_message));


        //DataProvider dataProvider= new DataProvider(getActivity(),mAdapter2);
        //dataProvider.loadFromUrl(AppConf.protocol+"://"+AppConf.host+"/weservis.php","json",listLoading);
        DataProvider.loadAdapterFromUrl(this,mAdapter2,
                AppConf.protocol+"://"+AppConf.host+"/weservis.php","json",listLoading);

        mRecycleView.setAdapter(mAdapter2);

        mRecycleView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener(){
                    @Override
                    public void onItemClick(View view, int pos){
                        mAdapter2.removeAt(pos);
                    }

                })
        );

        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mRedirectListener != null) {
            //mRedirectListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume(){
        Log.d(TAG,"modPostion: "+modPosition);
        ((AppMain) getActivity()).onSectionAttached(modPosition);
        super.onResume();
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

    @Override
    public void setAdapterDataSet(String data) {
        Gson gson= new Gson();
        NetworkResponse networkResponse= gson.fromJson(data,NetworkResponse.class);
        ArrayList<StringClass> dataSet= networkResponse.responseData;
        mAdapter= new PlacesAdapter(dataSet,mRecycleView);
        Log.d(TAG,"dataSet changed: "+data);
        mRecycleView.setAdapter(mAdapter);
    }

    public class NetworkResponse {
        public String responseStatus;
        public String responseText;
        public ArrayList<StringClass> responseData;
    }
}
