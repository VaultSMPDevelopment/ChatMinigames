package me.aroze.chatminigames.minigame

import me.aroze.chatminigames.ChatMinigames
import me.aroze.chatminigames.ChatMinigames.Companion.config
import me.aroze.chatminigames.minigame.impl.MathGame
import me.aroze.chatminigames.minigame.impl.RushGame
import me.aroze.chatminigames.minigame.impl.UnscrambleGame
import org.bukkit.Bukkit

object GameScheduler {

    private val games = mapOf(
        "math" to MathGame,
        "rush" to RushGame,
        "unscramble" to UnscrambleGame
    )

    fun start(plugin: ChatMinigames) {
        scheduleNext(plugin)
    }

    private fun scheduleNext(plugin: ChatMinigames) {
        val settings = config.getConfigurationSection("auto-start") ?: return
        if (!settings.getBoolean("enabled")) return

        val minTicks = settings.getLong("min-interval") * 20L
        val maxTicks = settings.getLong("max-interval") * 20L
        val delay = (minTicks..maxTicks).random()

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val enabledGames = games.filter { (key, _) ->
                settings.getConfigurationSection("games")?.getBoolean(key) == true
            }.values.toList()

            if (GameHandler.runningGame == null && System.currentTimeMillis() >= GameHandler.cooldownUntil && enabledGames.isNotEmpty()) {
                enabledGames.random().start()
            }

            scheduleNext(plugin)
        }, delay)
    }

}
