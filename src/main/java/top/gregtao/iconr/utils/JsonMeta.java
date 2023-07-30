package top.gregtao.iconr.utils;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonMeta {
    public ItemStack itemStack;
    public String zhName;
    public String enName;
    public String regName;
    public String creativeTab;
    public CreativeModeTab itemGroup;
    public RenderType type;
    public int maxStackSize;
    public int maxDurability;
    public String tags;
    public String smallIcon;
    public String largeIcon;
    public ImageHelper imageHelper;

    public JsonMeta(ItemStack itemStack, CreativeModeTab group) {
        this.itemStack = itemStack;
        this.itemGroup = group;
        this.regName = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
        this.type = itemStack.getItem() instanceof BlockItem ? RenderType.Block : RenderType.Item;
        this.maxDurability = itemStack.getMaxDamage();
        this.maxStackSize = itemStack.getMaxStackSize();
        List<TagKey<Item>> tags = new ArrayList<>();
        itemStack.getTags().forEach(tags::add);
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i < tags.size() - 1; ++i) {
            stringBuilder.append(tags.get(i).location()).append(", ");
        }
        if (tags.size() > 0) stringBuilder.append(tags.get(tags.size() - 1).location());
        stringBuilder.append("]");
        this.tags = stringBuilder.toString();
        this.imageHelper = new ImageHelper(this);
    }

    public JsonObject toJsonObject() {
        JsonObject result = new JsonObject();
        result.addProperty("name", this.zhName);
        result.addProperty("englishName", this.enName);
        result.addProperty("registerName", this.regName);
        result.addProperty("CreativeTabName", this.creativeTab);
        result.addProperty("type", this.type.toString());
        result.addProperty("OredictList", this.tags);
        result.addProperty("maxStacksSize", this.maxStackSize);
        result.addProperty("maxDurability", this.maxDurability);
        result.addProperty("smallIcon", this.smallIcon);
        result.addProperty("largeIcon", this.largeIcon);
        return result;
    }
}
