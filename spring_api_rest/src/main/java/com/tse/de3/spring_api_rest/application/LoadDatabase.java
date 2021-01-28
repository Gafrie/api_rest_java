package com.tse.de3.spring_api_rest.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tse.de3.spring_api_rest.datamodel.Player;
import com.tse.de3.spring_api_rest.datamodel.Team;
import com.tse.de3.spring_api_rest.datarepository.PlayerRepository;
import com.tse.de3.spring_api_rest.datarepository.TeamRepository;

@Configuration
public class LoadDatabase {

	@Bean
	CommandLineRunner initDatabase(TeamRepository teamRepository, PlayerRepository playerRepository) {
		return args -> {

			Player player1 = new Player("Luc", "Abalo", 36);
			playerRepository.save(player1);
			
			Player player2 = new Player("Alexandre", "Tritta", 26);
			playerRepository.save(player2);
			
			Player player3 = new Player("Mickael", "Guigou", 38);
			playerRepository.save(player3);
			
			Team team1 = new Team("CSMBH", "Chambéry", "Alpi le yeti"); 
			team1.getPlayers().add(player1);
			team1.getPlayers().add(player2);
			teamRepository.save(team1);
			
			Team team2 = new Team("SRVHB", "Saint-Raphaël", "Leo le lion");
			teamRepository.save(team2);
			
		};
	}
	
}
