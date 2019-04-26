import java.util.Stack;

public class card {
	public String[] card = new String[52];
	static public Stack stackk = new Stack();

	public card() {
		//ServSentDataClient serv = new ServSentDataClient(this);
		
	}
	public static void main(String[] args) {
		card c = new card();
		c.testCard();
	}
	public void testCard() {
		String[] ranCard = new String[52];
		int j=1;
		int c=1;
		for (int i = 0; i < card.length; i++) {
			card[i] = ""+(j)+"-"+(c);
			ranCard[i] = "";
			System.out.println("card : "+card[i]+" "+i);
			if(c==4) {
				c=0;
				j++;
			}
			c++;
		}
		
		for (int i = 0; i < card.length; i++) {
			boolean check = false;
			int ran = 0;
			ran = (int) (Math.random()*51);
			
			if(i==0) {
				System.out.println("Success");
				ranCard[i] = ""+ran;
				System.out.println(ranCard[i]+" "+i);
				continue;
			}
			for (int k = 0; k < ranCard.length; k++) {
				String xx = ""+ran;
				if(ranCard[k].equals(xx)) {
					check = true;
					break;
				}
			}
			if(check==false) {
				
				ranCard[i] = ""+ran;
				stackk.push(ranCard[i]);
				System.out.println(ranCard[i]+" "+i);
			}else {
				i--;
			}
			for (int k = 0; k < stackk.size(); k++) {
				System.out.println("Data in stack : "+stackk.pop());
			}
		}
	}

}
