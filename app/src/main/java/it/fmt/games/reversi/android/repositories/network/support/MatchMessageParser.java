package it.fmt.games.reversi.android.repositories.network.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import it.fmt.games.reversi.android.repositories.network.NetworkPlayerHandler;
import it.fmt.games.reversi.android.repositories.network.StompConstants;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessageType;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import timber.log.Timber;
import ua.naiksoftware.stomp.dto.StompMessage;

import static it.fmt.games.reversi.android.repositories.network.NetworkClientImpl.getHeaderValue;

public abstract class MatchMessageParser {

  private MatchMessageParser() {

  }

  public static void parser(ConnectedUser user, @NotNull ObjectMapper objectMapper,
                            @NotNull StompMessage message,
                            @NotNull NetworkPlayerHandler handler
  ) throws JsonProcessingException {
    MatchMessageType messageType = MatchMessageType.valueOf(getHeaderValue(message, StompConstants.HEADER_TYPE));
    Timber.i("%s received message type %s: %s", user.getName(), messageType, message.getPayload());
    MatchMessage matchMessage = objectMapper.readValue(message.getPayload(), messageType.getType());
    matchMessage.accept(handler);
  }
}
