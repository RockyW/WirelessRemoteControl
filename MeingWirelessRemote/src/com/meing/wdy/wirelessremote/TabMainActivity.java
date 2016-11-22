package com.meing.wdy.wirelessremote;

import zxing.toptech.utils.Constants;
import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TabMainActivity extends TabActivity {

	private TabHost tabHost;
	private SharedPreferences sp;
	private String connectionValue = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		tabHost = getTabHost();
		final TabWidget tabWidget = tabHost.getTabWidget();
		/* ȥ����ǩ�·��İ��� */
		tabWidget.setStripEnabled(false);
		if (Build.VERSION.SDK_INT >= 11)
			tabWidget.setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);

		tabHost.addTab(tabHost.newTabSpec("meing").setIndicator("")
				.setContent(new Intent(this, MainActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("light").setIndicator("")
				.setContent(new Intent(this, LightActivity.class)));
		tabHost.setCurrentTabByTag("meing");
		updateTab(tabHost);
		tabHost.setOnTabChangedListener(new OnTabChangedListener());
		
		sp = getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
		connectionValue = sp.getString(Constants.CONNECTION_KEY_SHARED, "");
		if (connectionValue == null || "".equals(connectionValue)) {
			Intent intent = new Intent(this, MatchActivity.class);
			startActivity(intent);
			finish();
		}
	}

	class OnTabChangedListener implements OnTabChangeListener {

		@Override
		public void onTabChanged(String tabId) {
			tabHost.setCurrentTabByTag(tabId);
			System.out.println("tabid " + tabId);
			System.out.println("curreny after: " + tabHost.getCurrentTabTag());
			updateTab(tabHost);
		}
	}

	/**
	 * ����Tab��ǩ����ɫ�����������ɫ
	 * 
	 * @param tabHost
	 */
	private void updateTab(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View view = tabHost.getTabWidget().getChildAt(i);
			TextView tv = (TextView) view.findViewById(android.R.id.title); // �õ�title
			ImageView iv = (ImageView) view.findViewById(android.R.id.icon); // �õ�icon
			tv.setTextSize(16);
			tv.setTypeface(Typeface.SERIF, 2); // ��������ͷ��
			if (tabHost.getCurrentTab() == i) {// ѡ��
				if (i == 0) {
					view.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.tab_film_pressed));// ѡ�к�ı���
				} else {
					view.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.tab_light_pressed));// ѡ�к�ı���
				}
				// tv.setTextColor(this.getResources().getColorStateList(
				// android.R.color.black));
			} else {// ��ѡ��
				if (i == 0) {
					view.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.tab_film_normal));// δѡ�к�ı���
				} else {
					view.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.tab_light_normal));// δѡ�к�ı���
				}
				// tv.setTextColor(this.getResources().getColorStateList(
				// android.R.color.white));
			}
		}
	}
}
