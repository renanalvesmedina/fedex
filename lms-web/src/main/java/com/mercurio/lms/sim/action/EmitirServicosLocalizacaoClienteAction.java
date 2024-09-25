package com.mercurio.lms.sim.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.sim.report.EmitirCartaServicoLocalizacaoClienteService;
import com.mercurio.lms.sim.report.EmitirRelatorioServicosLocalizacaoClienteService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sim.emitirServicosLocalizacaoClienteAction"
 */

public class EmitirServicosLocalizacaoClienteAction extends ReportActionSupport {
	
	private ContatoService contatoService;
	private ClienteService clienteService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private EmitirRelatorioServicosLocalizacaoClienteService emitirRelatorioServicosLocalizacaoClienteService;
	private EmitirCartaServicoLocalizacaoClienteService emitirCartaServicoLocalizacaoClienteService;
	private FilialService filialService;
	private MoedaPaisService moedaPaisService;
	
	@Override
	public java.io.File execute(TypedFlatMap parameters) throws Exception {
		String tpRelatorio = parameters.getString("tpRelatorio");
		
		if (("".equals(parameters.getString("remetente.idCliente")) && "".equals(parameters.getString("destinatario.idCliente"))) ||
			(!"".equals(parameters.getString("remetente.idCliente")) && !"".equals(parameters.getString("destinatario.idCliente")))){
				
			throw new BusinessException("LMS-10041");
		}
				
		if (tpRelatorio.equals("R")) {		
			
			this.reportServiceSupport = emitirRelatorioServicosLocalizacaoClienteService;
			
		} else if (tpRelatorio.equals("C")){
			
			this.reportServiceSupport = emitirCartaServicoLocalizacaoClienteService;
		}
		
		return super.execute(parameters);
	}
	
	
	public List findContatos(TypedFlatMap criterions){
		return contatoService.findComboContatos(criterions);
	}
	
	public List findLookupCliente(TypedFlatMap tfm){
		
		List clientes = clienteService.findLookupSimplificado(tfm.getString("pessoa.nrIdentificacao"),null);
		
		List retorno = new ArrayList();
			
		for (Iterator iter = clientes.iterator(); iter.hasNext();) {
				
			Cliente element = (Cliente) iter.next();
				
			TypedFlatMap map = new TypedFlatMap();
			map.put("pessoa.nrIdentificacao",element.getPessoa().getNrIdentificacao());
			map.put("idCliente",element.getIdCliente());
			map.put("pessoa.nmPessoa",element.getPessoa().getNmPessoa());
			map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(element.getPessoa().getTpIdentificacao(),
																					   element.getPessoa().getNrIdentificacao()));
				
			retorno.add(map);			
		}
		
		return retorno;			
	}
	
	
	/**
	 * M�todo respons�vel por requisitar informa��es do usu�rio logado.
	 * @return
	 */
	public TypedFlatMap findInfoUsuarioLogado() {
		Filial f = SessionUtils.getFilialSessao();		
		TypedFlatMap retorno = new TypedFlatMap();

    	MoedaPais moedaPais = filialService.findMoedaPaisByIdFilial(f.getIdFilial());
    	if (moedaPais != null) {
    		retorno.put("moedaPais.idMoedaPais",moedaPais.getIdMoedaPais());    		
    	}
		
		return retorno;
	}
	
	/**
	 * Find da combo de moeda a partir do pa�s do usu�rio logado.
	 * @return
	 */
	public List findComboMoedaPais() {
		Pais p = SessionUtils.getPaisSessao();
		List moedaPaises = moedaPaisService.findByPais(p.getIdPais(),Boolean.TRUE);
		
		List retorno = new ArrayList(moedaPaises.size()); 
		
		Iterator iMoedaPaises = moedaPaises.iterator();
		while (iMoedaPaises.hasNext()) {
			MoedaPais mp = (MoedaPais)iMoedaPaises.next();
			Moeda m = mp.getMoeda();
			TypedFlatMap map = new TypedFlatMap();
			map.put("idMoedaPais",mp.getIdMoedaPais());
			map.put("moeda.siglaSimbolo",m.getSiglaSimbolo());
			map.put("moeda.dsSimbolo",m.getDsSimbolo());
			map.put("moeda.idMoeda",m.getIdMoeda());
			map.put("pais.idPais",mp.getPais().getIdPais());
			retorno.add(map);
		}
		
		return retorno;
	}
	
	
	public List findLookupLocalizacaoMercadoria(Map criteria){
		return this.localizacaoMercadoriaService.findLookup(criteria);
	}
    

	/**
	 * @param contatoService The contatoService to set.
	 */
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	/**
	 * @param clienteService The clienteService to set.
	 */
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	/**
	 * @param localizacaoMercadoriaService The localizacaoMercadoriaService to set.
	 */
	public void setLocalizacaoMercadoriaService(
			LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}

	/**
	 * @param emitirCartaServicoLocalizacaoClienteService The emitirCartaServicoLocalizacaoClienteService to set.
	 */
	public void setEmitirCartaServicoLocalizacaoClienteService(
			EmitirCartaServicoLocalizacaoClienteService emitirCartaServicoLocalizacaoClienteService) {
		this.emitirCartaServicoLocalizacaoClienteService = emitirCartaServicoLocalizacaoClienteService;
	}


	/**
	 * @param emitirRelatorioServicosLocalizacaoClienteService The emitirRelatorioServicosLocalizacaoClienteService to set.
	 */
	public void setEmitirRelatorioServicosLocalizacaoClienteService(
			EmitirRelatorioServicosLocalizacaoClienteService emitirRelatorioServicosLocalizacaoClienteService) {
		this.emitirRelatorioServicosLocalizacaoClienteService = emitirRelatorioServicosLocalizacaoClienteService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
}
