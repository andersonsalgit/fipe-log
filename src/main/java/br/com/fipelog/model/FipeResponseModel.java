package br.com.fipelog.model;

import lombok.Data;

@Data
public class FipeResponseModel {
	
	public FipeResponseModel(String resultado) {
		this.resultado = resultado;
	}

	private String resultado;

}
