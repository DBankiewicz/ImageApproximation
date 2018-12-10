import javafx.scene.Group;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FindImage {

    Color[][] target;
    int maxX;
    int maxY;
    Random gen;


    public FindImage(Color[][] img, int x, int y ){
        target = img;
        maxX = x;
        maxY = y;
        gen = new Random();
    }

    //Calculate polygon vector error
    public double calculatePolyVectorError( List<ConvexPolygon> ls){
        Group image = new Group();
        for (ConvexPolygon p : ls){
            image.getChildren().add(p);
        }

        WritableImage wimg = new WritableImage(maxX, maxY);
        image.snapshot(null, wimg);
        PixelReader pr = wimg.getPixelReader();

        double res=0;
        for (int i=0;i<maxX;i++){
            for (int j=0;j<maxY;j++){
                Color c = pr.getColor(i, j);
                res += Math.pow(c.getBlue()-target[i][j].getBlue(),2)
                        +Math.pow(c.getRed()-target[i][j].getRed(),2)
                        +Math.pow(c.getGreen()-target[i][j].getGreen(),2);
            }
        }
        return Math.sqrt(res);
    }

    //Mutate image
    public List<ConvexPolygon> mutateImage(List<ConvexPolygon> p, int step){
        List<ConvexPolygon> mutated = new ArrayList<>();
        int k = gen.nextInt(50);
        for(int i = 0; i < 50; i++){
            if(i != k)
                mutated.add(p.get(i));
            else{
                int g = gen.nextInt(5);

                switch (g) {
                    //change of opacity
                    case 0:
                        ConvexPolygon mutation =p.get(i).clone();
                        mutation.setOpacity(gen.nextDouble());
                        mutated.add(mutation);
                        break;
                    //change of color red
                    case 1:
                        mutation = p.get(i).clone();
                        Color mutColor = (Color) mutation.getFill();
                        double blue = mutColor.getBlue();
                        double green = mutColor.getGreen();
                        mutation.setFill(Color.color(gen.nextDouble(), green, blue ));
                        mutated.add(mutation);
                        break;
                    //change of green color
                    case 2:
                        mutation = p.get(i).clone();
                        mutColor = (Color) mutation.getFill();
                        double red = mutColor.getRed();
                        blue = mutColor.getBlue();
                        mutation.setFill(Color.color(red, gen.nextDouble(), blue ));
                        mutated.add(mutation);
                        break;
                    //change of blue color
                    case 3:
                        mutation = p.get(i).clone();
                        mutColor = (Color) mutation.getFill();
                        red = mutColor.getRed();
                        green = mutColor.getGreen();
                        mutation.setFill(Color.color(red, green, gen.nextDouble() ));
                        mutated.add(mutation);
                        break;
                    case 4:
                        //move around triangle a little
                        if(p.get(i).getPoints().size() == 6 ){
                            ConvexPolygon clo = p.get(i).clone();
                            int point = gen.nextInt(6);
                            double val = clo.getPoints().get(point);
                            clo.getPoints().set(point, val + Math.pow(-1, gen.nextInt(2))*gen.nextDouble() * step);
                            mutated.add(clo);
                        }
                        //new polygon with the same color
                        else{
                            mutation = new ConvexPolygon(7);
                            mutation.setFill(p.get(i).getFill());
                            mutated.add(mutation);
                        }
                        break;
                    //no mutation
                    default:
                        System.out.println("DEFAULT");
                        mutated.add(p.get(i).clone());
                        break;
                }
            }
        }
        return mutated;
    }

    //Approximate
    public List<ConvexPolygon> approximate(List<ConvexPolygon> p, double goal, int step){
        double error = calculatePolyVectorError(p);
        int generation = 0, round = 0, total = 0;
        while(error > goal){
            List<ConvexPolygon> mutated = mutateImage(p, step);
            double e = calculatePolyVectorError(mutated);
            if(e < error){
                total += round;
                generation += 1;
                System.out.println(generation+"Error "+e+" "+round+" "+total);
                error = e;
                p = mutated;
                round = 0;
            }
            round += 1;
        }
        return p;
    }
}
