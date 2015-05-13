package app.com.apptemplate.modules;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.apptemplate.AppMain;
import app.com.apptemplate.R;
import app.com.apptemplate.interfaces.RedirectInterface;
import app.com.apptemplate.utils.SessionControl;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the *
 * Use the {@link ModBlank#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModBlank extends Fragment {
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
    public static ModBlank newInstance(String param1, String param2) {
        ModBlank fragment = new ModBlank();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ModBlank() {
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
    public void onResume(){
        Log.d(TAG, "modPostion: " + modPosition);
        ((AppMain) getActivity()).onSectionAttached(modPosition);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
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

}
