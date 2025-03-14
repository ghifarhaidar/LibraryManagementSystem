package com.maids.Library.repository;

import com.maids.Library.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron,Long> {

}
