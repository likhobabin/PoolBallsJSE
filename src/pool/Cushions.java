package pool;

public class Cushions extends Shape {
	
	public Cushions(double __width, double __height){
		setBorder(__width, __height);
		setImpactProcessor(new ElasticImpactOfFixed());
	}
	//
	
	public double getW(){
		return(FWidth);
	}
	//
	
	public double getH(){
		return(FHeight);
	}
	//
	//Interactable interface
	public boolean isCollided(Interactable __interacting_th){
		if(__interacting_th instanceof Ball){
			Ball c_ball = (Ball)__interacting_th;
			//
			if((-1.0)*(FWidth/2)>=c_ball.getMinX() || FWidth/2<=c_ball.getMaxX())
				return(true);
			if((-1.0)*(FHeight/2)>=c_ball.getMinY() || FHeight/2<=c_ball.getMaxY())
				return(true);
			
		}
		return(false);
	}
	//
	
	public void doInteract(Interactable __interacting_th){
		getImpactProcessor().processCollision(__interacting_th);
	}
	//
	
	public void display(){
		
	}
	
	void setBorder(double __width, double __height){
		FWidth = __width;
		FHeight = __height;
	}
	//
	
	class ElasticImpactOfFixed implements Impactable {
		public void processCollision(Interactable __impacting){
			if(__impacting instanceof Ball){
				Ball c_ball = (Ball)__impacting;
				double x_comp = c_ball.vecClone().getXComp();
				double y_comp = c_ball.vecClone().getYComp();
				//
				//reflection related to OX
		        if((-1.0)*(FWidth/2)>=c_ball.getMinX() || FWidth/2<=c_ball.getMaxX() ){
		        	x_comp *=(-1);
		        }
		        //
				//reflection related to OY
		        if((-1.0)*(FHeight/2)>=c_ball.getMinY() || FHeight/2<=c_ball.getMaxY()){
		        	y_comp *= (-1);
		        }
		        //
	        	c_ball.setVector(x_comp, y_comp);
			}
		}
	}
	
	private double FWidth;
	private double FHeight;
}
