package database;

import models.dbModels.DBChannel;
import models.dbModels.DBMessage;
import models.dbModels.DBParticipant;
import models.dbModels.DBPostboxMessage;

import java.util.List;

public interface IMSADBService {

    void setupConnection();

    void createAllTables();

    void dropAllTables();

    void shutdown();

    void dropChannel();

    // Inserts

    void insertType(String type);

    void insertAlgorithm(String algorithm);

    void insertMessage(String participantSender, String participantReceiver, String algorithm, String keyFile, String plainMessage, String encryptedMessage);

    void insertMessage(DBMessage message);

    void insertParticipant(String name, String type);

    void insertParticipant(DBParticipant participant);

    void insertChannel(DBChannel channel);

    void insertChannel(String name, String participantA, String participantB);

    void insertPostboxMessage(DBPostboxMessage postboxMessage);

    void insertPostboxMessage(String participantSender, String participantReceiver, String message);


    // Getter

    List<String> getAlgorithms();

    List<String> getTypes();

    List<DBParticipant> getParticipants();

    List<DBChannel> getChannels();

    List<DBPostboxMessage> getPostboxMessages(String participant);

    DBChannel getOneChannel(String participantA, String participantB);

    String getOneParticipantType(String participantName);

    DBParticipant getOneParticipant(String participantName);


    // Check for existence

    boolean channelExists(String channelName);

    boolean participantExists(String name);
}
