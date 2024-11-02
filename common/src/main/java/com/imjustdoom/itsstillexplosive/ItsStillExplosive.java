package com.imjustdoom.itsstillexplosive;

import com.imjustdoom.itsstillexplosive.config.Config;
import com.imjustdoom.itsstillexplosive.mixin.CriterionRegistryAccessor;
import com.imjustdoom.itsstillexplosive.trigger.ItemExplosionTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ItsStillExplosive {
    public static final String MOD_ID = "itsstillexplosive";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ItemExplosionTrigger BOOM = CriterionRegistryAccessor.registerCriterion(new ItemExplosionTrigger());

    public static void init() {
        try {
            Config.init();
        } catch (IOException exception) {
            LOGGER.error("There was an error setting up or saving the config file for Doom's It's Still Explosive :(");
            exception.printStackTrace();
        }
    }
    // TODO: advancement for killing player with thrown tnt and one for a powerful explosion
}
