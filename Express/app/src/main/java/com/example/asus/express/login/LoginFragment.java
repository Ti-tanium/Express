package com.example.asus.express.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.express.HttpAgent;
import com.example.asus.express.R;
import com.example.asus.express.Utils;
import com.example.asus.express.config;
import com.example.asus.express.register.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {
    private static String TAG="LoginFragment";
    private EditText mAccount;
    private EditText mPassword;
    private Button mLoginBtn;
    private TextView mRegisterLink;
    public static String KEY_LOGIN_ACCOUNT="KEY_LOGIN_ACCOUNT";

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // init
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        mAccount=(EditText)view.findViewById(R.id.profile_account);
        mPassword=(EditText)view.findViewById(R.id.password);
        mLoginBtn=(Button)view.findViewById(R.id.Login_btn);
        mRegisterLink=(TextView)view.findViewById(R.id.login_register_link);
        getActivity().setTitle(getString(R.string.login));
        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new checkAuthrizationTask().execute();
            }
        });
        mRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.newIntent(getActivity()));
            }
        });
        return view;
    }

    private class checkAuthrizationTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            String url= config.request_authrization+"?account="+mAccount.getText().toString();
            JSONObject jsonObject=new HttpAgent().fetchJSON(url);
            if(jsonObject==null){
                Utils.showAlertDialog(getString(R.string.alert),getString(R.string.unreachable_server),getActivity()).show();
                return getString(R.string.network_error);
            }
            String toastText=null;
            try{
                String code=jsonObject.getString("code");
                if(code.equals("1")){
                    String account=mAccount.getText().toString();
                    String password=mPassword.getText().toString();
                    String checkPassUrl=config.check_password+"?account="+account+"&password="+password;
                    JSONObject checkPass=new HttpAgent().fetchJSON(checkPassUrl);
                    String authentificated=checkPass.getString("code");
                    if(authentificated.equals("1")){
                        toastText="Welcome!";
                        Utils.getPreference(getActivity()).edit().putString(KEY_LOGIN_ACCOUNT,account).apply();
                        getActivity().finish();
                    }else{
                        toastText="Wrong Password.";
                    }
                }else {
                    toastText="Register First";
                }
            }catch (JSONException je){
                Log.e(TAG,"Failed to get code from JSON："+je);
            }
            return toastText;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
        }
    }

}
