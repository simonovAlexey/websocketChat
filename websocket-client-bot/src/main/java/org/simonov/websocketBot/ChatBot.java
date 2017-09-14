/**
 * ChatBot.java
 * http://programmingforliving.com
 */
package org.simonov.websocketBot;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.net.URI;

/**
 * ChatBot
 */
public class ChatBot {

    public static void main(String[] args) throws Exception {
        try {
            final ChatClientEndpoint clientEndPoint = new ChatClientEndpoint(new URI("ws://localhost:8080/ch/chat"));
            clientEndPoint.addMessageHandler(
                    new ChatClientEndpoint.MessageHandler() {
                        public void handleMessage(String message) {
                            JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
                            String userName = jsonObject.getString("user");
                            if (!"bot".equals(userName)) {
                                String mess = jsonObject.getString("message");
                                System.out.println("Handle and send message to: " + userName + " : " + mess);

                                clientEndPoint.sendMessage(getMessage("Hello " + userName + ", How are you?"));
                                // other bot logic goes here.. :)
                            }
                        }
                    });


            while (true) {
                clientEndPoint.sendMessage(getMessage("Hi There!!"));
                Thread.sleep(30000);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * Create a json representation.
     *
     * @param message
     * @return
     */
    private static String getMessage(String message) {
        return Json.createObjectBuilder()
                .add("user", "bot")
                .add("message", message)
                .build()
                .toString();
    }
}
