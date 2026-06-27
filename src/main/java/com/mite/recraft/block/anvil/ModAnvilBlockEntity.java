package com.mite.recraft.block.anvil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

/**
 * 金属砧 BlockEntity — 存储累计 damage
 */
public class ModAnvilBlockEntity extends BlockEntity {
    private int damage;

    public ModAnvilBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ModAnvilBlockEntity(BlockPos pos, BlockState state) {
        this(ModAnvilBlock.getAnvilBlockEntityType(), pos, state);
    }

    /** 从物品的 DataComponentGetter 恢复 damage，并同步方块 STAGE */
    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        Integer dmg = components.get(DataComponents.DAMAGE);
        if (dmg != null) {
            this.damage = dmg;
        }
        syncStage();
    }

    /** 将 damage 暴露给战利品表 copy_components（挖掉方块时保留 NBT） */
    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(DataComponents.DAMAGE, this.damage);
    }

    /** 增加 damage，同步到客户端 */
    public void addDamage(int amount, Level world) {
        Block block = getBlockState().getBlock();
        if (block instanceof ModAnvilBlock anvil) {
            int oldStage = anvil.getDamageStage(this.damage);
            this.damage += amount;
            int newStage = anvil.getDamageStage(this.damage);

            if (newStage >= 3) {
                world.destroyBlock(worldPosition, false);
            } else if (newStage != oldStage) {
                BlockState currentState = getBlockState();
                if (currentState.hasProperty(ModAnvilBlock.STAGE)) {
                    BlockState newState = currentState.setValue(ModAnvilBlock.STAGE, newStage);
                    world.setBlock(worldPosition, newState, Block.UPDATE_ALL);
                }
            }
            // 确保 setChanged 在 setBlock 之后生效
            setChanged();
            world.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        this.damage = input.getIntOr("Damage", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putInt("Damage", this.damage);
    }

    /** 放置/加载时同步方块 STAGE 属性 */
    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        syncStage();
    }

    /** 根据当前 damage 同步方块 STAGE（放置和加载两处调用） */
    private void syncStage() {
        if (this.level != null && !this.level.isClientSide()) {
            Block block = getBlockState().getBlock();
            if (block instanceof ModAnvilBlock anvil) {
                int stage = anvil.getDamageStage(this.damage);
                BlockState state = getBlockState();
                if (state.hasProperty(ModAnvilBlock.STAGE)
                        && state.getValue(ModAnvilBlock.STAGE) != stage) {
                    this.level.setBlock(worldPosition,
                            state.setValue(ModAnvilBlock.STAGE, stage),
                            Block.UPDATE_ALL);
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putInt("Damage", this.damage);
        return tag;
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
