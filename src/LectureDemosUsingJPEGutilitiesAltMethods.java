import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class LectureDemosUsingJPEGutilitiesAltMethods extends JPEGutilitiesAlt{

	public static void main(String[] args) {
		
		boolean runDemo = true;
		while(runDemo){
			System.out.println("\nPlease select a demo to run\n");
			System.out.println("1. demo1();	// Lecture 08, page 9, example - reversability of both versions");
			System.out.println("2. demo2(); 	//Lecture 08, page 9, example - quantized coefficients of both versions");
			System.out.println("3. demo3();	//Lecture 08, page 10, example");
			System.out.println("4. demo4();	//Lecture 08, page 11, top frame example");
			System.out.println("5. demo5();	//Lecture 08, page 11, bottom frame example");
			System.out.println("6. demo6();	//Lecture 08, JPEG supplemental Page");
			System.out.println("7. demo7(); // all black");
			System.out.println("8. exit");
			
			switch(readInputInteger()){
			case 1:
				demo1();
				break;
			case 2:
				demo2();
				break;
			case 3:
				demo3();
				break;
			case 4:
				demo4();
				break;
			case 5:
				demo5();
				break;
			case 6:
				demo6();
				break;
			case 7:
				demo7();
				break;
			default:
				runDemo = false;	
			}
			System.out.println("\n--- Good Bye ---");
		}
	}
	
	public static void demo1(){
		System.out.println("Demo 1()");
		System.out.println("All calculations are done in double and passed as doubles");
		System.out.println("Values are rounded and converted to ints only for printing output\n");
		System.out.println("Lecture 08, page 9, example - reversability of both versions");
		
		double[][] sampleBlock = getLect08page9_SampleBlock();
		
		System.out.println("\nsampleBlock input");
		printArray(sampleBlock);
		
		System.out.println("---------");
		
		System.out.println("\nOutput of runDCTconvertion()");
		double[][] DCT = runDCTconvertion(sampleBlock);
		printArray(DCT);
		
		System.out.println("\nOutput of reverseDCTconvertion()");
		double[][] reverseDCT = reverseDCTconvertion(DCT);
		printArray(reverseDCT);
		
		System.out.println("---------");
		
		System.out.println("\nOutput of runDCTconvertionTrim()  - this is what the homework uses");
		double[][] DCTtrim = runDCTconvertionTrim(sampleBlock);
		printArray(DCTtrim);
		
		System.out.println("Output of reverseDCTconvertionTrim() - this is what the homework uses");
		double[][] reverseDCTtrim = reverseDCTconvertion(DCTtrim);
		printArray(reverseDCTtrim);
		
		System.out.println("---------");
		
		System.out.println("\nOutput of runDCTconvertionAlt() - version that adds and subtracts 128");
		System.out.println("This is the  output that matches the example output");
		double[][] DCTalt = runDCTconvertionAlt(sampleBlock);
		printArray(DCTalt);
		
		System.out.println("\nOutput of reverseDCTconvertionAlt()");
		double[][] DCTaltR = reverseDCTconvertionAlt(DCTalt);
		printArray(DCTaltR);
		
		
	}
	
	public static void demo2() {
		System.out.println("Demo 1()");
		System.out.println("All calculations are done in double and passed as doubles");
		System.out.println("Values are rounded and converted to ints only for printing output\n");
		System.out.println("Lecture 08, page 9, example - quantized coefficients of both versions");
		
		double[][] sampleBlock = getLect08page9_SampleBlock();
		double[][] quantizeTable = getLect08page9_QuantizeTable();
		
		System.out.println("\nsampleBlock");
		printArrayIntView(sampleBlock);
		
		System.out.println("\nOutput of runDCTconvertion()");
		double[][] DCT = runDCTconvertion(sampleBlock);
		printArrayIntView(DCT);
		
		System.out.println("\nOutput of quantize(DCT)");
		double[][] quantDCT = quantize(DCT, 0, quantizeTable);
		printArrayIntView(quantDCT);
		
		System.out.println("\n---------");
	
		
		System.out.println("\nOutput of runDCTconvertionAlt() - version that adds and subtracts 128");
		System.out.println("This is the  output that matches the example output");
		double[][] DCTalt = runDCTconvertionAlt(sampleBlock);
		printArrayIntView(DCTalt);
		
		System.out.println("\nOutput of quantize(DCTalt)");
		double[][] quantDCTalt = quantize(DCTalt, 0, quantizeTable);
		printArray(quantDCTalt);
	}
	
	public static void demo3() {	
		System.out.println("Demo 3()");
		System.out.println("All calculations are done in double and passed as doubles");
		System.out.println("Values are rounded and converted to ints only for printing output\n");
		System.out.println("Lecture 08, page 10, example - using Alt DCT conversion");
		
		double[][] sampleBlock = getLect08page10_SampleBlock();
		double[][] lumaTable = getLect08page10_LumaTable();
		
		System.out.println("\ninitial image block");
		printArrayIntView(sampleBlock);
		
		System.out.println("\nQuantization Table");
		printArrayIntView(lumaTable);
		
		
		System.out.println("\nOutput of runDCTconvertionAlt()");
		double[][] DCT = runDCTconvertionAlt(sampleBlock);
		printArrayIntView(DCT);
		
		System.out.println("Luma Quantization");
		printArrayIntView(quantize(DCT, 0, lumaTable));
		
	}
	
	public static void demo4() {
		System.out.println("Demo 4()");
		System.out.println("All calculations are done in double and passed as doubles");
		System.out.println("Values are rounded and converted to ints only for printing output\n");
		System.out.println("Lecture 08, page 11, example using Alt DCT conversion");
		
		double[][] sampleBlock = getLect08page11_Top_SampleBlock();
		double[][] lumaTable = getLect08page10_LumaTable();
		
		System.out.println("\ninitial image block");
		printArrayIntView(sampleBlock);
		
		System.out.println("\nQuantization Table");
		printArrayIntView(lumaTable);
		
		
		System.out.println("\nOutput of runDCTconvertionAlt()");
		double[][] DCT = runDCTconvertionAlt(sampleBlock);
		printArrayIntView(DCT);
		
		System.out.println("Luma Quantization");
		printArrayIntView(quantize(DCT, 0, lumaTable));
		
	}
	

	public static void demo5() {
		System.out.println("Demo 5()");
		System.out.println("All calculations are done in double and passed as doubles");
		System.out.println("Values are rounded and converted to ints only for printing output\n");
		System.out.println("Lecture 08, page 11, bottom of page - example using normal DCT conversion");
		
		double[][] sampleBlock = getLect08page11_Bottom_SampleBlock();
		double[][] lumaTable = getLect08page10_LumaTable();
		
		System.out.println("\ninitial image block");
		printArrayIntView(sampleBlock);
		
		System.out.println("\nQuantization Table");
		printArrayIntView(lumaTable);
		
		
		System.out.println("\nOutput of runDCTconvertion()");
		double[][] DCT = runDCTconvertion(sampleBlock);
		printArrayIntView(DCT);
		
		System.out.println("Luma Quantization");
		double[][] quantize = quantize(DCT, 0, lumaTable);
		printArrayIntView(quantize);
		
		System.out.println("Luma de-quantize");
		double[][] deQuantize = deQuantize(quantize, 0, lumaTable);
		printArrayIntView(deQuantize);
		
		System.out.println("\nOutput of reverseDCTconvertion()");
		double[][] DCTrev = reverseDCTconvertion(deQuantize);
		printArrayIntView(DCTrev);
		
	}
	
	public static void demo6() {
		System.out.println("Demo 6()");
		System.out.println("All calculations are done in double and passed as doubles");
		System.out.println("Values are rounded and converted to ints only for printing output\n");
		System.out.println("Lecture 08, JPEG supplimental Page - example using DCT-trim conversion");
		
		double[][] sampleBlock = getLec08_JPEGsupplimentalPage_SampleBlock();
		double[][] lumaTable = getLec08_JPEGsupplimentalPage_QuantizeTable();
		
		System.out.println("\ninitial image block");
		printArray(sampleBlock);
		
		System.out.println("\nQuantization Table");
		printArray(lumaTable);
		
		
		System.out.println("\nOutput of runDCTconvertionTrim()");
		double[][] DCT = runDCTconvertionTrim(sampleBlock);
		printArray(DCT);
		
		System.out.println("Luma Quantization");
		double[][] quantize = quantize(DCT, 0, lumaTable);
		printArray(quantize);
		
		
		printArray(blockToSequence(quantize));
		System.out.println(blockToSequence(quantize)[0]);
		System.out.println("");
		
		System.out.println("Luma de-quantize");
		double[][] deQuantize = deQuantize(quantize, 0, lumaTable);
		printArray(deQuantize);
		
		System.out.println("\nOutput of reverseDCTconvertion()");
		double[][] DCTrev = reverseDCTconvertion(deQuantize);
		printArray(DCTrev);
		
	}
	
	public static void demo7() {
		System.out.println("Demo 7()");
		System.out.println("All calculations are done in double and passed as doubles");
		System.out.println("Values are rounded and converted to ints only for printing output\n");
		System.out.println("all black");
		
		double[][] sampleBlock = getAllBlack();
		System.out.println("\nsampleBlock input");
		printArray(sampleBlock);
		
		System.out.println("\nOutput of runDCTconvertion()");
		double[][] DCT = runDCTconvertion(sampleBlock);
		printArray(DCT);
		
		System.out.println("\nOutput of reverseDCTconvertion()");
		double[][] reverseDCT = reverseDCTconvertion(DCT);
		printArray(reverseDCT);
		
		System.out.println("---------");
		
		System.out.println("\nOutput of runDCTconvertionTrim()  - this is what the homework uses");
		double[][] DCTtrim = runDCTconvertionTrim(sampleBlock);
		printArray(DCTtrim);
		
		System.out.println("Output of reverseDCTconvertionTrim() - this is what the homework uses");
		double[][] reverseDCTtrim = reverseDCTconvertion(DCTtrim);
		printArray(reverseDCTtrim);
		
		System.out.println("---------");
		
		System.out.println("\nOutput of runDCTconvertionAlt() - version that adds and subtracts 128");
		double[][] DCTalt = runDCTconvertionAlt(sampleBlock);
		printArray(DCTalt);
		
		System.out.println("\nOutput of reverseDCTconvertionAlt()");
		double[][] DCTaltR = reverseDCTconvertionAlt(DCTalt);
		printArray(DCTaltR);
		
	}
	
	public static int readInputInteger(){
		InputStreamReader inreader = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(inreader);
		String stringIn = "";
		
		while(!isInteger(stringIn)){	
			try{	
				stringIn = in.readLine();
			} catch (IOException err) {
				System.out.println("oops - fatal error reading line");
				System.exit(1);
			}
		}
		return Integer.parseInt(stringIn);
	}

	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }	    
	    return true;
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
	

	private static double[][] getLect08page9_SampleBlock() {
		double[][] table = new double[8][8];
		
		table[0][0] = 139;
		table[1][0] = 144;
		table[2][0] = 149;
		table[3][0] = 153;
		table[4][0] = 155;
		table[5][0] = 155;
		table[6][0] = 155;
		table[7][0] = 155;

		table[0][1] = 144;
		table[1][1] = 151;
		table[2][1] = 153;
		table[3][1] = 156;
		table[4][1] = 159;
		table[5][1] = 156;
		table[6][1] = 156;
		table[7][1] = 156;

		table[0][2] = 150;
		table[1][2] = 155;
		table[2][2] = 160;
		table[3][2] = 163;
		table[4][2] = 158;
		table[5][2] = 156;
		table[6][2] = 156;
		table[7][2] = 156;

		table[0][3] = 159;
		table[1][3] = 161;
		table[2][3] = 162;
		table[3][3] = 160;
		table[4][3] = 160;
		table[5][3] = 159;
		table[6][3] = 159;
		table[7][3] = 159;

		table[0][4] = 159;
		table[1][4] = 160;
		table[2][4] = 161;
		table[3][4] = 162;
		table[4][4] = 162;
		table[5][4] = 155;
		table[6][4] = 155;
		table[7][4] = 155;

		table[0][5] = 161;
		table[1][5] = 161;
		table[2][5] = 161;
		table[3][5] = 161;
		table[4][5] = 160;
		table[5][5] = 157;
		table[6][5] = 157;
		table[7][5] = 157;

		table[0][6] = 162;
		table[1][6] = 162;
		table[2][6] = 161;
		table[3][6] = 163;
		table[4][6] = 162;
		table[5][6] = 157;
		table[6][6] = 157;
		table[7][6] = 157;

		table[0][7] = 162;
		table[1][7] = 162;
		table[2][7] = 161;
		table[3][7] = 161;
		table[4][7] = 163;
		table[5][7] = 158;
		table[6][7] = 158;
		table[7][7] = 158;
		
		return table;
	}
	
	public static double[][] getLect08page9_QuantizeTable() {
		double[][] table = new double[8][8];
		
		table[0][0] = 16;
		table[1][0] = 11;
		table[2][0] = 10;
		table[3][0] = 16;
		table[4][0] = 24;
		table[5][0] = 40;
		table[6][0] = 51;
		table[7][0] = 61;
		
		table[0][1] = 12;
		table[1][1] = 12;
		table[2][1] = 14;
		table[3][1] = 19;
		table[4][1] = 26;
		table[5][1] = 58;
		table[6][1] = 60;
		table[7][1] = 55;
		
		table[0][2] = 14;
		table[1][2] = 13;
		table[2][2] = 16;
		table[3][2] = 24;
		table[4][2] = 40;
		table[5][2] = 57;
		table[6][2] = 69;
		table[7][2] = 56;
		
		table[0][3] = 14;
		table[1][3] = 17;
		table[2][3] = 22;
		table[3][3] = 29;
		table[4][3] = 51;
		table[5][3] = 87;
		table[6][3] = 80;
		table[7][3] = 62;
		
		table[0][4] = 18;
		table[1][4] = 22;
		table[2][4] = 37;
		table[3][4] = 56;
		table[4][4] = 68;
		table[5][4] = 109;
		table[6][4] = 103;
		table[7][4] = 77;
		
		table[0][5] = 24;
		table[1][5] = 35;
		table[2][5] = 55;
		table[3][5] = 64;
		table[4][5] = 81;
		table[5][5] = 104;
		table[6][5] = 113;
		table[7][5] = 92;
		
		table[0][6] = 49;
		table[1][6] = 64;
		table[2][6] = 78;
		table[3][6] = 87;
		table[4][6] = 103;
		table[5][6] = 121;
		table[6][6] = 120;
		table[7][6] = 101;
		
		table[0][7] = 72;
		table[1][7] = 92;
		table[2][7] = 95;
		table[3][7] = 98;
		table[4][7] = 112;
		table[5][7] = 100;
		table[6][7] = 103;
		table[7][7] = 99; 
		
		return table;
	}
	

	public static double[][] getLect08page10_LumaTable() {

		return getLect08page9_QuantizeTable();

	}
	
	public static double[][] getLect08page10_ChromaTable() {
		double[][] table = new double[8][8];
		
		table[0][0] = 17;
		table[1][0] = 18;
		table[2][0] = 24;
		table[3][0] = 47;
		table[4][0] = 99;
		table[5][0] = 99;
		table[6][0] = 99;
		table[7][0] = 99;
		
		table[0][1] = 18;
		table[1][1] = 21;
		table[2][1] = 26;
		table[3][1] = 66;
		table[4][1] = 99;
		table[5][1] = 99;
		table[6][1] = 99;
		table[7][1] = 99;
		
		table[0][2] = 24;
		table[1][2] = 26;
		table[2][2] = 56;
		table[3][2] = 99;
		table[4][2] = 99;
		table[5][2] = 99;
		table[6][2] = 99;
		table[7][2] = 99;
		
		table[0][3] = 47;
		table[1][3] = 66;
		table[2][3] = 99;
		table[3][3] = 99;
		table[4][3] = 99;
		table[5][3] = 99;
		table[6][3] = 99;
		table[7][3] = 99;
		
		table[0][4] = 99;
		table[1][4] = 99;
		table[2][4] = 99;
		table[3][4] = 99;
		table[4][4] = 99;
		table[5][4] = 99;
		table[6][4] = 99;
		table[7][4] = 99;
		
		table[0][5] = 99;
		table[1][5] = 99;
		table[2][5] = 99;
		table[3][5] = 99;
		table[4][5] = 99;
		table[5][5] = 99;
		table[6][5] = 99;
		table[7][5] = 99;
		
		table[0][6] = 99;
		table[1][6] = 99;
		table[2][6] = 99;
		table[3][6] = 99;
		table[4][6] = 99;
		table[5][6] = 99;
		table[6][6] = 99;
		table[7][6] = 99;
		
		table[0][7] = 99;
		table[1][7] = 99;
		table[2][7] = 99;
		table[3][7] = 99;
		table[4][7] = 99;
		table[5][7] = 99;
		table[6][7] = 99;
		table[7][7] = 99; 
		
		return table;
	}
	
	public static double[][] getLect08page10_SampleBlock() {
		double[][] table = new double[8][8];
		
		table[0][0] = 200;
		table[1][0] = 202;
		table[2][0] = 189;
		table[3][0] = 188;
		table[4][0] = 189;
		table[5][0] = 175;
		table[6][0] = 175;
		table[7][0] = 175;
		
		table[0][1] = 200;
		table[1][1] = 203;
		table[2][1] = 198;
		table[3][1] = 188;
		table[4][1] = 189;
		table[5][1] = 182;
		table[6][1] = 178;
		table[7][1] = 175;
		
		table[0][2] = 203;
		table[1][2] = 200;
		table[2][2] = 200;
		table[3][2] = 195;
		table[4][2] = 200;
		table[5][2] = 187;
		table[6][2] = 185;
		table[7][2] = 175;
		
		table[0][3] = 200;
		table[1][3] = 200;
		table[2][3] = 200;
		table[3][3] = 200;
		table[4][3] = 197;
		table[5][3] = 187;
		table[6][3] = 187;
		table[7][3] = 187;
		
		table[0][4] = 200;
		table[1][4] = 205;
		table[2][4] = 200;
		table[3][4] = 200;
		table[4][4] = 195;
		table[5][4] = 188;
		table[6][4] = 187;
		table[7][4] = 175;
		
		table[0][5] = 200;
		table[1][5] = 200;
		table[2][5] = 200;
		table[3][5] = 200;
		table[4][5] = 200;
		table[5][5] = 190;
		table[6][5] = 187;
		table[7][5] = 175;
		
		table[0][6] = 205;
		table[1][6] = 200;
		table[2][6] = 199;
		table[3][6] = 200;
		table[4][6] = 191;
		table[5][6] = 187;
		table[6][6] = 187;
		table[7][6] = 175;
		
		table[0][7] = 210;
		table[1][7] = 200;
		table[2][7] = 200;
		table[3][7] = 200;
		table[4][7] = 188;
		table[5][7] = 185;
		table[6][7] = 187;
		table[7][7] = 186;
		
		return table;
	}
	
	public static double[][] getLect08page11_Top_SampleBlock() {
		double[][] table = new double[8][8];
		
		table[0][0] = 70;
		table[1][0] = 70;
		table[2][0] = 100;
		table[3][0] = 70;
		table[4][0] = 87;
		table[5][0] = 87;
		table[6][0] = 150;
		table[7][0] = 187;
		
		table[0][1] = 85;
		table[1][1] = 100;
		table[2][1] = 96;
		table[3][1] = 79;
		table[4][1] = 87;
		table[5][1] = 154;
		table[6][1] = 87;
		table[7][1] = 113;
		
		table[0][2] = 100;
		table[1][2] = 85;
		table[2][2] = 116;
		table[3][2] = 79;
		table[4][2] = 70;
		table[5][2] = 87;
		table[6][2] = 86;
		table[7][2] = 196;
		
		table[0][3] = 136;
		table[1][3] = 69;
		table[2][3] = 87;
		table[3][3] = 200;
		table[4][3] = 79;
		table[5][3] = 71;
		table[6][3] = 117;
		table[7][3] = 96;
		
		table[0][4] = 161;
		table[1][4] = 70;
		table[2][4] = 87;
		table[3][4] = 200;
		table[4][4] = 103;
		table[5][4] = 71;
		table[6][4] = 96;
		table[7][4] = 113;
		
		table[0][5] = 161;
		table[1][5] = 123;
		table[2][5] = 147;
		table[3][5] = 133;
		table[4][5] = 113;
		table[5][5] = 113;
		table[6][5] = 85;
		table[7][5] = 161;
		
		table[0][6] = 146;
		table[1][6] = 147;
		table[2][6] = 175;
		table[3][6] = 100;
		table[4][6] = 103;
		table[5][6] = 103;
		table[6][6] = 163;
		table[7][6] = 187;
		
		table[0][7] = 156;
		table[1][7] = 146;
		table[2][7] = 189;
		table[3][7] = 70;
		table[4][7] = 113;
		table[5][7] = 161;
		table[6][7] = 163;
		table[7][7] = 197;
		
		return table;
	}
	
	public static double[][] getLect08page11_Bottom_SampleBlock() {
		double[][] table = new double[8][8];
		
		table[0][0] = 178;
		table[1][0] = 187;
		table[2][0] = 183;
		table[3][0] = 175;
		table[4][0] = 178;
		table[5][0] = 177;
		table[6][0] = 150;
		table[7][0] = 183;
		
		table[0][1] = 191;
		table[1][1] = 174;
		table[2][1] = 171;
		table[3][1] = 182;
		table[4][1] = 176;
		table[5][1] = 171;
		table[6][1] = 170;
		table[7][1] = 188;
		
		table[0][2] = 199;
		table[1][2] = 153;
		table[2][2] = 128;
		table[3][2] = 177;
		table[4][2] = 171;
		table[5][2] = 167;
		table[6][2] = 173;
		table[7][2] = 183;
		
		table[0][3] = 195;
		table[1][3] = 178;
		table[2][3] = 158;
		table[3][3] = 167;
		table[4][3] = 167;
		table[5][3] = 165;
		table[6][3] = 166;
		table[7][3] = 177;
		
		table[0][4] = 190;
		table[1][4] = 186;
		table[2][4] = 158;
		table[3][4] = 155;
		table[4][4] = 159;
		table[5][4] = 164;
		table[6][4] = 158;
		table[7][4] = 178;
		
		table[0][5] = 194;
		table[1][5] = 184;
		table[2][5] = 137;
		table[3][5] = 148;
		table[4][5] = 157;
		table[5][5] = 158;
		table[6][5] = 150;
		table[7][5] = 173;
		
		table[0][6] = 200;
		table[1][6] = 194;
		table[2][6] = 148;
		table[3][6] = 151;
		table[4][6] = 161;
		table[5][6] = 155;
		table[6][6] = 148;
		table[7][6] = 167;
		
		table[0][7] = 200;
		table[1][7] = 195;
		table[2][7] = 172;
		table[3][7] = 159;
		table[4][7] = 159;
		table[5][7] = 152;
		table[6][7] = 156;
		table[7][7] = 154;
		
		return table;
	}
	
	
	public static double[][] getLec08_JPEGsupplimentalPage_SampleBlock() {
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
	
	
	public static double[][] getLec08_JPEGsupplimentalPage_QuantizeTable() {
		double[][] table = new double[8][8];
		
		table[0][0] = 16;
		table[1][0] = 11;
		table[2][0] = 10;
		table[3][0] = 16;
		table[4][0] = 24;
		table[5][0] = 40;
		table[6][0] = 51;
		table[7][0] = 61;
		
		table[0][1] = 12;
		table[1][1] = 12;
		table[2][1] = 14;
		table[3][1] = 19;
		table[4][1] = 26;
		table[5][1] = 58;
		table[6][1] = 60;
		table[7][1] = 55;
		
		table[0][2] = 14;
		table[1][2] = 13;
		table[2][2] = 16;
		table[3][2] = 24;
		table[4][2] = 40;
		table[5][2] = 57;
		table[6][2] = 69;
		table[7][2] = 56;
		
		table[0][3] = 14;
		table[1][3] = 17;
		table[2][3] = 22;
		table[3][3] = 29;
		table[4][3] = 51;
		table[5][3] = 87;
		table[6][3] = 80;
		table[7][3] = 62;
		
		table[0][4] = 18;
		table[1][4] = 22;
		table[2][4] = 37;
		table[3][4] = 56;
		table[4][4] = 68;
		table[5][4] = 109;
		table[6][4] = 103;
		table[7][4] = 77;
		
		table[0][5] = 24;
		table[1][5] = 35;
		table[2][5] = 55;
		table[3][5] = 64;
		table[4][5] = 81;
		table[5][5] = 104;
		table[6][5] = 113;
		table[7][5] = 92;
		
		table[0][6] = 49;
		table[1][6] = 64;
		table[2][6] = 78;
		table[3][6] = 87;
		table[4][6] = 103;
		table[5][6] = 121;
		table[6][6] = 120;
		table[7][6] = 101;
		
		table[0][7] = 72;
		table[1][7] = 92;
		table[2][7] = 95;
		table[3][7] = 98;
		table[4][7] = 112;
		table[5][7] = 100;
		table[6][7] = 103;
		table[7][7] = 99; 
		
		return table;
	}
	
	private static double[][] getRedblack_YCbCr() {
		double[][] table = new double[8][8];
		
		table[0][0] = -51.755;
		table[1][0] = -51.755;
		table[2][0] = -128.0;
		table[3][0] = -128.0;
		table[4][0] = -51.755;
		table[5][0] = -51.755;
		table[6][0] = -128.0;
		table[7][0] = -128.0;

		table[0][1] = -51.755;
		table[1][1] = -51.755;
		table[2][1] = -128.0;
		table[3][1] = -128.0;
		table[4][1] = -51.755;
		table[5][1] = -51.755;
		table[6][1] = -128.0;
		table[7][1] = -128.0;

		table[0][2] = -51.755;
		table[1][2] = -51.755;
		table[2][2] = -128.0;
		table[3][2] = -128.0;
		table[4][2] = -51.755;
		table[5][2] = -51.755;
		table[6][2] = -128.0;
		table[7][2] = -128.0;

		table[0][3] = -51.755;
		table[1][3] = -51.755;
		table[2][3] = -128.0;
		table[3][3] = -128.0;
		table[4][3] = -51.755;
		table[5][3] = -51.755;
		table[6][3] = -128.0;
		table[7][3] = -128.0;

		table[0][4] = -51.755;
		table[1][4] = -51.755;
		table[2][4] = -128.0;
		table[3][4] = -128.0;
		table[4][4] = -51.755;
		table[5][4] = -51.755;
		table[6][4] = -128.0;
		table[7][4] = -128.0;

		table[0][5] = -51.755;
		table[1][5] = -51.755;
		table[2][5] = -128.0;
		table[3][5] = -128.0;
		table[4][5] = -51.755;
		table[5][5] = -51.755;
		table[6][5] = -128.0;
		table[7][5] = -128.0;

		table[0][6] = -51.755;
		table[1][6] = -51.755;
		table[2][6] = -128.0;
		table[3][6] = -128.0;
		table[4][6] = -51.755;
		table[5][6] = -51.755;
		table[6][6] = -128.0;
		table[7][6] = -128.0;

		table[0][7] = -51.755;
		table[1][7] = -51.755;
		table[2][7] = -128.0;
		table[3][7] = -128.0;
		table[4][7] = -51.755;
		table[5][7] = -51.755;
		table[6][7] = -128.0;
		table[7][7] = -128.0;
		
		return table;
	}
	
	public static double[][] getAllBlack() {
		double[][] table = new double[8][8];
		for(int y = 0; y < 8; y++){
			for(int x = 0; x < 8; x++){
				table[x][y] = 255.0;
			}
		}
		
		return table;
		}
	
	public static double[][] getBlankTable() {
		double[][] table = new double[8][8];
		
//		table[0][0] = ;
//		table[1][0] = ;
//		table[2][0] = ;
//		table[3][0] = ;
//		table[4][0] = ;
//		table[5][0] = ;
//		table[6][0] = ;
//		table[7][0] = ;
//		
//		table[0][1] = ;
//		table[1][1] = ;
//		table[2][1] = ;
//		table[3][1] = ;
//		table[4][1] = ;
//		table[5][1] = ;
//		table[6][1] = ;
//		table[7][1] = ;
//		
//		table[0][2] = ;
//		table[1][2] = ;
//		table[2][2] = ;
//		table[3][2] = ;
//		table[4][2] = ;
//		table[5][2] = ;
//		table[6][2] = ;
//		table[7][2] = ;
//		
//		table[0][3] = ;
//		table[1][3] = ;
//		table[2][3] = ;
//		table[3][3] = ;
//		table[4][3] = ;
//		table[5][3] = ;
//		table[6][3] = ;
//		table[7][3] = ;
//		
//		table[0][4] = ;
//		table[1][4] = ;
//		table[2][4] = ;
//		table[3][4] = ;
//		table[4][4] = ;
//		table[5][4] = ;
//		table[6][4] = ;
//		table[7][4] = ;
//		
//		table[0][5] = ;
//		table[1][5] = ;
//		table[2][5] = ;
//		table[3][5] = ;
//		table[4][5] = ;
//		table[5][5] = ;
//		table[6][5] = ;
//		table[7][5] = ;
//		
//		table[0][6] = ;
//		table[1][6] = ;
//		table[2][6] = ;
//		table[3][6] = ;
//		table[4][6] = ;
//		table[5][6] = ;
//		table[6][6] = ;
//		table[7][6] = ;
//		
//		table[0][7] = ;
//		table[1][7] = ;
//		table[2][7] = ;
//		table[3][7] = ;
//		table[4][7] = ;
//		table[5][7] = ;
//		table[6][7] = ;
//		table[7][7] = ;
		
		return table;
	}

}
