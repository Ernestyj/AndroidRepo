package com.androidstudy.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidstudy.R;

/**DownloadWithProgressBar extends Activity
 * @author Eugene
 * @data 2014-12-20
 */
public class DownloadWithProgressBar extends Activity{
	private static final String TAG = "DownloadWithProgressBar";
	
	private static final int DOWNLOAD_ERROR = 1;
	private static final int THREAD_ERROR = 2;
	private static final int DWONLOAD_FINISH = 3;
	
	//线程的数量
	private int threadCount = 3;
	//每个下载区块的大小
	private long blocksize;
	//正在运行的线程的数量
	private  int runningThreadCount;
	
	private EditText et_path;
	private EditText et_count;
	//存放进度条的布局
	private LinearLayout ll_container;
	//进度条的集合
	private List<ProgressBar> pbList;
	
	//android下的消息处理器，在主线程创建，才可以更新UI
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_ERROR:
				Toast.makeText(getApplicationContext(), "Download fail.", Toast.LENGTH_SHORT).show();
				break;
			case THREAD_ERROR:
				Toast.makeText(getApplicationContext(), "Thread fail, please retry.", Toast.LENGTH_SHORT).show();
				break;
			case DWONLOAD_FINISH:
				Toast.makeText(getApplicationContext(), "Download complete.", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_downloadwithpb);
		
