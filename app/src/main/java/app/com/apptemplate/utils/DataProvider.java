package app.com.apptemplate.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.com.apptemplate.adapters.RecyclerAdapter;

/**
 * Created by steve on 28/04/2015.
 */
public class DataProvider {
    final String TAG="DataProvider";

    RecyclerAdapter mAdapter=null;
    View mView=null;
    ResponseClass reqResponse;
    Context mContext;

    public DataProvider(Context context,RecyclerAdapter adapter, ArrayList<?> arrayData){
        mAdapter=adapter;

        reqResponse= new ResponseClass(arrayData);
        Log.d(TAG,"reqResponse.reqData type: "+reqResponse.responseData.getClass().getName());
        mContext= context;
    }

    public DataProvider(RecyclerAdapter adapter, Object data){
        mAdapter=adapter;
    }

    public DataProvider(View view){
        mView=view;
    }

    public void loadFromUrl(String dataUrl,String type){
        Request request= null;
        if(type.equals("json")){
            Log.d(TAG,"loading json");
            StringRequest jsonReq= new StringRequest(Request.Method.GET,dataUrl,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            Gson gson= new Gson();
                            reqResponse= gson.fromJson(response, ResponseClass.class);
                            mAdapter.setDataArray(reqResponse.responseData);
                            mAdapter.notifyDataSetChanged();

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
            NetworkProvider.getInstance(mContext).addToRequestQueue(request);
        }
    }


}
