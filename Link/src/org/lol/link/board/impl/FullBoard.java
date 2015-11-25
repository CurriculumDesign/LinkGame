package org.lol.link.board.impl;

import java.util.ArrayList;
import java.util.List;

import org.lol.link.board.AbstractBoard;
import org.lol.link.object.GameConf;
import org.lol.link.view.Piece;

/**
 * 矩阵游戏区域的实现类
 */
public class FullBoard extends AbstractBoard {
	@Override
	protected List<Piece> createPieces(GameConf config, Piece[][] pieces) {
		// 创建一个Piece集合, 该集合里面存放初始化游戏时所需的Piece对象
		List<Piece> notNullPieces = new ArrayList<Piece>();
		for (int i = 1; i < pieces.length - 1; i++) {
			for (int j = 1; j < pieces[i].length - 1; j++) {
				Piece piece = new Piece(i, j);
				// 添加到Piece集合中
				notNullPieces.add(piece);
			}
		}
		return notNullPieces;
	}
}
