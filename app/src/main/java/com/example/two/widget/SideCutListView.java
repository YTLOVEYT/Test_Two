package com.example.two.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

import com.example.yintao.utils.CommonUtil;
import com.example.yintao.utils.LogUtil;

/**
 * 自定义ListView ,实现滑动删除
 * Created by YinTao on 2018/1/18.
 */

public class SideCutListView extends ListView
{
    private Context context;
    private int screenWidth;//屏幕的宽度
    private Scroller scroller; //滑动类
    private int touchSlop; //触发移动的事件的最短距离（viewPager）
    private int downX;
    private int downY;//手指按下的点
    private int slidePosition;
    private View itemView; //点击的itemView
    private boolean isSlide = false; //是否在滑动

    private static final int SNAP_VELOCITY = 600;
    private VelocityTracker tracker;
    private Direction direction; //滑动方向
    private int velX;

    public SideCutListView(Context context)
    {
        this(context, null);
    }

    public SideCutListView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);

    }

    public SideCutListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /** 滑动方向枚举值 */
    public enum Direction
    {
        LEFT, RIGHT;
    }

    private void init(Context context)
    {
        this.context = context;
        screenWidth = CommonUtil.getScreenWidth(context); //获取屏幕的宽度
        scroller = new Scroller(context); //滑动的类
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();//触发移动时间的最短距离
        LogUtil.e("touchSlop=" + touchSlop + ",screenWidth=" + screenWidth);
    }

    /** 拦截事件 分发事件 */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:  //按下时
                addVelocityTracker(ev);
                if (!scroller.isFinished()) //如果滑动没有结束，就不拦截事件
                {
                    return super.dispatchTouchEvent(ev);
                }
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                slidePosition = pointToPosition(downX, downY);
                LogUtil.e("slidePosition=" + slidePosition);
                if (slidePosition == AdapterView.INVALID_POSITION)//无效的position位置
                {
                    return super.dispatchTouchEvent(ev);
                }
                LogUtil.e("getFirstVisiblePosition" + getFirstVisiblePosition() + ",getChildCount=" + getChildCount());//10

                itemView = getChildAt(slidePosition - getFirstVisiblePosition());
                break;
            case MotionEvent.ACTION_MOVE:
                //水平方向的距离要大于最小滑动距离，垂直方向要小于最小滑动距离---左右滑动
                if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY
                        || (Math.abs(ev.getX() - downX) > touchSlop && Math.abs(ev.getY() - downY) < touchSlop))
                {
                    isSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker(); //手指抬起时
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /** 回收滑动速率辅助 */
    private void recycleVelocityTracker()
    {
        if (tracker != null)
        {
            tracker.recycle();
            tracker = null;//手动释放内存
        }
    }

    /** 获取滑x方向的滑动速度 》600 */
    private int getScrollVelocity()
    {
        tracker.computeCurrentVelocity(1000);//计算1s内
        return (int) tracker.getXVelocity();
    }

    private void addVelocityTracker(MotionEvent ev)
    {
        if (tracker == null)
        {
            tracker = VelocityTracker.obtain(); //滑动速率辅助类
            LogUtil.e("tracker" + tracker);
        }
        tracker.addMovement(ev);
    }

    /** 处理事件 */
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (isSlide && slidePosition != AdapterView.INVALID_POSITION) //是在滑动  并且点击位置有效
        {
            requestDisallowInterceptTouchEvent(true);//强制拦截
            addVelocityTracker(ev);//添加滑动速率的辅助
            int action = ev.getAction();
            int x = (int) ev.getX();
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    LogUtil.e("速率down" + getScrollVelocity());
                    // FIXME: 2018/1/18
                    MotionEvent motionEvent = MotionEvent.obtain(ev);
                    //移动时要取消listView的其他动作
                    motionEvent.setAction(MotionEvent.ACTION_CANCEL | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    onTouchEvent(motionEvent);

                    int deltaX = downX - x;
                    downX = x;
                    itemView.scrollBy(deltaX, 0);
                    velX = getScrollVelocity();//获取x方法的滑动速率
                    return true;//处理动作
                case MotionEvent.ACTION_UP:
                    LogUtil.e("速率up=" + velX);
                    if (velX > SNAP_VELOCITY)//600
                    {
                        scrollRight();
                    }
                    else if (velX < -SNAP_VELOCITY)
                    {
                        scrollLeft();
                    }
                    else
                    {
                        scrollByDistanceX();
                    }
                    recycleVelocityTracker();
                    isSlide = false;
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll()
    {
        LogUtil.e("scroller.computeScrollOffset()=" + scroller.computeScrollOffset());
        if (scroller.computeScrollOffset())
        {
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
            LogUtil.e("1 isFinished" + scroller.isFinished());
            if (scroller.isFinished())
            {
                LogUtil.e("2");
                if (removeListener != null)
                {
                    LogUtil.e("3");
                    itemView.scrollTo(0, 0);
                    removeListener.removeItem(direction, slidePosition);
                }
            }
        }
    }

    /** scrollByDistanceX */
    private void scrollByDistanceX()
    {
        if (itemView.getScaleX() >= screenWidth / 2)
        {
            scrollLeft();
        }
        else if (itemView.getScaleX() <= -screenWidth / 2)
        {
            scrollRight();
        }
        else
        {
            itemView.scrollTo(0, 0);
        }
    }

    /** 左滑动 */
    private void scrollLeft()
    {
        LogUtil.e("itemView.getScrollX()" + itemView.getScrollX());
        direction = Direction.LEFT;
        int delta = screenWidth - itemView.getScrollX(); //移除屏幕
        scroller.startScroll(itemView.getScrollX(), 0, delta, 0, Math.abs(delta));
        postInvalidate();
    }

    /** 右滑动 */
    private void scrollRight()
    {
        LogUtil.e("itemView.getScrollX()" + itemView.getScrollX());
        direction = Direction.RIGHT;
        int delta = screenWidth + itemView.getScrollX(); //移除屏幕
        scroller.startScroll(itemView.getScrollX(), 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }

    private RemoveListener removeListener;

    /** 自定义监听器 */
    public interface RemoveListener
    {
        /**
         * 删除item
         * @param direction 方向
         * @param position  位置
         */
        void removeItem(Direction direction, int position);
    }

    public void setOnSlideRemoveLister(RemoveListener removeLister)
    {
        this.removeListener = removeLister;
    }

    public void Test()
    {
        Scroller scroller = new Scroller(context);
        View view=new View(context);
        view.scrollTo(0,0);
        view.scrollBy(0,0);

    }

}
