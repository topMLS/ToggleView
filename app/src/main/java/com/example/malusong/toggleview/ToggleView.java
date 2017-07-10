package com.example.malusong.toggleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by malusong on 2017/7/10.
 */

public class ToggleView extends View{
    private Bitmap switchBackgroupBitmap; // 背景图片
    private Bitmap slideButtonBitmap; // 滑块图片
    private Paint paint; // 画笔
    private boolean mSwitchState = false; // 开关状态, 默认false
    private boolean isTouchMode = false;//是否滑动
    private float currentX;

    public ToggleView(Context context) {
        super(context);
        init();
    }

    public ToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        String namespace = "http://schemas.android.com/apk/res/com.example.malusong.toggleview";
        int switchBackgroundResource = attrs.getAttributeResourceValue(namespace, "switch_background", -1);
        int slideButtonResource = attrs.getAttributeResourceValue(namespace , "slide_button", -1);
        mSwitchState = attrs.getAttributeBooleanValue(namespace, "switch_state", false);

        setSwitchBackgroundResource(switchBackgroundResource);
        setSlideButtonResource(slideButtonResource);
    }

    public ToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
       paint= new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switchBackgroupBitmap.getWidth(),switchBackgroupBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(switchBackgroupBitmap, 0, 0, paint);
        if (isTouchMode){
            // 根据当前用户触摸到的位置画滑块

            // 让滑块向左移动自身一半大小的位置
            float newLeft = currentX - slideButtonBitmap.getWidth() / 2.0f;

            int maxLeft = switchBackgroupBitmap.getWidth() - slideButtonBitmap.getWidth();

            // 限定滑块范围
            if(newLeft < 0){
                newLeft = 0; // 左边范围
            }else if (newLeft > maxLeft) {
                newLeft = maxLeft; // 右边范围
            }
            canvas.drawBitmap(slideButtonBitmap, newLeft, 0, paint);
        }else{
            if(mSwitchState){
                //开
                canvas.drawBitmap(slideButtonBitmap, switchBackgroupBitmap.getWidth()-slideButtonBitmap.getWidth(), 0, paint);
            }else{
                //关
                canvas.drawBitmap(slideButtonBitmap, 0, 0, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouchMode=true;
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                currentX = event.getX();
                isTouchMode=false;
                float center = switchBackgroupBitmap.getWidth() / 2.0f;

                // 根据当前按下的位置, 和控件中心的位置进行比较.
                boolean state = currentX > center;

                // 如果开关状态变化了, 通知界面. 里边开关状态更新了.
                if(state != mSwitchState && onSwitchStateUpdateListener != null){
                    // 把最新的boolean, 状态传出去了
                    onSwitchStateUpdateListener.onStateUpdate(state);
                }

                mSwitchState = state;
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 设置背景图
     * @param switchBackground
     */
    public void setSwitchBackgroundResource(int switchBackground) {
        switchBackgroupBitmap = BitmapFactory.decodeResource(getResources(), switchBackground);
    }

    /**
     * 设置滑块图片资源
     * @param slideButton
     */
    public void setSlideButtonResource(int slideButton) {
        slideButtonBitmap = BitmapFactory.decodeResource(getResources(), slideButton);
    }

    /**
     * 设置开关状态
     * @param
     */
    public void setSwitchState(boolean mSwitchState) {
        this.mSwitchState = mSwitchState;
    }

    public interface OnSwitchStateUpdateListener{
        // 状态回调, 把当前状态传出去
        void onStateUpdate(boolean state);
    }
    private OnSwitchStateUpdateListener onSwitchStateUpdateListener;
    public void setOnSwitchStateUpdateListener(
            OnSwitchStateUpdateListener onSwitchStateUpdateListener) {
        this.onSwitchStateUpdateListener = onSwitchStateUpdateListener;
    }

}
