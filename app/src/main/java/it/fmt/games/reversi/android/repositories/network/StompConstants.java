package it.fmt.games.reversi.android.repositories.network;

public abstract class StompConstants {
  public static final String HEADER_TYPE = "type";
  public static final String WS_TOPIC_USER_MATCH_DESTINATION = "/topic/user/{uuid}";
  public static final String WS_USER_STATUS_REPLY = "/user/status";
  public static final String WS_APP_USER_DESTINATION = "/app/users/{uuid}/moves";
  public static final String WS_APP_USER_READY_DESTINATION = "/app/users/{uuid}/ready";
  public static final String WS_APP_USER_NOT_READY_DESTINATION = "/app/users/{uuid}/not-ready";

  private StompConstants() {

  }
}
