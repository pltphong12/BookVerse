package com.example.bookverse.repository.impl;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.QBook;
import com.example.bookverse.repository.custom.BookRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Book> filter(String title, long publisherId, long authorId, long categoryId, LocalDate dateFrom, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBook qBook = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (title != null && !title.isBlank()) {
            builder.and(qBook.title.containsIgnoreCase(title));
        }
        if (publisherId != 0) {
            builder.and(qBook.publisher.id.eq(publisherId));
        }
        if (authorId != 0) {
            builder.and(qBook.authors.any().id.eq(authorId));
        }
        if (categoryId != 0) {
            builder.and(qBook.category.id.eq(categoryId));
        }
        if (dateFrom != null) {
            Instant fromInstant = dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qBook.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Book> books = queryFactory.selectFrom(qBook)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qBook)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(books, pageable, total);
    }
}
