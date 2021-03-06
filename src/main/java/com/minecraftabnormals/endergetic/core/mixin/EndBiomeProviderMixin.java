package com.minecraftabnormals.endergetic.core.mixin;

import java.util.List;

import com.minecraftabnormals.endergetic.common.world.util.LazyAreaContextEndergetic;

import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecraftabnormals.endergetic.common.world.util.EndergeticLayerUtil;
import com.minecraftabnormals.endergetic.core.registry.EEBiomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.layer.Layer;

@Mixin(EndBiomeProvider.class)
public abstract class EndBiomeProviderMixin extends BiomeProvider {
	@Shadow
	@Final
	private SimplexNoiseGenerator generator;

	@Shadow
	@Final
	private Registry<Biome> lookupRegistry;

	@Shadow
	@Final
	private Biome theEndBiome;
	@Shadow
	@Final
	private Biome endHighlandsBiome;
	@Shadow
	@Final
	private Biome endMidlandsBiome;
	@Shadow
	@Final
	private Biome smallEndIslandsBiome;
	@Shadow
	@Final
	private Biome endBarrensBiome;

	private Layer noiseBiomeLayer;
	private Biome chorusPlains;

	private EndBiomeProviderMixin(List<Biome> biomes) {
		super(biomes);
	}

	@Inject(at = @At("RETURN"), method = "<init>")
	private void init(Registry<Biome> lookupRegistry, long seed, CallbackInfo info) {
		this.noiseBiomeLayer = EndergeticLayerUtil.createBiomeLayer(lookupRegistry, (seedModifier) -> new LazyAreaContextEndergetic(25, seed, seedModifier));
		this.chorusPlains = this.lookupRegistry.getValueForKey(EEBiomes.CHORUS_PLAINS.getKey());
	}

	@Inject(at = @At("HEAD"), method = "getNoiseBiome(III)Lnet/minecraft/world/biome/Biome;", cancellable = true)
	private void addEndergeticBiomes(int x, int y, int z, CallbackInfoReturnable<Biome> info) {
		int i = x >> 2;
		int j = z >> 2;
		if ((long) i * (long) i + (long) j * (long) j <= 4096L) {
			info.setReturnValue(this.theEndBiome);
		} else {
			float noise = EndBiomeProvider.getRandomNoise(this.generator, i * 2 + 1, j * 2 + 1);
			Biome biome = this.noiseBiomeLayer.func_242936_a(this.lookupRegistry, x, z);
			boolean isChorus = biome == this.chorusPlains;
			if (noise > 40.0F) {
				info.setReturnValue(isChorus ? this.endHighlandsBiome : biome);
			} else if (noise >= 0.0F) {
				info.setReturnValue(isChorus ? this.endMidlandsBiome : biome);
			} else {
				info.setReturnValue(noise < -20.0F ? this.smallEndIslandsBiome : isChorus ? this.endBarrensBiome : biome);
			}
		}
	}
}