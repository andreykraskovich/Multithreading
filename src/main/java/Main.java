import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Введите адрес сайта: ");
        String url = sc.nextLine();

        System.out
                .println("Введите количество создаваемых потоков: ");
        int numThreads = sc.nextInt();

        System.out.println("Start");
        long start = System.currentTimeMillis();
        Link link = new Link(url, url);
        String siteMap = numThreads == 0 ? new ForkJoinPool().invoke(link)
                : new ForkJoinPool(numThreads).invoke(link);

        System.out.println("Finish");
        System.out
                .println("Время сканирования " + ((System.currentTimeMillis() - start) / 1000) + " сек.");
        writeFiles(siteMap);
    }

    private static void writeFiles(String map) {
        String filePath = "siteMap.txt";

        File file = new File(filePath);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
