package de.smacsoftwares.aplanapp.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial1;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial2;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial3;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial4;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial5;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial6;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial7;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial8;
import de.smacsoftwares.aplanapp.util.GlobalClass;

public class TutorialActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    FrameLayout frameLayoutMain;
    private int dotsCount;
    private ImageView[] imgDots;
    public ViewPager viewPager;
    LinearLayout linearSectionIndicator;
    ImageView imgDot1,imgDot2,imgDot3,imgDot4,imgDot5,imgDot6,imgDot7,imgDot8;
    SamplePagerAdapter adapter;
    TextView txtSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutorial);
        initContorl();
    }
    //    this method intialize control and class all initial work
    public void initContorl() {
        if(!GlobalClass.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        GlobalClass.fontRegular = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPath);
        GlobalClass.fontLight = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathLight);
        GlobalClass.fontBold = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathBold);
        GlobalClass.fontMedium = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathMedium);
        frameLayoutMain = (FrameLayout)findViewById(R.id.framelayout_tutorial);
        GlobalClass.setupTypeface(frameLayoutMain, GlobalClass.fontLight);
        txtSkip = (TextView)findViewById(R.id.txt_skip);
        txtSkip.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);
        linearSectionIndicator=(LinearLayout)findViewById(R.id.linearsection);
        adapter = new SamplePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        setPageIndicator();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_skip:
                viewPager.setCurrentItem(7);
                break;
        }
    }
    public class SamplePagerAdapter extends FragmentPagerAdapter {
        public SamplePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public Fragment getItem(int position)
        {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new FragmentTutorial1();
            }
            else if(position == 1){
                return new FragmentTutorial2();
            }
            else  if (position ==2){
                return new FragmentTutorial3();
            }
            else if(position ==3){
                return new FragmentTutorial4();
            }
            else if(position == 4){

                return new FragmentTutorial5();
            }
            else if(position == 5){

                return new FragmentTutorial6();
            }
            else if(position == 6){

                return new FragmentTutorial7();
            }
            else if(position == 7){

                return new FragmentTutorial8();
            }
            else {
                return new FragmentTutorial1();
            }
        }
        @Override
        public int getCount() {
            return 8;
        }
    }
    /* this method draws pager indicator as page is changed to show current selected page */
    private void setPageIndicator() {
        dotsCount = adapter.getCount();
        imgDots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            imgDots[i] = new ImageView(this);
            imgDots[i].setImageDrawable(getResources().getDrawable(R.drawable.dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            linearSectionIndicator.addView(imgDots[i], params);
        }
        imgDots[0].setImageDrawable(getResources().getDrawable(R.drawable.dot_selected));
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        if(position==7){
            txtSkip.setVisibility(View.GONE);
        }
        else {
            txtSkip.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < dotsCount; i++) {
            imgDots[i].setImageDrawable(getResources().getDrawable(R.drawable.dot));
        }
        imgDots[position].setImageDrawable(getResources().getDrawable(R.drawable.dot_selected));
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
