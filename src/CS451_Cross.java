// Richard Cross
// CS-451 Spring 2013


import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

import javax.swing.JFileChooser;

public class CS451_Cross {

	public static void main(String[] args) {		
		File file = new File("");
		if (args.length > 1) {
			usage();
			System.exit(1);
		}
		else if(args.length > 0){ //if there is a command line argument use that file
			file = new File(args[0]);
			if(!file.isFile()){
				System.out.println("could not find input file");
				usage();
				System.exit(1);
			}
		}
	
		System.out.println("--Welcome to Multimedia Software System--");
		System.out.println("   CS-451 spring 2013    Richard Cross");

		while (true) {			
			switch (getMainMenu()) {
				
				case 1: // homework 1  
					runHomework_One(file);
					break;
	
				case 2: //   homework 2  
					runHomework_Two(file);
					break;
					
				case 3: //   homework 3  
					runHomework_Three(file);
					break;
					
				case 4: //   homework 4  
					runHomework_Four(file);
					break;
	
				default: //main menu selection
					System.out.println("--Good Bye--");
					System.exit(0);
					break;
			}// end Main Menu switch
		}// end outside while
	} // end of main

	
	
	public static void usage() {
		System.out.println("\nUsage: java CS451_Cross [inputfile]\n");
	}

	public static void runHomework_One(File file){	// 1  1  1  1  1  1  1  1  1  1  1  1  1 
		if(!file.isFile()){
			file = InOutUtilities.getUserFileSelection();
			if(!file.isFile()){
				System.out.println("could not find selected file");
				return;
			}
		}
		Image img = new Image(file.getAbsolutePath());
		img.display(file.getName());
		
		Image outimg;
		boolean h1 = true;
		while (h1) {
			System.out.print("\n-- " + file.getName() + " --");
			int h1In = get_H1_MenuInput(); // sends the system into the menu labyrinth
			// selection handler for homework
			switch (h1In) { 
			case 1: // 1 - quantization
				// use 8-bit greyscale conversion method
				outimg = QuantizationUtilities.convertTo8bitGray(img);
				outimg.display(file.getName() + "-8 bit grey");
				outimg.write2PPM("out-8bitGrey.ppm");
				break;
			case 2:
				// Task 2 bi-level conversion
				if (getBilevelVersionselection() == 1) {
					// option 1 using threshold value
					outimg = QuantizationUtilities
							.bilevelConversionMean(img);
					outimg.display(file.getName()
							+ "-bi-level using threshold conversion");
					outimg.write2PPM("out-bilevelThreshold.ppm");
				} else {
					// option 2 using diffusion method
					outimg = QuantizationUtilities
							.bilevelConversionDiffusion(img);
					outimg.display(file.getName()
							+ "-bi-level using diffusion");
					outimg.write2PPM("out-bilevelDiffusion.ppm");
				}
				break;
			case 3:
				// Task 3 - quad-level conversion
				outimg = QuantizationUtilities
						.quadlevelConversionDiffusion(img);
				outimg.display(file.getName() + "-quad-level using diffusion");
				outimg.write2PPM("out-quadlevelDiffusion.ppm");
				break;
			case 4:
				// Task 4 - uniform color quantization
				outimg = QuantizationUtilities.convert8bitColor(img,
						file.getName());
				outimg.display(file.getName() + "-convert8bitColor");
				outimg.write2PPM("out-8bitColor.ppm");
				break;
			case 5:
				file = InOutUtilities.getUserFileSelection();
				if(!file.isFile()){
					return;
				}
				img = new Image(file.getAbsolutePath());
				img.display(file.getName());
			
				break;
			case 6:
				h1 = false;
				break;
			default:
				break;
			// Extra credit - median-cut color quantization
			// sadly did not get to implement this
			}// end switch
		}// end h1 while
	}
	
