package discordBot.main.botIO.input;

import discordBot.main.Bot;
import discordBot.main.fileUtil.Attachments;
import discordBot.main.fileUtil.image.ImageLogic;
import discordBot.main.botIO.output.ChannelHandling.TradingChannelObject;
import discordBot.main.botIO.output.Window.PrintEmbed;
import net.dv8tion.jda.core.entities.*;

import java.io.File;

class Commands {
    private ImageLogic imageLogic = new ImageLogic();
    private String preFix = ".";
    void serverAdmin(User user, Message objMsg, MessageChannel objChannel) {
        //Splits the command at spaces
        String[] stringInput = objMsg.getContentRaw().split(" ");

        //prints out all the channels available
        if (objMsg.getContentRaw().equalsIgnoreCase(preFix + "ShowAllChannels")) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < Bot.textChannels.size(); i++) {
                s.append(i).append(".  ").append(Bot.textChannels.get(i).toString()).append("\r\n");
            }
            objChannel.sendMessage(s).queue();
        }
        //tests printing in another channel
        if (objMsg.getContentRaw().equalsIgnoreCase(preFix + "printIn")) {
            Bot.textChannels.get(4).sendMessage("printing in another channel!").queue();

        }

        if (objMsg.getContentRaw().equalsIgnoreCase(preFix + "sendTestImage")) {
            objChannel.sendFile(new File("ImagesDownloaded/test.png")).queue();
        }


    }

    void removeItems(Message objMsg, MessageChannel objChannel, Bot main) {
        boolean removeAll = true;
        if (objMsg.getContentRaw().startsWith("!wipe") && objMsg.getContentRaw().contains(" ")) {

            String[] temp = objMsg.getContentRaw().substring(6).split(" ");
            for (int i = 0; i < temp.length; i++) {
                temp[i] = temp[i].toLowerCase();

            }
            for (String s: temp) {
                if (s.startsWith("<:")) {
                    removeAll = false;
                }
            }
            TradingInput tradingInput = new TradingInput();
            String[] items = tradingInput.returnRemoveItems(temp,removeAll);
            String[] channelCommandsArray = {"ba", "ve", "se", "me", "ca", "va", "ka", "ar"};
            int[] channelNumberArray = {1, 2, 3, 4, 5, 6};

            outerLoop:
            for (String channelCommand : channelCommandsArray) {
                for (int number : channelNumberArray) {
                    //if channel limit is met it will break
                    if (channelCommand.equals("ka") && number < 4 || channelCommand.equals("ar") && number < 1) {
                        break outerLoop;
                    } else if (temp[0].startsWith(channelCommand) && temp[0].endsWith(String.valueOf(number))) {
                        TradingChannelObject tradingChannel = main.channelManager.getTradingChannelWithCallSignAndId(temp[0].substring(0, 2), Integer.parseInt(String.valueOf(temp[0].charAt(2))), main);
                        for (String item : items) {
                            tradingChannel.removeItem(item);
                        }
                        break outerLoop;
                    }
                }

            }
            printEmbed(objChannel,main);
        }
    }

    void addItems(User objUser, Message objMsg, MessageChannel objChannel, Bot main) {

        Attachments attachments = new Attachments();
        if (!objMsg.getContentRaw().startsWith("!")) {

            String[] inputMsg;
            String[] temp;
            if (!objMsg.getContentRaw().contains(" ")) {
                inputMsg = new String[2];
                temp = new String[2];
                temp[0] = "N/A";
                temp[1] = "N/A";
                inputMsg[0] = objMsg.getContentRaw();
                inputMsg[1] = "This string is useless but don't remove it";
            } else {
                inputMsg = objMsg.getContentRaw().split(" ");
                temp = objMsg.getContentRaw().substring(4).split(" ");


            }

            for (int i = 0; i < inputMsg.length; i++) {
                inputMsg[i] = inputMsg[i].toLowerCase();
            }

            TradingInput tradingInput = new TradingInput();
            String[] items = tradingInput.returnItems(temp);
            String[] amount = tradingInput.returnAmount(temp);
            String[] channelCommandsArray = {"ba", "ve", "se", "me", "ca", "va", "ka", "ar"};
            int[] channelNumberArray = {1, 2, 3, 4, 5, 6};

            outerLoop:
            for (String channelCommand : channelCommandsArray) {
                for (int number : channelNumberArray) {
                    //if channel limit is met it will break
                    if (inputMsg[0].substring(0, 1).equals("ar") && number >= 2 || inputMsg[0].substring(0, 1).equals("ka") && number >= 5) {
                        break outerLoop;
                    }
                    else if (inputMsg[0].startsWith(channelCommand) && inputMsg[0].endsWith(String.valueOf(number))) {
                        if (attachments.CheckForAttachments(objMsg)) {
                            if (imageLogic.compareImage(objChannel, objMsg, main)) {
                                printEmbed(objChannel,main);
                            }
                        } else {
                            TradingChannelObject tradingChannel = main.channelManager.getTradingChannelWithCallSignAndId(inputMsg[0].substring(0, 2), Integer.parseInt(String.valueOf(inputMsg[0].charAt(2))), main);
                            for (int i = 0; i < items.length; i++) {
                                try {
                                    tradingChannel.addItem(items[i],amount[i]);

                                } catch (ArrayIndexOutOfBoundsException e) {
                                    tradingChannel.addItem(items[i],null);

                                }
                            }
                            printEmbed(objChannel,main);
                        }
                        break outerLoop;
                    }

                }
            }
        }
    }
    private void printEmbed(MessageChannel objChannel, Bot main) {
        PrintEmbed printEmbed = new PrintEmbed();
        printEmbed.printEmbed(main,objChannel);
    }
    void checkIfChannelsAreNeeded(Bot main){
        //creates trading channels
        if (main.tradingChannelObjects.isEmpty()) {
            main.channelManager.clearTradingChannels(main);
            main.channelManager.initiateTradingChannels(main);
        }
    }
}
