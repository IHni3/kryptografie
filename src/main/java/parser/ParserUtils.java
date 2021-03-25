/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package parser;

import configuration.AlgorithmType;

public class ParserUtils {
    public static AlgorithmType parseAlgorithm(final String algorithm) throws ParserException {
        for ( AlgorithmType value :AlgorithmType.values()) {
            if(value.name().equalsIgnoreCase(algorithm)) {
                return value;
            }
        }
        throw new ParserException();
    }
}
