package com.exitpoint.kkakkung;

import android.content.ClipData;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
=======
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
>>>>>>> 8865c91a3f89527812d0ad7893cd9ed2b28ffdad


public class Sample2 extends ActionBarActivity {
    private Button icon1;
    private Button icon2;
    private Button icon3;
    private Button icon4;

    private LinearLayout layout11;
    private LinearLayout layout22;

    int deviceHeight;
    int deviceWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample2);

<<<<<<< HEAD
        Toast.makeText(getApplicationContext(), "button shadow test", Toast.LENGTH_SHORT).show();
        icon1 = (Button) findViewById(R.id.button21);
        icon2 = (Button) findViewById(R.id.button22);
        layout11 = (LinearLayout) findViewById(R.id.layout11);
        layout22 = (LinearLayout) findViewById(R.id.layout22);

        layout11.setOnDragListener(new MyDragListener());
        layout22.setOnDragListener(new MyDragListener());
        icon1.setOnTouchListener(m_onTouchListener);
        icon2.setOnTouchListener(m_onTouchListener);
    }


    View.OnTouchListener m_onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View p_v, MotionEvent p_event) {
            switch (p_event.getAction()) {
                case MotionEvent.ACTION_DOWN: { // 놓았을때
                   /*
                   String id = String.valueOf(p_v.getId());//View id data test
                   Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                   */
                    String name = p_v.toString();//View id data test
                    Toast.makeText(getApplicationContext(), " " + name, Toast.LENGTH_SHORT).show();
                    ClipData data = ClipData.newPlainText("sssss", "aaaa");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(icon1);
                    p_v.startDrag(data, shadow, null, 0);

                }

                case MotionEvent.ACTION_MOVE: {
                    break;
                }
            }
            return true;
        }
    };

    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:

                    return true;
                case DragEvent.ACTION_DRAG_EXITED:

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:

                    return true;
                case DragEvent.ACTION_DROP: {
                    ClipData dragData = event.getClipData();
                    final String tag = dragData.getItemAt(0).getText().toString();

                    if (v == findViewById(R.id.layout11))
                        Toast.makeText(getApplicationContext(), "targetLayout: " + v.getTag() + " dragged :" + tag, Toast.LENGTH_SHORT).show();
                    else if (v == findViewById(R.id.layout22))
                        Toast.makeText(getApplicationContext(), "targetLayout: " + v.getTag() + " dragged :" + tag, Toast.LENGTH_SHORT).show();

                    return true;
                }

                case DragEvent.ACTION_DRAG_ENDED: {
                    // drag가 끝났을때

                    return (true);
                }

                default:
                    break;
            }
            return true;
        }
=======
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

>>>>>>> 8865c91a3f89527812d0ad7893cd9ed2b28ffdad
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
        // automatically handle clicks k on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
