package com.ethem00.betalatethannever.item;

import com.ethem00.betalatethannever.block.DynamiteBlock;
import com.ethem00.betalatethannever.block.ModBlocks;
import com.ethem00.betalatethannever.entity.CustomFallingBlockEntity;
import com.ethem00.betalatethannever.entity.DynamiteEntity;
import com.ethem00.betalatethannever.entity.UnlitDynamiteEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;



public class ThrowableDynamiteItem extends BlockItem {

    public ThrowableDynamiteItem(Block block, Settings settings) {
        super(block, settings);
    }

    /**
     * Right-click on a block
     * - Places a DynamiteBlock normally
     * - Throws DynamiteEntity if flint & steel is in offhand
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        ItemStack stack = context.getStack();

        if (player == null) return ActionResult.PASS;

        boolean playerIsCrouched = player.isSneaking();

        boolean hasFlintAndSteel = player.getOffHandStack().isOf(Items.FLINT_AND_STEEL);

        boolean hasFireCharge = player.getOffHandStack().isOf(Items.FIRE_CHARGE);

        // Cancel only if trying to replace an existing dynamite block directly
        if (world.getBlockState(context.getBlockPos()).getBlock() instanceof DynamiteBlock
                && world.getBlockState(context.getBlockPos()).isReplaceable()) {
            return ActionResult.FAIL;
        }

        if (hasFlintAndSteel && !playerIsCrouched) {
            throwDynamite(world, player, stack, player.getOffHandStack(), 1);
            return ActionResult.success(world.isClient);
        }

        if (hasFireCharge && !playerIsCrouched) {
            throwDynamite(world, player, stack, player.getOffHandStack(), 2);
            return ActionResult.success(world.isClient);
        }

        // Safe: let BlockItem handle normal placement (including offset logic)
        return super.useOnBlock(context);
    }

    // Right Click in air
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        ItemStack offhand = player.getOffHandStack();

        if (offhand.isOf(Items.FLINT_AND_STEEL)) {
            throwDynamite(world, player, stack, offhand, 1);
            return TypedActionResult.success(stack, world.isClient);
        }

        if (offhand.isOf(Items.FIRE_CHARGE)) {
            throwDynamite(world, player, stack, offhand, 2);
            return TypedActionResult.success(stack, world.isClient);
        }

        throwDynamite(world, player, stack, offhand, 0);
        return TypedActionResult.success(stack, world.isClient);
    }


    /**
     * Spawns a thrown DynamiteEntity and plays priming sound
     */
    private void throwDynamite(World world, PlayerEntity player, ItemStack dynamiteStack, ItemStack offhandStack, int throwType) {
        if (!world.isClient) {

            float heightModifier = 0;

            if(player.isSneaking()) {heightModifier = 0.25f;}
            else {heightModifier = 0.35f;}


            if(throwType > 0)
            {
                DynamiteEntity dynamite = new DynamiteEntity(
                        world,
                        player.getX(),
                        player.getY() + player.getStandingEyeHeight() - heightModifier,
                        player.getZ(),
                        player
                );
                dynamite.setVelocity(player.getRotationVector().multiply(1.0)); // adjust throw speed
                world.spawnEntity(dynamite);

                // Primed Sound
                world.playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_TNT_PRIMED,
                        SoundCategory.PLAYERS,
                        0.75f, 1.0f
                );
            }
            else
            {
                UnlitDynamiteEntity unlitDynamite = new UnlitDynamiteEntity(
                        world,
                        player.getX(),
                        player.getY() + player.getStandingEyeHeight() - heightModifier,
                        player.getZ(),
                        player
                );
                //FallingBlockEntity unlitDynamite = new CustomFallingBlockEntity(EntityType.FALLING_BLOCK, world, Block.getBlockFromItem(Items.MOSSY_COBBLESTONE));
                unlitDynamite.setPosition(
                        player.getX(),
                        player.getY()+ player.getStandingEyeHeight() - heightModifier,
                        player.getZ());

                unlitDynamite.setVelocity(player.getRotationVector().multiply(1.0)); // adjust throw speed
                world.spawnEntity(unlitDynamite);
            }

            world.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_SNOWBALL_THROW,
                    SoundCategory.PLAYERS,
                    0.5f, 0.25f
            );

            if(throwType == 1)
            {
                offhandStack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));

                world.playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ITEM_FLINTANDSTEEL_USE,
                        SoundCategory.PLAYERS,
                        1.0f, 1.0f
                );
            }

            if(throwType == 2)
            {
                if (!player.getAbilities().creativeMode) { offhandStack.decrement(1); }

                // Play Firecharge Sound
                world.playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ITEM_FIRECHARGE_USE,
                        SoundCategory.PLAYERS,
                        1.0f, 1.0f
                );
            }
        }

        // Decrement dynamite item in hand if not creative
        if (!player.getAbilities().creativeMode) {
            dynamiteStack.decrement(1);
        }
    }
}