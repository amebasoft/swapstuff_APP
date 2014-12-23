package project.swapstuff.adapter;

import java.util.ArrayList;

import project.swapstuff.R;
import project.swapstuff.TestViewPager;
import project.swapstuff.model.NearbyItems;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.loopj.android.image.SmartImageView;

public class ViewPagerAdapter extends PagerAdapter {

	Context activity;
	ArrayList<NearbyItems> imageArray = new ArrayList<NearbyItems>();

	public ViewPagerAdapter(Context act, ArrayList<NearbyItems> imgArra) {
		imageArray = imgArra;
		activity = act;
	}

	public int getCount() {
		return imageArray.size();
	}

	public Object instantiateItem(View convertView, int position) {

		

		 LayoutInflater mInflater = (LayoutInflater) activity
		 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 View view = mInflater.inflate(R.layout.viewpager_like_dislike, null);
		
		 SmartImageView viewImg = (SmartImageView) view
		 .findViewById(R.id.uiC_imgVViewPager);
		 
		 if(position==0 || position==2)
		 {
			 if(position==0)
			 {
				 viewImg.setImageResource(R.drawable.like_icon) ;
			 }
			 else
			 {
				 viewImg.setImageResource(R.drawable.dislike_icon) ;
			 }
			 ((ProgressBar)view.findViewById(R.id.progressBarViewPager)).setVisibility(View.INVISIBLE);
		 }
		 
		 viewImg.setImageUrl(imageArray.get(position).getImgs());
		 viewImg.bringToFront();
		
		
		 ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
		 lp.width = ViewPager.LayoutParams.FILL_PARENT;
		 lp.height = ViewPager.LayoutParams.FILL_PARENT;
		 view.setLayoutParams(lp);
		 ((ViewGroup) convertView).addView(view);

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