package skills.dungeoneering;

public class RoomReference {

	private int x, y;

	public RoomReference(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "[RoomReference][" + x + "][" + y + "]";
	}
}
