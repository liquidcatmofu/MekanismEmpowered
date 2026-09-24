package dev.lapis256.mekanism_empowered.mixin.common.tile;

import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade;
import mekanism.api.Upgrade;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Restores the vanilla Mekanism upgrade recalculation path for addon factory bases that override
 * recalculateUpgrades without delegating to TileEntityMekanism.
 */
@Pseudo
@Mixin(
    targets = {
        "com.jerry.mekanism_extras.common.tile.factory.TileEntityExtraFactory",
        "com.jerry.mekanism_extras.common.integration.mekaf.tile.factory.base.TileEntityExtraAdvancedFactoryBase",
        "com.jerry.mekanism_extras.common.integration.mekmm.tile.TileEntityExtraMoreMachineFactory",
        "io.github.masyumero.emextras.common.tile.factory.TileEntityEMExtraFactory",
        "io.github.masyumero.emextras.common.integration.mekaf.tile.factory.base.TileEntityEMExtraAdvancedFactoryBase",
        "io.github.masyumero.emextras.common.integration.mekmm.tile.TileEntityEMExtraMoreMachineFactory"
    },
    remap = false
)
public abstract class MixinAddonFactoryUpgradeRecalculation extends TileEntityMekanism {
    protected MixinAddonFactoryUpgradeRecalculation(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "recalculateUpgrades", at = @At("TAIL"))
    private void mekanismEmpowered$recalculateAdditionalEnergyUpgrades(Upgrade upgrade, CallbackInfo ci) {
        if (upgrade == MekEmpUpgrade.getEMPOWERED_SPEED()) {
            for (IEnergyContainer energyContainer : getEnergyContainers(null)) {
                if (energyContainer instanceof MachineEnergyContainer<?> machineEnergy) {
                    machineEnergy.updateEnergyPerTick();
                }
            }
        } else if (upgrade == MekEmpUpgrade.getEMPOWERED_ENERGY()) {
            for (IEnergyContainer energyContainer : getEnergyContainers(null)) {
                if (energyContainer instanceof MachineEnergyContainer<?> machineEnergy) {
                    machineEnergy.updateMaxEnergy();
                    machineEnergy.updateEnergyPerTick();
                }
            }
        }
    }
}
