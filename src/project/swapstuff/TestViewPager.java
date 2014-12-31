package project.swapstuff;

import project.swapstuff.model.Utills;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.viewpagerindicator.CirclePageIndicator;

public class TestViewPager extends Activity {

	ViewPager myPager;
	CirclePageIndicator CircleIndicator;
	Button btnTutorialContinue;

	boolean contiNue = false;
	private int previousState, currentState;
	boolean contiNueEND = true;

	private int imageArra[] = { R.drawable.first, R.drawable.second,
			R.drawable.third, R.drawable.fourth };

	int currentPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_test_view_pager);

		btnTutorialContinue = (Button) findViewById(R.id.btnTutorialExit);

		ViewPagerAdapterStartup adapter = new ViewPagerAdapterStartup();
		myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(0);

		CircleIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);
		CircleIndicator.setViewPager(myPager);

		btnTutorialContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (contiNue) {

					SharedPreferences SharedPref_StartUp = getSharedPreferences(
							"Startup", MODE_PRIVATE);
					Editor editTutorial = SharedPref_StartUp.edit();
					editTutorial.putString("tutorial", "Y");
					editTutorial.commit();

					Intent gotoHome = new Intent(getApplicationContext(),
							Login.class);
					startActivity(gotoHome);
					finish();

				}

				else {
//					currentPage++;
//					myPager.setCurrentItem(currentPage, true);
					SharedPreferences SharedPref_StartUp = getSharedPreferences(
							"Startup", MODE_PRIVATE);
					Editor editTutorial = SharedPref_StartUp.edit();
					editTutorial.putString("tutorial", "Y");
					editTutorial.commit();

					Intent gotoHome = new Intent(getApplicationContext(),
							Login.class);
					startActivity(gotoHome);
					finish();

				}
			}
		});

		myPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				CircleIndicator.setCurrentItem(position);

				if (position >= 3) {
					contiNue = true;
					btnTutorialContinue.setText("Continue..");
				} else {
					contiNue = false;
					btnTutorialContinue.setText("Skip");
					currentPage = position;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float positionOffset, int arg2) {
				// TODO Auto-generated method stub
				

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				int currentPage = myPager.getCurrentItem(); // ViewPager current page

				if (currentPage == 3) {
					previousState = currentState;
					currentState = state;
					if (previousState == 1 && currentState == 0) {

//						Utills.showToast(getApplicationContext(), "hi");
						SharedPreferences SharedPref_StartUp = getSharedPreferences(
								"Startup", MODE_PRIVATE);
						Editor editTutorial = SharedPref_StartUp.edit();
						editTutorial.putString("tutorial", "Y");
						editTutorial.commit();

						Intent gotoHome = new Intent(getApplicationContext(),
								Login.class);
						startActivity(gotoHome);
						finish();

					}
				}
			}
		});

	}

	public class ViewPagerAdapterStartup extends PagerAdapter {

		public int getCount() {
			return imageArra.length;
		}

		public Object instantiateItem(View collection, int position) {
			ImageView view = new ImageView(TestViewPager.this);
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			view.setScaleType(ScaleType.FIT_XY);
			view.setBackgroundResource(imageArra[position]);

			((ViewPager) collection).addView(view, 0);

			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	
}
