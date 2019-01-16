package com.example.asus.express.home;

import android.Manifest;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.asus.express.AESUtil;
import com.example.asus.express.HttpAgent;
import com.example.asus.express.R;
import com.example.asus.express.Utils;
import com.example.asus.express.config;
import com.example.asus.express.login.LoginFragment;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.CryptoPrimitive;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private Button mScanButton;
    private TextView mDestination;
    private TextView mSource;
    private TextView mMailer;
    private TextView mReceiver;
    private TextView mMailerPhone;
    private TextView mReceiverPhone;
    private String mUserId;
    private String mPackageId;
    private String mEncryptedPackageInfo;
    public static int REQUEST_CODE_SCAN=1;
    public static String TAG="HomeFragment";

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        mScanButton=view.findViewById(R.id.home_scan_btn);
        mDestination=view.findViewById(R.id.home_scan_destination);
        mSource=view.findViewById(R.id.home_scan_source);
        mMailer=view.findViewById(R.id.home_scan_mailer);
        mReceiver=view.findViewById(R.id.home_scan_receiver);
        mMailerPhone=view.findViewById(R.id.home_scan_mailer_phone_number);
        mReceiverPhone=view.findViewById(R.id.home_scan_receiver_phone_number);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserId=Utils.getPreference(getActivity()).getString(LoginFragment.KEY_LOGIN_ACCOUNT,null);
                if(mUserId==null){
                    Utils.showAlertDialog(getString(R.string.alert),getString(R.string.login_first_alert),getActivity()).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Log.i(TAG,"Content:"+content);
                String[] packageInfo=content.split(":");
                Log.i(TAG,"len:"+packageInfo.length);
                mPackageId=packageInfo[0];
                mEncryptedPackageInfo=packageInfo[1];
                new CheckPermissionTask().execute();
            }
        }
    }

    private class CheckPermissionTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            String url= config.permission+"?postman_id="+mUserId+"&package_id="+mPackageId;
            JSONObject jsonObject=HttpAgent.fetchJSON(url);
            String code=null;
            try{
                code=jsonObject.getString("code");
                if(code.equals("1")){
                    return jsonObject.getString("key");
                }else {
                    return code;
                }
            }catch (JSONException je){
                Log.e(TAG,je.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String key) {
            if(key.equals("-1")||key==null){
                Utils.showAlertDialog(getString(R.string.warn_alert),getString(R.string.permission_denied_alert),getActivity()).show();
                return;
            }
            Log.i(TAG,"Key:"+key);
            String information=AESUtil.decrypt(mEncryptedPackageInfo,key);
            String[] infoArray=information.split(":");
            Log.i(TAG,"Information:"+information);
            mDestination.setText(infoArray[1]);
            mSource.setText(infoArray[2]);
            mMailer.setText(infoArray[3]);
            mMailerPhone.setText(infoArray[4]);
            mReceiver.setText(infoArray[5]);
            mReceiverPhone.setText(infoArray[6]);
        }
    }
}
