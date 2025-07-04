package com.example.bookverse.service;

import com.example.bookverse.domain.Publisher;
import com.example.bookverse.domain.response.ResPagination;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PublisherService {
    // Create
    Publisher create(Publisher publisher) throws Exception;

    //Update
    Publisher update(Publisher publisher) throws Exception;

    // Fetch a one
    Publisher fetchPublisherById(long id) throws Exception;

    // Fetch all
    List<Publisher> fetchAllPublisher() throws Exception;

    ResPagination fetchAllPublisherWithPaginationAndFilter(String name, LocalDate dataFrom, Pageable pageable) throws Exception;

    // Delete
    void delete(long id) throws Exception;
}
