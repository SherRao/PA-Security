package tech.sherrao.discord.pasecurity;

import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class Main extends AnnotatedEventManager implements EventListener {

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
			
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace(); //TODO: add log message
			
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
		if( message.startsWith(BotData.COMMAND_PREFIX) ) {
			Member user = event.getMember();
			if(!user.getUser().isBot()) {
				
				
				
			} else 
				return;			
			
		} else
			return;
		
		
	}
	
}
