package com.pedrogallardo.cli;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTests {

    @Test
    public void testConstructorAndGetters() {
        String name = "Item1";
        List<Item> subitems = new ArrayList<>();

        Item item = new Item(name, subitems);

        assertEquals(name, item.getName());
        assertEquals(subitems, item.getItems());
    }

    @Test
    public void testSetters() {
        Item item = new Item("Item1", new ArrayList<>());

        String newName = "Item2";
        List<Item> newItems = new ArrayList<>();
        newItems.add(new Item("Sub1", new ArrayList<>()));

        item.setName(newName);
        item.setItems(newItems);

        assertEquals(newName, item.getName());
        assertEquals(newItems, item.getItems());
    }

    @Test
    public void testEmptySubItems() {
        Item item = new Item("single", new ArrayList<>());

        assertTrue(item.getItems().isEmpty());
    }
}
