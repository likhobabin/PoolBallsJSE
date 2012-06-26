package pool;

import java.awt.geom.Point2D;

import pool.Vector.Polar;
//

public class Ball extends AgileShape {	
	static final double DENSITY = 0.77f;
	//
	
	public Ball(double __startX, double __startY, double __endX, double __endY,
				 double __radius, double __velocityMod) {
		//
		FCenterX = __startX;
		FCenterY = __startY;
		FRadius = __radius;
		Vector velocity_vec = Vector.unitVec(__startX, __startY, __endX, __endY);
		velocity_vec.scalarMultiple(__velocityMod);
		setVector(velocity_vec);
		//
		setMover(new LinearMotion());
		setImpactProcessor(new ElasticImpactOfMoveable());
	}
	//
	
	Vector vecClone(){
		return(new Vector(FVector.getXComp(), FVector.getYComp()));
	}
	//
	
	//abstract class Moveable implementation
	public void step(){
		//abstract class Moveable method
		doStep();
	}
	//
	
	//Interactable interface impl.
	public boolean isCollided(Interactable __interacting_th){
		if(__interacting_th instanceof Ball){
			Ball ball = (Ball)__interacting_th;
			//calculating of balls distance
			double radius_summ = getRadius()+ball.getRadius();
			Vector dist_vec = new Vector(getCenter().getX() - ball.getCenter().getX(),
									   	 getCenter().getY() - ball.getCenter().getY());
			double distance = Vector.Polar.getRadius(dist_vec);
			//
			if(distance<=radius_summ){
				return(true);
			}
		}
		return(false);
	}
	//
	
	public void doInteract(Interactable __interacting_th){
		if(getImpactProcessor() instanceof ElasticImpactOfMoveable){
			ElasticImpactOfMoveable nat_coll_processor = (ElasticImpactOfMoveable)getImpactProcessor();
			nat_coll_processor.processCollision(__interacting_th);
		}
	}
	//
	
	public void display( ){
		getPainter().display(this);
	}
	
	public void setComponents(double __startX, double __startY, double __endX, double __endY){
        FCenterX = __startX;
        FCenterY = __startY;
        //
        FVector = new Vector(__startX, __startY, __endX, __endY);		
	}
	//
	
	Point2D getCenter(){
		return(new Point2D.Double(FCenterX, FCenterY));
	}
	//
	
	Point2D getStart(){
		double xComp = FVector.getXComp();
		double yComp = FVector.getYComp();
		double xStart = FCenterX + xComp;
		double yStart = FCenterY + yComp;
		//
		return(new Point2D.Double(xStart, yStart));				
	}
	//
	
	double getRadius(){
		return(FRadius);
	}
	//
	
	double getMaxX(){
		return(FCenterX + getRadius());
	}
	//
	
	double getMinX(){
		return(FCenterX - getRadius());
	}
	//
	
	double getMaxY(){
		return(FCenterY + getRadius());
	}
	//
	
	double getMinY(){
		return(FCenterY - getRadius());
	}
	//
	
	double getMass(){
		double volume = (4/3)*Math.PI*Math.pow(getRadius(), 3);
		//
		return(volume*DENSITY);
	}
	//
	
	public void setVector(Vector __newValue){
		if(null == FVector){
			FVector = new Vector(__newValue.getXComp(), __newValue.getYComp());
		}
		else{
			FVector.setComponent(__newValue.getXComp(), __newValue.getYComp());
		}
	}
	//
	
	public void setVector(double __x_comp, double __y_comp){
		if(null == FVector){
			FVector = new Vector(__x_comp, __y_comp);
		}
		else{
			FVector.setComponent(__x_comp, __y_comp);
		}
	}
	//
	
	public class LinearMotion implements MoveItable {

		@Override
		public void moveIt() {
			FCenterX += getUnitX();
			FCenterY += getUnitY();
		}
		//
		
		private double getUnitX(){
			double angle = Vector.Polar.getAngle(FVector);
			double unit_v = (Table.REFRESH_RATE*Polar.getRadius(FVector))/Table.ONE_SEC;
			//
			return(unit_v*Math.cos(angle));
		}
		//
		
		private double getUnitY(){
			double angle = Vector.Polar.getAngle(FVector);
			double unit_v = (Table.REFRESH_RATE*Polar.getRadius(FVector))/Table.ONE_SEC;
			//
			return(unit_v*Math.sin(angle));
		}
		//
	}
	//
	
	class ElasticImpactOfMoveable implements Impactable {
		public void processCollision(Interactable __impacting){
			if(__impacting instanceof Ball){
				Ball ball = (Ball)__impacting;
				//calculating a normal and a tangent vector 
				Vector unit_normal = new Vector(getCenter().getX() - ball.getCenter().getX(),
										   		getCenter().getY() - ball.getCenter().getY())
												.unitVec();
				Vector unit_tangent = new Vector((-1)*unit_normal.getYComp(), 
												 unit_normal.getXComp());
				
				//
				TransferMatrix2D transfMatrix = new TransferMatrix2D(unit_normal, unit_tangent);
				Vector nt_before_coll_velocity_1 = transfMatrix.vectorTransfer(vecClone());

		    	System.out.println("1)Before x"+vecClone().getXComp() + " y "+vecClone().getYComp());
		    	System.out.println("2)Before x"+ball.vecClone().getXComp() + " y "+ball.vecClone().getYComp());
				//
				Vector nt_before_coll_velocity_2 = transfMatrix.vectorTransfer(ball.vecClone());
				double bc_nComp_1 = nt_before_coll_velocity_1.getXComp();
				double bc_nComp_2 = nt_before_coll_velocity_2.getXComp();
				//
				double ac_nComp_1 = ( bc_nComp_1*(getMass() - ball.getMass()) + 2*ball.getMass()*bc_nComp_2 ) /
						( getMass() + ball.getMass() );
				//normal and tangent vector of velocity2 after collision
				double ac_nComp_2 = ( bc_nComp_2*(ball.getMass() - getMass()) + 2*getMass()*bc_nComp_1 ) /
							( getMass() + ball.getMass() );				
				double ac_tComp_1 = nt_before_coll_velocity_1.getYComp();
				double ac_tComp_2 = nt_before_coll_velocity_2.getYComp();
				//
				Vector nt_after_coll_velocity_1 = new Vector(ac_nComp_1, ac_tComp_1);
				Vector xy_after_coll_velocity_1 = transfMatrix.vectorTransferBack(nt_after_coll_velocity_1);
				Vector nt_after_coll_velocity_2 = new Vector(ac_nComp_2, ac_tComp_2);
				Vector xy_after_coll_velocity_2 = transfMatrix.vectorTransferBack(nt_after_coll_velocity_2);
				//
		    	System.out.println("1)After x"+xy_after_coll_velocity_1.getXComp() + " y "+xy_after_coll_velocity_1.getYComp());
		    	System.out.println("2)After x"+xy_after_coll_velocity_2.getXComp() + " y "+xy_after_coll_velocity_2.getYComp());
				//
		    	System.out.println();
		    	//
				setVector(xy_after_coll_velocity_1);
				ball.setVector(xy_after_coll_velocity_2);
			}
		}
		//
	}
	
	
	private double FCenterX;
	private double FCenterY;
	private Vector FVector;
	private double FRadius;
	//
}
