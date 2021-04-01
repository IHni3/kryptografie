/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

public class ParticipantIntruderFactory {
    public static Participant createInstance(String name, String type) {
        if (type.equals("intruder")) {
            return new Intruder(name);
        } else if (type.equals("normal")) {
            return new Participant(name);
        } else {
            throw new IllegalArgumentException("unknown type");
        }
    }
}
