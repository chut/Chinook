package com.chinook.activities;
//hello git
import java.util.ArrayList;
import java.util.Date;


import com.chinook.app.Node;
import com.chinook.app.PathView;
import com.chinook.app.Route;
import com.chinook.app.RouteStep;
import com.chinook.app.async_core.AsyncConstants;
import com.chinook.app.io.AppIO;
import com.chinook.app.io.IMapViewCallback;

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
import android.os.SystemClock;
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

public class MapViewActivity extends Activity implements OnTouchListener, IMapViewCallback{
	private TextView textView;
	private String mode, startID, endID, soe, verbose;
	
	private AppIO appIO;
	private Route r2;
	ArrayList<RouteStep> routePut;
	ArrayList<RouteStep> breakNodes = new ArrayList<RouteStep>();
	
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
	RouteStep fBreakNode;
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
		boolean goOn = false;
	    //
	    //test shit
	    SensorManager sman;
	    
	    RouteStep nextFloorNode;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);	// this is required for indeterminate in title bar
		setContentView(R.layout.map);
        
		//textView = (TextView) findViewById(R.id.textView1);
		//textView.setText("MAP VIEW - the native custom graphics view will go here\n\n");
		//textView.setVisibility(8);
		am = getAssets();
		
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
		
		SystemClock.sleep(3000);	//TODO find better solution than sleep
		
