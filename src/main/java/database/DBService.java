/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package database;

import configuration.Configuration;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public enum DBService implements IDBService {
    instance;

    private final HSQLDB db = HSQLDB.instance;

    @Override
    public void setupConnection() {
        db.setupConnection();
    }

    @Override
    public void createAllTables() {

        try {
            db.createTableTypes();
            db.createTableAlgorithms();
            db.createTableParticipants();
            db.createTableChannel();
            db.createTableMessages();
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printCritical(exception.toString());
        }

    }

    @Override
    public void dropAllTables() {
    }

    @Override
    public void shutdown() {
        db.shutdown();
    }

    @Override
    public void dropChannel() {

    }

    @Override
    public void insertType(String type) {
        type = type.toLowerCase();
        if (getTypeID(type) >= 0)
            return;

        try {
            db.update(String.format("INSERT INTO types (name) VALUES ('%s')",type));
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printWarning(exception.toString());
        }
    }

    @Override
    public void insertAlgorithm(String algorithm) {
        if (getAlgorithmID(algorithm) >= 0) return;

        try {
            db.update(String.format("INSERT INTO algorithms (name) VALUES ('%s')",algorithm));
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printWarning(exception.toString());
        }
    }

    @Override
    public void insertMessage(String participantSender, String participantReceiver, String algorithm, String keyFile, String plainMessage, String encryptedMessage) {
        int participantFromID = getParticipantID(participantSender);
        int participantToID = getParticipantID(participantReceiver);
        int algorithmID = getAlgorithmID(algorithm);
        long timeStamp = Instant.now().getEpochSecond();


        try {
            db.update(String.format("INSERT INTO messages" +
                            "(PARTICIPANT_FROM_ID, PARTICIPANT_TO_ID, PLAIN_MESSAGE, ALGORITHM_ID, ENCRYPTED_MESSAGE, KEYFILE, TIMESTAMP)" +
                            "VALUES (%d,%d,'%s',%d,'%s','%s',%d)",
                    participantFromID,
                    participantToID,
                    plainMessage,
                    algorithmID,
                    encryptedMessage,
                    keyFile,
                    timeStamp)
            );
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printWarning(exception.toString());
        }
    }

    @Override
    public void insertMessage(Message message) {
        insertMessage(message.getParticipantSender().getName(),
                message.getParticipantReceiver().getName(),
                message.getPlainMessage(),
                message.getAlgorithm().name(),
                message.getEncryptedMessage(),
                message.getKeyfile());
    }

    @Override
    public void insertParticipant(String name, String type) {
        name = name.toLowerCase();
        int typeID = getTypeID(type);

        try {
            db.update(String.format("INSERT INTO participants (name,type_id) VALUES ('%s', %d)", name, typeID));
            db.createTablePostbox(name);
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printWarning(exception.toString());
        }
    }

    @Override
    public void insertParticipant(Participant participant) {
        insertParticipant(participant.getName(), participant.getType());
    }

    @Override
    public void insertChannel(String name, String participantA, String participantB) {
        name = name.toLowerCase();
        int participantA_ID = getParticipantID(participantA);
        int participantB_ID = getParticipantID(participantB);

        try {
            db.update(String.format("INSERT INTO channel" +
                            "(name,participant_01,participant_02)" +
                            "VALUES ('%s', %d, %d)",
                    name,
                    participantA_ID,
                    participantB_ID));
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printWarning(exception.toString());
        }
    }

    @Override
    public void insertChannel(Channel channel) {
        insertChannel(channel.getName(),
                channel.getParticipantA().getName(),
                channel.getParticipantB().getName());
    }

    @Override
    public void insertPostboxMessage(String participantSender, String participantReceiver, String message) {
        if (!participantExists(participantSender) || !participantExists(participantReceiver)) {
            Configuration.instance.getLogger().printWarning("Could not save postbox message, participant not found.");
            return;
        }

        int participantFromID = getParticipantID(participantSender);
        long timeStamp = Instant.now().getEpochSecond();
        try {
            db.update(String.format("INSERT INTO postbox_%s" +
                            "(participant_from_id, message, timestamp)" +
                            "VALUES (%d, %s, %d)",
                    participantReceiver,
                    participantFromID,
                    message,
                    timeStamp));
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printWarning(exception.toString());
        }
    }

    @Override
    public void insertPostboxMessage(PostboxMessage postboxMessage) {
        insertPostboxMessage(postboxMessage.getParticipantReceiver().getName(),
                postboxMessage.getParticipantSender().getName(),
                postboxMessage.getMessage());
    }

    public boolean removeChannel(String channelName) {
        var sql = String.format("DELETE FROM channel WHERE name='%s'", channelName);

        int affected = 0;
        try {
            affected = db.update(sql);
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printWarning(exception.toString());
            return false;
        }
        return affected != 0;

    }

    @Override
    public List<String> getAlgorithms() {
        List<String> algorithms = new ArrayList<>();
        try {
            String sqlStatement = "SELECT * from ALGORITHMS";
            ResultSet resultSet = db.executeQuery(sqlStatement);
            while (resultSet.next()) {
                algorithms.add(resultSet.getString("name"));
            }

        } catch (SQLException sqlException) {
            Configuration.instance.getLogger().printError(sqlException.getMessage());
        }
        return algorithms;
    }

    @Override
    public List<String> getTypes() {
        List<String> types = new ArrayList<>();
        try {
            String sqlStatement = "SELECT * from TYPES";
            ResultSet resultSet = db.executeQuery(sqlStatement);
            while (resultSet.next()) {
                types.add(resultSet.getString("name"));
            }

        } catch (SQLException sqlException) {
            Configuration.instance.getLogger().printError(sqlException.getMessage());
        }
        return types;
    }

    @Override
    public List<Participant> getParticipants() {
        List<Participant> participants = new ArrayList<>();
        try {
            String sqlStatement = "SELECT * from PARTICIPANTS";
            ResultSet resultSet = db.executeQuery(sqlStatement);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String type = getTypeName(resultSet.getInt("type_id"));

                Participant p = ParticipantIntruderFactory.createInstance(name, type);
                participants.add(p);
            }

        } catch (SQLException sqlException) {
            Configuration.instance.getLogger().printError(sqlException.getMessage());
        }
        return participants;
    }

    @Override
    public List<Channel> getChannels() {
        List<Channel> channelList = new ArrayList<>();

        try {
            ResultSet resultSet = db.executeQuery("SELECT * from channel");
            while (resultSet.next()) {
                int participant1ID = resultSet.getInt("participant_01");
                int participant2ID = resultSet.getInt("participant_02");
                Participant participantA = getParticipant(participant1ID);
                Participant participantB = getParticipant(participant2ID);
                channelList.add(new Channel(resultSet.getString("name"), participantA, participantB));
            }
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printError(exception.getMessage());
        }

        return channelList;
    }

    @Override
    public List<PostboxMessage> getPostboxMessages(String participant) {
        List<PostboxMessage> msgList = new ArrayList<>();
        if (!participantExists(participant)) {
            Configuration.instance.getLogger().printWarning("Couldn't get postbox message, participant wasn't found.");
        }

        try {
            ResultSet resultSet = db.executeQuery("SELECT * from POSTBOX_" + participant);
            while (resultSet.next()) {
                int partFromID = resultSet.getInt("participant_from_id");
                String partFromName = getParticipantName(partFromID);
                Participant partFrom = ParticipantIntruderFactory.createInstance(partFromName, getOneParticipantType(partFromName));
                Participant partTo =  ParticipantIntruderFactory.createInstance(participant, getOneParticipantType(participant));
                String timestamp = Integer.toString(resultSet.getInt("timestamp"));
                String message = resultSet.getString("message");
                PostboxMessage pbM = new PostboxMessage(partFrom, partTo, message, timestamp);
                msgList.add(pbM);
            }
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printError(exception.getMessage());
        }

        return msgList;
    }

    @Override
    public Channel getChannel(String participantA, String participantB) {
        int partAID = getParticipantID(participantA);
        int partBID = getParticipantID(participantB);

        String sql = MessageFormat.format("SELECT name from channel where (participant_01=''{0}'' AND participant_02=''{1}'') or (participant_01=''{1}'' AND participant_02=''{0}'')", partAID, partBID);
        String channelName;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (!resultSet.next()) {
                throw new SQLException("No channel found with participants: " + participantA + " & " + participantB);
            }
            channelName = resultSet.getString("name");
            return new Channel(channelName, getOneParticipant(participantA), getOneParticipant(participantB));
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printError(exception.getMessage());
        }


        return null;
    }

    public Channel getChannel(String channelName) {
        int part1Id;
        int part2Id;

        try {
            ResultSet resultSet = db.executeQuery(String.format("SELECT name, participant_01, participant_02 FROM channel WHERE name='%s'", channelName));
            if (!resultSet.next()) {
                throw new SQLException("No channel found with name: " + channelName);
            }
            part1Id = resultSet.getInt("participant_01");
            part2Id = resultSet.getInt("participant_02");
            return new Channel(channelName, getParticipant(part1Id), getParticipant(part2Id));
        } catch (SQLException sqlException) {
            Configuration.instance.getLogger().printError(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public String getOneParticipantType(String participantName) {
        if (participantName == null)
            return "";

        participantName = participantName.toLowerCase();
        int typeID = -1;

        try {
            ResultSet resultSet = db.executeQuery("SELECT TYPE_ID from PARTICIPANTS where name='" + participantName + "'");
            if (!resultSet.next()) {
                throw new SQLException(participantName + " participant wasn't found.");
            }
            typeID = resultSet.getInt("TYPE_ID");
            return getTypeName(typeID);

        } catch (SQLException exception) {
            Configuration.instance.getLogger().printError(exception.getMessage());
        }

        return "";
    }

    @Override
    public Participant getOneParticipant(String participantName) {
        participantName = participantName.toLowerCase();
        if (participantExists(participantName)) {
            return ParticipantIntruderFactory.createInstance(participantName, getOneParticipantType(participantName));
        }
        return null;
    }

    @Override
    public boolean channelExists(String channelName) {
        try {
            ResultSet resultSet = db.executeQuery("SELECT name from channel where LOWER(name)='" + channelName.toLowerCase() + "'");
            if (!resultSet.next()) {
                throw new SQLException(channelName + " channel wasn't found");
            }
            return true;
        } catch (SQLException sqlException) {
            Configuration.instance.getLogger().printError(sqlException.getMessage());
        }

        return false;
    }

    @Override
    public boolean participantExists(String name) {
        int participantID = getParticipantID(name.toLowerCase());
        return participantID != -1;
    }


    private int getTypeID(String name) {
        name = name.toLowerCase();
        try {
            ResultSet resultSet = db.executeQuery("SELECT ID from TYPES where name='" + name + "'");
            if (!resultSet.next()) {
                throw new SQLException(name + " wasn't found in Type-Table");
            }
            return resultSet.getInt("ID");
        } catch (SQLException sqlException) {
            Configuration.instance.getLogger().printWarning(sqlException.getMessage());
        }

        return -1;
    }

    private int getParticipantID(String name) {
        try {
            ResultSet resultSet = db.executeQuery("SELECT ID from PARTICIPANTS where name='" + name + "'");
            if (!resultSet.next()) {
                throw new SQLException(name + " wasn't found in Participant-Table.");
            }
            return resultSet.getInt("ID");
        } catch (SQLException sqlException) {
            Configuration.instance.getLogger().printError(sqlException.getMessage());
        }
        return -1;
    }

    private String getParticipantName(int participantID) {
        try {
            ResultSet resultSet = db.executeQuery("SELECT name from participants where ID=" + participantID);
            if (!resultSet.next()) {
                throw new SQLException(" Name of participant wasn't found for ID:" + participantID);
            }
            return resultSet.getString("name");
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printError(exception.getMessage());
        }

        return "";
    }

    private String getTypeName(int typeID) {
        try {
            ResultSet resultSet = db.executeQuery(String.format("SELECT name from TYPES where ID=%d", typeID));
            if (!resultSet.next()) {
                throw new SQLException("No name of type found for ID: " + typeID);
            }
            return resultSet.getString("name");
        } catch (SQLException exception) {
            Configuration.instance.getLogger().printError(exception.getMessage());
        }

        return "";
    }

    private int getAlgorithmID(String algorithm) {
        try {
            ResultSet resultSet = db.executeQuery(String.format("SELECT ID from ALGORITHMS where LOWER(name)=LOWER('%s')", algorithm));
            if (!resultSet.next()) {
                throw new SQLException((algorithm + " algorithm wasn't found in Algorithm Table"));
            }
            return resultSet.getInt("ID");
        } catch (SQLException sqlException) {
            Configuration.instance.getLogger().printError(sqlException.getMessage());
        }

        return -1;
    }

    private Participant getParticipant(int partID) {
        String name = getParticipantName(partID);
        return ParticipantIntruderFactory.createInstance(name, getOneParticipantType(name));
    }

    public void createInitialValues() {
        insertType("normal");
        insertType("intruder");

        var branch_hkg = new Participant("branch_hkg");
        var branch_wuh = new Participant("branch_wuh");
        var branch_cpt = new Participant("branch_cpt");
        var branch_syd = new Participant("branch_syd");
        var branch_sfo = new Participant("branch_sfo");
        var msa = new Intruder("msa");

        insertParticipant(branch_hkg);
        insertParticipant(branch_wuh);
        insertParticipant(branch_cpt);
        insertParticipant(branch_syd);
        insertParticipant(branch_sfo);
        insertParticipant(msa);

        insertChannel(new Channel("hkg_wuh",branch_hkg,branch_wuh));
        insertChannel(new Channel("hkg_cpt",branch_hkg,branch_cpt));
        insertChannel(new Channel("cpt_syd",branch_cpt,branch_syd));
        insertChannel(new Channel("syd_sfo",branch_syd,branch_sfo));
    }
}
