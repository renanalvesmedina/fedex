package com.mercurio.lms.tabelaprecos.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.tabelaprecos.model.TarifaSpot;
import com.mercurio.lms.tabelaprecos.model.dao.TarifaSpotDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.tarifaSpotService"
 */
public class TarifaSpotService extends CrudService<TarifaSpot, Long> {

	/**
	 * Recupera uma instância de <code>TarifaSpot</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @deprecated Nao utilizar. Ver findTarifaSpotById
	 */
    public TypedFlatMap findByIdMap(java.lang.Long id) {
        TarifaSpot tarifaSpot = (TarifaSpot)super.findById(id);
        TypedFlatMap retorno = new TypedFlatMap();

        retorno.put("idTarifaSpot", tarifaSpot.getIdTarifaSpot());
        retorno.put("vlTarifaSpot", tarifaSpot.getVlTarifaSpot());
        retorno.put("nrPossibilidades", tarifaSpot.getNrPossibilidades());
        retorno.put("nrUtilizacoes", tarifaSpot.getNrUtilizacoes());
        retorno.put("dtLiberacao", tarifaSpot.getDtLiberacao());
        retorno.put("dsSenha", tarifaSpot.getDsSenha());

        Filial filial = tarifaSpot.getFilial();
        if (filial != null) {
	        retorno.put("filial.idFilial", filial.getIdFilial());
	        retorno.put("filial.sgFilial", filial.getSgFilial());
	        retorno.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
        }
        Usuario usuario = tarifaSpot.getUsuarioByIdUsuarioSolicitante();
        if (usuario != null) {
        	retorno.put("usuarioByIdUsuarioSolicitante.idUsuario", usuario.getIdUsuario());
        	retorno.put("funcionario.nrMatricula", usuario.getNrMatricula());
        	retorno.put("funcionario.codPessoa.nome", usuario.getNmUsuario());
        }
        Empresa empresa = tarifaSpot.getEmpresa();
        if (empresa != null) {
	        retorno.put("empresa.idEmpresa", empresa.getIdEmpresa());
	        Pessoa pessoa = empresa.getPessoa();
			retorno.put("empresa.pessoa.nrIdentificacao", pessoa.getNrIdentificacao());
	        retorno.put("empresa.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao()));
	        retorno.put("empresa.pessoa.nmPessoa", pessoa.getNmPessoa());
        }
        Aeroporto aeroportoOrigem = tarifaSpot.getAeroportoByIdAeroportoOrigem();
        if (aeroportoOrigem != null) {
	        retorno.put("aeroportoByIdAeroportoOrigem.idAeroporto", aeroportoOrigem.getIdAeroporto());
	        retorno.put("aeroportoByIdAeroportoOrigem.sgAeroporto", aeroportoOrigem.getSgAeroporto());
	        retorno.put("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa", aeroportoOrigem.getPessoa().getNmPessoa());
        }
        Aeroporto aeroportoDestino = tarifaSpot.getAeroportoByIdAeroportoDestino();
        if (aeroportoDestino != null) {
	        retorno.put("aeroportoByIdAeroportoDestino.idAeroporto", aeroportoDestino.getIdAeroporto());
	        retorno.put("aeroportoByIdAeroportoDestino.sgAeroporto", aeroportoDestino.getSgAeroporto());
	        retorno.put("aeroportoByIdAeroportoDestino.pessoa.nmPessoa", aeroportoDestino.getPessoa().getNmPessoa());
        }
        Moeda moeda = tarifaSpot.getMoeda();
        if (moeda != null) {
	        retorno.put("moeda.idMoeda", moeda.getIdMoeda());
	        retorno.put("moeda.dsSimbolo", moeda.getDsSimbolo());
        }
        Usuario usuarioLiberador = tarifaSpot.getUsuarioByIdUsuarioLiberador();
        if (usuarioLiberador != null) {
        	retorno.put("usuarioByIdUsuarioLiberador.nmUsuario", usuarioLiberador.getNmUsuario());
	        retorno.put("usuarioByIdUsuarioLiberador.funcionario.chapa", usuarioLiberador.getNrMatricula());
	        retorno.put("usuarioByIdUsuarioLiberador.funcionario.codPessoa.nome", usuarioLiberador.getNmUsuario());
        }
        return retorno;
    }

    public TarifaSpot findTarifaSpotById(Long id) {
    	return (TarifaSpot) super.findById(id);
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
	// FIXME corrigir para retornar o ID
    public HashMap store(TarifaSpot bean) {
        super.store(bean);
        HashMap retorno = new HashMap(3);
        retorno.put("idTarifaSpot", bean.getIdTarifaSpot());
        retorno.put("dtLiberacao", bean.getDtLiberacao());
        retorno.put("dsSenha", bean.getDsSenha());
        return retorno;
    }

    protected TarifaSpot beforeInsert(TarifaSpot bean) {
    	TarifaSpot ts = (TarifaSpot)bean;
    	ts.setDtLiberacao(JTDateTimeUtils.getDataAtual());
    	ts.setDsSenha(gerarCodigoLiberacao());
      	Usuario us = SessionContext.getUser();
    	ts.setUsuarioByIdUsuarioLiberador(us);
    	return super.beforeInsert(bean);
    }
    
    public String gerarCodigoLiberacao(){
    	return RandomStringUtils.random(8, false, true);
    }
    
    public ResultSetPage findPaginated(Map criteria) {
    	FilterResultSetPage filterRs = new FilterResultSetPage(super.findPaginated(criteria)) {
			public Map filterItem(Object item) {
				TarifaSpot ts = (TarifaSpot)item;
				TypedFlatMap retorno = new TypedFlatMap();
				retorno.put("idTarifaSpot", ts.getIdTarifaSpot());
		        retorno.put("vlTarifaSpot", ts.getVlTarifaSpot());
		        retorno.put("dtLiberacao", ts.getDtLiberacao());
		        retorno.put("dsSenha", ts.getDsSenha());
		        Moeda m = ts.getMoeda();
		        retorno.put("moeda.dsSimbolo", m.getDsSimbolo());
		        retorno.put("moeda.sgMoeda", m.getSgMoeda());
		        retorno.put("filial.sgFilial", ts.getFilial().getSgFilial());
		        retorno.put("empresa.pessoa.nmPessoa", ts.getEmpresa().getPessoa().getNmPessoa());
		        retorno.put("aeroportoByIdAeroportoOrigem.sgAeroporto", 
		        		ts.getAeroportoByIdAeroportoOrigem().getSgAeroporto());
		        retorno.put("aeroportoByIdAeroportoDestino.sgAeroporto", 
		        		ts.getAeroportoByIdAeroportoDestino().getSgAeroporto());
				return retorno;
			}
    	};
               
        
        return (ResultSetPage) filterRs.doFilter();
	}

    public Map findUsuarioLogado(){
    	Map retorno = new HashMap(3);
		Usuario us = SessionContext.getUser();
    	retorno.put("idUsuario", us.getIdUsuario());
    	retorno.put("nmUsuario", us.getNmUsuario());
    	retorno.put("chapa", us.getNrMatricula());
    	return retorno;
    }

    public TarifaSpot findByDsSenha(String dsSenha) {
    	return getTarifaSpotDAO().findByDsSenha(dsSenha);
    }

    /**
     * Incrementa valor 'nrUtilizacoes' da TarifaSpot, LOCK == 'true'
     * @param idTarifaSpot
     */
    public void generateProximoNrUtilizacoes(Long idTarifaSpot) {
    	TarifaSpot tarifaSpot = getTarifaSpotDAO().findById(idTarifaSpot, true);

    	Integer nrUtilizacoes = IntegerUtils.defaultInteger(Integer.valueOf(tarifaSpot.getNrUtilizacoes()));
		tarifaSpot.setNrUtilizacoes(Byte.valueOf(IntegerUtils.incrementValue(nrUtilizacoes).toString()));

		store(tarifaSpot);
	}

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTarifaSpotDAO(TarifaSpotDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    public TarifaSpotDAO getTarifaSpotDAO() {
        return (TarifaSpotDAO) getDao();
    }

}