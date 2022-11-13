package me.bunnykick.randomchoose.utils;

public class ConfigPaths {

    private ConfigPaths(){}

    public static class Permission {
        public static final String CHOOSE_RANDOM = "Permission.ChooseRandom";
        public static final String CHOOSE_RANDOM_DEFAULT = "fmc.chooserandom";
        public static final String RANDOM_BYPASS = "Permission.RandomBypass";
        public static final String RANDOM_BYPASS_DEFAULT = "fmc.randombypass";
        public static final String RANDOM_REWARD = "Permission.RandomReward";
        public static final String RANDOM_REWARD_DEFAULT = "fmc.randomreward";

        public static final String getDefault(String path) {
            switch(path) {
                case CHOOSE_RANDOM -> {
                    return CHOOSE_RANDOM_DEFAULT;
                }
                case RANDOM_BYPASS -> {
                    return RANDOM_BYPASS_DEFAULT;
                }
                case RANDOM_REWARD -> {
                    return RANDOM_REWARD_DEFAULT;
                }
            }
            return null;
        }
    }

    public static class Message {
        public static final String NOPERM = "Message.NoPerm";
    }
}
