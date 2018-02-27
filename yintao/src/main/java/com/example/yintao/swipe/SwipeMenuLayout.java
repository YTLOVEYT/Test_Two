package com.example.yintao.swipe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.yintao.R;
import com.example.yintao.utils.LogUtil;

/**
 * <p>强大的Item之layout外嵌布局
 * 不支持item的点击</p>
 * Created by YinTao on 2018/1/22.
 */

public class SwipeMenuLayout extends ViewGroup
{

    private static SwipeMenuLayout viewCache; //需要释放
    private int touchSlop;//最小触摸滑动距离
    private int maxVelocity;//最大的加速度
    private boolean isSwipeEnable; //默认支持滑动
    private boolean isIos; //默认
    private boolean isLeftSwipe; //默认

    private int rightMenuWidths; //右侧菜单宽度总和(最大滑动距离)
    private int height;//自己的高度
    private View contentView;//内容布局
    private int limitSwipe;//滑动的临界值
    private VelocityTracker tracker;//滑动速度跟踪类
    private static boolean isTouching = false; //防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空
    private boolean isUserSwiped;
    private boolean isUnMove;
    private boolean iosInterceptFlag;
    private PointF lastPoint=new PointF();; //保存上一次点击的点(float)
    private PointF newPoint=new PointF();; //保存本次点击的点(float)
    private int pointerId;
    private boolean isExpand = false;

    private ValueAnimator expandAnimator, closeAnimator; //展开动画和关闭动画

