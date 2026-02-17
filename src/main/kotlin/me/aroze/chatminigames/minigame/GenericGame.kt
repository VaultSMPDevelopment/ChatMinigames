package me.aroze.chatminigames.minigame

import me.aroze.arozeutils.kotlin.extension.replacePlaceholders
import me.aroze.arozeutils.minecraft.generic.coloured
import me.aroze.chatminigames.ChatMinigames
import me.aroze.chatminigames.ChatMinigames.Companion.config
import me.aroze.chatminigames.minigame.GameHandler.runningGame
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

abstract class GenericGame(val type: GameType) {

    val values: HashMap<String, String> = hashMapOf()
    var startTime: Long = 0
    private var timeoutTask: BukkitTask? = null

    abstract fun create()

    fun start() {
        create()
        startTime = System.currentTimeMillis()
        val broadcast = type.getMessage("startBroadcast")
            .replacePlaceholders(values)

        Bukkit.broadcastMessage(broadcast.coloured())
        runningGame = this

        val timeoutSeconds = config.getLong("misc-settings.game-timeout")
        if (timeoutSeconds > 0) {
            timeoutTask = Bukkit.getScheduler().runTaskLater(ChatMinigames.instance, Runnable {
                if (runningGame === this) {
                    Bukkit.broadcastMessage(
                        type.getMessage("expiredBroadcast")
                            .replacePlaceholders(values)
                            .coloured()
                    )
                    runningGame = null
                }
                timeoutTask = null
            }, timeoutSeconds * 20L)
        }
    }

    fun cancelTimeout() {
        timeoutTask?.cancel()
        timeoutTask = null
    }

}

enum class GameType(private val configName: String? = null) {
    UNSCRAMBLE, RUSH, MATH,
    REACTIONTIME("reactionTime"),
    ;

    fun getConfigName() = configName ?: name.lowercase()

    fun getMessage(type: String) : String {
        return when(type) {
            "startBroadcast" -> config.getStringList("minigame-messages.game-start.${getConfigName()}").joinToString("\n")
            "answeredBroadcast" -> config.getStringList("minigame-messages.answered-correctly-broadcast.${getConfigName()}").joinToString("\n")
            "answeredPrivate" -> config.getStringList("minigame-messages.answered-correctly-private.${getConfigName()}").joinToString("\n")
            "expiredBroadcast" -> config.getStringList("minigame-messages.game-expired.${getConfigName()}").joinToString("\n")
            else -> "this shouldnt happen"
        }
    }
}