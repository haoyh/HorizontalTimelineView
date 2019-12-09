package com.haoyh.horizontaltimelineview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.haoyh.horizontaltimelineviewlib.R;

import java.util.List;

/**
 * 横向时间轴
 * 使用须知：在布局文件中需要给定控件高。
 * @author haoyh
 */
public class HorizontalTimelineView extends View {

    private Context mContext;

    private List<TimeLineInfo> mDataList;

    /**
     * 定义画笔
     */
    private Paint mLinePaint;
    private Paint mBitmapPaint;
    private Paint mDatePaint;
    private Paint mNamePain;

    /**
     * view 大小，间距
     */
    private float mViewWidth, mViewHeight;

    /**
     * 用户计算 TextView 的准确 y 坐标
     */
    private Paint.FontMetrics mNameFontMetrics;
    private Paint.FontMetrics mDateFontMetrics;

    /**
     * 定义图标 bitmap
     */
    private Bitmap mBitmapPass;
    private Bitmap mBitmapNoPass;
    private Bitmap statusBitmap;

    /**
     * 留一点安全距离，避免收尾部分字过长时候会被挤出去
     */
    private int mSafeDistance = 5;

    public HorizontalTimelineView(Context context) {
        super(context);
        setWillNotDraw(false);
        this.mContext = context;
        init();
    }

    public HorizontalTimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        this.mContext = context;
        init();
    }

    public HorizontalTimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        this.mContext = context;
        init();
    }

    public void setDataList(List<TimeLineInfo> dataList) {
        mDataList = dataList;
    }

    /**
     * 初始化画笔、创建 bitmap 等
     */
    private void init() {
        // 关闭硬件加速，不关闭情况下虚线效果没有
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#999999"));
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        mBitmapPaint = new Paint();
        mBitmapPaint.setFilterBitmap(true);

        mDatePaint = new Paint();
        mDatePaint.setColor(Color.parseColor("#666666"));
        mDatePaint.setTextSize(dip2px(mContext, 12));
        mDatePaint.setStyle(Paint.Style.FILL);
        mDatePaint.setTextAlign(Paint.Align.CENTER);
        mDatePaint.setAntiAlias(true);

        mNamePain = new Paint();
        mNamePain.setColor(Color.parseColor("#666666"));
        mNamePain.setTextSize(dip2px(mContext, 13));
        mNamePain.setStyle(Paint.Style.FILL);
        mNamePain.setTextAlign(Paint.Align.CENTER);
        mNamePain.setAntiAlias(true);

        mNameFontMetrics = mNamePain.getFontMetrics();
        mDateFontMetrics = mDatePaint.getFontMetrics();

        mBitmapPass = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_pass);
        mBitmapNoPass = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_no_pass);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec) - dip2px(mContext, mSafeDistance * 2);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        // canvas.drawColor(Color.parseColor("#ffffff"));
        // 画虚线
        canvas.drawLine(0, mViewHeight / 2, mViewWidth + dip2px(mContext, mSafeDistance * 2), mViewHeight / 2, mLinePaint);
        if (null != mDataList && !mDataList.isEmpty()) {
            float itemW = mViewWidth / mDataList.size();
            for (int i = 0; i < mDataList.size(); i++) {
                TimeLineInfo entity = mDataList.get(i);
                // 画图标
                statusBitmap = entity.isPass() ? mBitmapPass : mBitmapNoPass;
                // 图标 x，y 坐标
                float bX = itemW * i + itemW / 2 - statusBitmap.getWidth() / 2f + dip2px(mContext, mSafeDistance);
                float bY = mViewHeight / 2 - statusBitmap.getHeight() / 2f;
                canvas.drawBitmap(statusBitmap, bX, bY, mBitmapPaint);

                // 画文字
                // 坐标：x 同图标水平居中 ；y 根据在图标上下位置进行计算
                float dateX = bX + statusBitmap.getWidth() / 2f;
                float dateY;
                // 偶数项时文字在图标上显示
                float nameTextHeight = (mDateFontMetrics.bottom - mDateFontMetrics.top) / 2 + mDateFontMetrics.bottom;
                float dateTextHeight = (mNameFontMetrics.bottom - mDateFontMetrics.top) / 2 + mDateFontMetrics.bottom;

                // 以水平线为基准,先画距离水平线近的 TextView
                if (i % 2 == 0) {
                    dateY = mViewHeight / 2 - dip2px(mContext, 19) + nameTextHeight / 2;
                    canvas.drawText(entity.getInfo(), dateX, dateY, mNamePain);

                    canvas.drawText(entity.getDate(), dateX, dateY - dateTextHeight - dateTextHeight / 2, mDatePaint);
                } else {
                    dateY = mViewHeight / 2 + dip2px(mContext, 19) + dateTextHeight / 2;
                    canvas.drawText(entity.getDate(), dateX, dateY, mDatePaint);

                    canvas.drawText(entity.getInfo(), dateX, dateY + dateTextHeight + nameTextHeight / 2, mNamePain);
                }
            }
        }
        canvas.restore();
    }

    /**
     * 根据手机分辨率从 dp 单位 转成 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
