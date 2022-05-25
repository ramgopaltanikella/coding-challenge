package com.mongodb.codingchallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONUtils {

	private static final Gson GSON = new GsonBuilder().create();

	/**
	 * @param json String representing valid JSON Object
	 * @return String representing flattened version of the JSON object
	 */
	public static String flatten(final String json) {
		final JsonObject flattened = new JsonObject();
		flatten(JsonParser.parseString(json).getAsJsonObject(), flattened, new StringBuilder());
		return GSON.toJson(flattened);
	}

	/**
	 * @param jsonObject Current JSON object being processed
	 * @param flattened Final output that is the flattened version of the original JSON object
	 * @param flattenedKey Buffer to build the compound flattened key
	 * 
	 * Recursive function to flatten the JSON object
	 * This method uses DFS for each of the keys with nested values in the input JSON object,
	 * collecting the compound key in the flattenedKey.
	 * For keys with non-nested values, it will add the current compound flattenedKey to flattened output JSON object.
	 */
	private static void flatten(final JsonObject jsonObject, final JsonObject flattened, final StringBuilder flattenedKey) {
		// Iterate through the keys in the current jsonObject
		for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			// Save the length so we can restore flattenedKey to its original value when recursion is done
			final int originalLength = flattenedKey.length();
			// Add '.' only if it is not the first key being added
			if (originalLength > 0) {
				flattenedKey.append(".");
			}
			// Add this key to the flattenedKey
			flattenedKey.append(entry.getKey());

			final JsonElement value = entry.getValue();
			if (value instanceof JsonObject) {
				// If value is a JSON object, flatten it via recursive call
				flatten((JsonObject) value, flattened, flattenedKey);
			} else { // Otherwise add flattenedKey and value to output JSON
				flattened.add(flattenedKey.toString(), value);
			}
			// Restore the original length as DFS is done on this key-value pair
			flattenedKey.setLength(originalLength);
		}
	}

	/**
	 * @param args ignored
	 * 
	 * Expects a String representing valid JSON Object via stdin
	 * String representing flattened version of the JSON object is output to stdout
	 */
	public static void main(String[] args) {
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); final Scanner scanner = new Scanner(reader)) {
			final StringBuilder json = new StringBuilder();
			// TODO: Basic multi-line now works, but if input has blank line it does not work
			while (scanner.hasNextLine()) {
				final String line = scanner.nextLine();
				if (line == null || line.isEmpty()) {
					break;
				}
				json.append(line);
			}

			System.out.println(flatten(json.toString()));
		} catch (final IOException ioe) {
			System.err.println(ioe.getMessage());
		} catch (final RuntimeException rte) {
			System.err.println("Input is not a valid JSON object");
		}
	}
}
