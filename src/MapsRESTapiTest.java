import java.util.ArrayList;

import com.waataja.fooddrop.LatLong;


public class MapsRESTapiTest {

	public static void main(String[] args) {
	ArrayList<LatLong> l1 = new ArrayList<LatLong>();
	//l1.add(new LatLong("5733 33rd ave NE 98105 Seattle, WA"));
	//l1.add(new LatLong("4103 sunnyside ave N Seattle, WA"));
	//l1.add(new LatLong("1104 N 74th street N Seattle WA"));
	//l1.add(new LatLong("1156 N 76th street Seattle, WA"));
			//System.out.println(Arrays.toString(d));
	//System.out.println(Arrays.toString(d2));
	l1.add(new LatLong(1.0,1.0));
	l1.add(new LatLong(6.0,6.0));
	l1.add(new LatLong(3.0,3.0));
	System.out.println(LatLong.sortAll(new LatLong(0.0,0.0), l1));
		

	}

}