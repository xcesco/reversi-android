package it.fmt.games.reversi.android.repositories.network.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Cell;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardDeserializer extends StdDeserializer<Board> {
  public BoardDeserializer() {
    super(Board.class);
  }

  @Override
  public Board deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    List<Cell> cells = new ArrayList<>();
    int counter = 0;
    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
      cells.add(new Cell(Coordinates.of(counter / Board.BOARD_SIZE, counter % Board.BOARD_SIZE),
              Piece.valueOf(jsonParser.getValueAsString())));
      counter++;
    }
    Board board = new Board(cells.toArray(new Cell[cells.size()]));
    return board;
  }
}
