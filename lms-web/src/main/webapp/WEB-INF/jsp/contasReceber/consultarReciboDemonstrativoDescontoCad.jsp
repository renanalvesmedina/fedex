<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.consultarReciboDemonstrativoDescontoAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/consultarReciboDemonstrativoDesconto" idProperty="idReciboDemonstrativo" >
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-36101"/>			
		</adsm:i18nLabels>
	
		<adsm:hidden property="desconto"/>
	
		<adsm:hidden property="tpTipoDesconto" serializable="false" />
		<adsm:textbox label="tipoDesconto" dataType="text" property="tpDesconto" labelWidth="18%" width="32%" disabled="true"/>
		
		<adsm:hidden property="idFilial" serializable="true"/>
		<adsm:textbox label="filialOrigem" dataType="text" property="sgFilial" size="3" maxLength="3" width="35%" disabled="true">
			<adsm:textbox dataType="text" property="nmFilial" size="30" maxLength="60" disabled="true"/>
		</adsm:textbox>
		
   	    <adsm:textbox label="numero" property="nrDesconto" dataType="integer" size = "10" labelWidth="18%" width="32%" disabled="true"/>

        <adsm:textbox label="situacao" dataType="text" property="tpSituacao" disabled="true" width="35%"/>
        
		<adsm:textbox label="dataEmissao" property="dtEmissaoInicial" dataType="text" size="10" disabled="true" labelWidth="18%" width="32%" />
		
		<adsm:textbox label="valorDesconto" dataType="text" size="10" disabled="true" property="siglaSimbolo" width="35%">
			<adsm:textbox property="vlDesconto" dataType="currency" size="15" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox label="situacaoAprovacao" dataType="text" property="tpSituacaoAprovacao" disabled="true" labelWidth="18%" width="82%"/>
		
		<adsm:textarea label="observacao" property="observacao" maxLength="500" columns="90" rows="5" labelWidth="18%" width="82%" disabled="true"/>

        <adsm:buttonBar>
     		<adsm:button caption="historicoAprovacao" id="btnHistoricoAprovacao" disabled="false"
						 onclick="showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+getElementValue('pendencia.idPendencia'),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');" />
			<adsm:button caption="reenviarRecibo" boxWidth="185" id="btnReenviar" disabled="true" onclick="reenviarRecibo()"/>
			<adsm:button caption="cancelar" id="btnCancelar" disabled="false" onclick="cancelar()"/>
		</adsm:buttonBar>
		
		
		<%-- Usado para guardar o se é recibo ou demonstrativo   --%>
		<adsm:hidden property="tp" />
		<adsm:hidden property="pendencia.idPendencia" />
		<adsm:hidden property="idProcessoWorkflow" />
	</adsm:form>
	
