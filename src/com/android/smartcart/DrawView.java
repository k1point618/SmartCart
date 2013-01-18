package com.android.smartcart;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
    private static final String TAG = "DrawView";

    Paint paint = new Paint();
    Signature sig = new Signature();
    
    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
    	ArrayList<Line> lines = sig.stroke;
    	for (Line l : lines) {
            Point prev = null;
    		for(Point p: l.points){
    			if(prev != null){
    				canvas.drawLine(prev.x, prev.y, p.x, p.y, paint);
    			}
    			else{
    				canvas.drawCircle(p.x, p.y, 1, paint);
    			}
    			prev = p;
    		}
    		
    	}
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
    	float downx = 0;
    	float downy = 0;
		float upx = 0;
		float upy = 0;
    	int action = event.getAction();
            switch (action) {
            case MotionEvent.ACTION_DOWN:
            	Log.d(TAG, "Action Down");
              	downx = event.getX();
              	downy = event.getY();
              	Line line = new Line();
              	line.points.add(new Point(downx, downy));
              	sig.stroke.add(line);
              	break;
            case MotionEvent.ACTION_MOVE:
            	Log.d(TAG, "Action move");
            	upx = event.getX();
            	upy = event.getY();
            	line = sig.getLast();
            	line.points.add(new Point(upx, upy));
            	break;
            case MotionEvent.ACTION_UP:
            	Log.d(TAG, "Action up");
            	upx = event.getX();
            	upy = event.getY();
            	line = sig.getLast();
            	line.points.add(new Point(upx, upy));
            	break;
            
            default:
              break;
            }
            invalidate();
            return true;      
    }
}

class Signature{
	ArrayList<Line> stroke= new ArrayList<Line>();
	
	public Line getLast(){
		return stroke.get(stroke.size()-1);
	}
}

class Line {
	ArrayList<Point> points = new ArrayList<Point>();
}

class Point {
    float x, y;
    public Point(float a, float b){
    	x = a;
    	y = b;
    }
    @Override
    public String toString() {
        return x + ", " + y;
    }
}
