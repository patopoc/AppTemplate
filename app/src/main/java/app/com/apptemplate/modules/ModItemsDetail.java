package app.com.apptemplate.modules;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import app.com.apptemplate.AppMain;
import app.com.apptemplate.R;
import app.com.apptemplate.interfaces.RedirectInterface;
import app.com.apptemplate.utils.SessionControl;
import app.com.apptemplate.widgets.WidgetProvider;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the *
 * Use the {@link app.com.apptemplate.modules.ModItemsDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModItemsDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String TAG="ModItemsDetail";

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
    public static ModItemsDetail newInstance(String param1, String param2) {
        ModItemsDetail fragment = new ModItemsDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ModItemsDetail() {
        // Required empty public constructor
    }

    int modPosition=0;

    private SessionControl sessionControl;
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


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mod_items_detail, container, false);
        Button button= (Button) view.findViewById(R.id.theButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction(WidgetProvider.ACTION_ANY);
                getActivity().sendBroadcast(intent);
                Log.d(TAG,"boradcast sent");
            }
        });
        return view;
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

}
