package com.pedrogallardo.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DataTests {

    private Data data;

    @BeforeEach
    public void setUp() {
        data = new Data();
    }

    @Test
    public void testInitialDataIsNull() {
        assertNull(data.getData(), "Data is null initially");
    }

    @Test
    public void testSetDataAndGetData() {
        List<Item> testData = new ArrayList<>();
        testData.add(new Item("Item1", new ArrayList<>()));
        testData.add(new Item("Item2", new ArrayList<>()));

        data.setData(testData);

        List<Item> currentData = data.getData();
        assertEquals(2, currentData.size(), "Data size is 2");
        assertEquals("Item1", currentData.get(0).getName(), "First item name is 'Item1'");
        assertEquals("Item2", currentData.get(1).getName(), "Second item name is 'Item2'");
    }
}