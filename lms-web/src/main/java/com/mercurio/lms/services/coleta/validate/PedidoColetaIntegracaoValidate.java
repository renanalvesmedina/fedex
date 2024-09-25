package com.mercurio.lms.services.coleta.validate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.mercurio.lms.configuracoes.ConfiguracoesFacade;

import br.com.tntbrasil.integracao.domains.pedidocoleta.PedidoColetaIntegracaoEntradaDMN;

public class PedidoColetaIntegracaoValidate implements Serializable {

	private static final long serialVersionUID = 1841687655256156843L;
	
	private ConfiguracoesFacade configuracoesFacade;
	
	public PedidoColetaIntegracaoValidate(ConfiguracoesFacade configuracoesFacade) {
		super();
		this.configuracoesFacade = configuracoesFacade;
	}
	
	private List<String> mensagens = new ArrayList<String>();
	
    public List<String> validar(PedidoColetaIntegracaoEntradaDMN input) {
    	// validar INPUT
    	if (input == null) {
    		this.mensagens.add(configuracoesFacade.getMensagem("LMS-36337", new Object[] {"JSON"}));
    	} else {
    		validarNomeDoArquivoImportacao(input);
    		validarDataHoraProcessamento(input);
    		validarPedidoColeta(input);
    	}
    	
    	return this.mensagens;
    }
    
    private void validarNomeDoArquivoImportacao(PedidoColetaIntegracaoEntradaDMN input) {
		if (input.getNomeArquivo() == null || input.getNomeArquivo().isEmpty()) {
			this.mensagens.add("'Nome do arquivo' de importação não foi informado");
		}
    }
    
