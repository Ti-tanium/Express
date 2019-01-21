package com.example.asus.manager.Main;

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

import com.example.asus.manager.HttpAgent;
import com.example.asus.manager.R;
import com.example.asus.manager.Utils;
import com.example.asus.manager.config;

import org.json.JSONException;
import org.json.JSONObject;

public class MainFragment extends Fragment {
    private EditText mPostmanId;
    private EditText mPacakgeId;
    private Button mAddPermission;
    private Button mDeletePermission;
    public static String TAG="MainFragment";
    private static String ADD="ADD";
    private static String DELETE="DELETE";
    private String operation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_main,container,false);
        mPostmanId=view.findViewById(R.id.main_postman_id);
        mPacakgeId=view.findViewById(R.id.main_package_id);
        mAddPermission=view.findViewById(R.id.main_add_permission);
        mDeletePermission=view.findViewById(R.id.main_delete_permission_btn);
        mAddPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation=ADD;
                new AddPermissionTask().execute();
            }
        });
        mDeletePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation=DELETE;
                new AddPermissionTask().execute();
            }
        });
        return view;
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    private class AddPermissionTask extends AsyncTask<Void,Void,String>{
        @Override
        public String doInBackground(Void ...Void){

            String url= config.addPermission+"?postman="+mPostmanId.getText().toString()+"&package="+mPacakgeId.getText().toString()+"&operation="+operation;
            JSONObject jsonObject=HttpAgent.fetchJSON(url);
            String code=null;
            if(jsonObject==null){
                return "-1";
            }else {
                try {
                    code=jsonObject.getString("code");
                    return code;
                }catch (JSONException je){
                    Log.e(TAG,je.toString());
                }
            }
            return "-1";
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("1")){
                Toast.makeText(getActivity(),operation.equals(ADD)?"Successfully Added!":"Successfully Deleted.",Toast.LENGTH_SHORT).show();

            }else if(s.equals("-1")){
                Utils.showAlertDialog(getString(R.string.alert),getString(R.string.alert_no_postman),getActivity()).show();
            }else if(s.equals("-2")){
                Utils.showAlertDialog(getString(R.string.alert),getString(R.string.alert_no_package),getActivity()).show();
            }
            else {
                Utils.showAlertDialog(getString(R.string.alert),getString(R.string.alert_failed_add_permission),getActivity()).show();
            }

        }
    }

}
