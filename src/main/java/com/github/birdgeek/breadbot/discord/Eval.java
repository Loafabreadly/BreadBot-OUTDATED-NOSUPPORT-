package com.github.birdgeek.breadbot.discord;

import com.github.birdgeek.breadbot.utility.ConfigFile;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.concurrent.*;

class Eval extends ListenerAdapter {

    private final ScheduledExecutorService eval = Executors.newScheduledThreadPool(1);
    private final ScriptEngine engine;

    Eval() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util);");

        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        JDA jda = e.getJDA();
        TextChannel channel = e.getChannel();
        User author = e.getAuthor();
        Message message = e.getMessage();
        String msg = message.getContent();
        Guild guild = e.getGuild();

        char prefix = ConfigFile.getCallsign();

        //Specifically listen for the eval command
        if (!author.getId().equals("121795270806601730")
                || !msg.toLowerCase().startsWith(prefix + "eval")
                || !msg.contains(" ")) return;

        String input = msg.substring(msg.indexOf(' ') + 1);

        engine.put("e", e);
        engine.put("event", e);
        engine.put("api", jda);
        engine.put("jda", jda);
        engine.put("channel", channel);
        engine.put("author", author);
        engine.put("message", message);
        engine.put("guild", guild);
        engine.put("input", input);
        engine.put("mentionedUsers", message.getMentionedUsers());

        ScheduledFuture<?> future = eval.schedule(() -> {

            Object out = null;
            try {
                out = engine.eval(
                        "(function() {" +
                                "with (imports) {\n" + input + "\n}" +
                                "})();");

            } catch (Exception ex) {
                sendMessage("**Exception**: ```\n" + ex.getLocalizedMessage() + "```", e);
                return;
            }

            String outputS;
            if (out == null)
                outputS = "`Task executed without errors.`";
            else if (out.toString().length() >= 1980)
                outputS = "The output is longer than 2000 chars!";
            else
                outputS = "Output: ```\n" + out.toString().replace("```", "\\`\\`\\`") + "\n```";

            sendMessage(outputS, e);

        }, 0, TimeUnit.MILLISECONDS);

        try {
            future.get(10, TimeUnit.SECONDS);

        } catch (TimeoutException ex) {
            future.cancel(true);
            sendMessage("Your task exceeds the time limit!", e);

        } catch (ExecutionException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(String msg, GuildMessageReceivedEvent e) {
        TextChannel channel = e.getChannel();

        if (channel.checkPermission(e.getJDA().getSelfInfo(), Permission.MESSAGE_WRITE) && e.getGuild().checkVerification()) {
            if (msg.contains("@everyone"))
                msg = msg.replace("@everyone", "@\u180Eeveryone");
            if (msg.contains("@here"))
                msg = msg.replace("@here", "@\u180Ehere");

            channel.sendMessage(msg);
        }
    }
}