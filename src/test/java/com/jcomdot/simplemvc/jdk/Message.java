package com.jcomdot.simplemvc.jdk;

public class Message {
	String text;
	
	private Message(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public static Message newMessage(String text) {
		return new Message(text);
	}
}
