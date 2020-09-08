package tech.sherrao.discord.pasecurity;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.internal.requests.Route.Emotes;

/**
 * Represents the main part of the Bot
 * 
 * @author Nausher Rao
 *
 */
public class Main extends AnnotatedEventManager {

	private Logger log;
	private JDA jda;

	private ScheduledExecutorService executor;
	private Scanner console;

	public Main() {
		this.log = LoggerFactory.getLogger("PA Security/Main");
		try {
			this.jda = JDABuilder.createDefault(BotData.BOT_TOKEN).build();
			jda.awaitReady();
			jda.setEventManager(this);
			jda.addEventListener(this);
			jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.watching("you"));

			this.executor = Executors.newScheduledThreadPool(2);
			this.console = new Scanner(System.in);

		} catch (LoginException | InterruptedException e) {
			e.printStackTrace(); // TODO: add log message

		}

	}

	/**
	 * Attempts to process any ban and kick commands targetted toward this bot.
	 * 
	 * @param event The text channel message event
	 * 
	 */
	@SubscribeEvent
	public void onCommandAttempt(GuildMessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw().toLowerCase();
		TextChannel channel = event.getChannel();
		Member user = event.getMember();
		List<Member> mentioned = event.getMessage().getMentionedMembers();

		if (message.startsWith(BotData.COMMAND_PREFIX) && !user.getUser().isBot()) {
			String[] args = message.substring(1).split(" ");
			if( args.length > 0 && (user.hasPermission(Permission.KICK_MEMBERS) || user.hasPermission(Permission.BAN_MEMBERS)) ) {
				switch (args[0]) {
					case "ban":
						if (user.hasPermission(Permission.BAN_MEMBERS)) {
							if(mentioned.size() > 0) {
								try {
									Member target = mentioned.get(0);
									target.ban(0);
									channel.sendMessage("\u2705 " + target.getEffectiveName() + " has been banned!").complete();

								} catch(HierarchyException | InsufficientPermissionException e) {
									channel.sendMessage("\u26D4 You don't have permission to ban that member!").complete();
								}
								
							} else 
								channel.sendMessage("\u26D4 That isn't a valid member in this server!!").complete();
								
						} else
							channel.sendMessage("\u26D4 You don't have permission to ban members!").complete();

						break;

					case "kick":
						if (user.hasPermission(Permission.KICK_MEMBERS)) {
							if(mentioned.size() > 0) {
								try {
									Member target = mentioned.get(0);
									target.kick();
									channel.sendMessage("\u2705 " + target.getEffectiveName() + " has been kicked!").complete();

								} catch(HierarchyException | InsufficientPermissionException e) {
									channel.sendMessage("\u26D4 You don't have permission to kick that member!").complete();
								}
									
							} else 
								channel.sendMessage("\u26D4 That isn't a valid member in this server!!").complete();

						} else
							channel.sendMessage("\u26D4 You don't have permission to kick members!").complete();
							
						break;

					default:
						channel.sendMessage("\u26D4 That isn't a valid command!").complete();
						break;

					}

			} else
				return;

		} else
			return;

	}

}