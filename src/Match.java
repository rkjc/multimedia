
public class Match {

	private double val;
	private Pair coord;
	
	public Match(){
		this.val = 1000.0; // pixelDiff
		this.coord = new Pair(0, 0);
	}
	
	public Match(double val, Pair coord){
		this.val = val;
		this.coord = coord;
	}
	
	public double getPixelDiff(){
		return val;
	}
	
	public Pair getCoord(){
		return coord;
	}
	
	public void putIfSmaller(double pixelDiff, Pair loc) {
		if(pixelDiff < val){
			val = pixelDiff;
			coord = loc;
		}
		else if(pixelDiff == val) {
			double dist1 = Math.sqrt(Math.pow(loc.getX(), 2) + Math.pow(loc.getY(), 2));
			double dist2 = Math.sqrt(Math.pow(coord.getX(), 2) + Math.pow(coord.getY(), 2));
			if(dist1 < dist2){
				coord = loc;
			}		
		}		
	}
	
}
