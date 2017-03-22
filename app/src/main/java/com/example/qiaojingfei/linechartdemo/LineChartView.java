package com.example.qiaojingfei.linechartdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiaojingfei on 2017/3/21.
 */

public class LineChartView extends View {
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private String[] xCoordText = {"0", "1", "2", "3", "4", "5", "6", "7"};//x坐标轴文字
    private String[] yCoordText = {"0", "10", "20", "30", "40", "50"};//y坐标轴文字
    private Map<String, String> pointMap = new HashMap<>();
    private int paddingBottom;//坐标轴距离底部的距离
    private int paddingLeft;//坐标轴距离左端的距离
    private int paddingTop;//坐标轴距离顶部的高度
    private float mPointRadius = 10;//圆点的半径
    private List<Float> pointXs;//存放点的x坐标
    private List<Float> pointYs;//存放点的y坐标

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = DimenUtils.dpToPx(300, getResources());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = DimenUtils.dpToPx(200, getResources());
        }
        paddingLeft = DimenUtils.dpToPx(10, getResources());
        paddingTop = DimenUtils.dpToPx(10, getResources());
        paddingBottom = DimenUtils.dpToPx(10, getResources());
        for (int i = 0; i < xCoordText.length; i++) {
            pointMap.put(String.valueOf(i), yCoordText[(int) (Math.random() * 5)]);
        }
        Log.e("pointMap", pointMap.size() + "");
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        int textSize = DimenUtils.sp2px(mContext, 16);
        paint.setTextSize(textSize);
        float textLength = paint.measureText(yCoordText[1]);
        float textHeight = paint.getFontMetrics().descent - paint.getFontMetrics().ascent;
        float halfTextLength = textLength / 2;
        Log.e("text==", "textLength=" + textLength + " textHeight=" + textHeight + " length/2=" + halfTextLength);
        float xSpace = (mWidth - paddingLeft) / xCoordText.length;//x轴刻度间隔
        float ySpace = (mHeight - paddingTop - paddingBottom) / yCoordText.length;//y轴刻度间隔
        Log.e("space", "spaceX=" + xSpace + " spaceY=" + ySpace);
        drawAxis(canvas, paint, textLength, textHeight, halfTextLength, xSpace, ySpace);
        drawPointAndLine(canvas, textLength, textHeight, xSpace, ySpace);
    }

    /**
     * 画点并将它们用线连接起来
     * @param canvas
     * @param textLength
     * @param textHeight
     * @param xSpace
     * @param ySpace
     */
    private void drawPointAndLine(Canvas canvas, float textLength, float textHeight, float xSpace, float ySpace) {
        //画点
        Paint pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(Color.BLUE);
        pointXs = new ArrayList<>();
        pointYs = new ArrayList<>();
        for (int i = 0; i < pointMap.size(); i++) {
            float y = Float.valueOf(pointMap.get(String.valueOf(i)));
            float cx = paddingLeft + textLength + xSpace * i;
            float cy = mHeight - paddingBottom - (ySpace * (y / 10)) - textHeight - 5;
            pointXs.add(cx);
            pointYs.add(cy);
            Log.e("point", "pointX=" + cx + " pointY=" + cy);
            canvas.drawCircle(cx, cy, mPointRadius, pointPaint);
        }
        //画线
        for (int i = 0; i < pointMap.size(); i++) {
            if (i > 0) {
                canvas.drawLine(pointXs.get(i - 1), pointYs.get(i - 1), pointXs.get(i), pointYs.get(i), pointPaint);
            }
        }
    }

    /**
     * 画坐标轴
     * @param canvas
     * @param paint
     * @param textLength        文字长度
     * @param textHeight        文字高度
     * @param halfTextLength    文字长度的一半
     * @param xSpace            x轴刻度间隔
     * @param ySpace            y轴刻度间隔
     */
    private void drawAxis(Canvas canvas, Paint paint, float textLength, float textHeight, float halfTextLength, float xSpace, float ySpace) {
        //画Y轴上的刻度值
        for (int i = 0; i < yCoordText.length; i++) {
            if (i == 0) {
                canvas.drawText(yCoordText[i], paddingLeft + halfTextLength, mHeight - paddingBottom - (ySpace * i), paint);
            } else {
                canvas.drawText(yCoordText[i], paddingLeft, mHeight - paddingBottom - (ySpace * i) - textHeight / 2, paint);
            }
        }
        //画X轴上的刻度值
        for (int i = 0; i < xCoordText.length; i++) {
            canvas.drawText(xCoordText[i], paddingLeft + halfTextLength + xSpace * i, mHeight - paddingBottom, paint);
        }
        //画y轴
        float startX = paddingLeft + textLength + 5;
        float startY = mHeight - paddingBottom - textHeight - 5;
        float stopX = paddingLeft + textLength + 5;
        float stopY = paddingTop;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        Path path = new Path();
        path.moveTo(stopX + 15, stopY + 25);
        path.lineTo(stopX, stopY);
        path.lineTo(stopX - 15, stopY + 25);
        canvas.drawPath(path, paint);
        //画x轴
        stopX = paddingLeft + xSpace * (xCoordText.length);
        stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        Path path2 = new Path();
        path2.moveTo(stopX - 25, stopY + 15);
        path2.lineTo(stopX, stopY);
        path2.lineTo(stopX - 25, stopY - 15);
        canvas.drawPath(path2, paint);
    }
}
