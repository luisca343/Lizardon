package es.boffmedia.teras.event;


import com.pixelmonmod.pixelmon.api.events.battles.AttackEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleMessageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class TerasBattleLogEvent {
    @SubscribeEvent
    public void eventoAtaque(AttackEvent event){
        /*
        BattleController bc = event.user.bc;
        if(!Teras.getLBC().existeCombateEspecial(bc.battleIndex)) return;
        Combate combate = Teras.getLBC().getCombateEspecial(bc.battleIndex);

        if(event instanceof AttackEvent.Use) {
            Teras.LOGGER.info("Evento ataque");
        }

        if(event instanceof AttackEvent.Damage) {

            Teras.LOGGER.info("Ataque: " + event.user.attack.getMove().getAttackName());
            Teras.LOGGER.info("El ataque le ha provocado " + ((AttackEvent.Damage) event).damage + " de daño. Actualmente tiene " + (event.target.getHealth() - ((AttackEvent.Damage) event).damage) + " de vida.");

        }
*/
    }

    @SubscribeEvent
    public void msg(BattleMessageEvent event){

    }

}
