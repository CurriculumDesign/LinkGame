package org.lol.link.board;

import java.util.List;

import org.lol.link.object.GameConf;
import org.lol.link.util.ImageUtil;
import org.lol.link.view.Piece;
import org.lol.link.view.PieceImage;

/**
 * 游戏区域的实现，我们选用抽象类，让子类去实现横向排列，纵向排列和矩阵排列
 */
public abstract class AbstractBoard {
	// 定义一个抽象方法, 让子类去实现（此处查阅了大量资料！！！）
	protected abstract List<Piece> createPieces(GameConf config,
			Piece[][] pieces);

	public Piece[][] create(GameConf config) {
		// 创建Piece[][]数组
		Piece[][] pieces = new Piece[config.getXSize()][config.getYSize()];
		List<Piece> notNullPieces = createPieces(config, pieces);
		List<PieceImage> playImages = ImageUtil.getPlayImages(
				config.getContext(), notNullPieces.size());
		// 所有图片的宽、高都是相同的
		int imageWidth = playImages.get(0).getImage().getWidth();
		int imageHeight = playImages.get(0).getImage().getHeight();

		for (int i = 0; i < notNullPieces.size(); i++) {

			Piece piece = notNullPieces.get(i);
			piece.setImage(playImages.get(i));
			// 计算每个方块左上角的X、Y座标
			piece.setBeginX(piece.getIndexX() * imageWidth
					+ config.getBeginImageX());
			piece.setBeginY(piece.getIndexY() * imageHeight
					+ config.getBeginImageY());
			// 将该方块对象放入方块数组的相应位置处
			pieces[piece.getIndexX()][piece.getIndexY()] = piece;
		}
		return pieces;
	}
}
