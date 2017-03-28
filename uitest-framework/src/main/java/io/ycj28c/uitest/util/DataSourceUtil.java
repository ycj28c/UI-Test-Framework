package io.ycj28c.uitest.util;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * datasource connection util, use to persist multiple datasource connection pool
 * 
 * @author ryang
 * @version 2016/05/12
 */
public class DataSourceUtil {
	
	private static Hashtable<String, DataSource> connectionPools = null;
	private static String activeDataSource = null;
	static{
		connectionPools = new Hashtable<String, DataSource>(2,0.75F);
	}
	
	/**
	 * look up the active/current datasource
	 * @return
	 * @throws NameNotFoundException
	 */
	public static DataSource lookupActiveDS()
			throws NameNotFoundException {
		return lookup(activeDataSource);
	}
	
	/**
	 * look the specific datasource
	 * @param dataSource
	 * @return
	 * @throws NameNotFoundException
	 */
	public static DataSource lookup(String dataSource)
			throws NameNotFoundException {
		Object ds = null;
		ds = connectionPools.get(dataSource);
		if (ds == null || !(ds instanceof DataSource)){
			throw new NameNotFoundException("Lookup Exception, the datasource " + dataSource
					+ " is not existed in datasource map");
		}
		return (DataSource) ds;
	}
	
	/**
	 * active the specific datasource as activeDataSource
	 * @param name
	 * @throws NameNotFoundException
	 */
	public static void active(String name)
			throws NameNotFoundException {
		if (name.isEmpty() || !connectionPools.containsKey(name)) {
			throw new NameNotFoundException("Active Exception, the datasource "
					+ name + " is not existed in datasource map");
		}
		activeDataSource= name;
	}
	
//	/**
//	 * look up the whole datasource pool
//	 * @return
//	 */
//	public static Hashtable<String, JdbcTemplate> lookupAllDS() {
//		Hashtable<String, JdbcTemplate> map = new Hashtable<String, JdbcTemplate>();
//		for(Entry<String, DataSource> item:connectionPools.entrySet()){
//			JdbcTemplate jdbcTemp = new JdbcTemplate(item.getValue());
//			map.put(item.getKey(), jdbcTemp);
//		}
//		return map;
//	}
	
	/**
	 * get the hash table for all the datasource
	 * @return
	 */
	public static Hashtable<String, DataSource> toHash() {
		return connectionPools;
	}
	
	/** 
	 * bind the datasource with the configuration
	 * @param name
	 * @param param
	 * @return
	 * @throws NameAlreadyBoundException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */
	public static DataSource bind(String name, HikariConfig param)
			throws NameAlreadyBoundException, ClassNotFoundException,
			IllegalAccessException, InstantiationException, SQLException {
		HikariDataSource source = null;
		try {
			lookup(name);
			throw new NameAlreadyBoundException(
					"Bind Exception, the datasource " + name
							+ "is already existed in map");
		} catch (NameNotFoundException e) {
			source = new HikariDataSource(param);
			activeDataSource = name;
			connectionPools.put(name, source);
		}
		return source;
	}
	
	/**
	 * rebind the datasource with the configuration
	 * @param name
	 * @param param
	 * @return
	 * @throws NameAlreadyBoundException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */
	public static DataSource rebind(String name, HikariConfig param)
			throws NameAlreadyBoundException, ClassNotFoundException,
			IllegalAccessException, InstantiationException, SQLException {
		try {
			unbind(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bind(name, param);
	}
	
	/**
	 * unbind the specific datasource name
	 * @param name
	 * @throws NameNotFoundException
	 */
	public static void unbind(String name) throws NameNotFoundException {
		DataSource dataSource = lookup(name);
		/* TODO 
		 * may need to close the database connection
		 * */
		if(connectionPools.contains(dataSource)){
			connectionPools.remove(name);
			if(activeDataSource.equals(name)){
				activeDataSource = null;
			}
		}
	}
	
	/**
	 * show current connection pool information
	 * @return
	 */
	public static String getConnectionPoolsInfo(){
		if(connectionPools.isEmpty()){
			return null;
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("\n########### Database Pool ###########");
			int cnt = 1;
			for (Entry<String, DataSource> item : connectionPools.entrySet()) {
				sb.append("\n"+cnt+". datasource key: " + item.getKey());
				cnt++;
			}
			sb.append("\n#####################################");
			return sb.toString();
		}
	}

}
