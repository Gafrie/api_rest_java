package com.tse.de3.spring_api_rest.rest;

import java.util.ArrayList;
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
import com.tse.de3.spring_api_rest.datamodel.Team;
import com.tse.de3.spring_api_rest.datarepository.PlayerRepository;
import com.tse.de3.spring_api_rest.datarepository.TeamRepository;
import com.tse.de3.spring_api_rest.exception.PlayerNotFoundException;
import com.tse.de3.spring_api_rest.exception.TeamNotFoundException;

@RestController
public class TeamRest {

	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	public TeamRest() {
		
	}
	
	@GetMapping("/teams")
	public List<Team> allTeams() {	
		return this.teamRepository.findAll();
	}
	
	@GetMapping("/teams/{id}")
	public Team team(@PathVariable Long id) {	
		return this.teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
	}
	
	@PostMapping("/teams")
	public Team createTeam(@RequestBody Team team) {	
		return this.teamRepository.save(team);
	}
	
	@DeleteMapping("/teams/{id}")
	public void deletePlayer(@PathVariable Long id) {	
		this.teamRepository.deleteById(id);
	}
	
	@PutMapping("/teams/{id}")
	public Team replaceTeam(@PathVariable Long id, @RequestBody Team newTeam) {	
		Team teamToReplace = this.teamRepository.findById(id)
				.orElseThrow(() -> new TeamNotFoundException(id));
		
		if(teamToReplace != null) {
			teamToReplace.setName(newTeam.getName());
			teamToReplace.setCity(newTeam.getCity());
			teamToReplace.setMascot(newTeam.getMascot());
			
			teamToReplace = this.teamRepository.save(teamToReplace);
		}
		
		return teamToReplace;
	}
	
	@GetMapping("/teams/{id}/players")
	public List<Player> playersOfATeam(@PathVariable Long id) {	
		List<Player> players = new ArrayList<>();
		this.teamRepository.findById(id).ifPresent(team ->
				players.addAll(team.getPlayers())
		);
		
		return players;
	}
	
	@PostMapping("/teams/{idTeam}/players")
	public Team addNewPlayerInATeam(@PathVariable Long idTeam, @RequestBody Player player) {	
		Team teamToAddPlayer = this.teamRepository.findById(idTeam)
				.orElseThrow(() -> new TeamNotFoundException(idTeam));
		
		if (teamToAddPlayer != null) {
			player = this.playerRepository.save(player);
			if (!teamToAddPlayer.getPlayers().contains(player)) {
				teamToAddPlayer.getPlayers().add(player);
			}
			teamToAddPlayer = this.teamRepository.save(teamToAddPlayer);	
		}
		return teamToAddPlayer;
	}
	
	@PutMapping("/teams/{idTeam}/players/{idPlayer}")
	public Team addExistingPlayerInATeam(@PathVariable Long idTeam, @PathVariable Long idPlayer) {	
		Team teamToAddPlayer = this.teamRepository.findById(idTeam).orElseThrow(() -> new TeamNotFoundException(idTeam));
		
		if (teamToAddPlayer != null) {
			
			Player player = this.playerRepository.findById(idPlayer).orElseThrow(() -> new PlayerNotFoundException(idPlayer));
			
			if (player != null) {
				if (!teamToAddPlayer.getPlayers().contains(player)) {
					teamToAddPlayer.getPlayers().add(player);
				}
			}
			teamToAddPlayer = this.teamRepository.save(teamToAddPlayer);	
		}
		return teamToAddPlayer;
	}
	
	@DeleteMapping("/teams/{idTeam}/players/{idPlayer}")
	public Team deletePlayerFromATeam(@PathVariable Long idTeam, @PathVariable Long idPlayer) {	
		Team team = this.teamRepository.findById(idTeam).orElseThrow(() -> new TeamNotFoundException(idTeam));
				
		if (team != null) {
			Player playerToRemove = this.playerRepository.findById(idPlayer).orElseThrow(() -> new PlayerNotFoundException(idPlayer));
			
			if (playerToRemove != null) {
				if (team.getPlayers().contains(playerToRemove)) {
					team.getPlayers().remove(playerToRemove);
				}
			}
			team = this.teamRepository.save(team);
		}
		return team;
	}
}
