import java.io.File;


public class JPEGutilities {

	
	public static void processJPG(File file) {
		int quality = 0;
		Image origImg;
		origImg = new Image(file.getAbsolutePath());
		//origImg.display(file.getName() + "-original image");
		
		// Get and store original size
		int imgOrigSizeX = origImg.getW();
		int imgOrigSizeY = origImg.getH();
		int padX = 0;
		if(imgOrigSizeX % 8 > 0)
			padX = 8 - imgOrigSizeX % 8;
		int padY = 0;
		if(imgOrigSizeY % 8 > 0)
			padY = 8 - imgOrigSizeY % 8;
		
		int sizeX = imgOrigSizeX + padX;
		int sizeY = imgOrigSizeY + padY;
		
		// Calculate size of Cb and Cr subsample image arrays, greater than (image size/2) and divisible by 8
		int chromaSizeX = (sizeX / 2);
		if(chromaSizeX % 8 > 0)
			chromaSizeX += 8 - chromaSizeX % 8;
		
		int chromaSizeY = (sizeY / 2);
		
		if(chromaSizeY % 8 > 0)
			chromaSizeY += 8 - chromaSizeY % 8;
		
		System.out.println("imgOrigSizeX = " + origImg.getW());
		System.out.println("imgOrigSizeY = " + origImg.getH());
		System.out.println("padX = " + padX);
		System.out.println("padY = " + padY);
		System.out.println("sizeX = " + sizeX);
		System.out.println("sizeY = " + sizeY);
		System.out.println("chromaSizeX = " + chromaSizeX);
		System.out.println("chromaSizeY = " + chromaSizeY);
		
		// Convert original image to an Array
		double[][][] origImgArray = imageRGBtoDoubleArray(origImg);
		
		// Pad Array size to multiple of 8
		double[][][] padArrayRGB = padImageArray(origImgArray, padX, padY);
		
		// Transform each pixel	from RGB to YCbCr		
		double[][][] arrayYCbCr = new double[sizeX][sizeY][3];
		for(int y = 0; y < sizeY; y++){
			for(int x = 0; x < sizeX; x++){	
				//ycbcr[][][3] = convert(rgb[][][3])
				arrayYCbCr[x][y] = convertRGBtoYCbCR(padArrayRGB[x][y]);
			}
		}
		
		// Seperate the YCbCr array into it's individule component arrays
		double[][] arrayY = new double[sizeX][sizeY];
		double[][] arrayCb = new double[sizeX][sizeY];
		double[][] arrayCr = new double[sizeX][sizeY];
		for(int y = 0; y < sizeY; y++){
			for(int x = 0; x < sizeX; x++){	
				arrayY[x][y] = arrayYCbCr[x][y][0];
				arrayCb[x][y] = arrayYCbCr[x][y][1];
				arrayCr[x][y] = arrayYCbCr[x][y][2];
			}
		}
		
		// Subsample Cb and	Cr using 4:2:0 (MPEG1) chrominance subsampling scheme.
		double[][] subSampleCb = new double[chromaSizeX][chromaSizeY];
		double[][] subSampleCr = new double[chromaSizeX][chromaSizeY];	
		for(int y = 0; y < chromaSizeY; y++){	// Initialize empty array to all 0's
			for(int x = 0; x < chromaSizeX; x++){	
				subSampleCb[x][y] = 0.0;
				subSampleCr[x][y] = 0.0;
			}
		}
		for(int y = 0; y < sizeY; y += 2){
			for(int x = 0; x < sizeX; x += 2){
				subSampleCb[x/2][y/2] = (arrayCb[x][y] + arrayCb[x + 1][y] + arrayCb[x][y + 1] + arrayCb[x + 1][y + 1]) / 4.0;
				subSampleCr[x/2][y/2] = (arrayCr[x][y] + arrayCr[x + 1][y] + arrayCr[x][y + 1] + arrayCr[x + 1][y + 1]) / 4.0;
			}
		}
					
		// Perform the DCT for Y image, Cb image, and Cr image 
		double[][] YarrayDCT = runDCTconvertionTrim(arrayY);
		double[][] CbArrayDCT = runDCTconvertionTrim(subSampleCb);
		double[][] CrArrayDCT = runDCTconvertionTrim(subSampleCr);
		
		// Define for use outside the compression ratio output loop
		double[][] quantizedY = new double [sizeX][sizeY];
		double[][] quantizedCb = new double [chromaSizeX][chromaSizeY];
		double[][] quantizedCr = new double [chromaSizeX][chromaSizeY];
		
		System.out.println("\n" + file.getName());

		
		// ############ Calculate compression ratio ############	
		
		for(quality = 0; quality < 6; quality++){	//loop quantization values [0 - 5]
		
			// Quantize image components
			quantizedY = quantizeLuma(YarrayDCT, quality);
			quantizedCb = quantizeChroma(CbArrayDCT, quality);
			quantizedCr = quantizeChroma(CrArrayDCT, quality);
			
			
			// Calculate component compression costs
			double totalCost = 0;
			double countY = 0;
			double countCb = 0;
			double countCr = 0;
			double bitCostY = 0;
			double bitCostCb = 0;
			double bitCostCr = 0;
			double origCost = 0;
			
			System.out.println("\nFor a quantization level n = " + quality);
			
			origCost = imgOrigSizeX * imgOrigSizeY * 24;
			System.out.println("The original image cost, (S), is " + origCost);
			
			//loop through all the image 8x8 blocks and quantize the values
			for(int xx = 0; xx < sizeX; xx += 8){
				for(int yy = 0; yy < sizeY; yy += 8){
					double[][] block = new double[8][8];
					for(int y = 0; y < 8; y++){
						for(int x = 0; x < 8; x++){
							block[x][y] = quantizedY[xx + x][yy + y];
						}
					}
					// Convert from 2D block to 1D sequence (zigzag)
					double[] sequence = blockToSequence(block);
					// Perform a run length encode of 1D sequence and count number of sequence pairs for the AC component
					bitCostY += (9 - quality);  // Adds the DC for that block
					countY = runLengthEncode(sequence); 
					bitCostY += (15 - quality) * countY;
				}
			}
			System.out.println("The Y values cost is " + bitCostY + " bits.");
			
	
			for(int xx = 0; xx < chromaSizeX; xx += 8){
				for(int yy = 0; yy < chromaSizeY; yy += 8){
					
					double[][] block = new double[8][8];
					for(int y = 0; y < 8; y++){
						for(int x = 0; x < 8; x++){
							block[x][y] = quantizedCb[xx + x][yy + y];
						}
					}
					// Convert from 2D block to 1D sequence (zigzag)
					double[] sequence = blockToSequence(block);
					// Perform a run length encode of 1D sequence and count number of sequence pairs
					bitCostCb += (8 - quality);
					countCb = runLengthEncode(sequence);
					bitCostCb += (14 - quality) * countCb;
				}
			}
			System.out.println("The Cb values cost is " + bitCostCb + " bits.");
			
	
			for(int xx = 0; xx < chromaSizeX; xx += 8){
				for(int yy = 0; yy < chromaSizeY; yy += 8){
					double[][] block = new double[8][8];
					for(int y = 0; y < 8; y++){
						for(int x = 0; x < 8; x++){
							block[x][y] = quantizedCr[xx + x][yy + y];
						}
					}
					// Convert from 2D block to 1D sequence (zigzag)
					double[] sequence = blockToSequence(block);
					// Perform a run length encode of 1D sequence and count number of sequence pairs
					bitCostCr += (8 - quality);
					countCr = runLengthEncode(sequence);
					bitCostCr += (14 - quality) * countCr;
				}
			}
			System.out.println("The Cr values cost is " + bitCostCr + " bits.");
			
			// Calculate total compression costs
			totalCost = bitCostY + bitCostCb + bitCostCr;
			System.out.println("The total compressed image cost, (D), is " + totalCost + " bits.");
			
			System.out.println("The compression ratio, (S/D), is " + origCost/totalCost);
			
		
		// ############ END Calculate compression ratio ############
		
		// *** at this point the image is a JPG(ish) thing *** 
		
		// Undo all the conversions
			// De-Quantize the image
			double[][] deQuantizedY = deQuantizeLuma(quantizedY, quality);
			double[][] deQuantizedCb = deQuantizeChroma(quantizedCb, quality);
			double[][] deQuantizedCr = deQuantizeChroma(quantizedCr, quality);
			
			// Undo the DCT for Cb image and Cr image and the Y image
			double[][] invDctArrayY = reverseDCTconvertion(deQuantizedY);
			double[][] invDctArrayCb = reverseDCTconvertion(deQuantizedCb);
			double[][] invDctArrayCr = reverseDCTconvertion(deQuantizedCr);
			
			// Supersample prep
			double[][] outArrayY = new double[sizeX][sizeY];
			double[][] outArrayCb = new double[sizeX][sizeY];
			double[][] outArrayCr = new double[sizeX][sizeY];
				
			outArrayY = invDctArrayY;
			// Supersample
			for(int y = 0; y < sizeY; y++){
				for(int x = 0; x < sizeX; x++){
					outArrayCb[x][y] = invDctArrayCb[x/2][y/2];
					outArrayCr[x][y] = invDctArrayCr[x/2][y/2];
				}
			}
			
			// Recombine the separate arrays into a single image array
			double[][][] outYCbCrArray = new double[sizeX][sizeY][3];
			for(int y = 0; y < sizeY; y++){
				for(int x = 0; x < sizeX; x++){	
					 outYCbCrArray[x][y][0] = outArrayY[x][y];
					 outYCbCrArray[x][y][1] = outArrayCb[x][y];
					 outYCbCrArray[x][y][2] = outArrayCr[x][y];
				}
			}
						
			// Reconverting the YCbCr image back into an RGB image 
			double[][][] outPadImgArrayRGB = new double[sizeX][sizeY][3];
			
			for(int y = 0; y < sizeY; y++){
				for(int x = 0; x < sizeX; x++){
					outPadImgArrayRGB[x][y] =  convertYCbCRtoRGB(outYCbCrArray[x][y]);
					//debug bypass
					//outPadImgArrayRGB[x][y] =  matrixYCbCRtoRGB(arrayYCbCr[x][y]);
				}
			}
			
			// Remove the padding
			double[][][] outImgArrayRGB = unPadImageArray(outPadImgArrayRGB, padX, padY);
			
			//display the RGB image
			//System.out.println("\n---------------------------------\ndisplaying reconverted output image");
			Image outImgRGB = arrayRGBtoImage(outImgArrayRGB);
			outImgRGB.display("imgRGB2-quantization= " + quality);
			
		} // end calculate compression for-loop
		
		
	}
	
//@@@@@@@@@@@@@@@@@@@@@@@@@
	
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
	
