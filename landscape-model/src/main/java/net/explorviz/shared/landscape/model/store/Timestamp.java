package net.explorviz.shared.landscape.model.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.shared.landscape.model.helper.BaseEntity;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model representing timestamps (a single software landscape for a specific UNIX timestamp in
 * milliseconds).
 */
@SuppressWarnings("serial")
@Type("timestamp")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class Timestamp extends BaseEntity {

  private long timestamp;
  private int totalRequests;

  @JsonCreator
  public Timestamp(@JsonProperty("id") final String id,
      @JsonProperty("timestampValue") final long timestampValue,
      @JsonProperty("requests") final int requests) {
    super(id);
    this.setTimestamp(timestampValue);
    this.setTotalRequests(requests);
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(final long timestamp) {
    this.timestamp = timestamp;
  }

  public int getTotalRequests() {
    return this.totalRequests;
  }

  public void setTotalRequests(final int totalRequests) {
    this.totalRequests = totalRequests;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(timestamp).append(totalRequests).append(id).build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Timestamp other = (Timestamp) obj;
    if (timestamp != other.timestamp)
      return false;
    if (totalRequests != other.totalRequests)
      return false;
    return true;
  }


}
