package org.ligi.android.dubwise.rc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class RemoteControlView extends View {

	private int rect_size = 0;
	private int circle_size = 0;

	private Paint mPaint = new Paint();

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		rect_size = h - 42;
		circle_size = 30;// Math.min(w, h) / 15 + 1;
	}

	public RemoteControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private float nick = 0.0f, roll = 0.0f, yaw = 0.0f, gas = 0.0f;

	@Override
	protected void onDraw(Canvas canvas) {

		mPaint.setColor(0xCCCCCCCC);
		mPaint.setStyle(Style.FILL);
		canvas.drawRect(
				new Rect(0, this.getHeight()-rect_size, rect_size, this.getHeight()), mPaint);
		canvas.drawRect(
				new Rect(this.getWidth()- rect_size, this.getHeight() - rect_size, this.getWidth()
						, this.getHeight()), mPaint);

		mPaint.setColor(0xFF000000);
		canvas.drawLine(0, this.getHeight(), rect_size, this.getHeight()
				- rect_size, mPaint);
		canvas.drawLine(0, this.getHeight() - rect_size, rect_size,
				this.getHeight(), mPaint);

		canvas.drawLine(this.getWidth(), this.getHeight(), this.getWidth()
				- rect_size, this.getHeight() - rect_size, mPaint);
		canvas.drawLine(this.getWidth(), this.getHeight() - rect_size,
				this.getWidth() - rect_size, this.getHeight(), mPaint);

		// circles for calced position
		mPaint.setColor(Color.RED);
//		mPaint.setColor(0xCCCCCCBB);

		canvas.drawCircle(rect_size / 2 + yaw * rect_size / 2, this.getHeight()
				- rect_size / 2 + gas * rect_size / 2, circle_size, mPaint);
		canvas.drawCircle(this.getWidth() - rect_size / 2 - roll * rect_size
				/ -2, this.getHeight() - rect_size / 2 + nick * rect_size / 2,
				circle_size, mPaint);

		mPaint.setColor(0xFFFFFFFF);

		mPaint.setTextSize(20);
		canvas.drawText("nicka" + circle_size + " - roll" + roll + " gas" + gas
				+ " yaw" + yaw, 0, 15, mPaint);

		invalidate();
	}
}
