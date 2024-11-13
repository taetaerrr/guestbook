package com.example.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.entity.QGuestBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

// QuerydslPredicateExecutor 동적쿼리
public interface GuestBookRepository extends JpaRepository<GuestBook, Long>, QuerydslPredicateExecutor<GuestBook> {

    default Predicate makePredicate(String type, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();
        QGuestBook qGuestBook = QGuestBook.guestBook;

        // gno > 0 : range scan
        builder.and(qGuestBook.gno.gt(0L));

        if (type == null)
            return builder;

        // content like '%검색어%' or title like '%검색어%' or writer like '%검색어%'
        BooleanBuilder conditionbuilder = new BooleanBuilder();
        if (type.equals("c")) { // 카테고리
            conditionbuilder.or(qGuestBook.content.contains(keyword));
        }
        if (type.equals("t")) { // 제목
            conditionbuilder.or(qGuestBook.title.contains(keyword));
        }
        if (type.equals("w")) { // 저자
            conditionbuilder.or(qGuestBook.writer.contains(keyword));
        }

        // gno > 0 and(content like '%검색어%' or title like '%검색어%' or writer like
        // '%검색어%')
        builder.and(conditionbuilder);

        return builder;
    }
}
