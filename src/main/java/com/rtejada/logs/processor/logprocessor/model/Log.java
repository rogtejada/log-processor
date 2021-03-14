package com.rtejada.logs.processor.logprocessor.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "logs")
public class Log {

  @Id
  private String id;

  @Field(type = FieldType.Keyword, name = "UrlPath")
  private String urlPath;

  @Field(type = FieldType.Text, name = "CallerCountry")
  private String callerCountry;

  @Field(type = FieldType.Date, name = "Timestamp")
  private String timestamp;

  @Field(type = FieldType.Text, name = "Action")
  private String action;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrlPath() {
    return urlPath;
  }

  public void setUrlPath(String urlPath) {
    this.urlPath = urlPath;
  }

  public String getCallerCountry() {
    return callerCountry;
  }

  public void setCallerCountry(String callerCountry) {
    this.callerCountry = callerCountry;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
