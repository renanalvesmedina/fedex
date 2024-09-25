package com.mercurio.lms.municipios.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;

@ServiceSecurity
public class ConsultarContatosFiliaisSiteAction {
	private FilialService filialService;
	
	@MethodSecurity(processGroup = "site", 
			processName = "contatosFiliaisSiteAction.findContatosFiliaisMercurio",
			authenticationRequired=false)	
	public List<Map<String, Object>> findContatosFiliaisMercurio() {
		List<Filial> contatos = filialService.findContatosFiliaisMercurio();  		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>();
		Map<String, Boolean> unique = new HashMap<String, Boolean>();
		
		Map<String, Object> gerentes = filialService.findGerentesFiliaisMercurio(); 
		
		for(Filial filial : contatos) {
			if(!unique.containsKey(filial.getSgFilial())) {
				unique.put(filial.getSgFilial(), true);
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("sgFilial", filial.getSgFilial());
				item.put("nmFilial", filial.getPessoa().getNmFantasia());
				item.put("dsEmail", filial.getPessoa().getDsEmail());
				item.put("nmGerente", gerentes.get(filial.getSgFilial()));				
				
				EnderecoPessoa ende = filial.getPessoa().getEnderecoPessoa();
				if ( ende != null &&
					 ende.getDtVigenciaInicial().isBefore(JTDateTimeUtils.getDataAtual()) && 
					 (	ende.getDtVigenciaFinal() == null ||
						ende.getDtVigenciaFinal().isAfter(JTDateTimeUtils.getDataAtual()))) {								
					String dsEndereco = ende.getTipoLogradouro().getDsTipoLogradouro().getValue(); 
					dsEndereco += " " + ende.getDsEndereco(); 
					dsEndereco += ", " + ende.getNrEndereco();
					if(ende.getDsComplemento() != null) {
						dsEndereco += " - " + ende.getDsComplemento();
					}
					if(ende.getDsBairro() != null) {
						dsEndereco += " - " + ende.getDsBairro();
					}
					dsEndereco += " - " + ende.getMunicipio().getNmMunicipio();
					dsEndereco += " - " + ende.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();
					dsEndereco += " | CEP " + ende.getNrCep();				
					item.put("dsEndereco", dsEndereco);			
							
					if(ende.getTelefoneEnderecos() != null && ende.getTelefoneEnderecos().size() > 0) {
						String nrTelefone = null;
						String nrFax = null;					
						for(TelefoneEndereco tele : ende.getTelefoneEnderecos()) {
							if(tele.getTpUso().getValue().equals("FO")) {
								if(nrTelefone == null) {
									nrTelefone = tele.getDddTelefone();							
								} else {
									nrTelefone += " | " + tele.getDddTelefone();
								}
							}
							if(tele.getTpUso().getValue().equals("FA")) {
								if(nrFax == null) {
									nrFax = tele.getDddTelefone();
								} else {
									nrFax += " | " + tele.getDddTelefone();
								}							
							}						
						}			
						
						item.put("nrTelefone", nrTelefone);
						item.put("nrFax", nrFax);
					}
				}
				
				retorno.add(item);
			}
		}
		
		return retorno;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}