	public static Image arrayRGBtoImage (double[][][] imgArray) {
		int lenX = imgArray.length;
		int lenY = imgArray[0].length;
		Image retImg = new Image(lenX, lenY);
		for(int y = 0; y < lenY; y++){
			for(int x = 0; x < lenX; x++){
				int R = (int)Math.min(Math.max((Math.round(imgArray[x][y][0])), 0.0),  255);
				int G = (int)Math.min(Math.max((Math.round(imgArray[x][y][1])), 0.0),  255);
				int B = (int)Math.min(Math.max((Math.round(imgArray[x][y][2])), 0.0),  255);
				int[] rgb = {R, G, B};
				retImg.setPixel(x, y, rgb);
			}
		}
		return retImg;
	}
	
	public static double[][][] padImageArray(double[][][] origImg, int padX, int padY) {
		int orgX = origImg.length;
		int orgY = origImg[0].length;
		int padWX = orgX + padX;
		int padHY = orgY + padY;
		double[][][] padImg = new double[padWX][padHY][3];
		for(int y = 0; y < padHY; y++){
			for(int x = 0; x < padWX; x++){
				if(x < orgX && y < orgY){
					padImg[x][y][0] = origImg[x][y][0];
					padImg[x][y][1] = origImg[x][y][1];
					padImg[x][y][2] = origImg[x][y][2];
				} else {
					padImg[x][y][0] = 0;
					padImg[x][y][1] = 0;
					padImg[x][y][2] = 0;
				}
			}
		}
		return padImg;
	}
	
