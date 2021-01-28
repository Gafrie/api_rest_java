package com.tse.de3.spring_api_rest.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tse.de3.spring_api_rest.datamodel.Player;
import com.tse.de3.spring_api_rest.datarepository.PlayerRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import org.hamcrest.Matchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerRestTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private PlayerRepository playerRepository;

	@Test
	public void testAll() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
				.get("/players")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			    .andExpect(jsonPath("$.length()", is(3)))
			    .andExpect(jsonPath("$[?(@.name == 'Luc')].familyName", Matchers.contains("Abalo")));
	}
	
	@Test
	public void testGetPlayer1() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders
				.get("/players/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			    .andExpect(jsonPath("$.name", is("Luc")))
			    .andExpect(jsonPath("$.familyName", is("Abalo")))
	    		.andExpect(jsonPath("$.age", is(36)));
	}
	
	@Test
	public void testNewPlayer() throws Exception {
		
		Player player = new Player("Yann", "Genty", 39);
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] playerAsBytes = mapper.writeValueAsBytes(player);
		
		mvc.perform(
				MockMvcRequestBuilders
				.post("/players")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(playerAsBytes))
				.andExpect(status().isOk());
		
		assertEquals(4, playerRepository.count());
		
		Collection<Player> players = playerRepository.findAll();
		
		boolean foundPlayer = false;
		
		for (Player currentPlayer : players) {
			
			if (currentPlayer.getName().equals("Yann")) {
				
				foundPlayer = true;
				
				playerRepository.delete(currentPlayer);
			}
		}
		
		assertTrue(foundPlayer);
	}
	
	@Test
	public void testDeletePlayer() throws Exception {
		
		Player player = new Player("Yann", "Genty", 39);
		
		playerRepository.save(player);
		
		Collection<Player> players = playerRepository.findAll();
		
		Long id = 0L;
		
		for (Player currentPlayer : players) {
			
			if (currentPlayer.getName().equals("Yann")) {
				
				id = currentPlayer.getId();
			}
		}
		
		mvc.perform(
				MockMvcRequestBuilders
				.delete("/players/" + id)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		assertEquals(3, playerRepository.count());
	}
	
	@Test
	public void testReplacePlayer() throws Exception {
		
		Player player = new Player("Luc", "Abalo", 37);
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] playerAsBytes = mapper.writeValueAsBytes(player);
        
        mvc.perform(
				MockMvcRequestBuilders
				.put("/players/1")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(playerAsBytes))
				.andExpect(status().isOk());
        
        player = playerRepository.findById(1L).orElse(null);
        
        if (player == null) {
        	
        	fail("Player not found");
        }
        
        assertEquals(37, player.getAge());
        
        player.setAge(36);
        player = playerRepository.save(player);
        
        assertEquals(36, player.getAge());
	}
}
