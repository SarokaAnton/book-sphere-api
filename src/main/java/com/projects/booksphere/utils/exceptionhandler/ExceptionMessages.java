package com.projects.booksphere.utils.exceptionhandler;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String GENRE_NOT_FOUND = "Genre with id %d not found";
    public static final String GENRE_ALREADY_EXIST = "Genre with name %s already exists";
    public static final String GENRE_UPDATE_EXCEPTION_MESSAGE = "Bad genre update request";
    public static final String BOOK_NOT_FOUND = "Book with id %d not found";
    public static final String BOOK_ALREADY_EXIST = "Such book already exists";
    public static final String BOOK_UPDATE_EXCEPTION_MESSAGE = "Bad book update request";
    public static final String AUTHOR_NOT_FOUND = "Author with id %d not found";
    public static final String AUTHOR_ALREADY_EXIST = "This author already exists";
    public static final String AUTHOR_UPDATE_EXCEPTION_MESSAGE = "Bad author update request";
    public static final String TAG_NOT_FOUND = "Tag with id %d not found";
    public static final String TAG_ALREADY_EXIST = "Tag with name %s already exists";
    public static final String TAG_UPDATE_EXCEPTION_MESSAGE = "Bad tag update request";
}