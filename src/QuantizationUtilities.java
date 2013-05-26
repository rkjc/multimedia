
public class QuantizationUtilities {

	
	public static Image convertTo8bitGray(Image img) {
		int h = img.getH();
		int w = img.getW();
		Image output = new Image(w, h);
		int[] rgb = new int[3];

		for (int hd = 0; hd < h; hd++) {
			for (int wd = 0; wd < w; wd++) {
				img.getPixel(wd, hd, rgb);

				int gr = (int) Math.round(0.299 * rgb[0] + 0.587 * rgb[1]
						+ 0.114 * rgb[2]);
				gr = Math.min(gr, 255);

				rgb[0] = rgb[1] = rgb[2] = gr;
				output.setPixel(wd, hd, rgb);
			}
		}
		return output;
	}
	
	public static Image bilevelConversionMean(Image img) {
		Image tempimg = convertTo8bitGray(img);
		int ga = getImageMean(tempimg);
		System.out.println("image mean value ga = " + ga);

		int h = tempimg.getH();
		int w = tempimg.getW();
		Image output = new Image(w, h);
		int[] rgb = new int[3];
		int gr = 0;

		for (int hd = 0; hd < h; hd++) {
			for (int wd = 0; wd < w; wd++) {
				tempimg.getPixel(wd, hd, rgb);
				gr = rgb[0];

				if (gr > ga)
					gr = 255;
				else
					gr = 0;

				rgb[0] = rgb[1] = rgb[2] = gr;
				output.setPixel(wd, hd, rgb);
			}
		}
		return output;
	}
	
	public static Image bilevelConversionDiffusion(Image img) {
		Image greyimg = convertTo8bitGray(img);
		int ga = getImageMean(greyimg);

		int h = greyimg.getH();
		int w = greyimg.getW();

		Image output = new Image(w, h);
		int[] rgb = new int[3];
		int outvalue = 0;
		double error = 0;
		double imageArray[][] = new double[w][h];

		// create an array to manipulate the image pixel values in as doubles
		for (int hj = 0; hj < h; hj++)
			for (int wj = 0; wj < w; wj++) {
				int[] rgblocal = new int[3];
				greyimg.getPixel(wj, hj, rgblocal);
				imageArray[wj][hj] = (double) rgblocal[0];
			}

		for (int hi = 0; hi < h; hi++) {
			for (int wi = 0; wi < w; wi++) {
				// cut-off testing part of algorithm
				if ((int) Math.round(imageArray[wi][hi]) > ga)
					outvalue = 255;
				else
					outvalue = 0;

				error = imageArray[wi][hi] - (double) outvalue;

				// calculating Floyd-Steinberg values
				if (hi < h && wi + 1 < w) {
					imageArray[wi + 1][hi] += 7.0 * error / 16.0;
				}
				if (hi + 1 < h) {
					if (wi - 1 >= 0 && wi - 1 < w) {
						imageArray[wi - 1][hi + 1] += 3.0 * error / 16.0;
					}
					if (wi < w) {
						imageArray[wi][hi + 1] += 5.0 * error / 16.0;
					}
					if (wi + 1 < w) {
						imageArray[wi + 1][hi + 1] += error / 16.0;
					}
				}
				// set output image pixel to output value
				rgb[0] = rgb[1] = rgb[2] = outvalue;
				output.setPixel(wi, hi, rgb);
			}
		}
		return output;
	}

