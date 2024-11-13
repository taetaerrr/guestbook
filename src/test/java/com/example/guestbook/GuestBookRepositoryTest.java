package com.example.guestbook;

import java.util.concurrent.Flow.Publisher;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.entity.QGuestBook;
import com.example.guestbook.repository.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

@SpringBootTest
public class GuestBookRepositoryTest {

    @Autowired
    private GuestBookRepository guestBookRepository;

    @Test
    public void testGuestBookInsert() {

        IntStream.rangeClosed(1, 300).forEach(i -> {

            GuestBook guestBook = GuestBook.builder()
                    .writer("작가" + i)
                    .title("Book title " + i)
                    .content("content" + i)
                    .build();
            guestBookRepository.save(guestBook);
        });
    }

    @Test
    public void testUpdate() {
        // 300 번 수정
        GuestBook guestBook = guestBookRepository.findById(300L).get();
        guestBook.setContent("300번 content");
        guestBook.setTitle("300번 title");
        guestBookRepository.save(guestBook);
    }

    @Test
    public void testSearch() {
        // 제목에 1이라는 글자가 들어있는 게시물 조회

        QGuestBook qGuestBook = QGuestBook.guestBook;

        // 1페이지에 10개만 갖고와라
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qGuestBook.title.contains("1"));

        // Predicate predicate(BooleanBuilder), Pageable pageable;
        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> System.out.println(guestbook));
    }

    @Test
    public void testSearch2() {
        // 제목or 내용에 1이라는 글자가 들어있고, gno > 0

        // 1페이지에 10개만 갖고와라
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        // Predicate predicate(BooleanBuilder), Pageable pageable;
        Page<GuestBook> result = guestBookRepository.findAll(guestBookRepository.makePredicate("tc", "title"),
                pageable);

        result.stream().forEach(guestbook -> System.out.println(guestbook));
    }

    @Test
    public void testDelete() {
        guestBookRepository.deleteById(250L);
    }
}
