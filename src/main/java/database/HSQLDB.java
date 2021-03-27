/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package database;

import configuration.Configuration;

import java.sql.*;

public enum HSQLDB {
    instance;

    private Connection connection;

    public void setupConnection() {
       Configuration.instance.getLogger().printDebug("setupConnection");

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = DBConfiguration.instance.driverName + DBConfiguration.instance.databaseFile;
            connection = DriverManager.getConnection(databaseURL, DBConfiguration.instance.username, DBConfiguration.instance.password);
        } catch (Exception e) {
           Configuration.instance.getLogger().printError(e.getMessage());
        }
    }

    public synchronized int update(String sqlStatement) throws SQLException {

        Configuration.instance.getLogger().printDebug("executing: " + sqlStatement);

            Statement statement = connection.createStatement();
            var ret = statement.executeUpdate(sqlStatement);
            statement.close();
            return ret;

    }

    public synchronized ResultSet executeQuery(final String sqlStatement) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlStatement);
    }

    /*
       [algorithms]
       id TINYINT       NOT NULL PK
       name VARCHAR(10) NOT NULL unique
    */
    public void createTableAlgorithms() throws SQLException {
       Configuration.instance.getLogger().printDebug("createTableAlgorithms");

        update("CREATE TABLE IF NOT EXISTS algorithms (id TINYINT NOT NULL," +
                "name VARCHAR(10) NOT NULL," +
                "PRIMARY KEY (id))");
        update("CREATE UNIQUE INDEX IF NOT EXISTS idx_algorithms ON algorithms (name)");
    }

    /*
       [types]
       id TINYINT       NOT NULL PK
       name VARCHAR(10) NOT NULL unique
    */
    public void createTableTypes() throws SQLException {
       Configuration.instance.getLogger().printDebug("createTableTypes");

        update("CREATE TABLE IF NOT EXISTS types (id TINYINT NOT NULL," +
                "name VARCHAR(10) NOT NULL," +
                "PRIMARY KEY (id))");
        update("CREATE UNIQUE INDEX IF NOT EXISTS idx_types ON types (name)");
    }

    /*
       [participants]
       id TINYINT       NOT NULL PK
       name VARCHAR(50) NOT NULL unique
       type_id TINYINT  NOT NULL FK
    */
    public void createTableParticipants() throws SQLException {
       Configuration.instance.getLogger().printDebug("createTableParticipants");

        update("CREATE TABLE IF NOT EXISTS participants (id TINYINT NOT NULL," +
                "name VARCHAR(50) NOT NULL," +
                "type_id TINYINT NULL," +
                "PRIMARY KEY (id))");
        update("CREATE UNIQUE INDEX IF NOT EXISTS idx_participants ON types (name)");
        update("ALTER TABLE participants ADD CONSTRAINT IF NOT EXISTS fk_participants FOREIGN KEY (type_id) REFERENCES types (id) ON DELETE CASCADE");
    }

    /*
       [channel]
       name           VARCHAR(25) NOT NULL PK
       participant_01 TINYINT NOT NULL FK
       participant_02 TINYINT NOT NULL FK
    */
    public void createTableChannel() throws SQLException {
       Configuration.instance.getLogger().printDebug("createTableChannel");

        update("CREATE TABLE IF NOT EXISTS channel (name VARCHAR(25) NOT NULL," +
                "participant_01 TINYINT NOT NULL," +
                "participant_02 TINYINT NOT NULL," +
                "PRIMARY KEY (name))");
        update("ALTER TABLE channel ADD CONSTRAINT IF NOT EXISTS fk_channel_01 FOREIGN KEY (participant_01) REFERENCES participants (id) ON DELETE CASCADE");
        update("ALTER TABLE channel ADD CONSTRAINT IF NOT EXISTS fk_channel_02 FOREIGN KEY (participant_02) REFERENCES participants (id) ON DELETE CASCADE");
    }

    /*
      [messages]
      id                  TINYINT NOT NULL
      participant_from_id TINYINT NOT NULL
      participant_to_id   TINYINT NOT NULL
      plain_message       VARCHAR(50) NOT NULL
      algorithm_id        TINYINT NOT NULL
      encrypted_message   VARCHAR(50) NOT NULL
      keyfile             VARCHAR(20) NOT NULL
      timestamp           INT
    */
    public void createTableMessages() throws SQLException {
       Configuration.instance.getLogger().printDebug("createTableMessages");

        update("CREATE TABLE IF NOT EXISTS messages (" +
                "id TINYINT NOT NULL," +
                "participant_from_id TINYINT NOT NULL," +
                "participant_to_id TINYINT NOT NULL," +
                "plain_message VARCHAR(50) NOT NULL," +
                "algorithm_id TINYINT NOT NULL," +
                "encrypted_message VARCHAR(50) NOT NULL," +
                "keyfile VARCHAR(20) NOT NULL," +
                "timestamp INT NOT NULL," +
                "PRIMARY KEY (id))");

        update("ALTER TABLE messages ADD CONSTRAINT IF NOT EXISTS fk_messages_01 FOREIGN KEY (participant_from_id) REFERENCES participants (id) ON DELETE CASCADE");

        update("ALTER TABLE messages ADD CONSTRAINT IF NOT EXISTS fk_messages_02 FOREIGN KEY (participant_to_id) REFERENCES participants (id) ON DELETE CASCADE");

        update("ALTER TABLE messages ADD CONSTRAINT IF NOT EXISTS fk_messages_03 FOREIGN KEY (algorithm_id) REFERENCES algorithms (id) ON DELETE CASCADE");
    }

    /*
       [postbox_[participant_name]]
       id                  TINYINT NOT NULL
       participant_from_id TINYINT NOT NULL
       message             VARCHAR(50) NOT NULL
       timestamp           INT
     */
    public void createTablePostbox(String participantName) throws SQLException {
        String tableName = "postbox_" + participantName;
        Configuration.instance.getLogger().printDebug("createTablePostbox_" + participantName);

        update(String.format("CREATE TABLE IF NOT EXISTS %s (id TINYINT NOT NULL," +
                        "participant_from_id TINYINT NOT NULL," +
                        "message VARCHAR(50) NOT NULL," +
                        "timestamp BIGINT NOT NULL," +
                        "PRIMARY KEY (id))",
                tableName));

        update(String.format("ALTER TABLE %s ADD CONSTRAINT IF NOT EXISTS fk_postbox_%s FOREIGN KEY (participant_from_id) REFERENCES participants (id) ON DELETE CASCADE", tableName, participantName));
    }

    public void shutdown() {
       Configuration.instance.getLogger().printDebug("shutdown");

        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
           Configuration.instance.getLogger().printError(sqle.getMessage());
        }
    }

    public void setupDatabase() throws SQLException {
        setupConnection();
        createTableAlgorithms();
        createTableTypes();
        createTableParticipants();
        createTableChannel();
        createTableMessages();
        createTablePostbox("msa");
    }
}