    private void validarQuantidadeEPeso(BigDecimal quantidade, BigDecimal peso) {
		if (quantidade == null) {
			this.mensagens.add(configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'quantidadeTotalVolumes'"}));
		}
		if (peso == null) {
			this.mensagens.add(configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'pesoTotal'"}));
		}
    }
    
    private void validarDataHoraProcessamento(PedidoColetaIntegracaoEntradaDMN input) {
		if (input.getDataHoraProcessamento() == null) {
			this.mensagens.add(configuracoesFacade.getMensagem("LMS-02131", new Object[] {"'dataHoraProcessamento'"}));
		}
    }
    
    private void validarPedidoColeta(PedidoColetaIntegracaoEntradaDMN input) {
		if (input.getPedidoColeta() == null) {
			this.mensagens.add(configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'pedidoColeta'"}));
		} else {
			validarCepColeta(input.getPedidoColeta().getCepColeta());
			validarLogradouro(input.getPedidoColeta().getTipoLogradouro(), input.getPedidoColeta().getLogradouroColeta(), input.getPedidoColeta().getNumeroLogradouroColeta());
			validarCepDestino(input.getPedidoColeta().getCepDestino());
			validarDocumentoRemetente(input.getPedidoColeta().getCnpjRemetente());
			validarNomeCliente(input.getPedidoColeta().getNomeRemetente());
			validarTelefoneContato(input.getPedidoColeta().getDddTelefoneContato(), input.getPedidoColeta().getNumeroTelefoneContato());
			validarTipoPessoa(input.getPedidoColeta().getTipoPessoa());
			validarNumeroColeta(input.getPedidoColeta().getNumeroColeta());
			validarInscricaoEstadual(input.getPedidoColeta().getNumeroIE());
			validarQuantidadeEPeso(input.getPedidoColeta().getQuantidadeTotalVolumes(), input.getPedidoColeta().getPesoTotal());
		}
    }
    
    private void validarNomeCliente(String nomeCliente) {
    	String auxiliar = !StringUtils.isEmpty(nomeCliente) ? nomeCliente.trim() : nomeCliente;
		if (StringUtils.isEmpty(auxiliar)) {
			this.mensagens.add(configuracoesFacade.getMensagem("LMS-02131", new Object[] {"'nomeRemetente'"}));
		}
	}

    private void validarTelefoneContato(String ddd, String numeroTelefone) {
    	Boolean dddOK = ddd != null && !"".equals(ddd.trim());
    	Boolean numeroTelefoneOK = numeroTelefone != null && !"".equals(numeroTelefone.trim());
		if ( (dddOK && !dddOK.equals(numeroTelefoneOK)) ) {
			this.mensagens.add(configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'numeroTelefoneContato'"}));
		}
		if ( (numeroTelefoneOK && !numeroTelefoneOK.equals(dddOK)) ) {
			this.mensagens.add(configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'dddTelefoneContato'"}));
		}
	}

    private void validarLogradouro(String tipoLogradouro, String logradouro, String numeroLogradouro) {
    	String auxiliar = !StringUtils.isEmpty(tipoLogradouro) ? tipoLogradouro.trim() : tipoLogradouro;  
		if (StringUtils.isEmpty(auxiliar)) {
			this.mensagens.add(configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'tipoLogradouro'"}));
		}
		
    	auxiliar = !StringUtils.isEmpty(logradouro) ? logradouro.trim() : logradouro;  
		if (StringUtils.isEmpty(auxiliar)) {
			this.mensagens.add( configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'logradouroColeta'"}));
		} else {
			String expressao = "^[.,-/ a-zA-Z0-9]+$";
			if (!auxiliar.matches(expressao)) {
				this.mensagens.add( configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'logradouroColeta'"}));
			}
		}
		
    	auxiliar = !StringUtils.isEmpty(numeroLogradouro) ? numeroLogradouro.trim() : numeroLogradouro;  
		if (StringUtils.isEmpty(auxiliar)) {
			this.mensagens.add( configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'numeroLogradouroColeta'"}));
		}
	}

	private void validarInscricaoEstadual(String numeroIE) {
    	String auxiliar = !StringUtils.isEmpty(numeroIE) ? numeroIE.trim() : numeroIE;  
		if (StringUtils.isEmpty(auxiliar) || (!"ISENTO".equals(auxiliar) && !StringUtils.isNumeric(auxiliar)) ) {
			this.mensagens.add( configuracoesFacade.getMensagem("LMS-02131", new Object[] {"'numeroIE'"}));
		}
	}

	private void validarCepColeta(String cep) {
    	try {
    		String cepAuxiliar = cep.replace(".", "").replace("-", "");
    		int icep = Integer.parseInt(cepAuxiliar);
    		if (icep < 1 || icep > 99999999) {
    			throw new Exception(); 
    		}
    	} catch (Exception e) {
			this.mensagens.add( configuracoesFacade.getMensagem("LMS-02133", new Object[] {cep}));
    	}
    }

    private void validarCepDestino(String cep) {
    	try {
    		String cepAuxiliar = cep.replace(".", "").replace("-", "");
    		int icep = Integer.parseInt(cepAuxiliar);
    		if (icep < 1 || icep > 99999999) {
    			throw new Exception(); 
    		}
    	} catch (Exception e) {
			this.mensagens.add( configuracoesFacade.getMensagem("LMS-02133", new Object[] {cep}));
    	}
    }

    private void validarDocumentoRemetente(String documento) {
    	try {
    		long idocumento = Long.parseLong(documento);
    		if (idocumento < 1 || idocumento > 99999999999999l) {
    			throw new Exception(); 
    		}
    	} catch (Exception e) {
			this.mensagens.add( configuracoesFacade.getMensagem("LMS-02131", new Object[] {"'cnpjRemetente'"}));
    	}
    }

    private void validarNumeroColeta(String numeroColeta) {
    	String auxiliar = !StringUtils.isEmpty(numeroColeta) ? numeroColeta.trim() : numeroColeta;  
		if (StringUtils.isEmpty(auxiliar)) {
			this.mensagens.add(configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'numeroColeta'"}));
    	}
    }

    public static final String TIPO_PESSOA_FISICA = "F";
    public static final String TIPO_IDENTIFICACAO_PF = "CPF";
    public static final String TIPO_PESSOA_JURIDICA = "J";
    public static final String TIPO_IDENTIFICACAO_PJ = "CNPJ";
    
    private void validarTipoPessoa(String tipoPessoa) {
    	String auxiliar = !StringUtils.isEmpty(tipoPessoa) ? tipoPessoa.toUpperCase() : tipoPessoa;  
    	if (!TIPO_PESSOA_FISICA.equals(auxiliar) && !TIPO_PESSOA_JURIDICA.equals(auxiliar)) {
			this.mensagens.add( configuracoesFacade.getMensagem("LMS-02131", new Object[] {"'tipoPessoa'"}));
    	}
    }

}
