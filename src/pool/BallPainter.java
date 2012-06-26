package pool;

import java.awt.geom.Ellipse2D;

public class BallPainter extends NativePainter {
	public void display(Shape __thing){
		if(__thing instanceof Ball){
			Ball ball = (Ball)__thing;
			//
			double bottomLeftX = 0;
			double bottomLeftY = 0;
			Ellipse2D out_ellipse = new Ellipse2D.Double();
			//
			bottomLeftX = ball.getCenter().getX() - ball.getRadius();
			bottomLeftY = ball.getCenter().getY() - ball.getRadius();
			//
			out_ellipse.setFrame(bottomLeftX, bottomLeftY, 2 * ball.getRadius(), 2 * ball.getRadius());
			getBrush().draw(out_ellipse);
		}
	}

}
