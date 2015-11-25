//Activity实现

package org.lol.link;

import java.util.Timer;
import java.util.TimerTask;

import org.lol.link.board.GameService;
import org.lol.link.board.impl.GameServiceImpl;
import org.lol.link.object.GameConf;
import org.lol.link.object.LinkInfo;
import org.lol.link.view.GameView;
import org.lol.link.view.Piece;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Link extends Activity {
	private GameConf config;// 游戏配置对象
	private GameService gameService;// 游戏业务逻辑接口
	private GameView gameView;// 游戏界面
	private Button startButton;// 开始按钮
	private TextView timeTextView;// 记录剩余时间的TextView
	private AlertDialog.Builder lostDialog;// 失败后弹出的对话框
	private AlertDialog.Builder successDialog;// 游戏胜利后的对话框
	private Timer timer = new Timer();// 计时器
	private int gameTime;// 记录游戏的剩余时间
	private boolean isPlaying;// 记录是否处于游戏状态
	private Piece selected = null;// 记录已经选中的方块
	// 播放音效的SoundPool
	SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 8);
	int dis;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x123:
				timeTextView.setText("剩余时间： " + gameTime);
				gameTime--;
				// 时间小于0, 游戏失败
				if (gameTime < 0) {
					stopTimer();
					// 更改游戏的状态
					isPlaying = false;
					lostDialog.show();
					soundPool.play(dis, 1, 1, 0, 0, 1);
					return;
				}
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 初始化界面
		init();
	}

	// 初始化游戏的方法体
	private void init() {
		config = new GameConf(8, 9, 2, 10, 100000, this);
		// 得到游戏区域对象
		gameView = (GameView) findViewById(R.id.gameView);
		// 获取显示剩余时间的文本框
		timeTextView = (TextView) findViewById(R.id.timeText);
		// 获取开始按钮
		startButton = (Button) this.findViewById(R.id.startButton);
		// 初始化音效
		dis = soundPool.load(this, R.raw.dis, 1);

		// 添加背景音乐：此处略坑！！！
		Intent intent = new Intent(Link.this, MusicService.class);
		startService(intent);

		gameService = new GameServiceImpl(this.config);
		gameView.setGameService(gameService);

		// 开始按钮添加事件监听
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View source) {
				startGame(GameConf.DEFAULT_TIME);
			}
		});

		// 游戏区域触摸绑定事件监听
		this.gameView.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent e) {
				if (!isPlaying) {
					return false;
				}
				if (e.getAction() == MotionEvent.ACTION_DOWN) {
					gameViewTouchDown(e);
				}
				if (e.getAction() == MotionEvent.ACTION_UP) {
					gameViewTouchUp(e);
				}
				return true;
			}
		});

		// 游戏失败的对话框
		lostDialog = createDialog("Defeat", "联盟以你为耻，再来一发吗？骚年", R.drawable.lost)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startGame(GameConf.DEFAULT_TIME);
					}
				});
		// 游戏胜利的对话框
		successDialog = createDialog("Victory", "联盟为你荣耀，等你来战",
				R.drawable.success).setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startGame(GameConf.DEFAULT_TIME);
					}
				});
	}

	// 背景音乐的停止控制
	protected void onStop() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Link.this, MusicService.class);
		stopService(intent);
		super.onStop();
	}

	// 暂停游戏
	@Override
	protected void onPause() {
		stopTimer();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// 如果处于游戏状态中
		if (isPlaying) {
			// 以剩余时间重写开始游戏
			startGame(gameTime);
		}
		super.onResume();
	}

	// 触碰游戏区域的处理方法
	private void gameViewTouchDown(MotionEvent event) // ①
	{
		Piece[][] pieces = gameService.getPieces();
		float touchX = event.getX();// 获取用户点击的x座标
		float touchY = event.getY();// 获取用户点击的y座标
		// 由触碰对象得到图像对象
		Piece currentPiece = gameService.findPiece(touchX, touchY); // ②
		// 如果没有选中任何Piece对象(即鼠标点击的地方没有图片), 不再往下执行
		if (currentPiece == null)
			return;
		// 将gameView中的选中方块设为当前方块
		this.gameView.setSelectedPiece(currentPiece);
		// 表示之前没有选中任何一个Piece
		if (this.selected == null) {
			// 将当前方块设为已选中的方块, 重新将GamePanel绘制, 并不再往下执行
			this.selected = currentPiece;
			this.gameView.postInvalidate();
			return;
		}
		// 表示之前已经选择了一个
		if (this.selected != null) {
			// 在这里就要对currentPiece和prePiece进行判断并进行连接
			LinkInfo linkInfo = this.gameService.link(this.selected,
					currentPiece); // ③
			// 两个Piece不可连linkInfo为null
			if (linkInfo == null) {
				// 如果连接不成功, 将当前方块设为选中方块
				this.selected = currentPiece;
				this.gameView.postInvalidate();
			} else {
				// 处理成功连接
				handleSuccessLink(linkInfo, this.selected, currentPiece, pieces);
			}
		}
	}

	// 触碰游戏区域的处理方法
	private void gameViewTouchUp(MotionEvent e) {
		this.gameView.postInvalidate();
	}

	// 游戏计时的处理
	private void startGame(int gameTime) {
		// 如果之前的timer还未取消，取消timer
		if (this.timer != null) {
			stopTimer();
		}
		// 重新设置游戏时间
		this.gameTime = gameTime;
		// 如果游戏剩余时间与总游戏时间相等，即为重新开始新游戏
		if (gameTime == GameConf.DEFAULT_TIME) {
			// 开始新的游戏游戏
			gameView.startGame();
		}
		isPlaying = true;
		this.timer = new Timer();
		// 启动计时器，每秒刷新
		this.timer.schedule(new TimerTask() {
			public void run() {
				handler.sendEmptyMessage(0x123);
			}
		}, 0, 1000);
		// 将选中方块设为null。
		this.selected = null;
	}

	/*
	 * 成功连接后的处理
	 */

	// 核心逻辑算法，判断是否相连和判断当前游戏状态
	private void handleSuccessLink(LinkInfo linkInfo, Piece prePiece,
			Piece currentPiece, Piece[][] pieces) {
		// 它们可以相连, 让GamePanel处理LinkInfo
		this.gameView.setLinkInfo(linkInfo);
		this.gameView.setSelectedPiece(null);
		this.gameView.postInvalidate();
		// 将两个Piece对象从数组中删除
		pieces[prePiece.getIndexX()][prePiece.getIndexY()] = null;
		pieces[currentPiece.getIndexX()][currentPiece.getIndexY()] = null;
		// 将选中的方块设置null。
		this.selected = null;
		// 播放音效
		soundPool.play(dis, 1, 1, 0, 0, 1);
		// 判断是否还有剩下的方块, 如果没有, 游戏胜利
		if (!this.gameService.hasPieces()) {
			// 游戏胜利，对话框弹出
			this.successDialog.show();
			// 停止计时器
			stopTimer();
			// 更改游戏状态
			isPlaying = false;
		}
	}

	// 创建对话框
	private AlertDialog.Builder createDialog(String title, String message,
			int imageResource) {
		return new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(imageResource);
	}

	// 停止计时
	private void stopTimer() {
		this.timer.cancel();
		this.timer = null;
	}
}