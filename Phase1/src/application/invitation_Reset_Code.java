package application;

import java.util.Random;

public class invitationCode {

	public static void main(String[] args) {
		generateinviteCode();
	}
	
	public static void generateinviteCode() {
		Random rand = new Random();
		int [] random = new int[10];

		
		for (int i = 0; i < 10; i++) {
			random[i] = rand.nextInt(10);
		}
		
		for(int i = 0; i < 10; i++) {
			System.out.print(random[i]);
		}
		System.out.println();
	}
	
	
}
