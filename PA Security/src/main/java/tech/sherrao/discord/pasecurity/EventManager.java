package tech.sherrao.discord.pasecurity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class EventManager extends AnnotatedEventManager {

	private JDA jda;
	private Logger log;

	private File file;
	private TextChannel logChannel;

	public EventManager(Main main) {
		this.jda = main.getJda();
		this.log = LoggerFactory.getLogger("PA Security/EventManager");

		try {
			this.file = new File("log-channel.txt");
			if (file.exists()) {
				String id = Files.readString(Paths.get(file.toURI()));
				this.logChannel = jda.getTextChannelById(id);
				log.info("Using log channel: " + id);
			
			}
		} catch (IOException e) {
			log.error("Problem while trying to read/write from the log channel data file!", e);

		}
	}

	/**
	 * Writes data to the file on bot shutdown.
	 * 
	 * @param event The shutdown event.
	 *
	 */
	@SubscribeEvent
	public void onShutdown(ShutdownEvent event) {
		log.info("Bot shutting down!");
		if (logChannel != null) {
			try (FileWriter out = new FileWriter(file)) {
				out.write(logChannel.getId());

			} catch (IOException e) {
				log.error("Problem while trying to read/write from the log channel data file!", e);

			}
		}
	}

	/**
	 * Logs all server setting changes to the log channel.
	 * 
	 * @param event The guild update event.
	 * 
	 */
	@SubscribeEvent
	public void onGuildSettingsUpdate(GenericGuildUpdateEvent<?> event) {
		String id = event.getPropertyIdentifier();
		String a = event.getOldValue().toString().replace("_", "");
		String b = event.getNewValue().toString().replace("_", "");
		log.info("Logging event '" + id + "': " + a + " -> " + b);

		switch (id) {
		case "icon":
			logMessage("Server Icon Change", "Icon changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "name":
			logMessage("Server Name Change", "Name changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "mfa_level":
			logMessage("Server 2FA Auth Change",
					"Two-Factor authentication level changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "description":
			logMessage("Server Description Change", "Description changed from _" + a + "_ to _" + b + "_",
					jda.getSelfUser());
			break;

		case "owner":
			logMessage("Server Owner Change", "Owner changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "features":
			logMessage("Server Feature Change", "Features changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "locale":
			logMessage("Server Locale Change", "Locale changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "max_members":
			logMessage("Server Max Members Update", "Maximum members updated from _" + a + "_ to _" + b + "_",
					jda.getSelfUser());
			break;

		case "max_presences":
			logMessage("Server Max Presences Update", "Maximum presences updated from _" + a + "_ to _" + b + "_",
					jda.getSelfUser());
			break;

		case "afk_timeout":
			logMessage("Server AFK Timeout Change", "AFK timeout changed from _" + a + "_ to _" + b + "_",
					jda.getSelfUser());
			break;

		case "afk_channel":
			logMessage("Server AFK Channel Change", "AFK channel changed from _" + a + "_ to _" + b + "_",
					jda.getSelfUser());
			break;

		case "banner":
			logMessage("Server Banner Change", "Banner changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "system_channel":
			logMessage("Server System Message Channel Change",
					"System message channel changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "notification_level":
			logMessage("Server Global Notification Level Change",
					"Notification level changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "verification_level":
			logMessage("Server Global Verification Level Change",
					"Verification level changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "explicit_content_filter":
			logMessage("Server Explicit Content Filter Level Change",
					"Explicit content filter level changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "vanity_code":
			logMessage("Server Vanity Code Change", "Vanity code changed from _" + a + "_ to _" + b + "_",
					jda.getSelfUser());
			break;

		case "boost_time":
			logMessage("Nitro Boost Time Change", "Boost time changed from _" + a + "_ to _" + b + "_",
					jda.getSelfUser());
			break;

		case "boost_count":
			logMessage("Nitro Boost Count Change", "Boost count changed from _" + a + "_ to _" + b + "_",
					jda.getSelfUser());
			break;

		case "boost_tier":
			logMessage("Nitro Boost Tier Change", "Boost tier changed from _" + a + "_ to _" + b + "_",
					jda.getSelfUser());
			break;

		case "region":
			logMessage("Server Region Change", "Region changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		case "splash":
			logMessage("Server Splash Change", "Splash changed from _" + a + "_ to _" + b + "_", jda.getSelfUser());
			break;

		}
	}

	/**
	 * Attempts to process any ban and kick commands targeted toward this bot.
	 * 
	 * @param event The text channel message event.
	 * 
	 */
	@SubscribeEvent
	public void onCommandAttempt(GuildMessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw().toLowerCase();
		TextChannel channel = event.getChannel();
		Member user = event.getMember();
		List<Member> mentioned = event.getMessage().getMentionedMembers();

		if (message.startsWith(BotData.COMMAND_PREFIX) && !user.getUser().isBot()) {
			log.info("Attempting command process");
			String[] args = message.substring(1).split(" ");
			if (args.length > 0
					&& (user.hasPermission(Permission.KICK_MEMBERS) || user.hasPermission(Permission.BAN_MEMBERS))) {
				switch (args[0]) {
				case "ban":
					if (user.hasPermission(Permission.BAN_MEMBERS)) {
						if (mentioned.size() > 0) {
							try {
								Member target = mentioned.get(0);
								target.ban(0).complete();
								channel.sendMessage("\u2705 " + target.getEffectiveName() + " has been banned!")
										.complete();

							} catch (HierarchyException | InsufficientPermissionException e) {
								channel.sendMessage("\u26D4 You don't have permission to ban that member!").complete();
							}

						} else
							channel.sendMessage("\u26D4 That isn't a valid member in this server!!").complete();

					} else
						channel.sendMessage("\u26D4 You don't have permission to ban members!").complete();

					break;

				case "kick":
					if (user.hasPermission(Permission.KICK_MEMBERS)) {
						if (mentioned.size() > 0) {
							try {
								Member target = mentioned.get(0);
								target.kick().complete();
								channel.sendMessage("\u2705 " + target.getEffectiveName() + " has been kicked!")
										.complete();

							} catch (HierarchyException | InsufficientPermissionException e) {
								channel.sendMessage("\u26D4 You don't have permission to kick that member!").complete();
							}

						} else
							channel.sendMessage("\u26D4 That isn't a valid member in this server!!").complete();

					} else
						channel.sendMessage("\u26D4 You don't have permission to kick members!").complete();

					break;

				case "setlogchannel":
					if (user.hasPermission(Permission.ADMINISTRATOR)) {
						logChannel = channel;
						channel.sendMessage("\u2705 This channel is now the new log channel!").complete();

					} else
						channel.sendMessage("\u26D4 You don't have permission to change the log channel!");

					break;

				case "quit":
					if(user.hasPermission(Permission.ADMINISTRATOR)) {
						channel.sendMessage("\u2705 The bot is now shutting down!").complete();
						log.info("Shutting down the bot!");
						if (logChannel != null) {
							if(!file.exists()) {
								try {
									file.createNewFile();
									
								} catch (IOException e) {
									log.error("Problem while trying to read/write from the log channel data file!", e);

								}
							}
							
							try(FileWriter out = new FileWriter(file)) {
								out.write(logChannel.getId());
								out.flush();
								out.close();
								
							} catch(IOException e) {}
							
						}
						
						log.info("Done");
						jda.shutdown();
						System.exit(0);
						
					} else 
						channel.sendMessage("\u26D4 You don't have permission to shut down the bot!");

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

	private void logMessage(String title, String message, User target) {
		logChannel.sendMessage(new EmbedBuilder().setAuthor(target.getName(), null, target.getAvatarUrl())
				.setTitle(title).setDescription(message).setThumbnail(target.getAvatarUrl())
				.setFooter("Developed by SherRao").setTimestamp(Instant.now()).build()).complete();

	}

}
