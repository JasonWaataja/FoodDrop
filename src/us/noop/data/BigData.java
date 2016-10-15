/*
 * Copyright (c) 2016, Jason Waataja
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *    
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *    
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package us.noop.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.waataja.fooddrop.FoodDonator;
import com.waataja.fooddrop.FoodItem;
import com.waataja.fooddrop.Giveaway;
import com.waataja.fooddrop.Giveaway.GiveawayType;
import static java.lang.Math.toIntExact;

public class BigData {
	
	private File dataDir;
	private JSONParser parser;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy mm dd");
	
	private ArrayList<Giveaway> giveaways;//should be a LinkedHashMap
	
	/**
	 * Constructor. Sets up a new data object that loads a file called "saves" in the specified folder.
	 * @param dataDir the directory in which to store things
	 */
	public BigData(File dataDir){
		if(dataDir == null || (dataDir.exists() && !dataDir.isDirectory())) throw new IllegalArgumentException();
		if(!dataDir.exists()) dataDir.mkdir();
		this.dataDir = dataDir;
		parser = new JSONParser();
		loadSavedData();
	}
	
	/**
	 *
	 * @return The ArrayList in which giveaway objects are stored.
	 */
	public ArrayList<Giveaway> getGiveaways(){
		return giveaways;
	}
	
	/**
	 * Loads saved data from the file. If changes have been made since this method was last called, those will be REVERTED.
	 */
	public void loadSavedData(){
		try{
			ArrayList<Giveaway> gs = new ArrayList<Giveaway>();
			File s = new File(dataDir, "save");
			System.out.println(s.getAbsolutePath());
			donators = new HashMap<Integer, ArrayList<FoodDonator>>();
			if(!s.exists()){
				giveaways = gs;
				return;
			}
			JSONArray ar = (JSONArray) ((JSONObject) parser.parse(new FileReader(s))).get("saves");
			Iterator<?> it = ar.iterator();
			while(it.hasNext()){
				try{
					gs.add(parseGiveaway((JSONObject) it.next()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			giveaways = gs;
		}catch(IOException | ParseException e){
			e.printStackTrace();
		}
	}

	private Giveaway parseGiveaway(JSONObject dat) throws FileNotFoundException {
		Giveaway g = new Giveaway(parseDonator((JSONObject) dat.get("donator")), parseDate(dat.get("start")), parseDate(dat.get("end")), parseGiveawayType(dat.get("type")), (String) dat.get("availability"));
		g.setItems(parseItemList((JSONArray) dat.get("items")));
		return g;
	}
	
	private List<FoodItem> parseItemList(JSONArray jsonArray) {
		List<FoodItem> r = new ArrayList<FoodItem>();
		Iterator<?> it = jsonArray.iterator();
		while(it.hasNext()){
			r.add(parseFoodItem((JSONObject) it.next()));
		}
		return r;
	}

	private FoodItem parseFoodItem(JSONObject next) {
		return new FoodItem((String) next.get("name"), (String) next.get("desc"), toIntExact((long) next.get("amount")));
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
	
	private HashMap<Integer, ArrayList<FoodDonator>> donators;
	
	private FoodDonator parseDonator(JSONObject donator){
		String name = (String) donator.get("name");
		String address = (String) donator.get("address");
		String desc = (String) donator.get("desc");
		int hash = (name + address + desc).hashCode();
		FoodDonator ff = new FoodDonator(name, address, desc);
		if(donators.keySet().contains(hash)){
			for(FoodDonator fd : donators.get(hash)){
				if(fd.getAddress().equals(address) && fd.getDescription().equals(desc) && fd.getName().equals(name)){
					return fd;
				}
			}
			donators.get(hash).add(ff);
			return ff;
		}else{
			ArrayList<FoodDonator> fffff = new ArrayList<FoodDonator>();
			fffff.add(ff);
			donators.put(hash, fffff);
			return ff;
		}
	}
	
	/**
	 * Saves the giveaway objects stored within the error.
	 */
	@SuppressWarnings("unchecked")
	public void saveGiveaways(){
		try {
			File saves = new File(dataDir, "save");
			saves.createNewFile();
			JSONArray gives = new JSONArray();
			for(Giveaway g : giveaways){
				gives.add(jsonGiveaway(g));
			}
			JSONObject obj = new JSONObject();
			obj.put("saves", gives);
			FileWriter fw = new FileWriter(saves);
			fw.write(obj.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject jsonGiveaway(Giveaway g) {
		JSONObject ts = new JSONObject();
		ts.put("items", jsonItemList(g.getItems()));
		ts.put("start", dateString(g.getStart()));
		ts.put("end", dateString(g.getEnd()));
		ts.put("availability", g.getAvailability());
		ts.put("donator", jsonDonator(g.getDonator()));
		ts.put("type", jsonGiveawayType(g.getType()));
		return ts;
	}
	
	private String jsonGiveawayType(GiveawayType g) {
		switch(g){
		case PEOPLE:
			return "PEOPLE";
		case FOODBANK:
			return "FOODBANK";
		case ANY:
			return "ANY";
		default:
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject jsonDonator(FoodDonator donator) {
		JSONObject jo = new JSONObject();
		jo.put("name", donator.getName());
		jo.put("address", donator.getAddress());
		jo.put("desc", donator.getDescription());
		return jo;
	}

	@SuppressWarnings("unchecked")
	private JSONArray jsonItemList(List<FoodItem> items) {
		JSONArray r = new JSONArray();
		for(FoodItem f : items){
			r.add(jsonFoodItem(f));
		}
		return r;
	}

	@SuppressWarnings("unchecked")
	private JSONObject jsonFoodItem(FoodItem f) {
		JSONObject jo = new JSONObject();
		jo.put("name", f.getName());
		jo.put("desc", f.getDescription());
		jo.put("amount", f.getAmount());
		return jo;
	}
	
	private String dateString(GregorianCalendar g){
		return g.get(GregorianCalendar.YEAR) + " " + g.get(GregorianCalendar.MONTH) + " " + g.get(GregorianCalendar.DAY_OF_MONTH);
	}
}
