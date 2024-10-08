package com.mercurio.lms.expedicao.swt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.AutenticacaoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.LiberacaoDocServ;
import com.mercurio.lms.expedicao.model.TipoLiberacaoEmbarque;
import com.mercurio.lms.expedicao.model.service.ConhecimentoNormalService;
import com.mercurio.lms.expedicao.model.service.TipoLiberacaoEmbarqueService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.VendasUtils;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.swt.liberacaoEmbarqueAction"
 */
public class LiberacaoEmbarqueAction extends CrudAction {
	
	private TipoLiberacaoEmbarqueService tipoLiberacaoEmbarqueService;
	private UsuarioService usuarioService; 
	private ConhecimentoNormalService conhecimentoNormalService;
	private AutenticacaoService authenticationService; 	
	
	/**
	 * Busca os funcion�rios promotores de acordo com os crit�rios passados por par�metro
	 * @param map Crit�rios de pesquisa para funcion�rios
	 * @return Lista contendo os funcion�rios  encontrados na pesquisa
	 */
	public List findLookupUsuarioFuncionario(TypedFlatMap criteria) {
		return usuarioService.findLookupUsuarioFuncionario(null,criteria.getString("nrMatricula"),null,null,null,null,true); 
	}

	public Map<String, Object> storeInSession(Map<String, Object> parameters) {
		Map<String, Object> result = new HashMap<String, Object>();
		Usuario usuario = new Usuario();
		usuario.setIdUsuario((Long) parameters.get("idUsuario"));

		AutenticacaoDMN autenticacaoDMN = new AutenticacaoDMN();
		autenticacaoDMN.setLogin((String) parameters.get("login"));
		autenticacaoDMN.setSenha((String) parameters.get("password"));
		
    	authenticationService.validateAutenticacao(autenticacaoDMN);

		conhecimentoNormalService.validateUsuarioLiberacaoEmbarqueClienteMunicipio(usuario);

		LiberacaoDocServ liberacaoDocServ = new LiberacaoDocServ();
		liberacaoDocServ.setObLiberacao((String) parameters.get("obLiberacao"));

		Long idTipoLiberacaoEmbarque = (Long) parameters.get("idTipoLiberacaoEmbarque");
		if (idTipoLiberacaoEmbarque != null) {
			TipoLiberacaoEmbarque novoTpLb = new TipoLiberacaoEmbarque();
			novoTpLb.setIdTipoLiberacaoEmbarque(idTipoLiberacaoEmbarque);
			liberacaoDocServ.setTipoLiberacaoEmbarque(novoTpLb);
		}

		liberacaoDocServ.setTpBloqueioLiberado(new DomainValue((String) parameters.get("tpBloqueioLiberacao")));
		usuario.setIdUsuario((Long) parameters.get("idUsuario"));
		liberacaoDocServ.setUsuario(usuario);

		String tpDocumentoServico = (String) parameters.get(ConstantesExpedicao.TP_DOCUMENTO_IN_SESSION);
		if (ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(tpDocumentoServico) || 
				ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpDocumentoServico)|| 
				ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumentoServico)|| 
				ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)) {
			//Libera��o de Conhecimento e NFT
			Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
			if(conhecimento == null) {
				conhecimento = new Conhecimento();
			}
			liberacaoDocServ.setDoctoServico(conhecimento);
			conhecimento.addLiberacaoEmbarque(liberacaoDocServ);

			result.put("conhecimento", conhecimento);
		} else if (ConstantesVendas.COTACAO.equals(tpDocumentoServico)) {
			//Libera��o da Cota��o
			Cotacao cotacao = VendasUtils.getCotacaoInSession();

			liberacaoDocServ.setCotacao(cotacao);
			cotacao.addLiberacaoEmbarque(liberacaoDocServ);
			VendasUtils.setCotacaoInSession(cotacao);
		}
		return result;
	}

	public List findTipoLiberacao(Map criteria){
		return tipoLiberacaoEmbarqueService.find(criteria);
	}
	
	public void setConhecimentoNormalService(
			ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setTipoLiberacaoEmbarqueService(
			TipoLiberacaoEmbarqueService tipoLiberacaoEmbarqueService) {
		this.tipoLiberacaoEmbarqueService = tipoLiberacaoEmbarqueService;
	}

	public void setAuthenticationService(AutenticacaoService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
}