	public static void runHomework_Two(File file){		// 2  2  2  2  2  2  2  2  2  2  2  2  2  2  2
		boolean h2 = true;
		while (h2) {
			int h2In = get_H2_MenuInput();
			switch (h2In) {	//homework 2 switch
			case 1: // Aliasing
				// get M, N, K
				int val[] = new int[3];
				getInput_N_M_K(val);
				int N = val[0];
				int M = val[1];
				int K = val[2];

				// make circle image - display image
				Image imgH2 = ImageUtilities.makeCirclesImg512(N, M);
				imgH2.display("out-circles_N=" + N + "_M=" + M + ".ppm");
				imgH2.write2PPM("out-circles_N=" + N + "_M=" + M + ".ppm");

				Image h2out;

				// display no filter
				h2out = ImageUtilities.resizeNoFilter(imgH2, K);
				h2out.display("out-circlesResizeNoFilter_N=" + N + "_M=" + M + "_K=" + K + ".ppm");
				//h2out.write2PPM("out-circlesResizeNoFilter_N=" + N + "_M=" + M + "_K=" + K + ".ppm");

				// display average filter
				h2out = ImageUtilities.resizeAverageFilter(imgH2, K);
				h2out.display("out-circlesResizeAverageFilter_N=" + N + "_M=" + M + "_K=" + K + ".ppm");
				//h2out.write2PPM("out-circlesResizeAverageFilter_N=" + N + "_M=" + M + "_K=" + K + ".ppm");

				// display 3x3 filter 1
				h2out = ImageUtilities.resize3x3ver1Filter(imgH2, K);
				h2out.display("out-circlesResize3x3ver1Filter_N=" + N + "_M=" + M + "_K=" + K + ".ppm");
				//h2out.write2PPM("out-circlesResize3x3ver1Filter_N=" + N + "_M=" + M + "_K=" + K + ".ppm");

				// display 3x3 filter 2
				Image h2out2 = ImageUtilities.resize3x3ver2Filter(imgH2, K);
				h2out2.display("out-circlesResize3x3ver2Filter_N=" + N + "_M=" + M + "_K=" + K + ".ppm");
				//h2out2.write2PPM("out-circlesResize3x3ver2Filter_N=" + N + "_M=" + M + "_K=" + K + ".ppm");

				break;
				
			case 2: // Dictionary Coding2 - codes and decodes using LZW	
				boolean loop = false;
				do{
					loop = false;
					String origData = "";
					
					if(file.isFile()){
						origData = InOutUtilities.readFileInput(file);
					}
					else{
						file = InOutUtilities.getUserFileSelection();
						origData = InOutUtilities.readFileInput(file);
					}
					
					if(origData.equals("")){
						System.out.println("Found nothing to encode");
						break;
					}
	
					int fullDictSize = getDictionarySize(); //get input from user
														
					int[] encodedData = {0};
					
					if(CompressionUtilities.encodeLZW(origData, fullDictSize, encodedData)){
						HashMap<Integer, String> dictionary = CompressionUtilities.newDictLZW(origData);
						int startDictSize = dictionary.size();
						
						String decodeData = CompressionUtilities.decodeLZW(encodedData, dictionary, fullDictSize);
						
						System.out.println("\n -- input data --");
						System.out.println(origData);
						
						System.out.println(" -- encoded data --");
						CompressionUtilities.printIntArray(encodedData);
						
						System.out.println("\n -- decoded output --");
						System.out.println(decodeData);
						
						System.out.println("\n -- compression ratio --");
						System.out.println(CompressionUtilities.calcCompressRatio(startDictSize, dictionary, origData, encodedData));
						
						System.out.println("\n -- compression ratio if start condition dictionary is included with endoded data --");
						System.out.println(CompressionUtilities.calcCompressRatioInclStartDict(startDictSize, dictionary, origData, encodedData));
					}
					System.out.println("\nencode another file?");
					System.out.println("1. yes");
					System.out.println("2. no");
					if(InOutUtilities.readInputInteger() == 1){
						file = InOutUtilities.getUserFileSelection();
						if(!file.isFile())
							return;
						loop = true;
					}
				} while(loop);			
				break;
			case 3: //homework 2 - return to next level up
				h2 = false;
				break;
			default:
				break;
			} //end homework 2 switch
		}// end h2 while
	}

	public static void runHomework_Three(File file){	// 3  3  3  3  3  3  3  3  3  3  3  3  3  3  3  3  3 
		       
		if(!file.isFile()){
			file = InOutUtilities.getUserFileSelection();
			if(!file.isFile()){
				System.out.println("could not find selected file");
				return;
			}
		}
		System.out.println("\n----- JPG Processing and Analysis method selection -----");
		System.out.println("           File to process is " + file.getName());
		System.out.println("\n1. Run JPG process version 1 - using algorithms given in homework");
		System.out.println("2. Run JPG process version 2 - using algorithms that give expected answers");
		
		
		if(InOutUtilities.readInputInteger() == 1) {
			JPEGutilities.processJPG(file);
		} else {
			JPEGutilitiesAlt.processJPG(file);
		}
		
		while (true) {
			System.out.println("\n-----  JPG Processing and Analysis method selections  -----");
			System.out.println("          File to process is " + file.getName());
			System.out.println("\n1. Run JPG process version 1 - using algorithms given in homework");
			System.out.println("2. Run JPG process version 2 - using algorithms that give expected answers\n");
			System.out.println("3. Select another file to process");
			System.out.println("4. return to previous menu");
			System.out.println("");
			System.out.println("Please enter the task number [1-4]:");
			
			int h3In = InOutUtilities.readInputInteger();

			switch (h3In) { 
				case 1:
					JPEGutilities.processJPG(file);
					break;
				case 2:
					JPEGutilitiesAlt.processJPG(file);
					break;
				case 3: // 1 - new file entry
					file = InOutUtilities.getUserFileSelection();
					if(!file.isFile()){
						System.out.println("could not find selected file");
						return;
					}
					break;
				default:
				return;
			}
		}
	}
	