	public static double[][][] unPadImageArray(double[][][] origImg, int padX, int padY) {
		int orgX = origImg.length;
		int orgY = origImg[0].length;
		int padWX = orgX - padX;
		int padHY = orgY - padY;
		double[][][] unPadImg = new double[padWX][padHY][3];
		for(int y = 0; y < padHY; y++){
			for(int x = 0; x < padWX; x++){
				unPadImg[x][y][0] = origImg[x][y][0];
				unPadImg[x][y][1] = origImg[x][y][1];
				unPadImg[x][y][2] = origImg[x][y][2];	
			}
		}
		return unPadImg;
	}
	
	public static double[] convertRGBtoYCbCR(double[] rgb) {
		double Y = 0.0; double Cb = 0.0; double Cr = 0.0;
		double R = (double)rgb[0]; double G = (double)rgb[1]; double B = (double)rgb[2];
		
		Y = R*(0.2990) + G*(0.5870) + B*(0.1140);
		Cb = R*(-0.168736) + G*(-0.331264) + B*(0.5000);
		Cr = R*(0.5000) + G*(-0.418688) + B*(-0.081312);
		
		Y = Y - 128.0;
		Cb = Cb - 0.5;
		Cr = Cr - 0.5;
		
		Y = Math.max(Math.min(Y, 127.0), -128.0);
		Cb = Math.max(Math.min(Cb, 127.0), -128.0);
		Cr = Math.max(Math.min(Cr, 127.0), -128.0);
	
		return new double[] {Y, Cb, Cr};
	}
	
