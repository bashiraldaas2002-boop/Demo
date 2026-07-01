package com.example.demo.repository;

import com.example.demo.entity.AppOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
import java.util.List;

public interface AppOrderRepository extends JpaRepository<AppOrder, Long> {

    @Query("SELECT o FROM AppOrder o JOIN FETCH o.user")
    List<AppOrder> findAllWithUserFetch();
}