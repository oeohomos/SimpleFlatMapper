package org.sfm.jdbc.spring;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sfm.beans.DbObject;
import org.sfm.jdbc.DbHelper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class JdbcTemplateMapperFactoryTest {
	
	JdbcTemplate template;
	
	@Before
	public void setUp() throws SQLException {
		template = new JdbcTemplate(new SingleConnectionDataSource(DbHelper.objectDb(), true));
	}

	@Test
	public void testRowMapper() throws SQLException, ParseException  {
		RowMapper<DbObject> mapper = new JdbcTemplateMapperFactory().newRowMapper(DbObject.class);
		List<DbObject> results = template.query(DbHelper.TEST_DB_OBJECT_QUERY, mapper);
		DbHelper.assertDbObjectMapping(results.get(0));
	}
	
	@Test
	public void testPreparedStatementCallback() throws SQLException, ParseException  {
		PreparedStatementCallback<List<DbObject>> mapper = new JdbcTemplateMapperFactory().newPreparedStatementCallback(DbObject.class);
		List<DbObject> results = template.execute(DbHelper.TEST_DB_OBJECT_QUERY, mapper);
		DbHelper.assertDbObjectMapping(results.get(0));
	}

	@Test
	public void testResultSetExtractor() throws SQLException, ParseException  {
		ResultSetExtractor<List<DbObject>> mapper = new JdbcTemplateMapperFactory().newResultSetExtractor(DbObject.class);
		List<DbObject> results = template.query(DbHelper.TEST_DB_OBJECT_QUERY, mapper);
		DbHelper.assertDbObjectMapping(results.get(0));
	}
}
