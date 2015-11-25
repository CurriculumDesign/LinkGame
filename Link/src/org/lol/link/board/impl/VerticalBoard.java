package org.lol.link.board.impl;

import java.util.ArrayList;
import java.util.List;

import org.lol.link.board.AbstractBoard;
import org.lol.link.object.GameConf;
import org.lol.link.view.Piece;

/**
 * 竖向排列的游戏区域实现类
 */
public class VerticalBoard extends AbstractBoard {
	protected List<Piece> createPieces(GameConf config, Piece[][] pieces) {
		// 创建一个Piece集合, 该集合里面存放初始化游戏时所需的Piece对象
		List<Piece> notNullPieces = new ArrayList<Piece>();
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				// 加入判断, 符合一定条件才去构造Piece对象, 并加到集合中
				if (i % 2 == 0) {
					// 如果x能被2整除, 即单数列不会创建方块
					Piece piece = new Piece(i, j);
					// 添加到Piece集合中
					notNullPieces.add(piece);
				}
			}
		}
		return notNullPieces;
	}
}
