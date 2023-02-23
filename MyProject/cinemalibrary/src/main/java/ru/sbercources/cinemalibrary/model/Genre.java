package ru.sbercources.cinemalibrary.model;

public enum Genre {
    ROMANTIC_COMEDY ("Романтическая комедия"),
    SCIENCE_FICTION("Научная фантастика"),
    HORROR("Ужасы"),
    DOCUMENTARY("Документалистика"),
    ANIMATED_FILM("Анимация"),
    ACTION("Боевик"),
    ТHRILLER("Триллер"),
    DRAMA("Драма"),
    COMEDY("Комедия"),
    ADVENTURE("Приключения");

    private  final  String genreText;

    Genre(String genreText) {
        this.genreText = genreText;
    }
    public  String  getGenreText() {
        return this.genreText;
    }
}
