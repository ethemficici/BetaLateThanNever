package com.ethem00.betalatethannever.item;

import com.ethem00.betalatethannever.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class SoulChargeItem extends Item {
    public SoulChargeItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        boolean bl = false;
        if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
            blockPos = blockPos.offset(context.getSide());
            if (AbstractFireBlock.canPlaceAt(world, blockPos, context.getHorizontalPlayerFacing())) {
                this.playParticleAndSound(world, blockPos);
                world.setBlockState(blockPos, ModBlocks.CHARGED_SOUL_FIRE.getDefaultState());
                world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, blockPos);
                bl = true;
            }
        } else {
            this.playParticleAndSound(world, blockPos);
            world.setBlockState(blockPos, blockState.with(Properties.LIT, true));
            world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
            bl = true;
        }

        if (bl) {
            context.getStack().decrement(1);
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.FAIL;
        }
    }

    // Method called if used above a lapis block
    private void checkMystPortal()
    {
        int cornersFilled;
        boolean northFilled;
        boolean eastFilled;
        boolean southFilled;
        boolean westFilled;

        /* TODO:
         Check lapis block for adjacent lazuli cobblestone blocks. Determine direction.
         Loop through in a line until another lapis block is found. Check above for Soul Fire, cornersFilled++ then
         repeat until North, East, South, and West booleans are true and cornersFilled = 4.

         Check if middle is entirely air or replaceable blocks (empty), then check below empty space for crying obsidian.

         If all criteria is met, replace empty space with Myst Portal blocks.
         */
    }

    private void playParticleAndSound(World world, BlockPos pos) {
        Random random = world.getRandom();
        world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1F, (random.nextFloat() - random.nextFloat()) * 0.2F - 0.5F);
        world.playSound(null, pos, SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.BLOCKS, 0.25F, (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F);

        //ALWAYS CHECK IF WORLD IS CLIENT FIRST! OTHERWISE YOU'LL CRASH
        if(!world.isClient)
        {
            for(int i = 0; i < 10; i++) {
                ((ServerWorld) world).spawnParticles(ParticleTypes.SCULK_SOUL,
                        pos.getX() + ((random.nextFloat() - random.nextFloat()) * 0.5F) + 0.5,
                        pos.getY(),
                        pos.getZ() + ((random.nextFloat() - random.nextFloat()) * 0.5F) + 0.5,
                        2, 0.00, 0.01, 0.00, 0.05);
            }
            }
    }
}
