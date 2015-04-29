package app.com.apptemplate.utils;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by steve on 29/04/2015.
 */
public class NetworkProvider {
    private static NetworkProvider mInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private NetworkProvider(Context ctx){
        mContext= ctx;
    }

    public synchronized static NetworkProvider getInstance(Context context){
        if(mInstance == null){
            mInstance= new NetworkProvider(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            //mRequestQueue= new RequestQueue(cache, network);
            mRequestQueue= Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
