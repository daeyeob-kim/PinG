package com.exitpoint.kkakkung;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class Sample2 extends ActionBarActivity {

    int deviceHeight;
    int deviceWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample2);

        //디바이스 사이즈를 구합시다.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        deviceWidth = metrics.widthPixels;
        deviceHeight = metrics.heightPixels;

        //레이아웃 사이즈 동적 변경을 위한 레이아웃 불러옴
        //만들고
        LinearLayout layout_friendslistpage1,layout_friendslistpage1_1,layout_friendslistpage1_2,layout_friendslistpage1_3;
        //매핑해주고
        layout_friendslistpage1=(LinearLayout) findViewById(R.id.layout_friendslistpage1);
        layout_friendslistpage1_1=(LinearLayout) findViewById(R.id.layout_friendslistpage1_1);
        layout_friendslistpage1_2=(LinearLayout) findViewById(R.id.layout_friendslistpage1_2);
        layout_friendslistpage1_3=(LinearLayout) findViewById(R.id.layout_friendslistpage1_3);

        layout_friendslistpage1_1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,deviceHeight/6+30));
        layout_friendslistpage1_2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,deviceHeight/6+30));
        layout_friendslistpage1_3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,deviceHeight/6+30));
        System.out.println("deviceWidth---------"+deviceWidth);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
