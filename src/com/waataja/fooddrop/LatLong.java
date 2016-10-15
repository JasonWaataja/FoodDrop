package com.waataja.fooddrop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class LatLong implements Serializable {
	
	public static final long serialVersionUID = 739847028;
	
	private double lat;
	private double lon;

	public LatLong(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public LatLong(String address) {
		try {
			URL u = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(" ", "+")
					+ "&key=AIzaSyBHJZR3rqsqZoB7Na1ATfJH9bQEHyARU78");
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			BufferedReader b = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			ArrayList<String> read = new ArrayList<String>();
			while (b.ready()) {
				read.add(b.readLine());
			}
			int line = -1;
			for (String s : read) {
				line++;
				if (s.contains("location")) {
					break;
				}
				System.out.println(s);
			}
			String s1 = read.get(line + 1);
			s1 = s1.substring(s1.indexOf(":") + 2, s1.indexOf(","));
			String s2 = read.get(line + 2);
			s2 = s2.substring(s2.indexOf(":") + 2);

			this.lat = Double.parseDouble(s1);
			this.lon = Double.parseDouble(s2);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String toString() {
		return "latitude: " + lat + " longitude: " + lon;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public static double LatLongDistance(LatLong l1, LatLong l2) {
		Double latDistance = Math.toRadians(l2.getLat() - l1.getLat());
		Double lonDistance = Math.toRadians(l2.getLon() - l1.getLon());
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(l1.getLat()))
				* Math.cos(Math.toRadians(l2.getLat())) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return 6373 * c;

	}

	public static ArrayList<LatLong> sortAll(LatLong p1, ArrayList<LatLong> l1) {
		l1.sort(new Comparator<LatLong>() {

			@Override
			public int compare(LatLong o1, LatLong o2) {
				return (int) (LatLongDistance(p1, o1) - LatLongDistance(p1, o2));

			}

		});
		return l1;

	}
	
	public static ArrayList<Giveaway> sortAll(FoodReceiver f1, ArrayList<Giveaway> al) {
		al.sort(new Comparator<Giveaway>() {

			@Override
			public int compare(Giveaway g1, Giveaway g2) {
				return (int) (LatLongDistance(f1.getLatLong(),new LatLong(g1.getDonator().getAddress()))
						- (LatLongDistance(f1.getLatLong(),new LatLong(g2.getDonator().getAddress()))));

			}
			
		});
		return al;
		
	}

}
