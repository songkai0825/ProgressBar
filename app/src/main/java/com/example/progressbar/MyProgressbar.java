package com.example.progressbar;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class MyProgressbar extends View {

    private Paint mPaint;//画笔

    private float mPaintWidth = 6f;//初始画笔宽度

    private int mProgressbarWidth;//控件外边框宽度

    private int mProgressbarHeight;//控件外边框高度

    private float mProgressToFrameWidth;//内部填充进度距内边框距离

    private int mPercent = 0;//已转化为0至100范围的当前进度，随动画时间改变而改变

    public MyProgressbar(Context context) {
        super(context);
    }

    @SuppressLint("Recycle")
    public MyProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MyProgressbar);
        mProgressbarWidth = (int)array.getDimension(R.styleable.MyProgressbar_progressbar_width, 100);
        mProgressbarHeight = (int)array.getDimension(R.styleable.MyProgressbar_progressbar_height, 10);
        mProgressToFrameWidth = array.getDimension(R.styleable.MyProgressbar_progressbar_ProgressToFrameWidth, 6f);
    }

    public MyProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mProgressbarWidth, mProgressbarHeight);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        //绘制进度条外边框
        initPaint();
        RectF frameRectF = new RectF(mPaintWidth / 2, mPaintWidth / 2, mProgressbarWidth - mPaintWidth / 2, mProgressbarHeight - mPaintWidth / 2);
        canvas.drawRoundRect(frameRectF, 15, 15, mPaint);
        //填充内部进度
        mPaint.setPathEffect(null);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        //内部进度填充长度，随动画时间改变而改变
        float percent = (float) mPercent / 100f;
        RectF progressRectF = new RectF(mPaintWidth + mProgressToFrameWidth, mPaintWidth + mProgressToFrameWidth, (mPaintWidth + mProgressToFrameWidth) + percent * (mProgressbarWidth - 2 * mPaintWidth - 2 * mProgressToFrameWidth), mProgressbarHeight - mPaintWidth - mProgressToFrameWidth);
        canvas.drawRoundRect(progressRectF, 15, 15, mPaint);
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mPaintWidth);
    }

    public void setProgress(int progress, int maxProgress) {

        int percent = progress * 100 / maxProgress;//得出当前progress占最大进度值百分比（0-100）
        if (percent < 0) {
            percent = 0;
        }
        if (percent > 100) {
            percent = 100;
        }

        ValueAnimator animator = ValueAnimator.ofInt(0, percent);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPercent = (int)valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

}
