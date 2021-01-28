package com.tse.de3.spring_api_rest.datarepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tse.de3.spring_api_rest.datamodel.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
