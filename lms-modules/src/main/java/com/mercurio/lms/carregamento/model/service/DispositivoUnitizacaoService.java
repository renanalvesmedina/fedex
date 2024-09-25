package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NumberUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.DispCarregDescQtde;
import com.mercurio.lms.carregamento.model.DispCarregIdentificado;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.EstoqueDispIdentificado;
import com.mercurio.lms.carregamento.model.EstoqueDispositivoQtde;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.dao.DispositivoUnitizacaoDAO;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.dispositivoUnitizacaoService"
 */
public class DispositivoUnitizacaoService extends CrudService<DispositivoUnitizacao, Long> {
	
	private static final String PALLET = "PALLET";
	private static final String BAG = "BAG";
	private static final String GAIOLA = "GAIOLA";
	private static final String COFRE = "COFRE";
	
	private EstoqueDispIdentificadoService estoqueDispIdentificadoService;
	private EstoqueDispositivoQtdeService estoqueDispositivoQtdeService;
	private DispCarregDescQtdeService dispCarregDescQtdeService;
	private DispCarregIdentificadoService dispCarregIdentificadoService;
	private ManifestoService manifestoService;			
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private EmpresaService empresaService;
	
	/**
	 * Recupera uma instância de <code>DispositivoUnitizacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    @Override
	public DispositivoUnitizacao findById(java.lang.Long id) {
        return getDispositivoUnitizacaoDAO().findById(id);
    }

    
    public List findConhecimentoByDispositivoUnitizacao(Long idDispositivoUnitizacao, Long idControleCarga){
    	return getDispositivoUnitizacaoDAO().findConhecimentoByDispositivoUnitizacao(idDispositivoUnitizacao, idControleCarga);
    }
    public List findConhecimentoByDispositivoUnitizacao(Long idDispositivoUnitizacao){
    	return findConhecimentoByDispositivoUnitizacao(idDispositivoUnitizacao, null);
    }
    
    public DispositivoUnitizacao findByBarcode(String barcode){
    	DispositivoUnitizacao dispUnit = getDispositivoUnitizacaoDAO().findByBarcode(barcode);
    	if(dispUnit!=null && dispUnit.getTpSituacao()!=null && dispUnit.getTpSituacao().equals(new DomainValue("I"))){
    		//O dispositivo de unitizacao informado se encontra inativo.
    		throw new BusinessException("LMS-45067");
    	}
		return dispUnit;
    }
    
    public DispositivoUnitizacao findByBarcodeIdEmpresa(String barcode , Long idEmpresa){
    	DispositivoUnitizacao dispUnit = getDispositivoUnitizacaoDAO().findByBarcodeIdEmpresa(barcode, idEmpresa);
    	if(dispUnit!=null && dispUnit.getTpSituacao()!=null && dispUnit.getTpSituacao().equals(new DomainValue("I"))){
    		//O dispositivo de unitizacao informado se encontra inativo.
    		throw new BusinessException("LMS-45067");
    	}
		return dispUnit;
    }
    
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    @Override
	public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public DispositivoUnitizacao storeMWW(DispositivoUnitizacao bean) {
    	super.store(bean);
        return findByBarcode(bean.getNrIdentificacao());
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    @Override
	public java.io.Serializable store(DispositivoUnitizacao bean) {
        return super.store(bean);
    }

    /**
     * Caso Tipo de Dispositivo for (COFRE DE CARGA, BAG, PALLET ou GAIOLA)
     * então deve validar que a identificação possua 12 dígitos, sendo os 2 primeiros dígitos igual a:
     * 	21 para Gaiola
     * 	22 para Cofre de Carga
     * 	23 para Bag
     * 	24 para Pallet
     * 
     * @param bean
     */
    private void validarTipoDispositivoUnitizacao(DispositivoUnitizacao bean) {
    	Long idPallet = tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoPallet(); 
    	Long idGaiola = tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoGaiola();
    	Long idCofre = tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoCofre();
    	Long idBag = tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoBag();    	    
    	
    	Long idTipoDispositivo = bean.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao();
    		
    	if(idTipoDispositivo.equals(idPallet) || idTipoDispositivo.equals(idGaiola) || 
    			idTipoDispositivo.equals(idCofre) || idTipoDispositivo.equals(idBag)) {
    		if(!NumberUtils.isNumber(bean.getNrIdentificacao())){
    			throw new BusinessException("LMS-26106");
			}
    		
    		if(bean.getNrIdentificacao().length()<12){
    			throw new BusinessException("LMS-26106");
    		}
    		
    		String filtroIdentificacao = tipoDispositivoUnitizacaoService.getNrTipoDispositivoUnitizacaoById(idTipoDispositivo).toString();
    		if(!bean.getNrIdentificacao().startsWith(filtroIdentificacao)){
    			throw new BusinessException("LMS-26106");
    		}
    	}
	}

