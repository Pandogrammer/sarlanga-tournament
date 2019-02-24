package farguito.sarlanga.tournament.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.connection.DefoldResponse;

@RestController
@RequestMapping("card")
public class CardsController {

	@Autowired
	private CardFactory cardFactory;
	
	@GetMapping
	public DefoldResponse cards() {
		DefoldResponse response = new DefoldResponse("cards_response");

		response.put("criatures", cardFactory.getCriatures());
		response.put("actions", cardFactory.getActions());
		
		return response;
	}
}
