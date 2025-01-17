package com.example.lirarymagement_api.data.model;

import java.time.LocalDateTime;

import com.example.lirarymagement_api.data.constant.ACTIVITY;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Log {
  @Id
  private Long Id;
  private Long userId ;
  private Long bookId;
  private String description;
  @Enumerated(EnumType.STRING)
  private ACTIVITY action;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class )
  private LocalDateTime timestamp;

  @PrePersist
  protected void onCreate() {
    timestamp = LocalDateTime.now();
  }
}
