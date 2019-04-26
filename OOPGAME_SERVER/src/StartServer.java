
public class StartServer {

	public static void main(String[] args) {
		BroadcastServer b = new BroadcastServer();
		ServSentDataClient s = new ServSentDataClient();
		s.start();
		b.start();
	}
}
