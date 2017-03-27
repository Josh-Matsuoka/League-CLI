package com.jmatsuok.leaguecli.main.command.model;

import java.util.Collections;
import java.util.Map;

/**
 * Container class for holding statistics related to in-game champion statistics
 */
public class ChampionStatistics {

    private Map<String, Integer> game_stats;
    private Map<String, Integer> kda_stats;
    private Map<String, Integer> multikill_stats;
    private Map<String, Integer> damage_stats;
    private Map<String, Integer> misc_stats;

    /**
     * Constructs a new instance
     *
     * See ChampionStatisticsBuilder for construction details
     */
    public ChampionStatistics() {
        game_stats = Collections.emptyMap();
        kda_stats = Collections.emptyMap();
        multikill_stats = Collections.emptyMap();
        damage_stats = Collections.emptyMap();
        misc_stats = Collections.emptyMap();
    }

    /**
     * Sets the gameplay statistics for this champion
     *
     * @param game_stats gameplay statistics for the given champion
     */
    public void setGameStats(Map<String, Integer> game_stats) {
        this.game_stats = game_stats;
    }

    /**
     * Sets the KDA statistics for this champion
     *
     * @param kda_stats KDA statistics for the given champion
     */
    public void setKDAStats(Map<String, Integer> kda_stats) {
        this.kda_stats = kda_stats;
    }

    /**
     * Sets the damage statistics for this champion
     *
     * @param damage_stats damage statistics for the given champion
     */
    public void setDamageStats(Map<String, Integer> damage_stats) {
        this.damage_stats = damage_stats;
    }

    /**
     * Sets the multikill statistics for this champion
     *
     * @param multikill_stats multikill statistics for the given champion
     */
    public void setMultikillStats(Map<String, Integer> multikill_stats) {
        this.multikill_stats = multikill_stats;
    }

    /**
     * Sets the miscellaneous statistics for this champion
     *
     * @param misc_stats miscellaneous statistics for the given champion
     */
    public void setMiscStats(Map<String, Integer> misc_stats) {
        this.misc_stats = misc_stats;
    }

    /**
     * Retrieves the gameplay statistics for this champion
     *
     * @return gameplay statistics for the given champion
     */
    public Map<String, Integer> getGameStats() {
        return game_stats;
    }

    /**
     * Retrieves the KDA statistics for this champion
     *
     * @return KDA statistics for the given champion
     */
    public Map<String, Integer> getKDAStats() {
        return kda_stats;
    }

    /**
     * Retrieves the damage statistics for this champion
     *
     * @return damage statistics for the given champion
     */
    public Map<String, Integer> getDamageStats() {
        return damage_stats;
    }

    /**
     * Retrieves the multikill statistics for this champion
     *
     * @return multikill statistics for the given champion
     */
    public Map<String, Integer> getMultikillStats() {
        return multikill_stats;
    }

    /**
     * Retrieves the miscellaneous statistics for this champion
     *
     * @return miscellaneous statistics for the given champion
     */
    public Map<String, Integer> getMiscStats() {
        return misc_stats;
    }


}
