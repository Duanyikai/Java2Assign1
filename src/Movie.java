import java.util.*;

public class Movie {

    String Poster_Link;
    String Series_Title;
    int Released_Year;
    String Certificate;
    int Runtime;
    String[] Genre;
    float IMDB_Rating;
    String Overview;
    String Meta_score;
    String Director;
    String[] stars;
    int No_of_votes;
    int Gross;





    public Movie(String poster_Link, String series_Title, String released_Year, String certificate, String runtime, String genre, String iMDB_Rating, String overview, String meta_score, String director, String star1, String star2, String star3, String star4, String no_of_votes, String gross) {
        Poster_Link = poster_Link.replace('\"',' ').trim();
        Series_Title = series_Title.replace("\"", "");
//        Series_Title = series_Title;
        Released_Year = released_Year.equals("") ? Released_Year = 0 :Integer.parseInt(released_Year);
        Certificate = certificate;
        Runtime = Integer.parseInt(runtime.substring(0,runtime.indexOf(" ")));
        Genre = genre.replace('\"',' ').replace(" ","").split(",");
        this.IMDB_Rating = iMDB_Rating.equals("") ? IMDB_Rating = 0.0f :  Float.parseFloat(iMDB_Rating);
        Overview = overview.replace('\"',' ').trim();
        Meta_score = meta_score;
        Director = director;
        stars = new String[]{star1, star2, star3, star4};
        No_of_votes = no_of_votes.equals("") ? No_of_votes = 0 :Integer.parseInt(no_of_votes);
        Gross = gross.equals("") ? Gross = 0 : Integer.parseInt(gross.replaceAll("\\D",""));
    }

    public String getPoster_Link() {
        return Poster_Link;
    }

    public void setPoster_Link(String poster_Link) {
        Poster_Link = poster_Link;
    }

    public String getSeries_Title() {
        return Series_Title;
    }

    public void setSeries_Title(String series_Title) {
        Series_Title = series_Title;
    }

    public int getReleased_Year() {
        return Released_Year;
    }

    public void setReleased_Year(int released_Year) {
        Released_Year = released_Year;
    }

    public String getCertificate() {
        return Certificate;
    }

    public void setCertificate(String certificate) {
        Certificate = certificate;
    }

    public int getRuntime() {
        return Runtime;
    }

    public void setRuntime(int runtime) {
        Runtime = runtime;
    }

    public String getGenre1() {
        return Genre[0];
    }

    public String getGenre2() {
        if (Genre.length > 1) {
            return Genre[1];
        }
        return "nb";
    }

    public String getGenre3() {
        if (Genre.length > 2) {
            return Genre[2];
        }
        return "nb and np";
    }

    public boolean hasGenre(String genre) {
        if (genre.equals(getGenre1()) || genre.equals(getGenre2()) || genre.equals(getGenre3())) {
            return true;
        } else {
            return false;
        }
    }


    public void setGenre(String[] genre) {
        Genre = genre;
    }

    public float getIMDB_Rating() {
        return IMDB_Rating;
    }

    public void setIMDB_Rating(float IMDB_Rating) {
        this.IMDB_Rating = IMDB_Rating;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getMeta_score() {
        return Meta_score;
    }

    public void setMeta_score(String meta_score) {
        Meta_score = meta_score;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String[] getStars() {
        return stars;
    }

    public boolean hasThisStar(String star) {
        boolean b = false;
        for (String s : stars) {
            if (star.equals(s)) {
                b = true;
                break;
            }
        }
        return b;
    }

    public List<List<String>> getCostars() {
//        int id = 0;
//        String[] temp = stars;
//        for (int i = 0; i < stars.length; i++) {
//            for (int j = i + 1; j < stars.length; j++) {
//                if (stars[i].replaceAll("\\d", "").equals(stars[j].replaceAll("\\d", ""))) {
//                    temp[i] = stars[i] + id;
//                    id++;
//                    temp[j] = stars[j] + id;
//                }
//            }
//        }

        ArrayList<List<String>> coStarsList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                ArrayList<String> coStars = new ArrayList<>();
                coStars.add(stars[i]);
                coStars.add(stars[j]);
                coStars.sort(String::compareToIgnoreCase);
                coStarsList.add(coStars);
            }
        }
        return coStarsList;
    }

    public boolean hasCoStar(List<String> coStar ) {
        ArrayList<List<String>> coStarsList2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                ArrayList<String> coStars = new ArrayList<>();
                coStars.add(stars[i]);
                coStars.add(stars[j]);
                coStars.sort(String::compareToIgnoreCase);
                coStarsList2.add(coStars);
            }
        }

        for (List<String> cs: coStarsList2) {
            if (cs.equals(coStar)) {
                return true;
            }
        }
        return false;
    }

    public void setStars(String[] stars) {
        this.stars = stars;
    }

    public int getNo_of_votes() {
        return No_of_votes;
    }

    public void setNo_of_votes(int no_of_votes) {
        No_of_votes = no_of_votes;
    }

    public int getGross() {
        return Gross;
    }

    public void setGross(int gross) {
        Gross = gross;
    }
}
