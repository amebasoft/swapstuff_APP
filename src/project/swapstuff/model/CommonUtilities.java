package project.swapstuff.model;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
    static final String SERVER_URL = "https://android.googleapis.com/gcm/send"; 

    // Google project id
    public static final String SENDER_ID = "326171808899";  
    
    public static String GCM_ID = "";

    public static final String DISPLAY_MESSAGE_ACTION =
            "project.swapstuff.model.DISPLAY_MESSAGE"; 

    public static final String EXTRA_MESSAGE = "message"; 
    
    
    public static int FragmentToOpen = 0;
    
    

    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
