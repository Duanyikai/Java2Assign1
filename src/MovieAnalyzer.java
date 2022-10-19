import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class MovieAnalyzer {

    List<Movie> movies;
    String dataset_path;

    public MovieAnalyzer(String dataset_path) throws IOException {
        this.dataset_path = dataset_path;
        File csv = new File(dataset_path);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr;
        BufferedReader br = null;

        try {
            isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String line;
        ArrayList<String> records = new ArrayList<>();
        assert br != null;
        br.readLine();

        try {
            while ((line = br.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stream<Movie> movieStream = records.stream()
                .map(l -> l.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1))
                .map(a -> new Movie(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7],
                        a[8], a[9], a[10], a[11], a[12], a[13], a[14], a[15]));
        movies = movieStream.toList();
    }

    public Map<Integer, Integer> getMovieCountByYear() {
        Stream<Movie> movieStream = movies.stream();
        Map<Integer, Long> collect = movieStream
                .collect((Collectors.groupingBy(Movie::getReleased_Year, Collectors.counting())));
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Map.Entry<Integer, Long> entry : collect.entrySet()) {
            map.put(entry.getKey(), entry.getValue().intValue());
        }
        TreeMap<Integer, Integer> map2 = new TreeMap<>(new MapKeyComparator());
        map2.putAll(map);
        return map2;
    }

    static class MapKeyComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer s1, Integer s2) {
            return -s1.compareTo(s2);
        }
    }

    public Map<String, Integer> getMovieCountByGenre() {
        Stream<Movie> movieStream = movies.stream();
        Map<String, Long> collect1 = movieStream
                .collect((Collectors.groupingBy(Movie::getGenre1, Collectors.counting())));
        movieStream = movies.stream();
        Map<String, Long> collect2 = movieStream.filter(m -> m.Genre.length > 1)
                .collect((Collectors.groupingBy(Movie::getGenre2, Collectors.counting())));
        movieStream = movies.stream();
        Map<String, Long> collect3 = movieStream.filter(m -> m.Genre.length > 2)
                .collect((Collectors.groupingBy(Movie::getGenre3, Collectors.counting())));
        Map<String, Long> collect = addMap1(collect3, addMap1(collect1, collect2));
        HashMap<String, Integer> map = new HashMap<>();
        for (Map.Entry<String, Long> entry : collect.entrySet()) {
            map.put(entry.getKey(), entry.getValue().intValue());
        }

        Map<String, Integer> map2 = map
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        if (o1 != null && o2 != null) {
                            if (o1.getValue().equals(o2.getValue())) {
                                String s1 = (String) o1.getKey();
                                String s2 = (String) o2.getKey();
                                return -s1.compareTo(s2);
                            } else {
                                return o1.getValue().compareTo(o2.getValue());
                            }
                        } else {
                            throw new RuntimeException();
                        }
                    }
                }))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        return map2;
    }

    public Map<String, Long> addMap1(Map<String,Long> map1, Map<String,Long> map2) {
        Set<String> keySet1 = map1.keySet();
        Iterator<String> iterator1 = keySet1.iterator();
        String key;
        for (int index = 0; index < keySet1.size(); index++) {
            key = iterator1.next();
            Long valueTemp1 = map1.get(key);
            Long valueTemp2 = map2.get(key);
            if (null == valueTemp1 || null == valueTemp2) {
                continue;
            }
            map1.put(key, valueTemp1+valueTemp2);
        }
        Set<String> keySet2 = map2.keySet();
        Iterator<String> iterator2 = keySet2.iterator();
        for (int index = 0; index < keySet2.size(); index++) {
            key = iterator2.next();
            Long valueTemp1 = map1.get(key);
            Long valueTemp2 = map2.get(key);
            if (null == valueTemp2) {
                continue;
            }
            if (null == valueTemp1) {
                map1.put(key, valueTemp2);
            }
        }
        return map1;
    }

    public Map<List<String>, Integer> getCoStarCount() {
        Map<List<String>, Integer> collect = new HashMap<>();
        LinkedHashSet<List<String>> set = new LinkedHashSet<>();
        for (Movie movie: movies) {
            List<List<String>> list = movie.getCostars();
            set.addAll(list);
        }
        List<List<String>> realCoStarsList = set.stream().toList();
        for (List<String> costars: realCoStarsList) {
            List<Movie> coStaredMovie = movies.stream().filter(m -> m.hasCoStar(costars)).toList();
            collect.put(costars, coStaredMovie.size());
        }

//        for (int i = 0; i < 6; i++) {
//            Stream<Movie> movieStream = movies.stream();
//            int finalI = i;
//            Map<List<String>, Long> temp = movieStream
//                    .collect((Collectors.groupingBy(e -> e.getCostars().get(finalI), Collectors.counting())));
//            collect = addMap2(collect,temp);
//        }
//
//        HashMap<List<String>, Integer> map = new HashMap<>();
//        for (Map.Entry<List<String>, Long> entry : collect.entrySet()) {
//            map.put(entry.getKey(), entry.getValue().intValue());
//        }
        return collect;
    }

    public Map<List<String>, Long> addMap2(Map<List<String>,Long> map1, Map<List<String>,Long> map2) {
        Set<List<String>> keySet1 = map1.keySet();
        Iterator<List<String>> iterator1 = keySet1.iterator();
        List<String> key;
        for (int index = 0; index < keySet1.size(); index++) {
            key = iterator1.next();
            Long valueTemp1 = map1.get(key);
            Long valueTemp2 = map2.get(key);
            if (null == valueTemp1 || null == valueTemp2) {
                continue;
            }
            map1.put(key, valueTemp1+valueTemp2);
        }
        Set<List<String>> keySet2 = map2.keySet();
        Iterator<List<String>> iterator2 = keySet2.iterator();
        for (int index = 0; index < keySet2.size(); index++) {
            key = iterator2.next();
            Long valueTemp1 = map1.get(key);
            Long valueTemp2 = map2.get(key);
            if (null == valueTemp2) {
                continue;
            }
            if (null == valueTemp1) {
                map1.put(key, valueTemp2);
            }
        }
        return map1;
    }


    public List<String> getTopMovies(int top_k, String by){
        if (by.equals("runtime")) {
            List<Movie> top = movies.stream().sorted((o1, o2) -> {
                if (o1 != null && o2 != null) {
                    if (o1.Runtime == o2.Runtime) {
                        return o1.Series_Title.compareTo(o2.Series_Title);
                    } else {
                        return -Integer.compare(o1.Runtime, o2.Runtime);
                    }
                } else {
                    throw new RuntimeException();
                }
            }).toList();
            List<Movie> topK = top.subList(0, top_k);
            ArrayList<String> topKString = new ArrayList<>();
            for (Movie movie : topK) {
                topKString.add(movie.Series_Title);
            }
            return topKString;
        }
        if (by.equals("overview")) {
            List<Movie> top = movies.stream().sorted((o1, o2) -> {
                if (o1 != null && o2 != null) {
                    if (o1.Overview.length() == o2.Overview.length()) {
                        return o1.Series_Title.compareTo(o2.Series_Title);
                    } else {
                        return -Integer.compare(o1.Overview.length(), o2.Overview.length());
                    }
                } else {
                    throw new RuntimeException();
                }
            }).toList();
            List<Movie> topK = top.subList(0, top_k);
            ArrayList<String> topKString = new ArrayList<>();
            for (Movie movie : topK) {
                topKString.add(movie.Series_Title);
            }
            return topKString;
        }
        return null;
    }

    public List<String> getTopStars(int top_k, String by) {
        LinkedHashSet<String> allStarSet = new LinkedHashSet<>();
        for (Movie movie: movies) {
            allStarSet.addAll(Arrays.asList(movie.stars));
        }
        List<String> allStarList = allStarSet.stream().toList();
        ArrayList<Star> stars = new ArrayList<>();
        for (int i = 0; i < allStarList.size(); i++) {
            int finalI = i;
            List<Movie> staredMovies = movies.stream().filter(m -> m.hasThisStar(allStarList.get(finalI))).toList();
            double averageGross = getAverageGross(staredMovies);
            double averageRate = getAverageRate(staredMovies);
            stars.add(new Star(allStarList.get(i), averageGross, averageRate));
        }
        if (by.equals("gross")) {
            List<Star> top = stars.stream().sorted((o1, o2) -> {
                if (o1 != null && o2 != null) {
                    if (o1.averageGross == o2.averageGross) {
                        return o1.starName.compareTo(o2.starName);
                    } else {
                        return -Double.compare(o1.averageGross, o2.averageGross);
                    }
                } else {
                    throw new RuntimeException();
                }
            }).toList();
            List<Star> topK = top.subList(0, top_k);
            ArrayList<String> topKString = new ArrayList<>();
            for (Star star : topK) {
                topKString.add(star.starName);
            }
            return topKString;
        }
        if (by.equals("rating")) {
            List<Star> top = stars.stream().sorted((o1, o2) -> {
                if (o1 != null && o2 != null) {
                    if (o1.averageRate == o2.averageRate) {
                        return o1.starName.compareTo(o2.starName);
                    } else {
                        return -Double.compare(o1.averageRate, o2.averageRate);
                    }
                } else {
                    throw new RuntimeException();
                }
            }).toList();
            List<Star> topK = top.subList(0, top_k);
            ArrayList<String> topKString = new ArrayList<>();
            for (Star star : topK) {
                topKString.add(star.starName);
            }
            return topKString;
        }
        return null;
    }

    public double getAverageGross(List<Movie> staredMovies) {
        double Sum = 0;
        int realSize = 0;
        for (Movie movie : staredMovies) {
            if (movie.Gross == 0) {
                continue;
            }
            Sum += movie.Gross;
            realSize++;
        }
        if (realSize == 0) {
            return 0;
        }
        BigDecimal num1 = new BigDecimal(Sum);
        BigDecimal num2 = new BigDecimal(realSize);
        BigDecimal result = num1.divide(num2, BigDecimal.ROUND_HALF_DOWN, BigDecimal.ROUND_HALF_UP);
        return result.doubleValue();
    }

    public double getAverageRate(List<Movie> staredMovies) {
        double Sum = 0;
        int realSize = 0;
        for (Movie movie : staredMovies) {
            if (movie.IMDB_Rating == 0) {
                continue;
            }
            Sum += movie.IMDB_Rating;
            realSize++;
        }
        if (realSize == 0) {
            return 0;
        }
//        BigDecimal num1 = new BigDecimal(Sum);
//        BigDecimal num2 = new BigDecimal(realSize);
//        BigDecimal result = num1.divide(num2, BigDecimal.ROUND_HALF_DOWN, BigDecimal.ROUND_DOWN);
//        return result.doubleValue();
        return  Sum / realSize;
    }

    public List<String> searchMovies(String genre, float min_rating, int max_runtime) {
        List<Movie> searchedMovies = new ArrayList<>(movies.stream().filter(m -> m.hasGenre(genre)).filter(m -> m.getIMDB_Rating() >= min_rating).filter(m -> m.getRuntime() <= max_runtime).toList());
        searchedMovies.sort((o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.Series_Title.compareTo(o2.Series_Title);
            } else {
                throw new RuntimeException();
            }
        });
        ArrayList<String> result = new ArrayList<>();
        for (Movie searchedMovie : searchedMovies) {
            result.add(searchedMovie.Series_Title);
        }
        return result;

    }

















}