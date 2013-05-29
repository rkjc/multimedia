
public class ZigzagDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		double[][] arrayCb = new double[8][8];
		
		int count = 0;
		for(int y = 0; y < 8; y++){
			for(int x = 0; x < 8; x++){
				arrayCb[x][y] = count++;
			}
		}
		System.out.println("Starting with a block to be converted by zigzag into a sequence");
		printArray(arrayCb);
		
		double[] seq1 = blockToSequence(arrayCb);
		System.out.println("normal zigzag");
		printArray(seq1);
		
		System.out.println("");
		
		double[] seq2 = blockToSequenceInvert(arrayCb);
		System.out.println("inverted zigzag");
		printArray(seq2);

		System.out.println("\n----------------------------------\n");
		System.out.println("Using zigzags to generate an array block\n");
		
		System.out.println("Normal zigzag");
		int[][] zigzag = getZigzagPattern();
		double[][] normZig = new double[8][8];
		for(int i = 0; i < 64; i++){
			normZig[zigzag[i][0]][zigzag[i][1]] = i;
		}
		printArray(normZig);
		
		System.out.println("\nInverted zigzag");
		int[][] zigzagAlt = getZigzagPatternAlt();
		double[][] altZig = new double[8][8];
		for(int i = 0; i < 64; i++){
			altZig[zigzagAlt[i][0]][zigzagAlt[i][1]] = i;
		}
		printArray(altZig);
		
	}
	
	public static void zzTableGenerator(){
		int[][] zigzag = getZigzagPattern();
		for(int i = 0; i < 64; i++){
			System.out.println("zigzag[" + i + "][0] = " + zigzag[i][1] + ";" + "  zigzag[" + i + "][1] = " + zigzag[i][0] + ";");
		}
	}
	
	public static double[] blockToSequence(double[][] input) {
		double[] output = new double[64];
		int[][] zigzag = getZigzagPattern();	
		for(int i = 0; i < 64; i++){
			output[i] = input[zigzag[i][0]][zigzag[i][1]];
		}	
		return output;
	}
	
	public static double[] blockToSequenceInvert(double[][] input) {
		double[] output = new double[64];
		int[][] zigzag = getZigzagPatternAlt();	
		for(int i = 0; i < 64; i++){
			output[i] = input[zigzag[i][0]][zigzag[i][1]];
		}	
		return output;
	}
	
	public static int[][] generateZigzagPatternTable() {
		int[][] zigzag = new int [64][2];
		
		// x value				y value
		zigzag[0][0] = 0;   zigzag[0][1] = 0;
		
		zigzag[1][0] = 1;   zigzag[1][1] = 0;
		zigzag[2][0] = 0;   zigzag[2][1] = 1;
		
		zigzag[3][0] = 0;   zigzag[3][1] = 2;
		zigzag[4][0] = 1;   zigzag[4][1] = 1;
		zigzag[5][0] = 2;   zigzag[5][1] = 0;
		
		zigzag[6][0] = 3;   zigzag[6][1] = 0;
		zigzag[7][0] = 2;   zigzag[7][1] = 1;
		zigzag[8][0] = 1;   zigzag[8][1] = 2;
		zigzag[9][0] = 0;   zigzag[9][1] = 3;
		
		zigzag[10][0] = 0;   zigzag[10][1] = 4;
		zigzag[11][0] = 1;   zigzag[11][1] = 3;
		zigzag[12][0] = 2;   zigzag[12][1] = 2;
		zigzag[13][0] = 3;   zigzag[13][1] = 1;
		zigzag[14][0] = 4;   zigzag[14][1] = 0;
		
		zigzag[15][0] = 5;   zigzag[15][1] = 0;
		zigzag[16][0] = 4;   zigzag[16][1] = 1;
		zigzag[17][0] = 3;   zigzag[17][1] = 2;
		zigzag[18][0] = 2;   zigzag[18][1] = 3;
		zigzag[19][0] = 1;   zigzag[19][1] = 4;
		zigzag[20][0] = 0;   zigzag[20][1] = 5;
		
		zigzag[21][0] = 0;   zigzag[21][1] = 6;
		zigzag[22][0] = 1;   zigzag[22][1] = 5;
		zigzag[23][0] = 2;   zigzag[23][1] = 4;
		zigzag[24][0] = 3;   zigzag[24][1] = 3;
		zigzag[25][0] = 4;   zigzag[25][1] = 2;
		zigzag[26][0] = 5;   zigzag[26][1] = 1;
		zigzag[27][0] = 6;   zigzag[27][1] = 0;
		
		zigzag[28][0] = 7;   zigzag[28][1] = 0;
		zigzag[29][0] = 6;   zigzag[29][1] = 1;
		zigzag[30][0] = 5;   zigzag[30][1] = 2;
		zigzag[31][0] = 4;   zigzag[31][1] = 3;
		zigzag[32][0] = 3;   zigzag[32][1] = 4;
		zigzag[33][0] = 2;   zigzag[33][1] = 5;
		zigzag[34][0] = 1;   zigzag[34][1] = 6;
		zigzag[35][0] = 0;   zigzag[35][1] = 7;
		
		zigzag[36][0] = 1;   zigzag[36][1] = 7;
		zigzag[37][0] = 2;   zigzag[37][1] = 6;
		zigzag[38][0] = 3;   zigzag[38][1] = 5;
		zigzag[39][0] = 4;   zigzag[39][1] = 4;
		zigzag[40][0] = 5;   zigzag[40][1] = 3;
		zigzag[41][0] = 6;   zigzag[41][1] = 2;
		zigzag[42][0] = 7;   zigzag[42][1] = 1;
		
		zigzag[43][0] = 7;   zigzag[43][1] = 2;
		zigzag[44][0] = 6;   zigzag[44][1] = 3;
		zigzag[45][0] = 5;   zigzag[45][1] = 4;
		zigzag[46][0] = 4;   zigzag[46][1] = 5;
		zigzag[47][0] = 3;   zigzag[47][1] = 6;
		zigzag[48][0] = 2;   zigzag[48][1] = 7;
		
		zigzag[49][0] = 3;   zigzag[49][1] = 7;
		zigzag[50][0] = 4;   zigzag[50][1] = 6;
		zigzag[51][0] = 5;   zigzag[51][1] = 5;
		zigzag[52][0] = 6;   zigzag[52][1] = 4;
		zigzag[53][0] = 7;   zigzag[53][1] = 3;
		
		zigzag[54][0] = 7;   zigzag[54][1] = 4;
		zigzag[55][0] = 6;   zigzag[55][1] = 5;
		zigzag[56][0] = 5;   zigzag[56][1] = 6;
		zigzag[57][0] = 4;   zigzag[57][1] = 7;
		
		zigzag[58][0] = 5;   zigzag[58][1] = 7;	
		zigzag[59][0] = 6;   zigzag[59][1] = 6;
		zigzag[60][0] = 7;   zigzag[60][1] = 5;
		
		zigzag[61][0] = 7;   zigzag[61][1] = 6;
		zigzag[62][0] = 6;   zigzag[62][1] = 7;
		
		zigzag[63][0] = 7;   zigzag[63][1] = 7;
		
		return zigzag;
	}

	public static int[][] getZigzagPattern() {
		int[][] zigzag = new  int[64][2]; //L = 0; R = 1
		zigzag[0][0] = 0;
		zigzag[0][1] = 0;
		zigzag[1][0] = 1;
		zigzag[1][1] = 0;
		
		int L = 1;
		int R = 0;
		int index = 1;	
		
		while(true){
			while(L != 0){
				index++;
				L--;
				R++;
				zigzag[index][0] = L;
				zigzag[index][1] = R;
			}
			if(L == 0 && R == 7)
				break;
			index++;
			R++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
	
			while(R != 0 ){
				index++;
				L++;
				R--;
				zigzag[index][0] = L;
				zigzag[index][1] = R;
			}
			
			index++;
			L++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
	
		}
			
			index++;
			L++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
			
		while(true){
			while(L != 7){
				index++;
				L++;
				R--;
				zigzag[index][0] = L;
				zigzag[index][1] = R;
			}
			index++;
			R++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
			
			while(R != 7){
				index++;
				L--;
				R++;
				zigzag[index][0] = L;
				zigzag[index][1] = R;
			}
			index++;
			L++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
			
			if(L == 7 && R == 7)
				break;
		}
		return zigzag;
	}
	
	
	public static int[][] getZigzagPatternAlt() {
		int[][] zigzag = new  int[64][2]; 
		
		zigzag[0][0] = 0;  zigzag[0][1] = 0;
		
		zigzag[1][0] = 0;  zigzag[1][1] = 1;
		zigzag[2][0] = 1;  zigzag[2][1] = 0;
		
		zigzag[3][0] = 2;  zigzag[3][1] = 0;		
		zigzag[4][0] = 1;  zigzag[4][1] = 1;
		zigzag[5][0] = 0;  zigzag[5][1] = 2;
		
		zigzag[6][0] = 0;  zigzag[6][1] = 3;	
		zigzag[7][0] = 1;  zigzag[7][1] = 2;
		zigzag[8][0] = 2;  zigzag[8][1] = 1;
		zigzag[9][0] = 3;  zigzag[9][1] = 0;
		
		zigzag[10][0] = 4;  zigzag[10][1] = 0;		
		zigzag[11][0] = 3;  zigzag[11][1] = 1;
		zigzag[12][0] = 2;  zigzag[12][1] = 2;
		zigzag[13][0] = 1;  zigzag[13][1] = 3;
		zigzag[14][0] = 0;  zigzag[14][1] = 4;
		
		zigzag[15][0] = 0;  zigzag[15][1] = 5;		
		zigzag[16][0] = 1;  zigzag[16][1] = 4;
		zigzag[17][0] = 2;  zigzag[17][1] = 3;
		zigzag[18][0] = 3;  zigzag[18][1] = 2;
		zigzag[19][0] = 4;  zigzag[19][1] = 1;
		zigzag[20][0] = 5;  zigzag[20][1] = 0;
		
		zigzag[21][0] = 6;  zigzag[21][1] = 0;		
		zigzag[22][0] = 5;  zigzag[22][1] = 1;
		zigzag[23][0] = 4;  zigzag[23][1] = 2;
		zigzag[24][0] = 3;  zigzag[24][1] = 3;
		zigzag[25][0] = 2;  zigzag[25][1] = 4;
		zigzag[26][0] = 1;  zigzag[26][1] = 5;
		zigzag[27][0] = 0;  zigzag[27][1] = 6;
		
		zigzag[28][0] = 0;  zigzag[28][1] = 7;		
		zigzag[29][0] = 1;  zigzag[29][1] = 6;
		zigzag[30][0] = 2;  zigzag[30][1] = 5;
		zigzag[31][0] = 3;  zigzag[31][1] = 4;
		zigzag[32][0] = 4;  zigzag[32][1] = 3;
		zigzag[33][0] = 5;  zigzag[33][1] = 2;
		zigzag[34][0] = 6;  zigzag[34][1] = 1;
		zigzag[35][0] = 7;  zigzag[35][1] = 0;
		
		zigzag[36][0] = 7;  zigzag[36][1] = 1;
		zigzag[37][0] = 6;  zigzag[37][1] = 2;
		zigzag[38][0] = 5;  zigzag[38][1] = 3;
		zigzag[39][0] = 4;  zigzag[39][1] = 4;
		zigzag[40][0] = 3;  zigzag[40][1] = 5;
		zigzag[41][0] = 2;  zigzag[41][1] = 6;
		zigzag[42][0] = 1;  zigzag[42][1] = 7;
		
		zigzag[43][0] = 2;  zigzag[43][1] = 7;
		zigzag[44][0] = 3;  zigzag[44][1] = 6;
		zigzag[45][0] = 4;  zigzag[45][1] = 5;
		zigzag[46][0] = 5;  zigzag[46][1] = 4;
		zigzag[47][0] = 6;  zigzag[47][1] = 3;
		zigzag[48][0] = 7;  zigzag[48][1] = 2;
		
		zigzag[49][0] = 7;  zigzag[49][1] = 3;
		zigzag[50][0] = 6;  zigzag[50][1] = 4;
		zigzag[51][0] = 5;  zigzag[51][1] = 5;
		zigzag[52][0] = 4;  zigzag[52][1] = 6;
		zigzag[53][0] = 3;  zigzag[53][1] = 7;
		
		zigzag[54][0] = 4;  zigzag[54][1] = 7;
		zigzag[55][0] = 5;  zigzag[55][1] = 6;
		zigzag[56][0] = 6;  zigzag[56][1] = 5;
		zigzag[57][0] = 7;  zigzag[57][1] = 4;
		
		zigzag[58][0] = 7;  zigzag[58][1] = 5;
		zigzag[59][0] = 6;  zigzag[59][1] = 6;
		zigzag[60][0] = 5;  zigzag[60][1] = 7;
		
		zigzag[61][0] = 6;  zigzag[61][1] = 7;
		zigzag[62][0] = 7;  zigzag[62][1] = 6;
		
		zigzag[63][0] = 7;  zigzag[63][1] = 7;
		
		return zigzag;
	}
	
	public static void printArray(double[] F){
		int X = F.length;
		for(int x = 0; x < X; x++){
			//System.out.format("%.1d", F[u][v]);
			System.out.print(F[x] + "  ");
			System.out.print("\t");
		}
		System.out.println("");
	}
	
	public static void printArray(double[][] F) {
		int X = F.length;
		int Y = F[0].length;
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				System.out.format("%.1f", F[x][y]);
				//System.out.print(F[x][y] + "  ");
				System.out.print("\t");
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public static void printArray(int[][] F){
		int X = F.length;
		int Y = F[0].length;
		for(int y = 0; y < Y; y++){
			for(int x = 0; x < X; x++){
				//System.out.format("%.1d", F[u][v]);
				System.out.print(F[x][y] + "  ");
				System.out.print("\t");
			}
			System.out.println("");
		}	
		System.out.println("");
	}
	
	public static void printArray(double[][][] F){
		int X = F.length;
		int Y = F[0].length;
			for(int m = 0; m < 3; m++){
			for(int y = 0; y < Y; y++){
				for(int x = 0; x < X; x++){
					//System.out.format("%.1d", F[u][v]);
					System.out.print(F[x][y][m] + "  ");
					System.out.print("\t");
				}
				System.out.println("");
			}	
			System.out.println("");
		}
		System.out.println("");
	}
	
	public static double[][] getSampleBlock() {
		double[][] table = new double[8][8];
		
		table[0][0] = 49;
		table[1][0] = 61;
		table[2][0] = 69;
		table[3][0] = 61;
		table[4][0] = 78;
		table[5][0] = 89;
		table[6][0] = 100;
		table[7][0] = 112;
		
		table[0][1] = 68;
		table[1][1] = 60;
		table[2][1] = 51;
		table[3][1] = 42;
		table[4][1] = 62;
		table[5][1] = 69;
		table[6][1] = 80;
		table[7][1] = 89;
		
		table[0][2] = 90;
		table[1][2] = 81;
		table[2][2] = 58;
		table[3][2] = 49;
		table[4][2] = 69;
		table[5][2] = 72;
		table[6][2] = 68;
		table[7][2] = 69;
		
		table[0][3] = 100;
		table[1][3] = 91;
		table[2][3] = 79;
		table[3][3] = 72;
		table[4][3] = 69;
		table[5][3] = 68;
		table[6][3] = 59;
		table[7][3] = 58;
		
		table[0][4] = 111;
		table[1][4] = 100;
		table[2][4] = 101;
		table[3][4] = 91;
		table[4][4] = 82;
		table[5][4] = 71;
		table[6][4] = 59;
		table[7][4] = 49;
		
		table[0][5] = 131;
		table[1][5] = 119;
		table[2][5] = 120;
		table[3][5] = 102;
		table[4][5] = 90;
		table[5][5] = 90;
		table[6][5] = 81;
		table[7][5] = 59;
		
		table[0][6] = 148;
		table[1][6] = 140;
		table[2][6] = 129;
		table[3][6] = 99;
		table[4][6] = 92;
		table[5][6] = 78;
		table[6][6] = 59;
		table[7][6] = 39;
		
		table[0][7] = 151;
		table[1][7] = 140;
		table[2][7] = 142;
		table[3][7] = 119;
		table[4][7] = 98;
		table[5][7] = 90;
		table[6][7] = 72;
		table[7][7] = 39;  
		
		return table;
	}
	
	public static double[][][] gen16xBlockRedBlack() {
		double[][][] redblackImg = new double[16][16][3];
		for(int y = 0; y < 16; y++){
			for(int x = 0; x < 16; x += 4){
				redblackImg[x][y][0] = 255;
				redblackImg[x][y][1] = 0;
				redblackImg[x][y][2] = 0;
				
				redblackImg[x + 1][y][0] = 255;
				redblackImg[x + 1][y][1] = 0;
				redblackImg[x + 1][y][2] = 0;
	
				redblackImg[x + 2][y][0] = 0;
				redblackImg[x + 2][y][1] = 0;
				redblackImg[x + 2][y][2] = 0;
				
				redblackImg[x + 3][y][0] = 0;
				redblackImg[x + 3][y][1] = 0;
				redblackImg[x + 3][y][2] = 0;
			}
		}
		return redblackImg;
	}

}
