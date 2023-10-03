package es.allblue.lizardon.mixin;


import com.pixelmonmod.pixelmon.battles.attacks.Effectiveness;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.log.action.BattleAction;
import com.pixelmonmod.pixelmon.battles.controller.log.action.type.DamagePokemonAction;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.pixelmon.battle.Combate;
import es.allblue.lizardon.pixelmon.battle.LizardonBattleController;
import es.allblue.lizardon.pixelmon.battle.LizardonBattleLog;
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
        LizardonBattleController lbc = Lizardon.getLBC();
        if(lbc.existeCombateEspecial(bc.battleIndex)){
            Combate combate = lbc.getCombateEspecial(bc.battleIndex);
            LizardonBattleLog.logEvent(event, combate);
        }
    }

}
