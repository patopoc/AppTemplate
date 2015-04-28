package app.com.apptemplate.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.apptemplate.R;

/**
 * Created by steve on 27/04/2015.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder>{
    final String TAG="PlacesAdapter";
    private ArrayList<String> mDataset;
    RecyclerView mRecycleView;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(View v){
            super(v);
            mTextView= (TextView) v.findViewById(R.id.info_text);
        }
    }

    public PlacesAdapter(String[] mydataset, RecyclerView rv){
        mDataset= new ArrayList<String>();
        for(int i=0;i<mydataset.length;i++)
            mDataset.add(i,mydataset[i]);

        mRecycleView=rv;
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
        holder.mTextView.setText(mDataset.get(position));
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
}
