package app.com.apptemplate.utils;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.BaseAdapter;

import org.json.JSONObject;

/**
 * Created by steve on 28/04/2015.
 */
public class DataProvider {

    RecyclerView.Adapter mAdapter=null;
    View mView=null;

    public DataProvider(RecyclerView.Adapter adapter){
        mAdapter=adapter;
    }

    public DataProvider(View view){
        mView=view;
    }

    public void loadFromUrl(String dataUrl){

    }

    private class DataProviderTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {

            return null;
        }
    }
}
