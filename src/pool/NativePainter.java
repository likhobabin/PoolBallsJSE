package pool;
//
import java.awt.Graphics2D;
//
public abstract  class NativePainter implements Painter{	
	Graphics2D getBrush(){
		return(FBrush);
	}
	//
	
	void setGraphics(Graphics2D __brush){
		FBrush = __brush;
	}
	//
	
	private Graphics2D FBrush;
	//
}
