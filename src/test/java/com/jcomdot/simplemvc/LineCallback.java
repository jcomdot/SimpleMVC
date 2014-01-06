package com.jcomdot.simplemvc;

import java.io.IOException;

public interface LineCallback {
	Integer doSomethingWithLine(String line, Integer value) throws IOException;
}
