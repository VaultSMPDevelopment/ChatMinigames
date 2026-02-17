package me.aroze.chatminigames

import me.aroze.arozeutils.kotlin.type.Randomiser
import me.aroze.arozeutils.minecraft.FancyPlugin
import me.aroze.chatminigames.command.ReloadCommand
import me.aroze.chatminigames.command.TestCommand
import me.aroze.chatminigames.minigame.GameHandler
import me.aroze.chatminigames.minigame.GameScheduler
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration

class ChatMinigames : FancyPlugin() {

    companion object {
        lateinit var instance: ChatMinigames
        lateinit var config: FileConfiguration
        lateinit var randomisedWords: Randomiser
    }

    override fun onEnable() {

        saveDefaultConfig()
        ChatMinigames.config = this.config

        ChatMinigames.instance = this
        val wordList = config.getString("wordList")
            ?.replace(" ", "")
            ?.split(",")!!

        randomisedWords = Randomiser(wordList)

        // ???
        TestCommand
        ReloadCommand

        Bukkit.getPluginManager().registerEvents(GameHandler, this)
        GameScheduler.start(this)
    }
}