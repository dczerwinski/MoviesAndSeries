package com.example.recycleview.RecycleAdapters;

public abstract class ListItem {
    public static final int TYPE_HEADER = 3;
    public static final int TYPE_SERIES = 2;
    public static final int TYPE_MOVIE = 1;
    public String category;
    public String getCategory() {
        return category;
    }

    abstract public int getType();
}

class HeaderItem extends ListItem{


    public HeaderItem(String name) {
        this.category = name;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}

class SeriesItem extends ListItem{

    private String title;
    private String director;
    private String img_file;
    private int numer_of_episodes;

    public SeriesItem(String title, String director, String img_file, int numer_of_episodes, String category) {
        this.title = title;
        this.director = director;
        this.img_file = img_file;
        this.numer_of_episodes = numer_of_episodes;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getImg_file() {
        return img_file;
    }

    public int getNumer_of_episodes() {
        return numer_of_episodes;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int getType() {
        return TYPE_SERIES;
    }
}

class MovieItem extends ListItem{
    private String title;
    private String director;
    private String img_file;

    public MovieItem(String title, String director, String img_file, String category) {
        this.title = title;
        this.director = director;
        this.img_file = img_file;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getImg_file() {
        return img_file;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int getType() {
        return TYPE_MOVIE;
    }
}
