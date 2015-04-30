package app.com.apptemplate.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


/**
 * Created by steve on 30/04/2015.
 */
public class LoadingDialog extends DialogFragment {
    private String msg="";
    private FragmentManager fragmentManager;

    public static LoadingDialog newInstance(FragmentManager fragmentManager){
        LoadingDialog ld= new LoadingDialog();
        ld.setFragmentManager(fragmentManager);
        return ld;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState){
        ProgressDialog loadingDialog= new ProgressDialog(getActivity());
        loadingDialog.setMessage(msg);
        return loadingDialog;
    }

    public void setMessage(String msg){
        this.msg=msg;
    }

    public void showDialog(){
        show(fragmentManager, "loading");
    }

    public void closeDialog(){
        dismiss();
    }

    private void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager=fragmentManager;
    }
}
