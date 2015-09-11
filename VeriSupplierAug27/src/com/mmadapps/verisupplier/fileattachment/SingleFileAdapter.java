package com.mmadapps.verisupplier.fileattachment;

import android.content.Context;

public class SingleFileAdapter extends FileAdapter {

	public SingleFileAdapter(Context context) {
		super(context);
	}

	@Override
	protected boolean isCheckBoxVisibile() {
		return false;
	}

}
