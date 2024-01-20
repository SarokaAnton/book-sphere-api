package com.projects.booksphere.utils.exceptionhandler;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String GENRE_NOT_FOUND = "Genre with id %d not found";
    public static final String GENRE_ALREADY_EXIST = "Genre with name %s already exist";
    public static final String GENRE_UPDATE_EXCEPTION_MESSAGE = "Bad genre update request";
}