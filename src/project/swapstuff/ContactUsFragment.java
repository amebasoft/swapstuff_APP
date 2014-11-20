package project.swapstuff;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactUsFragment extends Fragment {
	
	public ContactUsFragment(){}
	public ContactUsFragment(Context conn){
		
		this.con=conn;
	}
	Context con;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_contactus, container, false);
        
        

        
         
        return rootView;
    }
}
