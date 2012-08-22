package jp.kuseful.leftmenusample;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;

public class LeftMenuSampleActivity extends ActivityGroup
	implements OnClickListener, OnItemClickListener {
	
	// 画面情報
	private int displayWidth = 0;
	private int displayHeight = 0;
	private int scrollX = 0;
	
	// メニューの開閉状態
	private boolean isOpenMenu = false;
	
	// View関係
	private RelativeLayout mainLayout = null;
	private ListView menuList = null;
	private ScrollView container = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // メインレイアウトを取得
        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        
        // メニューを生成
        menuList = (ListView)findViewById(R.id.menuList);
        createMenuList();
        menuList.setOnItemClickListener(this);
        
        // メニュー表示ボタンを取得しクリックイベントを設定
        Button headerButton = (Button)findViewById(R.id.headerButton);
        headerButton.setOnClickListener(this);
        
        // コンテナを取得
        container = (ScrollView)findViewById(R.id.container);
        changeActivity(0);
        
        // ディスプレイ情報を取得
        DisplayMetrics metrics = new DisplayMetrics();
        
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();
        
        // メニューをどのくらい表示するか
        scrollX = displayWidth - (int)(metrics.scaledDensity * 200);
    }

	@Override
	public void onClick(View v) {
		// メニューの表示・非表示を切り替える
		animMainLayout();
	}
	
	/**
	 * メニューの状態に合わせて表示・非表示を切り替える
	 */
	private void animMainLayout() {
		if (!isOpenMenu) {
			isOpenMenu = true;
			
			// メニューを表示するアニメーションを設定
			TranslateAnimation anim = new TranslateAnimation(0, scrollX, 0, 0);
			anim.setAnimationListener(new AnimationListener() {
				
				public void onAnimationStart(Animation animation) {}
				
				public void onAnimationRepeat(Animation animation) {}
				
				public void onAnimationEnd(Animation animation) {
					mainLayout.layout(scrollX, 0, displayWidth + scrollX, displayHeight);
					mainLayout.setAnimation(null);
				}
			});
			
			anim.setDuration(300);
			mainLayout.startAnimation(anim);
		} else {
			isOpenMenu = false;
			
			// メニューを非表示するアニメーションを設定
			mainLayout.layout(0, 0, displayWidth, displayHeight);
			TranslateAnimation anim = new TranslateAnimation(scrollX, 0, 0, 0);
			anim.setAnimationListener(new AnimationListener() {
				
				public void onAnimationStart(Animation animation) {}
				
				public void onAnimationRepeat(Animation animation) {}
				
				public void onAnimationEnd(Animation animation) {
					mainLayout.setAnimation(null);
				}
			});
			
			anim.setDuration(300);
			mainLayout.startAnimation(anim);
		}
	}
	
	/**
	 * メニューを生成する
	 */
	private void createMenuList() {
		Resources res = getResources();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		adapter.add(res.getString(R.string.home_text));
		adapter.add(res.getString(R.string.menu1_text));
		adapter.add(res.getString(R.string.menu2_text));
		adapter.add(res.getString(R.string.menu3_text));
		
		menuList.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// メニューを表示・非表示を切り替える
		animMainLayout();
		
		// ビューを切り替える
		changeActivity(position);
	}
	
	/**
	 * 表示するビューを切り替える
	 * @param position
	 */
	private void changeActivity(int position) {
		Intent intent = new Intent();
		String activeId = "";
		
		switch (position) {
			case 1:
				intent.setClass(this, Content1Activity.class);
				activeId = "content1Activity";
				break;
			case 2:
				intent.setClass(this, Content2Activity.class);
				activeId = "content2Activity";
				break;
			case 3:
				intent.setClass(this, Content3Activity.class);
				activeId = "content3Activity";
				break;
			default:
				intent.setClass(this, HomeActivity.class);
				activeId = "homeActivity";
				break;
		}
		
		// 表示されているビューを削除
		container.removeAllViewsInLayout();

		// 指定されたビューを表示
		Window containerActivity = getLocalActivityManager().startActivity(activeId, intent);
		container.addView(containerActivity.getDecorView());
	}
}