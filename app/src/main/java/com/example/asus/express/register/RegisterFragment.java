package com.example.asus.express.register;

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
import android.widget.Toast;

import com.example.asus.express.HttpAgent;
import com.example.asus.express.R;
import com.example.asus.express.Utils;
import com.example.asus.express.config;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends Fragment {
    private EditText mName;
    private EditText mPassword;
    private Button mRegisterBtn;
    private static String TAG="RegisterFragment";

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register,container,false);
        mName=view.findViewById(R.id.register_name);
        mPassword=view.findViewById(R.id.register_password);
        mRegisterBtn=view.findViewById(R.id.register_btn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterTask().execute();
            }
        });
        return view;
    }

    private class RegisterTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            String name=mName.getText().toString();
            String password=mPassword.getText().toString();
            String url= config.register+"?name="+name+"&password="+password;
            JSONObject jsonObject=HttpAgent.fetchJSON(url);
            if(jsonObject==null){
                Utils.showAlertDialog(getString(R.string.alert),getString(R.string.unreachable_server),getActivity());
                return "-1";
            }else{
                try{
                    String code=jsonObject.getString("code");
                    return code;
                }catch (JSONException je){
                    Log.e(TAG,je.toString());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String code) {
            if(code.equals("1")){
                Toast.makeText(getActivity(),getString(R.string.register_success_toast),Toast.LENGTH_SHORT).show();
            } else if(code.equals("2")){
                Utils.showAlertDialog(getString(R.string.alert),getString(R.string.alert_user_exists),getActivity()).show();
            }
            else{
                Utils.showAlertDialog(getString(R.string.alert),getString(R.string.unreachable_server),getActivity()).show();
            }

        }
    }
}
