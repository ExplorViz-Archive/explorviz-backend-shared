package net.explorviz.shared.landscape.model.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.shared.landscape.model.helper.BaseEntity;
import net.explorviz.shared.landscape.model.landscape.Landscape;

/**
 * Model representing an event occurring in a {@link Landscape}.
 */
@SuppressWarnings("serial")
@Type("event")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class Event extends BaseEntity {

  private long timestamp;

  private EEventType eventType = EEventType.UNKNOWN;

  private String eventMessage;

  public Event(final String id, final long timestamp, final EEventType eventType, final String eventMessage) {
    super(id);
    this.timestamp = timestamp;
    this.eventType = eventType;
    this.eventMessage = eventMessage;
  }

  public Event(final String id) {
    super(id);
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final long timestamp) {
    this.timestamp = timestamp;
  }

  public EEventType getEventType() {
    return eventType;
  }

  public void setEventType(final EEventType eventType) {
    this.eventType = eventType;
  }

  public String getEventMessage() {
    return eventMessage;
  }

  public void setEventMessage(final String eventMessage) {
    this.eventMessage = eventMessage;
  }

}
