package com.androidstudy.media;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.androidstudy.R;

/**VideoPlayer extends Activity
 * 视频播放（统一采用异步方式，可播放外部、网络音频文件）
 * 双缓冲机制：内部有两个子线程A和B。
 * 		A 解码图像--->前台显示--->解码图像--->前台显示
 * 		B 			    解码图像--->前台显示--->解码图像
 * 
 * 在低版本模拟器上运行记得加上下面的参数（不自己维护双缓冲区，而是等待多媒体播放框架主动的推送数据）：
 * 		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
 * 
 * 注意：3gp格式所有版本的都支持；
 * 
 * 使用MediaPlayer时，出现error value (1, -2147483648), 
 * 		the '1' value corresponds to the constant in MediaPlayer.MEDIA_ERROR_UNKNOWN.
 * 		the '-2147483648' corresponds to hexadecimal 0x80000000 which is defined as UNKNOWN_ERROR in frameworks/native/include/utils/Errors.h.
 * This shows that the error's source is hard to pin down as it is quite a generic return value,
 * thrown by codec and compatibility issues as mentioned above but also thread cancellations and several other types.
 * 即很有可能是视频解码器不支持的缘故。
 * 
 * @author Eugene
 * @data 2015-1-14
 */
public class VideoPlayer extends Activity{
	private static final String TAG = "VideoPlayer";
	
	private EditText et_path;
	private Button bt_play, bt_pause;
	private SeekBar seekBar;
	private SurfaceView sv;
	private SurfaceHolder holder;
	
	private MediaPlayer mediaPlayer;
	
	private Timer timer;
	private TimerTask task;
	
	private int position, duration;
	private String filepath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_videoplayer);
		
		et_path = (EditText) findViewById(R.id.et_path);
		bt_play = (Button) findViewById(R.id.btn_play);
		bt_pause = (Button) findViewById(R.id.btn_pause);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int postion = seekBar.getProgress();
				mediaPlayer.seekTo(postion);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
		});
		
		sv = (SurfaceView) findViewById(R.id.sv);
		//得到显示界面内容的容器
		holder = sv.getHolder();
		//在低版本模拟器上运行记得加上下面的参数（不自己维护双缓冲区，而是等待多媒体播放框架主动的推送数据）
		//holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.i(TAG, "surfaceDestroyed()");
				if(mediaPlayer != null && mediaPlayer.isPlaying()){
					//记录播放位置
					position = mediaPlayer.getCurrentPosition();
					//释放资源
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
					//重置计时器
					timer.cancel();
					task.cancel();
					timer = null;
					task = null;
				}
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Log.i(TAG, "surfaceCreated()" + " position = " + position);
				if(position > 0){//有播放进度记录时
					videoPlayAsync(filepath);
					//跳转到播放记录
					mediaPlayer.seekTo(position);
				}
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				Log.i(TAG, "surfaceChanged()");
			}
		});
		
	}
	
	/**播放视频（统一采用异步方式）
	 * @param view
	 */
	public void play(View view) {
		filepath = et_path.getText().toString().trim();
		File file = new File(filepath);
		if (filepath.startsWith("http://")) {
			videoPlayAsync(filepath);
		} else if(file.exists()){
			videoPlayAsync(filepath);
		}else{
			Toast.makeText(this, "File does not exist.", Toast.LENGTH_LONG).show();
		}
	}

	/**播放视频（异步）
	 */
	private void videoPlayAsync(String filepath) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(filepath);//设置播放的数据源
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			//Sets the SurfaceHolder to use for displaying the video portion of the media.
			mediaPlayer.setDisplay(holder);
			//准备开始播放
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					//getDuration()在onPrepared()中调用，以确保mediaPlayer已被初始化
					duration = mediaPlayer.getDuration();
					Log.i(TAG, "duration = " + duration);
					//设置拖动进度条的最大值
					seekBar.setMax(duration);
					
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
			//设置进度条与定时器
			initTimer();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Fail to play.", Toast.LENGTH_LONG).show();
		}
	}

	/**设置定时器
	 */
	private void initTimer() {
		//设置定时器
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				if(mediaPlayer != null) seekBar.setProgress(mediaPlayer.getCurrentPosition());
			}
		};
		timer.schedule(task, 0, 500);//每0.5s更新进度条
	}
	
	/**暂停播放视频
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
	
	/**停止播放视频
	 * @param view
	 */
	public void stop(View view) {
		if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		bt_pause.setText("暂停");
		bt_play.setEnabled(true);
	}
	
	/**重新播放视频
	 * @param view
	 */
	public void replay(View view) {
		if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
			mediaPlayer.seekTo(0);
		}else{
			play(view);
		}
		bt_pause.setText("暂停");
	}
	
}