	public static void runHomework_Four(File file){	// 4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4
		File targetFile;
		File referenceFile;
		
		while (true) {
			System.out.println("\n-----  Block-based Motion Compensation selections  -----\n");	
			System.out.println("1. Block-Based Motion Compensation");
			System.out.println("2. Removing Moving Objects");
			System.out.println("3. return to previous menu");
			System.out.println("");
			System.out.println("Please enter the task number [1-3]:");
			
			int h3In = InOutUtilities.readInputInteger();

			switch (h3In) { 
				case 1:
					// input file name or number
					targetFile = InOutUtilities.getH4fileInput();
					if(targetFile != null){
						System.out.println("Using as Target file: " + targetFile.getName());
						System.out.println("");
					} else {
						break;
					}
					
					referenceFile = InOutUtilities.getH4fileInput();
					if(referenceFile != null){	 
						System.out.println("Using as Reference file: " + referenceFile.getName());
						System.out.println("");
					} else {
						break;
					}
					
					// pass to function
					MotionUtilities.motionCompensation(targetFile, referenceFile);	
					break;
					
				case 2:	// Remove Motion elements - two methods
					// Input reference number
					File[] targetFileA = new File[1];
					File[] referenceFileA = new File[1];
					if(!InOutUtilities.getH4fileNumberInput(targetFileA, referenceFileA)){
						System.out.println("Sorry, had a problem reading the files");
						break;
					}
					targetFile = targetFileA[0];
					referenceFile = referenceFileA[0];
					
					// pass to first method
					MotionUtilities.removeMovingObjects_V1(targetFile, referenceFile);
					
					// Check if this is running on a file system that matches my home system
					boolean atHome = false;
					File checkFolder = new File("../data/IDB");
						atHome = checkFolder.exists();
					File replacementFile;
					if(atHome){
						replacementFile = new File("../data/IDB/Walk_005.ppm");
					} else {
						replacementFile = new File("Walk_005.ppm");
					}
					
					// pass to second method
					MotionUtilities.removeMovingObjects_V2(targetFile, referenceFile, replacementFile);
					break;
				default:
				return;
			}
		}	
	}
	
	public static int getMainMenu() {			
		System.out.println("");
		System.out.println("---------------Main Menu--------------------");
		System.out.println("1. Homework 1 - Image Quantization");
		System.out.println("2. Homework 2 - Aliasing and Dictionary Coding");
		System.out.println("3. Homework 3 - DCT Based Image Compression");
		System.out.println("4. Homework 4 - Block-based Motion Compensation");
		System.out.println("5. Quit");
		System.out.println("");
		System.out.println("Please enter the task number [1-4]:");

		return InOutUtilities.readInputInteger();
	}

	public static int get_H1_MenuInput() {
		System.out.println("");
		System.out.println("---------------Image Quantization Menu--------------------");
		System.out.println("1. Conversion to Gray-scale Image(24bits->8bits)");
		System.out.println("2. Conversion to Bi-level Image(24bits->1bit)");
		System.out.println("3. Conversion to Quad-level Image (24bits->2bits)");
		System.out.println("4. Conversion to 8bit Indexed Color Image using Uniform Color Quantization(24bits->8bits)");
		System.out.println("5. open a another file");
		System.out.println("6. Return to main menu");
		System.out.println("");
		System.out.println("Please enter the task number [1-5]:");

		return InOutUtilities.readInputInteger();
	}

	// H1 sub-menu
	public static int getBilevelVersionselection() {
		System.out.println("");
		System.out.println("----- select method of bi-level conversion -----");
		System.out.println("1. Conversion to Bi-level Image(24bits->1bit) using threshold method");
		System.out.println("2. Conversion to Bi-level Image(24bits->1bit) using error-diffusion method");
		System.out.println("");
		System.out.println("Please enter the selection number [1-2]:");

		return InOutUtilities.readInputInteger();
	}

	public static int get_H2_MenuInput() {
		System.out.println("");
		System.out.println("---------------Aliasing and Dictionary Coding Menu--------------------");
		System.out.println("1. Aliasing");
		System.out.println("2. Dictionary Coding");
		System.out.println("3. Return to main menu");
		System.out.println("");
		System.out.println("Please enter the task number [1-3]:");

		return InOutUtilities.readInputInteger();
	}
	
	// H2 sub-menu 1
	public static void getInput_N_M_K(int val[]) {
		System.out.println("");
		System.out.println("please enter N (space between rings)");
		val[0] = InOutUtilities.readInputInteger();	
		System.out.println("please enter M (thickness of rings)");
		val[1] = InOutUtilities.readInputInteger();
		System.out.println("Please enter K such that 1 <= K <= 16 (factor to resize image - must be power of 2");
		val[2] = InOutUtilities.readInputInteger();
	}
	
	// H2 sub-menu 2
	public static int getDictionarySize(){
		System.out.println("");
		System.out.println("please enter the max size of dictionary to use");
		return InOutUtilities.readInputInteger();	
	}
	
	public static void get_H3_MenuInput() {
		// Intentionally blank
	}
	
}


