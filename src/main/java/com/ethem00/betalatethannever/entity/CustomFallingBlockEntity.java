package com.ethem00.betalatethannever.entity;

import com.ethem00.betalatethannever.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.LandingBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CustomFallingBlockEntity extends FallingBlockEntity {

    private final Block block;

    public CustomFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world, Block newBlock) {
        super(entityType, world);
        this.block = newBlock;
    }

    @Override
    public void onDestroyedOnLanding(Block block, BlockPos pos) {
        if (block instanceof LandingBlock) {
            ((LandingBlock)block).onDestroyedOnLanding(this.getWorld(), pos, this);
        }
    }
}
