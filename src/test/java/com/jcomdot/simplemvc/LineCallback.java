package com.jcomdot.simplemvc;

import java.io.IOException;

public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value) throws IOException;
}
