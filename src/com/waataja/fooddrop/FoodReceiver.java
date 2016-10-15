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

package com.waataja.fooddrop;

import java.io.Serializable;

public class FoodReceiver implements Serializable {
	
	public static final long serialVersionUID = 510566894;
	
	public enum ReceiverType {
		PERSON, FOODBANK
	}
	
	private ReceiverType type;
	private LatLong l;
	
	public FoodReceiver(ReceiverType type, double latitude, double longitude) {
		super();
		this.type = type;
		this.l = new LatLong(latitude, longitude);
	}
	
	public ReceiverType getType() {
		return type;
	}
	public void setType(ReceiverType type) {
		this.type = type;
	}
	public double getLatitude() {
		return l.getLat();
	}
	public void setLatitude(double latitude) {
		l.setLat(latitude);
	}
	public double getLongitude() {
		return l.getLon();
	}
	public void setLongitude(double longitude) {
		l.setLon(longitude);
	}
	public LatLong getLatLong() {
		return l;
	}
}
