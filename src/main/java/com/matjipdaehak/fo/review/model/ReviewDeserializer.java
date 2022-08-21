package com.matjipdaehak.fo.review.model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewDeserializer extends StdDeserializer<Review> {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private final SimpleDateFormat dateFormat;

    public ReviewDeserializer(){ this(null); }

    public ReviewDeserializer(Class<?> vc){
        super(vc);
        dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        mapper = new ObjectMapper();
        reader = mapper.readerFor(new TypeReference<List<String>>() {});
    }

    @Override
    public Review deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        Review review = new Review();
        JsonNode json = jsonParser.getCodec().readTree(jsonParser);

        review.setPlaceId(
                json.get("place_id").asInt()
        );

        review.setUserId(
                json.get("user_id").asText()
        );

        try{
            review.setPostDate(
                    this.dateFormat.parse(json.get("post_date").asText()));
        }catch (ParseException ex){

        }

        review.setPostText(
                json.get("post_text").asText()
        );

        review.setRating(
                json.get("rating").asInt()
        );

        review.setImageUrls(
                reader.readValue(json.get("image_urls"))
        );

        return review;
    }
}
