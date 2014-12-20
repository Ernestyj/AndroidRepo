package com.androidstudy.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidstudy.MyApplication;
import com.androidstudy.R;

/**Download extends Activity
 * 多线程断点下载
 * 注意：网络操作一定要另起线程
 * @author Eugene
 * @date 2014-12-10
 */
public class Download extends Activity{
	private static final String TAG = "DownloadTest";
	
	private static final String URL_DOWNLOAD = "http://192.168.199.141:8080/SourceInsight.rar";
	//线程的数量
	private static int threadCount = 3;
	//每个下载区块的大小
	private static long blockSize;
	//正在运行的线程的数量
	private static int runningThreadCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final TextView textView = (TextView) findViewById(R.id.txView);
		
		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setVisibility(View.VISIBLE);
		btn1.setText("MultiThreadBreakpointDownload-JavaSE");
		btn1.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						MultiThreadDownload();
					}
				//注意：start()容易忘记写
				}).start();
				textView.setText("Downloading... See log.");
			}
		});
		
		Button btn2 = (Button) findViewById(R.id.btn2);
		btn2.setVisibility(View.VISIBLE);
		btn2.setText("DownloadWithProgessBar");
		btn2.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), DownloadWithProgressBar.class));
			}
		});
		
		Button btn3 = (Button) findViewById(R.id.btn3);
		btn3.setVisibility(View.VISIBLE);
		btn3.setText("DownloadByxUtils");
		btn3.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), DownloadByxUtils.class));
			}
		});
	}
	
	/**多线程断点下载（JavaSE）
	 * 保存到getFilesDir()
	 */
	public static void MultiThreadDownload(){
		URL url = null;
		HttpURLConnection conn = null;
		RandomAccessFile raf = null;
		int code = 0;
		try {
			url = new URL(URL_DOWNLOAD);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			code = conn.getResponseCode();
			if (code == 200) {
				// 得到服务端返回的文件的大小
				long fileSize = conn.getContentLength();
				Log.i(TAG, "File size：" + fileSize);
				//计算区块大小
				blockSize = fileSize / threadCount;
				// 1.首先在本地创建一个跟服务器文件一样大小的文件
				File file = new File(MyApplication.GetInstance().getFilesDir(), "test.dat");
				raf = new RandomAccessFile(file, "rw");
				//设置文件大小
				raf.setLength(fileSize);
				raf.close();
				// 2.开启若干个子线程分别去下载对应的资源
				runningThreadCount = threadCount;
				for (int i = 1; i <= threadCount; i++) {
					//0 * blockSize ~ 1 * blockSize -1
					//1 * blockSize ~ 2 * blockSize -1
					//2 * blockSize ~ 3 * blockSize -1 (fileSize - 1)
					long startIndex = (i - 1) * blockSize;
					long endIndex = i * blockSize - 1;
					if (i == threadCount) {
						// 最后一个线程分配的任务相对更多
						endIndex = fileSize - 1;
					}
					Log.i(TAG, "Thread " + i + " : " + startIndex + " ~ " + endIndex);
					
					new DownloadThread(URL_DOWNLOAD, i, startIndex, endIndex, 
							threadCount, runningThreadCount).start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.disconnect();
		}
	}
}

/**DownloadThread extends Thread
 * 断点下载线程（JavaSE）
 * 保存到getFilesDir()
 * @author Eugene
 * @data 2014-12-18
 */
class DownloadThread extends Thread {
	private static final String TAG = "DownloadTest";
	
	private int threadId;
	private long startIndex;
	private long endIndex;
	private String urlPath;
	private int threadCount, runningThreadCount;
	

	public DownloadThread(String urlPath, int threadId, long startIndex, long endIndex, 
			int threadCount, int runningThreadCount) {
		this.urlPath = urlPath;
		this.threadId = threadId;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.threadCount = threadCount;
		this.runningThreadCount = runningThreadCount;
	}

	@Override
	public void run() {
		try {
			// 当前线程下载的总大小
			int total = 0;
			// 保存下载进度的文件路径
			File logPath = new File(MyApplication.GetInstance().getFilesDir(), threadId + ".txt");
			
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			// 判断是否有记录，有则接着从上一次的位置继续下载数据
			if (logPath.exists() && logPath.length() > 0) {
				FileInputStream fis = new FileInputStream(logPath);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				// 获取当前线程上次下载的总大小是多少
				String lasttotalstr = br.readLine();
				int lastTotal = Integer.valueOf(lasttotalstr);
				Log.i(TAG, "Thread " + threadId + " downloaded " + startIndex + " ~ " + endIndex);
				startIndex += lastTotal;
				total += lastTotal;// 加上上次下载的总大小，更新total
				fis.close();
			}
			
			//设置请求头
			conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
			conn.setConnectTimeout(5000);
			
			InputStream is = conn.getInputStream();
			File dataPath = new File(MyApplication.GetInstance().getFilesDir(), "test.dat");
			RandomAccessFile dataFile = new RandomAccessFile(dataPath, "rw");
			// 指定文件开始写的位置
			dataFile.seek(startIndex);
			Log.i(TAG, "Thread " + threadId + " writes at: " + startIndex);
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				//写数据
				dataFile.write(buffer, 0, len);
				//写进度记录
				RandomAccessFile logFile = new RandomAccessFile(logPath, "rwd");//rwd模式写入更安全，但是对磁盘损伤大
				total += len;
				logFile.write(String.valueOf(total).getBytes());
				logFile.close();
			}
			is.close();
			dataFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 所有的线程都下载完毕后删除记录文件
			synchronized (DownloadThread.class) {//此处同步锁放置DownloadThread.class有点小问题，记录文件无法被删除
				Log.i(TAG, "Thread: " + threadId + " successfully done.");
				runningThreadCount--;
				if (runningThreadCount < 1) {
					Log.i(TAG, "All thread done. Deleting temp files.");
					for (int i = 1; i <= threadCount; i++) {
						File f = new File(MyApplication.GetInstance().getFilesDir(), threadId + ".txt");
						Log.i(TAG, "Deleted: " + f.delete());
					}
				}
			}

		}
	}
}

