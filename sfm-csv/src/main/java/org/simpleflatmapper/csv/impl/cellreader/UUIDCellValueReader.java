package org.simpleflatmapper.csv.impl.cellreader;

import org.simpleflatmapper.csv.CellValueReader;
import org.simpleflatmapper.csv.ParsingContext;
import org.simpleflatmapper.core.map.ParsingContextProvider;

import java.util.UUID;

public class UUIDCellValueReader implements CellValueReader<UUID> {

	public UUIDCellValueReader() {
	}
	
	@Override
	public UUID read(char[] chars, int offset, int length, ParsingContext parsingContext) {
		if (length > 0) {
			return UUID.fromString(StringCellValueReader.readString(chars, offset, length));
		}
		return null;
	}


    @Override
    public String toString() {
        return "UUIDCellValueReader{}";
    }
}