	public static Image quadlevelConversionDiffusion(Image img) {
		Image greyimg = convertTo8bitGray(img);
		int h = greyimg.getH();
		int w = greyimg.getW();

		Image output = new Image(w, h);
		int[] rgb = new int[3];
		int outvalue = 0;
		int gr = 0;
		double error = 0;
		double imageArray[][] = new double[w][h];

		// create an array to manipulate the image pixel values in as doubles
		for (int hj = 0; hj < h; hj++)
			for (int wj = 0; wj < w; wj++) {
				int[] rgblocal = new int[3];
				greyimg.getPixel(wj, hj, rgblocal);
				imageArray[wj][hj] = (double) rgblocal[0];
			}

		for (int hi = 0; hi < h; hi++) {
			for (int wi = 0; wi < w; wi++) {
				// cut-off testing part of algorithm
				gr = (int) Math.round(imageArray[wi][hi]);

				if (gr > 212)
					outvalue = 255;
				else if (gr > 127)
					outvalue = 170;
				else if (gr > 42)
					outvalue = 85;
				else
					outvalue = 0;

				error = imageArray[wi][hi] - (double) outvalue;

				// calculating Floyd-Steinberg values
				if (hi < h && wi + 1 < w) {
					imageArray[wi + 1][hi] += 7.0 * error / 16.0;
				}
				if (hi + 1 < h) {
					if (wi - 1 >= 0 && wi - 1 < w) {
						imageArray[wi - 1][hi + 1] += 3.0 * error / 16.0;
					}
					if (wi < w) {
						imageArray[wi][hi + 1] += 5.0 * error / 16.0;
					}
					if (wi + 1 < w) {
						imageArray[wi + 1][hi + 1] += error / 16.0;
					}
				}
				// set output image pixel to output value
				rgb[0] = rgb[1] = rgb[2] = outvalue;
				output.setPixel(wi, hi, rgb);
			}
		}
		return output;
	}

	public static Image convert8bitColor(Image img, String imgname) {
		int[] R = new int[256];
		int[] G = new int[256];
		int[] B = new int[256];
		gerateUniformLUT(R, G, B);
		printColorLUT(R, G, B);

		int h = img.getH();
		int w = img.getW();
		Image imgindex = new Image(w, h);

		int[] rgb = new int[3];
		int iout[] = new int[3];

		// generate image index file
		for (int hd = 0; hd < h; hd++) {
			for (int wd = 0; wd < w; wd++) {
				img.getPixel(wd, hd, rgb);
				int r = rgb[0];
				int g = rgb[1];
				int b = rgb[2];

				for (int i = 0; i < 256; i++) {
					if (r < R[i] + 16 && g < G[i] + 16 && b < B[i] + 32) {
						iout[0] = iout[1] = iout[2] = i;
						imgindex.setPixel(wd, hd, iout);
						break;
					}
				}
			}
		}
		// display and save the index file
		imgindex.display(imgname + "-index-out");
		imgindex.write2PPM(imgname + "-index.ppm");

		// reconstruct 8-bit color image
		Image returnimg = new Image(w, h);
		for (int hd = 0; hd < h; hd++) {
			for (int wd = 0; wd < w; wd++) {
				imgindex.getPixel(wd, hd, iout);
				int i = iout[0];
				rgb[0] = R[i];
				rgb[1] = G[i];
				rgb[2] = B[i];
				returnimg.setPixel(wd, hd, rgb);
			}
		}
		return returnimg;
	}

	public static void gerateUniformLUT(int[] r, int[] g, int[] b) {
		int i = 0;
		for (int R = 16; R < 256; R += 32)
			for (int G = 16; G < 256; G += 32)
				for (int B = 32; B < 256; B += 64) {
					r[i] = R;
					g[i] = G;
					b[i] = B;
					i++;
				}
	}

	public static void printColorLUT(int[] r, int[] g, int[] b) {
		System.out.println("Index" + "\t" + "R" + "\t" + "G" + "\t" + "B");
		System.out.println("-------------------------------------");
		for (int i = 0; i < 256; i++) {
			System.out.println(i + "\t" + r[i] + "\t" + g[i] + "\t" + b[i]);
		}
	}

	// only for grey scale images
	public static int getImageMean(Image img) {
		int h = img.getH();
		int w = img.getW();
		int[] rgb = new int[3];
		int subtotal = 0;

		for (int hd = 0; hd < h; hd++) {
			for (int wd = 0; wd < w; wd++) {
				img.getPixel(wd, hd, rgb);
				subtotal = subtotal + (rgb[0]);
			}
		}
		return Math
				.min((int) Math.round((double) subtotal
						/ ((double) h * (double) w)), 255);
	}
}
