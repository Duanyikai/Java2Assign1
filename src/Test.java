import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class Test {
    public static void main(String[] args) throws IOException {
        MovieAnalyzer movieAnalyzer = new MovieAnalyzer("D:\\Java项目\\CS209Java2项目\\A1_Sample\\resources\\imdb_top_500.csv");
//        System.out.println(movieAnalyzer.getMovieCountByYear());
//        System.out.println(movieAnalyzer.getMovieCountByGenre());
        Map<List<String>, Integer> coStarCount = movieAnalyzer.getCoStarCount();
//        System.out.println(coStarCount.size());
        coStarCount.entrySet().stream().sorted(Collections.reverseOrder(comparingByValue())).forEach(System.out::println);
//        System.out.println(movieAnalyzer.getCoStarCount());
//        System.out.println(movieAnalyzer.getTopStars(80, "rating"));

    }
}
