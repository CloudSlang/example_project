/*
 * (c) Copyright 2020 Micro Focus
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.cloudslang.content.json.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
import io.cloudslang.content.json.utils.JsonUtils;
import io.cloudslang.content.json.utils.StringUtils;
import net.minidev.json.JSONArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Folea Ilie Cristian on 2/3/2016.
 */
public class JsonService {

    @NotNull
    public static JsonNode evaluateJsonPathQuery(@Nullable final String jsonObject, @Nullable final String jsonPath) {
        final JsonContext jsonContext = JsonUtils.getValidJsonContext(jsonObject);
        final JsonPath path = JsonUtils.getValidJsonPath(jsonPath);
        return jsonContext.read(path);
    }

    private void removeEmptyElementsFromMap(Map<String, Object> json) {
        Set<Map.Entry<String, Object>> jsonElements = json.entrySet();
        Iterator<Map.Entry<String, Object>> jsonElementsIterator = jsonElements.iterator();
        while (jsonElementsIterator.hasNext()) {
            Map.Entry<String, Object> jsonElement = jsonElementsIterator.next();
            Object jsonElementValue = jsonElement.getValue();
            if (StringUtils.isEmpty(jsonElementValue)) {
                jsonElementsIterator.remove();
            } else if (jsonElementValue instanceof JSONArray) {
                if (((JSONArray) jsonElementValue).isEmpty()) {
                    jsonElementsIterator.remove();
                } else {
                    removeEmptyElementFromJsonArray((JSONArray) jsonElementValue);
                }
            } else if (jsonElementValue instanceof LinkedHashMap) {
                if (((LinkedHashMap) jsonElementValue).isEmpty()) {
                    jsonElementsIterator.remove();
                } else {
                    removeEmptyElementsFromMap((Map<String, Object>) jsonElementValue);
                }
            }
        }
    }


    private void removeEmptyElementFromJsonArray(JSONArray jsonArray) {

        Iterator jsonArrayIterator = jsonArray.iterator();
        while (jsonArrayIterator.hasNext()) {
            Object jsonArrayElement = jsonArrayIterator.next();
            if (StringUtils.isEmpty(jsonArrayElement)) {
                jsonArrayIterator.remove();
            } else if (jsonArrayElement instanceof JSONArray) {
                if (((JSONArray) jsonArrayElement).isEmpty()) {
                    jsonArrayIterator.remove();
                } else {
                    removeEmptyElementFromJsonArray((JSONArray) jsonArrayElement);
                }
            } else if (jsonArrayElement instanceof LinkedHashMap) {
                if (((LinkedHashMap) jsonArrayElement).isEmpty()) {
                    jsonArrayIterator.remove();
                } else {
                    removeEmptyElementsFromMap((LinkedHashMap) jsonArrayElement);
                }
            }
        }
    }


    @Contract(pure = true)
    private boolean shouldCharacterBeReplaced(char[] characters, char characterToReplace, int characterPosition) {
        return characters[characterPosition] == characterToReplace &&
                (characterPosition == 0 || characters[characterPosition - 1] != '\\');
    }
}
