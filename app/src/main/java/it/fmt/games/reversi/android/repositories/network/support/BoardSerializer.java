package it.fmt.games.reversi.android.repositories.network.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import it.fmt.games.reversi.model.Board;

import java.io.IOException;

public class BoardSerializer extends StdSerializer<Board> {

  public BoardSerializer() {
    this(null);
  }

  public BoardSerializer(Class<Board> t) {
    super(t);
  }

  @Override
  public void serialize(Board value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException {
    jgen.writeStartArray();
    value.getCellStream().forEach(cell -> {
      try {
        jgen.writeString(cell.getPiece().toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    jgen.writeEndArray();
  }
}