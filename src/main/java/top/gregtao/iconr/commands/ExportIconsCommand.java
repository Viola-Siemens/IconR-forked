package top.gregtao.iconr.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.commands.CommandRuntimeException;
import top.gregtao.iconr.commands.arg.ModIdArgumentType;
import top.gregtao.iconr.utils.FileHelper;

import java.io.IOException;
import java.util.Collection;

public class ExportIconsCommand {
    public static RequiredArgumentBuilder<FabricClientCommandSource, String> register() {
        return ClientCommandManager.argument("modid", ModIdArgumentType.instance()).executes(ExportIconsCommand::exportIcons);
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> registerEAI() {
        return ClientCommandManager.literal("ALL").executes(ExportIconsCommand::exportAllIcons);
    }

    public static int exportIcons(CommandContext<FabricClientCommandSource> context) throws CommandRuntimeException {
        String modId = ModIdArgumentType.getModId(context, "modid");
        try {
            new FileHelper(modId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int exportAllIcons(CommandContext<FabricClientCommandSource> context) throws CommandRuntimeException {
        Collection<ModContainer> loadedMods = FabricLoader.getInstance().getAllMods();
        for (final var m : loadedMods)
            try {
                new FileHelper(m.getMetadata().getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return 1;
    }
}
