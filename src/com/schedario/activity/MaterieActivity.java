package com.schedario.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.schedario.adapter.GalleryAdapter;
import com.schedario.bean.FirstImageBean;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class MaterieActivity extends BaseActivity{
	
	private LinearLayout ll_container,ll_take_photo,ll_gallery;
	private static final String TAB_TAKE_PHOTO = "take_picture";
	private static final String TAB_GALLERY= "gallery";
	
	public BaseActivity base;
	public static int CAMERA_PIC_REQUEST = 1;
	private ImageView iv_take_photo, iv_save_photo;
	private static File selectedImagePath = null;
	ProgressDialog pDialog;
	public HttpEntity resEntity;
	public String currentMiliTime = null;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private Uri fileUri;
	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	
	private LinearLayout  ll_include_take_photo,ll_include_gallery,ll_upload ;
	
	private ArrayList<FirstImageBean> list = new ArrayList<FirstImageBean>();
	private GalleryAdapter adapter;
	private  GridView gridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_materie);
		
		ll_take_photo = (LinearLayout)findViewById(R.id.ll_take_photo);
		ll_take_photo.setOnClickListener(this);
		
		ll_gallery = (LinearLayout)findViewById(R.id.ll_gallery);
		ll_gallery.setOnClickListener(this);
		
		ll_container = (LinearLayout)findViewById(R.id.ll_container);
		
		iv_take_photo = (ImageView)findViewById(R.id.iv_take_picture);
		iv_take_photo.setOnClickListener(this);

		iv_save_photo = (ImageView)findViewById(R.id.iv_save_photo);
		
		ll_include_gallery = (LinearLayout)findViewById(R.id.ll_include_gallery);
		
		ll_include_take_photo = (LinearLayout)findViewById(R.id.ll_include_take_photo);

		LinearLayout ll_upload = (LinearLayout)findViewById(R.id.ll_upload);
		ll_upload.setOnClickListener(this);
		
		gridView = (GridView)findViewById(R.id.gv_gallery);
		displayView(0);
	}
	
	public class AsyncDeleteImage extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			JSONObject req = new JSONObject();
			try {
				req.put("user_id", app.getUserinfo().getUser_id());
				String respponse = KlHttpClient.SendHttpPost(Constants.DELETE_IMAGE, req.toString());
				if(respponse != null){
					JSONObject ob = new JSONObject(respponse);
					if(ob.getBoolean("status")){
						return ob.getString("message");
					}else{
						return ob.getString("message");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(!result.equalsIgnoreCase("Successfully deleted")){
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				new GalleryAsynctask().execute();
			}else{
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void displayView(int position) {
		switch (position) {
		case 0:
			ll_include_take_photo.setVisibility(View.VISIBLE);
			ll_include_gallery.setVisibility(View.GONE);
			break;
		case 1:
			ll_include_take_photo.setVisibility(View.GONE);
			ll_include_gallery.setVisibility(View.VISIBLE);
			
			new GalleryAsynctask().execute();
			break;
		}
	}
	
	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.ll_take_photo:
			displayView(0);
			break;

		case R.id.ll_gallery:
			displayView(1);
			break;
			
		case R.id.iv_take_picture:
			System.out.println("!! camera");
			callCamera();
			break;

		case R.id.ll_upload:
			if (!(selectedImagePath != null)) {
				Toast.makeText(getApplicationContext(), "Please capture a picture",
						Toast.LENGTH_LONG).show();
			} else {
				new ImageUploadTask().execute();
			}
			break;
		}
		
	}
	
	public void callCamera() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}
	
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/*
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		
		if (type == MEDIA_TYPE_IMAGE) {
			selectedImagePath = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return selectedImagePath;
	}
	
	private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

	    ExifInterface ei;
	    try {
	        ei = new ExifInterface(path);
	        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
	                ExifInterface.ORIENTATION_NORMAL);
	        switch (orientation) {
	        case ExifInterface.ORIENTATION_ROTATE_90:
	            bitmap = rotateImage(bitmap, 90);
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_180:
	            bitmap = rotateImage(bitmap, 180);
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_270:
	            bitmap = rotateImage(bitmap, 270);
	            break;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return bitmap;
	}
	
	private Bitmap rotateImage(Bitmap source, float angle) {

	    Bitmap bitmap = null;
	    Matrix matrix = new Matrix();
	    matrix.postRotate(angle);
	    try {
	        bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
	                matrix, true);
	    } catch (OutOfMemoryError err) {
	        err.printStackTrace();
	    }
	    return bitmap;
	}
	
	class ImageUploadTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... param) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(Constants.IMAGE_UPLOAD);
				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				entity.addPart("id", new StringBody(
						app.getUserinfo().getUser_id()));
				entity.addPart("img", new FileBody(selectedImagePath));
				httpPost.setEntity(entity);
				System.out.println("!!Request  " + httpPost.getRequestLine());
				HttpResponse response;
				response = httpClient.execute(httpPost);
				resEntity = response.getEntity();
				final String response_str = EntityUtils.toString(resEntity);
				System.out.println("!!response:" + response_str);
				return response_str;
			} catch (Exception e) {
				pDialog.dismiss();
				return null;
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MaterieActivity.this);
			pDialog.setTitle("Uploading image...");
			pDialog.setMessage("Please Wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected void onPostExecute(String sResponse) {
			if (sResponse != null) {
				pDialog.dismiss();
				try {
					JSONObject json = new JSONObject(sResponse);
					if (json.getBoolean("status")) {
						Toast.makeText(getApplicationContext(), "Image uploaded successfully",
								Toast.LENGTH_LONG).show();
						displayView(1);
						iv_save_photo.setImageBitmap(null);
						selectedImagePath = null;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				// successfully captured the image
				// display it in image view
				previewCapturedImage();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
	/*
	 * Display image from a path to ImageView
	 */
	private void previewCapturedImage() {
		try {
			iv_save_photo.setVisibility(View.VISIBLE);

			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options);
			
			Bitmap mBitmap = null;
		    try {
		        mBitmap = imageOreintationValidator(bitmap,selectedImagePath.getAbsolutePath());
		    } catch (OutOfMemoryError err) {
		        err.printStackTrace();
		    }
			//imgPreview.setImageBitmap(bitmap);
			
		    iv_save_photo.setImageBitmap(mBitmap);

			//iv_save_photo.setImageBitmap(bitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public class GalleryAsynctask extends AsyncTask<Void, Void, ArrayList<FirstImageBean>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected ArrayList<FirstImageBean> doInBackground(Void... params) {

			try {
				JSONObject req = new JSONObject();
				req.put("id", app.getUserinfo().getUser_id());
				String response = KlHttpClient.SendHttpPost(Constants.GALLERY, req.toString());
				if (response != null) {
					JSONObject ob = new JSONObject(response);
					if (ob.getBoolean("status")) {
						Log.e("!reach here", "reach here");
						JSONArray jArr = ob.getJSONArray("images");
						list.clear();
						for(int i=0; i<jArr.length(); i++){
							String image  = jArr.getJSONObject(i).getString("image");
							String id = jArr.getJSONObject(i).getString("id");
						list.add(new FirstImageBean(id, image));
						}
						return list;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<FirstImageBean> result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if(result!=null){
				adapter = new GalleryAdapter(MaterieActivity.this, R.layout.row_gallery, result);
				gridView.setAdapter(adapter);
			}
		}
	}
}
