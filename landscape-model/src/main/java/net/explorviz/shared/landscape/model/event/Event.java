package net.explorviz.shared.landscape.model.event;

import com.github.jasminb.jsonapi.annotations.Type;

import net.explorviz.shared.landscape.model.helper.BaseEntity;
import net.explorviz.shared.landscape.model.landscape.Landscape;

/**
 * Model representing an event occurring in a {@link Landscape}.
 */
@SuppressWarnings("serial")
@Type("event")
public class Event extends BaseEntity {

	private long timestamp;

	private EEventType eventType = EEventType.UNKNOWN;

	private String eventMessage;

	public Event(long timestamp, EEventType eventType, String eventMessage) {
		this.timestamp = timestamp;
		this.eventType = eventType;
		this.eventMessage = eventMessage;
	}
	
	public Event() {
	  // Jackson
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public EEventType getEventType() {
		return eventType;
	}

	public void setEventType(EEventType eventType) {
		this.eventType = eventType;
	}

	public String getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}

}
