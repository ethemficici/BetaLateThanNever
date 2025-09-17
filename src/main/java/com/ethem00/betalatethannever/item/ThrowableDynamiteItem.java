package com.ethem00.betalatethannever.item;

import com.ethem00.betalatethannever.block.DynamiteBlock;
import com.ethem00.betalatethannever.block.ModBlocks;
import com.ethem00.betalatethannever.entity.DynamiteEntity;
import net.minecraft.block.Block;
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

        // Cancel only if trying to replace an existing dynamite block directly
        if (world.getBlockState(context.getBlockPos()).getBlock() instanceof DynamiteBlock
                && world.getBlockState(context.getBlockPos()).isReplaceable()) {
            return ActionResult.FAIL;
        }

        if (hasFlintAndSteel && !playerIsCrouched) {
            throwDynamite(world, player, stack, player.getOffHandStack());
            return ActionResult.success(world.isClient);
        }

        // Safe: let BlockItem handle normal placement (including offset logic)
        return super.useOnBlock(context);
    }

    /**
     * Right-click in air
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        ItemStack offhand = player.getOffHandStack();

        if (offhand.isOf(Items.FLINT_AND_STEEL)) {
            throwDynamite(world, player, stack, offhand);
            return TypedActionResult.success(stack, world.isClient);
        }

        return TypedActionResult.pass(stack);
    }


    /**
     * Spawns a thrown DynamiteEntity and plays priming sound
     */
    private void throwDynamite(World world, PlayerEntity player, ItemStack dynamiteStack, ItemStack flintAndSteel) {
        if (!world.isClient) {
            DynamiteEntity dynamite = new DynamiteEntity(
                    world,
                    player.getX(),
                    player.getY() + player.getStandingEyeHeight(),
                    player.getZ(),
                    player
            );
            dynamite.setVelocity(player.getRotationVector().multiply(1.0)); // adjust throw speed
            world.spawnEntity(dynamite);

            world.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_TNT_PRIMED,
                    SoundCategory.PLAYERS,
                    1.0f, 1.0f
            );

            // Damage flint & steel
            flintAndSteel.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
        }

        // Decrement dynamite item in hand if not creative
        if (!player.getAbilities().creativeMode) {
            dynamiteStack.decrement(1);
        }
    }
}