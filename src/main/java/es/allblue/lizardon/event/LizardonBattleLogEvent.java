package es.allblue.lizardon.event;


import com.pixelmonmod.pixelmon.api.events.battles.AttackEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleMessageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class LizardonBattleLogEvent {
    @SubscribeEvent
    public void eventoAtaque(AttackEvent event){
        /*
        BattleController bc = event.user.bc;
        if(!Lizardon.getLBC().existeCombateEspecial(bc.battleIndex)) return;
        Combate combate = Lizardon.getLBC().getCombateEspecial(bc.battleIndex);

        if(event instanceof AttackEvent.Use) {
            System.out.println("Evento ataque");
        }

        if(event instanceof AttackEvent.Damage) {
            System.out.println("Evento ataque hit");

            System.out.println("Ataque: " + event.user.attack.getMove().getAttackName());
            System.out.println("El ataque le ha provocado " + ((AttackEvent.Damage) event).damage + " de daño. Actualmente tiene " + (event.target.getHealth() - ((AttackEvent.Damage) event).damage) + " de vida.");

        }
*/
    }

    @SubscribeEvent
    public void msg(BattleMessageEvent event){
        //System.out.println("Evento turno");
        //System.out.println(event.textComponent.toString());

    }

}
