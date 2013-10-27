package com.example.mediarecorderexample;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		OnCompletionListener {

	TextView statusTextView;
	Button startRecording, stopRecording, playRecording, finishButton;
	MediaPlayer player;
	MediaRecorder recorder;
	File audioFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		statusTextView = (TextView) findViewById(R.id.StatusTextView);
		statusTextView.setText("Ready");

		stopRecording = (Button) findViewById(R.id.StopRecording);
		startRecording = (Button) findViewById(R.id.StartRecording);
		playRecording = (Button) findViewById(R.id.PlayRecording);
		finishButton = (Button) findViewById(R.id.FinishButton);

		startRecording.setOnClickListener(this);
		stopRecording.setOnClickListener(this);
		playRecording.setOnClickListener(this);
		finishButton.setOnClickListener(this);

		startRecording.setEnabled(true);
		stopRecording.setEnabled(false);
		playRecording.setEnabled(false);
		
	}

	@Override
	public void onClick(View v) {

		if (v == finishButton) {
			finish();
		} else if (v == stopRecording) {
			recorder.stop();
			recorder.release();
			player = new MediaPlayer();
			player.setOnCompletionListener(this);

			try {
				player.setDataSource(audioFile.getAbsolutePath());
				player.prepare();

				statusTextView.setText("Ready to play");

				playRecording.setEnabled(true);
				stopRecording.setEnabled(false);
				startRecording.setEnabled(true);
			} catch (Exception e) {
				Log.v("Exceptio in MediaPlayer.prepare()", e.toString());
			}
		} else if(v == startRecording) {
			recorder = new MediaRecorder();
			//设置音频输入源
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//设置音频的输出格式
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			//设置音频的编码格式
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			
			File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/recordFiles/");
			path.mkdirs();
			
			try {
				audioFile = File.createTempFile("recording", ".3gp", path);
			} catch(IOException e) {
				Log.v("Exception in CreateFile", e.toString());
			}
			//设置音频输出位置
			recorder.setOutputFile(audioFile.getAbsolutePath());
			//开始录制音频
			try {
				recorder.prepare();
			} catch(Exception e) {
				Log.v("MediaRecorder prepare error", e.toString());
			}
			recorder.start();
			statusTextView.setText("Recording...");
			
			playRecording.setEnabled(false);
			stopRecording.setEnabled(true);
			startRecording.setEnabled(false);
		} else if(v == playRecording) {
			// 开始回放录音
			player.start();
			
			statusTextView.setText("Playing...");
			
			playRecording.setEnabled(false);
			stopRecording.setEnabled(false);
			startRecording.setEnabled(false);
		}
	}
	
	
	@Override
	public void onCompletion(MediaPlayer mp) {

		playRecording.setEnabled(true);
		stopRecording.setEnabled(false);
		startRecording.setEnabled(true);
		statusTextView.setText("Ready");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
