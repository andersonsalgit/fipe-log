package br.com.fipelog.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.fipelog.model.FipePrecoVersao;
import br.com.fipelog.model.FipeResponseModel;

@Service
public class FipeService {
	
	public List<FipeResponseModel> listAll() {
		return null;
	}

	public List<FipeResponseModel> consulta(Long iDM, Long iDV) {
		
	    Map<String, Long> params = new HashMap<String, Long>();
	    params.put("idm", iDM);
	    params.put("idv", iDV);
		
	    //Busca modelo e versão e retorna o id-ano
		List<String> listIdAnos = new ArrayList<String>();
		ResponseEntity<String> response = buscaModeloVersao(iDM, iDV, params);
		getAnoID(response.getBody(), listIdAnos);	
		
		//Busca e retorna valor percentual
		Map<Float, String> resultMap = buscaPrecoVersao(iDM, iDV, listIdAnos, params);
		List<FipeResponseModel> fipeResponseModels = getValorPercentual(resultMap);
		
	    return fipeResponseModels;
		
	}

	private ResponseEntity<String> buscaModeloVersao(Long iDM, Long iDV, Map<String, Long> params) {
		
		String url = "http://fipeapi.appspot.com/api/1/carros/veiculo/{idm}/{idv}.json"; //21/4828
		
		RestTemplate restTemplate = new RestTemplate();
	    
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, params);
		
		return response;
	}
	
	private Map<Float, String> buscaPrecoVersao(Long iDM, Long iDV, List<String> listIdAnos, Map<String, Long> params) {
		
		Map<Float, String> resultMap = new HashMap<Float, String>();
		
		// fipe api 
		//String url = "http://fipeapi.appspot.com/api/1/carros/veiculo/"+iDM+"/"+iDV+"/";
		
		//api parallelum
		String url = "https://parallelum.com.br/fipe/api/v1/carros/marcas/{idm}/modelos/{idv}/anos/";
		
		RestTemplate restTemplate = new RestTemplate();
		
		for (int i = 0; i < listIdAnos.size(); i++) {
			
			FipePrecoVersao fipePrecoVersao = restTemplate.getForObject(url+listIdAnos.get(i), FipePrecoVersao.class, params);
			resultMap.put(fipePrecoVersao.getAnoModelo(), fipePrecoVersao.getValor());
			
		}
		
		return resultMap;
	}
	
	public static List<String> getAnoID(String body, List<String> listIdAnos) {
		
		Matcher matcher = Pattern.compile("\"id.*?\\}").matcher(body);
		int group = 0;
		while (matcher.find()) {
		   for (int i = 0; i <= matcher.groupCount(); i++) {
			   
			  //limpa string
		      String strTemp = matcher.group(i).replace("}", "");
		      strTemp = strTemp.substring(7);
		      
		      //add somente o id-ano
		      listIdAnos.add(strTemp.substring(0, strTemp.length()-1));
		      
		      group++;
		   }
		}
		return listIdAnos;
    }
	
	private List<FipeResponseModel> getValorPercentual(Map<Float, String> resultMap) {
		
		List<FipeResponseModel> list = new ArrayList<FipeResponseModel>();
		
		DecimalFormat dfAno = new DecimalFormat("0000");
		DecimalFormat dfPercent = new DecimalFormat("0");
		
		
		int count = 0;
		BigDecimal valorTemp = BigDecimal.ZERO;
		String anoAnterior = "";
		
		for (Entry<Float, String> pair : resultMap.entrySet()) {
			
			String valor = pair.getValue();
			valor = valor.substring(3).replace(".", "").replace(",", ".");
			
			if(count == 0) {
				
				list.add(new FipeResponseModel("Valor em "+dfAno.format(pair.getKey())+" -> "+pair.getValue()));
				
				valorTemp = new BigDecimal(valor);
				anoAnterior = dfAno.format(pair.getKey());
				
			} else {
				
				//Cálcula a diferença
				BigDecimal diferenca = valorTemp.subtract(new BigDecimal(valor));
				
				//Cálcula o percentual
				BigDecimal percentual = new BigDecimal(valor).divide(valorTemp, RoundingMode.HALF_EVEN);
				percentual = BigDecimal.ONE.subtract(percentual).multiply(new BigDecimal(100));
				
				list.add(new FipeResponseModel("Valor em "+dfAno.format(pair.getKey())+" ->  "+pair.getValue()
				+ " alteração de R$ "+diferenca+" ("+dfPercent.format(percentual)+"%) em relação a "+anoAnterior));
				
				valorTemp = new BigDecimal(valor);
				anoAnterior = dfAno.format(pair.getKey());
			}
			count++;
		}
		
		return list;
	}
	
}