	public static double[] convertYCbCRtoRGB(double[] ycbcr) {
		double R = 0.0; double G = 0.0; double B = 0.0;
		double Y = ycbcr[0]; double Cb = ycbcr[1]; double Cr = ycbcr[2];
		Y = Y + 128.0;
		Cb = Cb + 0.5;
		Cr = Cr + 0.5;
		
		R = Y*(1.0000) + Cb*(0.0) + Cr*(1.4020);
		G = Y*(1.0000) + Cb*(-0.34414) + Cr*(-0.71414);
		B = Y*(1.0000) + Cb*(1.7720) + Cr*(0.0);
		
		R = Math.round(R);
		G = Math.round(G);
		B = Math.round(B);
		
		R = Math.max(Math.min(R, 255), 0);
		G = Math.max(Math.min(G, 255), 0);
		B = Math.max(Math.min(B, 255), 0);

		return new double[] {R, G, B};
	}
	
	public static double[][] runDCTconvertionTrim(double[][] f) {			
		int sizeX = f.length;
		int sizeY = f[0].length;
		double[][] F = new double[sizeX][sizeY];
		int u, v, x, y;
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
			
				for(int u1 = xx; u1 < xx + 8; u1++){
					u = u1 - xx;
					for(int v1 = yy; v1 < yy + 8; v1++){
						v = v1 - yy;
						double subSum = 0.0;
						for(int x1 = xx; x1 < xx + 8; x1++){
							x = x1 - xx;
							for(int y1 = yy; y1 < yy + 8; y1++){
								y = y1 - yy;
								
								subSum += Math.cos(((2.0*x + 1.0)*u*Math.PI)/(16.0)) * Math.cos(((2.0*y + 1.0)*v*Math.PI)/(16.0)) * f[x1][y1];;
							}
						}
						F[u1][v1] = Math.max(Math.min(((Cfunc(u) * Cfunc(v)) / 4.0) * subSum, 1024.0), -1024.0);
					}
				}	
			}
		}	
		return F;
	}
	
	
	public static double[][] reverseDCTconvertion(double[][] Fp) {		
		int sizeX = Fp.length;
		int sizeY = Fp[0].length;
		double[][] fp = new double[sizeX][sizeY];
		int u, v, x, y;
		
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;
						double subSum = 0.0;
						for(int u1 = xx; u1 < xx + 8; u1++){
							u = u1 - xx;
							for(int v1 = yy; v1 < yy + 8; v1++){
								v = v1 - yy;
				
								subSum += Cfunc(u) * Cfunc(v) * Fp[u1][v1] * Math.cos(((2.0*x + 1.0)*u*Math.PI)/(16.0)) * Math.cos(((2.0*y + 1.0)*v*Math.PI)/(16.0));
							}
						}
						fp[x1][y1] = ((1.0 / 4.0) * subSum);
					}
				}
			}
		}
		return fp;
	}
	
	public static double Cfunc(double x) {
		if(x == 0)
			return Math.sqrt(2.0) / 2.0;
		else
			return 1.0;
	}

	
	public static double[][] quantizeLuma(double[][] input, int n){
		int x, y;
		int sizeX = input.length;
		int sizeY = input[0].length;
		double[][] output = new double[sizeX][sizeY];
		double[][] YquantTable = getLumaQuantizationTable_HW();

		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;

						output[x1][y1] = Math.round(input[x1][y1] / (YquantTable[x][y] * Math.pow(2, n)) );
					}
				}
			}
		}
		return output;
	}

	
	public static double[][] deQuantizeLuma(double[][] input, int n){
		int x, y;
		int sizeX = input.length;
		int sizeY = input[0].length;
		double[][] output = new double[sizeX][sizeY];
		double[][] YquantTable = getLumaQuantizationTable_HW();
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;

						output[x1][y1] = Math.round(input[x1][y1] * (YquantTable[x][y] * Math.pow(2, n)));
					}
				}
			}
		}
		return output;
	}
	
	public static double[][] quantizeChroma(double[][] input, int n){
		int x, y;
		int sizeX = input.length;
		int sizeY = input[0].length;
		double[][] output = new double[sizeX][sizeY];
		double[][] CromeQuant = getCromeQuantTable_HW();
		
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;

						output[x1][y1] = Math.round(input[x1][y1]/(CromeQuant[x][y] * Math.pow(2, n)));
					}
				}
			}
		}
		return output;
	}
	
	public static double[][] deQuantizeChroma(double[][] input, int n){
		int x, y;
		int sizeX = input.length;
		int sizeY = input[0].length;
		double[][] output = new double[sizeX][sizeY];
		double[][] CromeQuant = getCromeQuantTable_HW();
		
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;

						output[x1][y1] = (input[x1][y1] * (CromeQuant[x][y] * Math.pow(2, n)));
					}
				}
			}
		}
		return output;
	}
	
	
	public static double runLengthEncode(double[] inSequence){
		int pairCount = 0;
		double[][] RLEpairs = new double[64][2];
		double preValue = inSequence[1];  
		//double preValue = inSequence[0];   //breaks the code - make redblack work
		int cc = 1;
		int i = 0;
		for(i = 2; i < 64; i++){
		//for(i = 1; i < 64; i++){  //breaks the code - make redblack work
			if(inSequence[i] == preValue){
				cc++;
			}
			else{
				RLEpairs[pairCount][0] = preValue;
				RLEpairs[pairCount][1] = cc;
				pairCount++;
				preValue = inSequence[i];
				cc = 1;
			}
		}
		RLEpairs[pairCount][0] = preValue;
		RLEpairs[pairCount][1] = cc;
		pairCount++;
		
		return pairCount;
	}
	
	public static double[] blockToSequence(double[][] input) {
		double[] output = new double[64];
		int[][] zigzag = getZigzagPattern();	
		for(int i = 0; i < 64; i++){
			output[i] = input[zigzag[i][0]][zigzag[i][1]];
		}	
		return output;
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

	
	public static double[][] getLumaQuantizationTable_HW() {
		double[][] table = new double[8][8];
		
		table[0][0] = 4;
		table[1][0] = 4;
		table[2][0] = 4;
		table[3][0] = 8;
		table[4][0] = 8;
		table[5][0] = 16;
		table[6][0] = 16;
		table[7][0] = 32;
		
		table[0][1] = 4;
		table[1][1] = 4;
		table[2][1] = 4;
		table[3][1] = 8;
		table[4][1] = 8;
		table[5][1] = 16;
		table[6][1] = 16;
		table[7][1] = 32;
		
		table[0][2] = 4;
		table[1][2] = 4;
		table[2][2] = 8;
		table[3][2] = 8;
		table[4][2] = 16;
		table[5][2] = 16;
		table[6][2] = 32;
		table[7][2] = 32;
		
		table[0][3] = 8;
		table[1][3] = 8;
		table[2][3] = 8;
		table[3][3] = 16;
		table[4][3] = 16;
		table[5][3] = 32;
		table[6][3] = 32;
		table[7][3] = 32;
		
		table[0][4] = 8;
		table[1][4] = 8;
		table[2][4] = 16;
		table[3][4] = 16;
		table[4][4] = 32;
		table[5][4] = 32;
		table[6][4] = 32;
		table[7][4] = 32;
		
		table[0][5] = 16;
		table[1][5] = 16;
		table[2][5] = 16;
		table[3][5] = 32;
		table[4][5] = 32;
		table[5][5] = 32;
		table[6][5] = 32;
		table[7][5] = 32;
		
		table[0][6] = 16;
		table[1][6] = 16;
		table[2][6] = 32;
		table[3][6] = 32;
		table[4][6] = 32;
		table[5][6] = 32;
		table[6][6] = 32;
		table[7][6] = 32;
		
		table[0][7] = 32;
		table[1][7] = 32;
		table[2][7] = 32;
		table[3][7] = 32;
		table[4][7] = 32;
		table[5][7] = 32;
		table[6][7] = 32;
		table[7][7] = 32; 
		
		return table;
	}
	

	
	public static double[][] getCromeQuantTable_HW() {
		double[][] table = new double[8][8];
		
		table[0][0] = 8;
		table[1][0] = 8;
		table[2][0] = 8;
		table[3][0] = 16;
		table[4][0] = 32;
		table[5][0] = 32;
		table[6][0] = 32;
		table[7][0] = 32;
		
		table[0][1] = 8;
		table[1][1] = 8;
		table[2][1] = 8;
		table[3][1] = 16;
		table[4][1] = 32;
		table[5][1] = 32;
		table[6][1] = 32;
		table[7][1] = 32;
		
		table[0][2] = 8;
		table[1][2] = 8;
		table[2][2] = 16;
		table[3][2] = 32;
		table[4][2] = 32;
		table[5][2] = 32;
		table[6][2] = 32;
		table[7][2] = 32;
		
		table[0][3] = 16;
		table[1][3] = 16;
		table[2][3] = 32;
		table[3][3] = 32;
		table[4][3] = 32;
		table[5][3] = 32;
		table[6][3] = 32;
		table[7][3] = 32;
		
		table[0][4] = 32;
		table[1][4] = 32;
		table[2][4] = 32;
		table[3][4] = 32;
		table[4][4] = 32;
		table[5][4] = 32;
		table[6][4] = 32;
		table[7][4] = 32;
		
		table[0][5] = 32;
		table[1][5] = 32;
		table[2][5] = 32;
		table[3][5] = 32;
		table[4][5] = 32;
		table[5][5] = 32;
		table[6][5] = 32;
		table[7][5] = 32;
		
		table[0][6] = 32;
		table[1][6] = 32;
		table[2][6] = 32;
		table[3][6] = 32;
		table[4][6] = 32;
		table[5][6] = 32;
		table[6][6] = 32;
		table[7][6] = 32;
		
		table[0][7] = 32;
		table[1][7] = 32;
		table[2][7] = 32;
		table[3][7] = 32;
		table[4][7] = 32;
		table[5][7] = 32;
		table[6][7] = 32;
		table[7][7] = 32; 
		
		return table;
	}

}
