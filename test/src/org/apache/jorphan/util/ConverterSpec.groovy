/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jorphan.util;

import java.awt.event.ActionEvent

import org.apache.jmeter.junit.spock.JMeterSpec

import spock.lang.IgnoreIf
import spock.lang.Unroll

@Unroll
class ConverterSpec extends JMeterSpec {

    def "convert #value to #type should give [#expected] when value or type is null"() {
        when:
          def converted = Converter.convert(value, type)
        then:
          converted == expected
        where:
          value | type              | expected
          null       | null         | ""
          "anything" | null         | ""
          23         | null         | ""
          null       | String.class | ""
          null       | Number.class | ""
    }

    def "convert #value to #type should downcast gracefully and give [#expected]"() {
        when:
          def converted = Converter.convert(value, type)
        then:
          converted == expected
        where:
          value      | type         | expected
          "anything" | String.class | "anything"
          "anything" | Object.class | "anything"
          23         | Number.class | 23
    }

    def "convert #value to string should give [#expected]"() {
        when:
          def converted = Converter.convert(value, String.class)
        then:
          converted == expected
        where:
          value               | expected
          "anything"          | "anything"
          23                  | "23"
    }

    def "convert #value to #type should give number [#expected]"() {
        when:
          def converted = Converter.convert(value, type)
        then:
          Math.abs(converted - expected) < expected * 0.000001
        where:
          value      | type         | expected
          23f        | float.class  | 23f
          42f        | Float.class  | 42f
          "42"       | Float.class  | 42f
          23f        | double.class | 23d
          42f        | Double.class | 42d
          "42"       | Double.class | 42d
          23l        | int.class    | 23
          42         | Integer.class| 42
          "42"       | Integer.class| 42
          23l        | long.class   | 23
          42         | Long.class   | 42
          "42"       | Long.class   | 42
    }

    def "Convert #value to diverse class types (#type)"() {
        when:
          def converted = Converter.convert(value, type)
        then:
          converted == expected
        where:
          value              | type        | expected
          "java.lang.String" | Class.class | String.class
          "not.a.valid.class"| Class.class | "not.a.valid.class"
    }

    def "Convert #value to #type"() {
        when:
          def converted = Converter.convert(value, type)
        then:
          converted == expected
        where:
          value                | type            | expected
          ""                   | Boolean.class   | false
          "true"               | Boolean.class   | true
          true                 | Boolean.class   | true
          false                | Boolean.class   | false
          ""                   | boolean.class   | false
          "true"               | boolean.class   | true
          true                 | boolean.class   | true
          false                | boolean.class   | false
          "filename"           | File.class      | new File("filename")
          new File("filename") | File.class      | new File("filename")
          "c"                  | Character.class | 'c'
          "c"                  | char.class      | 'c'
          "char"               | char.class      | 'c'
          65                   | char.class      | 'A'
    }
}
