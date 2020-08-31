package org.squashtest.hibernatekata.jpa.dto;

public class FilmWithLanguageDto {

    private Integer id;

    private String title;

    private String language;

    public FilmWithLanguageDto(Integer id, String title, String language) {
        this.id = id;
        this.title = title;
        this.language = language;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
