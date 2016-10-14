package us.noop.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.waataja.fooddrop.FoodDonator;
import com.waataja.fooddrop.Giveaway;
import com.waataja.fooddrop.Giveaway.GiveawayType;

public class BigData {
	
	private File dataDir;
	private JSONParser parser;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy mm dd");
	
	private ArrayList<Giveaway> giveaways;//should be a LinkedHashMap
	
	public BigData(File dataDir){
		if(dataDir == null || (dataDir.exists() && !dataDir.isDirectory())) throw new IllegalArgumentException();
		if(!dataDir.exists()) dataDir.mkdir();
		parser = new JSONParser();
		loadSavedData();
	}
	
	public ArrayList<Giveaway> getGiveaways(){
		return giveaways;
	}
	
	public void loadSavedData(){
		ArrayList<Giveaway> gs = new ArrayList<Giveaway>();
		File[] contents = dataDir.listFiles((File f) -> !f.isDirectory());
		for(File f : contents){
			try{
				gs.add(parseGiveaway(f));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		giveaways = gs;
	}

	public Giveaway parseGiveaway(File f) throws FileNotFoundException {
		try {
			JSONObject dat = (JSONObject) parser.parse(new FileReader(f));
			return new Giveaway(parseDonator((JSONObject) dat.get("donator")), parseDate(dat.get("start")), parseDate(dat.get("end")), parseGiveawayType(dat.get("type")), (String) dat.get("availability"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private GiveawayType parseGiveawayType(Object object) {
		String t = (String) object;
		switch(t){
		case "ANY": return GiveawayType.ANY;
		case "FOODBANK": return GiveawayType.FOODBANK;
		case "PEOPLE": return GiveawayType.PEOPLE;
		default: return null;
		}
	}

	@SuppressWarnings("deprecation")
	private GregorianCalendar parseDate(Object object) {
		try {
			Date d = dateFormat.parse((String) object);
			return new GregorianCalendar(d.getYear() + 1900, d.getMonth(), d.getDay());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public FoodDonator parseDonator(JSONObject donator){
		return new FoodDonator((String) donator.get("name"), (String) donator.get("address"), (String) donator.get("desc"));
	}
}
