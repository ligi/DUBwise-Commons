package org.ligi.android.dubwise.rc;

import org.ligi.dubwise.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CurveEditView extends View  {

	private float[] curve = new float[] { 0.0f,0.25f,0.5f,0.75f,1f };

	private int rect_size = 0;
	private int circle_size = 0;

	private Paint thinnLinePaint = new Paint();
	private Paint thinnLinePaint2 = new Paint();
	private Paint thickLinePaint = new Paint();

	private Drawable curve_bg_drawable;
	private Drawable curve_circle_drawable;
	private Drawable curve_selected_circle_drawable;

	private int grabbed_courve_point=-1;


	public interface OnChangeListener {
		public void notifyChange();
	}
	
	private OnChangeListener ocl=null;
	
	public void notifyChangeListeners() {
		if (ocl!=null)
			ocl.notifyChange();
	}

	public void setOnChangeListener(OnChangeListener _ocl ) {
		ocl=_ocl;
	}
	
	public boolean setCurve(float[] new_curve) {
		if (new_curve.length!=curve.length)
			return false;
		curve=new_curve;
		notifyChangeListeners();
		this.invalidate();
		return true;
	}
	
	public float[] getCurve() {
		return curve;
	}
	
	public float getCurvePoint(int pos) {
		return curve[pos];
	}
	
	private Rect getCourvePointRect(int point,int area_mult) {
		Rect res= new Rect(0,0,circle_size*area_mult,circle_size*area_mult);
		res.offset(point*(rect_size/(curve.length-1))-(circle_size/2)*area_mult, (int)(rect_size*(1.0f-curve[point]))-(circle_size/2)*area_mult);
		return res;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		rect_size = Math.min(w, h);
		thinnLinePaint.setStyle(Paint.Style.STROKE);
		thinnLinePaint.setPathEffect( new DashPathEffect(new float[] { 5,15 }, 
				0.0f) ); 
		thinnLinePaint2.setStrokeWidth(2f);
		circle_size = 90;// Math.min(w, h) / 15 + 1;
		thickLinePaint.setStrokeWidth(rect_size/42+1);
		thickLinePaint.setColor(Color.BLACK);
		
		curve_bg_drawable.setBounds(new Rect(0,0,rect_size,rect_size));
		
		
	}

	public CurveEditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		curve_bg_drawable=context.getResources().getDrawable(R.drawable.curve_bg);
		curve_circle_drawable=context.getResources().getDrawable(R.drawable.curve_circle);
		curve_selected_circle_drawable=context.getResources().getDrawable(R.drawable.curve_circle_selected);
		this.setFocusable(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		curve_bg_drawable.draw(canvas); 
		
		int grid_elements=8;
		
		for (int i=1;i<grid_elements;i++) {
			canvas.drawLine(0, i*rect_size/grid_elements, rect_size,i*rect_size/grid_elements, 
					((i==grid_elements/2)?thinnLinePaint2:thinnLinePaint));
			canvas.drawLine( i*rect_size/grid_elements,0, i*rect_size/grid_elements,rect_size, 
					((i==grid_elements/2)?thinnLinePaint2:thinnLinePaint));
		}
		
		// for each point in the curve
		for (int i=0;i<curve.length;i++) {
			
			if (i<(curve.length-1))
				canvas.drawLine(i*rect_size / (curve.length-1) , (1.0f-curve[i])*rect_size,
						(i+1)*rect_size / (curve.length-1) , (1.0f-curve[i+1])*rect_size,
						thickLinePaint);
			
			if (i==grabbed_courve_point) {
				curve_selected_circle_drawable.setBounds(getCourvePointRect(i,1));
				curve_selected_circle_drawable.draw(canvas);
			}
			else {
				curve_circle_drawable.setBounds(getCourvePointRect(i,1));
				curve_circle_drawable.draw(canvas);
			}
		}
		
	}
	
	@Override 
	public boolean onTouchEvent(MotionEvent event) {
		
		if (event.getAction()==MotionEvent.ACTION_UP) 
			grabbed_courve_point=-1;
		
		for (int i=0;i<curve.length;i++)
			if (getCourvePointRect(i,1).contains((int)event.getX(),(int)event.getY()))
				grabbed_courve_point=i;
		
		float val=1.0f-(event.getY()/rect_size);
		if (val<0.0f)
			val=0.0f;
		if (val>1.0f)
			val=1.0f;
		
		if (grabbed_courve_point!=-1)
			curve[grabbed_courve_point]=val;
		
		notifyChangeListeners();
		invalidate();
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	    int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	    int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
	    int size = Math.min(parentWidth, parentHeight);
	    this.setMeasuredDimension(size,size);
	}
}
