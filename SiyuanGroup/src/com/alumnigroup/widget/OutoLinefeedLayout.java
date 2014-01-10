package com.alumnigroup.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 实现一种想网页的 浮动效果 1，每一个child 的height 应该相同
 * 
 * @author vector
 * 
 */
public class OutoLinefeedLayout extends ViewGroup {

	/**
	 * 画完全部ChildView 后，宽度到了那里？
	 */
	private int endWidth = 0;
	/**
	 * 一行的高度
	 */
	private int lineHeight;
	/**
	 * ChildView 写到的位置
	 */
	private int drawX = 0;
	private int drawY = 0;

	private int marginLeft = 0;
	private int marginTop = 0;
	private int marginRight = 0;
	private int marginButtom = 0;

	public OutoLinefeedLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 计算child view 位置 and 设定，四个参数共同决定，参数是GroupView 相对他的Activity 的位置和大小
	 * <p>
	 */
	@SuppressLint("DrawAllocation")
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		System.out.println("l = " + l + "t = " + t + "r = " + r + "b = " + b);
		final int count = getChildCount();
		
		/**
		 * 初始化,从（0,0） 开始画
		 */
		drawX = 0;
		drawY = 0;
		/**
		 * 循环为每一个child view 分配位置
		 */
		for (int i = 0; i < count; i++) {
			final View child = this.getChildAt(i);

			/**
			 * 拿到ChildView 的内容大小
			 */
			child.measure(0, 0);
			int widthChild = child.getMeasuredWidth();
			int heightChild = child.getMeasuredHeight();
			System.out.println(widthChild + " 我想要的高和宽 ===" + heightChild);


			/**
			 * 如果现在的位置不能把这个ChildView 放下，就换行
			 */
			if (i>0&&drawX + marginRight+widthChild > r - l) {
				drawX = 0;
				drawY += lineHeight;
			}

			/**
			 * 要画的位置
			 */

			int x = drawX + marginLeft;
			int y = drawY + marginTop;
			int width = x +widthChild;
			int height = y +heightChild;
			lineHeight = heightChild + marginTop;

			/**
			 * 设定位置
			 */
			 child.layout(x,y,width,height);
			 System.out.println("画到的位置：  "+drawX+"   "+drawY);
			System.out.println("位置              " + x + "  " + "  " + y + "   "
					+ width + "   " + height);
			/**
			 * 摆放位置增加
			 */
			endWidth = drawX = width;

		}

		/**
		 * 最后设置高度装满ChildView
		 */
		setWrapContent();
	}

	public void setWrapContent() {
		ViewGroup.LayoutParams params = getLayoutParams();

		if (endWidth == 0) {
			params.height = drawY + marginButtom;
		} else {
			params.height = drawY + lineHeight+marginButtom;
		}

		setLayoutParams(params);
	}

	public void setMargin(int left, int top, int right, int buttom) {
		this.marginButtom = buttom;
		this.marginLeft = left;
		this.marginRight = right;
		this.marginTop = top;
	}

	public void setMargin(int margin) {
		setMargin(margin, margin, margin, margin);
	}

}
