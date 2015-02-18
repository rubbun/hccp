package com.schedario.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.schedario.activity.PulicyActivity;
import com.schedario.activity.R;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService extends IntentService {
    public SampleSchedulingService() {
        super("SchedulingService");
    }
    
    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
    // The string the app searches for in the Google home page content. If the app finds 
    // the string, it indicates the presence of a doodle.  
    public static final String SEARCH_STRING = "doodle";
    // The Google home page URL from which the app fetches content.
    // You can find a list of other Google domains with possible doodles here:
    // http://en.wikipedia.org/wiki/List_of_Google_domains
    public static final String URL = "http://www.google.com";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    PulicyActivity pulicyActivity = new PulicyActivity();
    String date;
    int dayOfWeek;
    
    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        // The URL from which to fetch content.
        String urlString = URL;
    
        String result ="";
        
        // Try to connect to the Google homepage and download content.
        try {
        	
        	if(pulicyActivity.isNetworkAvailable(this)){
        		final Calendar calendar = Calendar.getInstance();
        		calendar.setTimeInMillis(System.currentTimeMillis());
        		int hour  = calendar.get(Calendar.HOUR_OF_DAY);
        		int minutes = calendar.get(Calendar.MINUTE);
        		dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        		if(hour>=16&&minutes>=0)
        		
        		pulicyActivity.parseSetDate();
        		date = pulicyActivity.month+"/"+pulicyActivity.day+"/"+pulicyActivity.year;
        		Constants.ADD_PULICY_CHECK = date+"("+"Pulicy"+")";
        		Constants.ADD_ANIMAL_CHECK = date+"("+"Animal"+")";
        		
        		String checkpulicy = Utility.getValueFromPersistence(this, Constants.ADD_PULICY_CHECK);
        		String checkanimal = Utility.getValueFromPersistence(this, Constants.ADD_PULICY_CHECK);
        		if(Utility.getValueFromPersistence(this, Constants.ADD_PULICY_CHECK).equalsIgnoreCase("NA")
						&& !Utility.getValueFromPersistence(this, Constants.ADD_PULICY_CHECK).equalsIgnoreCase("Yes")){
        			
        			//pulicyActivity.checkAddorEdit = "add";
        			addPulicy();
        		}else if(Utility.getValueFromPersistence(this, Constants.ADD_ANIMAL_CHECK).equalsIgnoreCase("NA")
						&& !Utility.getValueFromPersistence(this, Constants.ADD_ANIMAL_CHECK).equalsIgnoreCase("Yes")){
        			
        			//pulicyActivity.checkAddorEdit = "add";
        			addAnimal();
        		}
        	}else{
        		Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_LONG).show();
        	}
           // result = loadFromNetwork(urlString);
        } catch (Exception e) {
            //Log.i(TAG, getString(R.string.connection_error));
        }
    
        // If the app finds the string "doodle" in the Google home page content, it
        // indicates the presence of a doodle. Post a "Doodle Alert" notification.
        if (result.indexOf(SEARCH_STRING) != -1) {
            //sendNotification(getString(R.string.doodle_found));
            Log.i(TAG, "Found doodle!!");
        } else {
            //sendNotification(getString(R.string.no_doodle));
        	//SampleAlarmReceiver.completeWakefulIntent(intent);
            Log.i(TAG, "No doodle found. :-(");
        }
        result="";
        // Release the wake lock provided by the BroadcastReceiver.
        
        // END_INCLUDE(service_onhandle)
    }
    
    // Post a notification indicating whether a doodle was found.
    private void sendNotification(String msg) {
          mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, PulicyActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        //.setContentTitle(getString(R.string.doodle_alert))
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
 
//
// The methods below this line fetch content from the specified URL and return the
// content as a string.
//
    /** Given a URL string, initiate a fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";
      
        try {
            stream = downloadUrl(urlString);
            str = readIt(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }      
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
    
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    /** 
     * Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from www.google.com.
     * @return String version of InputStream.
     * @throws IOException
     */
    private String readIt(InputStream stream) throws IOException {
      
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        for(String line = reader.readLine(); line != null; line = reader.readLine()) 
            builder.append(line);
        reader.close();
        return builder.toString();
    }
    
    
    void addPulicy(){
		String response = null;
		try {
			JSONObject req = new JSONObject();
			req.put("user_id", "9");
			if(dayOfWeek==5){
				//thursday
				req.put("name", "PS");
			}else{
				req.put("name", "PO");
			}
			req.put("added_date", date);
			
			response = KlHttpClient.SendHttpPost(Constants.ADD_PULICY, req.toString());
			if (response != null) {
				JSONObject ob = new JSONObject(response);
				boolean result = ob.getBoolean("status");
				if(result)
					Constants.ADD_PULICY_CHECK = date+"("+"Pulicy"+")";
					Utility.storeValueOnPersistence(this, Constants.ADD_PULICY_CHECK, "Yes");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
    }
    
    
    void addAnimal(){
		String response = null;
		try {
			JSONObject req = new JSONObject();
			req.put("user_id", "9");
			req.put("name", "No Animali infestanti/No Pets weeds");
			req.put("added_date", date);
			
			response = KlHttpClient.SendHttpPost(Constants.ADD_ANIMAL, req.toString());
			if (response != null) {
				JSONObject ob = new JSONObject(response);
				boolean result = ob.getBoolean("status");
				if(result)
					Constants.ADD_ANIMAL_CHECK = date+"("+"Animal"+")";
					Utility.storeValueOnPersistence(this, Constants.ADD_PULICY_CHECK, "Yes");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
    }
    
    
    
    
}