//=========================================================================================================
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
	         
	    		
	    		

	    	//BUTTON LISTENERS
	    		next.setOnClickListener(
	    				new OnClickListener(){
	    					public void onClick(View v){
	    						index++;
	    						
	    						outsideHelper = 1;
	    						step();
	    						
	    						
//	    						Node stepNode = walkNodePath.get(index);
//	    			        	double dAngle = stepNode.getNNodeAngle();
//	    			        	String cardDir  = "Go to ";
	    						
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
		//textView.append(routeObj.toString() + "\n");
		//textView.append(routeObj.getMyMetrics().toString() + "\n");
		
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
		int ifloor = 0;
		boolean broken = false;
		
		sFloor = routePut.get(0).getStepNode().getFloorLevel();
		floor = sFloor;
		eFloor = routePut.get(routePut.size()-1).getStepNode().getFloorLevel();
		
		breakNodes.add(routePut.get(0));
		while(ifloor < routePut.size()){
			if(routePut.get(ifloor).getStepNode().getIsConnector()){
				//Log.v("in-if", "found breaknode");
				fBreakNode = routePut.get(ifloor);
				//Log.v("breakNodeContents", breakNodes.toString());
				breakNodes.add(fBreakNode);
				//broken = true;
			}
		ifloor++;
		}
		breakNodes.add(routePut.get(routePut.size()-1));
		
		for(int i = 0; i < breakNodes.size(); i++){
			Log.v("breakNodes", breakNodes.get(i).getStepNode().getNodeID());
		}
		
		//Log.v("fbreaknode", fBreakNode.toString());
		//Log.v("fbreaknode-index", Integer.toString(routePut.indexOf(fBreakNode)+1));
		//
		
		fBreakNode = breakNodes.get(1);
		
		if(fBreakNode != null){
			bNodeIndex = routePut.indexOf(fBreakNode);
			Log.v("in-if", "in multifloor setup");
			nextFloorNode = routePut.get(bNodeIndex + 1);
			multifloor = true;
			
			Log.v("in-if", "in multifloor setup  bnode:"+Integer.toString(bNodeIndex));
		}else bNodeIndex = routePut.size() - 1;	
		

		
		
		//Log.v("bnode", "                     bnode:"+Integer.toString(bNodeIndex));
		for(int i = 0; i <= bNodeIndex; i++){
			xPoints.add(routePut.get(i).getStepNode().getX());
			yPoints.add(routePut.get(i).getStepNode().getY());
			
		}
		
		
		
		
		//routePut.get(1).getStepNode().getIsConnector();
		
		
		//TODO set this somehow
        //SETUP PATHVIEW OBJECT AND DISPLAY
   		pv.makePathView(xPoints, yPoints, floor, am, sFloor, eFloor);
   		currentFloor = sFloor;
   		pv.setBackgroundColor(Color.WHITE);
   		pv.setOnTouchListener(this);
   		
   		
		//goOn = true;
	}
	
	int mapdex = 0;
	
	public void updateMap(RouteStep cNode){
//		if(nextFloorNode.getStepNode().getFloorLevel() != eFloor){
//			//bNodeIndex = routePut.indexOf(routePut.get(routePut.indexOf(fBreakNode)+1).getStepNode());
//			
//		}
		
		xPoints.clear();
		yPoints.clear();
		
		Log.v("cnode", cNode.getStepNode().getNodeID());
		int xyStart;
		int xyEnd;
		
		nextFloorNode = breakNodes.get(breakNodes.indexOf(cNode)+1);
		Log.v("nextFloor", nextFloorNode.getStepNode().getNodeID());
		
		//if(breakNodes.indexOf(cNode) == mapdex){
			RouteStep nextBreak = breakNodes.get(breakNodes.indexOf(cNode)+1);	//end of route
			//nextFloorNode is start of route
			
			for(int i = routePut.indexOf(cNode); i <= routePut.indexOf(nextBreak); i++){
				xPoints.add(routePut.get(i).getStepNode().getX());
				yPoints.add(routePut.get(i).getStepNode().getY());
				
			}
			floor = nextFloorNode.getStepNode().getFloorLevel();
			
			pv.updatePath(xPoints, yPoints, floor, sFloor, eFloor);
			pv.setCenterPoint(cNode.getStepNode());
			
			
		//}
		
		
		
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
			if(index < 0) {
			index = 0;
		} else if(index > routePut.size()) {
			index = routePut.size()-1;
		}
			
			if(outsideHelper == 1){
				boolean foundskip = false;
				while(!foundskip){
					if(!routePut.get(index).getIsNavPoint() && index < routePut.size()-2){
						index++;
					}else foundskip = true;
				}
			}else{
				boolean foundskip = false;
				while(!foundskip){
					if(!routePut.get(index).getIsNavPoint() && index > 1){
						index--;
					}else foundskip = true;
				}
			}
			
			//System.out.println("Hello, world!");
			
			//bNodeIndex = routePut.indexOf(fBreakNode)+1;
			
			currentNodeFloor = routePut.get(index).getStepNode().getFloorLevel();
			RouteStep cNode = routePut.get(index);
			//int cNodeFloor = cNode.getFloorLevel();
			
			Log.v("cnode-step", cNode.getStepNode().getNodeID());
			Log.v("bnodeindex", Integer.toString(breakNodes.indexOf(cNode)));
			
			if(breakNodes.indexOf(cNode) != -1 && floor != cNode.getStepNode().getFloorLevel()){
				updateMap(cNode);
			}else pv.setCenterPoint(cNode.getStepNode());
			
			
			//update map
			
			
			
			
			
			//Log.v("floor", "nextnodefloor:"+Integer.toString(nextFloorNode.getStepNode().getFloorLevel()));
			//Log.v("floor", "breaknodefloor:"+Integer.toString(fBreakNode.getStepNode().getFloorLevel()));
			//Log.v("floor", "         floor:"+Integer.toString(floor));
			
//			if(multifloor){
//				//Log.v("multi", "in the multifloor");
//				if(index >= bNodeIndex && floor != nextFloorNode.getStepNode().getFloorLevel()){
//					Log.v("in-if","in if - 1   index:" + Integer.toString(index));
//					xPoints.clear();
//					yPoints.clear();
//					for(int i = routePut.indexOf(nextFloorNode); i < routePut.size(); i++){
//						xPoints.add(routePut.get(i).getStepNode().getX());
//						yPoints.add(routePut.get(i).getStepNode().getY());
//					}
//					pv.updatePath(xPoints, yPoints, nextFloorNode.getStepNode().getFloorLevel(), sFloor, eFloor);
//					floor = nextFloorNode.getStepNode().getFloorLevel();
//					pv.setCenterPoint(nextFloorNode.getStepNode());			
//				} else if(index <= bNodeIndex && floor == nextFloorNode.getStepNode().getFloorLevel()){
//					Log.v("in-if","in if - 2    index:" + Integer.toString(index));
//					xPoints.clear();
//					yPoints.clear();
//					for(int i = 0; i < bNodeIndex; i++){
//						xPoints.add(routePut.get(i).getStepNode().getX());
//						yPoints.add(routePut.get(i).getStepNode().getY());
//					}
//					pv.updatePath(xPoints, yPoints, fBreakNode.getStepNode().getFloorLevel(), sFloor, eFloor);
//					floor = fBreakNode.getStepNode().getFloorLevel();
//					pv.setCenterPoint(fBreakNode.getStepNode());
//				} else if(floor == currentNodeFloor){
//					Log.v("in-if","in if - 3    index:" + Integer.toString(index));
//					pv.setCenterPoint(cNode);	
//				}
//			} else {
//				Log.v("in-if","in if - else    index:" + Integer.toString(index));
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
