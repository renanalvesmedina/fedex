<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterRedecoAction" onPageLoadCallBack="myOnPageLoadCallBack" onPageLoad="myOnPageLoad">

	<adsm:form action="/contasReceber/manterRedeco" 
				idProperty="idRedeco" 
				id="redecoForm"
				service="lms.contasreceber.manterRedecoAction">
				
		<adsm:i18nLabels>
			<adsm:include key="LMS-36220"/>
		</adsm:i18nLabels>				
    
    	<!--  adsm:hidden property="idProcessoWorkflow"/ -->
		<adsm:hidden property="tpSituacaoFaturaValido" value="4"/>		

    	
    	<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="33%"
					 labelWidth="19%"
					 required="true"
					 exactMatch="true">
					 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="30" disabled="true"/>
		</adsm:lookup>
    
        <adsm:textbox label="numeroRedeco" property="nrRedeco" dataType="integer" size="10" labelWidth="19%" width="29%" mask="0000000000"/>

		<adsm:combobox label="situacao" property="tpSituacaoRedeco" domain="DM_STATUS_REDECO" labelWidth="19%" width="33%"/>
	
		<adsm:range label="emissao" labelWidth="19%" width="29%" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal"/>
		</adsm:range>
    
        <adsm:range label="liquidacao" labelWidth="19%" width="33%" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtLiquidacaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtLiquidacaoFinal"/>
		</adsm:range>
		
		<adsm:combobox property="tpFinalidade" label="finalidade" labelWidth="19%" domain="DM_FINALIDADE_REDECO" width="29%"/>

		<adsm:lookup label="fatura"
					 popupLabel="pesquisarFilial"
					 action="/municipios/manterFiliais" 
					 service="lms.contasreceber.reemitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialByIdFilial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 picker="false"
					 size="3" 					 
					 maxLength="3" 
					 labelWidth="19%"
					 width="5%"
					 onDataLoadCallBack="verificaFilial"
					 onchange="return trocaFilial()"
					 onPopupSetValue="liberaNrFatura"
					 exactMatch="true">

			<adsm:propertyMapping   relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			
			<adsm:lookup serializable="true"
						 popupLabel="pesquisarFatura"
   					 	 service="lms.contasreceber.reemitirFaturasNacionaisAction.findLookupFatura" 
   					 	 dataType="integer" 
	   					 property="fatura" 
   	 					 idProperty="idFatura"
   	 					 criteriaProperty="nrFatura" 
   	 					 onPopupSetValue="fatura_cb"
    					 size="20"
   						 maxLength="10"
   						 mask="0000000000"
   					 	 width="75%"
   					  	 action="/contasReceber/manterFaturas">
	            
	            <adsm:propertyMapping criteriaProperty="tpSituacaoFaturaValido" modelProperty="tpSituacaoFaturaValido" inlineQuery="true"/> 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.idFilial" modelProperty="filialByIdFilial.idFilial"/> 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.sgFilial" modelProperty="filialByIdFilial.sgFilial" inlineQuery="true"/> 
   				<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filialByIdFilial.pessoa.nmFantasia" inlineQuery="true"/> 
	         </adsm:lookup>
    
        </adsm:lookup>

		<adsm:combobox property="blDigitacaoConcluida" label="digitacaoConcluida" domain="DM_SIM_NAO" labelWidth="19%" width="31%" />

        <adsm:buttonBar freeLayout="true">
			<adsm:button 
				buttonType="findButton"
				caption="consultar" 
				id="__buttonBar:0.findButton" 
				disabled="false" 
				onclick="return myFindButtonScript('redeco', this.form);"/>
				
			<adsm:button id="btnGerarFaturaExcel" caption="exportarExcel" onclick="onclick_imprimeFaturas()"/>			
			
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			idProperty="idRedeco"
			property="redeco" 
			autoSearch="true"
			rows="9"
			selectionMode="none"
			service="lms.contasreceber.manterRedecoAction.findPaginatedRedeco"
			rowCountService="lms.contasreceber.manterRedecoAction.getRowCount"
			defaultOrder="filial_.sgFilial, nrRedeco"
			>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="redeco" property="sgFilial" width="50" />
			<adsm:gridColumn title="" property="nrRedeco" width="40" mask="0000000000" dataType="integer"/>
		</adsm:gridColumnGroup>		
		<adsm:gridColumn width="15%" title="dataEmissao" property="dtEmissao" dataType="JTDate"/>
		<adsm:gridColumn width="15%" title="dataLiquidacao" property="dtLiquidacao" dataType="JTDate"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacaoRedeco" isDomain="true" dataType="text"/>
		<adsm:gridColumn width="14%" title="finalidade" property="tpFinalidade" isDomain="true" dataType="text"/>
		<adsm:gridColumn width="13%" title="tipoRecebimento" property="tpRecebimento" isDomain="true" dataType="text"/>
		<adsm:gridColumn width="10%" title="moeda" property="moeda.siglaSimbolo" dataType="text"/>		
		<adsm:buttonBar> 
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>		
	</adsm:grid>

