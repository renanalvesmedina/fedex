package com.mercurio.lms.coleta.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.coleta.model.DetalheColeta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DetalheColetaDAO extends BaseCrudDao<DetalheColeta, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DetalheColeta.class;
    }

    protected void initFindPaginatedLazyProperties(Map map) {
        map.put("servico",FetchMode.JOIN);
        map.put("naturezaProduto",FetchMode.JOIN);
        map.put("municipio",FetchMode.JOIN);
        map.put("cliente",FetchMode.JOIN);
        map.put("cliente.pessoa",FetchMode.JOIN);
        map.put("localidadeEspecial",FetchMode.JOIN);
        map.put("localidadeEspecial.filial",FetchMode.JOIN);
        map.put("localidadeEspecial.filial.pessoa",FetchMode.JOIN);
        map.put("pedidoColeta",FetchMode.JOIN);
        map.put("moeda",FetchMode.JOIN);
        map.put("filial",FetchMode.JOIN);
        map.put("cotacao",FetchMode.JOIN);
        map.put("cotacao.filialByIdFilialOrigem",FetchMode.JOIN);
    }
    
    
    protected void initFindByIdLazyProperties(Map map) {
        map.put("municipio",FetchMode.JOIN);
        map.put("cliente",FetchMode.JOIN);
        map.put("cliente.pessoa",FetchMode.JOIN);
        map.put("moeda",FetchMode.JOIN);
        map.put("pedidoColeta",FetchMode.JOIN);
        map.put("pedidoColeta.filialByIdFilialResponsavel",FetchMode.JOIN);
        map.put("pedidoColeta.filialByIdFilialResponsavel.pessoa",FetchMode.JOIN);
        map.put("pedidoColeta.filialByIdFilialSolicitante",FetchMode.JOIN);
        map.put("pedidoColeta.filialByIdFilialSolicitante.pessoa",FetchMode.JOIN);
        map.put("pedidoColeta.rotaColetaEntrega",FetchMode.JOIN);
        map.put("pedidoColeta.manifestoColeta",FetchMode.JOIN);
        map.put("pedidoColeta.manifestoColeta.controleCarga",FetchMode.JOIN);
        map.put("pedidoColeta.manifestoColeta.controleCarga.meioTransporteByIdTransportado",FetchMode.JOIN);
    }
    
    
    public ResultSetPage findPaginatedHorarioSaidaViagem(Map criteria, FindDefinition findDefinition) {
		StringBuffer projecao = new StringBuffer()
			.append("distinct new map ( ")
			.append("filialDestino.sgFilial as filial_sgFilial, ")
			.append("rotaOrigem.dsRota as rota_dsRota, ")
			.append("trechoRotaIdaVolta.hrSaida as trechoRotaIdaVolta_hrSaida, ")
			.append("trechoRotaIdaVolta.blSegunda as trechoRotaIdaVolta_blSegunda, ")
			.append("trechoRotaIdaVolta.blTerca as trechoRotaIdaVolta_blTerca, ")
			.append("trechoRotaIdaVolta.blQuarta as trechoRotaIdaVolta_blQuarta, ")
			.append("trechoRotaIdaVolta.blQuinta as trechoRotaIdaVolta_blQuinta, ")
			.append("trechoRotaIdaVolta.blSexta as trechoRotaIdaVolta_blSexta, ")
			.append("trechoRotaIdaVolta.blSabado as trechoRotaIdaVolta_blSabado, ")
			.append("trechoRotaIdaVolta.blDomingo as trechoRotaIdaVolta_blDomingo) ");

		StringBuffer from = new StringBuffer()
			.append(DetalheColeta.class.getName() + " as detalheColeta ")
			.append("join detalheColeta.pedidoColeta as pedidoColeta ")
			.append("join pedidoColeta.filialByIdFilialResponsavel as filialOrigem ")
			.append("join filialOrigem.filialRotas as filialRotaOrigem ")
			.append("join filialRotaOrigem.rota as rotaOrigem ")
			.append("join detalheColeta.filial as filialDestino ")
			.append("join filialDestino.filialRotas as filialRotaDestino ")
			.append("join filialRotaDestino.rota as rotaDestino ")
			.append("join rotaOrigem.rotaIdaVoltas as rotaIdaVolta ")
			.append("join rotaIdaVolta.trechoRotaIdaVoltas as trechoRotaIdaVolta ")
			.append("join trechoRotaIdaVolta.filialRotaByIdFilialRotaOrigem as filialRotaOrigemByTrechoRotaIdaVolta ")
			.append("join trechoRotaIdaVolta.filialRotaByIdFilialRotaDestino as filialRotaDestinoByTrechoRotaIdaVolta ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		
		Map mapPedidoColeta = (Map)criteria.get("pedidoColeta");
		String strIdPedidoColeta = (String)mapPedidoColeta.get("idPedidoColeta");
		
		sql.addCriteria("pedidoColeta.id", "=", Long.valueOf(strIdPedidoColeta));
		sql.addCustomCriteria("rotaOrigem.id = rotaDestino.idRota");
		sql.addCustomCriteria("filialRotaOrigem.nrOrdem < filialRotaDestino.nrOrdem");
		sql.addCustomCriteria("filialRotaOrigemByTrechoRotaIdaVolta.id = filialRotaOrigem.id");
		sql.addCustomCriteria("filialRotaDestinoByTrechoRotaIdaVolta.id = filialRotaDestino.id");
		
		sql.addOrderBy("filialDestino.sgFilial");
		sql.addOrderBy("trechoRotaIdaVolta.hrSaida");
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
    }  


	public Integer getRowCountDetalheColeta(Long idPedidoColeta) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(getDetachedCriteria(idPedidoColeta).setProjection(Projections.rowCount()));
	}


	private DetachedCriteria getDetachedCriteria(Long idPedidoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(DetalheColeta.class);
		dc.add(Restrictions.eq("pedidoColeta.id", idPedidoColeta));
		dc.setFetchMode("servico", FetchMode.JOIN);
		dc.setFetchMode("naturezaProduto", FetchMode.JOIN);
		dc.setFetchMode("municipio", FetchMode.JOIN);
		dc.setFetchMode("municipio.unidadeFederativa", FetchMode.JOIN);
		dc.setFetchMode("municipio.unidadeFederativa.pais", FetchMode.JOIN);
		dc.setFetchMode("localidadeEspecial", FetchMode.JOIN);
		dc.setFetchMode("cliente", FetchMode.JOIN);
		dc.setFetchMode("cliente.pessoa", FetchMode.JOIN);
		dc.setFetchMode("moeda", FetchMode.JOIN);
		dc.setFetchMode("filial", FetchMode.JOIN);
		dc.setFetchMode("filial.pessoa", FetchMode.JOIN);
		dc.setFetchMode("cotacao", FetchMode.JOIN);
		dc.setFetchMode("doctoServico", FetchMode.JOIN);
		dc.setFetchMode("doctoServico.filialByIdFilialOrigem", FetchMode.JOIN);
		dc.setFetchMode("ctoInternacional", FetchMode.JOIN);
		return dc;
	}
	
	private DetachedCriteria getDetachedCriteria(Long idPedidoColeta, Long idServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(DetalheColeta.class);
		dc.add(Restrictions.eq("pedidoColeta.id", idPedidoColeta));
		dc.add(Restrictions.eq("servico.id", idServico));
		return dc;
	}
	
	public List findDetalheColetaByPedidoColetaId(Long idPedidoColeta) {
		return super.findByDetachedCriteria(getDetachedCriteria(idPedidoColeta));		
	}
	
	public List findDetalheColetaByPedidoColetaId(Long idPedidoColeta, Long idServico) {
		return super.findByDetachedCriteria(getDetachedCriteria(idPedidoColeta, idServico));		
	}
	
    /**
     * Find com paginação que busca todos os detalhesColeta dos pedidosColeta pertencentes a um ManifestoColeta 
     * @param idManifestoColeta
     * @return
     */
    public ResultSetPage findPaginatedDetalheColetaByIdManifestoColeta(Long idManifestoColeta, FindDefinition findDefinition){
    	StringBuffer hql = new StringBuffer();
    	hql.append("select detalheColeta ");
    	hql.append("from DetalheColeta as detalheColeta ");
    	hql.append("left join fetch detalheColeta.pedidoColeta pedidoColeta ");
    	hql.append("left join fetch detalheColeta.filial filialDestino ");
    	hql.append("left join fetch detalheColeta.moeda moeda ");
    	hql.append("left join fetch pedidoColeta.cliente cliente ");
    	hql.append("left join fetch pedidoColeta.filialByIdFilialResponsavel filialResponsavel ");
    	hql.append("left join fetch pedidoColeta.usuario usuario ");
    	hql.append("left join fetch cliente.pessoa pessoa "); 
    	hql.append("where pedidoColeta.manifestoColeta.id = :idManifestoColeta ");
    	hql.append("order by pedidoColeta.filialByIdFilialResponsavel, ");
    	hql.append("pedidoColeta.nrColeta");
    	
    	Map map = new HashMap();
    	map.put("idManifestoColeta", idManifestoColeta);
    	return getAdsmHibernateTemplate().findPaginated(hql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), map);
    	
    }
    
    /**
     * RowCount dos detalhesColeta dos pedidosColeta pertencentes a um ManifestoColeta 
     * @param idManifestoColeta
     * @return
     */
    public Integer getRowCountDetalheColetaByIdManifestoColeta(Long idManifestoColeta){
    	StringBuffer hql = new StringBuffer();
    	hql.append("from DetalheColeta as detalheColeta ");
    	hql.append("join detalheColeta.pedidoColeta as pedidoColeta ");
    	hql.append("where pedidoColeta.manifestoColeta.id = :idManifestoColeta ");
    	Map map = new HashMap();
    	map.put("idManifestoColeta", idManifestoColeta);
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), map);
    }

	private SqlTemplate getSqlByPaginatedDetalheColeta(Long idPedidoColeta) {
		StringBuffer projecao = new StringBuffer()
	    	.append("new map(detalheColeta.idDetalheColeta as idDetalheColeta, ")
	    	.append("municipio.nmMunicipio as municipio_nmMunicipio, ")
	    	.append("naturezaProduto.dsNaturezaProduto as naturezaProduto_dsNaturezaProduto, ")
		    .append("clientePessoa.nmPessoa as cliente_pessoa_nmPessoa, ")
		    .append("clientePessoa.tpIdentificacao as cliente_pessoa_tpIdentificacao, ")
		    .append("clientePessoa.nrIdentificacao as cliente_pessoa_nrIdentificacao, ")
	    	.append("servico.dsServico as servico_dsServico, ")
	    	.append("servico.sgServico as servico_sgServico, ")
	    	.append("detalheColeta.tpFrete as tpFrete, ")
	    	.append("detalheColeta.psAforado as psAforado, ")
	    	.append("detalheColeta.qtVolumes as qtVolumes, ")
	    	.append("moeda.dsSimbolo as moeda_dsSimbolo, ")
	    	.append("moeda.sgMoeda as moeda_sgMoeda, ")
	    	.append("moeda.dsSimbolo as moeda_dsSimbolo, ")
	    	.append("detalheColeta.vlMercadoria as vlMercadoria, ")
	    	.append("pedidoColeta.obPedidoColeta as pedidoColeta_obPedidoColeta, ")
	    	.append("filial.sgFilial as filial_sgFilial, ")
	    	.append("cotacao.nrCotacao as cotacao_nrCotacao, ")
	    	.append("filialCotacao.sgFilial as cotacao_filial_sgFilial, ")
	    	.append("ctoInternacional.sgPais as ctoInternacional_sgPais, ")
			.append("doctoServico.nrDoctoServico as nrDoctoServico, ")
			.append("CONCAT(CONCAT(doctoServico.tpDocumentoServico,' '), filialOrigemDocto.sgFilial) as sgFilialOrigemDocto, ")
			.append("doctoServico.idDoctoServico as idDoctoServico, ")
			.append("detalheColeta.blEntregaDireta as blEntregaDireta, ")    	
	    	.append("ctoInternacional.nrCrt as ctoInternacional_nrCrt) ");

		StringBuffer from = new StringBuffer()
			.append(DetalheColeta.class.getName() + " as detalheColeta ")
			.append("left join detalheColeta.municipio as municipio ")
			.append("left join detalheColeta.naturezaProduto as naturezaProduto ")
			.append("left join detalheColeta.cliente as cliente ")
			.append("left join cliente.pessoa as clientePessoa ")
			.append("left join detalheColeta.servico as servico ")
			.append("left join detalheColeta.moeda as moeda ")
			.append("left join detalheColeta.pedidoColeta as pedidoColeta ")
			.append("left join detalheColeta.filial as filial ")
			.append("left join detalheColeta.cotacao as cotacao ")
			.append("left join cotacao.filial as filialCotacao ")
			.append("left join detalheColeta.ctoInternacional as ctoInternacional ")
			
			.append("left join detalheColeta.doctoServico doctoServico ")
			.append("left join detalheColeta.awbColetas awbColetas ")
			.append("left join awbColetas.awb awb ")
			.append("left join awb.ciaFilialMercurio cfmAwb ")
			.append("left join cfmAwb.empresa empresaAwb ")
			.append("left join empresaAwb.pessoa pessoaAwb ")
			.append("left join doctoServico.filialByIdFilialOrigem filialOrigemDocto ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());

		sql.addCriteria("pedidoColeta.id", "=", idPedidoColeta);
		return sql;
	}	
	
	public ResultSetPage findPaginatedByDetalhesColeta(Long idPedidoColeta, FindDefinition findDefinition) {
		SqlTemplate sql = getSqlByPaginatedDetalheColeta(idPedidoColeta);
		sql.addOrderBy("municipio.nmMunicipio");
		sql.addOrderBy("doctoServico.nrDoctoServico");
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
	}

	public Integer getRowCountByDetalhesColeta(Long idPedidoColeta){
    	SqlTemplate sql = getSqlByPaginatedDetalheColeta(idPedidoColeta);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
    }
	
	/**
     * verifica se existe uma controle de carga para o documento de servico (via detalhe da coleta)
     * @param idDoctoServico
     * @return
     */
    public boolean findCCByIdDoctoServicoDetalheColeta(Long idDoctoServico) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("1");
    	
    	hql.addFrom(DetalheColeta.class.getName()+ " dc " +
    			"join dc.doctoServico ds " +
    			"join dc.pedidoColeta pc " +
    			"join pc.manifestoColeta manC " +
    			"join manC.controleCarga cc ");
    	
    	hql.addCriteria("ds.idDoctoServico","=", idDoctoServico);
    	hql.addCriteria("cc.tpStatusControleCarga","<>", "CA");
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).size()>0;
    }
	
}