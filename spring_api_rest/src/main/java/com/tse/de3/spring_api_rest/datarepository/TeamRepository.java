package com.tse.de3.spring_api_rest.datarepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tse.de3.spring_api_rest.datamodel.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
