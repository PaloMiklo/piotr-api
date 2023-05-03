
package com.api.piotr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.piotr.entity.ImageTable;

public interface ImageRepository extends _HibernateRepository<ImageTable>, JpaRepository<ImageTable, Long> {
}
