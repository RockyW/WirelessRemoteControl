package com.meing.wdy.wirelessremote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zxing.android.CaptureActivity;

import zxing.toptech.utils.Constants;
import zxing.toptech.utils.PropertiesUtil;
import zxing.toptech.utils.Util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MatchActivity extends Activity implements CallBackInterface {

	private AutoCompleteTextView autoCompleteTextView = null;
	private SharedPreferences sp;
	private TextView infomationText = null;
	private List<String> recordList = null;
	private ArrayAdapter<String> adapter = null;
	private GetDeviceList device;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match);
		initView();
	}

	private void initView() {
		device = new GetDeviceList(this, this);

		recordList = new ArrayList<String>();
		recordList = PropertiesUtil.load();

		Iterator<String> interator = recordList.iterator();
		while (interator.hasNext()) {
			Log.d("remote", "interator.next() = " + interator.next());
		}

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, recordList);
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.connect_value);
		autoCompleteTextView.setAdapter(adapter);
		infomationText = (TextView) findViewById(R.id.infomation_label);
		sp = getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
	}

	public void onClick(View view) {
		int viewId = view.getId();
		switch (viewId) {
		case R.id.login_button:
			String value = autoCompleteTextView.getText().toString().trim();
			Log.d("remote", "autoCompleteTextView.getText() = " + value);
			if (value != null && value.length() > 0) {
				if (Util.regexMatch(value)) {
					if (!recordList.contains(value)) {
						recordList.add(value);
						PropertiesUtil.store((ArrayList<String>) recordList);
					}

					Intent mIntent = new Intent(this, TabMainActivity.class);
					sp.edit().putString(Constants.CONNECTION_KEY_SHARED, value)
							.commit();
					startActivity(mIntent);
					finish();
				} else {
					displayInformation(R.string.wrongvalue);
				}
			} else {
				displayInformation(R.string.nullvalue);
			}
			break;
		case R.id.back_to_mainactivity:
			Intent mIntent = new Intent(this, TabMainActivity.class);
			startActivity(mIntent);
			Constants.isget = false;
			finish();
			break;
		case R.id.img_scan:
			startScan();
			break;
		case R.id.img_search:
			startSearch();
			break;
		}
	}

	private void startSearch() {
		device.show();
	}

	private void startScan() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, CaptureActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			String result = intent.getStringExtra("result");
			if(result != null){
				autoCompleteTextView.setText(result);
			}
		}
	}

	private void displayInformation(int id) {
		autoCompleteTextView.setText("");
		infomationText.setVisibility(View.VISIBLE);
		infomationText.setText(getResources().getText(id));
	}

	@Override
	public void setSerial(String serical) {
		// TODO Auto-generated method stub
		autoCompleteTextView.setText(serical);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Constants.isget = false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Constants.isget = false;
		finish();
	}
}
