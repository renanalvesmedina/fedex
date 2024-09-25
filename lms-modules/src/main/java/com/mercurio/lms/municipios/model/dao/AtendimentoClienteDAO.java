package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.AtendimentoCliente;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AtendimentoClienteDAO extends BaseCrudDao<AtendimentoCliente, Long> {
	
    protected void initFindByIdLazyProperties(Map fetchModes) {

    	fetchModes.put("operacaoServicoLocaliza",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.filial",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.filial.pessoa",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.municipio",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.municipio.municipioDistrito",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.tipoLocalizacaoMunicipio",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.servico",FetchMode.JOIN);
    	fetchModes.put("cliente",FetchMode.JOIN);
    	fetchModes.put("cliente.pessoa",FetchMode.JOIN);

	}
	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
    	fetchModes.put("operacaoServicoLocaliza",FetchMode.JOIN);		
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.filial",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.filial.pessoa",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.municipio",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.municipio.municipioDistrito",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.tipoLocalizacaoMunicipio",FetchMode.JOIN);
    	fetchModes.put("operacaoServicoLocaliza.servico",FetchMode.JOIN);
    	fetchModes.put("cliente",FetchMode.JOIN);
    	fetchModes.put("cliente.pessoa",FetchMode.JOIN);

	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AtendimentoCliente.class;
    }
    
    
    /**
     * Verifica se não há outro atendimento para o mesmo cliente, mesmo município,
     * atendendo nos mesmos dias da semana para a mesma vigencia.
     * 
     * @param ac AtendimentoCliente
     * @return True se não encontrar.
     */
    public boolean verificaAtendimentoFrequenciasCliente(AtendimentoCliente ac) {
    	
    	DetachedCriteria dc = createDetachedCriteria();    	
    	if (ac.getIdAtendimentoCliente() != null)
    		dc.add(Restrictions.ne("idAtendimentoCliente", ac.getIdAtendimentoCliente()));
    	dc.add(Restrictions.eq("operacaoServicoLocaliza.idOperacaoServicoLocaliza",ac.getOperacaoServicoLocaliza().getIdOperacaoServicoLocaliza()));
    	dc.createAlias("cliente", "cli");
    	dc.add(Restrictions.eq("cli.idCliente", ac.getCliente().getIdCliente()) );    	

    	
    	dc.createAlias("operacaoServicoLocaliza", "osl");
    	
    	/* para o mesmo municipio */
    	dc.createAlias("osl.municipioFilial", "mf");
    	
    	dc.createAlias("mf.municipio", "mun");
    	
    	dc.add(Restrictions.eq("mun.idMunicipio", ac.getOperacaoServicoLocaliza().getMunicipioFilial().getMunicipio().getIdMunicipio()));
    	
    	/* com as mesmas frequencias de dias da semana */
    	dc.add(ac.getCriterionDiasChecados());
    	
    	/* para a mesma vigencia */
    	dc = JTVigenciaUtils.getDetachedVigencia(dc, ac.getDtVigenciaInicial(), ac.getDtVigenciaFinal());
    	
    	return !(findByDetachedCriteria(dc).size() > 0);
    }

    
    /**
     * Verifica se existe registro de atendimento para o cliente informado
     * @param idCliente
     * @return TRUE se o cliente possui atendimento, FALSE caso contrario
     */
    public boolean validateExisteAtendimentoCliente(Long idCliente, Long idOperacaoServicoLocaliza, YearMonthDay dtVigencia){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.setProjection(Projections.rowCount());
    	dc.add(Restrictions.eq("cliente.idCliente", idCliente));
    	dc.add(Restrictions.eq("operacaoServicoLocaliza.idOperacaoServicoLocaliza", idOperacaoServicoLocaliza));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.or(
					Restrictions.isNull("dtVigenciaFinal"),
					Restrictions.ge("dtVigenciaFinal",dtVigencia)));
    	
    	return (((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0);    	
    }
    
    /**
     * Verifica se existe registro de atendimento para a operação de serviço
     * informado.
     * 
     * @param idOperacaoServicoLocaliza
     * @param dtVigencia
     * @return
     */
    public boolean validateExisteAtendimentoCliente(Long idOperacaoServicoLocaliza, YearMonthDay dtVigencia) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.setProjection(Projections.rowCount());
    	dc.add(Restrictions.eq("operacaoServicoLocaliza.idOperacaoServicoLocaliza", idOperacaoServicoLocaliza));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.or(
					Restrictions.isNull("dtVigenciaFinal"),
					Restrictions.ge("dtVigenciaFinal",dtVigencia)));
    	
    	return (((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0); 
    }
	 
	public List findAtendimentosVigentesByIdServicoLocalizacao(Long idServicoLocalizacao){
		StringBuffer hql = new StringBuffer()
		.append("select new Map(")
		.append("atcp.nrIdentificacao as nrIdentificacao, ")
		.append("atcp.tpIdentificacao as tpIdentificacao, ")
		.append("atcp.nmPessoa as nmPessoa) ")
		.append("from "+AtendimentoCliente.class.getName()+"  as at ")
		.append("inner join at.cliente as atc ")
		.append("left outer join atc.pessoa as atcp ")
		.append("where at.operacaoServicoLocaliza.idOperacaoServicoLocaliza = ? ")
		.append("and at.dtVigenciaInicial <= ? ")
   		.append("and at.dtVigenciaFinal >= ? ")
		.append("order by atcp.nmFantasia, at.dtVigenciaInicial");
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		
		List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idServicoLocalizacao,dataAtual,dataAtual});
		
		return lista;
	}

}