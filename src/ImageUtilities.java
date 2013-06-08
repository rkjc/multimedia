
public class ImageUtilities {
	
	public static Image makeCirclesImg512(int N, int M){
		System.out.println("making circles");
		Image circImg = new Image(512, 512);
		
		boolean mask[][] = new boolean[512][512];
		for(int i=0;i<512;i++)
			for(int j=0;j<512;j++)
				mask[i][j]=false;
		
		for(int angl = 0; angl < 3600; angl++){
			for(int n = N; n < 256; n += N){
				for(int m = 0; m < M; m++){
					double r = (double)n + (double)m;
					if(r < 256){
					int x = 256 + (int)Math.round(r * Math.cos((double)Math.toRadians((double)angl/10.0)));
					int y = 256 + (int)Math.round(r * Math.sin((double)Math.toRadians((double)angl/10.0)));
					//System.out.println("x = " + x + "   y = " + y + "  n = " + n + "  m = " + m + "   angl =" + angl);
						mask[x][y] = true;
					}
				}
			}
		}
		
		//transfer and convert the boolean mask array to the Image circImg
		for (int x = 0; x < 512; x++) {
			for (int y = 0; y < 512; y++) {
				int[] rgb = new int[3];
				if(mask[x][y] == true)
					rgb[0] = rgb[1] = rgb[2] = 0;
				else
					rgb[0] = rgb[1] = rgb[2] = 255;
				circImg.setPixel(x, y, rgb);
			}
		}
			
		//return the image file
		return circImg;
	}
	
	public static Image resizeNoFilter(Image img, int K){
		int x = img.getH();
		int y = img.getW();
		int[][] imgArrayIn = new int[x][y];
		int[][] imgArrayOut = new int[x/K][y/K];
		ImgToArray(img, imgArrayIn);

		for(int xa = 0; xa < x/K; xa++)
			for(int ya = 0; ya < y/K; ya++){
				imgArrayOut[xa][ya] = imgArrayIn[xa*K][ya*K]; //outputting only the top left corner of the sample block (K x K)
			}

		return arrayToImg(imgArrayOut);
	}
	
	public static Image resizeAverageFilter(Image img, int K){
		int x = img.getH();
		int y = img.getW();
		int[][] imgArrayIn = new int[x][y];
		int[][] imgArrayOut = new int[x/K][y/K];
		ImgToArray(img, imgArrayIn);
	
		for(int xa = 0; xa < x/K; xa++)
			for(int ya = 0; ya < y/K; ya++){
				double avg = 0.0;
				for(int xb = 0; xb < K; xb++)
					for(int yb = 0; yb < K; yb++){
						avg += (double)imgArrayIn[xa*K + xb][ya*K + yb];
					}
				avg = avg/(double)(K * K);
				imgArrayOut[xa][ya] = Math.min((int)Math.round(avg), 255); 
			}

		return arrayToImg(imgArrayOut);
	}

	public static Image resize3x3ver1Filter(Image img, int K){
		int x = img.getW();
		int y = img.getH();
		int[][] imgArrayIn = new int[x][y];
		int[][] imgArrayOut = new int[x/K][y/K];
		ImgToArray(img, imgArrayIn);
	
		for(int xa = 0; xa < x/K; xa++){
			for(int ya = 0; ya < y/K; ya++){
				double avg = 0.0;
				
				avg += (double)imgArrayIn[xa*K][ya*K]/9.0;
				if(ya*K - 1 >= 0)
					avg += (double)imgArrayIn[xa*K][ya*K - 1]/9.0;
				if(ya*K + 1 < y)
					avg += (double)imgArrayIn[xa*K][ya*K + 1]/9.0;
				
				if(xa*K - 1 >= 0){
					avg += (double)imgArrayIn[xa*K - 1][ya*K]/9.0;
					if(ya*K - 1 >= 0)
						avg += (double)imgArrayIn[xa*K - 1][ya*K - 1]/9.0;
					if(ya*K + 1 < y)
						avg += (double)imgArrayIn[xa*K - 1][ya*K + 1]/9.0;
				}
				
				if(xa*K + 1 < x){
					avg += (double)imgArrayIn[xa*K + 1][ya*K]/9.0;
					if(ya*K - 1 >= 0)
						avg += (double)imgArrayIn[xa*K + 1][ya*K - 1]/9.0;
					if(ya*K + 1 < y)
						avg += (double)imgArrayIn[xa*K + 1][ya*K + 1]/9.0;
				}
				imgArrayOut[xa][ya] = Math.min((int)Math.round(avg), 255); 
				//System.out.println("xa = " + xa + "   ya = " + ya + "   avg =" + avg);
			}					
		}
		return arrayToImg(imgArrayOut);
	}

	public static Image resize3x3ver2Filter(Image img, int K){
		int x = img.getW();
		int y = img.getH();
		int[][] imgArrayIn = new int[x][y];
		int[][] imgArrayOut = new int[x/K][y/K];
		ImgToArray(img, imgArrayIn);
	
		for(int xa = 0; xa < x/K; xa++){
			for(int ya = 0; ya < y/K; ya++){
				double avg = 0.0;
				
				avg += (double)imgArrayIn[xa*K][ya*K]*4.0/16.0;
				if(ya*K - 1 >= 0)
					avg += (double)imgArrayIn[xa*K][ya*K - 1]*2.0/16.0;
				if(ya*K + 1 < y)
					avg += (double)imgArrayIn[xa*K][ya*K + 1]*2.0/16.0;
				
				if(xa*K - 1 >= 0){
					avg += (double)imgArrayIn[xa*K - 1][ya*K]*2.0/16.0;
					if(ya*K - 1 >= 0)
						avg += (double)imgArrayIn[xa*K - 1][ya*K - 1]/16.0;
					if(ya*K + 1 < y)
						avg += (double)imgArrayIn[xa*K - 1][ya*K + 1]/16.0;
				}
				
				if(xa*K + 1 < x){
					avg += (double)imgArrayIn[xa*K + 1][ya*K]*2.0/16.0;
					if(ya*K - 1 >= 0)
						avg += (double)imgArrayIn[xa*K + 1][ya*K - 1]/16.0;
					if(ya*K + 1 < y)
						avg += (double)imgArrayIn[xa*K + 1][ya*K + 1]/16.0;
				}
				imgArrayOut[xa][ya] = Math.min((int)Math.round(avg), 255); 
				//System.out.println("xa = " + xa + "   ya = " + ya + "   avg =" + avg);
			}					
		}
		return arrayToImg(imgArrayOut);
	}
	