    public SwipeMenuLayout(Context context)
    {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /** 初始化 */
    private void init(Context context, AttributeSet attrs, int defStyleAttr)
    {
        ViewConfiguration viewConfig = ViewConfiguration.get(context);
        touchSlop = viewConfig.getScaledTouchSlop(); //获取最大触摸滑动的距离
        maxVelocity = viewConfig.getScaledMaximumFlingVelocity();//获取滑动方向最大加速度
        isSwipeEnable = true;
        isIos = true;
        isLeftSwipe = true;
        TypedArray array = null;
        try
        {
            array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0);
            isSwipeEnable = array.getBoolean(R.styleable.SwipeMenuLayout_swipeEnable, true);
            isIos = array.getBoolean(R.styleable.SwipeMenuLayout_ios, true);
            isLeftSwipe = array.getBoolean(R.styleable.SwipeMenuLayout_leftSwipe, true);
        }
        catch (Exception e)
        {
            LogUtil.e("获取资源错误");
            e.printStackTrace();
        }
        finally
        {
            if (array != null)
            {
                array.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setClickable(true);
        rightMenuWidths = 0;//ViewHolder的复用机制，需要每次测量需要复位
        height = 0;
        int contentWidth = 0;//
        int childCount = getChildCount();
        boolean isExactly = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;//高度是否精确
        boolean isNeedMeasureChildHeight = false;
        for (int i = 0; i < childCount; ++i)
        {
            View child = getChildAt(i);
            child.setClickable(true);
            if (child.getVisibility() != GONE)
            {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                // final MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
                LayoutParams marginLayoutParams = child.getLayoutParams();
                height = Math.max(height, child.getMeasuredHeight());
                if (isExactly && marginLayoutParams.height == WindowManager.LayoutParams.MATCH_PARENT)//wrap-content则需要重新测量子View的高度
                {
                    isNeedMeasureChildHeight = true;
                }
                if (i > 0) //第一个item没有margin
                {
                    rightMenuWidths += child.getMeasuredWidth();//水平分布
                }
                else
                {
                    contentView = child;//layout中第一个布局是content布局
                    contentWidth = child.getMeasuredWidth();
                }
            }
        }
        setMeasuredDimension(contentWidth + getPaddingLeft() + getPaddingRight(), height + getPaddingBottom() + getPaddingTop());
        limitSwipe = rightMenuWidths * 4 / 10;
        if (isNeedMeasureChildHeight) //需要测量子view的高度，强制绘制高度
        {
            forceUniformHeight(childCount, widthMeasureSpec);
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p)
    {
        return super.generateLayoutParams(p);
    }

    /** 布局 */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int childCount = getChildCount();//获取子view的个数
        int paddingLeft = getPaddingLeft();//获取左边距
        int paddingRight = getPaddingRight();//获取右边距
        for (int i = 0; i < childCount; ++i)
        {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE)
            {
                if (i == 0)
                {
                    child.layout(paddingLeft, getPaddingTop(), child.getMeasuredWidth() + paddingLeft, child.getMeasuredHeight() + getPaddingTop());
                    paddingLeft += child.getMeasuredWidth();
                }
                else
                {
                    if (isLeftSwipe) //正常左滑
                    {
                        child.layout(paddingLeft, getPaddingTop(), paddingLeft + child.getMeasuredWidth(), child.getMeasuredHeight() + getPaddingTop());
                        paddingLeft += child.getMeasuredWidth();
                    }
                    else //右滑
                    {
                        child.layout(paddingRight - child.getMeasuredWidth(), getPaddingTop(), paddingRight, getPaddingTop() + child.getMeasuredHeight());
                        paddingRight -= child.getMeasuredWidth();
                    }
                }
            }
        }
    }

    /** 分发事件 */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (isSwipeEnable) //允许滑动
        {
            initTracker(ev);//初始化加速度监听
            final VelocityTracker velocityTracker = tracker;
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    isUserSwiped = false; //判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件
                    isUnMove = true;//仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单
                    iosInterceptFlag = false;//每次DOWN时，默认是不拦截的
                    if (isTouching) //默认是false,如果有触摸过，后续动作无效不拦截,放过
                    {
                        return false;
                    }
                    else
                    {
                        isTouching = true;
                    }
                    lastPoint.set(ev.getRawX(), ev.getRawY());//保存绝对坐标
                    newPoint.set(ev.getRawX(), ev.getRawY());
                    if (viewCache != null)
                    {
                        if (viewCache != this)
                        {
                            viewCache.smoothClose();
                            iosInterceptFlag = isIos;
                        }
                        //有一个菜单出来了就不允许父类的一切动作
                        getParent().requestDisallowInterceptTouchEvent(true);//拦截不下发了，
                    }
                    pointerId = ev.getPointerId(0);//求第一个触点的id,计算滑动速率用
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (iosInterceptFlag) //默认true
                    {
                        break;
                    }
                    float gap = lastPoint.x - ev.getRawX();//两点的距离
                    if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) //距离大于10个像素
                    {
                        getParent().requestDisallowInterceptTouchEvent(true);//左右滑动，屏蔽父类的拦截事件
                    }
                    if (Math.abs(gap) > touchSlop) //大于可滑动
                    {
                        isUnMove = false;
                    }
                    scrollBy((int) gap, 0); //滑动layout的view
                    if (isLeftSwipe) //左滑动
                    {
                        if (getScrollX() < 0)
                        {
                            scrollTo(0, 0); //防止溢出
                        }
                        if (getScrollX() > rightMenuWidths)
                        {
                            scrollTo(rightMenuWidths, 0);
                        }
                    }
                    else
                    {
                        if (getScrollX() < -rightMenuWidths) //防止溢出
                        {
                            scrollTo(-rightMenuWidths, 0);
                        }
                        if (getScrollX() > 0)
                        {
                            scrollTo(0, 0);
                        }
                    }
                    lastPoint.set(ev.getRawX(), ev.getRawY());
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (Math.abs(ev.getRawX() - newPoint.x) > touchSlop) //抬起时已经滑动了
                    {
                        isUserSwiped = true;
                    }
                    if (!iosInterceptFlag)//false
                    {
                        velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                        float xVelocity = velocityTracker.getXVelocity(pointerId);
                        if (Math.abs(xVelocity) > 1000)
                        {
                            if (xVelocity < -1000) //加速度
                            {
                                if (isLeftSwipe)
                                {
                                    smoothExpand();
                                }
                                else
                                {
                                    smoothClose();
                                }
                            }
                            else
                            {
                                if (isLeftSwipe)
                                {
                                    smoothClose();
                                }
                                else
                                {
                                    smoothExpand();
                                }
                            }
                        }
                        else
                        {
                            if (Math.abs(getScrollX()) > limitSwipe)
                            {
                                smoothExpand();
                            }
                            else
                            {
                                smoothClose();
                            }
                        }
                    }
                    releaseTracker();
                    isTouching = false;
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /** 拦截事件 */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (isSwipeEnable)
        {
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(ev.getRawX() - newPoint.x) > touchSlop) //如果检测到左右滑动，则由自己消费事件
                    {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (isLeftSwipe) //左滑动
                    {
                        if (getScrollX() > touchSlop)
                        {
                            if (ev.getX() < getWidth() - getScrollX())
                            {
                                if (isUnMove)
                                {
                                    smoothClose();
                                }
                                return true;
                            }
                        }
                    }
                    else  //右滑
                    {
                        if (-getScrollX() > touchSlop)
                        {
                            if (ev.getX() > -getScrollX())
                            {
                                if (isUnMove)
                                {
                                    smoothClose();
                                }
                                return true;
                            }
                        }
                    }
                    if (isUserSwiped)
                    {
                        return true;
                    }
                    break;
            }
            if (iosInterceptFlag)
            {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        if (this == viewCache)
        {
            viewCache.smoothClose();
            viewCache = null;//释放空间
        }
        super.onDetachedFromWindow();
    }

    /** 菜单弹出 ，禁止长安 */
    @Override
    public boolean performLongClick()
    {
        if (Math.abs(getScrollX()) > touchSlop)
        {
            return false;
        }
        return super.performLongClick();
    }

    private void releaseTracker()
    {
        if (tracker != null)
        {
            tracker.recycle();
            tracker = null;
        }
    }

    /** 自动打开 */
    private void smoothExpand()
    {
        viewCache = SwipeMenuLayout.this;
        if (contentView != null)
        {
            contentView.setLongClickable(true);
        }
        cancelAnimator();//先释放动画
        expandAnimator = ValueAnimator.ofInt(getScrollX(), isLeftSwipe ? rightMenuWidths : -rightMenuWidths);
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                scrollTo((Integer) animation.getAnimatedValue(), 0);//过度动画
            }
        });
        expandAnimator.setInterpolator(new OvershootInterpolator()); //渐变
        expandAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                isExpand = true;
            }
        });
        expandAnimator.setDuration(300).start();
    }

    /** 释放动画 */
    private void cancelAnimator()
    {
        if (closeAnimator != null && closeAnimator.isRunning())
        {
            closeAnimator.cancel();
        }
        if (expandAnimator != null && expandAnimator.isRunning())
        {
            expandAnimator.cancel();
        }
    }

    /** 自动关闭滑动菜单 */
    private void smoothClose()
    {
        viewCache = null;//释放
        if (contentView != null)
        {
            contentView.setLongClickable(true);
        }
        cancelAnimator();
        closeAnimator = ValueAnimator.ofInt(getScrollX(), 0);
        closeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        closeAnimator.setInterpolator(new AccelerateInterpolator());
        closeAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                isExpand = false;
            }
        });
        closeAnimator.setDuration(300).start();
    }

    public void quickClose()
    {
        if (this == viewCache)
        {
            cancelAnimator();
            viewCache.scrollTo(0, 0);
            viewCache = null;
        }
    }

    /** 初始化加速度追踪器 */
    private void initTracker(MotionEvent event)
    {
        if (tracker == null)
        {
            tracker = VelocityTracker.obtain();
        }
        tracker.addMovement(event);
    }

    /** 如果子view是wrap-content则需要测量子view的高度 */
    private void forceUniformHeight(int childCount, int widthMeasureSpec)
    {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
        for (int i = 0; i < childCount; ++i)
        {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE)
            {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
                if (marginLayoutParams.height == LayoutParams.MATCH_PARENT)
                {
                    int oldWidth = marginLayoutParams.width;
                    marginLayoutParams.width = child.getMeasuredWidth();
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    marginLayoutParams.width = oldWidth;
                }
            }
        }

    }
}
