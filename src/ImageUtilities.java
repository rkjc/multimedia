
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
	
	private static Image arrayToImg(int input[][]){
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

	private static void ImgToArray(Image imgIn, int imgArray[][]){
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
	
}
