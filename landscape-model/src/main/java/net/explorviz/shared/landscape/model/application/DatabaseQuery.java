package net.explorviz.shared.landscape.model.application;

import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.shared.landscape.model.helper.BaseEntity;

/**
 * Model representing executed database queries.
 */
@SuppressWarnings("serial")
@Type("databasequery")
public class DatabaseQuery extends BaseEntity {

  private long timestamp;
  // at the moment: Statement or PreparedStatement
  private String statementType;
  private String sqlStatement;
  private String returnValue;
  private long responseTime;

  @Relationship("parentApplication")
  private Application parentApplication;


  public DatabaseQuery(final String id) {
    super(id);
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(final long timestamp) {
    this.timestamp = timestamp;
  }

  public String getStatementType() {
    return this.statementType;
  }

  public void setStatementType(final String statementType) {
    this.statementType = statementType;
  }

  public String getSqlStatement() {
    return this.sqlStatement;
  }

  public void setSqlStatement(final String sqlStatement) {
    this.sqlStatement = sqlStatement;
  }

  public String getReturnValue() {
    return this.returnValue;
  }

  public void setReturnValue(final String returnValue) {
    this.returnValue = returnValue;
  }

  public long getResponseTime() {
    return this.responseTime;
  }

  public void setResponseTime(final long responseTime) {
    this.responseTime = responseTime;
  }

  public Application getParentApplication() {
    return this.parentApplication;
  }

  public void setParentApplication(final Application parentApplication) {
    this.parentApplication = parentApplication;
  }

}