</adsm:window>
<script>
	function myOnPageLoadCallBack_cb(data,error){
		onPageLoad_cb(data,error);
		findFilialUsuarioLogado();	
	}
	
	function findFilialUsuarioLogado(){
		if( getElement('filial.idFilial').masterLink != 'true' ){
			var remoteCall = {serviceDataObjects:new Array()};
		    var dataCall = createServiceDataObject("lms.contasreceber.manterRedecoAction.findFilialSessao", "onDataLoad", new Array());
		    remoteCall.serviceDataObjects.push(dataCall);
			xmit(remoteCall);
		}
	}	
	
	function initWindow(eventObj){
		if( eventObj.name == 'cleanButton_click' ){
			findFilialUsuarioLogado();	
		}
		setDisabled("btnGerarFaturaExcel", false);
	}
	
		/**
	  * Torna o botão fechar visível, caso a List esteja sendo chamada de uma lookup
	  */
	function myOnPageLoad(){
		
		var url = new URL(parent.location.href);
		
		/** Caso o idProcessoWorkFlow venha na URL, seleciona a Tab de CAD */
		if (url.parameters != undefined && url.parameters.idProcessoWorkflow != undefined && url.parameters.idProcessoWorkflow != ''){   
			setDisabled(document, true);
			getTabGroup(this.document).getTab("pesq").setDisabled(true);
			getTabGroup(this.document).selectTab("cad", "tudoMenosNulo", true);
		} else {
			onPageLoad();
			var url = new URL(location.href);
			if ((url.parameters != undefined) && (url.parameters["mode"] == "lookup")) {
				document.getElementById('btnFechar').property = ".closeButton";
				setDisabled('btnFechar',false);
			} else {
				setVisibility('btnFechar', false);
			}
		}
	}
	
	function myFindButtonScript(callback, form){
		if (!validateForm(document.forms[0])){
			return false;
		}
		
		if ((getElementValue("nrRedeco") == "") &&
			(getElementValue("dtEmissaoInicial") == "" || getElementValue("dtEmissaoFinal") == "") &&
			(getElementValue("dtLiquidacaoInicial") == "" || getElementValue("dtLiquidacaoFinal") == "")
			){
			alertI18nMessage("LMS-36220");
			return false;
		}
		
		return findButtonScript(callback, form);
	}	
	
	function onclick_imprimeFaturas() {
		executeReportWithCallback('lms.contasreceber.manterRedecoAction.execute', 'verificaEmissao', document.forms[0]);
	}

	function verificaEmissao_cb(strFile, error) {
		reportUrl = contextRoot+"/viewBatchReport?open=false&"+strFile._value;
		location.href(reportUrl);
	}


	/* Verifica se possui uma filial válido, se sim libera a NrFatura */
    function verificaFilial_cb(data, errors) {
    	var r = filialByIdFilial_sgFilial_exactMatch_cb(data);
    	
    	if ( data != undefined && data[0] != undefined ){
			setElementValue("filialByIdFilial.sgFilial",data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia",data[0].pessoa.nmFantasia);
    	}
    	
		if (r == true) { liberaNrFatura(); }
		else{ setFocus("filialByIdFilial.sgFilial");  }

		return r;
    }

	function trocaFilial(){
		setElementValue("fatura.idFatura","");
		setElementValue("fatura.nrFatura","");
		
	
		setDisabled("filialByIdFilial.idFilial",false);

		setDisabled("fatura.idFatura", false);
		setDisabled("fatura.nrFatura", true);

		var siglaFilial = getElement('filialByIdFilial.sgFilial');
		var siglaAnterior = siglaFilial.previousValue;
		
		var retorno = filialByIdFilial_sgFilialOnChangeHandler();		
		
		if( siglaAnterior != '' && siglaFilial.value == '' ){
			setFocus('fatura_lupa',false);		
		}

		return retorno;
	}

	function liberaNrFatura(){
		if (getElementValue("filialByIdFilial.sgFilial") != "" ){
			setDisabled("fatura.nrFatura",false);	
			setFocus("fatura.nrFatura");	
		}else{
			setDisabled("fatura.nrFatura",true);
			resetValue( getElement("fatura.idFatura") );
			setFocus("filialByIdFilial.sgFilial");
		}
	}

	function fatura_cb(data, errors){
		setElementValue("filialByIdFilial.sgFilial", data.filialByIdFilial.sgFilial);
		setElementValue("filialByIdFilial.idFilial", data.filialByIdFilial.idFilial);
	}


</script>