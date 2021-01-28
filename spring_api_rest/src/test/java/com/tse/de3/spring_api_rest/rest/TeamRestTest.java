package com.tse.de3.spring_api_rest.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tse.de3.spring_api_rest.datamodel.Player;
import com.tse.de3.spring_api_rest.datamodel.Team;
import com.tse.de3.spring_api_rest.datarepository.PlayerRepository;
import com.tse.de3.spring_api_rest.datarepository.TeamRepository;

import net.minidev.json.JSONArray;

@SpringBootTest
@AutoConfigureMockMvc
public class TeamRestTest {
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private PlayerRepository playerRepository;
	
	@Test
	public void testAll() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
				.get("/teams")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			    .andExpect(jsonPath("$.length()", is(2)))
			    .andExpect(jsonPath("$[?(@.name == 'CSMBH')].mascot", Matchers.contains("Alpi le yeti")));
	}
	
	@Test
	public void testGetTeam1() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
				.get("/teams/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			    .andExpect(jsonPath("$.name", is("CSMBH")))
			    .andExpect(jsonPath("$.city", is("Chambéry")))
			    .andExpect(jsonPath("$.mascot", is("Alpi le yeti")));
	}
	
	@Test
	public void testNewTeam() throws Exception {
		
		Team team = new Team("PAUC", "Aix-en-Provence", "Paucky");
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] teamAsBytes = mapper.writeValueAsBytes(team);
		
		mvc.perform(
				MockMvcRequestBuilders
				.post("/teams")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(teamAsBytes))
				.andExpect(status().isOk());
		
		assertEquals(3, teamRepository.count());
		
		Collection<Team> teams = teamRepository.findAll();
		
		boolean foundTeam = false;
		
		for (Team currentTeam : teams) {
			
			if (currentTeam.getName().equals("PAUC")) {
				
				foundTeam = true;
				
				teamRepository.delete(currentTeam);
			}
		}
		
		assertTrue(foundTeam);
	}
	
	@Test
	public void testDeleteTeam() throws Exception {
		
		Team team = new Team("PAUC", "Aix-en-Provence", "Paucky");
		
		teamRepository.save(team);
		
		Collection<Team> teams = teamRepository.findAll();
		
		Long id = 0L;
		
		for (Team currentTeam : teams) {
			
			if (currentTeam.getName().equals("PAUC")) {
				
				id = currentTeam.getId();
			}
		}
		
		mvc.perform(
				MockMvcRequestBuilders
				.delete("/teams/" + id)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		assertEquals(2, teamRepository.count());
	}
	
	@Test
	public void testReplaceTeam() throws Exception {
		
		Team team = new Team("CSH", "Chambé", "Alpi le yéti");
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] teamAsBytes = mapper.writeValueAsBytes(team);
        
        mvc.perform(
				MockMvcRequestBuilders
				.put("/teams/1")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(teamAsBytes))
				.andExpect(status().isOk());
        
        team = teamRepository.findById(1L).orElse(null);
        
        if (team == null) {
        	fail("Team not found");
        }
        
        assertEquals("Alpi le yéti", team.getMascot());
        assertEquals("CSH", team.getName());
        assertEquals("Chambé", team.getCity());
        
        team.setName("CSMBH");
        team.setCity("Chambéry");
        team.setMascot("Alpi le yeti");
        team = teamRepository.save(team);

        assertEquals("CSMBH", team.getName());
        assertEquals("Chambéry", team.getCity());
        assertEquals("Alpi le yeti", team.getMascot());
	}
	
	@Test
	public void testGetAllPlayersFromTeam() throws Exception {
		        
        mvc.perform(
				MockMvcRequestBuilders
				.get("/teams/1/players")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	    		.andExpect(jsonPath("$.length()", is(2)))
	    		.andExpect(jsonPath("$[?(@.name == 'Alexandre')].familyName", Matchers.contains("Tritta")))
	    		.andExpect(jsonPath("$[?(@.name == 'Luc')].familyName", Matchers.contains("Abalo")));

	}
	
	@Test
	public void testAddNewPlayerToTeam() throws Exception {	
		Player player = new Player("Yann", "Genty", 39);
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] playerAsBytes = mapper.writeValueAsBytes(player);
        
		mvc.perform(
				MockMvcRequestBuilders
				.post("/teams/1/players")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(playerAsBytes))
				.andExpect(status().isOk());
        
		Team team = teamRepository.findById(1L).orElse(null);
        
        if (team == null) {
        	fail("Team not found");
        }
        
		assertEquals(3, team.getNbOfPlayers());
		assertEquals(4, playerRepository.count());		
	}
	
	@Test
	public void testAddExistingPlayerToTeam() throws Exception {		
		JSONArray tritta = new JSONArray();
		tritta.add("Tritta");
		
		JSONArray abalo = new JSONArray();
		abalo.add("Abalo");
		
		JSONArray guigou = new JSONArray();
		guigou.add("Guigou");
		
		mvc.perform(
				MockMvcRequestBuilders
				.put("/teams/1/players/3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			    .andExpect(jsonPath("$.name", is("CSMBH")))
			    .andExpect(jsonPath("$.city", is("Chambéry")))
			    .andExpect(jsonPath("$.mascot", is("Alpi le yeti")))
			    .andExpect(jsonPath("$.players.[?(@.name == 'Alexandre')].familyName", is(tritta)))
			    .andExpect(jsonPath("$.players.[?(@.name == 'Luc')].familyName", is(abalo)))
			    .andExpect(jsonPath("$.players.[?(@.name == 'Mickael')].familyName", is(guigou)));

	}
	
	@Test
	public void testDeletePlayerFromATeam() throws Exception {		
		JSONArray tritta = new JSONArray();
		tritta.add("Tritta");
		
		JSONArray abalo = new JSONArray();
		abalo.add("Abalo");
		
		JSONArray emptyArray = new JSONArray();
		
		mvc.perform(
				MockMvcRequestBuilders
				.delete("/teams/1/players/3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
			    .andExpect(jsonPath("$.name", is("CSMBH")))
			    .andExpect(jsonPath("$.city", is("Chambéry")))
			    .andExpect(jsonPath("$.mascot", is("Alpi le yeti")))
			    .andExpect(jsonPath("$.players.[?(@.name == 'Alexandre')].familyName", is(tritta)))
			    .andExpect(jsonPath("$.players.[?(@.name == 'Luc')].familyName", is(abalo)))
			    .andExpect(jsonPath("$.players.[?(@.name == 'Mickael')].familyName", is(emptyArray)));
		
	}
}
