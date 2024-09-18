package com.pedrogallardo.cli;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTests {
    @Test
    public void testSettersAndGetters() {
        Item item = new Item();

        String name = "Item2";
        List<Item> subitems = new ArrayList<>();

        item.setName(name);
        item.setItems(subitems);

        assertEquals(name, item.getName());
        assertEquals(subitems, item.getItems());
    }
}
