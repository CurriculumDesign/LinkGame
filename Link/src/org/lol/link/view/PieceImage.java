/**
 *
 */
package org.lol.link.view;

import android.graphics.Bitmap;

/**
 * 封装图片与其ID的类
 */
public class PieceImage {
	private Bitmap image;
	private int imageId;

	// 构造函数
	public PieceImage(Bitmap image, int imageId) {
		super();
		this.image = image;
		this.imageId = imageId;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
}
