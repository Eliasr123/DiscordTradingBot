package discordBot.main;

import discordBot.main.FileUtil.image.ImageLogic;

import java.awt.image.BufferedImage;
import java.io.File;

public class TestingGround {
    private static ImageLogic imageLogic = new ImageLogic();
    public static void main(String[] args) {
        BufferedImage ref = null;
        ref = ImageLogic.fileManager.load(new File("ImagesDownloaded/StaticRef1.png"));
        if (ref != null) {
            imageLogic.createSubImages(ref);
        }

        //imageLogic.compareImageTest(new File("ImagesDownloaded/staticrefser.png"),new File("ImagesDownloaded/ref4.png"));

    }
    private void runTest() {


    }
}