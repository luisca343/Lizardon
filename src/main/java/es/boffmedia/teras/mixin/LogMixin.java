package es.boffmedia.teras.mixin;


import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.log.action.BattleAction;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.pixelmon.battle.TerasBattle;
import es.boffmedia.teras.pixelmon.battle.TerasBattleController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.pixelmonmod.pixelmon.battles.controller.log.BattleLog")
public class LogMixin {
    @Shadow @Final protected BattleController bc;

    @Inject(at= @At(value = "HEAD"), method = "logEvent")
    public void logEvent(BattleAction event, CallbackInfo ci) {
        Teras.LOGGER.info("=== Log event: " + event.toString() + " ===");
        Teras.LOGGER.info("Battle index: " + bc.battleIndex);
        TerasBattleController lbc = Teras.getLBC();
        Teras.LOGGER.info(lbc.terasBattles.toString());
        if(lbc.existsTerasBattle(bc.battleIndex)){
            TerasBattle combate = lbc.getTerasBattle(bc.battleIndex);
            combate.logEvent(event);
        } else {
            Teras.LOGGER.error("No se ha encontrado el combate");
        }
    }

}
