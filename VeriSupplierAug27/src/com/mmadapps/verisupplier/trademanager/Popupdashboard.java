package com.mmadapps.verisupplier.trademanager;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.mmadapps.verisupplier.R;

public class Popupdashboard  extends Activity {

	LinearLayout mainLayout;
	ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_dashboard);
//        actionBar=getActionBar();
//        actionBar.hide();
       /* mainLayout=(LinearLayout) findViewById(R.id.vL_popupDashboard);
		android.view.ViewGroup.LayoutParams layoutParams = mainLayout.getLayoutParams();
        layoutParams.width = getYOffSetbyDensityforCalwid();
        layoutParams.height = getYOffSetbyDensityforCal();
		mainLayout.setLayoutParams(layoutParams);
*/
    } 
    
    /*private int getYOffSetbyDensityforCalwid() {

        int density = getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_HIGH:
                return 450;
            case DisplayMetrics.DENSITY_XHIGH:
                return 650;
            case DisplayMetrics.DENSITY_XXHIGH:
                return 600;
            default:
                return 450;
        }
    }


    private int getYOffSetbyDensityforCal() {

        int density = getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_HIGH:
                return 450;
            case DisplayMetrics.DENSITY_XHIGH:
                return 650;
            case DisplayMetrics.DENSITY_XXHIGH:
                return 600;
            default:
                return 450;
        }
    }*/
}