package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.ItemRedeco;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.contasreceber.model.RepositorioItemRedeco;
import com.mercurio.lms.util.BigDecimalUtils;


/**
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.contasreceber.gerarRedecoService"
 */
public class GerarRedecoService {
	
	private RedecoService redecoService;
	
	private GerarFaturaRedecoService gerarFaturaRedecoService;
	
	private RepositorioItemRedecoService repositorioItemRedecoService;
	
	private AtualizarSituacaoFaturaService atualizarSituacaoFaturaService;
	
	private BoletoService boletoService;
	
	private FaturaService faturaService;
	
    public Redeco store(Redeco redeco, ItemList lstItemRedeco, ItemListConfig config){
    	
    	List lstItemRedecoNew = lstItemRedeco.getNewOrModifiedItems();
    	List lstItemRedecoDeleted = lstItemRedeco.getRemovedItems();    	
    	
    	/*Se a lista de novos itens de redeco ou a lista de itens redeco a excluir for maior que zero, 
    	cancelar todos os recibos de descontos e gerar novos*/
    	Boolean blCancelarReciboDesconto = false;
    	if (lstItemRedecoNew.size() > 0 || lstItemRedecoDeleted.size() > 0){
    		blCancelarReciboDesconto = true;
    	}
    	
    	for (ItemRedeco itemRedeco:(LinkedList<ItemRedeco>)lstItemRedecoDeleted){
    		if (itemRedeco.getFatura()!=null && Boolean.TRUE.equals(itemRedeco.getFatura().getBlOcorrenciaCorp())){
    			Fatura fatura = itemRedeco.getFatura(); 
    			throw new BusinessException("LMS-36266", new Object[]{fatura.getFilialByIdFilial().getSgFilial(),fatura.getNrFatura().toString()});
    		}
    	}
    	
    	/*Informa o tipo de moeda*/
    	setMoeda(redeco, lstItemRedecoNew);
    	
    	/*Salva o Redeco*/ 
    	redecoService.store(redeco);
		
    	/*Atualiza a fatura*/
		prepareNewItemRedeco(lstItemRedecoNew, redeco);
		
		/*Obtem a lista de faturas a serem excluidas*/
		List lstFaturaDeleted = prepareItemRedecoDeleted(redeco, lstItemRedecoDeleted);
		
		/*Salva a lista de itens*/
		redecoService.storeItemRedeco(lstItemRedecoNew, lstItemRedecoDeleted);
		
		/*Faz o somatorio e gera o recibo*/  
		redecoService.afterStore(redeco, lstItemRedeco, config, blCancelarReciboDesconto); 
		
		/*Atualiza a situacao da fatura e boleto*/
		atualizarSituacaoFaturaService.executeVoltarRedeco(lstFaturaDeleted);
		
		redeco.setBlDigitacaoConcluida(new DomainValue("N"));
		
		/*Salva novamente o redeco*/
		redecoService.store(redeco);

		return redeco;
    }

	/**
	 * @param redeco
	 * @param lstItemRedecoNew
	 */
	private void setMoeda(Redeco redeco, List lstItemRedecoNew) {
		if (!lstItemRedecoNew.isEmpty()){    	
    		Long idMoeda = redecoService.getMoeda((ItemRedeco)lstItemRedecoNew.get(0));
    		Moeda moeda = new Moeda();
    		
    		moeda.setIdMoeda(idMoeda);
    		
    		redeco.setMoeda(moeda);
    	}
	}




