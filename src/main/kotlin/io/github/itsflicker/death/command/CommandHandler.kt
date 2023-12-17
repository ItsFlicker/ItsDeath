package io.github.itsflicker.death.command

import io.github.itsflicker.death.ItsDeath
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.platform.util.sendInfoMessage

@CommandHeader("itsdeath", permission = "itsdeath.access")
object CommandHandler {

    @CommandBody(optional = true)
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            ItsDeath.conf.reload()
            ItsDeath.messages.reload()
            sender.sendInfoMessage("重载完成")
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

}