import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;


public class InOutUtilities {
	
	public static File getH4fileInput() {	
		System.out.println("\nPlease enter File Name or 3 digit number between 001 and 200 for HW-4 image file \"Walk_xxx.ppm\"");
		
		try {
			InputStreamReader inreader = new InputStreamReader(System.in);
			BufferedReader inName = new BufferedReader(inreader);
			String input = inName.readLine();
			
			// Check for various file structures
			File checkFolder;
			boolean atHome = false;
			checkFolder = new File("../data/IDB");
				atHome = checkFolder.exists();
				
			boolean subIDB = false;	
			checkFolder = new File("IDB");
				subIDB = checkFolder.exists();
			
			if(input.length() == 3 && isInteger(input)){
				//StringBuilder fileName = new StringBuilder("Walk_");
				StringBuilder fileName;
				if(atHome){
					fileName = new StringBuilder("../data/IDB/Walk_");
				} else if(subIDB) {
					fileName = new StringBuilder("IDB/Walk_");
				} else {
					fileName = new StringBuilder("Walk_");
				}
				fileName.append(input);
				fileName.append(".ppm");
				File file =  new File(fileName.toString());
				
				if(file.isFile()){
					return file;
				} else {
					System.out.println("sorry, could not find that file");
					return null;
				}
			} else {
				File file = new File(input);
				if(file.isFile()){
					return file;
				} else {
					System.out.println("sorry, could not find that file");
					return null;
				}
			}		
		} catch (IOException err) {
			System.out.println("error reading line in HW4 file input selection");
		}
		return null;
	}
	
	public static boolean getH4RemovefileInput(File[] targetFile, File[] referenceFile, File[] replacementFile) {	
		System.out.println("\nPlease a 3 digit number between 001 and 200 for HW-4 image file \"Walk_xxx.ppm\"");
		
		try {
			InputStreamReader inreader = new InputStreamReader(System.in);
			BufferedReader inName = new BufferedReader(inreader);
			String input = inName.readLine();

			// Check for various file structures
			File checkFolder;
			boolean atHome = false;
			checkFolder = new File("../data/IDB");
				atHome = checkFolder.exists();
				
			boolean subIDB = false;	
			checkFolder = new File("IDB");
				subIDB = checkFolder.exists();
			
			if(input.length() == 3 && isInteger(input)){
				
				StringBuilder stringName;
				if(atHome){
					stringName = new StringBuilder("../data/IDB/Walk_");
				} else if(subIDB) {
					stringName = new StringBuilder("IDB/Walk_");
				} else {
					stringName = new StringBuilder("Walk_");
				}
				
				// Get the Target File
				StringBuilder targetName = stringName;
				targetName.append(input);
				targetName.append(".ppm");
				targetFile[0] =  new File(targetName.toString());
				
				// Get the Reference File
				StringBuilder referenceName  = stringName;
				 // Need to append exactly 3 characters
				String formatted = String.format("%03d", (Integer.parseInt(input) - 2));
				referenceName.append(formatted);						
				referenceName.append(".ppm");
				referenceFile[0] =  new File(referenceName.toString());
				
				// Get the Replacement File
				StringBuilder replacementName = stringName;
				replacementName.append("Walk_005.ppm");
				replacementFile[0] =  new File(replacementName.toString());
				
				// Check that all the Files are there
				if(targetFile[0].isFile() && referenceFile[0].isFile() && replacementFile[0].isFile()){
					return true;
				} else {
					System.out.println("sorry, could not find at least one of those files");
					return false;
				}
			} else {		
				System.out.println("sorry, there is a problem with your input");
				return false;
			}		
		} catch (IOException err) {
			System.out.println("error reading line in HW4 file input selection");
		}
		return false;
	}
	

