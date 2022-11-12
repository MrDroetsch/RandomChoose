package me.bunnykick.randomchoose.utils;

public class ConfigPaths {

    private ConfigPaths(){}

    public static class Permission {
        public static final String CHOOSE_RANDOM = "Permission.ChooseRandom";
        public static final String CHOOSE_RANDOM_DEFAULT = "fmc.chooserandom";
        public static final String CHOOSE_RANDOM_BYPASS = "Permission.ChooseRandomBypass";
        public static final String CHOOSE_RANDOM_BYPASS_DEFAULT = "fmc.chooserandom.bypass";

        public static final String getDefault(String path) {
            switch(path) {
                case CHOOSE_RANDOM -> {
                    return CHOOSE_RANDOM_DEFAULT;
                }
                case CHOOSE_RANDOM_BYPASS -> {
                    return CHOOSE_RANDOM_BYPASS_DEFAULT;
                }
            }
            return null;
        }
    }

    public static class Message {
        public static final String NOPERM = "Message.NoPerm";
    }
}
