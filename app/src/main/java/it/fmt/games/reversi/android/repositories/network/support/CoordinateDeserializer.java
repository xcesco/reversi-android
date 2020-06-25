package it.fmt.games.reversi.android.repositories.network.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.fmt.games.reversi.model.Coordinates;

import java.io.IOException;

public class CoordinateDeserializer extends StdDeserializer<Coordinates> {
  public CoordinateDeserializer() {
    super(Coordinates.class);
  }

  @Override
  public Coordinates deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    String value = jsonParser.readValueAs(String.class);
    Coordinates coords = Coordinates.of(value);
    return coords;
  }
}
