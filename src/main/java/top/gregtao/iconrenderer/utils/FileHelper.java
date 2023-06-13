package top.gregtao.iconrenderer.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import top.gregtao.iconrenderer.IconRenderer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileHelper {
    public static File filePath = new File("./IconRendererOutput/");
    public File file, entityFile;
    public String modId;
    public List<JsonMeta> jsonMetas = new ArrayList<>();
    public List<EntityJsonMeta> entityJsonMetas = new ArrayList<>();

    public FileHelper(String modId) throws IOException {
        this.modId = modId;
        this.file = new File(filePath.toString() + "/" + modId + ".json");
        this.entityFile = new File(filePath.toString() + "/" + modId + "_entity.json");
        if (!filePath.exists() && !filePath.mkdir()) {
            IconRenderer.logger.error("Could not mkdir " + filePath);
        } else if (!this.file.exists() && !this.file.createNewFile()) {
            IconRenderer.logger.error("Could not create new file " + this.file);
        } else if (!this.entityFile.exists() && !this.entityFile.createNewFile()) {
            IconRenderer.logger.error("Could not create new file " + this.entityFile);
        } else {
            IconRenderer.logger.info("Exporting data of " + this.modId);
            this.fromModId();
            this.readNamesByLang();
            this.writeToFile();
            IconRenderer.logger.info("Exported data of " + this.modId);
        }
    }

    public void fromModId() {
        final Minecraft client = Minecraft.getInstance();
        final var handler = client.getConnection();
        if (handler == null) return;
        if (client.level == null) return;
        final RegistryAccess manager = client.level.registryAccess();
        CreativeModeTabs.tryRebuildTabContents(handler.enabledFeatures(), true, manager);
        for (CreativeModeTab group : CreativeModeTabs.allTabs()) {
            if (group != CreativeModeTabs.HOTBAR && group != CreativeModeTabs.INVENTORY && group != CreativeModeTabs.SEARCH) {
                Collection<ItemStack> itemStacks = group.getDisplayItems();
                for (ItemStack itemStack : itemStacks) {
                    if (ForgeRegistries.ITEMS.getKey(itemStack.getItem()).getNamespace().equals(this.modId)) {
                        this.jsonMetas.add(new JsonMeta(itemStack, group));
                    }
                }
            }
        }
        ForgeRegistries.ENTITY_TYPES.forEach(this::putEntity);
    }

    public void putEntity(EntityType<? extends Entity> type) {
        if (!type.getDefaultLootTable().getNamespace().equals(this.modId)) return;
        Entity entity = type.create(Minecraft.getInstance().level);
        if (!(entity instanceof Mob)) return;
        this.entityJsonMetas.add(new EntityJsonMeta(entity));
    }

    public void readNamesByLang() {
        resetLanguage("en_us");
        for (JsonMeta meta : this.jsonMetas) {
            meta.enName = meta.itemStack.getItem().getDescription().getString();
        }
        for (EntityJsonMeta meta : this.entityJsonMetas) {
            meta.enName = meta.entity.getDisplayName().getString();
            meta.mod = meta.entity.getType().getDefaultLootTable().getNamespace();
        }
        resetLanguage("zh_cn");
        for (JsonMeta meta : this.jsonMetas) {
            meta.zhName = meta.itemStack.getItem().getDescription().getString();
            meta.creativeTab = meta.itemGroup.getDisplayName().getString();
        }
        for (EntityJsonMeta meta : this.entityJsonMetas) {
            meta.zhName = meta.entity.getDisplayName().getString();
        }
    }

    public void writeToFile() throws IOException {
        FileWriter writer = new FileWriter(this.file, StandardCharsets.UTF_8);
        for (JsonMeta meta : this.jsonMetas) {
            writer.write(meta.toJsonObject().toString() + "\n");
        }
        writer.close();

        writer = new FileWriter(this.entityFile, StandardCharsets.UTF_8);
        for (EntityJsonMeta meta : this.entityJsonMetas) {
            writer.write(meta.toJsonObject().toString() + "\n");
        }
        writer.close();
    }

    private static void resetLanguage(String lang) {
        Minecraft client = Minecraft.getInstance();
        if (!client.options.languageCode.equals(lang)) {
            client.getLanguageManager().setSelected(lang);
            client.options.languageCode = lang;
            client.reloadResourcePacks();
            client.options.save();
            client.getLanguageManager().onResourceManagerReload(client.getResourceManager());
        }
    }

}
