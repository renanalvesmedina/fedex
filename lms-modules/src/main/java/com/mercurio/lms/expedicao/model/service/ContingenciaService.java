package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.Hours;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.Contingencia;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.dao.ContingenciaDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class ContingenciaService  extends CrudService<Contingencia, Long> {
	private ClienteService clienteService;
	private ParametroGeralService parametroGeralService;
	private ManifestoEletronicoService manifestoEletronicoService;


	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param Instância do DAO.
	 */
	public void setContingenciaDAO(ContingenciaDAO contingencia) {
		setDao( contingencia );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ContingenciaDAO getContingenciaDao() {
		return (ContingenciaDAO) getDao();
	}

	/**
	 * Recupera uma instância de <code>MonitoramentoDocEletronico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Contingencia findById(java.lang.Long id) {
		return (Contingencia)super.findById(id);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Contingencia contingencia) {
		return super.store(contingencia);
	}

	public Integer getRowCountContingencia(TypedFlatMap criteria) {
		return getContingenciaDao().getRowCountContingencia(criteria);
	}

	public ResultSetPage findPaginatedContingencia(TypedFlatMap criteria){
		return getContingenciaDao().findPaginatedContingencia(criteria, FindDefinition.createFindDefinition(criteria));
	}


	public Contingencia storeSolicitacao(TypedFlatMap criteria){
		Contingencia contingencia =  getContingenciaDao().findNaoFinalizadaByIdFilialAndTpContingencia(criteria.getLong("idFilial"),
				criteria.getString("tpContingencia"));

		if( contingencia != null ){
			throw new BusinessException("LMS-04395");
		}

		contingencia = new Contingencia();

		Filial filial = new Filial();
		filial.setIdFilial(criteria.getLong("idFilial"));
		contingencia.setFilial(filial);

		contingencia.setDsContingencia(criteria.getString("dsContingencia"));
		contingencia.setDhSolicitacao(JTDateTimeUtils.getDataHoraAtual());

		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());

		contingencia.setUsuarioSolicitante(usuarioLMS);
		contingencia.setTpSituacao(new DomainValue("S"));
		contingencia.setQtEmissoes(0);
		contingencia.setTpContingencia(new DomainValue( criteria.getString("tpContingencia") ));

		contingencia.setIdContingencia((Long)this.store(contingencia));

		return contingencia;
	}


	public Contingencia storeAprovacao(TypedFlatMap criteria){
		Contingencia contingencia = findById(criteria.getLong("idContingencia"));
		contingencia.setDhAprovacao(JTDateTimeUtils.getDataHoraAtual());

		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		contingencia.setUsuarioAprovador(usuarioLMS);

		contingencia.setTpSituacao(new DomainValue("A"));

		this.store(contingencia);

		return contingencia;
	}

	public Contingencia storeFinalizacao(TypedFlatMap criteria){

		Contingencia contingencia = findById(criteria.getLong("idContingencia"));
		contingencia.setDhFinalizacao(JTDateTimeUtils.getDataHoraAtual());

		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		contingencia.setUsuarioFinalizador(usuarioLMS);

		contingencia.setTpSituacao(new DomainValue("F"));

		this.store(contingencia);

		return contingencia;
	}

	/**
	 * 04.01.01.32 Manter Contingencias - ReenviaMDFeConting
	 * 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap executeReenviaMDFeConting(TypedFlatMap criteria) {
		
		TypedFlatMap map = new TypedFlatMap();
		
		
		//PASSO 1
		//Se a Contingência for para a Matriz 
		//(CONTINGENCIA.ID_FILIAL =  conteúdo do parâmetro geral “ID_EMPRESA_MERCURIO”) 
		Contingencia contingencia = findById(criteria.getLong("idContingencia"));
		Object o = parametroGeralService.findConteudoByNomeParametroWithoutException("ID_EMPRESA_MERCURIO", false);
		Long idEmpresaMercurio = (o instanceof BigDecimal) ? ((BigDecimal)o).longValue() : Long.valueOf(o.toString());
		List<ManifestoEletronico> listMdfe;
		if (contingencia.getFilial().getIdFilial().equals(idEmpresaMercurio)) {
			//selecionar todos os registros da tabela MANIFESTO_ELETRONICO e 
			//que estejam na situação “Enviado em Contingência” (MANIFESTO_ELETRONICO.TP_SITUACAO = “T”).
			listMdfe = manifestoEletronicoService.findManifestoEletronicoByTpSituacao(null, "T");
		} else {
			//Caso contrário 
			//selecionar todos os registros da tabela MANIFESTO_ELETRONICO em que 
			//a filial de origem seja igual a filial do da contingência que está sendo encerrada 
			//(CONTINGENCIA.ID_FILIAL = MANIFESTO_ELETRONICO.ID_FILIAL_ORIGEM) 
			//e que estejam na situação “Enviado em Contingência” (MANIFESTO_ELETRONICO.TP_SITUACAO = “T”).
			listMdfe = manifestoEletronicoService.findManifestoEletronicoByTpSituacao(contingencia.getFilial().getIdFilial(), "T");
		}
		
		//PASSO 2
		//Para cada registro selecionado, fazer:
		//- Acessar na tabela TBDATABASEINPUT_CTE o registro de envio para o registro selecionado acima 
		//  (MANIFESTO_ELETRONICO.ID_ENVIO_E = TBDATABASEINPUT_CTE.ID) 
		//  e alterar o campo TBDATABASEINPUT_CTE.STATUS para “0” (Não processado);
		//- Alterar a situação do manifesto eletrônico contido no registro acima para enviado 
		//  (MANIFESTO_ELETRONICO.TP_SITUACAO = “E”).
		for (ManifestoEletronico mdfe: listMdfe) {
			map.put("aguardarAutorizacaoMdfe", true);
			map.put("dhEmissao", JTDateTimeUtils.getDataHoraAtual().toString("yyyy-MM-dd HH:mm:ss"));
			
			manifestoEletronicoService.storeReenvio(mdfe);
	
			List<Long> idsManifestoEletronico = map.getList("idsManifestoEletronico");
			if (idsManifestoEletronico == null) {
				idsManifestoEletronico = new ArrayList<Long>();
				map.put("idsManifestoEletronico", idsManifestoEletronico);
			}
			idsManifestoEletronico.add(mdfe.getIdManifestoEletronico());

		}

		
		return map;
		
	}
	
	
	/**
	 * Incrementa um no número de emissões em contingência
	 * 
	 * @param idContingencia
	 * @param qtEmissoes
	 */
	public void updateQtUtilizacoes(Contingencia contingencia) {
		getContingenciaDao().updateQtUtilizacoes(contingencia);
	}
	

	public Boolean hasContingenciaValidaByFilial(Long idFilial) {
		return getContingenciaDao().hasContingenciaValidaByFilial(idFilial);
	}
	
	public Boolean hasContingenciaMdfeValida(Long idFilial) {
		return getContingenciaDao().hasContingenciaMdfeValida(idFilial);
	}
	
	public Boolean hasContingenciaMdfeValida(Long idFilial, Long idEmpresaMercurio) {
		return getContingenciaDao().hasContingenciaMdfeValida(idFilial, idEmpresaMercurio);
	}


	public void validateProcessarClienteEdi(Long idCliente) {
		Long idFilial 				= SessionUtils.getFilialSessao().getIdFilial();
		Cliente cliente 			= clienteService.findById(idCliente);
		Contingencia contingencia 	= findByFilial(idFilial, "A", "E");

		if (!BooleanUtils.isTrue(cliente.getBlLiberaEtiquetaEdi())) {
			validateContingenciaVencida(contingencia);
		}
	}

	public void validateProcessarClienteEdi(boolean blLiberaEtiquetaEdi, Long idFilial) {

		if (!BooleanUtils.isTrue(blLiberaEtiquetaEdi)) {
			Contingencia contingencia = findByFilial(idFilial, "A", "E");
			validateContingenciaVencida(contingencia);
		}
	}

	public void validateContingenciaFilial(Filial filial) {
		if (filial.getDtImplantacaoLMS() !=null && filial.getDtImplantacaoLMS().isBefore(JTDateTimeUtils.getDataAtual())) {
			Contingencia contingencia = findByFilial(filial.getIdFilial(), "A", "E");
			validateContingenciaVencida(contingencia);
		}
	}


	public Contingencia validateContingenciaVencidaFilial(Filial filial) {
		Contingencia contingencia = findByFilial(filial.getIdFilial(), "A", "E");
		validateContingenciaVencida(contingencia);
		return contingencia;
	}


	private void validateContingenciaVencida(Contingencia contingencia) {
		if (contingencia != null) {
			ParametroGeral paramGeral	= parametroGeralService.findByNomeParametro("TEMPO_VAL_CONTINGENCIA", false);
			Hours horasDiff				= Hours.hoursBetween(contingencia.getDhAprovacao(), JTDateTimeUtils.getDataHoraAtual());
			Hours horasParam			= Hours.hours(Integer.parseInt(paramGeral.getDsConteudo()));

			if (horasDiff.isGreaterThan(horasParam) || horasDiff.equals(horasParam)) {
				throw new BusinessException("LMS-04402");
			}
		}
	}

	public Contingencia findByFilial(Long idFilial, String tpSituacao, String tpContingencia) {
		return getContingenciaDao().findByFilial(idFilial, tpSituacao, tpContingencia);
	}


	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setManifestoEletronicoService(ManifestoEletronicoService manifestoEletronicoService) {
		this.manifestoEletronicoService = manifestoEletronicoService;
	}

}

