package com.mite.recraft.content.item.throwable;

import com.mite.recraft.content.entity.projectile.BrickEntity;
import com.mite.recraft.registry.ModEntityTypes;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** 砖块。抛出后生成 {@link BrickEntity}，对应 MITE 1.6.4 ItemBrick。 */
public class BrickItem extends Item {

    public BrickItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            BrickEntity brick = new BrickEntity(ModEntityTypes.BRICK,
                    player.getX(), player.getEyeY() - 0.1D, player.getZ(), level);
            brick.setOwner(player);
            brick.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(brick);
            level.playSound(null, player.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.6F);
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
