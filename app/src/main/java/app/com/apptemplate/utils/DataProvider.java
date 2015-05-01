package app.com.apptemplate.utils;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.com.apptemplate.adapters.RecyclerAdapter;
import app.com.apptemplate.interfaces.DataSetInterface;

/**
 * Created by steve on 28/04/2015.
 */
public class DataProvider {
    final static String TAG="DataProvider";

    //LugaresAdapter mAdapter=null;
    View mView=null;
    Context mContext;

    /*public DataProvider(Context context,LugaresAdapter adapter){
        mAdapter=adapter;
        mContext= context;
    }*/

    public DataProvider(View view){
        mView=view;
    }

    public static void loadJson(Context context, String url, Response.Listener respListener){
        StringRequest jsonReq= new StringRequest(Request.Method.GET,url,
        respListener,
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "err: " + error.getMessage());
            }
        });

        NetworkProvider.getInstance(context).addToRequestQueue(jsonReq);
    }

    public static void loadImage(Context context,String url, final ImageView imageView, final ProgressBar progressBar){
        ImageRequest imgReq= new ImageRequest(url,new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
                Log.d(TAG,"image loaded");
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        },0,0,null,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG,error.getMessage());
                }
            });
        imgReq.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkProvider.getInstance(context).addToRequestQueue(imgReq);

    }

    public static void loadFromUrl(Fragment fragment, String dataUrl,String type,final LoadingDialog loadingFragment){
        Request request= null;
        final DataSetInterface dataSetInterface= (DataSetInterface)fragment;
        if(loadingFragment != null) {
            loadingFragment.showDialog();
        }
        if(type.equals("json")){
            Log.d(TAG,"loading json: "+dataUrl);
            StringRequest jsonReq= new StringRequest(Request.Method.GET,dataUrl,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG,"json loaded");
                            //if(mAdapter!=null)
                            //    mAdapter.setDataSet(response);
                            dataSetInterface.setAdapterDataSet(response);
                            if(loadingFragment != null) {
                                loadingFragment.closeDialog();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "err: " + error.getMessage());
                        }
                    });
            request= jsonReq;
        }
        if(request != null) {
            request.setRetryPolicy(new DefaultRetryPolicy(6000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            NetworkProvider.getInstance(fragment.getActivity()).addToRequestQueue(request);
        }
    }


}
