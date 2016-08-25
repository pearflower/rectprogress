package com.muxi.rectprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RectProgressBar extends View {
	private Paint paint;

	private float rectWidth; // ����߳��Ŀ��
	private float progressWidth; // �������Ŀ��
	private int rectColor; // �������ɫ
	private int progressColor; // ����������ɫ
	private int progress; // ����

	private final int PROGRESS_FLAT = 0; // ����ƫС
	private final int PROGRESS_NORMAL = 1; // ��������
	private final int PROGRESS_HIGH = 2; // ����ƫ��

	public float getRectWidth() {
		return rectWidth;
	}

	public void setRectWidth(float rectWidth) {
		this.rectWidth = rectWidth;
	}

	public float getProgressWidth() {
		return progressWidth;
	}

	public void setProgressWidth(float progressWidth) {
		this.progressWidth = progressWidth;
	}

	public int getRectColor() {
		return rectColor;
	}

	public void setRectColor(int rectColor) {
		this.rectColor = rectColor;
	}

	public int getProgressColor() {
		return progressColor;
	}

	public void setProgressColor(int progressColor) {
		this.progressColor = progressColor;
	}

	public synchronized int getProgress() {
		return progress;
	}

	public synchronized void setProgress(int progress) {
		this.progress = progress;
		postInvalidate();
		// invalidate();
	}

	public RectProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray typeArray = context.obtainStyledAttributes(attrs,
				R.styleable.RectProgressBar);
		rectWidth = typeArray.getDimension(
				R.styleable.RectProgressBar_rectWidth, dp2px(5));
		progressWidth = typeArray.getDimension(
				R.styleable.RectProgressBar_progressWidth, dp2px(6));
		rectColor = typeArray.getColor(R.styleable.RectProgressBar_rectColor,
				Color.GRAY);
		progressColor = typeArray.getColor(
				R.styleable.RectProgressBar_progressColor, Color.RED);
		progress = typeArray
				.getInteger(R.styleable.RectProgressBar_progress, 0);
		initPaint();
		typeArray.recycle();
	}

	/**
	 * �������Գ�ʼ������
	 */
	private void initPaint() {
		if (paint == null) {
			paint = new Paint();
		}
		// ����
		paint.setStyle(Style.STROKE);
		// ���û��ʿ��
		paint.setStrokeWidth(rectWidth);
		paint.setDither(true);
		paint.setAntiAlias(true);
		// ������ɫ
		paint.setColor(rectColor);
	}

	public RectProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RectProgressBar(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getMeasureResult(widthMeasureSpec);
		int height = getMeasureResult(heightMeasureSpec);
		// ȡ���ֵ��Ϊ�߳�
		int size = Math.max(width, height);
		setMeasuredDimension(size, size);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initPaint();
		// �߳�
		int length = getMeasuredWidth();
		// �ܳ�
		int perimeter = 4 * length;
		Rect rect = new Rect(0, 0, length, length);
		canvas.drawRect(rect, paint);

		paint.setStrokeWidth(progressWidth);
		paint.setColor(progressColor);
		int progress_validity = checkProgressValidity(progress);
		Log.e("muxi", "progress_validity:" + progress_validity);
		switch (progress_validity) {
		case PROGRESS_FLAT:
			break;
		case PROGRESS_HIGH:
			// ��������
			canvas.drawRect(rect, paint);
			break;
		default:
			// paint.setStrokeWidth(progressWidth);
			// paint.setColor(progressColor);
			// ���ݽ��Ȼ�������
			Log.e("muxi", "progress:" + progress);
			float specific_value = progress / 100f;
			// Log.e("muxi", "��ֵ��"+specific_value);
			float progress_length = specific_value * perimeter;
			// Log.e("muxi", "���������ȣ�"+progress_length);
			// �ڼ����߿�ʼ
			int side_sn = (int) (progress_length / length);
			// �ӵ�ǰ�߿�ʼ���೤
			float side_length = progress_length % (float) length;
			if (side_sn == 0) {
				canvas.drawLine(0, 0, side_length, 0, paint);
			} else if (side_sn == 1) {
				canvas.drawLine(0, 0, length, 0, paint);
				canvas.drawLine(length, 0, length, side_length, paint);
			} else if (side_sn == 2) {
				canvas.drawLine(0, 0, length, 0, paint);
				canvas.drawLine(length, 0, length, length, paint);
				canvas.drawLine(length, length, length - side_length, length,
						paint);
			} else {
				canvas.drawLine(0, 0, length, 0, paint);
				canvas.drawLine(length, 0, length, length, paint);
				canvas.drawLine(length, length, 0, length, paint);
				canvas.drawLine(0, length, 0, length - side_length, paint);
			}
			break;
		}
	}

	private int getMeasureResult(int widthMeasureSpec) {
		int result = 100;
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int size = MeasureSpec.getSize(widthMeasureSpec);
		switch (mode) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = size;
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * �����ȵĺϷ���
	 */
	private int checkProgressValidity(int progress) {
		if (progress <= 0) {
			return PROGRESS_FLAT;
		} else if (progress >= 100) {
			return PROGRESS_HIGH;
		} else {
			return PROGRESS_NORMAL;
		}
	}

	/**
	 * dpתpx
	 */
	private float dp2px(float dp) {
		float scale = getContext().getResources().getDisplayMetrics().density;
		return (dp * scale + 0.5f);
	}

	private int dp2pxInt(float dp) {
		return (int) dp2px(dp);
	}
}
