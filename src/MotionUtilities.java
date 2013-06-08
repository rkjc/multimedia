import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;




public class MotionUtilities {
	
	public static void motionCompensation(File targetFile, File referenceFile) {		
		Image refImg = new Image(referenceFile.getPath());
		Image targetImg = new Image(targetFile.getPath());
		
		double[][][] referenceUnpadArray = ImageUtilities.imageRGBtoDoubleArray(refImg);
		double[][][] targetUnpadArray = ImageUtilities.imageRGBtoDoubleArray(targetImg);	
		
		double[][][] targetArray = ImageUtilities.padImageArrayMultipleOf(targetUnpadArray, 16);
		double[][][] referenceArray = ImageUtilities.padImageArrayMultipleOf(referenceUnpadArray, 16);
		
		Pair[][] motionVectors = getMotionVectors(targetArray, referenceArray, true);	
		
		InOutUtilities.writeMotionVectorsFile(motionVectors, targetFile, referenceFile);
		InOutUtilities.printMotionVectors(motionVectors, targetFile, referenceFile);
	}
	
	public static void motionCompensationHalfPixel(File targetFile, File referenceFile) {
		Image refImg = new Image(referenceFile.getPath());
		Image targetImg = new Image(targetFile.getPath());
		
		double[][][] referenceUnpadArray = ImageUtilities.imageRGBtoDoubleArray(refImg);
		double[][][] targetUnpadArray = ImageUtilities.imageRGBtoDoubleArray(targetImg);	
		
		double[][][] targetArray = ImageUtilities.padImageArrayMultipleOf(targetUnpadArray, 16);
		double[][][] referenceArray = ImageUtilities.padImageArrayMultipleOf(referenceUnpadArray, 16);
		
		Pair[][] motionVectors = getMotionVectorsHalfPixel(targetArray, referenceArray, true);
		
		InOutUtilities.writeMotionVectorsFile(motionVectors, targetFile, referenceFile);
		InOutUtilities.printMotionVectors(motionVectors, targetFile, referenceFile);	
	}
	
	public static void removeMovingObjects_V1(File targetFile, File referenceFile) {		
		Image refImg = new Image(referenceFile.getPath());
		Image targetImg = new Image(targetFile.getPath());	
		
		double[][][] referenceArray = ImageUtilities.imageRGBtoDoubleArray(refImg);
		double[][][] targetArray = ImageUtilities.imageRGBtoDoubleArray(targetImg);	
		
		double[][][] clearImageArray = removeObjectMethod_1(targetArray, referenceArray);
		
		ImageUtilities.arrayToImg(clearImageArray).display("ClearFrame-method_1");	
	}
	
	public static void removeMovingObjects_V2(File targetFile, File referenceFile, File replacementFile) {		
		Image refImg = new Image(referenceFile.getPath());
		Image targetImg = new Image(targetFile.getPath());
		Image replaceImg = new Image(replacementFile.getPath());
		
		double[][][] referenceArray = ImageUtilities.imageRGBtoDoubleArray(refImg);
		double[][][] targetArray = ImageUtilities.imageRGBtoDoubleArray(targetImg);	
		double[][][] replacementArray = ImageUtilities.imageRGBtoDoubleArray(replaceImg);
		
		double[][][] clearImageArray = removeObjectMethod_2(targetArray, referenceArray, replacementArray);
		
		ImageUtilities.arrayToImg(clearImageArray).display("ClearFrame-method_2");		
	}
	
		
	
