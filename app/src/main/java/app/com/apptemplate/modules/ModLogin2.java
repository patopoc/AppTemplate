package app.com.apptemplate.modules;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.com.apptemplate.R;
import app.com.apptemplate.interfaces.RedirectInterface;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ModLogin2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModLogin2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
     * @return A new instance of fragment modLogin.
     */
    // TODO: Rename and change types and number of parameters
    public static ModLogin2 newInstance(String param1, String param2) {
        ModLogin2 fragment = new ModLogin2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ModLogin2() {
        // Required empty public constructor
    }

    EditText txtUser;
    EditText txtPassword;
    TextView lblMsg;
    Button btnLogin;

    int modPosition=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            modPosition= getArguments().getInt("modPosition");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView= inflater.inflate(R.layout.fragment_mod_login, container, false);

        txtUser= (EditText) fragmentView.findViewById(R.id.txtUser);
        txtPassword= (EditText) fragmentView.findViewById(R.id.txtPassword);
        btnLogin= (Button) fragmentView.findViewById(R.id.btnLogin);
        lblMsg= (TextView) fragmentView.findViewById(R.id.lblLoginMsg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUser.getText().equals("") || txtPassword.getText().equals("")){
                    lblMsg.setText("Ingresar User y Pass");
                    txtUser.requestFocus();
                }
                if(!checkUser(txtUser.getText().toString(),txtPassword.getText().toString())){
                    lblMsg.setText("User o pass incorrectos");
                    txtUser.setText("");
                    txtPassword.setText("");
                    txtUser.requestFocus();
                }
                else{
                    mRedirectListener.redirectMod(modPosition);
                }
            }
        });

        return fragmentView;

    }

    public boolean checkUser(String user, String pass){
        SharedPreferences pref= getActivity().getPreferences(Context.MODE_PRIVATE);
        String userName= pref.getString("USER",user);
        String password= pref.getString("PASSWORD",pass);
        SharedPreferences.Editor prefEditor= pref.edit();

        if(userName.equals(user) && password.equals(pass)){
            prefEditor.putString("USER",user);
            prefEditor.putString("PASSWORD",pass);
            prefEditor.commit();
            return true;
        }
        return false;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mRedirectListener != null) {

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

}
