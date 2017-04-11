package com.example.devendranagar.halonotepad;


class Item {
	
	private int id;
	private String title;
	
	Item(int id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

}
