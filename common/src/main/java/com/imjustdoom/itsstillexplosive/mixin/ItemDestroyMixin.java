package com.imjustdoom.itsstillexplosive.mixin;

import com.imjustdoom.itsstillexplosive.ItsStillExplosive;
import com.imjustdoom.itsstillexplosive.config.Config;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {Item.class, BlockItem.class})
public abstract class ItemDestroyMixin {

    @Inject(method = "onDestroyed", at = @At("HEAD"))
    public void onDestroyed(ItemEntity itemEntity, CallbackInfo ci) { // TODO: Account for tnt in shulker

        if (itemEntity.level().isClientSide()) return;

        Item item = itemEntity.getItem().getItem();
        if (!Config.ENABLED_ITEMS.containsKey(item)) return;
        itemEntity.discard();

        itemEntity.level().explode(itemEntity.getOwner(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 1f + Config.ENABLED_ITEMS.get(item) * (float) Math.log(itemEntity.getItem().getCount()), Level.ExplosionInteraction.TNT);

        if (itemEntity.getOwner() != null && itemEntity.getOwner() instanceof ServerPlayer) {
            ItsStillExplosive.BOOM.trigger((ServerPlayer) itemEntity.getOwner());
        }
    }
}
