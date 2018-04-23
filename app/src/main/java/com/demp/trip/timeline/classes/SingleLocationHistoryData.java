package com.demp.trip.timeline.classes;

public class SingleLocationHistoryData
{
	public int row_id;
	public String location_name;
	public String location_latitude;
	public String location_longitude;
	public String location_address;
	public String location_city;
	public String location_state;
	public String location_country;
	public String location_pincode;
	public String start_time;
	public String end_time;
	public String saved_date;

	public SingleLocationHistoryData()
	{
		row_id = 0;
		location_name = "";
		location_latitude = "";
		location_longitude = "";
		location_address = "";
		location_city = "";
		location_state = "";
		location_country = "";
		location_pincode = "";
		start_time = "";
		end_time = "";
		saved_date = "";
	}
}
