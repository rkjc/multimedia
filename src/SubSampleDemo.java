
public class SubSampleDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		double[][] arrayCb = new double[24][24];
		
		int sizeX = 24;
		int sizeY = 24;
		
		int count = 0;
		for(int y = 0; y < sizeY; y++){
			for(int x = 0; x < sizeX; x++){
				arrayCb[x][y] = count++;
			}
		}
		printArray(arrayCb);
		
		int chromaSizeX = (sizeX / 2);
		if(chromaSizeX % 8 > 0)
			chromaSizeX += 8 - chromaSizeX % 8;
		
		int chromaSizeY = (sizeY / 2);
		if(chromaSizeY % 8 > 0)
			chromaSizeY += 8 - chromaSizeY % 8;
		System.out.println("chromaSizeX   " + chromaSizeX);
		
		
		// Subsample Cb and	Cr using 4:2:0 (MPEG1) chrominance subsampling scheme.
		double[][] subSampleCb = new double[chromaSizeX][chromaSizeY];
		for(int y = 0; y < chromaSizeY; y++){	// Initialize empty array to all 0's
			for(int x = 0; x < chromaSizeX; x++){	
				subSampleCb[x][y] = 0.0;
			}
		}
		for(int y = 0; y < sizeY; y += 2){
			for(int x = 0; x < sizeX; x += 2){
				subSampleCb[x/2][y/2] = (arrayCb[x][y] + arrayCb[x + 1][y] + arrayCb[x][y + 1] + arrayCb[x + 1][y + 1]) / 4.0;
			}
		}
		
		printArray(subSampleCb);
		
		double[][] superSampleCb = new double[sizeX][sizeY];
		for(int y = 0; y < sizeY; y++){
			for(int x = 0; x < sizeX; x++){
				superSampleCb[x][y] = subSampleCb[x/2][y/2];
			}
		}
		
		printArray(superSampleCb);
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
				//System.out.format("%.1f", F[x][y]);
				System.out.print(F[x][y] + "  ");
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

}
