package vicshady.demo.youtubetest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayActivity extends Activity{
	 private String videoPath;

	 private static ProgressDialog progressDialog;
	 String videourl;  
	 VideoView videoView ;


	 protected void onCreate(Bundle savedInstanceState)
	 {

	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.play_video);

	   videoView = (VideoView) findViewById(R.id.videoview);
	   Intent intent=getIntent();
	   String Path=intent.getStringExtra("videourl");

	   progressDialog = ProgressDialog.show(VideoPlayActivity.this, "", "Buffering video...", true);
	   progressDialog.setCancelable(true);  


	   PlayVideo(Path);

	 }
	 private void PlayVideo(String videoPath)
	 {
	  try
	       {    
		        //requestWindowFeature(Window.FEATURE_NO_TITLE);
		        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	             getWindow().setFormat(PixelFormat.TRANSLUCENT);
	              MediaController mediaController = new MediaController(this);
	              mediaController.setAnchorView(videoView);           

	               Uri video = Uri.parse(videoPath);             
	               videoView.setMediaController(mediaController);
	               videoView.setVideoURI(video);
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

