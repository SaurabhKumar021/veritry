package com.mmadapps.verisupplier.leftmenu;

import com.mmadapps.verisupplier.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShippingQuotationFragment extends Fragment{
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.quotation_fragment, container, false);
		return view;
	}

}
