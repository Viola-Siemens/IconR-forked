package top.gregtao.iconr;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gregtao.iconr.commands.ExportIconsCommand;
import top.gregtao.iconr.commands.arg.ModIdArgumentType;

public class IconRenderer implements ModInitializer {
    public static final String MODID = "iconr";
    public static final Logger logger = LoggerFactory.getLogger("Icon Renderer");

    @Override
    public void onInitialize() {
        ArgumentTypeRegistry.registerArgumentType(new ResourceLocation(MODID, "modid"), ModIdArgumentType.class, new ModIdArgumentType.Serializer());
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("exporticons").then(ExportIconsCommand.register()));
            dispatcher.register(ClientCommandManager.literal("exporticons").then(ExportIconsCommand.registerEAI()));
        });
    }
}
