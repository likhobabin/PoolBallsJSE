package pool;

public interface Interactable {
	public boolean isCollided(Interactable __interacting_th);
	public void doInteract(Interactable __interacting_th);
}
