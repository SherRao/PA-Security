package tech.sherrao.discord.pasecurity;

import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Helper class to send formatted messages and embeds.
 * 
 * @author Nausher Rao
 *
 */
public class MessageUtils {

	private TextChannel logChannel;
	
	public MessageUtils(TextChannel logChannel) {
		this.logChannel = logChannel;

	}
	
	public void updateChannel(TextChannel channel) {
		this.logChannel = channel;
	}
	
	public void error(Member member, String message) {
	}

	public void log(String title, String message, User target) {
		logChannel.sendMessage( new EmbedBuilder()
				.setAuthor(target.getName(), null, target.getAvatarUrl())
				.setTitle(title)
				.setDescription(message)
				.setThumbnail(target.getAvatarUrl())
				.setFooter("Developed by SherRao")
				.setTimestamp(Instant.now())
				.build() )
			.complete();

	}

}