	/**
	 * Busca a lista de dispositivos que contenham um pai com o parametro enviado.
	 * @param idDispositivoUnitizacaoPai
	 * @return lista de dispositivos unitizacao 
     */
	public List<DispositivoUnitizacao> findDispositivosByIdPai(Long idDispositivoUnitizacaoPai) {		
		return getDispositivoUnitizacaoDAO().findDispositivosByIdPai(idDispositivoUnitizacaoPai);
    }
    
    /**
     * Desfaz o carregamento dos dispositivos de unitização.
     * Necessária para cancelar o fim do carregamento, excluir um Manifesto ou Controle de Cargas. 
     * @param idManifesto
     */
    public void executeVoltarDispositivosUnitizacaoCarregados(Long idManifesto){
    	List listDispCarregDescQtde = dispCarregDescQtdeService.findDispCarregDescQtdeByIdManifesto(idManifesto);
    	for (Iterator iter = listDispCarregDescQtde.iterator(); iter.hasNext();) {
			DispCarregDescQtde dispCarregDescQtde = (DispCarregDescQtde) iter.next();
			//Dispositivos vinculados a um Controle de Carga
			EstoqueDispositivoQtde estoqueDispositivoQtdeCc = estoqueDispositivoQtdeService.findEstoqueDispositivoQtde(
					dispCarregDescQtde.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao(),
					dispCarregDescQtde.getEmpresa().getIdEmpresa(),
					dispCarregDescQtde.getCarregamentoDescarga().getControleCarga().getIdControleCarga(),
					null);
			if (estoqueDispositivoQtdeCc!=null){
				Integer estoqueAtual = estoqueDispositivoQtdeCc.getQtEstoque();
				estoqueDispositivoQtdeCc.setQtEstoque(estoqueAtual - dispCarregDescQtde.getQtDispositivo());
				if (estoqueDispositivoQtdeCc.getQtEstoque().intValue()<0){
					estoqueDispositivoQtdeCc.setQtEstoque(Integer.valueOf(0));
				}
				estoqueDispositivoQtdeService.store(estoqueDispositivoQtdeCc);
			}
			//Dispositivos vinculados a uma Filial		
			EstoqueDispositivoQtde estoqueDispositivoQtdeFilial = estoqueDispositivoQtdeService.findEstoqueDispositivoQtde(
					dispCarregDescQtde.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao(),
					dispCarregDescQtde.getEmpresa().getIdEmpresa(),
					null,
					SessionUtils.getFilialSessao().getIdFilial());
			if (estoqueDispositivoQtdeFilial!=null){
				Integer estoqueAtual = estoqueDispositivoQtdeFilial.getQtEstoque();
				estoqueDispositivoQtdeFilial.setQtEstoque(estoqueAtual + dispCarregDescQtde.getQtDispositivo());
				estoqueDispositivoQtdeService.store(estoqueDispositivoQtdeFilial);
			}
			dispCarregDescQtdeService.removeById(dispCarregDescQtde.getIdDispCarregDescQtde());
		}
    	
    	//Dispositivos Identificados
    	List listDispCarregIdentificado = dispCarregIdentificadoService.findDispCarregIdentificadoByIdManifesto(idManifesto);
    	for (Iterator iter = listDispCarregIdentificado.iterator(); iter.hasNext();) {
			DispCarregIdentificado dispCarregIdentificado = (DispCarregIdentificado) iter.next();
			EstoqueDispIdentificado estoqueDispIdentificado = estoqueDispIdentificadoService.findEstoqueDispIdentificado(
					dispCarregIdentificado.getDispositivoUnitizacao().getIdDispositivoUnitizacao(),
					dispCarregIdentificado.getCarregamentoDescarga().getControleCarga().getIdControleCarga(),
					null);
			if (estoqueDispIdentificado!=null){
				estoqueDispIdentificado.setControleCarga(null);
				estoqueDispIdentificado.setFilial(SessionUtils.getFilialSessao());
				estoqueDispIdentificadoService.store(estoqueDispIdentificado);
			}
			dispCarregIdentificadoService.removeById(dispCarregIdentificado.getIdDispCarregIdentificado());
		}
    }
    
    
	public void executeAtualizarFilialLocalizacaoDispositivo(Long idDispositivoUnitizacao) {
		
		DispositivoUnitizacao dispositivoUnitizacao = findById(idDispositivoUnitizacao);
		LocalizacaoMercadoria localizacaoMercadoriaNoTerminal = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(ConstantesSim.CD_MERCADORIA_NO_TERMINAL);
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		
		// Atualiza filial dos volumes do dispositivo
		for (VolumeNotaFiscal volumeNotaFiscal : dispositivoUnitizacao.getVolumes()) {
			volumeNotaFiscalService.executeAtualizarFilialLocalizacaoVolume(volumeNotaFiscal);
		}
		
		// Atualiza filial dos sub-dispositivos do disposito
		List<DispositivoUnitizacao> dispositivos = dispositivoUnitizacao.getDispositivosUnitizacao();
		
		for (DispositivoUnitizacao subDispositivoUnitizacao : dispositivos) {
			
			subDispositivoUnitizacao.setLocalizacaoFilial(filialUsuarioLogado);
			subDispositivoUnitizacao.setLocalizacaoMercadoria(localizacaoMercadoriaNoTerminal);
			
			getDispositivoUnitizacaoDAO().executeAtualizarFilialLocalizacaoDispositivo(subDispositivoUnitizacao);
			
			// Atualiza filial dos volumes do sub-dispositivo
			for(VolumeNotaFiscal volumeNotaFiscal : subDispositivoUnitizacao.getVolumes()) {
				volumeNotaFiscalService.executeAtualizarFilialLocalizacaoVolume(volumeNotaFiscal);	
			}
		}
		
		// Atualiza filial do dispositivo e a localização para o terminal
		dispositivoUnitizacao.setLocalizacaoFilial(filialUsuarioLogado);
		dispositivoUnitizacao.setLocalizacaoMercadoria(localizacaoMercadoriaNoTerminal);
		getDispositivoUnitizacaoDAO().executeAtualizarFilialLocalizacaoDispositivo(dispositivoUnitizacao);
	}
    
