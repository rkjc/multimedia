import java.io.File;




public class MotionUtilities {

	
	
	
	
	
	public static void motionCompensation(File targetImg, File referenceImg) {
		
		
		File file1 = new File("../data/IDB/Walk_060.ppm");
		File file2 = new File("../data/IDB/Walk_057.ppm");
		
		
		Image img1 = new Image(file1.getPath());
		
		Image img2 = new Image(file2.getPath());
		
		img1.display(file1.getName());
		img2.display(file2.getName());
		
		DebugUtilities.imageCompare(img1, img2);
		
	
		
	}


	public static void removeObjects(File targetImg, File referenceImg) {
		File fifthImg = new File ("Walk_005.ppm");
		
		
	}
	
	
}
