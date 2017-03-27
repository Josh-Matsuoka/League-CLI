package com.jmatsuok.leaguecli.main.command.impl;

import com.jmatsuok.leaguecli.main.command.model.ChampionStatistics;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder class for constructing a ChampionStatistics instance
 */
public class ChampionStatisticsBuilder {

    private ChampionStatistics statistics;

    /**
     * Constructs a new instance
     */
    public ChampionStatisticsBuilder() {
        statistics = new ChampionStatistics();
    }

    /**
     * Sets the gameplay statistics for the desired champion statistics
     *
     * @param games_played Number of games played on this champion
     * @param games_won Number of games won on this champion
     * @param games_lost Number of games lost on this champion
     * @return This builder instance
     */
    public ChampionStatisticsBuilder game_stats(int games_played, int games_won, int games_lost) {
        Map<String, Integer> stats = new HashMap<String, Integer>();
        stats.put("Games Played: ", games_played);
        stats.put("Games Won: ", games_won);
        stats.put("Games Lost: ", games_lost);
        statistics.setGameStats(stats);
        return this;
    }

    /**
     * Sets the KDA statistics for the desired champion statistics
     *
     * @param total_kills Number of kills on this champion
     * @param deaths Number of deaths on this champion
     * @param assists Number of assists on this champion
     * @return This builder instance
     */
    public ChampionStatisticsBuilder kda_stats(int total_kills, int deaths, int assists) {
        Map<String, Integer> stats = new HashMap<String, Integer>();
        stats.put("Total Kills: ", total_kills);
        stats.put("Total Deaths: ", deaths);
        stats.put("Total Assists: ", assists);
        statistics.setKDAStats(stats);
        return this;
    }

    /**
     * Sets the multikill statistics for the desired champion statistics
     *
     * @param doubles Number of double kills on this champion
     * @param triples Number of triple kills on this champion
     * @param quadras Number of quadra kills on this champion
     * @param pentas Number of penta kills on this champion
     * @return This builder instance
     */
    public ChampionStatisticsBuilder multikill_stats(int doubles, int triples, int quadras, int pentas) {
        Map<String, Integer> stats = new HashMap<String, Integer>();
        stats.put("Double Kills: ", doubles);
        stats.put("Triple Kills: ", triples);
        stats.put("Quadra Kills: ", quadras);
        stats.put("Penta Kills: ", pentas);
        statistics.setMultikillStats(stats);
        return this;
    }

    /**
     * Sets the damage statistics for the desired champion statistics
     *
     * @param total_damage Total damage dealt on this champion
     * @param phys_damage Total phyiscal damage dealt on this champion
     * @param magic_damage Total magic damage dealt on this champion
     * @param damage_taken Total damage taken on this champion
     * @return This builder instance
     */
    public ChampionStatisticsBuilder damage_stats(int total_damage, int phys_damage,
                                                  int magic_damage, int damage_taken) {
        Map<String, Integer> stats = new HashMap<String, Integer>();
        stats.put("Total Damage Dealt", total_damage);
        stats.put("Physical Damage Dealt", phys_damage);
        stats.put("Magic Damage Dealt", magic_damage);
        stats.put("Damage Taken", damage_taken);
        statistics.setDamageStats(stats);
        return this;
    }

    /**
     * Sets the miscellaneous statistics for the desired champion statistics
     *
     * @param turrets Total number of turrets destroyed on this champion
     * @param minions Total number of minions killed on this champion
     * @param gold_earned Total gold earned on this champion
     * @param first_bloods Number of first bloods taken on this champion
     * @return This builder instance
     */
    public ChampionStatisticsBuilder misc_stats(int turrets, int minions, int gold_earned, int first_bloods) {
        Map<String, Integer> stats = new HashMap<String, Integer>();
        stats.put("Turrets Taken", turrets);
        stats.put("Minions Slain", minions);
        stats.put("Gold Earned", gold_earned);
        stats.put("First Bloods", first_bloods);
        statistics.setMiscStats(stats);
        return this;
    }

    /**
     * Builds a new ChampionStatistics instance with the supplied statistics.
     *
     * @return New ChampionStatistics instance
     */
    public ChampionStatistics build() {
        return statistics;
    }
}
