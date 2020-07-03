package it.fmt.games.reversi.android.repositories.network.support;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Coordinates;

public abstract class JSONMapperFactory {
  private JSONMapperFactory() {

  }

  public static ObjectMapper createMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    SimpleModule module = new SimpleModule();
    module.addSerializer(Coordinates.class, new CoordinateSerializer());
    module.addDeserializer(Coordinates.class, new CoordinateDeserializer());
    module.addSerializer(Board.class, new BoardSerializer());
    module.addDeserializer(Board.class, new BoardDeserializer());
    mapper.registerModule(module);

    return mapper;
  }
}