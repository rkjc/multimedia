public class DebugUtilities {

	public static int test128(String filename) {
		Image testimg = new Image(filename);
		int[] rgb = new int[3];
		testimg.getPixel(128, 128, rgb);
		return rgb[0];
	}

	public static void printBlockOfValues(int wstart, int hstart, Image img) {
		//don't use closer than 5 pixels to bottom or right
		int[] rgb = new int[3];
		System.out.println("");
		System.out.println("--- 20 X 5 pixel block --- w=" +wstart + " h=" + hstart);
		for (int h = hstart; h < hstart + 5; h++) {
			for (int w = wstart; w < wstart + 20; w++) {

				img.getPixel(w, h, rgb);

				System.out.print(rgb[0] + "\t");

			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	public static boolean imageCompare(Image img1, Image img2, int[] pix, int[] error) {
		// error is a single value array for returning the accumulated count of total errors
		// pix is an array size 8 that contains the last pixel difference found - location and both pixels
		int h = img1.getH();
		int w = img1.getW();
		Image output = new Image(w, h);
		int[] rgb1 = new int[3];
		int[] rgb2 = new int[3];
		int outpxR = 0;
		int outpxG = 0;
		int outpxB = 0;
		boolean pass = true;
		error[0] = 0;
		
		for (int hd = 0; hd < h; hd++) { // y  value
			for (int wd = 0; wd < w; wd++) { // x  value
				img1.getPixel(wd, hd, rgb1);
				img2.getPixel(wd, hd, rgb2);
				
				if(rgb1[0] != rgb2[0] || rgb1[1] != rgb2[1] || rgb1[2] != rgb2[2]) {
					pass = false;
					error[0]++;
					pix[0] = wd;
					pix[1] = hd;
					pix[2] = rgb1[0];
					pix[3] = rgb1[1];
					pix[4] = rgb1[2];
					pix[5] = rgb2[0];
					pix[6] = rgb2[1];
					pix[7] = rgb2[2];
				}
				
				outpxR = Math.abs(rgb1[0] - rgb2[0]);
				outpxG = Math.abs(rgb1[1] - rgb2[1]);
				outpxB = Math.abs(rgb1[2] - rgb2[2]);
				
				outpxR = Math.min(outpxR, 255);
				outpxG = Math.min(outpxG, 255);
				outpxB = Math.min(outpxB, 255);

				rgb1[0] = outpxR;
				rgb1[1] = outpxG;
				rgb1[2] = outpxB;
				output.setPixel(wd, hd, rgb1);
			}
		}
		
		output.display("comparisonDifference.ppm");
		//output.write2PPM("comparisonDifference.ppm");
		return pass;
	}
	
	public static void imageCompare(Image img1, Image img2) {
		int h = img1.getH();
		int w = img1.getW();
		Image output = new Image(w, h);
		int[] rgb1 = new int[3];
		int[] rgb2 = new int[3];
		int outpxR = 0;
		int outpxG = 0;
		int outpxB = 0;
		
		for (int hd = 0; hd < h; hd++) { // y  value
			for (int wd = 0; wd < w; wd++) { // x  value
				img1.getPixel(wd, hd, rgb1);
				img2.getPixel(wd, hd, rgb2);
				
				outpxR = Math.abs(rgb1[0] - rgb2[0]);
				outpxG = Math.abs(rgb1[1] - rgb2[1]);
				outpxB = Math.abs(rgb1[2] - rgb2[2]);
				
				outpxR = Math.min(outpxR, 255);
				outpxG = Math.min(outpxG, 255);
				outpxB = Math.min(outpxB, 255);

				rgb1[0] = outpxR;
				rgb1[1] = outpxG;
				rgb1[2] = outpxB;
				output.setPixel(wd, hd, rgb1);
			}
		}
		
		output.display("comparisonDifference.ppm");
		//output.write2PPM("comparisonDifference.ppm");
	}
}
