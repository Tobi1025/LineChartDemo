## LineChartView

### xml文件中：
``` java
<com.example.qiaojingfei.linechartdemo.LineChartView
        android:layout_width="300dp"
        android:layout_height="200dp"
        android:layout_centerInParent="true">
</com.example.qiaojingfei.linechartdemo.LineChartView>```

### java代码中：
1.onMeasure()中获取控件的宽高，并转化成px
``` java
    //获取自定义控件的宽高
    if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
    } else {
        mWidth = DimenUtils.dpToPx(300, getResources());
    }

    if (heightMode == MeasureSpec.EXACTLY) {
        mHeight = heightSize;
    } else {
        mHeight = DimenUtils.dpToPx(200, getResources());
    }``` 
    
2.onDraw()中开始画坐标轴，然后描点连线。
* 坐标轴上的刻度用canvas.drawText()画;
* 坐标轴x和y轴用 canvas.drawLine()画；
* 坐标轴顶端箭头通过path来实现。

3.最后一步画点连线
``` java
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
        }```
