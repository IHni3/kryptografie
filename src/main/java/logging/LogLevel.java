/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package logging;

public enum LogLevel {
    DEBUG(1),       //Protokolle, die für interaktive Untersuchungen während der Entwicklung verwendet werden. Diese Protokolle sollten hauptsächlich für das Debuggen hilfreiche Informationen enthalten. Sie besitzen keinen langfristigen Nutzen.
    INFO(2),        //Protokolle, die den allgemeinen Ablauf der Anwendung nachverfolgen. Diese Protokolle sollten einen langfristigen Nutzen besitzen.
    WARNING(3),     //Protokolle, die auf ein ungewöhnliches oder unerwartetes Ereignis im Anwendungsfluss hinweisen. Sie bewirken jedoch keinen Abbruch der Anwendungsausführung.
    ERROR(4),       //Protokolle, die auf den Abbruch des aktuellen Ausführungsflusses aufgrund eines Fehlers hinweisen. Sie geben einen Fehler in der aktuellen Aktivität an, keinen Fehler der gesamten Anwendung.
    CRITICAL(5),    //Protokolle, die einen nicht behebbaren Anwendungs- oder Systemabsturz beschreiben oder einen schwerwiegenden Fehler, der unmittelbare Aufmerksamkeit erfordert.
    NONE(6);        //Wird nicht zum Schreiben von Protokollmeldungen verwendet. Gibt an, dass eine Protokollierungskategorie keine Meldungen schreiben soll.

    private final int value;

    LogLevel(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
