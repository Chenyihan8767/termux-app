package com.termux.app.terminal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Collections;

/** Gives the session drawer part of the left edge when Android gesture navigation is active. */
@SuppressLint("RtlHardcoded")
public class TermuxSessionDrawerLayout extends DrawerLayout {

    private static final int MAX_GESTURE_EXCLUSION_HEIGHT_DP = 160;

    public TermuxSessionDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addDrawerListener(new SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                updateGestureExclusion();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                updateGestureExclusion();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateGestureExclusion();
    }

    @Override
    public void setDrawerLockMode(int lockMode, int edgeGravity) {
        super.setDrawerLockMode(lockMode, edgeGravity);
        updateGestureExclusion();
    }

    public void updateGestureExclusion() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return;

        WindowInsets insets = getRootWindowInsets();
        int width = insets == null ? 0 : Math.min(getWidth(), insets.getSystemGestureInsets().left);
        int maxHeight = Math.round(MAX_GESTURE_EXCLUSION_HEIGHT_DP * getResources().getDisplayMetrics().density);
        int height = Math.min(getHeight() / 2, maxHeight);

        if (width > 0 && height > 0 && getDrawerLockMode(Gravity.LEFT) == LOCK_MODE_UNLOCKED
                && !isDrawerOpen(Gravity.LEFT)) {
            setSystemGestureExclusionRects(Collections.singletonList(new Rect(0, 0, width, height)));
        } else {
            setSystemGestureExclusionRects(Collections.emptyList());
        }
    }
}
