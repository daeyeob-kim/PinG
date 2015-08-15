package com.exitpoint.kkakkung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by S on 2015-08-12.
 */
public class GcmActivity extends Activity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String TAG = "GCMActivity";
    //서버주소
    final static String REG_URL = "http://192.168.0.6:8080/MyServer1/register_gcm.jsp";
    final static String SENDM_URL = "http://192.168.0.6:8080/MyServer1/SendMessage.jsp";

    GoogleCloudMessaging gcm;
    String mRegid;
    /**
     * 서버 : Sender 객체 선언
     */
    Sender sender;

    Handler handler = new Handler();
    /*
     * 등록된 ID 저장
     */
    ArrayList<String> idList = new ArrayList<String>();

    /**
     * collapseKey 설정을 위한 Random 객체
     */
    private Random random;

    /**
     * 구글 서버에 메시지 보관하는 기간(초단위로 4주까지 가능)
     */
    private int TTLTime = 60;

    /**
     * 단말기에 메시지 전송 재시도 횟수
     */
    private	int RETRY = 3;

    Button sendBtn;
    TextView receiveText;
    EditText sendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        sendBtn = (Button) findViewById(R.id.btn_send);
        receiveText = (TextView) findViewById(R.id.txt_receive);
        sendText = (EditText) findViewById(R.id.editText_send);

        sender = new Sender(GCMInfo.GOOGLE_API_KEY);

        //폰에 저장되어있는 GCM_ID를 불러옴
        mRegid = getRegistrationId(getApplicationContext());
        if(mRegid.isEmpty()) {
            registerInBackground();//구글에 폰 등록 후 GCM_ID받아옴
        } else
        {
            println("이미 등록 되어 있습니다..");
        }


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = sendText.getText().toString();

                sendMessage(data);
            }
        });

        // 인텐트를 전달받는 경우
        Intent intent = getIntent();
        if (intent != null) {
            processIntent(intent);
        }
    }

    //폰에 저장되어 있는 GCM_ID 가져온다. 없으면 ""반환
    private String getRegistrationId(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        Log.i(TAG,"REG_ID : " + PROPERTY_REG_ID);
        String registrationId = prefs.getString(PROPERTY_REG_ID,"");
        Log.i(TAG, "R : " + registrationId);
        if(registrationId.isEmpty()){
            Log.i(TAG,"Registration not found");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion != currentVersion){
            Log.i(TAG, "App version changed");
            return "";
        }

        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context)
    {
        return getSharedPreferences(GcmActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * @return {@code PackageManager}의 애플리케이션의 버전 코드.
     */
    //앱 버전을 받아온다
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // 일어나지 않아야 합니다
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    //구글에 단말 등록
    private void registerInBackground(){
        new AsyncTask<Void, Void ,Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try{
                    if(gcm == null){
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    GCMInfo.RegistrationId = gcm.register(GCMInfo.PROJECT_ID);
                    println("푸시 서비스를 위해 단말을 등록했습니다.");
                    println("등록 ID : " + GCMInfo.RegistrationId);

                    //서버로 보내기기
                   sendRegistrationIdToBackend(GCMInfo.RegistrationId);

                    //id 보관
                    storeRegistrationId(getApplicationContext(), GCMInfo.RegistrationId);

                }catch (IOException e){ e.printStackTrace();};
                return null;
            }
        }.execute(null,null,null);
    }

    //자체 서버로 전송(Http get방식)
    private void sendRegistrationIdToBackend(String regId){
        try {
            String parameter = "?" + URLEncoder.encode("gd", "UTF-8") + "=" + URLEncoder.encode(regId, "UTF-8");

            URL url = new URL(REG_URL + parameter);
            Log.i(TAG, url+"");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);

            con.connect();

            //con.getResponseCode() 해야 실행이 되는거 같음
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "Register OK");
            }
        }
        catch (Exception e){ e.printStackTrace();}

    }

    //앱 내부에 저장
    private void storeRegistrationId(Context context, String regId){
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version" + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    //매시지 보내기
    private void sendMessage(String msg){
        new AsyncTask<String, Void, Void>(){

            @Override
            protected Void doInBackground(String... params) {

                try{
                    String m="";
                    //어차피 한개 들어오지만.. ASYNC를 쓰기위해..
                    for(String s : params)
                        m = s;

                    String parameter = "?" + URLEncoder.encode("msg","UTF-8") + "=" + URLEncoder.encode(m, "UTF-8") + // 메시지부분
                            "&" + URLEncoder.encode("id", "UTF-8") + "="
                            + URLEncoder.encode("8","UTF-8");//"8" 대신 보낼 친구 R_ID 넣으면 됌.

                    URL url = new URL(SENDM_URL + parameter);
                    Log.i(TAG, "URL=" + url);

                    HttpURLConnection con =(HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(3000);
                    con.setReadTimeout(3000);

                    con.connect();

                    if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        Log.i(TAG, "Send Message OK");
                    }

                } catch (Exception e){e.printStackTrace();}


                return null;
            }
        }.execute(msg);
    }



    // google play service 사용 가능한지
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i(TAG, "this device is not supported");
            }
            return false;
        }
        return true;
    }


    private void println(String msg) {
        final String output = msg;
        handler.post(new Runnable() {
            public void run() {
                Log.d(TAG, output);
                Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
            }
        });
    }

    //이 아래부분은 호출이 안됌
    @Override
    protected void onNewIntent(Intent intent) {

        Log.d(TAG, "onNewIntent() called.");

        processIntent(intent);
        super.onNewIntent(intent);
    }

    /**
     * 수신자로부터 전달받은 Intent 처리
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if (from == null) {
            Log.d(TAG, "from is null.");
            return;
        }
        String data = intent.getStringExtra("msg");
        receiveText.setText("[" + from + "]로부터 수신한 데이터 : " + data);
    }
}
