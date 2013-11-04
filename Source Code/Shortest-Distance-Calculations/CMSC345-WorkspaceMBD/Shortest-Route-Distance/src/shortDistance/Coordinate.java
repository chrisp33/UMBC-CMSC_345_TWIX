package shortDistance;

/**
 * @author Mariama Barr-Dallas & Michael Tang
 *
 */
public class Coordinate {

	/**
	 * Default value for longitude and latitude
	 */
	private static final double DEFAULT_VALUE = 0;
	
	/**
	 * Label data member of Coordinate
	 */
	private String label;
	protected void setLabel(String label){this.label = label;}
	
	
	/**
	 * Longitude 
	 */
	private double x;
	protected void setLong(double x){this.x = x;}
	
	
	/**
	 * Latitude
	 */
	private double y;
	protected void setLat(double y){this.y = y;}
	
	
	/**
	 * @category Constructor
	 * @param label
	 * @param x
	 * @param y
	 * 
	 * This is the constructor for the Coordinate Object
	 * which takes in the latitude and longitude (x & y respectively)
	 *  
	 */
	public Coordinate(String label, double x, double y){
		
		this.label = label;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * This is a no-parameter constructor for the Coordinate class which sets
	 * the longitude and latitude to DEFAULT_VALUE
	 * Note: the default value for the label is 'null'
	 */
	public Coordinate(){
		
		this.x = DEFAULT_VALUE;
		this.y = DEFAULT_VALUE;
	}
	
	
	
	
}
