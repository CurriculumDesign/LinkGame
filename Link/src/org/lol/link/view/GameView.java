package org.lol.link.view;

import java.util.List;

import android.graphics.Point;
import org.lol.link.R;
import org.lol.link.board.GameService;
import org.lol.link.object.LinkInfo;
import org.lol.link.util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Game的view类的实现
 */
public class GameView extends View {
	// 游戏逻辑的实现类
	private GameService gameService;
	// 保存当前已经被选中的方块
	private Piece selectedPiece;
	// 连接信息对象
	private LinkInfo linkInfo;
	private Paint paint;
	// 选中标识的图片对象
	private Bitmap selectImage;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.paint = new Paint();
		// 加载心形图片作为连接线条
		this.paint.setShader(new BitmapShader(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.heart),
				Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
		// 设置连接线条的宽度
		this.paint.setStrokeWidth(9);
		this.selectImage = ImageUtil.getSelectImage(context);
	}

	public void setLinkInfo(LinkInfo linkInfo) {
		this.linkInfo = linkInfo;
	}

	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (this.gameService == null)
			return;
		Piece[][] pieces = gameService.getPieces();
		if (pieces != null) {
			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces[i].length; j++) {
					// 如果二维数组中该元素不为空（即有方块），将这个方块的图片画出来
					if (pieces[i][j] != null) {
						// 得到这个Piece对象
						Piece piece = pieces[i][j];
						// 根据方块左上角X、Y座标绘制方块
						canvas.drawBitmap(piece.getImage().getImage(),
								piece.getBeginX(), piece.getBeginY(), null);
					}
				}
			}
		}
		// 如果当前对象中有linkInfo对象, 即连接信息
		if (this.linkInfo != null) {
			// 绘制连接线
			drawLine(this.linkInfo, canvas);
			this.linkInfo = null;
		}
		// 画选中标识的图片
		if (this.selectedPiece != null) {
			canvas.drawBitmap(this.selectImage, this.selectedPiece.getBeginX(),
					this.selectedPiece.getBeginY(), null);
		}
	}

	private void drawLine(LinkInfo linkInfo, Canvas canvas) {

		List<Point> points = linkInfo.getLinkPoints();

		for (int i = 0; i < points.size() - 1; i++) {
			// 获取当前连接点与下一个连接点
			Point currentPoint = points.get(i);
			Point nextPoint = points.get(i + 1);
			// 绘制连线
			canvas.drawLine(currentPoint.x, currentPoint.y, nextPoint.x,
					nextPoint.y, this.paint);
		}
	}

	// 设置当前选中方块的方法
	public void setSelectedPiece(Piece piece) {
		this.selectedPiece = piece;
	}

	// 开始游戏方法
	public void startGame() {
		this.gameService.start();
		this.postInvalidate();
	}
}
