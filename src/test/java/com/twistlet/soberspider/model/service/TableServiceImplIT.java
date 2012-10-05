package com.twistlet.soberspider.model.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.twistlet.soberspider.model.type.DatabaseColumn;
import com.twistlet.soberspider.model.type.ForeignKey;

@ContextConfiguration("classpath:application-context.xml")
public class TableServiceImplIT extends AbstractJUnit4SpringContextTests {

	@Autowired
	TableService tableService;

	@Test
	public void testListTableDependenciesForTableNone1() {
		final List<String> list = tableService.listTableDependenciesForTable("calendar_event");
		assertEquals(0, list.size());
	}

	@Test
	public void testListTableDependenciesForTableNone2() {
		final List<String> list = tableService.listTableDependenciesForTable("fais_account_code");
		assertEquals(0, list.size());
	}

	@Test
	public void testListTableDependenciesForTableSingle1() {
		final List<String> actual = tableService.listTableDependenciesForTable("contract");
		final List<String> expected = Arrays.asList(new String[] { "ref_bank" });
		assertEquals(expected, actual);
	}

	@Test
	public void testListTableDependenciesForTableSingle2() {
		final List<String> actual = tableService.listTableDependenciesForTable("inbox");
		final List<String> expected = Arrays.asList(new String[] { "user" });
		assertEquals(expected, actual);
	}

	@Test
	public void testListTableDependenciesForTableMultiple1() {
		final List<String> actual = tableService.listTableDependenciesForTable("committee_detail");
		final List<String> expected = Arrays.asList(new String[] { "committee_master", "ref_position", "user" });
		assertEquals(expected, actual);
	}

	@Test
	public void testListTableDependenciesForTableMultiple2() {
		final List<String> actual = tableService.listTableDependenciesForTable("contract_item");
		final List<String> expected = Arrays.asList(new String[] { "contract", "item" });
		assertEquals(expected, actual);
	}

	@Test
	public void testListPKForTable1() {
		final List<String> actual = tableService.listPrimaryKeyColumnsForTable("item");
		final List<String> expected = Arrays.asList(new String[] { "id" });
		assertEquals(expected, actual);
	}

	@Test
	public void testListPKForTable2() {
		final List<String> actual = tableService.listPrimaryKeyColumnsForTable("vendor_equity");
		final List<String> expected = Arrays.asList(new String[] { "vendor_code" });
		assertEquals(expected, actual);
	}

	@Test
	public void testListColumnsCountForTable1() {
		final List<DatabaseColumn> list = tableService.listColumnsForTable("calendar_event");
		assertEquals(6, list.size());
	}

	@Test
	public void testListColumnsCountForTable2() {
		final List<DatabaseColumn> list = tableService.listColumnsForTable("user");
		assertEquals(5, list.size());
	}

	@Test
	public void testListColumnsCountForTablesAll() {
		final List<String> list = DatabaseServiceImplIT.generateExpectedTables();
		final List<DatabaseColumn> listColumns = new ArrayList<>();
		for (final String table : list) {
			listColumns.addAll(tableService.listColumnsForTable(table));
		}
		assertEquals(773, listColumns.size());
	}

	@Test
	public void testListForeignKeyNone1() {
		final List<ForeignKey> list = tableService.listForeignKeysForTable("calendar_event");
		assertEquals(0, list.size());
	}

	@Test
	public void testListForeignKeySingleCount1() {
		final List<ForeignKey> list = tableService.listForeignKeysForTable("ref_city");
		assertEquals(1, list.size());
	}

	@Test
	public void testListForeignKeySingleContent1() {
		final List<ForeignKey> list = tableService.listForeignKeysForTable("ref_city");
		final ForeignKey item = list.get(0);
		final String columnName = item.getColumnName();
		final String foreignTable = item.getForeignTable();
		final String foreignColum = item.getForeignColumn();
		final String actual = columnName + "/" + foreignTable + "/" + foreignColum;
		final String expected = "state_code/ref_state/id";
		assertEquals(expected, actual);
	}
}
