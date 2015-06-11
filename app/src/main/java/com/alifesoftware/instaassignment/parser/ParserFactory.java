package com.alifesoftware.instaassignment.parser;

import com.alifesoftware.instaassignment.interfaces.IPopularImageParser;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class ParserFactory {
    // Enum for Parser Types in case we want to support different Parser
    public static enum ParserType {
        PARSER_INSTAGRAM_JSON, // Uses standard JSON library
        PARSER_INSTAGRAM_GSON // In case I have time and I want to implement a Gson Based Parser as well
    }

    public static IPopularImageParser createParser(ParserType type) {
        IPopularImageParser parser = null;
        // Maybe use Switch-Case
        if(type == ParserType.PARSER_INSTAGRAM_JSON) {
            parser = new InstagramPopularPictureParserJson();
        }

        else if(type == ParserType.PARSER_INSTAGRAM_GSON) {
            parser = null; // If I have time and I implement a Gson Based Parser, then create the object here
        }

        return parser;
    }
}
