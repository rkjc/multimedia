import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CompressionUtilities {

	public static HashMap<Integer, String> newDictLZW(String inMsg) {
		HashMap<Integer, String> dict = new HashMap<Integer, String>();
		int j = 0;

		for (int i = 0; i < inMsg.length(); i++) {
			if (dict.containsValue(Character.toString(inMsg.charAt(i))) == false
					&& inMsg.charAt(i) != '\n') {
				dict.put((Integer) j, Character.toString(inMsg.charAt(i)));
				j++;
			}
		}
		return dict;
	}

	public static boolean encodeLZW(String inText, int dictSize, int[] encodedData) {
		int[] returnData = {0};
		try{
		encodedData = new int[inText.length()];
		int outCount = 0;
		HashMap<Integer, String> dict = newDictLZW(inText);

		String teststring = "";
		for (int i = 0; i < inText.length() - 1; i++) {
			teststring = String.valueOf(inText.charAt(i));
			String prevstring = teststring;
			int j = 0;
			// System.out.println("teststring = " + teststring +
			// ";  prevstring= " + prevstring + ";  i= " + i);
			while (dict.containsValue(teststring)
					&& i + j < inText.length() - 1) {
				prevstring = teststring;
				j++;
				teststring = teststring.concat(String.valueOf(inText.charAt(i
						+ j)));
			}
			// get key value for the string that matched
			Integer mapIndex = (getKeyByValue(dict, prevstring));

			// add this key to the encoded outText as string (ASCII 8 bit) outText =
			// outText.concat(String.valueOf(((char)Integer.parseInt(mapIndex))));
			
				encodedData[outCount] = mapIndex;
			
			outCount++;
			// add to dictionary the string that matched plus the next char in
			// sequence in inText
			// test that the new dictionary entry is not in the dictionary and
			// does not contain the new-line character
			if (!dict.containsValue(teststring)
					&& teststring.charAt(teststring.length() - 1) != '\n'
					&& dict.size() < dictSize)
				dict.put(dict.size(), teststring);

			// set index value i to point to that next char in sequence
			i = i + j - 1;
		}
		// TODO add switch to turn printing on and off
		System.out.println("\n-- Dictionary --");
		printDictionary(dict);

		returnData = new int[outCount]; // count entries and use here.
		for (int i = 0; i < outCount; i++) {
			returnData[i] = encodedData[i];
		}
		encodedData = returnData;
		} catch (Exception ex) {
			System.err.println("---------------------------------------------\nencodeLZW() method failed\nprobably tried to encode an incompatable file\n---------------------------------------------");
			return false;
		}
		return true;		
	}

	public static String decodeLZW(int[] encodedText, HashMap<Integer, String> dict, int dictSize) {
		String decodedText = "";

		for (int i = 0; i < encodedText.length; i++) {
			String nextString = "";
			// gets current character or string from dict using encoded index
			// value at position i
			String currentEntry = dict.get(encodedText[i]);

			// adds this entry to the decoded text
			decodedText = decodedText.concat(currentEntry);

			// at least one more index in encoded message AND dict is not full
			if (i + 1 < encodedText.length && dict.size() < dictSize) {
				// the dictionary index that will be called next exists
				if (encodedText[i + 1] < dict.size())
					nextString = dict.get(encodedText[i + 1]);
				else
					nextString = currentEntry;

				String nextChar = String.valueOf(nextString.charAt(0));

				// test to see if this new char set is in dictionary - if not
				// add it.
				if (!dict.containsValue(currentEntry.concat(nextChar))) {
					dict.put(dict.size(), currentEntry.concat(nextChar));
				}
			}
		}
		return decodedText;
	}

	// calculate compression ratio
	public static Double calcCompressRatio(int dictStartSize, HashMap<Integer, String> dict, String origData, int[] encodedData){
		double calcBase2 = Math.log10(dict.size()) / Math.log10(2);
		int numbBitsNeeded = (int)Math.ceil(calcBase2);
		int bitsInput = (origData.length() - 1) * 8;	//assumption of 8-bits/char in original data
		int bitsOut = encodedData.length * numbBitsNeeded; // + dictStartSize * 8;	
		return (double)bitsInput / (double)bitsOut;
	}
	
	public static Double calcCompressRatioInclStartDict(int dictStartSize, HashMap<Integer, String> dict, String origData, int[] encodedData){
		double calcBase2 = Math.log10(dict.size()) / Math.log10(2);
		int numbBitsNeeded = (int)Math.ceil(calcBase2);
		int bitsInput = (origData.length() - 1) * 8;	//assumption of 8-bits/char in original data
		int bitsOut = encodedData.length * numbBitsNeeded + dictStartSize * 8;	
		return (double)bitsInput / (double)bitsOut;
	}
	
	public static void printDictionary(HashMap<Integer, String> dict) {
		System.out.println("Index\t\tEntry\n---------------------");
		for (int i = 0; i < dict.size(); i++) {
			System.out.println(i + "\t\t" + dict.get(i));
		}
	}

	public static Integer getKeyByValue(Map<Integer, String> map, String value) {
		for (Entry<Integer, String> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static void printIntArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println("");
	}

}
