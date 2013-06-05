import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;


public class TestMain extends CS451_Cross {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("running test main");
		
		
		homework4();
	
		
		// Need to make the comparison so that if there are multiple lowest matching values it will pick the 
		// matching reference block closest to the target block (distance formula?)
		// currently it appears to be using the location of the last lowest matching value found 
		
		
		
	}
	
	public static void homework4() {
		
		// This will be copy/pastad into MotionUtility method 1ec half-pixel
		
//		int size_X = 32;
//		int size_Y = 32;
//		
//		int macroBlocks_X = 2;
//		int macroBlocks_Y = 2;
//	
//		double[][] greyRefArray = make32x32Array1();
//		double[][] greyTargetArray = make32x32Array2();
//		
//		ImageUtilities.arrayToGreyImgDisplay(greyRefArray, "greyRefArray");
//		ImageUtilities.arrayToGreyImgDisplay(greyTargetArray, "greyTargetArray");
		
		File refFile = new File("../data/IDB/Walk_060.ppm");
		File targetFile = new File("../data/IDB/Walk_057.ppm");
			
		Image refImg = new Image(refFile.getPath());
		Image targetImg = new Image(targetFile.getPath());
		
		int size_X = refImg.getW();
		int size_Y = refImg.getH();
		
		int macroBlocks_X = size_X / 16;
		int macroBlocks_Y = size_Y / 16;
			
		double[][][] refArray = imageRGBtoDoubleArray(refImg);
		double[][][] targetArray = imageRGBtoDoubleArray(targetImg);
		
		double[][] greyRefArray = convertTo8bitGrayArray(refArray);
		double[][] greyTargetArray = convertTo8bitGrayArray(targetArray);
		
		double[][] errorFrame = new double[size_X][size_Y];
		double[][][] colorErrorFrame = new double[size_X][size_Y][3];
	
		//refImg.display(refFile.getName());
		//targetImg.display(targetFile.getName());
		
		
		
		Pair[][] motionVectors = new Pair[macroBlocks_X][macroBlocks_Y];
		
		
		TreeMap<Double, Pair> bestMatch = new TreeMap<Double, Pair>();
		
		// Tx, Ty for target block locations {used to calculate top left corner} (12 x 9 for homework 4 images)
		// Rx, Ry for reference block locations {used to calculate top left corner} (25 x 25 -> for p=12)
		// Bx, By block pixel location values (16 x 16)
		
		for(int Ty = 0; Ty < macroBlocks_Y; Ty++){
			for(int Tx = 0; Tx < macroBlocks_X; Tx++){
				int targetLocX = Tx * 16;
				int targetLocY = Ty * 16;
				
				bestMatch = new TreeMap<Double, Pair>();
				for(int Ry = -12; Ry < 13; Ry++){
					for(int Rx = -12; Rx < 13; Rx++){	

						if(targetLocX + Rx >= 0 && targetLocX + Rx + 15 < size_X && targetLocY + Ry >= 0 && targetLocY + Ry + 15 < size_Y){
							
							double pixelDiff = 0;
							for(int By = 0; By < 16; By++){ //Block compare
								for(int Bx = 0; Bx < 16; Bx++){
									
										pixelDiff += Math.pow(greyRefArray[Bx + targetLocX + Rx][By + targetLocY + Ry] - greyTargetArray[Bx + targetLocX][By + targetLocY], 2);							
								}	
							} // End block compare
							pixelDiff = pixelDiff * (1.0 / (16.0 * 16.0));
							bestMatch.put(pixelDiff, new Pair(-Rx, -Ry));

						}
					}
				} // End reference block scans

				motionVectors[Tx][Ty] = bestMatch.firstEntry().getValue();
			}
		} // End MacroBlock scan
		
		// Generating error frame
		for(int Ty = 0; Ty < macroBlocks_Y; Ty++){	// Scan through Macro-Block indexes
			for(int Tx = 0; Tx < macroBlocks_X; Tx++){
				int targetLocX = Tx * 16;
				int targetLocY = Ty * 16;
				int refLocX = motionVectors[Tx][Ty].getX();
				int refLocY = motionVectors[Tx][Ty].getY();
				
				for(int By = 0; By < 16; By++){ // Scan through Block pixel locations
					for(int Bx = 0; Bx < 16; Bx++){
						
						double errorPixel = greyTargetArray[targetLocX + Bx][targetLocY + By] - greyRefArray[targetLocX + Bx - refLocX][targetLocY + By - refLocY];
						errorFrame[targetLocX + Bx][targetLocY + By] = errorPixel;
					}
				}
			}
		}
		
		// Re-scale error frame 
		double minVal = 512;
		double maxVal = -512;
		for(int y = 0; y < size_Y; y++){
			for(int x = 0; x < size_X; x++){		
				if(errorFrame[x][y] < minVal) { minVal = errorFrame[x][y];}
				if(errorFrame[x][y] > maxVal) { maxVal = errorFrame[x][y];}
			}
		}
		double range = maxVal - minVal;
		double scale = 255.0 / range;
		// Shift values, scale, then round, then box, and re-save the errorFrame pixel values
		for(int y = 0; y < size_Y; y++){
			for(int x = 0; x < size_X; x++){		
				errorFrame[x][y] = Math.max(Math.min(Math.round((errorFrame[x][y] - minVal) * scale), 255.0), 0.0);
			}
		}
		
		ImageUtilities.arrayToGreyImgDisplay(errorFrame, "error frame");
		writeMotionVectors(motionVectors);
		
	}
	
	public static double[][][] imageRGBtoDoubleArray(Image img) {
		int imgX = img.getW();
		int imgY = img.getH();
		double[][][] imgArray = new double[imgX][imgY][3];
		int[] rgb = new int[3];
		for(int y = 0; y < imgY; y++){
			for(int x = 0; x < imgX; x++){
				img.getPixel(x, y, rgb);
				imgArray[x][y][0] = rgb[0];
				imgArray[x][y][1] = rgb[1];
				imgArray[x][y][2] = rgb[2];
			}
		}
		return imgArray;
	}
	
	public static double[][] convertTo8bitGrayArray(double[][][] imgArray) {
		int x = imgArray.length;
		int y = imgArray[0].length;
		double[][] outputArray = new double[x][y];

		for (int yy = 0; yy < y; yy++) {
			for (int xx = 0; xx < x; xx++) {
				double grey =  Math.round(0.299 * imgArray[xx][yy][0] + 0.587 * imgArray[xx][yy][1] + 0.114 * imgArray[xx][yy][2]);
				grey = Math.min(grey, 255);
				outputArray[xx][yy] = grey;
			}
		}
		return outputArray;
	}

	public static void writeMotionVectors(Pair[][] motionVectors) {
		int macroBlock_X = motionVectors.length;
		int macroBlock_Y = motionVectors[0].length;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter( new FileWriter( "mv.txt"));
			
			
			for(int y = 0; y < macroBlock_Y; y++){
				for(int x = 0; x < macroBlock_X; x++){
					System.out.print("[" + motionVectors[x][y].getX() + ", " + motionVectors[x][y].getY() + "]  \t");
					writer.write("[" + motionVectors[x][y].getX() + ", " + motionVectors[x][y].getY() + "]  \t");	
				}
				System.out.println("");
				writer.newLine();
			}
			writer.newLine();
			writer.write("can you hear me now?");
			writer.write("how about now?");
			writer.newLine();
			writer.write("did this work?");
		}
		catch ( IOException e){
		}
		finally{
			try{
				if ( writer != null)
					writer.close( );
			}
			catch ( IOException e){
			}
	    }
	}
	
	public static double[][] make32x32Array1() {
		double[][] retArray = new double[32][32];
		for(int y = 0; y < 32; y++){
			for(int x = 0; x < 32; x++){
				retArray[x][y] = 120.0;
			}
		}
		for(int y = 11; y < 14; y++){
			for(int x = 11; x < 14; x++){
				retArray[x][y] = 37.0;
			}
		}
		return retArray;
	}
	
	public static double[][] make32x32Array2() {
		double[][] retArray = new double[32][32];
		for(int y = 0; y < 32; y++){
			for(int x = 0; x < 32; x++){
				retArray[x][y] = 120.0;
			}
		}
		for(int y = 3 + 16; y < 6 + 16; y++){
			for(int x = 3 + 16; x < 6 + 16; x++){
				retArray[x][y] = 37.0;
			}
		}
		return retArray;
	}
	
	public static double[][] make16x16Array() {
		double[][] retArray = new double[16][16];
		for(int y = 0; y < 16; y++){
			for(int x = 0; x < 16; x++){
				retArray[x][y] = 120.0;
			}
		}
		return retArray;
	}
	
//	System.out.println("targetLocX + Rx " + targetLocX + " " + Rx + " = " + (targetLocX + Rx) + "  targetLocY + Ry " + targetLocY + " " + Ry + " = " + (targetLocY + Ry));
//	System.out.println((targetLocX + Rx >= 0) + "  " + (targetLocX + Rx + 15 < size_X) + "  " + (targetLocY + Ry >= 0) + "  " + (targetLocY + Ry + 15 < size_Y));
//	System.out.println("pixelDiff, new Pair(-Rx, -Ry) " + pixelDiff + " "  + Rx + " " + Ry);	
//	System.out.println("Tx " + Tx + "  Ty "  + Ty + " bestMatch.firstEntry().getKey() " + bestMatch.firstEntry().getKey());	
	
}
