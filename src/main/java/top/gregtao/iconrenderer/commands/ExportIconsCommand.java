package top.gregtao.iconrenderer.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
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

    public static int exportIcons(CommandContext<CommandSourceStack> context) throws CommandRuntimeException {
        String modId = ModIdArgumentType.getModId(context, "modid");
        try {
            new FileHelper(modId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int exportAllIcons(CommandContext<CommandSourceStack> context) throws CommandRuntimeException {
        List<IModInfo> loadedMods = ModList.get().getMods();
        int i;
        for (i = 0; i < loadedMods.size(); ++i) {
            try {
                new FileHelper(loadedMods.get(i).getModId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }
}
