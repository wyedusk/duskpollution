package dev.wyedusk.duskpollution;

import dev.wyedusk.duskpollution.block.CarbonBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.wyedusk.duskpollution.DuskPollution.ITEMS;

public class DPBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DuskPollution.MODID);

    // Tags
    public static final TagKey<Block> POLLUTANTS = BlockTags.create(
            // Generic pollutants that inflict non-lethal but hindering effects in large quantities
            ResourceLocation.fromNamespaceAndPath(DuskPollution.MODID, "pollutants")
    );
    public static final TagKey<Block> POLLUTANTS_TOXIC = BlockTags.create(
            // Toxic pollutants that inflict lethal effects in large quantities
            ResourceLocation.fromNamespaceAndPath(DuskPollution.MODID, "pollutants_toxic")
    );

    // Gaseous Pollutants
    public static final DeferredBlock<Block> CARBON_BLOCK = BLOCKS.registerBlock("carbon",
            CarbonBlock::new,
            BlockBehaviour.Properties.of()
                .noCollission()
                .noOcclusion()
                .noLootTable()
                .isSuffocating((state, level, pos) -> true)
                .explosionResistance(18000000)
                .strength(-1.0F, 3600000.0F)
                .mapColor(MapColor.COLOR_GRAY));
    public static final DeferredItem<BlockItem> CARBON_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("carbon",
            CARBON_BLOCK);

    public static void register(
            IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
