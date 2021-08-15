package sf.ssf.sfort.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Random;

@Mixin(AnimalEntity.class)
public class SteedStopper {
	@Inject(method = "isValidNaturalSpawn", at = @At("HEAD"), cancellable = true)
	private static void isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> info) {
		if ((type.equals(EntityType.DONKEY)||type.equals(EntityType.HORSE)||type.equals(EntityType.SKELETON_HORSE)||type.equals(EntityType.ZOMBIE_HORSE)) && spawnReason == SpawnReason.NATURAL){
			info.setReturnValue(false);
		}
	}
}
