package app.com.apptemplate.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.com.apptemplate.R;
import app.com.apptemplate.utils.StringClass;

/**
 * Created by steve on 27/04/2015.
 */
public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>{

    public static class VH extends RecyclerView.ViewHolder{

        public VH (View v){
            super(v);
        }
    }

    public BaseRecyclerAdapter(RecyclerView rv){

    }

    public void setDataSet( String data) {

    }
    // Create new views (invoked by the layout manager)


    // Replace the contents of a view (invoked by the layout manager)

    public void removeAt(int pos){

    }
}
