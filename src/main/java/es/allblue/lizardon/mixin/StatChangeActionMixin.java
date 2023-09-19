package es.allblue.lizardon.mixin;


import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.log.action.BattleAction;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
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

@Mixin(targets = "com.pixelmonmod.pixelmon.battles.controller.log.action.type.StatChangeAction")
public class StatChangeActionMixin {

    private PixelmonWrapper pokemon;

    @Inject(at= @At(value = "TAIL"), method = "<init>(ILcom/pixelmonmod/pixelmon/battles/controller/participants/PixelmonWrapper;[I[I)V")
    private void init(int turn, PixelmonWrapper pokemon, int[] oldStats, int[] newStats, CallbackInfo ci) {
        this.pokemon = pokemon;
    }

}
