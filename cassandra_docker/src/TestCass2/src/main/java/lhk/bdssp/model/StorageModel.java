package lhk.bdssp.model;

import lhk.bdssp.beans.Book;

import java.util.List;

/**
 * Created by dmitry on 2/27/17.
 */
public interface StorageModel {
    StatusType storeBook(Book bean);
    List<Book> searchBookByName(String searchPattern);
    List<Book> searchBookByContent(String searchPattern);
}
