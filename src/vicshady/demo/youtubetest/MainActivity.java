package vicshady.demo.youtubetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

   EditText url;
   Button start;
   private String urldata;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        url=(EditText)findViewById(R.id.url);
        start=(Button)findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
			
			@Override
    public void onClick(View v) {
	// TODO Auto-generated method stub
	String data=url.getText().toString();
	String vid=getData(data);
     
/*
 * Redirecting To Specfic Player Youtube | Direct URL FETCHING...
 */
	if(vid=="invalid") //CAN"T FETCH VIDEO FROM THESE URL
	   {
		   Toast.makeText(getApplicationContext(), "Can't Fetch Video From these URL...", Toast.LENGTH_SHORT).show();
		   
	   }
	   else if(vid==data) //DIRECT FETCHING VIDEO FROM URL
	   {
		   Toast.makeText(getApplicationContext(), "FETCHING VIDEO...", Toast.LENGTH_LONG).show();
		   Intent i=new Intent(getApplicationContext(),VideoPlayActivity.class);
		   i.putExtra("videourl", vid);
		   startActivity(i);
	   }
	   else //YOUTUBE PLAYER
	   {
		Toast.makeText(getApplicationContext(), "FETCHING VIDEO FROM YOUTUBE...", Toast.LENGTH_LONG).show();
		Intent in=new Intent(getApplicationContext(),YouTubeActivity.class);
		in.putExtra("videourl", vid);
		startActivity(in);
	   }
/*
 * 		
 */
			}	
		});
    }
	
	public String getData(String data)
	{
				
		if(data.length()!=0)
		{
			int n= data.length();
			String ext=data.substring(n-4, n-3);
			String extension=data.substring(n-3, n);
			if(data.substring(0,22).equalsIgnoreCase("http://www.youtube.com"))
		    {
				urldata=data.substring(31, 42);
		//    	Toast.makeText(getApplicationContext(), ""+urldata, Toast.LENGTH_LONG).show();
			
	      	}
		   else if(data.substring(0,18).equalsIgnoreCase("http://youtube.com"))
		   {
			   	urldata=data.substring(27, 38);
			//   Toast.makeText(getApplicationContext(), ""+urldata, Toast.LENGTH_LONG).show();
		   }
		   else if(data.substring(0,15).equalsIgnoreCase("www.youtube.com"))
			   {
			       urldata=data.substring(24, 35);
				//   Toast.makeText(getApplicationContext(), ""+urldata, Toast.LENGTH_LONG).show();
			   }
		   else if(data.substring(0, 20).equalsIgnoreCase("http://m.youtube.com"))
		   {
			   urldata=data.substring(n-11, n);
			   //Toast.makeText(getApplicationContext(), ""+urldata, Toast.LENGTH_SHORT).show();
		   }
		   else if(data.substring(0, 13).equalsIgnoreCase("m.youtube.com"))
		   {
			   urldata=data.substring(n-11, n);
			 //  Toast.makeText(getApplicationContext(), ""+urldata, Toast.LENGTH_SHORT).show();
		   }
			   else if(ext.equalsIgnoreCase("."))
			   {
					//   Toast.makeText(getApplicationContext(), ""+extension, Toast.LENGTH_LONG).show();
					   urldata=data;
			   }
			   else
			   {
				  // Toast.makeText(getApplicationContext(), "Can't Fetch Video From these URL..", Toast.LENGTH_SHORT).show();
				   urldata="invalid";
			   }
		}
		else
		{
			urldata="invalid";
			Toast.makeText(getApplicationContext(), "Enter Valid Url", Toast.LENGTH_SHORT).show();
		}
	return urldata;	
	}    
  }