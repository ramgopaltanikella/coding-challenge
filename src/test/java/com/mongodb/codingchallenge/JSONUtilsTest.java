package com.mongodb.codingchallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONUtilsTest {

	@ParameterizedTest(name = "#{index} - Test Valid JSON")
	@MethodSource("validJsonAndExpectedFlatJsonProvider")
	public void testValidJSON(String json, String expectedFlatJson) {
		try {
			final String flattened = JSONUtils.flatten(json);
			final JsonObject jsonObject = JsonParser.parseString(flattened).getAsJsonObject();
			final JsonObject expectedJsonObject = JsonParser.parseString(expectedFlatJson).getAsJsonObject();
			assertEquals(expectedJsonObject, jsonObject);
		} catch (final RuntimeException rte) {
			fail(rte.getMessage());
		}
	}

	@Test
	public void testInvalidJSON() {
		RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
			JsonParser.parseString(JSONUtils.flatten("{")).getAsJsonObject();
		}, "RuntimeException was expected");

		assertTrue(thrown.getMessage().contains("EOFException"));
	}
	
	@Test
	public void testEmptyJSON() {
		RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
			JsonParser.parseString(JSONUtils.flatten("")).getAsJsonObject();
		}, "RuntimeException was expected");

		assertTrue(thrown.getMessage().contains("Not a JSON Object"));
	}
	
	@Test
	public void testNullJSON() {
		RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
			JsonParser.parseString(JSONUtils.flatten(null)).getAsJsonObject();
		}, "RuntimeException was expected");

		assertTrue(thrown instanceof NullPointerException);
	}

	private static Stream<Arguments> validJsonAndExpectedFlatJsonProvider() {
		final String VALID_JSON_1 = "{\"a\": 1, \"b\": true, \"c\": {\"d\": 3,\"e\": \"test\"}}";
		final String EXPECTED_FLAT_JSON_1 = "{\"a\": 1, \"b\": true, \"c.d\": 3, \"c.e\": \"test\"}";

		final String VALID_JSON_2 = "{\n"
				+ "  \"id\": 7,\n"
				+ "  \"name\": \"John Doe\",\n"
				+ "  \"age\": 22,\n"
				+ "  \"hobbies\": {\n"
				+ "        \"indoor\": \"Chess\",\n"
				+ "        \"outdoor\": \"BasketballStand-up Comedy\"\n"
				+ "    }\n"
				+ "}";
		final String EXPECTED_FLAT_JSON_2 = "{\n"
				+ "  \"id\": 7,\n"
				+ "  \"name\": \"John Doe\",\n"
				+ "  \"age\": 22,\n"
				+ "  \"hobbies.indoor\": \"Chess\",\n"
				+ "  \"hobbies.outdoor\": \"BasketballStand-up Comedy\"\n"
				+ "}";

		// Minimum valid JSON object
		final String VALID_JSON_3 = "{}";
		final String EXPECTED_FLAT_JSON_3 = VALID_JSON_3;
		
		// W/ null value, no nesting
		final String VALID_JSON_4 = "{\"name\":\"John\", \"age\":30, \"car\":null}";
		// We have chosen the default option in gson library of not serializing nulls, so "car" is gone!
		final String EXPECTED_FLAT_JSON_4 = "{\"name\":\"John\", \"age\":30}";
		
		// W/O null value, no nesting
		final String VALID_JSON_5 = "{\"id\": 1,\"name\": \"Zaragoza\",\"country\": \"Spain\",\"population\": 680000}";
		final String EXPECTED_FLAT_JSON_5 = VALID_JSON_5;
		
		// Deep nesting
		final String VALID_JSON_6 = "{\n"
				+ "  \"a\": {\n"
				+ "        \"b\": {\n"
				+ "            \"c\": {\n"
				+ "                \"d\": {\n"
				+ "                    \"e\": {\n"
				+ "                        \"f\": {\n"
				+ "                            \"g\": {\n"
				+ "                                \"h\": {\n"
				+ "                                    \"i\": {\n"
				+ "                                        \"j\": {\n"
				+ "                                            \"k\": {\n"
				+ "                                                 \"l\": {\n"
				+ "                                                     \"m\": {\n"
				+ "                                                          \"n\": {\n"
				+ "                                                              \"o\": {\n"
				+ "                                                                  \"p\": {\n"
				+ "                                                                      \"q\": {\n"
				+ "                                                                          \"r\": {\n"
				+ "                                                                              \"s\": {\n"
				+ "                                                                                  \"t\": {\n"
				+ "                                                                                      \"u\": {\n"
				+ "                                                                                          \"v\": {\n"
				+ "                                                                                              \"w\": {\n"
				+ "                                                                                                  \"x\": {\n"
				+ "                                                                                                      \"y\": {\n"
				+ "                                                                                                          \"z\": \"Gotcha!!!\"\n"
				+ "                                                                                                      }\n"
				+ "                                                                                                  }\n"
				+ "                                                                                              }\n"
				+ "                                                                                          }\n"
				+ "                                                                                      }\n"
				+ "                                                                                  }\n"
				+ "                                                                              }\n"
				+ "                                                                          }\n"
				+ "                                                                      }\n"
				+ "                                                                  }\n"
				+ "                                                              }\n"
				+ "                                                          }\n"
				+ "                                                     }\n"
				+ "                                                 }\n"
				+ "                                            }\n"
				+ "                                        }\n"
				+ "                                    }\n"
				+ "                                }\n"
				+ "                            }\n"
				+ "                        }\n"
				+ "                    }\n"
				+ "                }\n"
				+ "            }\n"
				+ "        }\n"
				+ "    }\n"
				+ "}";
		final String EXPECTED_FLAT_JSON_6 = "{\"a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q.r.s.t.u.v.w.x.y.z\":\"Gotcha!!!\"}";
		
		// Deep nesting, w/ null
		final String VALID_JSON_7 = "{\n"
				+ "  \"a\": {\n"
				+ "        \"b\": {\n"
				+ "            \"c\": {\n"
				+ "                \"d\": {\n"
				+ "                    \"e\": {\n"
				+ "                        \"f\": {\n"
				+ "                            \"g\": {\n"
				+ "                                \"h\": {\n"
				+ "                                    \"i\": {\n"
				+ "                                        \"j\": {\n"
				+ "                                            \"k\": {\n"
				+ "                                                 \"l\": {\n"
				+ "                                                     \"m\": {\n"
				+ "                                                          \"n\": {\n"
				+ "                                                              \"o\": {\n"
				+ "                                                                  \"p\": {\n"
				+ "                                                                      \"q\": {\n"
				+ "                                                                          \"r\": {\n"
				+ "                                                                              \"s\": {\n"
				+ "                                                                                  \"t\": {\n"
				+ "                                                                                      \"u\": {\n"
				+ "                                                                                          \"v\": {\n"
				+ "                                                                                              \"w\": {\n"
				+ "                                                                                                  \"x\": {\n"
				+ "                                                                                                      \"y\": {\n"
				+ "                                                                                                          \"z\": null\n"
				+ "                                                                                                      }\n"
				+ "                                                                                                  }\n"
				+ "                                                                                              }\n"
				+ "                                                                                          }\n"
				+ "                                                                                      }\n"
				+ "                                                                                  }\n"
				+ "                                                                              }\n"
				+ "                                                                          }\n"
				+ "                                                                      }\n"
				+ "                                                                  }\n"
				+ "                                                              }\n"
				+ "                                                          }\n"
				+ "                                                     }\n"
				+ "                                                 }\n"
				+ "                                            }\n"
				+ "                                        }\n"
				+ "                                    }\n"
				+ "                                }\n"
				+ "                            }\n"
				+ "                        }\n"
				+ "                    }\n"
				+ "                }\n"
				+ "            }\n"
				+ "        }\n"
				+ "    }\n"
				+ "}";
		final String EXPECTED_FLAT_JSON_7 = "{}";

		return Stream.of(Arguments.of(VALID_JSON_1, EXPECTED_FLAT_JSON_1),
				Arguments.of(VALID_JSON_2, EXPECTED_FLAT_JSON_2),
				Arguments.of(VALID_JSON_3, EXPECTED_FLAT_JSON_3),
				Arguments.of(VALID_JSON_4, EXPECTED_FLAT_JSON_4),
				Arguments.of(VALID_JSON_5, EXPECTED_FLAT_JSON_5),
				Arguments.of(VALID_JSON_6, EXPECTED_FLAT_JSON_6),
				Arguments.of(VALID_JSON_7, EXPECTED_FLAT_JSON_7));
	}
}
