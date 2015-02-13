package com.schedario.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;
import com.schedario.constants.Constants;

@SuppressLint("ValidFragment")
public class TakePhotoFragment extends Fragment implements OnClickListener {

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

	public TakePhotoFragment(BaseActivity base) {
		this.base = base;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_take_photo, container,
				false);
		iv_take_photo = (ImageView) v.findViewById(R.id.iv_take_picture);
		iv_take_photo.setOnClickListener(this);

		iv_save_photo = (ImageView) v.findViewById(R.id.iv_save_photo);

		LinearLayout ll_upload = (LinearLayout) v.findViewById(R.id.ll_upload);
		ll_upload.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_take_picture:
			System.out.println("!! camera");
			callCamera();
			break;

		case R.id.ll_upload:
			if (!(selectedImagePath != null)) {
				Toast.makeText(base, "Please capture a picture",
						Toast.LENGTH_LONG).show();
			} else {
				new ImageUploadTask().execute();
			}
			break;
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		/*if (resultCode == Activity.RESULT_OK && requestCode == 1) {
			selectedImagePath = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/DCIM/"
					+ "img" + currentMiliTime + ".jpg");
			Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath
					.getAbsolutePath());
			iv_save_photo.setImageBitmap(bitmap);
		}
*/	
		
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				// successfully captured the image
				// display it in image view
				previewCapturedImage();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(base,
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(base,
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

			iv_save_photo.setImageBitmap(bitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void callCamera() {
		/*
		 * Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		 * currentMiliTime = ""+System.currentTimeMillis(); File file = new
		 * File(Environment.getExternalStorageDirectory().getAbsolutePath() +
		 * "/DCIM/" + "img"+currentMiliTime+".jpg");
		 * intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		 * startActivityForResult(intent, 1);
		 */

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		getActivity().startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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

	/*
	 * public String getRealPathFromURI(Uri contentUri) { try { String[] proj =
	 * {MediaStore.Images.Media.DATA}; Cursor cursor =
	 * base.managedQuery(contentUri, proj, null, null, null); int column_index =
	 * cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	 * cursor.moveToFirst(); return cursor.getString(column_index); } catch
	 * (Exception e) { return contentUri.getPath(); } }
	 */
	class ImageUploadTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... param) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(Constants.IMAGE_UPLOAD);
				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				entity.addPart("id", new StringBody(
						base.app.getUserinfo().user_id));
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
			pDialog = new ProgressDialog(base);
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
						Toast.makeText(base, "Image uploaded successfully",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
