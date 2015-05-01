package app.com.apptemplate.modules;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;

import app.com.apptemplate.AppConf;
import app.com.apptemplate.R;
import app.com.apptemplate.adapters.RecyclerAdapter;
import app.com.apptemplate.interfaces.RedirectInterface;
import app.com.apptemplate.utils.DataProvider;
import app.com.apptemplate.utils.LoadingDialog;
import app.com.apptemplate.utils.RecyclerItemClickListener;
import app.com.apptemplate.utils.SessionControl;
import app.com.apptemplate.utils.StringClass;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the *
 * Use the {@link app.com.apptemplate.modules.ModImages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModImages extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG="ModImages";

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
    public static ModImages newInstance(String param1, String param2) {
        ModImages fragment = new ModImages();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ModImages() {
        // Required empty public constructor
    }

    private SessionControl sessionControl;

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
        View fragmentView= inflater.inflate(R.layout.fragment_mod_images, container, false);
        final ProgressBar pb= (ProgressBar) fragmentView.findViewById(R.id.pb_img_1);
        final ImageView imageView= (ImageView) fragmentView.findViewById(R.id.img_1);
        final ArrayList<StringClass> imgUrls;
        Log.d(TAG, "empezo ModImaged");
        //LoadingDialog listLoading= LoadingDialog.newInstance(getActivity().getSupportFragmentManager());
        //listLoading.setMessage(getResources().getString(R.string.loading_message));

        Response.Listener<String> respListener= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson= new Gson();
                NetworkResponse resp= gson.fromJson(response,NetworkResponse.class);
                if(resp.responseStatus.equals("ok")){
                    String url= resp.responseData.get(0).cadena;
                    DataProvider.loadImage(getActivity(),url,imageView,pb);
                }
            }
        };
        DataProvider.loadJson(getActivity(),AppConf.protocol+"://"+AppConf.host+"/weservis.php",respListener);

        return fragmentView;
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

    public class NetworkResponse {
        public String responseStatus;
        public String responseText;
        public ArrayList<StringClass> responseData;
    }
}
