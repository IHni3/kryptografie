import configuration.Configuration;
import database.DBService;
import gui.GUI;

public class Application {

    DBService dbService = DBService.instance;

    public static void main(String[] args) {

        Application app = new Application();
        app.init();
        javafx.application.Application.launch(GUI.class);
    }

    public void init(){
        Configuration.instance.textAreaLogger.setUseParentHandlers(false);
        dbService.setupConnection();
    }

    private void startupGUI(){

    }

    private void close(){
        dbService.shutdown();
    }

    private void initNetworks(){

    }
}
