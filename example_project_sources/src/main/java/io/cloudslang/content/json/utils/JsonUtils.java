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


package io.cloudslang.content.json.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.json.utils.JsonExceptionValues.INVALID_JSONOBJECT;
import static io.cloudslang.content.json.utils.JsonExceptionValues.INVALID_JSONPATH;

public class JsonUtils {

    public static JsonPath getValidJsonPath(final String jsonPath) {
        try {
            return JsonPath.compile(jsonPath);
        } catch (IllegalArgumentException iae) {
            throw hammerIllegalArgumentExceptionWithMessage(INVALID_JSONPATH, iae);
        }
    }

    @NotNull
    public static JsonContext getValidJsonContext(final String jsonObject) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            final AbstractJsonProvider provider = new JacksonJsonNodeJsonProvider(objectMapper);
            final Configuration configuration = Configuration.defaultConfiguration()
                    .jsonProvider(provider);
            final JsonContext jsonContext = new JsonContext(configuration);
            jsonContext.parse(jsonObject);
            return jsonContext;
        } catch (IllegalArgumentException iae) {
            throw hammerIllegalArgumentExceptionWithMessage(INVALID_JSONOBJECT, iae);
        }
    }

    @NotNull
    public static IllegalArgumentException hammerIllegalArgumentExceptionWithMessage(@NotNull final String message, @NotNull final Throwable throwable) {
        final IllegalArgumentException iae = new IllegalArgumentException(message);
        iae.setStackTrace(throwable.getStackTrace());
        return iae;
    }
}
