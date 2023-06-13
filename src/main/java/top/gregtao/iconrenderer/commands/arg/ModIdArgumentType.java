package top.gregtao.iconrenderer.commands.arg;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.concurrent.CompletableFuture;

public class ModIdArgumentType implements ArgumentType<String> {
    public static ModIdArgumentType instance() {
        return new ModIdArgumentType();
    }

    public static String getModId(final CommandContext<?> context, final String name) {
        return context.getArgument(name, String.class);
    }

    private ModIdArgumentType() {
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        ModList.get().getMods().stream().map(IModInfo::getModId).forEach(builder::suggest);
        return builder.buildFuture();
    }
}