	public static Pair[][] getMotionVectors(double[][][] targetArray, double[][][] referenceArray, boolean displayErrorFrame) {					
		int size_X = targetArray.length;
		int size_Y = targetArray[0].length;	
		int macroBlocks_X = size_X / 16;
		int macroBlocks_Y = size_Y / 16;
		
		double[][] greyRefArray = ImageUtilities.convertTo8bitGrayArray(referenceArray);
		double[][] greyTargetArray = ImageUtilities.convertTo8bitGrayArray(targetArray);
		
		Pair[][] motionVectors = new Pair[macroBlocks_X][macroBlocks_Y];	

		Match bestMatch;
		
		// Tx, Ty for target block locations {used to calculate top left corner} (12 x 9 for homework 4 images)
		// Rx, Ry for Reference Frame locations in the Reference ImageArray {used to calculate top left corner}
		// Bx, By block pixel location values (16 x 16)
		
		// Scan through the Target macroBlocks
		for(int Ty = 0; Ty < macroBlocks_Y; Ty++){
			for(int Tx = 0; Tx < macroBlocks_X; Tx++){
				int targetLocX = Tx * 16;
				int targetLocY = Ty * 16;
				
				// Initialize the location holder for the closest frame with a MVC value of 1000 and Location of (0, 0)
				bestMatch = new Match();
				
				// Scan through the Reference frames relative to the TargetFrame
				for(int Ry = -12; Ry < 13; Ry++){	 // p = 12
					for(int Rx = -12; Rx < 13; Rx++){	

						if(targetLocX + Rx >= 0 && targetLocX + Rx + 15 < size_X && targetLocY + Ry >= 0 && targetLocY + Ry + 15 < size_Y){
							
							// Calculate MSD between the Reference frame and the Target macroBlock
							double pixelDiff = 0;
							for(int By = 0; By < 16; By++){ //Block compare
								for(int Bx = 0; Bx < 16; Bx++){								
									pixelDiff += Math.pow(greyRefArray[targetLocX + Rx + Bx][targetLocY + Ry + By] - greyTargetArray[targetLocX + Bx][targetLocY + By], 2);							
								}	
							} // End block compare
							pixelDiff = pixelDiff * (1.0 / (16.0 * 16.0));
							
							bestMatch.putIfSmaller(pixelDiff, new Pair(-Rx, -Ry)); // Keep only the Smallest/Closest minimum MSD location
						}
					}
				} // End reference block scans

				// Store this motion vector in the array the loop through the rest of the Target macroBlocks
				motionVectors[Tx][Ty] = bestMatch.getCoord();
			}
		} // End MacroBlock scan

		
		if(displayErrorFrame){
			// Generating error frame
			double[][] errorFrame = new double[size_X][size_Y];
			for(int Ty = 0; Ty < macroBlocks_Y; Ty++){	// Scan through Macro-Block indexes
				for(int Tx = 0; Tx < macroBlocks_X; Tx++){
					int targetLocX = Tx * 16;
					int targetLocY = Ty * 16;
					int refLocX = motionVectors[Tx][Ty].getX();
					int refLocY = motionVectors[Tx][Ty].getY();
					
					for(int By = 0; By < 16; By++){ // Scan through Block pixel locations and generate pixel errors
						for(int Bx = 0; Bx < 16; Bx++){
							double errorPixel = greyTargetArray[targetLocX + Bx][targetLocY + By] - greyRefArray[targetLocX + Bx - refLocX][targetLocY + By - refLocY];
							// Store the pixel error values in the errorFrame Array
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
			// Shift values, scale, round, then box, and re-save the errorFrame pixel values
			for(int y = 0; y < size_Y; y++){
				for(int x = 0; x < size_X; x++){		
					errorFrame[x][y] = Math.max(Math.min(Math.round((errorFrame[x][y] - minVal) * scale), 255.0), 0.0);
				}
			}
			ImageUtilities.arrayToGreyImgDisplay(errorFrame, "errorFrame");
		}
		
		return motionVectors;
	}
	
	public static Pair[][] getMotionVectorsHalfPixel(double[][][] targetArray, double[][][] referenceArray, boolean displayErrorFrame) {	
		int size_X = targetArray.length;
		int size_Y = targetArray[0].length;	
		int macroBlocks_X = size_X / 16;
		int macroBlocks_Y = size_Y / 16;
		
		double[][] greyTargetArray = ImageUtilities.convertTo8bitGrayArray(targetArray);
		double[][] greyRefArray = ImageUtilities.convertTo8bitGrayArray(referenceArray);		
		
		int halfPixSize_X = (size_X * 2) - 1;
		int halfPixSize_Y = (size_Y * 2) - 1;
		
		// Create expanded reference array with Half-Pixels 
		double[][] halfPixelGreyRefArray = buildHalfPixArray(greyRefArray);
				
		Pair[][] motionVectors = new Pair[macroBlocks_X][macroBlocks_Y];
		
		Match bestMatch;
				
		// Scan through the Target macroBlocks
		for(int Ty = 0; Ty < macroBlocks_Y; Ty++){
			for(int Tx = 0; Tx < macroBlocks_X; Tx++){
				int targetLocX = Tx * 16;
				int targetLocY = Ty * 16;
				int halfPixelLocX = Tx * 32;
				int halfPixelLocY = Ty * 32;
				
				// Initialize the location holder for the closest frame with a MVC value of 1000 and Location of (0, 0)
				bestMatch = new Match();
				
				// Scan through the Reference frames
				for(int Ry = -24; Ry < 25; Ry++){	// p = 12 * 2 for Half-Pixel accuracy
					for(int Rx = -24; Rx < 25; Rx++){	

						// Check that the Reference Frame to be compared is exists inside the ReferenceArray
						if(halfPixelLocX + Rx >= 0 && halfPixelLocX + Rx + (15 * 2) < halfPixSize_X && halfPixelLocY + Ry >= 0 && halfPixelLocY + Ry + (15 * 2) < halfPixSize_Y){
							
							// Calculate MSD between the Reference frame and the Target macroBlock
							double pixelDiff = 0;
							for(int By = 0; By < 16; By++){ //Block compare
								for(int Bx = 0; Bx < 16; Bx++){		
									pixelDiff += Math.pow(halfPixelGreyRefArray[halfPixelLocX + Rx + (Bx * 2)][halfPixelLocY + Ry + (By * 2)] - greyTargetArray[targetLocX + Bx][targetLocY + By], 2);									
								}	
							} // End block compare
							pixelDiff = pixelDiff * (1.0 / (16.0 * 16.0));
													
							bestMatch.putIfSmaller(pixelDiff, new Pair(-Rx, -Ry));
						}
					}
				} // End reference block scans

				// Store this motion vector in the array then loop through the rest of the Target macroBlocks	
				motionVectors[Tx][Ty] = bestMatch.getCoord();			
			}
		} // End MacroBlock scan
		
		
		if(displayErrorFrame){
			// Generating error frame
			double[][] errorFrame = new double[size_X][size_Y];
			for(int Ty = 0; Ty < macroBlocks_Y; Ty++){	// Scan through Macro-Block indexes
				for(int Tx = 0; Tx < macroBlocks_X; Tx++){
					int targetLocX = Tx * 16;
					int targetLocY = Ty * 16;
					int halfPixelLocX = Tx * 32;
					int halfPixelLocY = Ty * 32;
					int refLocX = motionVectors[Tx][Ty].getX();
					int refLocY = motionVectors[Tx][Ty].getY();
					
					for(int By = 0; By < 16; By++){ // Scan through Block pixel locations and generate pixel errors
						for(int Bx = 0; Bx < 16; Bx++){
							
							//halfPixelGreyRefArray - different from full pixel version
							double errorPixel = greyTargetArray[targetLocX + Bx][targetLocY + By] - halfPixelGreyRefArray[halfPixelLocX + (Bx * 2) - refLocX][halfPixelLocY + (By * 2) - refLocY];
	
							// Store the pixel error values in the errorFrame Array
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
			// Shift values, scale, round, then box, and re-save the errorFrame pixel values
			for(int y = 0; y < size_Y; y++){
				for(int x = 0; x < size_X; x++){		
					errorFrame[x][y] = Math.max(Math.min((int)Math.round((errorFrame[x][y] - minVal) * scale), 255.0), 0.0);
				}
			}	
			ImageUtilities.arrayToGreyImgDisplay(errorFrame, "errorFrame");
		}
		
		// For Half-Pixel accuracy the vector values in this array are based on the size of the Half-pixel reference image
		// They will have to be divided by 2 to put them into a range that matches the target image (in 0.5 increments)
		return motionVectors;
	}
	
	public static double[][] buildHalfPixArray(double[][] greyRefArray){
		int size_X = greyRefArray.length;
		int size_Y = greyRefArray[0].length;	
		int macroBlocks_X = size_X / 16;
		int macroBlocks_Y = size_Y / 16;
		
		int superSize_X = (macroBlocks_X * 32)  - 1;
		int superSize_Y = (macroBlocks_Y * 32)  - 1;
		
		double[][] halfPixelGreyRefArray = new double[superSize_X][superSize_Y];
		
		// Super sampling from TargetArray up to halfPixelArray
		// Four conditions:
		// x even, y even
		// x odd, y odd
		// x even, y odd
		// x odd, y even
		
		for(int y = 0; y < superSize_Y; y++){
			for(int x = 0; x < superSize_X; x++){
				if(x%2 == 0 && y%2 == 0){ // x even, y even
					
					halfPixelGreyRefArray[x][y] = greyRefArray[x/2][y/2];	
					
				} else if(x%2 != 0 && y%2 != 0){ // x odd, y odd
					
					halfPixelGreyRefArray[x][y] = (int)((greyRefArray[(int)((x - 1)/2)][(int)((y - 1)/2)] 
											+ greyRefArray[(int)((x - 1)/2)][(int)((y + 1)/2)] 
													+ greyRefArray[(int)((x + 1)/2)][(int)((y - 1)/2)] 
															+ greyRefArray[(int)((x + 1)/2)][(int)((y + 1)/2)] + 2) / 4);
					
				} else if(x%2 == 0 && y%2 != 0){ // x even, y odd
					
					halfPixelGreyRefArray[x][y] = (int)((greyRefArray[(int)((x)/2)][(int)((y - 1)/2)] + greyRefArray[(int)((x)/2)][(int)((y + 1)/2)] + 1) / 2);
					
				} else { // x odd, y even				
					halfPixelGreyRefArray[x][y] = (int)((greyRefArray[(int)((x - 1)/2)][(int)((y)/2)] + greyRefArray[(int)((x + 1)/2)][(int)((y)/2)] + 1) / 2);				
				}
			}
		}		
		return halfPixelGreyRefArray;
	}
	

	public static double[][][] removeObjectMethod_1(double[][][] targetArray, double[][][] referenceArray) {
		int size_X = targetArray.length;
		int size_Y = targetArray[0].length;	
		int macroBlocks_X = size_X / 16;
		int macroBlocks_Y = size_Y / 16;
					
		double[][] greyRefArray = ImageUtilities.convertTo8bitGrayArray(referenceArray);
		double[][] greyTargetArray = ImageUtilities.convertTo8bitGrayArray(targetArray);	
	
		Pair[][] motionVectors = getMotionVectors(targetArray, referenceArray, false);
		
		//TreeMap<Double, Pair> bestMatch = new TreeMap<Double, Pair>();	
		Match bestMatch;
		
		Pair[][] replaceBlockLoc = new Pair[macroBlocks_X][macroBlocks_Y];		
		// Step through motionVector list
		for(int Ty = 0; Ty < macroBlocks_Y; Ty++){	// Scan through Macro-Block target indexes
			for(int Tx = 0; Tx < macroBlocks_X; Tx++){
				int targetLocX = Tx * 16;
				int targetLocY = Ty * 16;
				
				// Initialize the location holder for the closest frame with a MVC value of 1000 and Location of (0, 0)
				bestMatch = new Match();
				
				// For Target macroBlocks where motion vector != (0,0) (Dynamic block)
				if(motionVectors[Tx][Ty].getX() != 0 || motionVectors[Tx][Ty].getY() != 0){
					
					// Scan through surrounding Reference macroBlocks				
					for(int m = -1; m < 3; m++){
						for(int n = -1; n < 3; n++){
							// If Reference macroBlock exists and is static, calculate MSD value
							if(Tx + m >= 0 && Tx + m < macroBlocks_X 
									&& Ty + n >= 0 && Ty + n < macroBlocks_Y 
									&& motionVectors[Tx + m][Ty + n].getX() == 0 
									&& motionVectors[Tx + m][Ty + n].getY() == 0){
								
									double pixelDiff = 0;
									for(int By = 0; By < 16; By++){ //Block compare
										for(int Bx = 0; Bx < 16; Bx++){
											int refLocX = (Tx + m) * 16;
											int refLocY = (Ty + n) * 16;
												pixelDiff += Math.pow(greyRefArray[Bx + refLocX][By + refLocY] - greyTargetArray[Bx + targetLocX][By + targetLocY], 2);							
										}	
									} // End block compare
									pixelDiff = pixelDiff * (1.0 / (16.0 * 16.0));
									bestMatch.putIfSmaller(pixelDiff, new Pair(m, n));
							}	
						}
					} // End of 16x16 Block scan 
				} // End of checking for Dynamic TargetBlock
				
				replaceBlockLoc[Tx][Ty] = bestMatch.getCoord();
			}
		}
		
		// Construct new color image using replaceVector info and color referenceArray
		double[][][] colorStillFrame = new double[size_X][size_Y][3];	
		for(int Ty = 0; Ty < macroBlocks_Y; Ty++){	// Scan through Macro-Block target indexes
			for(int Tx = 0; Tx < macroBlocks_X; Tx++){
				int targetLocX = Tx * 16;
				int targetLocY = Ty * 16;
				int offsetLocX = replaceBlockLoc[Tx][Ty].getX();
				int offsetLocY = replaceBlockLoc[Tx][Ty].getY();
				
				for(int By = 0; By < 16; By++){ // Scan through Block pixel locations
					for(int Bx = 0; Bx < 16; Bx++){
						int refLocX = (Tx + offsetLocX) * 16;
						int refLocY = (Ty + offsetLocY) * 16;
						colorStillFrame[targetLocX + Bx][targetLocY + By][0] = referenceArray[refLocX + Bx][refLocY + By][0];
						colorStillFrame[targetLocX + Bx][targetLocY + By][1] = referenceArray[refLocX + Bx][refLocY + By][1];
						colorStillFrame[targetLocX + Bx][targetLocY + By][2] = referenceArray[refLocX + Bx][refLocY + By][2];
					}
				}			
			}
		}	
		// Return RGB Image Array
		return colorStillFrame;
	}
	
	
	public static double[][][] removeObjectMethod_2(double[][][] targetArray, double[][][] referenceArray, double[][][] replacementArray) {			
		int size_X = targetArray.length;
		int size_Y = targetArray[0].length;		
		int macroBlocks_X = size_X / 16;
		int macroBlocks_Y = size_Y / 16;
				
		Pair[][] motionVectors = getMotionVectors(targetArray, referenceArray, false);
					
		// Construct new color image using replaceVector info and color referenceArray
		double[][][] colorStillFrame = new double[size_X][size_Y][3];		
		for(int Ty = 0; Ty < macroBlocks_Y; Ty++){	// Scan through Target Macro-Block indexes
			for(int Tx = 0; Tx < macroBlocks_X; Tx++){
				int targetLocX = Tx * 16;
				int targetLocY = Ty * 16;
					
				for(int By = 0; By < 16; By++){ // Scan through Block pixel locations
					for(int Bx = 0; Bx < 16; Bx++){
						
						// Check if the Target macroBlock Dynamic
						if(motionVectors[Tx][Ty].getX() != 0 || motionVectors[Tx][Ty].getY() != 0){
							colorStillFrame[targetLocX + Bx][targetLocY + By][0] = replacementArray[targetLocX + Bx][targetLocY + By][0];
							colorStillFrame[targetLocX + Bx][targetLocY + By][1] = replacementArray[targetLocX + Bx][targetLocY + By][1];
							colorStillFrame[targetLocX + Bx][targetLocY + By][2] = replacementArray[targetLocX + Bx][targetLocY + By][2];
						} else {
							colorStillFrame[targetLocX + Bx][targetLocY + By][0] = targetArray[targetLocX + Bx][targetLocY + By][0];
							colorStillFrame[targetLocX + Bx][targetLocY + By][1] = targetArray[targetLocX + Bx][targetLocY + By][1];
							colorStillFrame[targetLocX + Bx][targetLocY + By][2] = targetArray[targetLocX + Bx][targetLocY + By][2];
						}	
					}
				}			
			}
		}
			
		// Return RGB Image Array
		return colorStillFrame;		
	}
	
}
