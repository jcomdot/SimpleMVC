package springbook.learningtest.spring.web.atmvc;

import org.junit.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class UriComponentsBuilderTest {
	
	@Test
	public void uriCB() {
		UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://example.com/hotels/{hotel}/bookings/{booking}").build();
		System.out.println(uriComponents.expand("42", "21").encode().toString());
		
		int userId = 10;
		int orderId = 20;
		UriComponents uc = UriComponentsBuilder.fromUriString("http://www.myshop.com/users/{user}/orders/{order}").build();
		System.out.println(uc.expand(userId, orderId).encode().toString());
		
		UriComponents uc2 = UriComponentsBuilder.newInstance()
				.scheme("http")
				.host("www.yourshop.com")
				.path("/users/{user}/orders/{order}").build();
		System.out.println(uc2.expand(userId, orderId).encode().toString());
	}

}
