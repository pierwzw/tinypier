package com.pier.service;

import com.pier.bean.Book;
import com.pier.dao.BookDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhongweiwu
 * @date 2019/10/26 17:03
 */
@Service
public class BookService {

    @Autowired
    private BookDao bookDao;

    public Book getBook(int id){
        return bookDao.getBook(id);
    }

    public int insertBook(Book book){
        return bookDao.insertBook(book);
    }

    public List<Book> findBooks(String title){
        return bookDao.findBooks(title);
    }

    public List<Book> findBooksByCategory(String category){
        return bookDao.findBooksByCategory(category);
    }

    public void deleteBook(int id){
        bookDao.deleteBook(id);
    }

    public List<Book> listBooks() {
        return bookDao.listBooks();
    }
}
