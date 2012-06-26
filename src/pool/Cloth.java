package pool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
//
import javax.swing.JPanel;
//
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
//

public class Cloth extends JPanel implements Runnable {

    public Cloth() {
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        OwnMouseListener own_mouse_list = new OwnMouseListener();
        addMouseMotionListener(own_mouse_list);
        addMouseListener(own_mouse_list);
        setFocusable(true);
        addKeyListener(new KeyAdapter(){
        	
        	public void keyPressed(KeyEvent __e){
        		if(KeyEvent.VK_SPACE == __e.getKeyCode() && KeyEvent.VK_SPACE == FPrevKey){
        			bRepaint = true;
        			FPrevKey=KeyEvent.VK_UNDEFINED;
        		}
        		else
        			if(KeyEvent.VK_SPACE == __e.getKeyCode()){
                		FPrevKey=__e.getKeyCode();
        				bRepaint = false;
        			}
        	}
        	
        	private int FPrevKey=KeyEvent.VK_UNDEFINED;
        });
    }
    //

    public void addNotify() {
        super.addNotify();
        FAnimator = new Thread(this);
        FAnimator.start();
    }
    //

    public void paint(Graphics __gr) {
        super.paint(__gr);
        //
        getClothSize();
        //
        Graphics2D g2d = (Graphics2D)__gr;
        AffineTransform at = new AffineTransform(1.0, 0.0, 0.0, -1.0, FCushions.getW()/2, FCushions.getH()/2);
        //
        g2d.setTransform(at);
        g2d.setColor(Color.WHITE);
        FBallPainter.setGraphics(g2d);
        //
        Line2D axis_line = new Line2D.Double(-FCushions.getW()/2, 0, FCushions.getW()/2, 0);
        g2d.draw(axis_line);
        axis_line.setLine(0, FCushions.getH()/2, 0, -FCushions.getH()/2);
        g2d.draw(axis_line);
        //
        if(bAddBall){
            addNewBall();
            bAddBall = false;
        }
        else
        	 if(bShowClaimer){
        		 g2d.draw(FClaimer);
        	 }
        //
        for(AgileShape c_moveable : FMoveableList){
        	c_moveable.display();
        }
        for(Shape c_barrier : FFixedList){
        	c_barrier.display();
        }        
        Toolkit.getDefaultToolkit().sync();
        __gr.dispose();
    }
    //

    public void cycle() {
        //        
        for(AgileShape c_moveable : FMoveableList){
        	c_moveable.step();
        }
        //
        for(int i=1; FMoveableList.size()>i; i++){
            AgileShape keyMoveThing = FMoveableList.get(i);
            int j=i-1;
            //
            while(0<=j){
            	keyMoveThing.interacting(FMoveableList.get(j));
                --j;
            }
        }
        //  
        for(int i=0; FFixedList.size()>i; i++){
        	int j=0;
        	//
        	while(FMoveableList.size()>j){
        		AgileShape c_th = FMoveableList.get(j);
        		FFixedList.get(i).interacting(c_th);
        		++j;
        	}
        }
        //
      	}
    //

    public void run() {
        long beforeTime, timeDiff, sleep;
        //
        beforeTime = System.currentTimeMillis();
        getClothSize();
        while (true) {

            if(isFocusable() && bRepaint){
                cycle();
            	repaint();
            }
            else
            	if(!isFocusable()){
            		repaint();
                    cycle();
            	}

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = Table.REFRESH_RATE - timeDiff;

            if (sleep < 0)
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }
    }
	//

    private void getClothSize(){
    	double w = getSize().getWidth();
    	double h = getSize().getHeight();
    	if(null == FCushions){
    		FCushions = new Cushions(w, h);
        	FFixedList.add(FCushions);
    	}
    	else
    		FCushions.setBorder(w, h);
    }
    //

    private boolean addNewBall(){
    	double radius = Vector.Polar.getRadius(new Vector(FStartX, FStartY, FEndX, FEndY));
        Ball new_ball = new Ball(FStartX, FStartY, FEndX, FEndY, radius, new Random().nextInt(50));
        boolean bAdd = true;
        //
		if (0 != FMoveableList.size()){
			for (AgileShape c_ball : FMoveableList) 
				if (c_ball.isCollided(new_ball)) {
					bAdd = false;
				}
			}
		//
		for (Shape c_barrier : FFixedList) 
			if (c_barrier.isCollided(new_ball)) {
					bAdd = false;
			}
		//
        if(bAdd){
        	new_ball.setPainter(FBallPainter);
        	FMoveableList.add(new_ball);
            FStartX=-1;
            FStartY=-1;
            FEndX=-1;
            FEndY=-1;
        }
        //
        return(bAdd);
    }
    //
        
    class OwnMouseListener extends MouseAdapter
    {
    	  public void mousePressed(MouseEvent __ev){
              FStartX = __ev.getX();
              FStartY = __ev.getY();
              pressedTransform();
          }
          //
          
          public void mouseDragged(MouseEvent __ev){
        	  //
          	  bShowClaimer = true;
          	  FEndX = __ev.getX();
          	  FEndY = __ev.getY();
          	  //
              draggedTransform();
              if(FStartX != FEndX && FStartY != FEndY){
            	  ellipseTransfComponents();
              }
              repaint();
          }
          //

          public void mouseReleased(MouseEvent __ev){
              //
              super.mouseReleased(__ev);
              if(bShowClaimer && FStartX != FEndX && FStartY != FEndY){
                  bAddBall = true;
                  bShowClaimer = false;
              }
              //
          }
          //
          
          private void pressedTransform( ){
          	  //
	          getClothSize();
	          //
	          double offsetStartX = FStartX - FCushions.getW()/2;
	          double offsetSartY = FCushions.getH()/2 - FStartY;
	          //
	          FStartX = offsetStartX;
	          FStartY = offsetSartY;
	          //	          
          }
          //
          
          private void draggedTransform( ){
          	  //
	          getClothSize();
	          //
	          double offsetEndX = FEndX - FCushions.getW()/2;
	          double offsetEndY = FCushions.getH()/2 - FEndY;
	          //
	          FEndX = offsetEndX;
	          FEndY = offsetEndY;
	          //
          }
          //
          
          private void ellipseTransfComponents( ){
        	  double bottomLeftX=0;
        	  double bottomLeftY=0;
        	  double w_h = 2*Vector.Polar.getRadius(new Vector(FStartX, FStartY, FEndX, FEndY));
        	  //
        	  bottomLeftX = FStartX - w_h/2;
        	  bottomLeftY = FStartY - w_h/2;
        	  //
        	  FClaimer.setFrame(bottomLeftX, bottomLeftY, w_h, w_h);
          }
          //
    }
    //

    private Cushions FCushions;
    private BallPainter FBallPainter = new BallPainter();;
    private List<Shape > FFixedList = new ArrayList<Shape >();
    private List<AgileShape > FMoveableList = new ArrayList<AgileShape >();
    //
    private Ellipse2D FClaimer = new Ellipse2D.Double();
    private boolean bAddBall;
    private boolean bShowClaimer;
    //
    private Thread FAnimator;
    //
    private double FStartX=-1;
    private double FStartY=-1;
    private double FEndX=-1;
    private double FEndY=-1;
    //
    private boolean bRepaint=true;    
}
