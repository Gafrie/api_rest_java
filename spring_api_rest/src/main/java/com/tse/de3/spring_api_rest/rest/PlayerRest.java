package com.tse.de3.spring_api_rest.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tse.de3.spring_api_rest.datamodel.Player;
import com.tse.de3.spring_api_rest.datarepository.PlayerRepository;
import com.tse.de3.spring_api_rest.exception.PlayerNotFoundException;

@RestController
public class PlayerRest {

	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private TeamRest team;
	
	public PlayerRest() {
		
	}
	
	@GetMapping("/players")
	public List<Player> allPlayers() {	
		return this.playerRepository.findAll();
	}
	
	@GetMapping("/players/{id}")
	public Player player(@PathVariable Long id) {	
		return this.playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
	}
	
	@PostMapping("/players")
	public Player createPlayer(@RequestBody Player player) {	
		return this.playerRepository.save(player);
	}
	
	@DeleteMapping("/players/{id}")
	public void deletePlayer(@PathVariable Long id) {	
		Player player = this.playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
		team.allTeams().forEach(team -> {
			if(team.getPlayers().contains(player)) {
				team.getPlayers().remove(player);
			}
		});
		this.playerRepository.deleteById(id);
	}
	
	@PutMapping("/players/{id}")
	public Player replacePlayer(@PathVariable Long id, @RequestBody Player newPlayer) {	
		Player playerToReplace = this.playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
		
		if(playerToReplace != null) {
			playerToReplace.setName(newPlayer.getName());
			playerToReplace.setFamilyName(newPlayer.getFamilyName());
			playerToReplace.setAge(newPlayer.getAge());
			
			playerToReplace = this.playerRepository.save(playerToReplace);
		}
		return playerToReplace;
	}
}
