package com.chinook.activities;
//hello git
import java.util.ArrayList;
import java.util.Date;


import com.chinook.app.Route;
import com.chinook.app.RouteStep;
import com.chinook.app.async_core.AsyncConstants;
import com.chinook.app.io.AppIO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MapViewActivity extends Activity implements OnTouchListener{
	private TextView textView;
	private String mode, startID, endID, soe, verbose;
	
	private AppIO appIO;
	private Route r2;
	ArrayList<RouteStep> routePut;
	
	
	//things from pathdrawactivity
	PathView pv;
	
	Bundle bundle;
	ArrayList<Integer> xPoints = new ArrayList<Integer>();
	ArrayList<Integer> yPoints = new ArrayList<Integer>();
	int sWidth, sHeight, floor, sFloor, eFloor;
	AssetManager am;
	Button center;
	
	//PATH CALCULATION VARIABLES
	//Hashtable<String, Node> localHash = MGHWayFinderActivity.masterHash;
	//Dijkstra dijkstra;
	//Node sNode, eNode, bNode;
	String startnID, endnID;
	//ArrayList<Node> fullNodePath;
	ArrayList<RouteStep> walkNodePath = new ArrayList<RouteStep>();	//string correct type?
	boolean multifloor;
	int bNodeIndex;
	int currentNodeFloor;
	int outsideHelper;
	
	//IMAGEVIEW - TOUCH EVENT VARIABLES
	Matrix m;
	Matrix savedM;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int MODE = NONE;
	Point sPoint = new Point();
	Rect imageBounds;
	int currentFloor;
	boolean listTab = false;
	
	TextView tvX, tvY;
	float[] mValues = new float[9];
	
	//ui things from calum
		Button next;
		Button prev;
		Button list;
		Button help;
		Button view;
		int index = 0;
		ArrayList<String> nodeList = new ArrayList<String>();
		TabHost tabs;
		ArrayList<HashMap<String, String>> dirList = new ArrayList<HashMap<String, String>>();
		ImageView icon;
	    FrameLayout mainFrame;
	    RelativeLayout overlayout;
	    ListView lv;
	    ImageView overlay;
        HashMap<String, Drawable> pictures = new HashMap<String, Drawable>();
        Resources res;
        String[] validPics;   
	    boolean fromMap;    	
		
	    //
	    //test shit
	    SensorManager sman;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);	// this is required for indeterminate in title bar
		setContentView(R.layout.map);
        
		textView = (TextView) findViewById(R.id.textView1);
		textView.setText("MAP VIEW - the native custom graphics view will go here\n\n");
		
		startID = AppPrefs.getStartID(this);
		endID = AppPrefs.getEndID(this);
		String bVerbose = AppPrefs.getVerbose(this)? "true":"false";
		verbose = "verbose=" + bVerbose;
		soe = "soe=" + AppPrefs.getStairs(this);
		
		appIO = new AppIO(AsyncConstants.DEFAULT_THREAD_POOL_SIZE);
		r2 = Route.getInstance(this);
		
		if (startID != null && endID != null && startID.length() != 0 && endID.length() != 0) {
    		
    		r2.setup("testRoute2", startID, endID, verbose, soe);
    		
    		//TODO calc the route!
    		appIO.calculateRoute(this, r2, this);
    		 
    	} else {
    		textView.append("error passing data\n");
    	}
		
		//oncreate pieces from pathdrawactivity
	    res = getResources();

			next = (Button)findViewById(R.id.btnNext);
			next.setBackgroundDrawable(res.getDrawable(R.drawable.smallright));
			prev = (Button)findViewById(R.id.btnPrev);
			prev.setBackgroundDrawable(res.getDrawable(R.drawable.smallleft));

	        pv = (PathView)findViewById(R.id.pathView);
	        am = getAssets();
	        
	        
	    	//TODO is this code needed?
	    	//WHEN CALCULATING AN INTERFLOOR PATH, WE NEED TO BREAK IT UP INTO INDIVIDUAL FLOORS
//	            if(sFloor != eFloor){							
	    //
