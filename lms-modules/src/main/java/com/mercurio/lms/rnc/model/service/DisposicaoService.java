package com.mercurio.lms.rnc.model.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.rnc.model.Disposicao;
import com.mercurio.lms.rnc.model.MotivoDisposicao;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.dao.DisposicaoDAO;
import com.mercurio.lms.rnc.model.dao.MotivoDisposicaoDAO;
import com.mercurio.lms.rnc.model.dao.OcorrenciaNaoConformidadeDAO;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.rnc.disposicaoService"
 */
public class DisposicaoService extends CrudService<Disposicao, Long> {

    private static final String DS_MOTIVO_DISPOSICAO = "Mercadoria indenizada";
    private static final int PAD_MAX_SIZE = 8;

	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private MotivoDisposicaoService motivoDisposicaoService;
	private UsuarioService usuarioService;
	private FilialService filialService;
	private MotivoDisposicaoDAO motivoDisposicaoDAO;
	private OcorrenciaNaoConformidadeDAO ocorrenciaNaoConformidadeDAO;

	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public MotivoDisposicaoDAO getMotivoDisposicaoDAO() {
		return motivoDisposicaoDAO;
	}

	public void setMotivoDisposicaoDAO(MotivoDisposicaoDAO motivoDisposicaoDAO) {
		this.motivoDisposicaoDAO = motivoDisposicaoDAO;
	}

	public MotivoDisposicaoService getMotivoDisposicaoService() {
		return motivoDisposicaoService;
	}

	public void setMotivoDisposicaoService(MotivoDisposicaoService motivoDisposicaoService) {
		this.motivoDisposicaoService = motivoDisposicaoService;
	}

	public OcorrenciaNaoConformidadeService getOcorrenciaNaoConformidadeService() {
		return ocorrenciaNaoConformidadeService;
	}

	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public OcorrenciaNaoConformidadeDAO getOcorrenciaNaoConformidadeDAO() {
		return ocorrenciaNaoConformidadeDAO;
	}

	public void setOcorrenciaNaoConformidadeDAO(OcorrenciaNaoConformidadeDAO ocorrenciaNaoConformidadeDAO) {
		this.ocorrenciaNaoConformidadeDAO = ocorrenciaNaoConformidadeDAO;
	}

	/**
	 * Recupera uma instância de <code>Disposicao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public Disposicao findById(java.lang.Long id) {
        return (Disposicao)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
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
    public java.io.Serializable store(Disposicao bean) {
    	return super.store(bean);
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
     * Fara mais de uma insercao caso recebe mais de um id de 'Ocorrencia Não Conformidade'.
     * 
     * @param bean entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable storeRegistrarDisposicao(Object bean) {
    	
    	Usuario usuario = SessionUtils.getUsuarioLogado();
    	Filial filial = SessionUtils.getFilialSessao();
    	
        Disposicao disposicao = null;

    	Map mapDisposicao = (HashMap) bean;
    	Map mapMotivoDisposicao = (HashMap) mapDisposicao.get("motivoDisposicao"); 
    	List idsOcorrenciaNaoConformidade = (List) mapDisposicao.get("idsOcorrenciaNaoConformidade");

    	if (idsOcorrenciaNaoConformidade!=null) {
    		DoctoServico doctoServico = null;
    		Long idNaoConformidade = null;
    		for (Iterator iter = idsOcorrenciaNaoConformidade.iterator(); iter.hasNext();) {

				Map mapIdNaoConformidade = (HashMap) iter.next();

				OcorrenciaNaoConformidade onc = ocorrenciaNaoConformidadeService.findById(Long.valueOf((String)mapIdNaoConformidade.get("idOcorrenciaNaoConformidade")));

				idNaoConformidade = onc.getNaoConformidade().getIdNaoConformidade();
				if (onc.getNaoConformidade().getDoctoServico() != null) {
					doctoServico = onc.getNaoConformidade().getDoctoServico();
				}

				MotivoDisposicao motivoDisposicao = 
                        this.getMotivoDisposicaoService().findById(
                                Long.valueOf((String) mapMotivoDisposicao.get("idMotivoDisposicao")));
				

                if (CollectionUtils.isEmpty(onc.getDisposicoes())) {
				disposicao = new Disposicao();
                    disposicao.setOcorrenciaNaoConformidade(onc);
                    onc.getDisposicoes().add(disposicao);
                } else {
                    //O campo é único, só pode existir uma disposição vinculada ao
                    disposicao = (Disposicao) onc.getDisposicoes().get(0);
                }
				
				disposicao.setDhDisposicao(JTDateTimeUtils.getDataHoraAtual());
				disposicao.setDsDisposicao((String) mapDisposicao.get("dsDisposicao"));
				
				disposicao.setMotivoDisposicao(motivoDisposicao);
				
				disposicao.setUsuario(usuario);
				disposicao.setFilial(filial);
				
				//Seta o status da ocorrenciaNC para fechado. 
				onc.setTpStatusOcorrenciaNc(new DomainValue("F"));
				//Verifica o motivo da disposiçao
				if (motivoDisposicao.getBlReverteRespFilAbertura().booleanValue()) {
					onc.setFilialByIdFilialResponsavel(onc.getFilialByIdFilialAbertura());
				}
				//Salva a ocorrencia nao conformidade
				this.getOcorrenciaNaoConformidadeDAO().store(onc);
			}

            if (doctoServico != null && ocorrenciaNaoConformidadeService.findOcorrenciasNaoConformidadeAbertas(idNaoConformidade).isEmpty()) {
                String strDocumento = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(doctoServico.getNrDoctoServico().toString(), PAD_MAX_SIZE, '0');
					incluirEventosRastreabilidadeInternacionalService.
							generateEventoDocumento(ConstantesSim.EVENTO_RNC_ENCERRADA, 
													doctoServico.getIdDoctoServico(), 
													filial.getIdFilial(), 
													strDocumento, 
													JTDateTimeUtils.getDataHoraAtual(), 
													null, 
													filial.getSiglaNomeFilial(), 
													doctoServico.getTpDocumentoServico().getValue());
				}

			}
    	return disposicao; 
    	
    }
    
    /**
     * Faz a chamada para a consulta de <code>findDisposicaoByIdOcorrenciaNaoConformidade</code>
     * retornando um typedFlatMap com os resultados necessários para tela "Exibir Disposição"
     * 
     * @param idOcorrenciaNaoConformidade
     * @return
     */
    public TypedFlatMap findDisposicaoByIdOcorrenciaNaoConformidade(Long idOcorrenciaNaoConformidade) {
    	TypedFlatMap mapRetorno = new TypedFlatMap();
    	Disposicao disposicao = this.findByIdOcorrenciaNaoConformidade(idOcorrenciaNaoConformidade);
    	if (disposicao != null) {
	    	mapRetorno.put("idOcorrenciaNaoConformidade", idOcorrenciaNaoConformidade);
	    	mapRetorno.put("dsCausaNc", disposicao.getOcorrenciaNaoConformidade().getDsCausaNc());
	    	mapRetorno.put("dsCausaNaoConformidade", disposicao.getOcorrenciaNaoConformidade().getCausaNaoConformidade().getDsCausaNaoConformidade());    	
	    	mapRetorno.put("idDisposicao", disposicao.getIdDisposicao());
	    	mapRetorno.put("dsDisposicao", disposicao.getDsDisposicao());
	    	mapRetorno.put("dhDisposicao", disposicao.getDhDisposicao());
	    	mapRetorno.put("nmUsuario", disposicao.getUsuario().getNmUsuario());
	    	mapRetorno.put("dsMotivo", disposicao.getMotivoDisposicao().getDsMotivo());
    	}
    	return mapRetorno;
    }
    
