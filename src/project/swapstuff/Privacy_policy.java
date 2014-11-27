package project.swapstuff;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

public class Privacy_policy extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacy_policy);
		
		
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#0B90A8")));

		getActionBar().setIcon(R.drawable.btn_back);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> \t \t \t \t  Privacy Policy</font>"));
		
		
		 ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
		            ActionBar.LayoutParams.MATCH_PARENT,
		            ActionBar.LayoutParams.MATCH_PARENT, Gravity.RIGHT
		                    | Gravity.CENTER_VERTICAL);
		   TextView tvheading=new TextView(Privacy_policy.this);
		   tvheading.setLayoutParams(layoutParams);

		getActionBar().setCustomView(tvheading);
		
		
		
	}

	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	

		switch (item.getItemId()) {
		case android.R.id.home:
			
		finish();
			break;

		}

		return super.onOptionsItemSelected(item);
	}


}
