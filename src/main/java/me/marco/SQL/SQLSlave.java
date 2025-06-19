package me.marco.SQL;

import me.marco.Base.Core;
import me.marco.SQL.Objects.Query;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SQLSlave {

    private String jdbcDriver = "com.mysql.jdbc.Driver";

    private String USER = "root";
    private String PASS = "";
    private String IP = "";
    private String PORT = "";
    private String DBNAME = "Clans";

    private String autoReconnect = "?autoReconnect=true";
    private String URL = "";

    private String createDB = String.format("CREATE DATABASE IF NOT EXISTS `%s`", DBNAME);

    private ConcurrentLinkedQueue<Query> queries = new ConcurrentLinkedQueue<>();

    private Connection conn;

    public void runQueries(Core instance){
        new BukkitRunnable(){
            public void run(){
                Query query = queries.poll();
                if(query != null){
                    query.execute(conn);
                }
            }
        }.runTaskTimerAsynchronously(instance, 0L, 1L);
    }

    public void connect() {
        URL = "jdbc:mysql://" + IP + ":" + PORT + "/";
        try {
            Class.forName(jdbcDriver);
            conn = (Connection) DriverManager.getConnection(URL + DBNAME + autoReconnect, USER, PASS);
            System.out.println("Connected to DB " + DBNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("try this");
            try {
                System.out.println("Connecting to root DB and creating database " + DBNAME);
                conn = (Connection) DriverManager.getConnection(URL, USER, PASS);
                if(DBNAME.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")){
                    PreparedStatement ps = conn.prepareStatement(createDB);
                    ps.executeUpdate();
                }
                conn = (Connection) DriverManager.getConnection(URL + DBNAME, USER, PASS);
                System.out.println("Connected to database " + DBNAME);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void setPassword(String password) {
        PASS = password;
    }

    public void setUsername(String username) {
        USER = username;
    }

    public void setIP(String ip) {
        IP = ip;
    }

    public void setPort(String port) {
        PORT = port;
    }

    public void setDatabaseName(String dbname) {
        DBNAME = dbname;
    }

    public boolean tableExists(String name) throws SQLException {
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, name, null);
        return tables.next();
    }

    public void executePreparedStatement(String query) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(query);
        ps.executeUpdate(query);
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    public ConcurrentLinkedQueue<Query> getQueries() {
        return queries;
    }

    public void addQuery(Query query){
        this.queries.add(query);
    }

}
