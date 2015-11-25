package org.lol.link.board;

import org.lol.link.object.LinkInfo;
import org.lol.link.view.Piece;

/**
 * 此类主要是游戏逻辑接口
 */
public interface GameService {
	// 控制游戏开始的方法
	void start();

	// 定义一个接口方法, 用于返回一个二维数组@return 存放方块对象的二维数组
	Piece[][] getPieces();

	// 判断参数Piece[][]数组中是否还存在非空的Piece对象@return 如果还剩Piece对象返回true, 没有返回false
	boolean hasPieces();

	// 有鼠标获得X，Y坐标
	Piece findPiece(float touchX, float touchY);

	// 判断两个Piece是否可以相连
	LinkInfo link(Piece p1, Piece p2);
}