	/**
	 * Retorna a lista de faturas a ser excluido para poder atualizar elas e os redecos e recibos ligados.
	 * 
	 * @author Micka�l Jalbert
	 * @since 13/07/2006
	 * @param redeco
	 * @param lstItemRedecoDeleted
	 */
	private List prepareItemRedecoDeleted(Redeco redeco, List lstItemRedecoDeleted) {
		List lstFatura = new ArrayList();
		//Tratar os itens a ser excluido
		for (Iterator iter = lstItemRedecoDeleted.iterator(); iter.hasNext();) {
			ItemRedeco itemRedeco = (ItemRedeco) iter.next();
			
			//Se o itemRedeco j� foi salvou no banco, inserir um registro na tabela RepositorioItemRedeco
			if (itemRedeco.getIdItemRedeco() != null && itemRedeco.getIdItemRedeco().longValue() > 0){
				executeInsertRepositorioItemRedeco(redeco, itemRedeco);
				
				
				if (itemRedeco.getFatura() != null){
					lstFatura.add(itemRedeco.getFatura().getIdFatura());
					boletoService.updateSituacaoBoletoAnterior(itemRedeco.getFatura().getIdFatura());
				}
			}
		}
		
		return lstFatura;
	}




	/**
	 * @author Micka�l Jalbert
	 * @since 13/07/2006
	 * @param lstItemRedecoNew
	 */
	private void prepareNewItemRedeco(List lstItemRedecoNew, Redeco redeco) {
		//Tratar os novos itens
		for (Iterator iter = lstItemRedecoNew.iterator(); iter.hasNext();) {
			ItemRedeco itemRedeco = (ItemRedeco) iter.next();
			
			//Se a fatura n�o � null, tem que trocar a situa��o da fatura para 'Em redeco'
			if (itemRedeco.getFatura() != null){
				Fatura fatura = itemRedeco.getFatura();
				
				//Se o idFatura � null quer dizer que tem que gerar uma fatura a partir do devedor
				if (itemRedeco.getFatura().getIdFatura() == null){
					fatura = gerarFaturaRedecoService.storeFaturaWithDevedorDocServFat(new Fatura(), ((ItemFatura)itemRedeco.getFatura().getItemFaturas().get(0)).getDevedorDocServFat());
				}
				
				//Atualiza o boleto (se tem)
				if(itemRedeco.getIdItemRedeco() == null){
				boletoService.updateSituacaoBoleto(fatura.getIdFatura(), "RE");
				}
				
				itemRedeco.setFatura(fatura);
				
				//Alterar a situa��o da fatura para 'Em Redeco'
				fatura.setTpSituacaoFatura(new DomainValue("RE"));
				
				fatura.setRedeco(redeco);
				
				faturaService.storeBasic(fatura);
			}
		}
	}

    
    /**
     * Insere um registro na tabela RepositorioItemRedeco montado a partir do ItemRedeco informado
     * 
     * @author Micka�l Jalbert
     * @since 12/07/2006
     * 
     * @param ItemRedeco itemRedeco
     */
    private void executeInsertRepositorioItemRedeco(Redeco redeco, ItemRedeco itemRedeco){
		RepositorioItemRedeco repositorioItemRedeco = new RepositorioItemRedeco();
		
		repositorioItemRedeco.setFatura(itemRedeco.getFatura());
		repositorioItemRedeco.setObRepositorioItemRedeco(itemRedeco.getObItemRedeco());
		repositorioItemRedeco.setRedeco(redeco);
		repositorioItemRedeco.setVlJuros(itemRedeco.getVlJuros());
		repositorioItemRedeco.setVlTarifa(itemRedeco.getVlTarifa());
		
		repositorioItemRedecoService.store(repositorioItemRedeco);
    }

	public void setRedecoService(RedecoService redecoService) {
		this.redecoService = redecoService;
	}

	public void setGerarFaturaRedecoService(
			GerarFaturaRedecoService gerarFaturaRedecoService) {
		this.gerarFaturaRedecoService = gerarFaturaRedecoService;
	}

	
	public void setRepositorioItemRedecoService(RepositorioItemRedecoService repositorioItemRedecoService) {
		this.repositorioItemRedecoService = repositorioItemRedecoService;
	}




	public void setAtualizarSituacaoFaturaService(
			AtualizarSituacaoFaturaService atualizarSituacaoFaturaService) {
		this.atualizarSituacaoFaturaService = atualizarSituacaoFaturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}	
}
