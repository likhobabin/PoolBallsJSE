package pool;

import java.awt.geom.Point2D;

class TransferMatrix2D {	
	TransferMatrix2D(Vector __normal, Vector __tangent)  throws IllegalArgumentException {
		FTransfXYtoNT[0] = new double[2];
		FTransfXYtoNT[1] = new double[2];
		setTransfer( __normal, __tangent);
	}
	//
	
	void setTransfer(Vector __normal, Vector __tangent) throws IllegalArgumentException {
		calcXYtoNTTransf(__normal, __tangent);
		calcNTtoXYTransf();
	}
	//
	
	Vector vectorTransfer(Vector __right){
		double n_comp = FTransfXYtoNT[0][0] * __right.getXComp() + FTransfXYtoNT[0][1] * __right.getYComp();
		double t_comp = FTransfXYtoNT[1][0] * __right.getXComp() + FTransfXYtoNT[1][1] * __right.getYComp();
		//
		return(new Vector(n_comp, t_comp));
		
	}
	//
	
	Vector vectorTransferBack(Vector __right){
		//
		double x_comp = FTransfNTtoXY[0][0] * __right.getXComp() + FTransfNTtoXY[0][1] * __right.getYComp();
		double y_comp = FTransfNTtoXY[1][0] * __right.getXComp() + FTransfNTtoXY[1][1] * __right.getYComp();
		//
		return(new Vector(x_comp, y_comp));
		
	}
	//
	
	private void calcInverseMatrix(double[][] __matrix){
		double det = __matrix[0][0]*__matrix[1][1] - __matrix[1][0]*__matrix[0][1];
		if(0.0 == det)
			throw new IllegalArgumentException();
		else {
			// calculate transpose union matrix
			double[][] union_mx = new double[2][];
			union_mx[0] = new double[2];
			union_mx[1] = new double[2];
			//
			union_mx[0][0] = __matrix[1][1];
			union_mx[1][1] = __matrix[0][0];

			union_mx[0][1] = (-1) * __matrix[0][1];
			union_mx[1][0] = (-1) * __matrix[1][0];
			//
			__matrix[0][0] = union_mx[0][0] / det;
			__matrix[0][1] = union_mx[0][1] / det;
			__matrix[1][0] = union_mx[1][0] / det;
			__matrix[1][1] = union_mx[1][1] / det;
		}
		//
	}
	//
	
	private void calcNTtoXYTransf() {
		if (FTransfNTtoXY == null) {
			FTransfNTtoXY = new double[2][];
			FTransfNTtoXY[0] = new double[2];
			FTransfNTtoXY[1] = new double[2];
		}
		//
		FTransfNTtoXY[0][0] = FTransfXYtoNT[0][0];
		FTransfNTtoXY[1][1] = FTransfXYtoNT[1][1];
		FTransfNTtoXY[1][0] = FTransfXYtoNT[0][1];
		FTransfNTtoXY[0][1] = FTransfXYtoNT[1][0];
	}
	//
	
	private void calcXYtoNTTransf(Vector __normal, Vector __tangent) {
		if (0.0 == __normal.dotProduct(__tangent)) {
			FTransfXYtoNT[0][0] = __normal.getXComp();
			FTransfXYtoNT[0][1] = __normal.getYComp();
			FTransfXYtoNT[1][0] = __tangent.getXComp();
			FTransfXYtoNT[1][1] = __tangent.getYComp();
		} else {
			FTransfXYtoNT[0][0] = __normal.getXComp();
			FTransfXYtoNT[1][0] = __normal.getYComp();
			FTransfXYtoNT[0][1] = __tangent.getXComp();
			FTransfXYtoNT[1][1] = __tangent.getYComp();
			calcInverseMatrix(FTransfXYtoNT);
		}
	}
	//
	
	private double[][] FTransfNTtoXY = null;
	private double[][] FTransfXYtoNT = new double[2][];
	//
}

class Vector {
	static Vector vectorsSumm(Vector __left, Vector __right){
		return(new Vector(__left.getXComp()+__right.getXComp(), __left.getYComp()+__right.getYComp()));
	}
	//
	
	static Vector unitVec(double __xStart, double __yStart, double __xEnd, double __yEnd){
		double x_comp = __xEnd - __xStart;
		double y_comp = __yEnd - __yStart;
		double radius = Math.sqrt(x_comp*x_comp+y_comp*y_comp);
		//
		return(new Vector(x_comp/radius, y_comp/radius));		
	}
	//
	
	Vector(double __xStart, double __yStart, double __xEnd, double __yEnd){
		double x = __xEnd - __xStart;
		double y = __yEnd - __yStart;
		setComponent(x, y);
	}
	//
		
	Vector(double __xComp, double __yComp){		
		setComponent(__xComp, __yComp);
	}
	//
	
	void setComponent(double __xComp, double __yComp){
		vec.setLocation(__xComp, __yComp);
	}
	//
	
	double getXComp(){
		return(vec.getX());
	}
	//
	
	double getYComp(){
		return(vec.getY());
	}
	//
	
	Vector unitVec(){
		double radius = Polar.getRadius(this);
		//
		return(new Vector(getXComp()/radius, getYComp()/radius));
	}
	//
	
	double dotProduct(Vector __vect){
		return(getXComp()*__vect.getXComp() + getYComp()*__vect.getYComp());
	}

	void scalarMultiple(double __scalar){
		double nxComp = __scalar*getXComp();
		double nyComp = __scalar*getYComp();
		//
		vec.setLocation(nxComp, nyComp);
	}
	//
	
	static class Polar {
		
		static double getAngle(Vector vec){
			if(0 == vec.getXComp() && 0 == vec.getYComp())
				return(0.0);
			//
			double angle = Math.atan2(vec.getYComp(), vec.getXComp());
			if(0>angle){
				angle += 2*Math.PI;
			}
			//
			return(angle);
		}
		
		static double getRadius(Vector vec){
			double x_comp = vec.getXComp();
			double y_comp = vec.getYComp();
			double radius = Math.sqrt(x_comp*x_comp+y_comp*y_comp);
			//
			return(radius);			
		}
		//
		
		static double getAngleDegree(Vector vec){
			return((180*getAngle(vec))/Math.PI);
		}
	}
	//
	
	private Point2D vec = new Point2D.Double();	
	//
}
