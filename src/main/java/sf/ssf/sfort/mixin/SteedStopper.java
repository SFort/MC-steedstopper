package sf.ssf.sfort.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SpawnSettings.Builder.class)
public class SteedStopper {
	@Inject(method = "spawn", at = @At("HEAD"), cancellable = true)
	private void addSpawn(SpawnGroup group, SpawnSettings.SpawnEntry entry, CallbackInfoReturnable<SpawnSettings.Builder> info) {
		if (entry.type.equals(EntityType.DONKEY)||entry.type.equals(EntityType.HORSE)||entry.type.equals(EntityType.SKELETON_HORSE)||entry.type.equals(EntityType.ZOMBIE_HORSE))
			info.cancel();
	}
}
