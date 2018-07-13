package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeRepository;
import com.bhegstam.shoppinglist.util.TestDatabaseSetup;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.List;

import static com.bhegstam.shoppinglist.util.Matchers.isEmpty;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class JdbiItemTypeRepositoryTest {
    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private ItemTypeRepository itemTypeRepository;

    @Before
    public void setUp() {
        itemTypeRepository = testDatabaseSetup.getRepositoryFactory().createItemTypeRepository();
    }

    @Test
    public void createAndGetItemType() {
        ItemType itemType = new ItemType("Foo");

        // persist
        itemTypeRepository.createItemType(itemType);
        assertThat(itemType.getId(), notNullValue());
        assertThat(itemType.getName(), is("Foo"));

        // get
        ItemType persistedItemType = itemTypeRepository.get(itemType.getId());
        assertThat(persistedItemType.getId(), notNullValue());
        assertThat(persistedItemType.getName(), is("Foo"));
    }

    @Test
    public void getItemTypes_emptyDb() {
        assertThat(itemTypeRepository.getItemTypes(), isEmpty());
    }

    @Test
    public void getItemTypes() {
        // given
        ItemType itemType1 = new ItemType("Foo");
        itemTypeRepository.createItemType(itemType1);

        ItemType itemType2 = new ItemType("Bar");
        itemTypeRepository.createItemType(itemType2);

        // when
        List<ItemType> itemTypes = itemTypeRepository.getItemTypes();

        // then
        assertThat(itemTypes, contains(itemType2, itemType1));
    }

    @Test
    public void deleteItemType() {
        // given
        ItemType itemType1 = new ItemType("Foo");
        itemTypeRepository.createItemType(itemType1);

        // when
        itemTypeRepository.deleteItemType(itemType1.getId());

        // then
        assertThat(itemTypeRepository.getItemTypes(), isEmpty());
    }

    @Ignore("Partial text search not supported by H2")
    @Test
    public void findItemTypes() {
        // empty db
        assertThat(itemTypeRepository.findItemTypes("foo", 5).size(), is(0));

        // Prep db
        ItemType itemType1 = new ItemType("Foo");
        itemTypeRepository.createItemType(itemType1);

        ItemType itemType2 = new ItemType("Far");
        itemTypeRepository.createItemType(itemType2);

        ItemType itemType3 = new ItemType("Baz");
        itemTypeRepository.createItemType(itemType3);

        errorCollector.checkThat(itemTypeRepository.findItemTypes("", 0), isEmpty());
        errorCollector.checkThat(itemTypeRepository.findItemTypes("", 5), containsInAnyOrder(itemType3, itemType2, itemType1));

        errorCollector.checkThat(itemTypeRepository.findItemTypes("F", 5), contains(itemType2, itemType1));
        errorCollector.checkThat(itemTypeRepository.findItemTypes("Fo", 5), contains(itemType1));
        errorCollector.checkThat(itemTypeRepository.findItemTypes("Foo", 5), contains(itemType1));
        errorCollector.checkThat(itemTypeRepository.findItemTypes("Fooo", 5), isEmpty());

        errorCollector.checkThat(itemTypeRepository.findItemTypes("F", 0), isEmpty());
        errorCollector.checkThat(itemTypeRepository.findItemTypes("F", 1), contains(itemType2));
        errorCollector.checkThat(itemTypeRepository.findItemTypes("F", 2), contains(itemType2, itemType1));
        errorCollector.checkThat(itemTypeRepository.findItemTypes("F", 3), contains(itemType2, itemType1));
    }
}