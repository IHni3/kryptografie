package models.dbModels;

public class DBParticipant {
    private final String name;
    private final String type;

    public DBParticipant(String name, String type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
