package es.boffmedia.teras.objects_old.pixelmon;

import com.pixelmonmod.pixelmon.api.battles.BattleAIMode;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTiers;
import com.pixelmonmod.pixelmon.battles.api.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.api.rules.clauses.BattleClauseRegistry;
import es.boffmedia.teras.Teras;
import noppes.npcs.api.entity.ICustomNpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ConfigCombate {
    private String nombre;
    private transient String nombreArchivo;
    private transient String carpeta;
    private String nivel;
    private String modalidad;
    private String frecuencia;
    private ArrayList<Recompensa> recompensas;
    private ArrayList<String> normas;
    private transient ICustomNpc npc;
    private transient List<Pokemon> equipo;

    private ArrayList<String> gimmick;
    private int dinero;
    private String IA;
    private boolean curar;
    private boolean preview;

    private String logro;
    private boolean exp;


    public ConfigCombate(){
        this.dinero = 0;
        this.nivel = "0";
        this.modalidad = "1vs1";
        this.IA = "TÁCTICA";
        this.exp = false;
        this.curar = false;
        this.preview = false;
        this.frecuencia = "DIA";
        this.recompensas = new ArrayList<>();
        this.normas = new ArrayList<>();
    }

    public int getNumPkmJugador(){
        return getModalidad()[0];
    }

    public int getNumPkmRival(){
        return getModalidad()[1];
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
        Teras.PROXY.darObjetos(recompensas, uuid);
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

    public int[] getModalidad() {
        if(modalidad == null) return new int[]{1, 1};
        modalidad.replace("v", "vs");
        String[] partes = modalidad.split("vs");
        int[] numeros = new int[partes.length];
        for(int i = 0; i < partes.length; i++){
            numeros[i] = Integer.parseInt(partes[i]);
        }

        return numeros;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
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

    public List<Pokemon> getEquipo() {
        return equipo;
    }

    public void setEquipo(List<Pokemon> equipo) {
        this.equipo = equipo;
    }

    public int getNivelEquipo(int nivelJugador){
        int nivel;

        if(this.nivel.contains("+")) nivel = nivelJugador + Integer.parseInt(this.nivel.split("\\+")[1]);
        else if(this.nivel.contains("-")) nivel = nivelJugador - Integer.parseInt(this.nivel.split("-")[1]);
        else if(this.nivel.equals("0") || this.nivel.equals("=") || this.nivel.equals("IGUALADO") || this.nivel.equals("EQUAL")) nivel = nivelJugador;
        else nivel = Integer.parseInt(this.nivel);
        return nivel;
    }

    public void setNivelEquipo(int nivelJugador){
        int nivel = getNivelEquipo(nivelJugador);

        for (Pokemon pokemon : equipo) {
            pokemon.setLevel(nivel);
        }
    }

    public int getNivelesExtra(){
        if(this.nivel.contains("+")) return Integer.parseInt(this.nivel.split("\\+")[1]);
        else return 0;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getNombreObjetivo() {
        return nombreArchivo.replace("/","");
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public boolean esEntrenador(){
        return !carpeta.equals("eventos");
    }

    public Pokemon getFirstPokemon(){
        return getEquipo().get(0);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setExp(boolean b) {
        this.exp = b;
    }

    public ArrayList<String> getGimmick() {
        return gimmick;
    }

    public void setGimmick(ArrayList<String> gimmick) {
        this.gimmick = gimmick;
    }

    public boolean isCurar() {
        return curar;
    }

    public boolean isPreview() {
        return preview;
    }

    public String getLogro() {
        return logro;
    }

    public void setLogro(String logro) {
        this.logro = logro;
    }

    public boolean isExp() {
        return exp;
    }
}
