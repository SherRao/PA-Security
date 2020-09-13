package tech.sherrao.discord.pasecurity;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

/**
 * Represents the main part of the Bot.
 * 
 * @author Nausher Rao
 *
 */
public class Main implements Runnable {

	private Logger log;
	private JDA jda;
	
	private EventManager events;
	private ScheduledExecutorService executor;
	private Scanner console;
	private ScheduledFuture<?> consoleFuture;
	
	public Main() {
		this.log = LoggerFactory.getLogger("PA Security/Main");
		try {
			this.jda = JDABuilder.createDefault(BotData.BOT_TOKEN).build();
			jda.awaitReady();
			
			this.events = new EventManager(this);
			this.executor = Executors.newScheduledThreadPool(2);
			this.console = new Scanner(System.in);

			jda.setEventManager(events);
			jda.addEventListener(events);
			jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.watching("over you"));
			//this.consoleFuture = executor.schedule(this, 1, TimeUnit.SECONDS);

		} catch (LoginException | InterruptedException e) {
			log.error("Problem while logging into Discord servers, try again later.", e);
			
		}
		
	}
	
	/**
	 * Unused
	 * 
	 */
	@Override
	public void run() {
		while(!consoleFuture.isCancelled()) {
			String input = console.nextLine();
			if(input.contains("quit")) {
				log.info("Shutting down the bot!");
				jda.shutdown();
				return;
				
			} 
		}
	}

	public JDA getJda() { return this.jda; }
	
	public EventManager getEventManager() { return this.events; }
	
}