		et_path = (EditText) findViewById(R.id.et_path);
		et_count = (EditText) findViewById(R.id.et_count);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
	}
	
	/**多线程断点下载
	 * 保存到getExternalStorageDirectory()
	 * Button: android:onClick="download"
	 * 注意：指定android:onClick时应添加参数(View v)并声明public，否则无法识别；
	 * @param view
	 */
	public void download(View view){
		//下载文件的路径
		final String downloadPath = et_path.getText().toString().trim();
		if(TextUtils.isEmpty(downloadPath)){
			Toast.makeText(this, "Download path should not be null.", Toast.LENGTH_SHORT).show();
			return;
		}
		//下载文件的线程数
		String count = et_count.getText().toString().trim();
		if(TextUtils.isEmpty(downloadPath)){
			Toast.makeText(this, "Download thread should not be 0.", Toast.LENGTH_SHORT).show();
			return;
		}
		threadCount = Integer.parseInt(count);
		
		//先清空掉旧的进度条
		ll_container.removeAllViews();
		//在界面里面添加count个进度条
		pbList = new ArrayList<ProgressBar>();
		for(int i=0; i<threadCount; i++){
			ProgressBar pb = (ProgressBar) View.inflate(this, R.layout.progressbar, null);
			ll_container.addView(pb);
			pbList.add(pb);
		}
		Toast.makeText(this, "Begin to dowload.", Toast.LENGTH_SHORT).show();
		//注意调用.start();
		new Thread(){
			public void run() {
				HttpURLConnection conn = null;
				try {
					URL url = new URL(downloadPath);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if (code == 200) {
						long size = conn.getContentLength();// 得到服务端返回的文件的大小
						Log.i(TAG, "Download file size: " + size);
						blocksize = size / threadCount;
						// 1.首先在本地创建一个大小跟服务器一模一样的空白文件。
						File file = new File(Environment.getExternalStorageDirectory(), 
								getFileName(downloadPath));
						RandomAccessFile dataFile = new RandomAccessFile(file, "rw");
						// 分配下载文件大小
						dataFile.setLength(size);
						dataFile.close();
						// 2.开启若干个子线程分别去下载对应的资源。
						runningThreadCount = threadCount;
						for (int i = 1; i <= threadCount; i++) {
							//0 * blockSize ~ 1 * blockSize -1
							//1 * blockSize ~ 2 * blockSize -1
							//2 * blockSize ~ 3 * blockSize -1 (fileSize - 1)
							long startIndex = (i - 1) * blocksize;
							long endIndex = i * blocksize - 1;
							if (i == threadCount) {
								// 最后一个线程分配的任务相对更多
								endIndex = size - 1;
							}
							Log.i(TAG, "Thread " + i + " : " + startIndex + " ~ " + endIndex);
							
							int threadSize = (int) (endIndex - startIndex);
							pbList.get(i - 1).setMax(threadSize);
							
							new DownloadThread(downloadPath, i, startIndex, endIndex)
								.start();
						}
					}
				} catch (Exception e) {
					//obtain(): Allows us to avoid allocating new objects in many cases.
					Message msg = Message.obtain();
					msg.what = DOWNLOAD_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				} finally{
					conn.disconnect();
				}
				
			};
		}.start();
	}
	
	/**从下载路径获取下载文件名
	 * Ex: http://192.168.199.141:8080/SourceInsight.rar将返回SourceInsight.rar
	 * @param path
	 * @return 文件名
	 */
	private String getFileName(String path){
		int start = path.lastIndexOf("/") + 1;
		return path.substring(start);
	}
	
	/**DownloadThread extends Thread
	 * 断点下载线程
	 * 保存到getExternalStorageDirectory()
	 * @author Eugene
	 * @data 2014-12-20
	 */
	private class DownloadThread extends Thread {
		private int threadId;
		private long startIndex;
		private long endIndex;
		private String downloadPath;
		public DownloadThread(String path, int threadId, long startIndex,
				long endIndex) {
			this.downloadPath = path;
			this.threadId = threadId;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		@Override
		public void run() {
			try {
				// 当前线程下载的总大小
				int total = 0;
				// 保存下载进度的文件路径
				File positionFile = new File(Environment.getExternalStorageDirectory(), 
						getFileName(downloadPath) + threadId + ".txt");
				
				URL url = new URL(downloadPath);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				
				// 判断是否有记录，有则接着从上一次的位置继续下载数据
				if (positionFile.exists() && positionFile.length() > 0) {// 判断是否有记录
					//记录下载位置最好采用数据库，示例字段包括 _id, path, threadid, total
					FileInputStream fis = new FileInputStream(positionFile);
					BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					// 获取当前线程上次下载的总大小是多少
					String lasttotalstr = br.readLine();
					int lastTotal = Integer.valueOf(lasttotalstr);
					Log.i(TAG, "Thread " + threadId + " downloaded " + startIndex + " ~ " + endIndex);
					startIndex += lastTotal;
					total += lastTotal;// 加上上次下载的总大小。
					fis.close();
				}
				
				//设置请求头
				conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
				conn.setConnectTimeout(5000);
				
				InputStream is = conn.getInputStream();
				File dataPath = new File(Environment.getExternalStorageDirectory(),getFileName(downloadPath));
				RandomAccessFile dataFile = new RandomAccessFile(dataPath, "rw");
				// 指定文件开始写的位置。
				dataFile.seek(startIndex);
				Log.i(TAG, "Thread " + threadId + " writes at: " + startIndex);
				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					//写数据
					dataFile.write(buffer, 0, len);
					//写进度记录
					RandomAccessFile logFile = new RandomAccessFile(positionFile, "rwd");//rwd模式写入更安全，但是对磁盘损伤大
					total += len;
					logFile.write(String.valueOf(total).getBytes());
					logFile.close();
					
					//ProgressBar特性：可以在子线程调用更新
					pbList.get(threadId - 1).setProgress(total);
				}
				is.close();
				dataFile.close();
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = Message.obtain();
				msg.what = THREAD_ERROR;
				handler.sendMessage(msg);
			} finally {
				// 所有的线程都下载完毕后删除记录文件
				synchronized (DownloadWithProgressBar.class) {
					Log.i(TAG, "Thread: " + threadId + " successfully done.");
					runningThreadCount--;
					if (runningThreadCount < 1) {
						Log.i(TAG, "All thread done. Deleting temp files.");
						for (int i = 1; i <= threadCount; i++) {
							File f = new File(Environment.getExternalStorageDirectory(),getFileName(downloadPath)+ i + ".txt");
							Log.i(TAG, "Deleted: " + f.delete());
						}
						//obtain(): Allows us to avoid allocating new objects in many cases.
						Message msg = Message.obtain();
						msg.what = DWONLOAD_FINISH;
						handler.sendMessage(msg);
					}
				}

			}
		}
	}

}


