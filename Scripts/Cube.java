public class Cube {

	int left1, right1;
	int left2, right2;
	int left3, right3;

	int index;

	public Cube (int l1, int r1, int l2, int r2, int l3, int r3, int n) {

		left1 = l1;
		right1 = r1;
		left2 = l2;
		right2 = r2;
		left3 = l3;
		right3 = r3;
		index = n;
	}

	public Cube() {			//Default constructor

		left1 = 0;
		right1 = 0;
		left2 = 0;
		right2 = 0;
		left3 = 0;
		right3 = 0;
		index = 0;
	}

	public String toString() {

		String s = (index + 1) + ": " + left1 + " " + right1 + " " + left2 + " " + right2 + " " + left3 + " " + right3;

		return s;
	}
}
