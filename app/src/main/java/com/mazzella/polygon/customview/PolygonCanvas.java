/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.mazzella.polygon.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mazzella.polygon.polygonzone.R;

import java.util.ArrayList;

public class PolygonCanvas extends View {

    Point point1, point2, point3, point4;
    Point point12, point14, point23, point34;
    Point origPoint;
    private int w;
    private int h;
    private ArrayList<TouchPoint> touchPoints;
    private int pointId = 0;
    Paint paint;
    Canvas canvas;
    Context context;

    public PolygonCanvas(Context context) {
        super(context);
        this.context = context;
    }

    public PolygonCanvas(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PolygonCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private void init() {
        setFocusable(true);

        paint = new Paint();
        canvas = new Canvas();

        point1 = new Point();
        point1.x = 50;
        point1.y = 50;

        point2 = new Point();
        point2.x = this.w - 100;
        point2.y = 50;

        point3 = new Point();
        point3.x = this.w - 100;
        point3.y = this.h - 100;

        point4 = new Point();
        point4.x = 50;
        point4.y = this.h - 100;

        point12 = new Point();
        point12.x = point2.x / 2;
        point12.y = point1.y;

        point23 = new Point();
        point23.x = point2.x;
        point23.y = this.h / 2 - point2.y;

        point34 = new Point();
        point34.x = this.w / 2;
        point34.y = point4.y;

        point14 = new Point();
        point14.x = point1.x;
        point14.y = this.h / 2 - point1.y;

        touchPoints = new ArrayList<>();
        touchPoints.add(0, new TouchPoint(context, R.drawable.square_touchpoint, point1, 0));
        touchPoints.add(1, new TouchPoint(context, R.drawable.square_touchpoint, point2, 1));
        touchPoints.add(2, new TouchPoint(context, R.drawable.square_touchpoint, point3, 2));
        touchPoints.add(3, new TouchPoint(context, R.drawable.square_touchpoint, point4, 3));
        touchPoints.add(4, new TouchPoint(context, R.drawable.square_touchpoint, point12, 4));
        touchPoints.add(5, new TouchPoint(context, R.drawable.square_touchpoint, point23, 5));
        touchPoints.add(6, new TouchPoint(context, R.drawable.square_touchpoint, point34, 6));
        touchPoints.add(7, new TouchPoint(context, R.drawable.square_touchpoint, point14, 7));

        areaFill = new Paint();
        areaLine = new Path();
    }

    Paint areaFill;
    Path areaLine;

    @Override
    protected void onDraw(Canvas canvas) {
        areaFill.setColor(Color.parseColor("#55FFFFFF"));
        areaFill.setStyle(Paint.Style.FILL);

        areaLine.reset();
        areaLine.moveTo(touchPoints.get(0).getX() + touchPoints.get(0).getWidthOfTouchPoint()/2, touchPoints.get(0).getY() + touchPoints.get(0).getHeightOfTouchPoint()/2);
        areaLine.lineTo(touchPoints.get(4).getX() + touchPoints.get(0).getWidthOfTouchPoint()/2, touchPoints.get(4).getY() + touchPoints.get(0).getHeightOfTouchPoint()/2);
        areaLine.lineTo(touchPoints.get(1).getX() + touchPoints.get(0).getWidthOfTouchPoint()/2, touchPoints.get(1).getY() + touchPoints.get(0).getHeightOfTouchPoint()/2);
        areaLine.lineTo(touchPoints.get(5).getX() + touchPoints.get(0).getWidthOfTouchPoint()/2, touchPoints.get(5).getY() + touchPoints.get(0).getHeightOfTouchPoint()/2);
        areaLine.lineTo(touchPoints.get(2).getX() + touchPoints.get(0).getWidthOfTouchPoint()/2, touchPoints.get(2).getY() + touchPoints.get(0).getHeightOfTouchPoint()/2);
        areaLine.lineTo(touchPoints.get(6).getX() + touchPoints.get(0).getWidthOfTouchPoint()/2, touchPoints.get(6).getY() + touchPoints.get(0).getHeightOfTouchPoint()/2);
        areaLine.lineTo(touchPoints.get(3).getX() + touchPoints.get(0).getWidthOfTouchPoint()/2, touchPoints.get(3).getY() + touchPoints.get(0).getHeightOfTouchPoint()/2);
        areaLine.lineTo(touchPoints.get(7).getX() + touchPoints.get(0).getWidthOfTouchPoint()/2, touchPoints.get(7).getY() + touchPoints.get(0).getHeightOfTouchPoint()/2);
        areaLine.close();
        canvas.drawPath(areaLine, areaFill);

        for (TouchPoint touchPoint : touchPoints) {
            canvas.drawBitmap(touchPoint.getBitmap(), touchPoint.getX(), touchPoint.getY(), null);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                pointId = -1;
                origPoint = new Point(X, Y);
                for (TouchPoint ball : touchPoints) {
                    int centerX = ball.getX() + ball.getWidthOfTouchPoint();
                    int centerY = ball.getY() + ball.getHeightOfTouchPoint();

                    double radCircle = Math.sqrt((double) (((centerX - X) * (centerX - X)) + (centerY - Y) * (centerY - Y)));

                    if (radCircle < ball.getWidthOfTouchPoint()) {
                        pointId = ball.getId();
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointId > -1) {
                    if (X > 0 && X < this.w -  touchPoints.get(pointId).getWidthOfTouchPoint()) {
                        touchPoints.get(pointId).setX(X);
                    }

                    if (Y > 0 && Y < this.h - touchPoints.get(pointId).getHeightOfTouchPoint()) {
                        touchPoints.get(pointId).setY(Y);
                    }
                } else {
                    if (origPoint != null) {
                        int diffX = X - origPoint.x;
                        int diffY = Y - origPoint.y;
                        origPoint.x = X;
                        origPoint.y = Y;

                        int leftMost = 0;
                        int rightMost = 0;
                        int topMost = 0;
                        int bottomMost = 0;

                        int leftMostId = 0, rightMostId = 0, topMostId = 0, bottomMostId = 0;

                        for (TouchPoint touchPoint : touchPoints) {
                            if (touchPoint.getX() < leftMost) {
                                leftMost = touchPoint.getX();
                                leftMostId = touchPoint.getId();
                            }
                            if (touchPoint.getX() > rightMost) {
                                rightMost = touchPoint.getX();
                                rightMostId = touchPoint.getId();
                            }
                            if (touchPoint.getY() < topMost) {
                                topMost = touchPoint.getY();
                                topMostId = touchPoint.getId();
                            }
                            if (touchPoint.getY() > bottomMost) {
                                bottomMost = touchPoint.getY();
                                bottomMostId = touchPoint.getId();
                            }
                        }

                        if (diffX < 0) {
                            if (touchPoints.get(leftMostId).getX() > 0) {
                                touchPoints.get(0).addX(diffX);
                                touchPoints.get(1).addX(diffX);
                                touchPoints.get(2).addX(diffX);
                                touchPoints.get(3).addX(diffX);
                                touchPoints.get(4).addX(diffX);
                                touchPoints.get(5).addX(diffX);
                                touchPoints.get(6).addX(diffX);
                                touchPoints.get(7).addX(diffX);
                            }
                        } else {
                            if (touchPoints.get(rightMostId).getX() + touchPoints.get(2).getWidthOfTouchPoint() < this.w) {
                                touchPoints.get(0).addX(diffX);
                                touchPoints.get(1).addX(diffX);
                                touchPoints.get(2).addX(diffX);
                                touchPoints.get(3).addX(diffX);
                                touchPoints.get(4).addX(diffX);
                                touchPoints.get(5).addX(diffX);
                                touchPoints.get(6).addX(diffX);
                                touchPoints.get(7).addX(diffX);
                            }
                        }

                        if (diffY < 0) {
                            if (touchPoints.get(topMostId).getY() - 5 > 0) {
                                touchPoints.get(0).addY(diffY);
                                touchPoints.get(1).addY(diffY);
                                touchPoints.get(2).addY(diffY);
                                touchPoints.get(3).addY(diffY);
                                touchPoints.get(4).addY(diffY);
                                touchPoints.get(5).addY(diffY);
                                touchPoints.get(6).addY(diffY);
                                touchPoints.get(7).addY(diffY);
                            }
                        } else {
                            if (touchPoints.get(bottomMostId).getY() + touchPoints.get(2).getHeightOfTouchPoint() < this.h) {
                                touchPoints.get(0).addY(diffY);
                                touchPoints.get(1).addY(diffY);
                                touchPoints.get(2).addY(diffY);
                                touchPoints.get(3).addY(diffY);
                                touchPoints.get(4).addY(diffY);
                                touchPoints.get(5).addY(diffY);
                                touchPoints.get(6).addY(diffY);
                                touchPoints.get(7).addY(diffY);
                            }
                        }
                    }
                }
                break;
        }
        invalidate();
        return true;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        this.w = w;
        this.h = h;

        init();
        super.onSizeChanged(w, h, oldW, oldH);
    }
}
