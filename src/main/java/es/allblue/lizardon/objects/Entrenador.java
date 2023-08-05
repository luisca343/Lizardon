package es.allblue.lizardon.objects;

import com.pixelmonmod.pixelmon.api.battles.BattleAIMode;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTiers;
import com.pixelmonmod.pixelmon.battles.api.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.api.rules.clauses.BattleClauseRegistry;
import es.allblue.lizardon.Lizardon;
import net.minecraft.command.CommandSource;
import noppes.npcs.api.entity.ICustomNpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Entrenador {
    private int dinero;
    private String nivel;
    private String tipo;
    private String IA;
    private boolean curar;
    private boolean preview;
    private String frecuencia;
    private ArrayList<Recompensa> recompensas;
    private ArrayList<String> normas;

    private ICustomNpc npc;

    public Entrenador(int dinero, ArrayList<Recompensa> recompensas) {
        this.dinero = dinero;
        this.recompensas = recompensas;

    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public ArrayList<Recompensa> getRecompensas() {
        return recompensas;
    }

    public void setRecompensas(ArrayList<Recompensa> recompensas) {
        this.recompensas = recompensas;
    }

    public void recibirRecompensas(UUID uuid){
        Lizardon.PROXY.darObjetos(recompensas, uuid);
    }

    public boolean curar() {
        return curar;
    }

    public void setCurar(boolean curar) {
        this.curar = curar;
    }

    public boolean hasPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public int[] getTipo() {
        if(tipo == null) return new int[]{6, 6};
        tipo.replace("v", "vs");
        String[] partes = tipo.split("vs");
        int[] numeros = new int[partes.length];
        for(int i = 0; i < partes.length; i++){
            numeros[i] = Integer.parseInt(partes[i]);
        }

        return numeros;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFrecuencia() {
        return frecuencia == null ? "DIARIA" : frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public BattleAIMode getIA() {
        switch (IA.toUpperCase(Locale.ROOT)){
            case "AGGRESSIVE":
            case "AGRESIVA":
                return BattleAIMode.AGGRESSIVE;
            case "ADVANCED":
            case "AVANZADA":
                return BattleAIMode.ADVANCED;
            case "TACTICAL":
            case "TACTICA":
                return BattleAIMode.TACTICAL;
            default:
                return BattleAIMode.DEFAULT;
        }
    }

    public void setIA(String IA) {
        this.IA = IA;
    }

    public String getNivel() {
        if(nivel == null) return BossTiers.NOT_BOSS;
        if(nivel.contains("+")) return nivel.split("\\+")[1];
        if(nivel.contains("-")) return nivel;

        switch (nivel){
            case "0":
            case "=":
            case "IGUALADO":
                return BossTiers.EQUAL;
            default:
                return BossTiers.NOT_BOSS;
        }

    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public List<BattleClause> getNormas() {
        List<BattleClause> clauses = new ArrayList<>();
        for (String norma : normas) {
            clauses.add(BattleClauseRegistry.getClause(norma.toLowerCase(Locale.ROOT)));
        }
        return clauses;
    }

    public void setNormas(ArrayList<String> normas) {
        this.normas = normas;
    }

    public void setNpc(ICustomNpc npc){
        this.npc = npc;
    }

    public ICustomNpc getNpc(){
        return npc;
    }

}
