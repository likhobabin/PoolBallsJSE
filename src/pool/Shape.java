package pool;

public abstract class Shape implements Interactable {
	abstract  public void display();  
	//
	
	void setPainter(Painter __painter){
		FPainter = __painter;
	}
	//
	
	Painter getPainter(){
		return(FPainter);
	}
	//
	
	public void interacting(Shape __another_shape){
		if(isCollided(__another_shape))
			doInteract(__another_shape);
	}
	//
	
	void setImpactProcessor(Impactable __impact_proc){
		FImpactProcess = __impact_proc;
	}
	//
	
	Impactable getImpactProcessor(){
		return(FImpactProcess);
	}
	//
	
	private Impactable FImpactProcess;
	private Painter FPainter;
	//
}