    /**
     * Insere um novo registro na tabela Disposição
     *
     * @param recibo
     * @param ocorrencia
     */
    public void insertDisposicao(PpdRecibo recibo, OcorrenciaNaoConformidade ocorrencia){
    	Usuario usuarioLogado = SessionUtils.getUsuarioLogado();    	
    	DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
    	Filial filial = SessionUtils.getFilialSessao();
        MotivoDisposicao motivoDisposicao = motivoDisposicaoService.findMotivoDisposicaoByDsMotivoDisposicao(DS_MOTIVO_DISPOSICAO);
    
    	Disposicao disposicao = new Disposicao();
    	
    	disposicao.setOcorrenciaNaoConformidade(ocorrencia);    	
    	disposicao.setFilial(filial);
    	disposicao.setUsuario(usuarioLogado);    	
    	disposicao.setMotivoDisposicao(motivoDisposicao);    	
    	disposicao.setDsDisposicao("Mercadoria indenizada conforme RIM " + recibo.getFilial().getSgFilial() + "		" + recibo.getNrRecibo());    	
    	disposicao.setDhDisposicao(dhAtual);
    	
    	store(disposicao);    	
    }    
    
    /**
     * Solicitação CQPRO00004843 da integração.
     * Busca uma Disposição a partir do id da Ocorrência de Não Conformidade.
     * 
     * @param idOcorrenciaNaoConformidade
     * @return
     */
    public Disposicao findByIdOcorrenciaNaoConformidade(String idOcorrenciaNaoConformidade) {
    	return this.getDisposicaoDAO().findByIdOcorrenciaNaoConformidade(Long.valueOf(idOcorrenciaNaoConformidade));
    }

    /**
     * Busca uma Disposição a partir do id da Ocorrência de Não Conformidade.
     * Utiliza o método do DAO criado para a integração.
     * 
     * @param idOcorrenciaNaoConformidade
     * @return
     */
    public Disposicao findByIdOcorrenciaNaoConformidade(Long idOcorrenciaNaoConformidade) {
    	return this.getDisposicaoDAO().findByIdOcorrenciaNaoConformidade(idOcorrenciaNaoConformidade);
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param dao Instância do DAO.
     */
    public void setDisposicaoDAO(DisposicaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DisposicaoDAO getDisposicaoDAO() {
        return (DisposicaoDAO) getDao();
    }
}