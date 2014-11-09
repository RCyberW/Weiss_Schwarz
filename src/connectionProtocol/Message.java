package connectionProtocol;

import java.io.Serializable;

import gamePlay.Player;


/**
 * @author Frank Chen
 * @version 0.1
 * @since 2014-01-01
 */
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2036222712652202681L;
	protected Player sourcePlayer;
	protected Player targetPlayer;
	protected String chatMessage;
	
	public Message(Player sourcePlayer, Player targetPlayer) {
		setSourcePlayer(sourcePlayer);
		setTargetPlayer(targetPlayer);
	}

	/**
	 * @return the chatMessage
	 */
	public String getChatMessage() {
		return chatMessage;
	}

	/**
	 * @param chatMessage
	 *            the chatMessage to set
	 */
	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}

	/**
	 * @return the associatedPlayer
	 */
	public Player getSourcePlayer() {
		return sourcePlayer;
	}

	/**
	 * @param associatedPlayer
	 *            the associatedPlayer to set
	 */
	public void setSourcePlayer(Player associatedPlayer) {
		this.sourcePlayer = associatedPlayer;
	}

	/**
	 * @return the targetPlayer
	 */
	public Player getTargetPlayer() {
		return targetPlayer;
	}

	/**
	 * @param targetPlayer the targetPlayer to set
	 */
	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
	}
	
	public String toString() {
		return "";
	}
	
	public String getType() {
		return "Message";
	}

}
