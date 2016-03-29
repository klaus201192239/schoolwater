package upload;

import jodd.util.Base64;

public class Base64Util {
	@SuppressWarnings("static-access")
	public static String base64(byte[] target){
		return new Base64().encodeToString(target).replace('+', '-').replace('/', '_');
	}
}
