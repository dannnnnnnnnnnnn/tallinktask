package com.dan.conferenceReservation.services;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBaseService<T> {
        ResponseEntity<List<T>> findAll();
        ResponseEntity save(T entity);
        ResponseEntity<T> findById(long id);
        ResponseEntity delete(T entity);
        ResponseEntity deleteById(long id);
}
