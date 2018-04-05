package com.gigabyte.tugasakhir.vertx.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;
import com.gigabyte.tugasakhir.vertx.model.User;

public class userMessageCodec implements MessageCodec<User, User> {

    @Override
    public void encodeToWire(Buffer buffer, User user) {
        // Easiest ways is using JSON object
        JsonObject jsonToEncode = new JsonObject();
        jsonToEncode.put("username", user.getUsername());

        // Encode object to string
        String jsonToStr = jsonToEncode.encode();

        // Length of JSON: is NOT characters count
        int length = jsonToStr.getBytes().length;

        // Write data into given buffer
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public User decodeFromWire(int position, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = position;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos+=4, _pos+=length);
        JsonObject contentJson = new JsonObject(jsonStr);

        // Get fields
        String username = contentJson.getString("username");

        // We can finally create custom message object
        return new User(username);
    }

    @Override
    public User transform(User user) {
        // If a message is sent *locally* across the event bus.
        // This example sends message just as is
        return user;
    }

    @Override
    public String name() {
        // Each codec must have a unique name.
        // This is used to identify a codec when sending a message and for unregistering codecs.
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        // Always -1
        return -1;
    }
}
