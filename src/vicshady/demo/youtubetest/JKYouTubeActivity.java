package vicshady.demo.youtubetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * Name:		JKYouTubeActivity
 * Author:		Joshua Kendall <me@joshuakendall.com>
 * Description:	YouTube Activity to play videos in Android Applications, without launching the
 * 				browser or YouTube application by using VideoView and RTSP links.
 * Date:		May 6, 2011
 */
public class JKYouTubeActivity extends Activity {

	static final String YOUTUBE_INFO_URL = "http://gdata.youtube.com/feeds/mobile/videos/_ID_?alt=json";
	private String VIDEO_ID;
	private Window window;
	private VideoView videoView;
	 private static ProgressDialog progressDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		window = this.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		String gh="maw09z3pDAo";
		VIDEO_ID=gh;
		//VIDEO_ID = this.getIntent().getData().toString();
		Log.i("JKYOUTUBE_VIDEO_ID", VIDEO_ID);

		createView();
		JKYouTubeTask jkYouTubeTask = (JKYouTubeTask) new JKYouTubeTask().execute(VIDEO_ID);
		progressDialog = ProgressDialog.show(JKYouTubeActivity.this, "", "Buffering video...", true);
		   progressDialog.setCancelable(true);
	}

	private void createView() {
		// Linear Layout
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setId(1);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setBackgroundColor(Color.BLACK);
		this.setContentView(linearLayout);

		// Relative Layout
		RelativeLayout relativeLayout = new RelativeLayout(this);
		relativeLayout.setId(2);
		relativeLayout.setGravity(Gravity.CENTER);
		relativeLayout.setBackgroundColor(Color.BLACK);
		RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
		relativeLayout.setLayoutParams(relativeLayoutParams);
		linearLayout.addView(relativeLayout);

		// Video View
		videoView = new VideoView(this);
		videoView.setId(3);
		RelativeLayout.LayoutParams videoViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		videoViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		videoView.setLayoutParams(videoViewParams);
		relativeLayout.addView(videoView);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private class JKYouTubeTask extends AsyncTask<String, Void, Uri> {

		@Override
		protected Uri doInBackground(String... pParams) {
			String url = null;
			try {
				url = getUrl(VIDEO_ID);
			} catch (IOException e) {

				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
//			Log.i("JKYOUTUBE_PREPARED_URI", url);
			return Uri.parse(url);
		}

		@Override
		protected void onPostExecute(Uri result) {
			super.onPostExecute(result);
			try {
				videoView.setVideoURI(result);
				final MediaController mediaController = new MediaController(JKYouTubeActivity.this);
				videoView.setMediaController(mediaController);
				videoView.requestFocus();
				videoView.setOnPreparedListener(new OnPreparedListener()
	               {

	                   public void onPrepared(MediaPlayer mp)
	                   {                  
	                       progressDialog.dismiss();     
	                       videoView.start();
	                   }
	               });           

				
			} catch (Exception e) {
				Log.e("JKYOUTUBE_ERROR", "Error Playing Video!", e);
			}
		}

		private String getUrl(String id) throws IOException, JSONException {
			HttpClient client = new DefaultHttpClient();
			HttpGet clientGetMethod = new HttpGet(JKYouTubeActivity.YOUTUBE_INFO_URL.replace("_ID_", id));
			HttpResponse clientResponse = null;
			clientResponse = client.execute(clientGetMethod);
			String infoString = _convertStreamToString(clientResponse.getEntity().getContent());
			String urldata=new JSONObject(infoString).getJSONObject("entry").getJSONObject("media$group").getJSONArray("media$content").getJSONObject(0).getString("url");
			return new JSONObject(infoString).getJSONObject("entry").getJSONObject("media$group").getJSONArray("media$content").getJSONObject(0).getString("url");
		}

		private String _convertStreamToString(InputStream iS) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
			StringBuilder sB = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null)
				{
					sB.append(line).append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					iS.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sB.toString();
		}

	}

}