	public static File getUserFileSelection() {
		File file = new File("");
	
		System.out.println("");
		System.out.println("----- please select the method for file entry -----");

		while(!file.isFile()){
			System.out.println("1. input file name directly");
			System.out.println("2. use java file chooser GUI");
			System.out.println("3. return to main menu");
			
			int select = readInputInteger();
			
			switch (select) {
			case 0:
				break;
			case 1:
				System.out.println("** Please enter name of file to encode **");
				try {
					InputStreamReader inreader = new InputStreamReader(System.in);
					BufferedReader inName = new BufferedReader(inreader);
					file = new File(inName.readLine());
				} catch (IOException err) {
					System.out.println("error reading line in file name input");
				}
				break;
			case 2:
				JFileChooser fc = new JFileChooser();
				System.out.println("Use file chooser dialog box to select input file.\n(dialog box may be hidden behind IDE when first run)");
				File currentDir = new File(System.getProperty("user.dir"));
				fc.setCurrentDirectory(currentDir);
				int returnVal = fc.showOpenDialog(null);
				 if (returnVal == JFileChooser.APPROVE_OPTION) {
		             file = fc.getSelectedFile();
		             System.out.println("Opening: " + file.getAbsolutePath());
		         } else {
		        	 System.out.println("Open command cancelled by user.");
		         }
				break;
			case 3:
				return new File("");
			}//end of file select switch
			if(!file.isFile()){
				System.out.println("can not find that file, please try again");
			}		
		}//while not file exists
		return file;
	}
	
	
	public static void writeMotionVectorsFile(Pair[][] motionVectors, File targetFile, File referenceFile) {
		int macroBlock_X = motionVectors.length;
		int macroBlock_Y = motionVectors[0].length;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter( new FileWriter( "mv.txt"));
			writer.write("# Name: Richard Cross");
			writer.newLine();
			writer.write("# Motion Vectors");
			writer.newLine();
			writer.write("# Target image name: " + targetFile.getName());
			writer.newLine();
			writer.write("# Reference image name: " + referenceFile.getName());
			writer.newLine();
			writer.write("# Number of target macro blocks: " + macroBlock_X + " x " + macroBlock_Y + " (image size is " + macroBlock_X*16 + " x " + macroBlock_Y*16 + ")");
			writer.newLine();
			writer.newLine();
			for(int y = 0; y < macroBlock_Y; y++){
				for(int x = 0; x < macroBlock_X; x++){
					writer.write("[" + String.format("%3d", (motionVectors[x][y].getX())) + ", " + String.format("%3d", (motionVectors[x][y].getY())) + "]  ");	
				}
				writer.newLine();
				writer.newLine();
			}
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
	
	public static void writeMotionVectorsFileHalfPixel(Pair[][] motionVectors, File targetFile, File referenceFile) {
		int macroBlock_X = motionVectors.length;
		int macroBlock_Y = motionVectors[0].length;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter( new FileWriter( "mv.txt"));
			writer.write("# Name: Richard Cross");
			writer.newLine();
			writer.write("# Motion Vectors - Half Pixel Method");
			writer.newLine();
			writer.write("# Target image name: " + targetFile.getName());
			writer.newLine();
			writer.write("# Reference image name: " + referenceFile.getName());
			writer.newLine();
			writer.write("# Number of target macro blocks: " + macroBlock_X + " x " + macroBlock_Y + " (image size is " + macroBlock_X*16 + " x " + macroBlock_Y*16 + ")");
			writer.newLine();
			writer.newLine();
			for(int y = 0; y < macroBlock_Y; y++){
				for(int x = 0; x < macroBlock_X; x++){
					writer.write("[" + String.format("%5.1f", (((double)motionVectors[x][y].getX()) / 2.0)) + ", " + String.format("%5.1f", (((double)motionVectors[x][y].getY()) / 2.0)) + "]  ");	
				}
				writer.newLine();
				writer.newLine();
			}
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
	
	
	public static void printMotionVectors(Pair[][] motionVectors, File targetFile, File referenceFile) {
		printMotionVectorsHeader(motionVectors, targetFile, referenceFile);
		printMotionVectors(motionVectors);
	}
	
	public static void printMotionVectorsHeader(Pair[][] motionVectors, File targetFile, File referenceFile) {
		int macroBlock_X = motionVectors.length;
		int macroBlock_Y = motionVectors[0].length;
		
			System.out.println("\n--- Motion Vectors ---");
			//System.out.println("# Name: Richard Cross");
			System.out.println("# Target image name: " + targetFile.getName());
			System.out.println("# Reference image name: " + referenceFile.getName());
			System.out.println("# Number of target macro blocks: " + macroBlock_X + " x " + macroBlock_Y + " (image size is " + macroBlock_X*16 + " x " + macroBlock_Y*16 + ")");
			System.out.println("");		
	}
			
	public static void printMotionVectors(Pair[][] motionVectors){	
		int macroBlock_X = motionVectors.length;
		int macroBlock_Y = motionVectors[0].length;
		for(int y = 0; y < macroBlock_Y; y++){
			for(int x = 0; x < macroBlock_X; x++){
				System.out.print("[" + String.format("%3d", (motionVectors[x][y].getX())) + ", " + String.format("%3d", (motionVectors[x][y].getY())) + "]  ");
			}
			System.out.println("");
		}
	}
	
	public static void printMotionVectorsHalfPixel(Pair[][] motionVectors, File targetFile, File referenceFile) {
		printMotionVectorsHeader(motionVectors, targetFile, referenceFile);
		printMotionVectorsHalfPixel(motionVectors);
	}
	
	public static void printMotionVectorsHalfPixel(Pair[][] motionVectors){	
		int macroBlock_X = motionVectors.length;
		int macroBlock_Y = motionVectors[0].length;
		for(int y = 0; y < macroBlock_Y; y++){
			for(int x = 0; x < macroBlock_X; x++){
				System.out.print("[" + String.format("%5.1f", (((double)motionVectors[x][y].getX()) / 2.0)) + ", " + String.format("%5.1f", (((double)motionVectors[x][y].getY()) / 2.0)) + "]  ");
			}
			System.out.println("");
		}
	}
	
	public static String readFileInput(File file){
		String everything = "";		
		if(file.isFile()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
	
				while (line != null) {
					sb.append(line);
					sb.append("\n");
					line = br.readLine();
				}
				everything = sb.toString();
				br.close();
	
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			System.err.println("could not find file");
		}
		return everything;
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
}
