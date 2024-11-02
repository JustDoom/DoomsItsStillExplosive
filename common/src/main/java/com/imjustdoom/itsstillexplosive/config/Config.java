package com.imjustdoom.itsstillexplosive.config;

import com.imjustdoom.itsstillexplosive.ItsStillExplosive;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Config {

    private static Path FILE_PATH;
    private static Properties PROPERTIES;

    public static Map<Item, Float> ENABLED_ITEMS;

    public static void init() throws IOException {
        PROPERTIES = new Properties();
        FILE_PATH = Path.of(getConfigDirectory() + "/doomsitsstillexplosive.properties");
        if (!FILE_PATH.toFile().exists()) {
            new File(FILE_PATH.toString()).createNewFile();
        }
        PROPERTIES.load(new FileInputStream(FILE_PATH.toFile()));

        ENABLED_ITEMS = getItemFloatMap("enabled-items", "minecraft:tnt:0.75,minecraft:tnt_minecart:0.75,minecraft:gunpowder:0.2");

        save();
    }

    private static String getString(final String setting, final String defaultValue) {
        String value = PROPERTIES.getProperty(setting);
        if (value == null) {
            PROPERTIES.setProperty(setting, defaultValue);
            value = defaultValue;
        }
        return value;
    }

    private static int getInt(final String setting, final String defaultValue) {
        String value = PROPERTIES.getProperty(setting);
        if (value == null) {
            PROPERTIES.setProperty(setting, defaultValue);
            value = defaultValue;
        }
        return Integer.parseInt(value);
    }

    private static float getFloat(final String setting, final String defaultValue) {
        String value = PROPERTIES.getProperty(setting);
        if (value == null) {
            PROPERTIES.setProperty(setting, defaultValue);
            value = defaultValue;
        }
        return Float.parseFloat(value);
    }

    private static boolean getBoolean(final String setting, final String defaultValue) {
        String value = PROPERTIES.getProperty(setting);
        if (value == null) {
            PROPERTIES.setProperty(setting, defaultValue);
            value = defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    private static List<String> getStringList(final String setting, final String defaultValue) {
        String value = PROPERTIES.getProperty(setting);
        if (value == null) {
            PROPERTIES.setProperty(setting, defaultValue);
            value = defaultValue;
        }
        return Arrays.asList(value.split(","));
    }

    private static Map<Item, Float> getItemFloatMap(final String setting, final String defaultValue) {
        List<String> stringList = getStringList(setting, defaultValue);
        Map<Item, Float> itemList = new HashMap<>();
        for (String itemString : stringList) {
            if (itemString.isBlank()) continue;
            String[] data = itemString.split(":");
            if (data.length != 2 && data.length != 3) continue;
            String id = data.length == 3 ? data[0] + ":" + data[1] : data[0];
            String explosionValue = data.length == 3 ? data[2] : data[1];

            if (id.isBlank() || explosionValue.isBlank()) continue;

            ResourceLocation resourceLocation = ResourceLocation.tryParse(id);
            if (BuiltInRegistries.ITEM.containsKey(resourceLocation)) {
                try {
                    itemList.put(BuiltInRegistries.ITEM.get(resourceLocation), Float.valueOf(explosionValue));
                    ItsStillExplosive.LOGGER.info("Item \"{}\" has been added to the item list with the value of \"{}\"", id, explosionValue);
                } catch (NumberFormatException exception) {
                    ItsStillExplosive.LOGGER.error("The value \"{}\" for the item \"{}\" is not a decimal number", explosionValue, id);
                }
            } else {
                ItsStillExplosive.LOGGER.warn("Item \"{}\" was unable to be found", id);
            }
        }
        return itemList;
    }

    public static void save() throws IOException {
        PROPERTIES.store(new FileWriter(FILE_PATH.toFile()),
                """
                        Config for Doom's It's Still Explosive
                        'enabled-items' is a list of items that will explode in contact with lava or fire.
                        The number is the explosion value per single item. Items from other mods are supported if you
                        add the mod id before the item, for vanilla items it is "minecraft" but is also not required.
                        default 'enabled_items=minecraft:tnt:0.75,minecraft:tnt_minecart:0.75,minecraft:gunpowder:0.2'
                        """);
    }

    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }
}