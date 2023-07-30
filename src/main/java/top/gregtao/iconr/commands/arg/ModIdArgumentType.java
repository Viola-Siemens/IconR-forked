package top.gregtao.iconr.commands.arg;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

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
        FabricLoader.getInstance().getAllMods().stream().map(m -> m.getMetadata().getId()).forEach(builder::suggest);
        return builder.buildFuture();
    }


    public static class Serializer implements ArgumentTypeInfo<ModIdArgumentType, Serializer.Template> {

        @Override
        public void serializeToNetwork(Template template, FriendlyByteBuf friendlyByteBuf) {

        }

        @Override
        public Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf) {
            return new Template();
        }

        @Override
        public void serializeToJson(Template template, JsonObject jsonObject) {

        }

        @Override
        public Template unpack(ModIdArgumentType argumentType) {
            return new Template();
        }

        public final class Template implements ArgumentTypeInfo.Template<ModIdArgumentType> {
            @Override
            public ModIdArgumentType instantiate(CommandBuildContext commandBuildContext) {
                return ModIdArgumentType.instance();
            }

            @Override
            public ArgumentTypeInfo<ModIdArgumentType, ?> type() {
                return Serializer.this;
            }
        }
    }
}
