package com.mercurio.lms.coleta.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.dao.DetalheColetaDAO;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.detalheColetaService"
 */
public class DetalheColetaService extends CrudService<DetalheColeta, Long> {

	private AwbColetaService awbColetaService;
	private NotaFiscalColetaService notaFiscalColetaService;	


	public AwbColetaService getAwbColetaService() {
		return awbColetaService;
	}
	public void setAwbColetaService(AwbColetaService awbColetaService) {
		this.awbColetaService = awbColetaService;
	}
	public NotaFiscalColetaService getNotaFiscalColetaService() {
		return notaFiscalColetaService;
	}
	public void setNotaFiscalColetaService(NotaFiscalColetaService notaFiscalColetaService) {
		this.notaFiscalColetaService = notaFiscalColetaService;
	}	
	
	
	/**
	 * Recupera uma instância de <code>DetalheColeta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public DetalheColeta findById(java.lang.Long id) {
        return (DetalheColeta)super.findById(id);
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
	 *
	 *
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
    public java.io.Serializable store(DetalheColeta bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDetalheColetaDAO(DetalheColetaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DetalheColetaDAO getDetalheColetaDAO() {
        return (DetalheColetaDAO) getDao();
    }

    /**
     * Find com paginação que busca todos os detalhesColeta dos pedidosColeta pertencentes a um ManifestoColeta 
     * @param idManifestoColeta
     * @return
     */
    public ResultSetPage findPaginatedDetalheColetaByIdManifestoColeta(Long idManifestoColeta, FindDefinition findDefinition){
    	return getDetalheColetaDAO().findPaginatedDetalheColetaByIdManifestoColeta(idManifestoColeta, findDefinition);
    }
    
    /**
     * RowCount dos detalhesColeta dos pedidosColeta pertencentes a um ManifestoColeta 
     * @param idManifestoColeta
     * @return
     */
    public Integer getRowCountDetalheColetaByIdManifestoColeta(Long idManifestoColeta){
    	return getDetalheColetaDAO().getRowCountDetalheColetaByIdManifestoColeta(idManifestoColeta);
    }

    public ResultSetPage findPaginatedHorarioSaidaViagem(Map criteria) {
    	ResultSetPage rsp = getDetalheColetaDAO().findPaginatedHorarioSaidaViagem(criteria, FindDefinition.createFindDefinition(criteria));
    	List lista = AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList());
    	for (Iterator iter = lista.iterator(); iter.hasNext();) {
    		Map map = (Map)iter.next();
    		Map mapTrechoRotaIdaVolta = (Map)map.get("trechoRotaIdaVolta");
    		mapTrechoRotaIdaVolta.put("strHrSaida", ( JTFormatUtils.format((TimeOfDay)mapTrechoRotaIdaVolta.get("hrSaida"), JTFormatUtils.SHORT) ));
    	}
    	rsp.setList(lista);
    	return rsp;
    }
    
	public List findDetalheColeta(Long idPedidoColeta) {
		List listDetalhesColeta = new ArrayList();
		List listDetalhes = getDetalheColetaDAO().findDetalheColetaByPedidoColetaId(idPedidoColeta);
		for(int i=0; i < listDetalhes.size(); i++) {
			DetalheColeta detalheColeta = (DetalheColeta) listDetalhes.get(i);
			detalheColeta.setAwbColetas(this.getAwbColetaService().findAwbColetaByIdDetalheColeta(detalheColeta.getIdDetalheColeta()));
			Hibernate.initialize(detalheColeta.getNotaFiscalColetas());

			listDetalhesColeta.add(i, detalheColeta);
		}
		return listDetalhesColeta;
	}
	
	public List findDetalheColetaByIdPedidoColeta(Long idPedidoColeta) {
		return getDetalheColetaDAO().findDetalheColetaByPedidoColetaId(idPedidoColeta);
	}
	
	public List findDetalheColetaByIdPedidoColetaIdServico(Long idPedidoColeta, Long idServico) {
		return getDetalheColetaDAO().findDetalheColetaByPedidoColetaId(idPedidoColeta, idServico);
	}
	
	public Integer getRowCountDetalheColeta(Long idPedidoColeta) {
		return getDetalheColetaDAO().getRowCountDetalheColeta(idPedidoColeta);
	}


    public ResultSetPage findPaginatedByDetalhesColeta(Long idPedidoColeta, FindDefinition findDefinition) {
    	ResultSetPage rsp = getDetalheColetaDAO().findPaginatedByDetalhesColeta(idPedidoColeta, findDefinition);
    	rsp.setList(AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(rsp.getList()));
    	return rsp;
    }

    /**
     * Executa uma consulta das coletas programadas de acordo com os filtros informados. Retorna o número de resultados retornados.
     * @param criteria
     * @return
     */
    public Integer getRowCountByDetalhesColeta(Long idPedidoColeta) {
    	Integer rowCount = getDetalheColetaDAO().getRowCountByDetalhesColeta(idPedidoColeta);
    	return rowCount;
    }
    
	/**
	 * Se a coleta é com entrega direta valida se a filial logada é a mesma ao da filial destino do cte
	 * 
	 * @param conhecimento
	 * @param blEntregaDireta
	 */
	public void validateEntregaDiretaCliente(Conhecimento conhecimento, Boolean blEntregaDireta) {
		if (blEntregaDireta && !SessionUtils.getFilialSessao().getIdFilial().equals(conhecimento.getFilialByIdFilialDestino().getIdFilial())) {
			throw new BusinessException("LMS-04529");
		}
	}  
	
    public boolean findCCByIdDoctoServicoDetalheColeta(Long idDoctoServico) {
		return getDetalheColetaDAO().findCCByIdDoctoServicoDetalheColeta(idDoctoServico);
	}
}