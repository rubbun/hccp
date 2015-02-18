package com.schedario.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.schedario.activity.R;

public class Utility {
	
	public static String PREFERENCE_STORAGE = "SchedarioHCCPPreference";
	public static final String [] sender= new String [] {"Lalit", "RobinHood", "Captain", "HotVerySpicy", "Dharmendra", "PareshMayani", "Abhi", "SpK", "CapDroid"};
	public static final String [] messages= new String [] {
		"Aah! thats cool",
		"Tu really CoLor 6e", 
		"Get Lost!!",
		"@AdilSoomro @AdilSoomro",
		"Lets see what the Rock is cooking..!!",
		"Yeah! thats great.",
		"Awesome Awesome!",
		"@RobinHood.",
		"Lalit ka Dillllll...!!!",
		"I'm fine, thanks, what about you?"};
	
	
	public static String readXMLinString(String fileName, Context c) {
		try {
			InputStream is = c.getAssets().open(fileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String text = new String(buffer);

			return text;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	 public static void CopyStream(InputStream is, OutputStream os)
	    {
	        final int buffer_size=1024;
	        try
	        {
	            byte[] bytes=new byte[buffer_size];
	            for(;;)
	            {
	              int count=is.read(bytes, 0, buffer_size);
	              if(count==-1)
	                  break;
	              os.write(bytes, 0, count);
	            }
	        }
	        catch(Exception ex){}
	    }
	 /**
		 * Purpose: Store the value on preference based
		 * 
		 * @param ctx
		 * @param key
		 * @param value
		 */
		public static void storeValueOnPersistence(Context ctx, String key, String value) {
			SharedPreferences.Editor editor = ctx.getSharedPreferences(PREFERENCE_STORAGE, Activity.MODE_PRIVATE).edit();
			editor.putString(key, value);
			editor.commit();
		}
		
		/**
		* Purpose: Get value of supplied key from shared preference
		* 
		* @param ctx
		* @param key
		* @return
		*/
		public static String getValueFromPersistence(Context ctx, String key) {
			String result = "NA";
			SharedPreferences userPreferences = ctx.getSharedPreferences(PREFERENCE_STORAGE, Activity.MODE_PRIVATE);
			if (userPreferences.contains(key)) {
				result = userPreferences.getString(key, "NA");
			}
			return result; 
			
		}
}
