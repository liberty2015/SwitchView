package com.example.hu.myapplication.widget;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.hu.myapplication.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 2016/11/21.
 */

public class SwitchView extends View {

    private Paint btnPaint,txtPaint,circlePaint,circlePaintMiddle,circlePaintOuter,circlePaintShadow;
    private int mWidth,mHeight;
    private float sLeft,sRight,sTop,sBottom;
    private float sCenterXOn,sCenterXOff,sCenterY,mCurrentX;
    private float sWidth,sHeight;
    private int circleRadius;
    private RectF sRectF;
    private float roundRadius;
    private float femaleX,femaleY,maleX,maleY;
    private String onTxt,offTxt;
    private List<Bitmap> bitmaps;
//    private ObjectAnimator colorAnim,circleAnim;
    private AnimatorSet setOn,setOff;

    public static final int ON=0;
    public static final int OFF=1;

    private int index=1;
    private Matrix matrix;

    private int onColor,offColor;


    @IntDef({ON,OFF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State{}

    @State int currentState=ON;

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.SwitchView);
        int bitmapId1=array.getResourceId(R.styleable.SwitchView_offImg,R.mipmap.ic_launcher);
        int bitmapId2=array.getResourceId(R.styleable.SwitchView_onImg,R.mipmap.ic_launcher);
        Bitmap bitmap1=((BitmapDrawable)getResources().getDrawable(bitmapId1)).getBitmap();
        Bitmap bitmap2=((BitmapDrawable)getResources().getDrawable(bitmapId2)).getBitmap();
        bitmaps=new ArrayList<>();
        bitmaps.add(bitmap1);
        bitmaps.add(bitmap2);
        onColor=array.getColor(R.styleable.SwitchView_onColor,getResources().getColor(R.color.colorPrimary));
        offColor=array.getColor(R.styleable.SwitchView_offColor,getResources().getColor(R.color.colorAccent));
        onTxt=array.getString(R.styleable.SwitchView_onTxt);
        if (onTxt==null)
            onTxt="";
        if (offTxt==null)
            offTxt="";
        offTxt=array.getString(R.styleable.SwitchView_offTxt);
        array.recycle();
        initPaint();
//        initAnim();
    }

