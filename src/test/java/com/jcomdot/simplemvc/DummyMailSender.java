package com.jcomdot.simplemvc;

import java.util.Arrays;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		System.out.println("email내용");
		System.out.println("from: " + simpleMessage.getFrom());
		System.out.println("to: " + Arrays.toString(simpleMessage.getTo()));
		System.out.println("title: " + simpleMessage.getSubject());
		System.out.println("body: " + simpleMessage.getText());
	}

	@Override
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		for (int i=0; i<simpleMessages.length; i++) {
			System.out.println("email내용" + String.valueOf(i));
			System.out.println("from: " + simpleMessages[i].getFrom());
			System.out.println("to: " + Arrays.toString(simpleMessages[i].getTo()));
			System.out.println("title: " + simpleMessages[i].getSubject());
			System.out.println("body: " + simpleMessages[i].getText());
		}
	}

}
