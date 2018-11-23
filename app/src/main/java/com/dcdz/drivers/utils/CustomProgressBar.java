package com.dcdz.drivers.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.dcdz.drivers.R;

public class CustomProgressBar extends ProgressBar {

    //绘制进度条的X,Y坐标
    private int marginXY = 0;
    //进度条背景，分隔线的paint
    private Paint progressbarBackgroundPaint, separtatedPaint, textPaint;
    //总数
    private int boxSum = 6;
    //画笔的宽度
    private int strokeWidth = 20;
    //进度条的背景颜色
    private int background;
    //分隔线的颜色
    private int separtatedLineBackground;
    //分隔线的宽度
    private int separtatedLineWidth;
    //层的标记
    public String layerNo;

    //设置视频总数
    public void setBoxSum(int boxSum) {
        this.boxSum = boxSum;
        invalidate();
    }

    //设置层号
    public void setLayerNo(String layerNo){
        this.layerNo = layerNo;
        invalidate();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        boxSum = typedArray.getInt(R.styleable.CustomProgressBar_CP_Video_Sum, 3);
        background = typedArray.getColor(R.styleable.CustomProgressBar_CP_Background, Color.parseColor("#9D9D9D"));//F1F1F1
        separtatedLineBackground = typedArray.getColor(R.styleable.CustomProgressBar_CP_Separated_Line_Background, Color.parseColor("#80ffffff"));
        separtatedLineWidth = typedArray.getDimensionPixelSize(R.styleable.CustomProgressBar_CP_Separated_Line_Width, 4);
        typedArray.recycle();

        progressbarBackgroundPaint = new Paint();
        progressbarBackgroundPaint.setAntiAlias(true);
        progressbarBackgroundPaint.setStrokeCap(Paint.Cap.SQUARE);
        progressbarBackgroundPaint.setStrokeWidth(strokeWidth);
        progressbarBackgroundPaint.setColor(background);

        separtatedPaint = new Paint();
        separtatedPaint.setAntiAlias(true);
        separtatedPaint.setStrokeWidth(strokeWidth);
        separtatedPaint.setColor(separtatedLineBackground);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(3);
        textPaint.setColor(Color.RED);

        setBackgroundColor(Color.parseColor("#ffffff"));
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);

        int finalWidth = size + getPaddingLeft() + getPaddingRight();
        int finalHeight = strokeWidth + getPaddingBottom() + getPaddingTop();

        setMeasuredDimension(finalWidth, finalHeight);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //开始绘制进度条的X，Y坐标
        marginXY = strokeWidth / 2;
        //绘制背景进度
//        canvas.drawLine(marginXY, marginXY, getWidth() - marginXY, marginXY, progressbarBackgroundPaint);
        canvas.drawLine(0, marginXY, getWidth(), marginXY, progressbarBackgroundPaint);

        //绘制文字
        int startX = 0;
        for (int i = 1; i <= boxSum; i++){
            if (i == 1){
                startX = (getWidth() - separtatedLineWidth * boxSum) / boxSum / 2;
            }else {
                startX += (getWidth() - separtatedLineWidth * boxSum) / boxSum + separtatedLineWidth;
            }
            canvas.drawText(layerNo + String.valueOf(i), startX-8, 15, textPaint);
        }

        //分隔线的X坐标(注意，这一步必须放在“开始绘制进度条”之后，否则绘制的进度条会把分隔线覆盖。)
        int separtatedLineX = 0;
        for (int i = 0; i < boxSum - 1; i++) {
            //计算分隔线的X坐标
//            separtatedLineX += (getWidth() - 2 * marginXY) / boxSum;
            separtatedLineX += getWidth() / boxSum;
            //绘制分隔线
//            canvas.drawLine(marginXY + separtatedLineX - (separtatedLineWidth / 2), marginXY, marginXY + separtatedLineX + (separtatedLineWidth/2), marginXY, separtatedPaint);
//            canvas.drawLine(separtatedLineX  , marginXY, separtatedLineX + separtatedLineWidth , marginXY, separtatedPaint);
//            canvas.drawLine(marginXY + separtatedLineX - (separtatedLineWidth / 2), marginXY, marginXY + separtatedLineX, marginXY, separtatedPaint);
            canvas.drawLine(separtatedLineX - (separtatedLineWidth / 2), marginXY, separtatedLineX + (separtatedLineWidth / 2), marginXY, separtatedPaint);
        }
    }
}