//	            	bNode = dijkstra.getBreakNode();										//SET BNODE TO THE FIRST NODE ON THE SECOND FLOOR OF TRAVEL (WE CAN GET AT IT'S PREDECESSOR VIA .getPreviousNode()
//	            	bNodeIndex = walkNodePath.indexOf(bNode.getPreviousNode());
//	            	
//	            	multifloor = true;
//	            	
//	            	for(int i = 0; i <= bNodeIndex; i++){
//	            		xPoints.add(walkNodePath.get(i).getX());
//	            		yPoints.add(walkNodePath.get(i).getY());
//	            	}
//	            	
//	            } else {
	    //
//	            	multifloor = false;
//	            	
//	            	for(Node it:walkNodePath){
//	            		xPoints.add(it.getX());
//	            		yPoints.add(it.getY());
//	            	}
//	            }
	         
	    		
	    		//TODO set this somehow
	         //SETUP PATHVIEW OBJECT AND DISPLAY
	    		//pv.makePathView(xPoints, yPoints, floor, am, sFloor, eFloor);
	    		currentFloor = sFloor;
	    		pv.setBackgroundColor(Color.WHITE);
	    		pv.setOnTouchListener(this);

	    	//BUTTON LISTENERS
	    		next.setOnClickListener(
	    				new OnClickListener(){
	    					public void onClick(View v){
	    						index++;
	    						
	    						outsideHelper = 1;
	    						step();
	    						
//	    						if(walkNodePath.get(index).getNodeFloor() == 0){
//	    							index = walkNodePath.size() - 3;
//	    						}else step();
//	    						
//	    						if(index < walkNodePath.size() - 3){
//	    							step();
//	    						}
	    						
	    						
//	    						Node stepNode = walkNodePath.get(index);
//	    			        	double dAngle = stepNode.getNNodeAngle();
//	    			        	String cardDir  = "Go to ";
	    						
	    						
	    			        	//cardinal directions
//	    						if(dAngle == 180){
//	    							Log.v("cardinal", "north");
//	    							cardDir = "Go North at ";
//	    						}else if(dAngle == 0){
//	    							Log.v("cardinal", "south");
//	    							cardDir = "Go South at ";
//	    						}else if(dAngle == -90){
//	    							Log.v("cardinal", "east");
//	    							cardDir = "Go East at ";
//	    						}else if(dAngle == 90){
//	    							Log.v("cardinal", "west");
//	    							cardDir = "Go West at ";
//	    						}
	    						//the toast
	    						//Toast.makeText(PathDrawActivity.this, cardDir + walkNodePath.get(index).getNodeDepartment(), Toast.LENGTH_SHORT).show();
	    					}
	    				}
	    		);	
	    		
	    		prev.setOnClickListener(
	    				new OnClickListener(){
	    					public void onClick(View v){
	    						index--;
	    						outsideHelper = 0;
	    						step();
	    						
//	    						Node stepNode = walkNodePath.get(index);
//	    			        	double dAngle = stepNode.getNNodeAngle();
//	    			        	String cardDir  = "Go to ";
	    						
	    						
	    			        	//cardinal directions
//	    						if(dAngle == 180){
//	    							Log.v("cardinal", "north");
//	    							cardDir = "Go North at ";
//	    						}else if(dAngle == 0){
//	    							Log.v("cardinal", "south");
//	    							cardDir = "Go South at ";
//	    						}else if(dAngle == -90){
//	    							Log.v("cardinal", "east");
//	    							cardDir = "Go East at ";
//	    						}else if(dAngle == 90){
//	    							Log.v("cardinal", "west");
//	    							cardDir = "Go West at ";
//	    						}
	    						//the toast
	    						//Toast.makeText(PathDrawActivity.this, cardDir + walkNodePath.get(index).getNodeDepartment(), Toast.LENGTH_SHORT).show();
	    					}
	    				}
	    		);	
	    				
	    		
	    		
	    		//view.getBackground().setColorFilter(0xFF6685D1, PorterDuff.Mode.MULTIPLY);
	    	
	    		Thread c1 = new Thread(centerOnLoad, "onCreate Centering Thread");
	    		c1.start();
		
		
		
		
	}//end oncreate
	

	public void onPause(){
		super.onPause();
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void routeConvertData(Route routeObj) {
		//textView.append("runResult = " + result + "\n");
		textView.append(routeObj.toString() + "\n");
		textView.append(routeObj.getMyMetrics().toString() + "\n");
		
		routePut = routeObj.getRouteStepList();	//get ArrayList<RouteStep>
		
		
//		for(int i = 0; i < routePut.size(); i++){
//			int nodex = routePut.get(i).getStepNode().getX();
//			Log.v("X", Integer.toString(nodex));
//			int nodey = routePut.get(i).getStepNode().getY();
//			Log.v("Y", Integer.toString(nodey));
//			//boolean nod = routePut.get(1).getStepNode().
//		}
		
		drawMap();
		
	}//end routeconvert
	
	
	public void drawMap(){
		
	}
	
	
	
	//HANDLES TOUCH EVENTS - TRANSLATING AND SCALING PATHVIEW OBJECT
		public boolean onTouch(View v, MotionEvent e) {
			PathView view = (PathView) v;
			m = view.matrix;
			savedM = view.savedMatrix;
			
			
			switch(e.getAction() & MotionEvent.ACTION_MASK){
				case MotionEvent.ACTION_DOWN:
					savedM.set(m);
					sPoint.set((int)e.getX(), (int)e.getY());
					MODE = DRAG;
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					MODE = NONE;
					break;
				case MotionEvent.ACTION_MOVE:
					if (MODE == DRAG) {
						m.set(savedM);
						m.postTranslate(e.getX() - sPoint.x, e.getY() - sPoint.y);
						view.invalidate();
					}
					break;
			}
			
			return true;
		}
		
		//TODO this must be updated
		public boolean step(){
			//
			//everything to end is questionable
//			if(floor == 0 && listTab == false){
//				if(currentNodeFloor == 0  && outsideHelper == 1){
//					if(eFloor == 0){
//						index = walkNodePath.size()-1;
//					}else if(walkNodePath.indexOf(bNode) == index){ 
//						index = index;				
//					}else index = walkNodePath.indexOf(bNode)-1;
//				}
//				
//				if(currentNodeFloor == 0  && outsideHelper == 0){
//					if(sFloor == 0){
//						index = 0;
//					}else if(walkNodePath.indexOf(bNode) == index){ 
//						index--;				
//					}else index = walkNodePath.indexOf(bNode)+1;
//				}
//			}
//			
//			//end
//			if(index < 0) {
//				index = 0;
//			} else if(index >= walkNodePath.size()) {
//				index = walkNodePath.size()-1;
//			}
//			
//			
//			
//			
//			currentNodeFloor = walkNodePath.get(index).getNodeFloor();
//			Node cNode = walkNodePath.get(index);
//			int cNodeFloor = cNode.getNodeFloor();
//			
//			if(multifloor){
//				if(index <= bNodeIndex && floor != cNodeFloor){
//					xPoints.clear();
//					yPoints.clear();
//					for(int i = 0; i <= bNodeIndex; i++){
//						xPoints.add(walkNodePath.get(i).getX());
//						yPoints.add(walkNodePath.get(i).getY());
//					}
//					pv.updatePath(xPoints, yPoints, cNodeFloor, sFloor, eFloor);
//					floor = cNodeFloor;
//					pv.setCenterPoint(cNode);			
//				} else if(index > bNodeIndex && floor != cNodeFloor){
//					xPoints.clear();
//					yPoints.clear();
//					for(int i = bNodeIndex+1; i < walkNodePath.size(); i++){
//						xPoints.add(walkNodePath.get(i).getX());
//						yPoints.add(walkNodePath.get(i).getY());
//					}
//					pv.updatePath(xPoints, yPoints, cNodeFloor, sFloor, eFloor);
//					floor = cNodeFloor;
//					pv.setCenterPoint(cNode);
//				} else if(floor == cNodeFloor){
//					pv.setCenterPoint(cNode);	
//				}
//			} else {
//				pv.setCenterPoint(cNode);
//			}
			
			return true;
		}
		
		Runnable centerOnLoad = new Runnable(){
		   	public void run(){
		   		try {
		   				Thread.sleep(2000);
		   				handler.sendEmptyMessage(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		   	}
		   };
			
			protected Handler handler = new Handler() {
				 @Override
				 public void handleMessage(Message msg) {
				     step();
				 }
			};
			
			
			
			
}
