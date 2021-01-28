const axios = require('axios');

///////////////////////////////////////////////////Player Management////////////////////////////////
// Get all the players
axios.get('http://localhost:8091/players')
  .then(function (response) {
    // handle success
    console.log(response.data.length + ' players found :');

    response.data.forEach(element => {
        
        console.log(" - id : " + element.id + ", name : " + element.name + ', family name :' + element.familyName + ', age :' + element.age);
    });
    console.log("");
  })
  .catch(function (error) {
    // handle error
    console.log(error);
  });

// Get player 1
axios.get('http://localhost:8091/players/1')
  .then(function (response) {
    // handle success
    console.log('player 1 found :');

    var player = response.data;
    console.log(" - id : " + player.id + ", name : " + player.name + ', family name :' + player.familyName + ', age :' + player.age + '\n');
  })
  .catch(function (error) {
    // handle error
    console.log(error);
  });

  
// Add one player
axios.post('http://localhost:8091/players', {
    "name": "Yann",
    "familyName": "Genty",
    "age": 39 })
  .then(function (response) {
    // handle success
    console.log(' Player added succesfully :');

    var player = response.data;
    console.log(" - id : " + player.id + ", name : " + player.name + ', family name :' + player.familyName + ', age :' + player.age + '\n');
  })
  .catch(function (error) {
    // handle error
    console.log(error);
  });

// Delete one player
axios.delete('http://localhost:8091/players/2')
  .then(function (response) {
    // handle success
    console.log(' Player 2 deleted succesfully\n');
  })
  .catch(function (error) {
    // handle error
    console.log(error);
  });

// Modifying one player
axios.put('http://localhost:8091/players/3', {
    "name": "Arthur",
    "familyName": "Anquetil",
    "age": 25 })
  .then(function (response) {
    // handle success
    console.log(' Player 3 changed succesfully :');

    var player = response.data;
    console.log(" - id : " + player.id + ", name : " + player.name + ', family name :' + player.familyName + ', age :' + player.age + '\n');
  })
  .catch(function (error) {
    // handle error
    console.log(error);
  });




///////////////////////////////////////////////////Team Management////////////////////////////////
// Get all the teams
axios.get('http://localhost:8091/teams')
.then(function (response) {
  // handle success
  console.log(response.data.length + ' teams found :');

  response.data.forEach(element => {
      
      console.log(" - id : " + element.id + ", name : " + element.name + ', city :' + element.city + ', mascot :' + element.mascot);
      for (var i = 0; i < element.players.length; i++) {
        console.log(element.players[i].name + " " + element.players[i].familyName + " " + element.players[i].age);
      }
  });
  console.log('');
})
.catch(function (error) {
  // handle error
  console.log(error);
});

// Get Team with id 1
axios.get('http://localhost:8091/teams/1')
.then(function (response) {
  // handle success
  
  console.log('Team 1 found :');

  var team = response.data;
  console.log(" -  id : " +  team.id + ", name : " + team.name + ', city :' + team.city + ', mascot :' + team.mascot);
  if (team.players) {
    for (var i = 0; i < team.players.length; i++) {
      console.log(team.players[i].name + " " + team.players[i].familyName + " " + team.players[i].age);
    }
  }
  console.log('');
})
.catch(function (error) {
  // handle error
  console.log(error);
});

// Add one team
axios.post('http://localhost:8091/teams', {
  "name": "PAUC",
  "city": "Aix-en-Provence",
  "mascot": "Paucky"
})
.then(function (response) {
  // handle success
  
  console.log('Team succesfully added :');

  var team = response.data;
  console.log(" -  id : " +  team.id + ", name : " + team.name + ', city :' + team.city + ', mascot :' + team.mascot + '\n');
})
.catch(function (error) {
  // handle error
  console.log(error);
});

// Delete Team with id 2
axios.delete('http://localhost:8091/teams/2')
.then(function (response) {
    // handle success
  
  console.log('Team 2 deleted \n');
})
.catch(function (error) {
  // handle error
  console.log(error);
});

// Modify team 1
axios.put('http://localhost:8091/teams/1', {
  "name": "CSH",
  "city": "Chambé",
  "mascot": "Alpi"
})
.then(function (response) {
// handle success
  
  console.log('Team 1 succesfully modify :');

  var team = response.data;
  console.log(" -  id : " +  team.id + ", name : " + team.name + ', city :' + team.city + ', mascot :' + team.mascot + '\n');
})
.catch(function (error) {
  // handle error
  console.log(error);
});

// Get all players of team 1
axios.get('http://localhost:8091/teams/1/players')
.then(function (response) {
  console.log('Players of team 1 :');

  response.data.forEach(player => {
    console.log(" - id : " + player.id + ", name : " + player.name + ', family name :' + player.familyName + ', age :' + player.age);
  });
  console.log('');
})
.catch(function (error) {
  // handle error
  console.log(error);
});

// Add new player in team 1
axios.post('http://localhost:8091/teams/1/players', {
  "name": "Yann",
  "familyName": "Genty",
  "age": 39 
})
.then(function (response) {
    // handle success
  
  console.log('Player added in team 1 ');

  var team = response.data;
  console.log(" -  id : " +  team.id + ", name : " + team.name + ', city :' + team.city + ', mascot :' + team.mascot + ', players :');
  for (var i = 0; i < team.players.length; i++) {
    console.log(team.players[i].name + " " + team.players[i].familyName + " " + team.players[i].age);
  }
  console.log('\n');
})
.catch(function (error) {
  // handle error
  console.log(error);
});

// Add existing player 3 to team 1
axios.put('http://localhost:8091/teams/1/players/3')
.then(function (response) {
  console.log('Player 3 added to team 1 :');

  var team = response.data;
  console.log(" -  id : " +  team.id + ", name : " + team.name + ', city :' + team.city + ', mascot :' + team.mascot + ', players :');
  for (var i = 0; i < team.players.length; i++) {
    console.log(team.players[i].name + " " + team.players[i].familyName + " " + team.players[i].age);
  }
  console.log('\n');
})
.catch(function (error) {
  // handle error
  console.log(error);
});

// Delete player 1 in team 1
axios.delete('http://localhost:8091/teams/1/players/1')
.then(function (response) {
    // handle success
  
  console.log('Player 1 deleted from team 1 ');

  var team = response.data;
  console.log(" -  id : " +  team.id + ", name : " + team.name + ', city :' + team.city + ', mascot :' + team.mascot + ', players :');
  for (var i = 0; i < team.players.length; i++) {
    console.log(team.players[i].name + " " + team.players[i].familyName + " " + team.players[i].age);
  }
  console.log('\n');
})
.catch(function (error) {
  // handle error
  console.log(error);
});