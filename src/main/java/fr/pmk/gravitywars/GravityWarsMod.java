package fr.pmk.gravitywars;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GravityWarsMod implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "gravity-wars";
    public static final String MOD_NAME = "GravityWars";

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        //TODO: Initializer
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}