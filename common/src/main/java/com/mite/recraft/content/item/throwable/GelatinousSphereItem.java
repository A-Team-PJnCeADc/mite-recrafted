package com.mite.recraft.content.item.throwable;

import com.mite.recraft.content.entity.projectile.GelatinousSphereEntity;
import com.mite.recraft.registry.ModEntityTypes;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** 凝胶球。抛出后生成 {@link GelatinousSphereEntity}，对应 MITE 1.6.4 ItemGelatinousSphere。 */
public class GelatinousSphereItem extends Item {

    /** MITE 的 subtype：0 粘液、1 软泥，其余类型共用同一份投掷逻辑。 */
    private final int subtype;

    public GelatinousSphereItem(Properties properties, int subtype) {
        super(properties);
        this.subtype = subtype;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            GelatinousSphereEntity sphere = new GelatinousSphereEntity(ModEntityTypes.GELATINOUS_SPHERE,
                    player.getX(), player.getEyeY() - 0.1D, player.getZ(), level);
            sphere.setSubtype(subtype);
            sphere.setOwner(player);
            sphere.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(sphere);
            level.playSound(null, player.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F);
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
