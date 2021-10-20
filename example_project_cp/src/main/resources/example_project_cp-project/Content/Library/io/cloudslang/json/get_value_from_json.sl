#   (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
#   All rights reserved. This program and the accompanying materials
#   are made available under the terms of the Apache License v2.0 which accompany this distribution.
#
#   The Apache License is available at
#   http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#
########################################################################################################################
#!!
#! @description: This flow validates the JSON provided in the json_input and then parses the given JSON input to retrieve
#!               the corresponding value addressed by the json_path input.
#!
#! @input json_input: The JSON in the form of a string.
#!                    Example: {'key1': 'value1', 'key2': 'value2'}
#! @input json_path: The JSON path to be executed.
#!                   Example: '$.key1'#!
#!
#! @output return_result: The corresponding value of the key referred to by json_path.
#! @output return_code: 0 if the flow succeeded, -1 otherwise.
#! @output error_message: Error message if there was an error when executing, empty otherwise.
#!
#! @result SUCCESS: Retrieving the value from JSON was successful (return_code == '0').
#! @result FAILURE: Otherwise.
#!!#
########################################################################################################################

namespace: io.cloudslang.base.json

imports:
  json_validation: io.cloudslang.base.json.json_validation
  json_query: io.cloudslang.base.json.json_query

flow:
  name: get_value_from_json

  inputs:
    - json_input
    - json_path
  workflow:
    - json_validation:
        do:
          json_validation:
            - json_input: '${json_input}'
        publish:
          - return_result
          - return_code
          - error_message
        navigate:
          - SUCCESS: json_query
          - FAILURE: on_failure
    - json_query:
        do:
          json_query:
            - json_object: '${json_input}'
            - json_path: '${json_path}'
        publish:
          - return_result
          - return_code
          - exception
        navigate:
          - SUCCESS: SUCCESS
          - FAILURE: on_failure
  outputs:
    - return_result: '${return_result}'
    - return_code: '${return_code}'
    - exception: '${exception}'
  results:
    - FAILURE
    - SUCCESS
extensions:
  graph:
    steps:
      json_validation:
        x: 100
        y: 150
      json_query:
        x: 400
        y: 150
        navigate:
          5ca4819a-fe68-9248-493c-6bb0bd60e282:
            targetId: bf9c10bc-d5a2-184a-7da2-4bba2e04a51f
            port: SUCCESS
    results:
      SUCCESS:
        bf9c10bc-d5a2-184a-7da2-4bba2e04a51f:
          x: 700
          y: 150
