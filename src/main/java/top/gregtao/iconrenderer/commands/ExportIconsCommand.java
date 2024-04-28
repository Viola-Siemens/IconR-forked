package top.gregtao.iconrenderer.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import top.gregtao.iconrenderer.IconRenderer;
import top.gregtao.iconrenderer.commands.arg.ModIdArgumentType;
import top.gregtao.iconrenderer.utils.FileHelper;

import java.io.IOException;
import java.util.List;

public class ExportIconsCommand {
    public static RequiredArgumentBuilder<CommandSourceStack, String> register() {
        return Commands.argument("modid", ModIdArgumentType.instance()).executes(ExportIconsCommand::exportIcons);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> registerEAI() {
        return Commands.literal("ALL").executes(ExportIconsCommand::exportAllIcons);
    }

    public static int exportIcons(CommandContext<CommandSourceStack> context) {
        String modId = ModIdArgumentType.getModId(context, "modid");
        try {
            new FileHelper(modId);
        } catch (IOException e) {
            IconRenderer.LOGGER.error("IOException while exporting icons", e);
        }
        return 1;
    }

    public static int exportAllIcons(CommandContext<CommandSourceStack> context) {
        List<IModInfo> loadedMods = ModList.get().getMods();
        int i;
        for (i = 0; i < loadedMods.size(); ++i) {
            try {
                new FileHelper(loadedMods.get(i).getModId());
            } catch (IOException e) {
                IconRenderer.LOGGER.error("IOException while exporting all icons", e);
            }
        }
        return 1;
    }
}
