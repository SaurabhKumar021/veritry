package com.mmadapps.verisupplier.leftmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mmadapps.verisupplier.R;

public class InfoFragment extends Fragment {
	
	LinearLayout vL_QF_titleLayout;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.quotation_fragment, container, false);
		initControl(view);
		return view;
	}

	private void initControl(View view) {
		vL_QF_titleLayout = (LinearLayout) view.findViewById(R.id.vL_QF_titleLayout);
		vL_QF_titleLayout.setVisibility(View.GONE);
	}
	
}
