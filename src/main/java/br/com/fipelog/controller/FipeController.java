package br.com.fipelog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.fipelog.model.FipeResponseModel;
import br.com.fipelog.service.FipeService;

@RestController
public class FipeController {

	@Autowired
	private FipeService fipeService;
	
	@GetMapping("/fipe/{idMarca}/{idVeiculo}")
	public List<FipeResponseModel> consulta(@PathVariable(value = "idMarca") Long IDM, @PathVariable(value = "idVeiculo") Long IDV) {
		
		return fipeService.consulta(IDM, IDV);
	}
	
}
