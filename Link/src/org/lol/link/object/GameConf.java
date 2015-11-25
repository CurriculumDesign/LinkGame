package org.lol.link.object;

import android.content.Context;

/**
 * 提供一个参数构造器，给基本参数赋值，并进行构造
 */
public class GameConf {
	// 设置连连看的每个方块的图片的宽、高
	public static final int PIECE_WIDTH = 40;
	public static final int PIECE_HEIGHT = 40;
	// 记录游戏的总时间（60秒）.
	public static int DEFAULT_TIME = 60;
	// Piece[][]数组第一维的长度
	private int xSize;
	// Piece[][]数组第二维的长度
	private int ySize;
	// 游戏区域第一张图片出现的x座标
	private int beginImageX;
	// 游戏区域中第一张图片出现的y座标
	private int beginImageY;
	// 记录游戏的总时间, 单位是毫秒
	private long gameTime;
	private Context context;

	public GameConf(int xSize, int ySize, int beginImageX, int beginImageY,
			long gameTime, Context context) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.beginImageX = beginImageX;
		this.beginImageY = beginImageY;
		this.gameTime = gameTime;
		this.context = context;
	}

	public long getGameTime() {
		return gameTime;
	}

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public int getBeginImageX() {
		return beginImageX;
	}

	public int getBeginImageY() {
		return beginImageY;
	}

	public Context getContext() {
		return context;
	}
}
