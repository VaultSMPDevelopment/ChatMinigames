package me.aroze.chatminigames.command

import me.aroze.arozeutils.kotlin.type.Randomiser
import me.aroze.arozeutils.minecraft.command.CommandInfo
import me.aroze.arozeutils.minecraft.command.FancyCommand
import me.aroze.arozeutils.minecraft.generic.coloured
import me.aroze.chatminigames.ChatMinigames
import org.bukkit.command.CommandSender

@CommandInfo(
    description = "Reload the ChatMinigames config",
    permission = "chatminigames.reload",
    permissionMessage = "no permission",
)
object ReloadCommand : FancyCommand("chatgames-reload") {
    override fun onCommand(sender: CommandSender, label: String, args: Array<out String>) {
        val plugin = ChatMinigames.instance

        plugin.reloadConfig()
        ChatMinigames.config = plugin.config

        val wordList = ChatMinigames.config.getString("wordList")
            ?.replace(" ", "")
            ?.split(",")!!
        ChatMinigames.randomisedWords = Randomiser(wordList)

        sender.sendMessage("&#ffb3a6ChatMinigames config reloaded.".coloured())
    }
}
