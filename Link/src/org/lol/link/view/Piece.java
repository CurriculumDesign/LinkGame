package org.lol.link.view;

import android.graphics.Point;

/**
 * 练练看游戏区域方块对象的处理类
 */
public class Piece {
	// 保存方块对象的所对应的图片
	private PieceImage image;
	// 该方块图像的左上角的x坐标
	private int beginX;
	// 该方块图像的左上角的y座标
	private int beginY;

	private int indexX;

	private int indexY;

	public Piece(int indexX, int indexY) {
		this.indexX = indexX;
		this.indexY = indexY;
	}

	public int getBeginX() {
		return beginX;
	}

	public void setBeginX(int beginX) {
		this.beginX = beginX;
	}

	public int getBeginY() {
		return beginY;
	}

	public void setBeginY(int beginY) {
		this.beginY = beginY;
	}

	public int getIndexX() {
		return indexX;
	}

	public void setIndexX(int indexX) {
		this.indexX = indexX;
	}

	public int getIndexY() {
		return indexY;
	}

	public void setIndexY(int indexY) {
		this.indexY = indexY;
	}

	public PieceImage getImage() {
		return image;
	}

	public void setImage(PieceImage image) {
		this.image = image;
	}

	// 获取该Piece的中心
	public Point getCenter() {
		return new Point(getImage().getImage().getWidth() / 2 + getBeginX(),
				getBeginY() + getImage().getImage().getHeight() / 2);
	}

	// 判断两个图片是否相同
	public boolean isSameImage(Piece other) {
		if (image == null) {
			if (other.image != null)
				return false;
		}
		// 只要图片ID相同，即可认为两个Piece相等。
		return image.getImageId() == other.image.getImageId();
	}
}
