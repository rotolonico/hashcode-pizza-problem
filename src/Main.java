import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static int width;
    private static int height;
    private static int minIngredients;
    private static int maxSliceSize;

    public static void main(String[] args) throws IOException {
        String filePath = new File("").getAbsolutePath();
        System.out.println(filePath);
        File input = new File("input.txt");

        BufferedReader br = new BufferedReader(new FileReader(input));

        String pizzaMetadata = br.readLine();
        String[] metadata = pizzaMetadata.split(" ");

        height = Integer.parseInt(metadata[0]);
        width = Integer.parseInt(metadata[1]);
        minIngredients = Integer.parseInt(metadata[2]);
        maxSliceSize = Integer.parseInt(metadata[3]);

        char[][] pizza = new char[width][height];

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if ((pizza[x][y] = (char)br.read()) == '\n'){
                    pizza[x][y] = (char)br.read();
                }
            }
        }

        //char flattening
        char[] flatPizza = new char[width*height];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                flatPizza[y*width + x] = pizza[x][y];
                System.out.print(flatPizza[y*width + x]);
            }
            System.out.println(" ");
        }

        ArrayList<Slice> slices = new ArrayList<>();

        int i = 0;
        while (i < flatPizza.length-maxSliceSize+1){
            int tomatoes = 0;
            int mushrooms = 0;
            for (int j = 0; j < maxSliceSize; j++){
                if (flatPizza[i+j] == 'T'){
                    tomatoes++;
                    System.out.println("Tomato");
                }
                if (flatPizza[i+j] == 'M'){
                    mushrooms++;
                    System.out.println("Mushroom");
                }
            }

            if (tomatoes < minIngredients || mushrooms < minIngredients){
                System.out.println("Skip");
                if (width - (i % width) >= maxSliceSize+1){
                    i++;
                } else {
                    i += width - ((i) % width);
                }
                continue;
            }

            Slice slice = new Slice();
            slice.startX = i % width;
            slice.startY = i / width;
            slice.endX = (i + maxSliceSize-1) % width;
            slice.endY = (i + maxSliceSize-1) / width;
            slice.size = maxSliceSize;
            slices.add(slice);

            if (width - ((i) % width) >= maxSliceSize*2){
                i += maxSliceSize;
            } else {
                i += width - ((i) % width);
            }
        }

        ArrayList<String> outputLines = new ArrayList<>();
        int points = 0;

        outputLines.add(String.valueOf(slices.size()));
        for (Slice slice : slices) {
            outputLines.add(slice.startY + " " + slice.startX + " " + slice.endY + " " + slice.endX);
            System.out.print(slice.startX + " " + slice.endX + " " + slice.startY + " " + slice.endY);
            System.out.println(" ");
            points += slice.size;
        }

        Path output = Paths.get("output.txt");
        Files.write(output, outputLines, Charset.forName("UTF-8"));

        System.out.println(" ");
        System.out.println("Points scored: " + points);
    }
}
