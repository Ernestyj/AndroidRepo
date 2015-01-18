package com.androidstudy.media;

import java.io.File;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidstudy.R;

/**AudioPlayer extends Activity
 * 音频播放（可播放外部、网络音频文件）
 * @author Eugene
 * @data 2015-1-14
 */
public class AudioPlayer extends Activity{
//	private static final String TAG = "AudioPlayer";

	private EditText et_path;
	private Button bt_play, bt_pause;
	
	private MediaPlayer mediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_audioplayer);
		
		et_path = (EditText) findViewById(R.id.et_path);
		bt_play = (Button) findViewById(R.id.btn_play);
		bt_pause = (Button) findViewById(R.id.btn_pause);
	}
	
	/**播放音频（同步、异步方式播放）
	 * @param view
	 */
	public void play(View view) {
		String filepath = et_path.getText().toString().trim();
		File file = new File(filepath);
		if (filepath.startsWith("http://")) {//网络音频
			audioPlayAsync(filepath);
		}else if(file.exists()){//本地音频
			audioPlaySync(filepath);
		}else{
			Toast.makeText(this, "File dose not exist.", Toast.LENGTH_LONG).show();
		}
	}

	/**播放音频（同步方式），常用于本地资源播放
	 * @param filepath
	 */
	private void audioPlaySync(String filepath) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(filepath);//设置播放的数据源
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			//准备开始播放（同步方式）（通知本地做好播放准备）
			//播放的逻辑是C语言代码在新的线程里面执行
			mediaPlayer.prepare();
			mediaPlayer.start();
			
			bt_play.setEnabled(false);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					bt_play.setEnabled(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Fail to play.", Toast.LENGTH_LONG).show();
		}
	}
	
	/**播放音频（异步方式），常用于网络资源播放
	 * @param filepath
	 */
	private void audioPlayAsync(String filepath) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(filepath);//设置播放的数据源
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			//准备开始播放（异步方式）；开新的线程执行
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.start();
					bt_play.setEnabled(false);
				}
			});
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					bt_play.setEnabled(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Fail to play.", Toast.LENGTH_LONG).show();
		}
	}
	
	/**暂停播放音频
	 * @param view
	 */
	public void pause(View view) {
		if("继续".equals(bt_pause.getText().toString())){
			mediaPlayer.start();
			bt_pause.setText("暂停");
			return;
		}
		if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
			mediaPlayer.pause();
			bt_pause.setText("继续");
		}
	}
	
	/**停止播放音频
	 * @param view
	 */
	public void stop(View view) {
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		bt_pause.setText("暂停");
		bt_play.setEnabled(true);
	}
	
	/**重新播放音频
	 * @param view
	 */
	public void replay(View view) {
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.seekTo(0);
		}else{
			play(view);
		}
		bt_pause.setText("暂停");
	}
}
