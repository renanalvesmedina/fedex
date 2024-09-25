package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.dao.DevedorDocServDAO;
import com.mercurio.lms.mdfe.model.v300.InfContratante;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.devedorDocServService"
 */
public class DevedorDocServService extends CrudService<DevedorDocServ, Long> {

	public Cliente findByIdDoctoServico(Long idDoctoServico){
		return getDevedorDocServDAO().findClienteDevedorByIdDoctoServico(idDoctoServico);
	}

	
	/**
	 * Obtem DevedorDocServ atrav�s do id do documento
	 * 
	 * Efetuando proxy em DevedorDocServ.Cliente.Pessoa
	 * 
	 * @param  idDoctoServico
	 * @return DevedorDocServ
	 */
	public DevedorDocServ findDevedorByDoctoServico(Long idDoctoServico){
		return getDevedorDocServDAO().findDevedorByDoctoServico(idDoctoServico);
	}	
		
	public List<DevedorDocServ> findDevedoresByDoctoServico(Long idDoctoServico){
		return getDevedorDocServDAO().findDevedoresByDoctoServico(idDoctoServico);
	}
		
	/**
	 * Recupera uma inst�ncia de <code>DevedorDocServ</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public DevedorDocServ findById(Long id) {
		return (DevedorDocServ)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(DevedorDocServ bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setDevedorDocServDAO(DevedorDocServDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private DevedorDocServDAO getDevedorDocServDAO() {
		return (DevedorDocServDAO) getDao();
	}

	public Integer getRowCountByIdDoctoServicoIdCliente(Long idDoctoServico, Long idCliente) {
		return getDevedorDocServDAO().getRowCountByIdDoctoServicoIdCliente(idDoctoServico, idCliente);
	}

	public List<Long> findIdsByIdDoctoServico(Long idDoctoServico) {
		return getDevedorDocServDAO().findIdsByIdDoctoServico(idDoctoServico);
	}

	public void generateDevedorDocServ(DoctoServico doctoServico){
		DevedorDocServ dds = new DevedorDocServ();
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(doctoServico.getFilialByIdFilialOrigem().getIdFilial());
		dds.setCliente(cliente);
		dds.setDoctoServico(doctoServico);
		
		store(dds);
	}

	public List<InfContratante> gerarInfContratanteMdfe(ManifestoEletronico mdfe) {
		List<InfContratante> infContratanteList = new ArrayList<InfContratante>();
		
		for (Conhecimento conhecimento : mdfe.getConhecimentos()) {
			
			Pessoa pessoa = conhecimento.getDevedorDocServs().get(0).getCliente().getPessoa(); 
			
			
			infContratanteList.add(gerarInfContratante(pessoa));
		}
    	
    	return agrupar(infContratanteList);
	}
	
	private InfContratante gerarInfContratante(final Pessoa pessoa) {
		InfContratante infContratante = new InfContratante();
		
		String nrIdentificacao = pessoa.getNrIdentificacao();
		DomainValue tpPessoa = pessoa.getTpPessoa();
		
		if (tpPessoa != null && "J".equals(tpPessoa.getValue())) {
			infContratante.setCNPJ(nrIdentificacao);
		} else {
			infContratante.setCPF(nrIdentificacao);
		}

		return infContratante;
	}
	
	private List<InfContratante> agrupar(List<InfContratante> infContratanteList) {
    	
    	Map<String, InfContratante> infContratanteAgrupados = new HashMap<String, InfContratante>();
    	
    	for (InfContratante infContratante : infContratanteList) {
    		if(infContratante.getCNPJ() != null && !infContratanteAgrupados.containsKey(infContratante.getCNPJ())) {
    			infContratanteAgrupados.put(infContratante.getCNPJ(), infContratante);
    		} else if(infContratante.getCPF() != null && !infContratanteAgrupados.containsKey(infContratante.getCPF())) {
    			infContratanteAgrupados.put(infContratante.getCPF(), infContratante);
    		}
		}
		
    	return new ArrayList<InfContratante>(infContratanteAgrupados.values());
	}
	
}