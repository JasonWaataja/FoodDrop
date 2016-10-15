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
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.waataja.fooddrop.FoodDonator;
import com.waataja.fooddrop.FoodItem;
import com.waataja.fooddrop.Giveaway;
import com.waataja.fooddrop.Giveaway.GiveawayType;

public class Test {
	public static void main(String... args){
		BigData d = new BigData(new File("/Users/BigBear/Ulysses2/work/testfiles"));
		System.out.println(d.getGiveaways().size());
		FoodItem bread = new FoodItem("Bread", "some yummy bread", 12);
		FoodItem soup = new FoodItem("Soup", "delicious canned soup", 4);
		FoodItem grain = new FoodItem("Grain", "ergot-free rye", 70);
		List<FoodItem> fl = new ArrayList<FoodItem>();
		fl.add(bread);
		fl.add(soup);
		fl.add(grain);
		FoodDonator pcc = new FoodDonator("PCC", "600 N 34th St, Seattle, WA 98103", "Meet me in the back");
		GregorianCalendar start = new GregorianCalendar(2016, 10, 6);
		GregorianCalendar end = new GregorianCalendar(2016, 10, 7);
		Giveaway ng = new Giveaway(pcc, start, end, GiveawayType.PEOPLE, "availability");
		ng.setItems(fl);
		//d.getGiveaways().add(ng);
		System.out.println(d.getGiveaways().get(1).getDonator() == d.getGiveaways().get(0).getDonator());
	}
}
