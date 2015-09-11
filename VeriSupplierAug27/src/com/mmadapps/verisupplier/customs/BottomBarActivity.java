package com.mmadapps.verisupplier.customs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult;
import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;

public class BottomBarActivity extends Activity{
	
	//bottom layout for homepage,wishlist,dashboard,openquote,userprofile
	LinearLayout home_layout,wishlist_layout,dashboard_layout,openquote_layout,userprofile_layout;
	
	//bottombar imageview
	public static ImageView home_selected,home_notselected,wishlist_selected,wishlist_notselected,dashboard_selected,dashboard_notselected,openquote_selected,openquote_notselected,userprofile_selected,userprofile_notselected;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tabbottom_layout);
		
		initializeView();
	}

	private void initializeView() {
		home_layout=(LinearLayout)findViewById(R.id.vL_bottombar_home);
		
		home_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home_selected=(ImageView)findViewById(R.id.vI_bottombar_homeselected);
				home_notselected=(ImageView)findViewById(R.id.vI_bottombar_home_notselected);
				finish();
			}
		});
	}

}