    private void initPaint(){
        btnPaint=new Paint();
        btnPaint.setAntiAlias(true);
        btnPaint.setColor(onColor);
        btnPaint.setStyle(Paint.Style.FILL);
        txtPaint=new Paint();
        txtPaint.setAntiAlias(true);
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        float txtSize=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,15,metrics);
        txtPaint.setTextSize(txtSize);
        txtPaint.setColor(Color.parseColor("#ffffff"));
        roundRadius=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,metrics);
        Log.d("SwitchView","roundRadius="+roundRadius);
        circlePaint=new Paint();
        circlePaint.setAntiAlias(true);
        float strokeWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,metrics);
        circlePaint.setStrokeWidth(strokeWidth);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setColor(getResources().getColor(R.color.circle_color));
        circlePaintShadow=new Paint();
        circlePaintShadow.setAntiAlias(true);
        circlePaintShadow.setStrokeWidth(strokeWidth);
        circlePaintShadow.setStyle(Paint.Style.STROKE);
        float radius= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,metrics);
        BlurMaskFilter filter=new BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER);
        circlePaintShadow.setMaskFilter(filter);
        circlePaintMiddle=new Paint();
        circlePaintMiddle.setStrokeWidth(strokeWidth);
        circlePaintMiddle.setAntiAlias(true);
        circlePaintMiddle.setStyle(Paint.Style.STROKE);
        circlePaintMiddle.setColor(getResources().getColor(R.color.circle_stroke_color));
        circlePaintOuter=new Paint();
        circlePaintOuter.setStrokeWidth(strokeWidth);
        circlePaintOuter.setAntiAlias(true);
        circlePaintOuter.setStyle(Paint.Style.STROKE);
        circlePaintOuter.setColor(Color.BLACK);
    }

    private void initAnim(){
        ObjectAnimator colorAnim=ObjectAnimator.ofInt(this,"paintColor",onColor,offColor);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentColor= (int) valueAnimator.getAnimatedValue();
                btnPaint.setColor(currentColor);
                invalidate();
            }
        });

        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(300);
        ObjectAnimator circleAnim=ObjectAnimator.ofFloat(this,"xxx",sCenterXOn,sCenterXOff);
        circleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentX= (float) valueAnimator.getAnimatedValue();
                float currentScale=-2*valueAnimator.getAnimatedFraction()/2;
                Log.d("xxxxx","currentValue="+valueAnimator.getAnimatedFraction());
                Log.d("xxxxx","currentScale="+currentScale);
                Log.d("xxxxx","index="+index);
                matrix.setScale(currentScale,1,mCurrentX,sCenterY);
                if (valueAnimator.getAnimatedFraction()>=0.5f){
                    index=0;
                }else {
                    index=1;
                }
                invalidate();
            }
        });
        circleAnim.setDuration(300);
        matrix=new Matrix();
        setOn=new AnimatorSet();
        setOn.play(colorAnim).with(circleAnim);
        setOn.setDuration(300);
        ObjectAnimator colorAnimOff=ObjectAnimator.ofInt(this,"paintColor",offColor,onColor);
        colorAnimOff.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentColor= (int) valueAnimator.getAnimatedValue();
                btnPaint.setColor(currentColor);
                invalidate();
            }
        });
        colorAnimOff.setEvaluator(new ArgbEvaluator());
        colorAnimOff.setDuration(300);
        ObjectAnimator circleAnimOff=ObjectAnimator.ofFloat(this,"xxx",sCenterXOff,sCenterXOn);
        circleAnimOff.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentX= (float) valueAnimator.getAnimatedValue();
                float currentScale=2*valueAnimator.getAnimatedFraction()/2;
                Log.d("xxxxx","currentValue="+valueAnimator.getAnimatedFraction());
                Log.d("xxxxx","currentScale="+currentScale);
                Log.d("xxxxx","index="+index);
                matrix.setScale(currentScale,1,mCurrentX,sCenterY);
                if (valueAnimator.getAnimatedFraction()>=0.5f){
                    index=1;
                }else {
                    index=0;
                }
                invalidate();
            }
        });
        circleAnimOff.setDuration(300);
        setOff=new AnimatorSet();
        setOff.play(colorAnimOff).with(circleAnimOff);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize= (int) (0.6*widthSize);
        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCurrentX,sCenterY,circleRadius,circlePaintShadow);
        canvas.drawRoundRect(sRectF,roundRadius,roundRadius,btnPaint);
        canvas.drawText(onTxt,femaleX,femaleY,txtPaint);
        canvas.drawText(offTxt,maleX,maleY,txtPaint);
        canvas.drawCircle(mCurrentX,sCenterY,circleRadius,circlePaint);
        float strokeWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,getResources().getDisplayMetrics());
        canvas.drawCircle(mCurrentX,sCenterY,circleRadius,circlePaintMiddle);
        float radius=circleRadius+strokeWidth;
        canvas.drawCircle(mCurrentX,sCenterY,radius,circlePaintOuter);
        canvas.save();
        canvas.setMatrix(matrix);
        canvas.drawBitmap(bitmaps.get(index),mCurrentX-circleRadius/2,sCenterY-circleRadius/2,null);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if (currentState==ON){
                    setOn.start();
                    currentState=OFF;
                }else {
                    setOff.start();
                    currentState=ON;
                }
                break;
        }
        return true;
    }

    public void setCurrentState(@State int currentState) {
        this.currentState = currentState;
        if (this.currentState==ON){
            index=1;
//            matrix.setScale(1,1);
            btnPaint.setColor(onColor);
//            mCurrentX=sCenterXOn;
        }else {
            index=0;
//            matrix.setScale(-1,1);
            btnPaint.setColor(offColor);
//            mCurrentX=sCenterXOff;
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
        circleRadius= (int) (mHeight*0.38);
        sHeight= (float) (mHeight*0.6);
        sWidth= (float) (mWidth*0.8);
        sTop= (float) (0.25*mHeight);
        sLeft= (float) (0.10*mWidth);
        sBottom= sTop+sHeight;
        sRight= sLeft+sWidth;
        sRectF=new RectF(sLeft,sTop,sRight,sBottom);
        femaleX= (float) (0.19*mWidth);
        femaleY= (float) (0.65*mHeight);
        maleX= (float) (0.54*mWidth);
        maleY=femaleY;
        sCenterY= (float) (0.5*mHeight);
        sCenterXOn= (float) (0.75*mWidth);
        sCenterXOff= (float) (0.56*mWidth)/2;
        initAnim();
        if (this.currentState==ON){
            mCurrentX=sCenterXOn;
            matrix.setScale(1,1,mCurrentX,sCenterY);
        }else {
            mCurrentX=sCenterXOff;
            matrix.setScale(-1,1,mCurrentX,sCenterY);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
