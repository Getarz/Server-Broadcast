
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Stack;

public class ServSentDataClient extends Thread {
	public Stack stackk = new Stack();
	int[] ranCard = new int[52];
	public int card[] = new int[52];
	public int dekCard[] = new int[52];
	public int checkCard[] = new int[52];
	public int pointCard[] = new int[52];
	public int pointPlayer[] = new int[4];
	public String imagePlayer[] = new String[3];
	
	public ServSentDataClient() {
		for (int i = 0; i < card.length; i++) {
			card[i] = i + 1;
		}
		for (int i = 0; i < dekCard.length; i++) {
			int random = (int) (Math.random() * 52);
			if (i == 0) {
				dekCard[i] = random;
				stackk.push(dekCard[i]);
				checkCard[random] = 1;
			} else {
				if (checkCard[random] == 0) {
					dekCard[i] = random;
					stackk.push(dekCard[i]);
					checkCard[random] = 1;
				} else {
					i--;
				}
			}
		}
		for (int i = 0; i < dekCard.length; i++) {
			if (i == 0) {
				System.out.print(dekCard[i] + "\t");
			} else {
				System.out.print(dekCard[i] + "\t");
				if (i % 10 == 0)
					System.out.println();
			}
		}
		int numcard2 = 1;
		for (int i = 0; i < pointCard.length; i++) {
			if (i >= 0 && i <= 9) {
				pointCard[i] = numcard2;
				numcard2++;
			} else if (i >= 10 && i <= 12) {
				pointCard[i] = 10;
				numcard2 = 1;
			} else if (i >= 13 && i <= 22) {
				pointCard[i] = numcard2;
				numcard2++;
			} else if (i >= 23 && i <= 25) {
				pointCard[i] = 10;
				numcard2 = 1;
			} else if (i >= 26 && i <= 35) {
				pointCard[i] = numcard2;
				numcard2++;
			} else if (i >= 36 && i <= 38) {
				pointCard[i] = 10;
				numcard2 = 1;
			} else if (i >= 39 && i <= 48) {
				pointCard[i] = numcard2;
				numcard2++;
			} else if (i >= 49 && i <= 51) {
				pointCard[i] = 10;
				numcard2 = 1;
			}
			System.out.println(pointCard[i] + " " + i + " ");
		}
	}

	public void run() {
		ServerSocket servSoket;
		int check = 0;
		try {
			servSoket = new ServerSocket(50111);
			while (true) {
				try {
					String line = "";
					Socket socket = servSoket.accept();
					InputStream input = socket.getInputStream();
					InputStreamReader inputStream = new InputStreamReader(input);
					BufferedReader bufferIn = new BufferedReader(inputStream);
					System.out.println(socket);

					while ((line = bufferIn.readLine()) != null) {
						String[] str = line.split("-");
						System.out.println("Length of : " + str.length);
						if (str.length == 3) {
							for (int j = 0; j < str.length; j++) {
								if (str[0].equals(BroadcastServer.stack[j])) {
									line = line + "-" + j;
									imagePlayer[j] = str[1];
								}
							}
							check++;
							System.out.println("check : " + check);
							sentData(line);
						}
						if (check == 3) {
							int keep = 3;
							for (int i = 0; i < 2; i++) {
								for (int j = 0; j < BroadcastServer.stack.length - 1; j++) {
									int pop = (int) stackk.pop();
									Socket sock = new Socket(BroadcastServer.stack[j], 50113);
									PrintStream dat = new PrintStream(sock.getOutputStream());
									line = BroadcastServer.stack[j] + "-" + pop + "-" + j;
									System.out.println(line + " " + BroadcastServer.stack[j]);
									System.out.println("Point card : " + pointCard[pop] + " index " + pop);
									pointPlayer[j] = (pointPlayer[j] + pointCard[pop]) % 10;
									pop = (int) stackk.pop();
									pointPlayer[3] = (pointPlayer[3] + pointCard[pop]) % 10;
									dat.println(line);
									dat.close();
								}
							}
							System.out.println("PLayer " + 0 + " : " + pointPlayer[0]);
							System.out.println("PLayer " + 1 + " : " + pointPlayer[1]);
							System.out.println("PLayer " + 2 + " : " + pointPlayer[2]);
							System.out.println("Bot " + 3 + " : " + pointPlayer[3]);
							
							for (int i = 0; i < 3; i++) {
								if (pointPlayer[3] < pointPlayer[i]) {
									int s = Integer.parseInt(imagePlayer[i]);
									sentData("win",BroadcastServer.stack[i],s);
								}
								else {
									sentData("lost",BroadcastServer.stack[i],5);
								}
							}
							System.out.println("Winner : "+pointPlayer[keep]);
							
							check = 0;
						}
						if (str.length == 4 && str[0].equals("chat")) {
							sentData(line);
						}

					}
					bufferIn.close();

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void sentData(String line) {
		try {
			Socket socket1 = new Socket(BroadcastServer.stack[0], 50113);
			PrintStream dataOut = new PrintStream(socket1.getOutputStream());
			dataOut.println(line);
			dataOut.close();

			Socket socket2 = new Socket(BroadcastServer.stack[1], 50113);
			PrintStream dataOut2 = new PrintStream(socket2.getOutputStream());
			dataOut2.println(line);
			dataOut2.close();

			Socket socket3 = new Socket(BroadcastServer.stack[2], 50113);
			PrintStream dataOut3 = new PrintStream(socket3.getOutputStream());
			dataOut3.print(line);
			dataOut3.close();

//			Socket socket4 = new Socket(BroadcastServer.stack[3],50113);
//			PrintStream dataOut4 = new PrintStream(socket4.getOutputStream());
//			dataOut4.print(line);
//			dataOut4.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void sentData(String word, String string,int i) {
		try {
			Socket socket1 = new Socket(BroadcastServer.stack[0], 50113);
			PrintStream dataOut = new PrintStream(socket1.getOutputStream());
			dataOut.println(word+"-"+string+"-"+i);
			dataOut.close();

			Socket socket2 = new Socket(BroadcastServer.stack[1], 50113);
			PrintStream dataOut2 = new PrintStream(socket2.getOutputStream());
			dataOut2.println(word+"-"+string+"-"+i);
			dataOut2.close();

			Socket socket3 = new Socket(BroadcastServer.stack[2], 50113);
			PrintStream dataOut3 = new PrintStream(socket3.getOutputStream());
			dataOut3.println(word+"-"+string+"-"+i);
			dataOut3.close();

//			Socket socket4 = new Socket(BroadcastServer.stack[3],50113);
//			PrintStream dataOut4 = new PrintStream(socket4.getOutputStream());
//			dataOut4.print(line);
//			dataOut4.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
