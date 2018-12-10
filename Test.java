
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;


public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage myStage) {
        String targetImage = "monaLisa-100.jpg";
        Color[][] target = null;
        int maxX = 0;
        int maxY = 0;
        try {
            BufferedImage bi = ImageIO.read(new File(targetImage));
            maxX = bi.getWidth();
            maxY = bi.getHeight();
            ConvexPolygon.max_X = maxX;
            ConvexPolygon.max_Y = maxY;
            target = new Color[maxX][maxY];
            for (int i = 0; i < maxX; i++) {
                for (int j = 0; j < maxY; j++) {
                    int argb = bi.getRGB(i, j);
                    int b = (argb) & 0xFF;
                    int g = (argb >> 8) & 0xFF;
                    int r = (argb >> 16) & 0xFF;
                    int a = (argb >> 24) & 0xFF;
                    target[i][j] = Color.rgb(r, g, b);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(9);
        }
        System.out.println("Read target image " + targetImage + " " + maxX + "x" + maxY);


        Random gen = new Random();

        FindImage sol = new FindImage(target, maxX, maxY);
        List<ConvexPolygon> p = new ArrayList<>();
        for(int i =0; i < 50; i++){
            p.add(new ConvexPolygon(3));
        }
        saveTest(p, maxX, maxY, myStage);
        int step = 30;
        for(int goal = 85; goal > 0; goal -= 5){
            System.out.println("GOAL "+goal);
            List<ConvexPolygon> best = sol.approximate(p, goal, step);
            saveResult(best, maxX, maxY, myStage);
            p = best;
            if(goal < 25)
                goal += 3;
        }


    }

    //Save and show an image as "test"
    private void saveTest(List<ConvexPolygon> ls, int maxX, int maxY, Stage myStage){
        Group image = new Group();
        for (ConvexPolygon p : ls)
            image.getChildren().add(p);

        WritableImage wimg = new WritableImage(maxX,maxY);
        image.snapshot(null,wimg);
        PixelReader pr = wimg.getPixelReader();

        RenderedImage renderedImage = SwingFXUtils.fromFXImage(wimg, null);
        try {
            ImageIO.write(renderedImage, "png", new File("test.png"));
            System.out.println("wrote image in " + "test.png");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Scene scene = new Scene(image, maxX, maxY);
        myStage.setScene(scene);
        myStage.show();

    }

    //Save and show an image as "result"
    private void saveResult(List<ConvexPolygon> ls, int maxX, int maxY, Stage myStage){
        Group image = new Group();
        for (ConvexPolygon p : ls)
            image.getChildren().add(p);

        WritableImage wimg = new WritableImage(maxX,maxY);
        image.snapshot(null,wimg);
        PixelReader pr = wimg.getPixelReader();

        RenderedImage renderedImage = SwingFXUtils.fromFXImage(wimg, null);
        try {
            ImageIO.write(renderedImage, "png", new File("result_.png"));
            System.out.println("wrote image in " + "result_.png");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Scene scene2 = new Scene(image, maxX, maxY);
        myStage.setScene(scene2);
        myStage.show();

    }

}
