package parser;

import commands.CommandUtils;
import configuration.AlgorithmType;
import configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompactParser {

    public static void evaluateCommand(String command){
        String[] patterns = new String[]{
                "(encrypt message) \"(.+)\" using (rsa|shift) and keyfile ([A-Za-z0-9\\. ]*)",
                "(decrypt message) \"(.+)\" using (rsa|shift) and keyfile ([A-Za-z0-9\\. ]*)",
                "(crack encrypted message) \"(.+)\" using (shift)",
                "(crack encrypted message) \"(.+)\" using (rsa) and keyfile ([A-Za-z0-9\\. ]*)",
                "(register participant) ([A-Za-z0-9_ ]*) with type (normal|intruder)",
                "(create channel) ([A-Za-z0-9_ ]*) from ([A-Za-z0-9_ ]*) to ([A-Za-z0-9_ ]*)",
                "(show channel)",
                "(drop channel) ([A-Za-z0-9_ ]*)",
                "(intrude channel) ([A-Za-z0-9_ ]*) by ([A-Za-z0-9_ ]*)",
                "(send message) \"(.+)\" from ([A-Za-z0-9_ ]*) to ([A-Za-z0-9_ ]*) using ([A-Za-z0-9_ ]*) and keyfile ([A-Za-z0-9_\\. ]*)"
        };

        String[] extracted = null;
        for (String pattern : patterns) {
            extracted = getRegexGroups(pattern, command);
            if (extracted != null) break;
        }
        if (extracted == null) {
            Configuration.instance.textAreaLogger.info(String.format("Command \"%s\" could not be processed", command));
            return;
        }

        doSthWithCommand(extracted);
    }

    private static String[] getRegexGroups(String regex, String command) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            List<String> groups = new ArrayList<>();
            for (int i = 1; i < matcher.groupCount() + 1; i++) {
                groups.add(matcher.group(i));
            }
            return groups.toArray(new String[0]);
        }
        return null;
    }

    private static void doSthWithCommand(String[] extracted){
        CommandUtils utils = new CommandUtils();
        switch (extracted[0]) {
            case "encrypt message":
                String encrypted = utils.encrypt(extracted[1], extracted[2].equals("rsa") ? AlgorithmType.RSA : AlgorithmType.SHIFT, extracted[3]);
                if (encrypted != null) Configuration.instance.textAreaLogger.info(encrypted);
                break;
            case "decrypt message":
                String decrypted = utils.decrypt(extracted[1], extracted[2].equals("rsa") ? AlgorithmType.RSA : AlgorithmType.SHIFT, extracted[3]);
                if (decrypted != null)Configuration.instance.textAreaLogger.info(decrypted);
                break;
            case "crack encrypted message":
                String cracked = null;
                if (extracted[2].equals("shift")){
                    cracked = utils.crackShift(extracted[1]);
                    if (cracked != null) Configuration.instance.textAreaLogger.info(cracked);
                } else {
                    cracked = utils.crackRSA(extracted[1], extracted[3]);
                    if (cracked != null) Configuration.instance.textAreaLogger.info(cracked);
                }
                break;
            case "register participant":
                utils.registerParticipant(extracted[1], extracted[2]);
                break;
            case "create channel":
                utils.createChannel(extracted[1], extracted[2], extracted[3]);
                break;
            case "show channel":
                utils.showChannel();
                break;
            case "drop channel":
                utils.dropChannel(extracted[1]);
                break;
            case "intrude channel":
                utils.intrudeChannelCommand(extracted[1], extracted[2]);
                break;
            case "send message":
                utils.sendMessage(extracted[1], extracted[2], extracted[3], extracted[4].equals("rsa") ? AlgorithmType.RSA : AlgorithmType.SHIFT, extracted[5]);
                break;
        }
    }
}
