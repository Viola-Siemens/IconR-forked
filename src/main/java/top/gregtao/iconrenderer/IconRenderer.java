package top.gregtao.iconrenderer;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gregtao.iconrenderer.commands.ExportIconsCommand;

@Mod(IconRenderer.MODID)
public class IconRenderer {
    public static final String MODID = "iconr";
    public static final Logger logger = LoggerFactory.getLogger("Icon Renderer");

    public IconRenderer() {
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerCommands(RegisterClientCommandsEvent event) {
        final CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("exporticons").then(ExportIconsCommand.register()));
        dispatcher.register(Commands.literal("exporticons").then(ExportIconsCommand.registerEAI()));
    }
}
