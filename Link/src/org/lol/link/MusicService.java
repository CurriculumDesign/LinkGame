//背景音乐播放的实现
package org.lol.link;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
	private MediaPlayer mp;

	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		// 开始播放音乐
		mp.start();
		// 音乐播放完毕的事件处理
		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				// 循环播放
				try {
					mp.start();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// 播放音乐时发生错误的事件处理
		mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				// 释放资源
				try {
					mp.release();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return false;
			}
		});

		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		// 初始化音乐资源
		try {
			// 创建MediaPlayer对象
			mp = new MediaPlayer();
			// 将音乐保存在res/raw/lol.mp3,R.java中自动生成id
			mp = MediaPlayer.create(MusicService.this, R.raw.lol);
			// 在MediaPlayer取得播放资源与stop()之后要准备PlayBack的状态前一定要使用MediaPlayer.prepeare()
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// 服务停止时停止播放音乐并释放资源
		mp.stop();
		mp.release();

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
