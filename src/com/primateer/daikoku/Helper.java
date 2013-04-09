package com.primateer.daikoku;

import java.io.IOException;
import java.io.InputStream;

public class Helper {

	public static String loadAsset(String filename) throws IOException {
		InputStream input = Application.getInstance().getAssets()
				.open(filename);
		byte[] buffer = new byte[input.available()];
		input.read(buffer);
		input.close();
		return new String(buffer);
	}
}
