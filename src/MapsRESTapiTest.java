import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import com.waataja.fooddrop.LatGetter;

public class MapsRESTapiTest {

	public static void main(String[] args) {
	System.out.println(Arrays.toString(LatGetter.getLatLong("5733 33rd ave NE 98105 Seattle, WA")));
		

	}

}