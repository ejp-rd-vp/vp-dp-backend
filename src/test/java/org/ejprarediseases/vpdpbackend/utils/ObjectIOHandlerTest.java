package org.ejprarediseases.vpdpbackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.Hierarchy;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@Tag("UnitTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {ObjectIOHandler.class})
@DisplayName("ObjectIOHandler Unit Tests")
public class ObjectIOHandlerTest {

    @Test
    public void testDeserialization() throws JsonProcessingException {
        String input = """
                {
                    "apiVersion": "v1.0",
                    "parents": [
                        {
                            "level": 1,
                            "parents": [
                                {
                                    "label": "Genetic cystic renal disease",
                                    "code": "93587"
                                },
                                {
                                    "label": "Renal ciliopathy",
                                    "code": "156162"
                                },
                                {
                                    "label": "Rare genetic disorder with obstructive azoospermia",
                                    "code": "400003"
                                },
                                {
                                    "label": "Rare disorder potentially indicated for kidney transplant",
                                    "code": "506213"
                                },
                                {
                                    "label": "Rare disorder with obstructive azoospermia",
                                    "code": "399824"
                                }
                            ]
                        }
                    ]
                }
                """;
        Hierarchy underTest = ObjectIOHandler.deserialize(input,  Hierarchy.class);
        assertEquals(underTest.getApiVersion(), "v1.0");
        assertEquals(underTest.getParents().get(0).getLevel(), 1);
        assertEquals(underTest.getParents().get(0).getParents().get(0).getCode(), "93587");
    }

    @Test
    public void testDeserializationException() {
        String input = """
                {
                    "apiVersionn": 1,
                    "parents": [
                        {
                            "level": 1,
                            "parents": [
                                {
                                    "label": "Genetic cystic renal disease",
                                    "code": "93587"
                                },
                                {
                                    "label": "Renal ciliopathy",
                                    "code": "156162"
                                },
                                {
                                    "label": "Rare genetic disorder with obstructive azoospermia",
                                    "code": "400003"
                                },
                                {
                                    "label": "Rare disorder potentially indicated for kidney transplant",
                                    "code": "506213"
                                },
                                {
                                    "label": "Rare disorder with obstructive azoospermia",
                                    "code": "399824"
                                }
                            ]
                        }
                    ]
                }
                """;
        assertThrows(JsonProcessingException.class, () -> ObjectIOHandler.deserialize(input,  Hierarchy.class));
    }
}
