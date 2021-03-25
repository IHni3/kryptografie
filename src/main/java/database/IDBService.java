/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package database;

import models.Channel;
import models.Message;
import models.Participant;
import models.PostboxMessage;

import java.util.List;

public interface IDBService {

    void setupConnection();

    void createAllTables();

    void dropAllTables();

    void shutdown();

    void dropChannel();

    // Inserts

    void insertType(String type);

    void insertAlgorithm(String algorithm);

    void insertMessage(String participantSender, String participantReceiver, String algorithm, String keyFile, String plainMessage, String encryptedMessage);

    void insertMessage(Message message);

    void insertParticipant(String name, String type);

    void insertParticipant(Participant participant);

    void insertChannel(Channel channel);

    void insertChannel(String name, String participantA, String participantB);

    void insertPostboxMessage(PostboxMessage postboxMessage);

    void insertPostboxMessage(String participantSender, String participantReceiver, String message);


    // Getter

    List<String> getAlgorithms();

    List<String> getTypes();

    List<Participant> getParticipants();

    List<Channel> getChannels();

    List<PostboxMessage> getPostboxMessages(String participant);

    Channel getChannel(String participantA, String participantB);

    String getOneParticipantType(String participantName);

    Participant getOneParticipant(String participantName);


    // Check for existence

    boolean channelExists(String channelName);

    boolean participantExists(String name);
}
