package Apps.Main;

public class Main {

	public static void main(String[] args) {
		ViewMain a = new ViewMain();
		ControllerMain c = new ControllerMain(a);
		a.setVisible(true);
	}
}