    @Override
	protected DispositivoUnitizacao beforeStore(DispositivoUnitizacao bean) {
    	validarTipoDispositivoUnitizacao(bean);
    	return bean;
    }
    	  
    public List<DispositivoUnitizacao> findDispositivoUnitizacaoByIdentificacao(final String nrIdentificacao){
    	return getDispositivoUnitizacaoDAO().findDispositivoUnitizacaoByIdentificacao(nrIdentificacao);
    }
        
	public List<DispositivoUnitizacao> findByCarregamentoDescargaManifesto(Long idCarregamentoDescarga, Long idManifesto, List<Short> cdsLocalizacaoMercadoria, String tpManifesto) {
		return getDispositivoUnitizacaoDAO().findByCarregamentoDescargaManifesto(idCarregamentoDescarga, idManifesto, cdsLocalizacaoMercadoria, tpManifesto);
	}
    
	public List findByControleCarga(Long idControleCarga) {
		List lista =  getDispositivoUnitizacaoDAO().findByControleCarga(idControleCarga);
		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
	}

    /**
	 * CQPRO00025051
	 * @param idCarregamentoDescarga
	 * @param idManifesto
	 * @param tpManifesto
	 * @return
     */
	public List<DispositivoUnitizacao> findByCarregamentoDescargaManifesto(Long idCarregamentoDescarga, Long idManifesto, String tpManifesto) {
		return getDispositivoUnitizacaoDAO().findByCarregamentoDescargaManifesto(idCarregamentoDescarga, idManifesto, null, tpManifesto);
    }    
	
