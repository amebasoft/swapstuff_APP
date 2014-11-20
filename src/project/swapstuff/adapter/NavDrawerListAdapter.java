package project.swapstuff.adapter;

import java.util.ArrayList;

import project.swapstuff.R;
import project.swapstuff.model.NavDrawerItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView uiC_imgVIcon = (ImageView) convertView.findViewById(R.id.uiC_imgVicon);
		TextView uiC_txtTitle = (TextView) convertView.findViewById(R.id.uiC_txttitle);
		RelativeLayout rellayoutNavigation = (RelativeLayout) convertView
				.findViewById(R.id.layoutnavigationDrawer);
		
		
		if(position==0)
		{
			rellayoutNavigation.getBackground().setAlpha(220);
			rellayoutNavigation.setPadding(20, 20, 20, 20);
			uiC_imgVIcon.getLayoutParams().height=100;
			uiC_imgVIcon.getLayoutParams().width=150;
			uiC_imgVIcon.setImageResource(R.drawable.logo);
//			txtTitle.setText(navDrawerItems.get(position).getTitle());
//			txtTitle.setTypeface(Typeface.SANS_SERIF);
		}
		else
		{
			rellayoutNavigation.getBackground().setAlpha(220);
			rellayoutNavigation.setPadding(20, 20, 20, 20);
			uiC_imgVIcon.setImageResource(navDrawerItems.get(position).getIcon());
			uiC_txtTitle.setText(navDrawerItems.get(position).getTitle());

		}
	
//		if (position == 0) {
//			ArrayList<Profile_info> infoUser = ControlDB
//					.SelectFromUserDetails(context);
//
//			if (infoUser.size() > 0) {
//				rellayoutNavigation.getBackground().setAlpha(250);
//				
//				rellayoutNavigation.setBackgroundResource(R.drawable.bg);
//				imgIcon.getLayoutParams().height = 170;
//				imgIcon.getLayoutParams().width = 170;
//				imgIcon.setPadding(0, 10, 10, 10);
//
//				String htmlText = "<body><h1>"+infoUser.get(0).getTitle()+"</h1><br>View Profile</body>";
//				
//				// "<font color=#0B90A8>"+infoUser.get(0).getTitle()+">";
//				
//				imgIcon.setImageBitmap(Utills.StringToBitMap(infoUser.get(0)
//						.getImgDp()));
////				txtTitle.setText(TxttoShow + "\n" + "ViewProfile");
//				txtTitle.setTextColor(Color.parseColor("#0B90A8"));
//				txtTitle.setText(Html.fromHtml(htmlText));
//
//			}
//
//		} else {
//			rellayoutNavigation.getBackground().setAlpha(220);
//			rellayoutNavigation.setPadding(20, 20, 20, 20);
//			imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
//			txtTitle.setText(navDrawerItems.get(position).getTitle());
//		}

		return convertView;
	}
}
