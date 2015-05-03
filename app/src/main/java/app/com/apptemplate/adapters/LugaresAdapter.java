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
public class LugaresAdapter extends BaseRecyclerAdapter<LugaresAdapter.ViewHolder>{
    final String TAG="LugaresAdapter";
    private List<StringClass> mDataset;
    RecyclerView mRecycleView;
    NetworkResponse networkResponse;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(View v){
            super(v);
            mTextView= (TextView) v.findViewById(R.id.info_text);
        }
    }

    public LugaresAdapter(RecyclerView rv){
        super(rv);
        mRecycleView=rv;
        mDataset= new ArrayList<>();
        networkResponse= new NetworkResponse();
        networkResponse.responseData= new ArrayList<StringClass>();

    }

    @Override
    public void setDataSet( String data) {
        Gson gson= new Gson();
        networkResponse= gson.fromJson(data,NetworkResponse.class);
        mDataset=networkResponse.responseData;
        Log.d(TAG, mDataset.get(0).cadena);
        notifyDataSetChanged();

    }
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        /*TextView v= (TextView)LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view,parent,false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= mRecycleView.getChildPosition(v);
                mRecycleView.removeView(v);
                Log.d(TAG,"touched: "+mDataset[pos]);
            }
        });*/

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //holder.mTextView.setText(mDataset[position]);
        holder.mTextView.setText(mDataset.get(position).cadena);
    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

    public void removeAt(int pos){
        mDataset.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeRemoved(pos, mDataset.size());
    }

    //Respuesta desde webservice donde responseData debe ser igual a dataset del adapter

    public class NetworkResponse {
        public String responseStatus;
        public String responseText;
        public ArrayList<StringClass> responseData;
    }
}
