package shared.utils;

import java.io.File;

public class Utils {

	public static boolean isFileValid(String path) {
		return isFileValid(new File(path));
	}

	public static boolean isFileValid(File f) {
		return f.exists() && f.length() > 0;
	}
	
	public static String regularize(String input) {
		
		char[] chars = input.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {
			chars[i] = regularize(chars[i]);
		}	
		return new String(chars);
	}

	public static char regularize(char input) {
		if (input == 9585) {
			return 47;
		}
		else if (input == 12288) {
        return 32;
    }
    else if (input == 12304 || input == 12305) {
    	return (char) (input - 12264);
    }
    else if (input > 65280 && input < 65375) {
        return (char) (input - 65248);
    }
    return input;
	}
}
