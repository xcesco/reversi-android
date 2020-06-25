package it.fmt.games.reversi.android.repositories.network.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import it.fmt.games.reversi.model.Coordinates;

import java.io.IOException;

public class CoordinateSerializer extends StdSerializer<Coordinates> {

  public CoordinateSerializer() {
    this(null);
  }

  public CoordinateSerializer(Class<Coordinates> t) {
    super(t);
  }

  @Override
  public void serialize(Coordinates value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException {
    jgen.writeString(value.toString());
  }
}