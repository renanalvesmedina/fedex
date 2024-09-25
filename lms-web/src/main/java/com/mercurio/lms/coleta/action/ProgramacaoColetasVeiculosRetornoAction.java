package com.mercurio.lms.coleta.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.programacaoColetasVeiculosRetornoAction"
 */

public class ProgramacaoColetasVeiculosRetornoAction {

	private static final String VALIDA_GER_RISCO = "VALIDA_GER_RISCO";
	private ConfiguracoesFacade configuracoesFacade;
	private MeioTransporteService meioTransporteService;
	private OcorrenciaColetaService ocorrenciaColetaService;
	private PedidoColetaService pedidoColetaService;
	private UsuarioService usuarioService;

	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public OcorrenciaColetaService getOcorrenciaColetaService() {
		return ocorrenciaColetaService;
	}
	public void setOcorrenciaColetaService(OcorrenciaColetaService ocorrenciaColetaService) {
		this.ocorrenciaColetaService = ocorrenciaColetaService;
	}
    public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public List findOcorrenciaColeta(Map criteria) {
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsDescricaoCompleta:asc");
    	List list = getOcorrenciaColetaService().findListByCriteria(criteria, campoOrdenacao);
    	List listaRetorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		OcorrenciaColeta oc = (OcorrenciaColeta)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idOcorrenciaColeta", oc.getIdOcorrenciaColeta());
    		tfm.put("dsDescricaoCompleta", oc.getDsDescricaoCompleta());
    		tfm.put("blIneficienciaFrota", oc.getBlIneficienciaFrota());
    		listaRetorno.add(tfm);
    	}
        return listaRetorno;
    }

	public void generateRetornarColeta(TypedFlatMap criteria) {
    	getPedidoColetaService().generateRetornarColeta(criteria);
    }

	public List findLookupMeioTransporte(Map criteria) {
    	List list = getMeioTransporteService().findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		MeioTransporte meioTransporte = (MeioTransporte)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
    		typedFlatMap.put("nrIdentificador", meioTransporte.getNrIdentificador());
    		typedFlatMap.put("nrFrota", meioTransporte.getNrFrota());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
	
    public TypedFlatMap getDataUsuario() {
    	Filial filialUsuario = SessionUtils.getFilialSessao();
    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.put("filialUsuario.idFilial", filialUsuario.getIdFilial());
    	tfm.put("filialUsuario.sgFilial", filialUsuario.getSgFilial());
    	tfm.put("filialUsuario.pessoa.nmFantasia", filialUsuario.getPessoa().getNmFantasia());
    	return tfm;
    }


    public List findLookupUsuarioFuncionario(TypedFlatMap tfm){
    	return usuarioService.findLookupUsuarioFuncionario(
    			tfm.getLong("idUsuario"), tfm.getString("nrMatricula"), null, null, null, null, true);
    }


    public TypedFlatMap generateTransmitirColeta(TypedFlatMap criteria) {
    	Long idMeioTransporte = criteria.getLong("idMeioTransporte");
    	Long idControleCarga = criteria.getLong("idControleCarga");
    	Long idPedidoColeta = criteria.getLong("idPedidoColeta");
    	String nmUsuarioByLiberacao = criteria.getString("nmUsuarioByLiberacao");
    	String dsMotivoByLiberacao = criteria.getString("dsMotivoByLiberacao");
    	
    	PedidoColeta pc = pedidoColetaService.findById(idPedidoColeta);
    	
    	String dsEvento = "";
    	if (nmUsuarioByLiberacao != null && !nmUsuarioByLiberacao.equals("")) {
    		Object obj[] = new Object[] {nmUsuarioByLiberacao, 
    									 FormatUtils.formatSgFilialWithLong(pc.getFilialByIdFilialResponsavel().getSgFilial(), pc.getNrColeta(), "00000000"), 
    									 dsMotivoByLiberacao};
    		dsEvento = configuracoesFacade.getMensagem("LMS-02084", obj);
    	}
    	if (dsEvento.length() > 300) {
    		dsEvento = dsEvento.substring(0, 300);
    	}
    	getPedidoColetaService().generateTransmitirColeta(
    			idMeioTransporte, idControleCarga, idPedidoColeta, dsEvento);

    	TypedFlatMap mapRetorno = new TypedFlatMap();
   		mapRetorno.put("blRedirecionar", criteria.getBoolean("blRedirecionar"));
    	return mapRetorno;

    }


    public TypedFlatMap generateLiberacaoGerRisco(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("idControleCarga");
    	Long idPedidoColeta = criteria.getLong("idPedidoColeta");
    	if (idPedidoColeta == null) {
    		throw new BusinessException("LMS-02083");
    	}

    	TypedFlatMap mapRetorno = new TypedFlatMap();
    	try {
    		boolean validateGerRisco  = this.checkValidaGerRisco();    		
    		mapRetorno = pedidoColetaService.generateTransmissaoColetaByLiberacaoGerRisco(idControleCarga, criteria.getLong("idMeioTransporte"), idPedidoColeta, validateGerRisco);
    	} catch (BusinessException be) {
			if (be.getMessageKey().equals("ExecutarRollback")) {
				mapRetorno.put("blPossuiLiberacaoGerRisco", Boolean.FALSE);
				mapRetorno.put("idControleCarga", idControleCarga);

				TypedFlatMap mapExigGerRisco = pedidoColetaService.validateExigenciasGerRisco(idControleCarga);
		    	Boolean blPossuiExigenciasGerRisco = mapExigGerRisco.getBoolean("blPossuiExigenciasGerRisco");
		    	mapRetorno.put("blRedirecionar", blPossuiExigenciasGerRisco);
			}
			else
				throw be;
		}
    	return mapRetorno;
    }
    
	private boolean checkValidaGerRisco() {
		Object value = configuracoesFacade.getValorParametro(SessionUtils.getFilialSessao().getIdFilial(), VALIDA_GER_RISCO);
		return !"N".equals(value == null ? "" : value.toString());
	}
}