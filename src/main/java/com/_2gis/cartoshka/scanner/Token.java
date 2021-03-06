package com._2gis.cartoshka.scanner;

import com._2gis.cartoshka.Location;

public class Token {
    private final TokenType type;
    private final String text;
    private final Location location;

    public Token(TokenType type, String text, Location location) {
        this.type = type;
        this.text = text;
        this.location = location;
    }

    public TokenType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public String getText() {
        return text;
    }
}