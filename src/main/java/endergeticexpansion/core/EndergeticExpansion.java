package endergeticexpansion.core;

import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityCorrockCrown;
import endergeticexpansion.common.tileentities.TileEntityFrisbloomStem;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.common.tileentities.boof.TileEntityBoof;
import endergeticexpansion.common.tileentities.boof.TileEntityDispensedBoof;
import endergeticexpansion.core.proxy.ClientProxy;
import endergeticexpansion.core.proxy.CommonProxy;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EETileEntities;
import endergeticexpansion.core.registry.other.EEDispenserBehaviorRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = EndergeticExpansion.MOD_ID)
public class EndergeticExpansion {
	public static final String MOD_ID = "endergetic";
	
	public static EndergeticExpansion instance;
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    
	public EndergeticExpansion() {
		instance = this;
    	
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::registerTileEntities);
    }
    
	void preInit(final FMLCommonSetupEvent event) {
		proxy.preInit();
		EEDispenserBehaviorRegistry.registerAll();
	}
    
    @SubscribeEvent
	@SuppressWarnings("unchecked")
	public void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
    	event.getRegistry().register(EETileEntities.CORROCK_CROWN = (TileEntityType<TileEntityCorrockCrown>) TileEntityType.Builder.create(TileEntityCorrockCrown::new, 
    		EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING, EEBlocks.CORROCK_CROWN_OVERWORLD_WALL, EEBlocks.CORROCK_CROWN_NETHER_STANDING, EEBlocks.CORROCK_CROWN_NETHER_WALL,
    		EEBlocks.CORROCK_CROWN_END_STANDING, EEBlocks.CORROCK_CROWN_END_WALL)
    		.build(null).setRegistryName(MOD_ID, "corrock_crown"));
    	
    	event.getRegistry().register(EETileEntities.FRISBLOOM_STEM = (TileEntityType<TileEntityFrisbloomStem>) TileEntityType.Builder.create(TileEntityFrisbloomStem::new, EEBlocks.FRISBLOOM_STEM).build(null).setRegistryName(MOD_ID, "frisbloom_stem"));
    
    	event.getRegistry().register(EETileEntities.BOLLOOM_BUD = (TileEntityType<TileEntityBolloomBud>) TileEntityType.Builder.create(TileEntityBolloomBud::new, EEBlocks.BOLLOOM_BUD).build(null).setRegistryName(MOD_ID, "bolloom_bud"));
    
    	event.getRegistry().register(EETileEntities.PUFFBUG_HIVE = (TileEntityType<TileEntityPuffBugHive>) TileEntityType.Builder.create(TileEntityPuffBugHive::new, EEBlocks.PUFFBUG_HIVE).build(null).setRegistryName(MOD_ID, "puffbug_hive"));
    	
    	event.getRegistry().register(EETileEntities.BOOF_BLOCK = (TileEntityType<TileEntityBoof>) TileEntityType.Builder.create(TileEntityBoof::new, EEBlocks.BOOF_BLOCK).build(null).setRegistryName(MOD_ID, "boof_block"));
    	event.getRegistry().register(EETileEntities.BOOF_DISPENSED = (TileEntityType<TileEntityDispensedBoof>) TileEntityType.Builder.create(TileEntityDispensedBoof::new, EEBlocks.BOOF_DISPENSED_BLOCK).build(null).setRegistryName(MOD_ID, "boof_dispensed_block"));
    }
}
