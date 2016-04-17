/**
 * Copyright (C) ${year} Marvin Herman Froeder (marvin@marvinformatics.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rayjars.hibernate;

import org.junit.Before;
import org.junit.Test;
import org.rayjars.hibernate.model.Label;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JacksonListUserTypeTest {

    private JacksonListUserType type = null;

    @Before
    public void createType() {
        type = new JacksonListUserType() {
            @Override
            public Class<?> returnedClass() {
                return Label.class;
            }
        };
    }

    @Test
    public void testConvertJsonToObject() throws Exception {
        String json = "[{\"value\": \"french label\", \"lang\":\"fr\"}, {\"value\": \"english label\", \"lang\":\"en\"}]";

        List<Label> labels = (List<Label>) type.convertJsonToObject(json);

        assertThat(labels, hasSize(2));
        assertThat(labels, containsInAnyOrder(
                allOf(hasProperty("value", is("french label")), hasProperty("lang", is("fr"))),
                allOf(hasProperty("value", is("english label")), hasProperty("lang", is("en")))));
    }

    @Test
    public void testConvertObjectToJson() throws Exception {
        List<Label> labels = new ArrayList<Label>();
        labels.add(new Label("french label", "fr"));
        labels.add(new Label("english label", "en"));

        String json = type.convertObjectToJson(labels);

        assertThat(json, containsString("{\"value\":\"french label\",\"lang\":\"fr\"}"));
        assertThat(json, containsString("{\"value\":\"english label\",\"lang\":\"en\"}"));
    }

    @Test
    public void testDeepCopy() throws Exception {
        List<Label> labels = new ArrayList<Label>();
        labels.add(new Label("french label", "fr"));
        labels.add(new Label("english label", "en"));

        List<Label> copy = (List<Label>) type.deepCopy(labels);

        assertThat(labels, hasSize(2));
        assertThat(copy, equalTo(labels));
        assertThat(copy, not(sameInstance(labels)));

        assertThat(labels, containsInAnyOrder(
                allOf(hasProperty("value", is("french label")), hasProperty("lang", is("fr"))),
                allOf(hasProperty("value", is("english label")), hasProperty("lang", is("en")))));

    }

}
