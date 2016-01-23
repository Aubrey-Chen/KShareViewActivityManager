package com.kot32.kshareviewactivitylibrary.manager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.kot32.kshareviewactivitylibrary.actions.KShareViewActivityAction;

import java.util.HashMap;

/**
 * Created by kot32 on 16/1/21.
 * FirstActivity 中的View 暂不支持 WRAP_CONTENT
 * SecondActivity 中的View 可以
 */
public class KShareViewActivityManager {

    private Activity                         one;
    private Class                            two;
    private Handler                          replaceViewHandler;

    private HashMap<Object, View>            shareViews     = new HashMap<>();

    private HashMap<View, ShareViewInfo>     shareViewPairs = new HashMap<>();

    private KShareViewActivityAction         kShareViewActivityAction;

    private ViewGroup                        secondActivityLayout;

    private long                             duration       = 500;

    {
        replaceViewHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                afterAnimation();
            }
        };

        kShareViewActivityAction = new KShareViewActivityAction() {

            @Override
            public void onAnimatorStart() {

            }

            @Override
            public void onAnimatorEnd() {

            }
        };
    }

    private static KShareViewActivityManager INSTANCE;

    public static KShareViewActivityManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new KShareViewActivityManager();
        }
        return INSTANCE;
    }

    /**
     * 根据两个Activity 的布局中的View Tag 是否相同来判断是否属于一个元素
     *
     * @param one
     * @param two
     * @param targetActivityLayoutResrouceID
     * @param shareViews
     */
    public void startActivity(Activity one, Class two, int targetActivityLayoutResrouceID, View... shareViews) {

        // Reflect oAReflect = Reflect.on(one);
        // Instrumentation instrumentation = oAReflect.get("mInstrumentation");
        // oAReflect.set("mInstrumentation", new AnimationInstrumentation(instrumentation));
        this.shareViews.clear();
        this.shareViewPairs.clear();
        
        this.one = one;
        this.two = two;
        for (View v : shareViews) {
            this.shareViews.put(v.getTag(), v);
        }

        secondActivityLayout = (ViewGroup) LayoutInflater.from(one).inflate(targetActivityLayoutResrouceID, null);

        beforeAnimation();
    }

    private void startIntent() {
        Intent i = new Intent(one, two);
        one.startActivity(i);
    }

    /**
     * 位移前测量各种数据
     */
    private void beforeAnimation() {
        findAllTargetViews(secondActivityLayout);
        final int[] currentIndex = { 0 };

        for (final ShareViewInfo viewInfo : shareViewPairs.values()) {

            ViewGroup baseFrameLayout = (ViewGroup) one.findViewById(Window.ID_ANDROID_CONTENT);
            if (secondActivityLayout.getParent() != null) {
                baseFrameLayout.removeView(secondActivityLayout);
            }
            baseFrameLayout.addView(secondActivityLayout,
                                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                 ViewGroup.LayoutParams.MATCH_PARENT));
            secondActivityLayout.setAlpha(0);

            viewInfo.view.post(new Runnable() {

                @Override
                public void run() {
                    viewInfo.width = viewInfo.view.getWidth();
                    viewInfo.height = viewInfo.view.getHeight();
                    viewInfo.locationOnScreen = new Point(getViewLocationOnScreen(viewInfo.view)[0],
                                                          getViewLocationOnScreen(viewInfo.view)[1]);
                    synchronized (currentIndex) {
                        if (currentIndex[0] == shareViewPairs.values().size() - 1) {
                            startAnimation();
                        }
                        currentIndex[0]++;
                    }

                }
            });

        }

    }

    private void startAnimation() {

        final int[] currentIndex = { 0 };
        
        kShareViewActivityAction.onAnimatorStart();

        for (final View v : shareViewPairs.keySet()) {
            final ShareViewInfo pair = shareViewPairs.get(v);

            float ratioX = pair.width / v.getWidth();
            float ratioY = pair.height / v.getHeight();

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", ratioX);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", ratioY);

            // 放大
            AnimatorSet scaleAnimator = new AnimatorSet();
            scaleAnimator.setDuration(duration / 3);
            scaleAnimator.playTogether(scaleX, scaleY);
            scaleAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ViewGroup baseFrameLayout = (ViewGroup) one.findViewById(Window.ID_ANDROID_CONTENT);
                    baseFrameLayout.removeView(secondActivityLayout);

                    ObjectAnimator translationX = ObjectAnimator.ofFloat(v,
                                                                         "translationX",
                                                                         (pair.locationOnScreen.x - getViewLocationOnScreen(v)[0]));

                    ObjectAnimator translationY = ObjectAnimator.ofFloat(v,
                                                                         "translationY",
                                                                         (pair.locationOnScreen.y - getViewLocationOnScreen(v)[1]));

                    AnimatorSet transAnimator = new AnimatorSet();
                    transAnimator.setDuration(duration / 3 * 2);
                    transAnimator.playTogether(translationX, translationY);
                    transAnimator.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            synchronized (currentIndex) {

                                if (currentIndex[0] == shareViewPairs.keySet().size() - 1) {
                                    kShareViewActivityAction.onAnimatorEnd();
                                    startIntent();
                                    replaceViewHandler.sendEmptyMessageDelayed(1, 500);
                                }
                                currentIndex[0]++;
                            }
                        }
                    });
                    transAnimator.start();
                }
            });
            scaleAnimator.start();

        }
    }

    private void afterAnimation() {
        for (View v : shareViews.values()) {
            v.setTranslationX(0);
            v.setTranslationY(0);
            v.setScaleX(1);
            v.setScaleY(1);
        }

    }

    private void findAllTargetViews(ViewGroup viewGroup) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                if (child instanceof AdapterView) {
                    if (shareViews.containsKey(child.getTag())) {
                        shareViewPairs.put(shareViews.get(child.getTag()), new ShareViewInfo(child, new Point()));
                    }
                }
                findAllTargetViews(((ViewGroup) child));
            } else {
                if (shareViews.containsKey(child.getTag())) {
                    shareViewPairs.put(shareViews.get(child.getTag()), new ShareViewInfo(child, new Point()));
                }
            }
        }
    }

    private int[] getViewLocationOnScreen(View view) {
        int[] location = { 0, 0 };
        view.getLocationOnScreen(location);
        return location;
    }

    private Bitmap captureVisibleBitmap(View view) {
        Bitmap mCopyBmp = null;
        view.setDrawingCacheEnabled(true);
        Bitmap copy = view.getDrawingCache();
        if (null != copy) {
            if (null != mCopyBmp) {
                mCopyBmp.recycle();
            }
            mCopyBmp = Bitmap.createBitmap(copy);
        }

        view.setDrawingCacheEnabled(false);
        return mCopyBmp;
    }

    private int getTitleHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight;

        return statusBarHeight * 2 + titleBarHeight;
    }

    private static class ShareViewInfo {

        public View  view;
        public Point locationOnScreen;
        public float width;
        public float height;

        public ShareViewInfo(View view, Point locationOnScreen){
            this.view = view;
            this.locationOnScreen = locationOnScreen;
        }

    }

    public KShareViewActivityManager withAction(KShareViewActivityAction action) {
        this.kShareViewActivityAction = action;
        return this;
    }

    public KShareViewActivityManager setDuration(long duration) {
        this.duration = duration;
        return this;
    }

}
