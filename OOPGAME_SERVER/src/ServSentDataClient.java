
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
	FrameServerBroadcast frame;
	public Stack stackk = new Stack();
	int[] ranCard = new int[52];
	public int card[] = new int[52];
	public int dekCard[] = new int[52];
	public int checkCard[] = new int[52];
	public int pointCard[] = new int[52];
	public int pointPlayer[] = new int[4];
	public String imagePlayer[] = new String[3];
	public String name[] = new String[3];
	public String lineBot[] = new String[3];
	public int money[] = new int[4];
	public int checkPlayer =0;


	public ServSentDataClient(FrameServerBroadcast frame) {
		this.frame = frame;
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
						frame.text.append("Length of : " + str.length+"\n");
						
						///////////////// Wait player come to server  /////////////////
						if (str.length == 3) {
							for (int j = 0; j < str.length; j++) {
								if (str[0].equals(BroadcastServer.stack[j])) {
									line = line + "-" + j;
									imagePlayer[j] = str[1];
									name[j] = str[2];
								}
							}
							check++;
							System.out.println("check : " + check);
							frame.text.append("check : " + check+"\n");
							sentData(line);
						}
						
						/////////////////////// when player come all tree //////////////////////////
						if (check == 3) {
							int keep = 3;
							int p=5;
							while(true) {
								try {
								Thread.sleep(800);
								sentData("chat-server->>Game start in-"+p);
									if (p <=0) {
										break;
									}
									p--;
								} catch (Exception e) {}
							}
							
							//////////////////   Draw card from Stack /////////////////////
							for (int i = 0; i < 2; i++) {
								for (int j = 0; j < BroadcastServer.stack.length; j++) {
									int pop = (int) stackk.pop();
									System.out.println("Point card : " + pointCard[pop] + " index " + pop);
									frame.text.append("Point card : " + pointCard[pop] + " index " + pop+"\n");
									pointPlayer[j] = (pointPlayer[j] + pointCard[pop]) % 10;
									line = BroadcastServer.stack[j] + "-" + pop + "-" + j;
									if(j<3) {
										Socket sock = new Socket(BroadcastServer.stack[j], 50113);
										PrintStream dat = new PrintStream(sock.getOutputStream());
										System.out.println(line + " " + BroadcastServer.stack[j]);
										frame.text.append(line + " " + BroadcastServer.stack[j]+"\n");
										dat.println(line);
										dat.close();
									}
									else {
										lineBot[i] = "bot" + "-" + pop + "-" + j;
									}
								}
							}
							
							///////////////// Thread wait to show card bot /////////////////
							p=0;
//							while(true) {
//								try {
//								Thread.sleep(1000);
//									if (p ==3) {
//										break;
//									}
//									p++;
//								} catch (Exception e) {}
//							}
//							for (int i = 0; i < 2; i++) {
//								sentData(lineBot[i]);
//							}

							System.out.println(">>PLayer " + 0 + " : " + pointPlayer[0]);
							System.out.println(">>PLayer " + 1 + " : " + pointPlayer[1]);
							System.out.println(">>PLayer " + 2 + " : " + pointPlayer[2]);
							System.out.println(">>Bot " + 3 + " : " + pointPlayer[3]);
							frame.text.append(">>PLayer " + 0 + " : " + pointPlayer[0]+"\n"
									+">>PLayer " + 1 + " : " + pointPlayer[1]+"\n"
									+">>PLayer " + 2 + " : " + pointPlayer[2]+"\n"
									+">>Bot " + 3 + " : " + pointPlayer[3]+"\n");
							///////////////// Thread wait to show pointPlayer /////////////////
//							p=1;
//							while(true) {
//								try {
//								Thread.sleep(1000);
//									if (p == 5) {
//										break;
//									}
//								p++;
//								} catch (Exception e) {
//									// TODO: handle exception
//								}
//							}
							
							///////////////// Check winer and calculate money player /////////////////
							
								
						}
						
						///////////////// Check to chat  /////////////////
						if (str.length == 4 && str[0].equals("chat")) {
							sentData(line);
						}
						
						///////////////// Check Draw card  /////////////////
						if((str.length==3)&&(str[0].equals("Draw")||str[0].equals("Pass"))) {
							System.out.println("Check draw ....");
							frame.text.append("\n"+"Draw from ip : "+str[1]+"\n");
							int pos = Integer.parseInt(str[2]);
							int pop = (int) stackk.pop();
							if(str[0].equals("Draw")) {
								System.out.println("Point card : " + pointCard[pop] + " index " + pop);
								pointPlayer[pos] = (pointPlayer[pos] + pointCard[pop]) % 10;
								frame.text.append("\n"+"Point last draw : "+pointPlayer[pos]+"\n");
								line = str[1] + "-" + pop + "-" + pos + "-" +"Draw";
								sentData(line);
								checkPlayer++;
							}
							else if (str[0].equals("Pass")) {
								checkPlayer++;
							}
							if(checkPlayer==3) {
								for (int i = 0; i < 3; i++) {
									money[i]=5000;
									if (pointPlayer[3] < pointPlayer[i]) {
										int s = Integer.parseInt(imagePlayer[i]);
										Socket socke = new Socket(BroadcastServer.stack[i], 50113);
										PrintStream data1 = new PrintStream(socke.getOutputStream());
										money[i] = money[i]+500;
										money[3] = money[3]-500;
										data1.println("Win" + "-" + BroadcastServer.stack[i] + "-" + imagePlayer[i]+"-"+i+"-"+"win");
										data1.close();
									} else {
										Socket s = new Socket(BroadcastServer.stack[i], 50113);
										PrintStream d1 = new PrintStream(s.getOutputStream());
										money[i] = money[i]-500;
										money[3] = money[3]+500;
										d1.println("Lost" + "-" + BroadcastServer.stack[i] + "-" + 5 +"-"+i+"-"+"lost");
										d1.close();
									}
									int m  = money[i];
									sentData("money"+"-"+m+"-"+i);
								}
								sentData("chat-192.168.1.16->>Player "+ name[0] + "-" + pointPlayer[0]);
								sentData("chat-192.168.1.16->>Player "+ name[1] + "-" + pointPlayer[1]);
								sentData("chat-192.168.1.16->>Player "+ name[2] + "-" + pointPlayer[2]);
								sentData("chat-192.168.1.16->>Bot "+"-" + pointPlayer[3]);
								check = 0;
							}
							frame.text.append("Check player : " +checkPlayer+"\n");
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

	public void sentData(String word, String string, int i) {
		try {
			Socket socket1 = new Socket(BroadcastServer.stack[0], 50113);
			PrintStream dataOut = new PrintStream(socket1.getOutputStream());
			dataOut.println(word + "-" + string + "-" + i);
			dataOut.close();

			Socket socket2 = new Socket(BroadcastServer.stack[1], 50113);
			PrintStream dataOut2 = new PrintStream(socket2.getOutputStream());
			dataOut2.println(word + "-" + string + "-" + i);
			dataOut2.close();

			Socket socket3 = new Socket(BroadcastServer.stack[2], 50113);
			PrintStream dataOut3 = new PrintStream(socket3.getOutputStream());
			dataOut3.println(word + "-" + string + "-" + i);
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