	public ResultSetPage<DispositivoUnitizacao> findPaginated(PaginatedQuery paginatedQuery) {
		ResultSetPage rsp = getDispositivoUnitizacaoDAO().findPaginated(paginatedQuery);		
		List<DispositivoUnitizacao> listDispositivos = rsp.getList();
		List listRetorno = new ArrayList();
		Map map = null;
		for (Object dispositivoUnitizacao : listDispositivos.toArray()) {
			map = new TypedFlatMap();
			Object[] campos = (Object[]) dispositivoUnitizacao;
			if(campos.length == 9) { // Se veio faltando algum ï¿½tem, nï¿½o adiciona
				map.put("idDispositivoUnitizacao", Long.parseLong(campos[0].toString()));
				map.put("dsTipoDispositivoUnitizacao", campos[1].toString());
				map.put("idTipoDispositivoUnitizacao", Long.parseLong(campos[2].toString()));
				map.put("nrIdentificacao", campos[3].toString());
				map.put("nmPessoa", campos[4].toString());
				map.put("idEmpresa", Long.parseLong(campos[5].toString()));
				map.put("tpSituacao", campos[6].toString());
				map.put("volumes", Integer.valueOf(campos[7].toString()));
				map.put("dispositivos", Integer.valueOf(campos[8].toString()));				
				listRetorno.add(map);
	}
		}
		rsp.setList(listRetorno);
	
		return rsp; 
	}
	
	@Override
	public Integer getRowCount(Map criteria) {
		return getDispositivoUnitizacaoDAO().getRowCount(criteria);
	}
	
	public ResultSetPage<Map<String,Object>> findPaginatedMap(PaginatedQuery paginatedQuery) {
		ResultSetPage rsp = this.findPaginated(paginatedQuery);
    	
		return rsp;	
	}	
	
	public List findPaginatedMap(TypedFlatMap tfm) {
		ResultSetPage rs = findPaginated(new PaginatedQuery(tfm));
    
		List listDispositivos = rs.getList();
		List listRetorno = new ArrayList();
		Map map = null;
		for (Iterator iter = listDispositivos.iterator(); iter.hasNext();) {
			map = new TypedFlatMap();
			DispositivoUnitizacao dispositivoUnitizacao = (DispositivoUnitizacao) iter.next();
			map.put("idDispositivoUnitizacao", dispositivoUnitizacao.getIdDispositivoUnitizacao());
			map.put("dsTipoDispositivoUnitizacao", dispositivoUnitizacao.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().getValue());
			map.put("idTipoDispositivoUnitizacao", dispositivoUnitizacao.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao());
			map.put("nrIdentificacao", dispositivoUnitizacao.getNrIdentificacao());
			map.put("nmPessoa", dispositivoUnitizacao.getEmpresa().getPessoa().getNmPessoa());
			map.put("idEmpresa", dispositivoUnitizacao.getEmpresa().getIdEmpresa());
			map.put("tpSituacao", dispositivoUnitizacao.getTpSituacao().getDescription().getValue());
			map.put("volumes", dispositivoUnitizacao.getVolumes().size());
			map.put("dispositivos", dispositivoUnitizacao.getDispositivosUnitizacao().size());

			listRetorno.add(map);
		}
	
		return listRetorno;
	}
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DispositivoUnitizacaoDAO getDispositivoUnitizacaoDAO() {
        return (DispositivoUnitizacaoDAO) getDao();
    }
	public List<DispositivoUnitizacao> findByIdManifesto(Long idManifesto) {
		return getDispositivoUnitizacaoDAO().findByIdManifesto(idManifesto);
	}