	public static Image arrayToImg(int input[][]){
		int x = input.length;
		int y = input[0].length;
		Image output = new Image(x, y);
		int[] rgb = new int[3];
		for(int xa = 0; xa < x; xa++)
			for(int ya = 0; ya < y; ya++){
				rgb[0] = rgb[1] = rgb[2] = input[xa][ya];
				output.setPixel(xa, ya, rgb);
			}
		return output;
	}
	
	public static Image arrayToImg(double input[][][]){
		int x = input.length;
		int y = input[0].length;
		Image output = new Image(x, y);
		int[] rgb = new int[3];
		for(int xa = 0; xa < x; xa++)
			for(int ya = 0; ya < y; ya++){
				rgb[0] = (int)input[xa][ya][0];
				rgb[1] = (int)input[xa][ya][1];
				rgb[2] = (int)input[xa][ya][2];
				output.setPixel(xa, ya, rgb);
			}
		return output;
	}
	
	public static void arrayToGreyImgDisplay(double input[][], String filename){
		arrayToGreyImgDisplay(input, filename, false);
	}
	public static void arrayToGreyImgDisplay(double input[][], String filename, boolean writeToFile){
		int x = input.length;
		int y = input[0].length;
		Image output = new Image(x, y);
		int[] rgb = new int[3];
		for(int xa = 0; xa < x; xa++)
			for(int ya = 0; ya < y; ya++){
				rgb[0] = rgb[1] = rgb[2] = (int)input[xa][ya];
				output.setPixel(xa, ya, rgb);
			}
		 output.display(filename);
		 if(writeToFile)
			 output.write2PPM("arrayOut_" + filename + ".ppm");
	}

	public static void ImgToArray(Image imgIn, int imgArray[][]){
		int x = imgIn.getW();
		int y = imgIn.getH();
		int[] rgb = new int[3];
		for(int xa = 0; xa < x; xa++)
			for(int ya = 0; ya < y; ya++){
				imgIn.getPixel(xa, ya, rgb);
				imgArray[xa][ya] = rgb[0];
			}
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
	
	public static double[][][] padImageArrayMultipleOf(double[][][] origImg, int padMultiple) {
		// Don't care about undoing it version of the method
		int[] padXreturn = new int[1];
		int[] padYreturn = new int[1];
		return padImageArrayMultipleOf( origImg,  padMultiple, padXreturn,padYreturn);
	}
	public static double[][][] padImageArrayMultipleOf(double[][][] origImg, int padMultiple, int[] padXreturn, int[] padYreturn) {
		// Pads the image with grey (128, 128, 128)
		int imgOrigSizeX = origImg.length;
		int imgOrigSizeY = origImg[0].length;

		int padX = 0;
		if(imgOrigSizeX % padMultiple > 0)
			padX = padMultiple - imgOrigSizeX % padMultiple;
		int padY = 0;
		if(imgOrigSizeY % padMultiple > 0)
			padY = padMultiple - imgOrigSizeY % padMultiple;
		
		padXreturn[0] = padX;
		padYreturn[0] = padY;
		
		int sizeX = imgOrigSizeX + padX;
		int sizeY = imgOrigSizeY + padY;
							
		double[][][] paddedImg = new double[sizeX][sizeY][3];
		for(int y = 0; y < sizeY; y++){
			for(int x = 0; x < sizeX; x++){
				if(x < imgOrigSizeX && y < imgOrigSizeY){
					paddedImg[x][y][0] = origImg[x][y][0];
					paddedImg[x][y][1] = origImg[x][y][1];
					paddedImg[x][y][2] = origImg[x][y][2];
				} else {
					paddedImg[x][y][0] = 128;
					paddedImg[x][y][1] = 128;
					paddedImg[x][y][2] = 128;
				}
			}
		}
		return paddedImg;
	}
	
	public static double[][][] removePaddingImageArray(double[][][] origImg, int padX, int padY) {
		// Pads the image with black (0, 0, 0)
		int imgOrigSizeX = origImg.length;
		int imgOrigSizeY = origImg[0].length;
		
		int sizeX = imgOrigSizeX - padX;
		int sizeY = imgOrigSizeY - padY;
							
		double[][][] unPaddedImg = new double[sizeX][sizeY][3];
		for(int y = 0; y < sizeY; y++){
			for(int x = 0; x < sizeX; x++){
				unPaddedImg[x][y][0] = origImg[x][y][0];
				unPaddedImg[x][y][1] = origImg[x][y][1];
				unPaddedImg[x][y][2] = origImg[x][y][2];
			}
		}
		return unPaddedImg;
	}
	
	public static void fillArrayZero(double[][] arr){
		int X = arr.length;
		int Y = arr[0].length;
		for(int y = 0; y < Y; y++){
			for(int x = 0; x < X; x++){
				arr[x][y] = 0.0;
			}
		}
	}
}
