package pool;

public abstract class AgileShape extends Shape {
	public abstract void step();
	//
	public void setMover(MoveItable __mover){
		FMotor = __mover;
	}
	//
	
	public MoveItable getMover(){
		return(FMotor);
	}
	
	void doStep() throws NullPointerException {
		if(null == FMotor){
			throw new NullPointerException();
		}
		FMotor.moveIt();
	}
	//
	private MoveItable FMotor; 
	//
}