</adsm:window>
<script>

	/**
	*	Habilita/Desabilita o botão Reenviar Recibo conforme o tipo de desconto
	*/
	function myOnPageLoadCallBack_cb(data,erro){
		
		onPageLoad_cb(data,erro);		
		
		var tpTipoDesconto = getElementValue('tpTipoDesconto');

		if( tpTipoDesconto == "R" ){
			setDisabled('btnReenviar',false);			
		} else {
			setDisabled('btnReenviar',true);			
		}


		// verifica se a tela esta sendo chamada da tela de workflow
		var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		var tp = getElementValue('tp');
		
	
		

		if ( idProcessoWorkflow != undefined  && idProcessoWorkflow != "" ){
		
			var dados = new Array();
			setNestedBeanPropertyValue(dados, "tpDescontoSelecionado", tp);
	        setNestedBeanPropertyValue(dados, "idReciboDemonstrativo", idProcessoWorkflow);    

			_serviceDataObjects = new Array();
            addServiceDataObject(createServiceDataObject("lms.contasreceber.consultarReciboDemonstrativoDescontoAction.findByIdFromReciboDemonstrativo",
                                                     "retorno",
                                                     dados));
        	xmit(false);
        	
        	

        	
        }
	}

	/* depois de buscar os dados do  idProcessoWorkflow */
	function retorno_cb(data, erro, errorCode){

		if( erro != undefined ){
			alert(erro);
			return false;		
		}
		
		if (  data != null ){
			
			data = data[0];
			
			setElementValue('siglaSimbolo', data.siglaSimbolo);
			setElementValue('vlDesconto', data.vlDesconto);
			setElementValue('tpSituacao', data.tpSituacao.description);
			setElementValue('tpDesconto', data.tpDesconto);
			setElementValue('idFilial', data.idFilial);
			setElementValue('sgFilial', data.sgFilial);
			setElementValue('nmFilial', data.nmFilial);
			setElementValue('dtEmissaoInicial', data.dtEmissaoInicial);
			setElementValue('idReciboDemonstrativo', data.idReciboDemonstrativo);
			setElementValue('tpTipoDesconto', data.tpTipoDesconto);
			
			if( data.tpSituacaoAprovacao != undefined ){
				setElementValue('tpSituacaoAprovacao', data.tpSituacaoAprovacao.description);
			}else{
				setElementValue('tpSituacaoAprovacao', "");
			}
			
			setElementValue('observacao', data.observacao);
			setElementValue('nrDesconto', data.nrDesconto);
			setElementValue('desconto', data.sgFilial + ' ' + data.nrDesconto);		
			
			format(getElement('vlDesconto'));
			
			
			setDisabled('btnReenviar',true);
			setDisabled('btnCancelar',true);
		
		}
		
		
		return true;
		
		
		
	}

	/**
	*	Habilita/Desabilita o botão Reenviar Recibo conforme o tipo de desconto
	*/	
	function initWindow(eventObj){
	
		
		var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		
		var tpTipoDesconto = getElementValue('tpTipoDesconto');
	
		if( eventObj.name == 'gridRow_click' || eventObj.name == 'tab_click' ){

			if( tpTipoDesconto == "R" ){
				setDisabled('btnReenviar',false);			
			} else {
				setDisabled('btnReenviar',true);		
			}
		
			setDisabled('btnCancelar',false);	
		}
		
		hideMessage(document);
				
		
		if( tpTipoDesconto == "R" ){		
			setFocus(document.getElementById('btnReenviar'),true,true);
		} else {
			setFocus(document.getElementById('btnCancelar'),true,true);	
		}				

		if( eventObj.name == 'tab_click' && idProcessoWorkflow != ""){
			setDisabled('btnReenviar',true);
			setDisabled('btnCancelar',true);
		}

		setDisabled('btnHistoricoAprovacao',false);
		
	}
	
	/**
	*	Método chamado ao clicar no botão Reenviar Recibo, marca o registro ReciboDesconto no campo DH_TRANSMISSAO = null
	*/
	function reenviarRecibo(){
		
		var dados = new Array();
		var idReciboDemonstrativo = getElementValue('idReciboDemonstrativo');
         
        setNestedBeanPropertyValue(dados, "idReciboDemonstrativo", idReciboDemonstrativo);
         
        var sdo = createServiceDataObject("lms.contasreceber.consultarReciboDemonstrativoDescontoAction.executeReenviarRecibo",
                                          "retornoReenviarRecibo",
                                          dados);
        xmit({serviceDataObjects:[sdo]});		
		
	}
	
	/**
	*	Se houver exception lança a excessão, caso contrário mostra msg de sucesso
	*/
	function retornoReenviarRecibo_cb(data, erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('btnReenviar'));
			return false;
		}
		
		showSuccessMessage();
		
	}
	
	/**
	*	Cancela um recibo ou demonstrativo de desconto	
	*
	*/	
	function cancelar(){
	
		if (window.confirm(i18NLabel.getLabel('LMS-36101'))) { 
		
			setDisabled('btnCancelar',true);
	
			var tpDescontoSelecionado = getElementValue('tpTipoDesconto');
			var idReciboDemonstrativo = getElementValue('idReciboDemonstrativo');
			
			
			var dados = new Array();
	         
	        setNestedBeanPropertyValue(dados, "idReciboDemonstrativo", idReciboDemonstrativo);
	        setNestedBeanPropertyValue(dados, "tpDescontoSelecionado", tpDescontoSelecionado);        
	         
	        var sdo = createServiceDataObject("lms.contasreceber.consultarReciboDemonstrativoDescontoAction.executeCancelar",
	                                          "retornoCancelar",
	                                          dados);
	        xmit({serviceDataObjects:[sdo]});
	        
	    }
        	
	}
	
	/**
	*	Se houver exception lança a excessão, caso contrário mostra msg de sucesso
	*/
	function retornoCancelar_cb(data, erro){
		
		if( erro != undefined ){
			
			alert(erro);
			
			var tpDescontoSelecionado = getElementValue('tpTipoDesconto');
			setDisabled('btnCancelar',false);

			if( tpDescontoSelecionado == 'R' ){
				setFocus(document.getElementById('btnReenviar'),true,true);
			} else {
				setFocus(document.getElementById('btnCancelar'),true,true);
			}
			
			return false;
		} else {
			setDisabled('btnCancelar',true);
			
			setElementValue('pendencia.idPendencia', data.idPendencia);
			if ( data.tpSituacaoAprovacao != '' && data.tpSituacaoAprovacao != null  ){ 
				setElementValue('tpSituacaoAprovacao', data.tpSituacaoAprovacao);
			}				
			
		}
		
		showSuccessMessage();
		
	}
	
	function setaDados(data){
	
		if (data.pendencia != undefined){
			setElementValue('pendencia.idPendencia', data.pendencia.idPendencia);
		} else {
			setElementValue('pendencia.idPendencia', "");
		}
		setElementValue('siglaSimbolo', data.siglaSimbolo);
		setElementValue('vlDesconto', setFormat(getElement('vlDesconto'),data.vlDesconto) );
		setElementValue('tpSituacao', data.tpSituacao.description);
		setElementValue('tpDesconto', data.tpDesconto);
		setElementValue('idFilial', data.idFilial);
		setElementValue('sgFilial', data.sgFilial);
		setElementValue('nmFilial', data.nmFilial);
		setElementValue('dtEmissaoInicial', data.dtEmissaoInicial);
		setElementValue('idReciboDemonstrativo', data.idReciboDemonstrativo);
		setElementValue('tpTipoDesconto', data.tpTipoDesconto);
		
		if( data.tpSituacaoAprovacao != undefined ){
			setElementValue('tpSituacaoAprovacao', data.tpSituacaoAprovacao.description);
		}else{
			setElementValue('tpSituacaoAprovacao', "");
		}
		
		setElementValue('observacao', data.observacao);
		setElementValue('nrDesconto', data.nrDesconto);
		setElementValue('desconto', data.sgFilial + ' ' + data.nrDesconto);		
		
		
		
		var evento = new Object();
		
		evento.name = 'gridRow_click';
		initWindow(evento);	
		
	}
</script>