    public void generateEventoParaDispositivoRemovidoDoPreManifestoVolume(Long idManifesto, Long idDispositivoUnitizacao, String tpScan){
    	DispositivoUnitizacao dispositivoUnitizacao = this.findById(idDispositivoUnitizacao);
    	Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
    	ControleCarga controleCarga = manifesto.getControleCarga();

    	Short cdLocalizacao = dispositivoUnitizacao.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria(); 
    	
		List<Short> cdEventosList= new ArrayList();		
		if (cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_EM_CARREGAMENTO_ENTREGA)) {
			cdEventosList.add(ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_ENTREGA);
			cdEventosList.add(ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_ENTREGA);
		}else if(cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_EM_CARREGAMENTO_VIAGEM)){
			cdEventosList.add(ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_VIAGEM);
			cdEventosList.add(ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_VIAGEM);
		}else if(cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_AGUARDANDO_SAIDA_VIAGEM)){
			cdEventosList.add(ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_VIAGEM);
			cdEventosList.add(ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_VIAGEM);
			cdEventosList.add(ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_VIAGEM);			
		}else if(cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_AGUARDANDO_SAIDA_ENTREGA)){
			cdEventosList.add(ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_ENTREGA);
			cdEventosList.add(ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_ENTREGA);
			cdEventosList.add(ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_COLETA_ENTREGA);					
		} 
		
		if(controleCarga != null && controleCarga.getTpControleCarga().getValue().equals("V")) {			
			//na ET 05.01.01.07 está código 30 e 125, mas de acordo com conversa do o Leonardo vamos manter igual ao Pré-manifesto Documento
			//ou seja, somente gera evento se já existe evento de "FIM DE DESCARGA"
		
			Short[] codigosEvento = new Short[]{ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO}; 			
			List<EventoDispositivoUnitizacao> listEventoDispositivo = eventoDispositivoUnitizacaoService.findEventoDispositivoUnitizacao(
					idDispositivoUnitizacao, SessionUtils.getFilialSessao().getIdFilial(), codigosEvento);
					    			
			if (!listEventoDispositivo.isEmpty()) {
				if(!cdEventosList.contains(Short.valueOf("129")))
					cdEventosList.add(Short.valueOf("129"));
			} else {
				if(!cdEventosList.contains(ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_VIAGEM))
					cdEventosList.add(ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_VIAGEM);
			}
		} else if(controleCarga != null && controleCarga.getTpControleCarga().getValue().equals("C")){			
			//na ET 05.01.01.07 está código 31 e 125, mas de acordo com conversa do o Leonardo vamos manter igual ao Pré-manifesto Documento
			//ou seja, somente gera evento se já existe evento de "FIM DE DESCARGA"
			
			Short[] codigosEvento = new Short[]{ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO};
			List<EventoDispositivoUnitizacao> listEventoDispositivo = eventoDispositivoUnitizacaoService.findEventoDispositivoUnitizacao(
					idDispositivoUnitizacao, SessionUtils.getFilialSessao().getIdFilial(), codigosEvento);	    			
			if (!listEventoDispositivo.isEmpty()) {
				if(!cdEventosList.contains(Short.valueOf("128")))
					cdEventosList.add(Short.valueOf("128"));
			} else {
				if(!cdEventosList.contains(ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_ENTREGA))
					cdEventosList.add(ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_ENTREGA);
			}
		}				
		
		for(Short cdEvento : cdEventosList ){
			eventoDispositivoUnitizacaoService.generateEventoDispositivo(idDispositivoUnitizacao, cdEvento, tpScan);

		}		    
    }
    
    public String generateProximoNumeroDispositivo (String cnpj, String tpDispositivo) {
    	
    	Long tipoDispositivo = this.findTipoDispositivo(tpDispositivo);
    	String nrDipositivo = getDispositivoUnitizacaoDAO().findUltimoItenficadorPorEmpresa(cnpj, tipoDispositivo);
    	if(nrDipositivo == null) {
    		return "0";
    	}
    	Long nrIdentificacao = Long.valueOf(nrDipositivo) + Long.valueOf(1l);
    	Empresa empresa = this.empresaService.findEmpresaByIndetificador(cnpj);
    	this.executeDispositivo(nrIdentificacao.toString(), empresa, tipoDispositivo);
    	return nrIdentificacao.toString();
    }
    
    private Long findTipoDispositivo(String tpDispositivo) {
    	if(PALLET.equalsIgnoreCase(tpDispositivo)) {
    		return tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoPallet();    		    	
    	}
    	
    	if(BAG.equalsIgnoreCase(tpDispositivo)) {
    		return tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoBag();   
    	}
    	
		if(GAIOLA.equalsIgnoreCase(tpDispositivo)) {
			return tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoGaiola(); 	
		}
		
		if(COFRE.equalsIgnoreCase(tpDispositivo)) {
			return tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoCofre();   
		}
    	return null;
    }
    
    /**
     * Eliael
     * @param nrIdentificacao
     * @param idEmpresa
     * @return
     */
    public void executeDispositivo(String nrIdentificacao, Empresa empresa, Long tpDispositivo) {    	    	    	
    	DispositivoUnitizacao dispositivo = new DispositivoUnitizacao();    	
    	
    	dispositivo.setNrIdentificacao(nrIdentificacao);    	
    	dispositivo.setTpSituacao(new DomainValue("A")); 
    	
    	
    	TipoDispositivoUnitizacao tipoDispositivo = new TipoDispositivoUnitizacao();
    	tipoDispositivo.setIdTipoDispositivoUnitizacao(tpDispositivo);
    	dispositivo.setTipoDispositivoUnitizacao(tipoDispositivo);
    	
    	/* Seta o id da empresa */    	
    	dispositivo.setEmpresa(empresa);    	
    	this.store(dispositivo);    	
    }
	
	/**
	 * Busca a lista de dispositivos de unitização que está carregado no do controle de cargas. 
	 * @param idControleCarga
	 * @return
	 */
	public List<DispositivoUnitizacao> findListaDispositivoUnitizacaoCarregadoControleCarga(Long idControleCarga) {
		return getDispositivoUnitizacaoDAO().findListaDispositivoUnitizacaoCarregadoControleCarga(idControleCarga);
	}
	
	public Integer getRowCountDispositivosUnitizacao(final TypedFlatMap criteria) {
		return getDispositivoUnitizacaoDAO().getRowCountDispositivosUnitizacao(criteria);
	}
	
	public List<Map<String, Object>> findDispositivosUnitizacao(final TypedFlatMap criteria) {
		return getDispositivoUnitizacaoDAO().findDispositivosUnitizacao(criteria);
	}
	
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDispositivoUnitizacaoDAO(DispositivoUnitizacaoDAO dao) {
        setDao( dao );
    }


	public void setEstoqueDispIdentificadoService(
			EstoqueDispIdentificadoService estoqueDispIdentificadoService) {
		this.estoqueDispIdentificadoService = estoqueDispIdentificadoService;
	}


	public void setEstoqueDispositivoQtdeService(
			EstoqueDispositivoQtdeService estoqueDispositivoQtdeService) {
		this.estoqueDispositivoQtdeService = estoqueDispositivoQtdeService;
	}


	public void setDispCarregDescQtdeService(
			DispCarregDescQtdeService dispCarregDescQtdeService) {
		this.dispCarregDescQtdeService = dispCarregDescQtdeService;
	}


	public void setDispCarregIdentificadoService(
			DispCarregIdentificadoService dispCarregIdentificadoService) {
		this.dispCarregIdentificadoService = dispCarregIdentificadoService;
	}


	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}


	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}


	public void setTipoDispositivoUnitizacaoService(
			TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService) {
		this.tipoDispositivoUnitizacaoService = tipoDispositivoUnitizacaoService;
	}    	      


	public LocalizacaoMercadoriaService getLocalizacaoMercadoriaService() {
		return localizacaoMercadoriaService;
	}


	public void setLocalizacaoMercadoriaService(
			LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}


	public EmpresaService getEmpresaService() {
		return empresaService;
	}


	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

 }