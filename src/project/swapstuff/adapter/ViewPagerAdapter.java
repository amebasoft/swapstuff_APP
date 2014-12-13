package project.swapstuff.adapter;

import java.util.ArrayList;

import project.swapstuff.model.NearbyItems;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.loopj.android.image.SmartImageView;

public class ViewPagerAdapter extends PagerAdapter {

 Activity activity;
 ArrayList<NearbyItems> imageArray=new ArrayList<NearbyItems>();

 public ViewPagerAdapter(Activity act, ArrayList<NearbyItems> imgArra) {
  imageArray = imgArra;
  activity = act;
 }

 public int getCount() {
  return imageArray.size();
 }

 public Object instantiateItem(View collection, int position) {
  SmartImageView view = new SmartImageView(activity);
  view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
    LayoutParams.FILL_PARENT));
//  view.setScaleType(ScaleType.FIT_XY);
//  view.setBackgroundResource(R.drawable.loding_img);
  view.setImageUrl(imageArray.get(position).getImgs());
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