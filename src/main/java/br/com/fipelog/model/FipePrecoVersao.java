package br.com.fipelog.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FipePrecoVersao implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -1528226745583151227L;

	@JsonProperty 
	private String Valor;
	@JsonProperty 
	private String Marca;
	@JsonProperty 
	private String Modelo;
	@JsonProperty 
	private float AnoModelo;
	@JsonProperty 
	private String Combustivel;
	@JsonProperty 
	private String CodigoFipe;
	@JsonProperty 
	private String MesReferencia;
	@JsonProperty 
	private float TipoVeiculo;
	@JsonProperty 
	private String SiglaCombustivel;

}
