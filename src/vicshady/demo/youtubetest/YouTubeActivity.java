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
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class YouTubeActivity extends Activity{

	private static final String YOUTUBE_INFO_URL = "http://gdata.youtube.com/feeds/mobile/videos/_ID_?alt=json";
	private String VIDEO_ID;

	 private static ProgressDialog progressDialog;
	 String videourl;  
	 VideoView videoView ;


	 protected void onCreate(Bundle savedInstanceState)
	 {

	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.play_video);
	  Intent in=getIntent();
	  
	  VIDEO_ID=in.getStringExtra("videourl");
	  videoView = (VideoView) findViewById(R.id.videoview);
	  JKYouTubeTask jkYouTubeTask = (JKYouTubeTask) new JKYouTubeTask().execute(VIDEO_ID);

	   progressDialog = ProgressDialog.show(YouTubeActivity.this, "", "Buffering video...", true);
	   progressDialog.setCancelable(true);  
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
			String url = null;
			@Override
			protected Uri doInBackground(String... pParams) {
				
				try {
					url = getUrl(VIDEO_ID);
				} catch (IOException e) {

					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				Log.i("JKYOUTUBE_PREPARED_URI", url);
				return Uri.parse(url);
			}

			@Override
			protected void onPostExecute(Uri result) {
				super.onPostExecute(result);
				  try
			       {    
					  
						final MediaController mediaController = new MediaController(YouTubeActivity.this);
						videoView.setMediaController(mediaController);
						
			              mediaController.setAnchorView(videoView);           
			              videoView.setVideoURI(result);
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


			            }
			       catch(Exception e)
			       {
			                progressDialog.dismiss();
			                System.out.println("Video Play Error :"+e.toString());
			                finish();
			       }   


			
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