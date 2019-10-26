package com.pier.dao;

import org.apache.ibatis.annotations.Param;

import com.pier.bean.Book;
import java.util.List;

/**
 * @author zhongweiwu
 * @date 2019/3/30 13:39
 */
public interface BookDao {
    Book getBook(int id);

    int insertBook(@Param("book") Book book);

    List<Book> findBooks(String title);

    List<Book> findBooksByCategory(String category);

    void deleteBook(int id);

    List<Book> listBooks();
}
