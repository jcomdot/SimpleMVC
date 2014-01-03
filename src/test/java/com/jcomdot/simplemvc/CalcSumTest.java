package com.jcomdot.simplemvc;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class CalcSumTest {
	@Test
	public void sumOfNumbers() throws IOException {
		Calculator calculator = new Calculator();
		
		int sum = 0;
		try {
			sum = calculator.calcSum(getClass().getResource("/numbers.txt").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(sum, is(10));
	}
}
