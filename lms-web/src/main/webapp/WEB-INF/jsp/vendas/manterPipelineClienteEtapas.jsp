<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
function desabilitaLookupVisita(){
	onPageLoad();
}

function setaFocoLupa_cb() {
	onPageLoad_cb();
	desabilitaDsVisita();
}    
</script>
<adsm:window service="lms.vendas.manterPipelineClienteAction" onPageLoad="desabilitaLookupVisita" onPageLoadCallBack="setaFocoLupa">
	<adsm:form action="/vendas/manterPipelineCliente" idProperty="idPipelineCliente" 
		service="lms.vendas.manterPipelineClienteAction.findByIdCustom" height="380">

	<adsm:hidden property="idGerente" serializable="false"/>
	<adsm:hidden property="consultou" serializable="false"/>
	<adsm:hidden property="tpSituacao" serializable="false"/>
	<adsm:hidden property="filial.idFilial"/>
	<adsm:hidden property="dtAtual" serializable="false"/>
		<adsm:hidden property="idRegional" serializable="false" />
		<adsm:hidden property="idPipelineEtapa1" serializable="true"/>
		<adsm:hidden property="idPipelineEtapa2" serializable="true"/>
		<adsm:hidden property="idPipelineEtapa3" serializable="true"/>
		<adsm:hidden property="idPipelineEtapa4" serializable="true"/>
		<adsm:hidden property="idPipelineEtapa5" serializable="true"/>
		<adsm:hidden property="idPipelineEtapa6" serializable="true"/>	
		<adsm:hidden property="idPipelineEtapa7" serializable="true"/>
		<adsm:hidden property="idPipelineEtapa8" serializable="true"/>			
		<adsm:hidden property="idPipelineEtapa9" serializable="true"/>
		<adsm:hidden property="idPipelineEtapa10" serializable="true"/>
		<adsm:hidden property="idPipelineEtapa11" serializable="true"/>
		<adsm:hidden property="idPipelineEtapa12" serializable="true"/>
		<adsm:i18nLabels>
				<adsm:include key="LMS-10059"/>
				<adsm:include key="LMS-01056"/>
		</adsm:i18nLabels>
	
	<adsm:textbox
		label="filial"
		dataType="text"
		property="filial.sgFilial"
		size="3"
		maxLength="3"
		required="false"
		disabled="true"
		serializable="false"
		labelWidth="10%"
			width="45%">
		<adsm:textbox
			dataType="text"
			property="filial.pessoa.nmFantasia"
			size="25"
			maxLength="60"
			disabled="true"
			serializable="false"
		/>
	</adsm:textbox>

	<adsm:textbox
		dataType="text" 
		label="regional"
		property="siglaDescricao" 
		disabled="true"
		size="35"
		maxLength="60"
			width="25%"
		labelWidth="12%"
		serializable="false"
			required="false"/>
	
	<adsm:lookup
		action="/configuracoes/consultarFuncionariosView"
		service="lms.vendas.manterPipelineClienteAction.findLookupFuncionario"
		dataType="text"
		required="false"
		property="usuarioByIdUsuario"
		idProperty="idUsuario"
		criteriaProperty="nrMatricula"
		label="funcionario"
		size="17"
		maxLength="10"
		exactMatch="true"
		width="45%"
		disabled="true"
		labelWidth="10%"
		onDataLoadCallBack="funcCallBack"
			afterPopupSetValue="funcCallBack">
		<adsm:propertyMapping
			relatedProperty="usuarioByIdUsuario.nmUsuario"
			modelProperty="nmUsuario" />
		<adsm:textbox
			dataType="text" 
			property="usuarioByIdUsuario.nmUsuario" 
			size="35" 
			maxLength="50" 
			disabled="true"
			serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.sim.manterPedidosComprasAction.findLookupCliente" 
			required="false"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="cliente" 
			size="17" 
			disabled="true"
			maxLength="18"
			dataType="text"
			width="45%"
			labelWidth="10%">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text" 
				property="cliente.pessoa.nmPessoa" 
				size="35" 
				maxLength="50"
				disabled="true" 
				serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox
			property="cliente.tpCliente" 
			label="tipoCliente"
			domain="DM_TIPO_CLIENTE"
			onlyActiveValues="true"
			disabled="true"
			labelWidth="12%"
			width="25%"
			serializable="false"/>
			
		<!-- campos de data e observacao -->
	<adsm:section caption="prospeccao"/>	
		
	<adsm:label key="branco" width="20%" style="border:none;"/>	
		<adsm:label key="dataEvento" width="16%" style="border:none;"><font color="red" size="2">*</font></adsm:label>
	<adsm:label key="registroVisita" width="27%" style="border:none;"/>
		<adsm:label key="observacao" width="30%" style="border:none;"><font color="red" size="2">*</font></adsm:label>
				
		<!-- evento 1 -->
	<adsm:textbox
		dataType="JTDate" 
		label="oportunidade"
		property="dtEvento1" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
			onchange="return comparaDatasEvento('1');"/>
			
		<adsm:label key="espacoBranco" style="border:none;" width="27%"/>
		
	<adsm:textbox
		dataType="text" 
		property="dsObservacao1" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
		required="false" />
		
		<!-- evento 2 -->
	<adsm:textbox
		dataType="JTDate" 
		label="primeiroContato"
		property="dtEvento2" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDatasEvento('2');"/>
		<adsm:label key="espacoBranco" style="border:none;" width="27%"/>
	<adsm:textbox
		dataType="text" 
		property="dsObservacao2" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
		required="false" 	/>
	
	<adsm:section caption="negociacao"/>
		<!-- evento 3 -->
	<adsm:textbox
		dataType="JTDate" 
		label="primeiraReuniao"
		property="dtEvento3" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDatasEvento('3');"/>	
	<adsm:lookup
			action="/vendas/manterRegistrosVisita"
			service="lms.vendas.manterPipelineClienteAction.findLookupRegistroVisita" 
			property="visita3"
			idProperty="idVisita"
			criteriaProperty="dsVisita"
			size="30" 
			maxLength="100"
			dataType="text"
			width="27%"
			disabled="true"
			serializable="true"
			afterPopupSetValue="visitaPopup3" />
	<adsm:textbox
		dataType="text" 
		property="dsObservacao3" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
		required="false"  />
	
		<!--campo 4 com visita	 -->
	<adsm:textbox
		dataType="JTDate" 
		label="entregaProposta"
		property="dtEvento4" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDatasEvento('4');"/>	
	<adsm:lookup
			action="/vendas/manterRegistrosVisita"
			service="lms.vendas.manterPipelineClienteAction.findLookupRegistroVisita" 
			property="visita4"
			idProperty="idVisita"
			criteriaProperty="dsVisita"
			size="30" 
			maxLength="100"
			dataType="text"
			width="27%"
			serializable="true"
			afterPopupSetValue="visitaPopup4"/>
	<adsm:textbox
		dataType="text" 
		property="dsObservacao4" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
			required="false"/>
	
		<!-- 		evento 5		 -->
    <adsm:textbox
		dataType="JTDate" 
		label="reuniaoNegociacao"
		property="dtEvento5" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDatasEvento('5');"/>	
	<adsm:lookup
			action="/vendas/manterRegistrosVisita"
			service="lms.vendas.manterPipelineClienteAction.findLookupRegistroVisita" 
			property="visita5"
			idProperty="idVisita"
			criteriaProperty="dsVisita"
			size="30" 
			maxLength="100"
			dataType="text"
			width="27%"
			serializable="true"
			afterPopupSetValue="visitaPopup5"/>
	<adsm:textbox
		dataType="text" 
		property="dsObservacao5" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
		required="false" />			
	
		<!-- 		evento 6	 -->
	<adsm:textbox
		dataType="JTDate" 
		label="segundaProposta"
		property="dtEvento6" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDatasEvento('6');"/>	
	<adsm:lookup
			action="/vendas/manterRegistrosVisita"
			service="lms.vendas.manterPipelineClienteAction.findLookupRegistroVisita" 
			property="visita6"
			idProperty="idVisita"
			criteriaProperty="dsVisita"
			size="30" 
			maxLength="100"
			dataType="text"
			width="27%"
			serializable="true"
			afterPopupSetValue="visitaPopup6"/>
	<adsm:textbox
		dataType="text" 
		property="dsObservacao6" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
		required="false" 	/>	
	
	<adsm:section caption="fechamento"/>	
		<!-- evento 7 -->
	<adsm:textbox
		dataType="JTDate" 
		label="fechamentoNegocio"
		property="dtEvento7" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDatasEvento('7');"/>	
	<adsm:lookup
			action="/vendas/manterRegistrosVisita"
			service="lms.vendas.manterPipelineClienteAction.findLookupRegistroVisita" 
			property="visita7"
			idProperty="idVisita"
			criteriaProperty="dsVisita"
			size="30" 
			maxLength="100"
			dataType="text"
			width="27%"
			serializable="true"
			afterPopupSetValue="visitaPopup7"/>
	<adsm:textbox
		dataType="text" 
		property="dsObservacao7" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
		required="false" />	
		<!-- evento 8 -->
	<adsm:textbox
		dataType="JTDate" 
			label="dataEfetivacao"
		property="dtEvento8" 
			disabled="true"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDatasEvento('8');"/>
		<adsm:label key="espacoBranco" style="border:none;" width="27%"/>
	<adsm:textbox
		dataType="text" 
		property="dsObservacao8" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
		required="false" />
		<!-- evento 9  -->
	<adsm:textbox
		dataType="JTDate" 
			label="inicioPrevisto"
		property="dtEvento9" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDatasEvento('9');"/>
		<adsm:label key="espacoBranco" style="border:none;" width="27%"/>
	<adsm:textbox
		dataType="text" 
		property="dsObservacao9" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
		required="false" />
		
		<!-- evento 10 -->
	<adsm:textbox
		dataType="JTDate" 
			label="primeiroEmbarque"
			property="dtEvento10" 
			disabled="true"
			size="10"
			maxLength="10"
			width="16%"
			labelWidth="20%"
			serializable="true"
			required="false"
			onchange="return comparaDatasEvento('10');"/>
		<adsm:label key="espacoBranco" style="border:none;" width="27%"/>
		<adsm:textbox
			dataType="text" 
			property="dsObservacao10" 
			disabled="true"
			size="40"
			maxLength="50"
			width="30%"
			serializable="true"
			required="false"/>
		<!-- evento 11 -->
		<adsm:textbox
			dataType="JTDate" 
		label="posVenda"
			property="dtEvento11" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDatasEvento('11');"
	/>	
	<adsm:lookup
			action="/vendas/manterRegistrosVisita"
			service="lms.vendas.manterPipelineClienteAction.findLookupRegistroVisita" 
			property="visita11"
			idProperty="idVisita"
			criteriaProperty="dsVisita"
			size="30" 
			maxLength="100"
			dataType="text"
			width="27%"
			disabled="true"
			serializable="true"
			afterPopupSetValue="visitaPopup11"/>
	<adsm:textbox
		dataType="text" 
			property="dsObservacao11" 
		disabled="true"
			size="40"
		maxLength="50"
			width="30%"
		serializable="true"
		required="false" />	
		
		<adsm:section caption="cancelamento"/>
		<!-- evento 12 -->
		<adsm:textbox
			dataType="JTDate" 
			label="cancelamentoDoNegocio"
			property="dtEvento12" 
			disabled="false"
			size="10"
			maxLength="10"
			width="16%"
			labelWidth="20%"
			serializable="true"
			required="false"
			onchange="return comparaDatasEvento('12');"/>
		<adsm:label key="espacoBranco" style="border:none;" width="27%"/>
		<adsm:textbox
			dataType="text" 
			property="dsObservacao12" 
			disabled="true"
			size="40"
			maxLength="50"
			width="30%"
			serializable="true"
			required="false"/>
		
	<adsm:section caption="perdaNegocio"/>						
		<!-- campo perda -->
	<adsm:textbox
		dataType="JTDate" 
		label="dataPerda"
		property="dtPerda" 
		disabled="false"
			size="10"
			maxLength="10"
			width="16%"
		labelWidth="20%"
		serializable="true"
		required="false"
			onchange="return comparaDataPerda('12');"/>
		
		<adsm:combobox 
			label="motivo"
			serializable="true"
			labelWidth="8%"
			boxWidth="150"
			property="tpMotivoPerda"
			onchange="return tornaDtPerdaOb();"
			domain="DM_MOTIVO_PERDA"
			width="20%"/>
			
		<adsm:label key="espacoBranco" style="border:none;" width="50%"/>	
			
		<adsm:buttonBar freeLayout="true">
	   <adsm:storeButton id="storeButton" service="lms.vendas.manterPipelineClienteAction.storeEtapas" callbackProperty="afterStoreEtapas"/>
	</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function tornaDtEventoObrigatorioByObs(numero){
		if(getElementValue("dsObservacao"+numero)!= ''){
			document.getElementById("dtEvento"+numero).required = true;
		}else{
			document.getElementById("dtEvento"+numero).required = false;
		}
		return true;
	}
	
	function tornaDtEventoObrByVisita(numero){
		
		if(getElementValue("visita"+numero+".idVisita")!= ''){
			document.getElementById("dtEvento"+numero).required = true;
		}else{
			document.getElementById("dtEvento"+numero).required = false;
		}
		
	}

	function desabilitaDsVisita(){
		for(var i=1; i <=10; i++) {
			setDisabled("dsObservacao"+i, true);
			if(document.getElementById("visita"+i+".idVisita")!= null){
				setDisabled("visita"+i+".idVisita", true);
		    	setDisabled("visita"+i+".dsVisita", true);
		    }
		    if(i>=8){
		    	setDisabled("dtEvento"+i, true);
		    }	
	    }
		desabilitaEfetivacaoPrimeiroEmbarque();
	}
		
	function tornaDtPerdaOb(){
		if(getElementValue("tpMotivoPerda")!= ""){
			getElement("dtPerda").required = true;
		}else{
			getElement("dtPerda").required = false;
		}
	}

	function comparaDatasEvento(numData) {
		var dtEvento = document.getElementById("dtEvento"+numData);

		result = isDate(dtEvento.value, dtEvento.mask);

		if(result == true){
			var numComp = stringToNumber(numData);
			var numComp2 = stringToNumber(numData);

			setDisabled("dsObservacao"+numData, true);

			if(numComp!=9){
				document.getElementById("dtEvento"+numData).smallerThan = "dtAtual";
				if (!compareData(getElementValue("dtEvento"+numData), getElementValue("dtAtual"), "JTDate", "yyyy-MM-dd", "yyyy-MM-dd")){
					alertI18nMessage("LMS-10059");
					setFocus(document.getElementById("dtEvento"+numData));
					return false;
				}
			}

			numComp = numComp - 1;
			for(var i=numComp; i >= 0; i--) {
				if(i>0){
					var numString = i.toString();
					if(getElementValue("dtEvento"+numString)!= '' && isDtEmbarqueAndEfetivacao(numString)){
						if (!compareData(getElementValue("dtEvento"+numString), getElementValue("dtEvento"+numData), "JTDate", "yyyy-MM-dd", "yyyy-MM-dd")){
							alertI18nMessage("LMS-01056");
							setFocus(document.getElementById("dtEvento"+numData));
							return false;
							
						}
					}
				}else if(i==0){
					numComp2 = numComp2 + 1;
					for(var j=numComp2; j <= 10; j++) {
						var numString = j.toString();
						if(getElementValue("dtEvento"+numString)!= '' && isDtEmbarqueAndEfetivacao(numString)){
							if (compareData(getElementValue("dtEvento"+numString), getElementValue("dtEvento"+numData), "JTDate", "yyyy-MM-dd", "yyyy-MM-dd")){
								document.getElementById("dtEvento"+numData).smallerThan = "dtEvento"+numString;
								//return false;
							}
						}
					}
				}
				
			}
			validaCamposDesabilitar(numData);
			if(numData == 7){
				setFocus(document.getElementById("visita7.dsVisita"));
			}		
			return true;
		}
		return result;
	}
	
	function isDtEmbarqueAndEfetivacao(numString) {
		return numString != '8' && numString != '10';
	}
	
	function validaCamposDesabilitar(numData){
			
			if(getElementValue("dtEvento"+numData)!= ''){
				if(numData == 7){
					habilitaDtEventoByFechamento();
				}
			    if(document.getElementById("visita"+numData+".idVisita")!= null){
			    	setDisabled("visita"+numData+".idVisita", false);
					setDisabled("visita"+numData+".dsVisita", true);
				}
				setDisabled("dsObservacao"+numData, false);
				
			}else{
				 if(numData == 7)
					limpaDesabilitaDtEventoByFechamento(true);
				
				 if(document.getElementById("visita"+numData+".idVisita")!= null){
					setElementValue("visita"+numData+".idVisita",'');
					setElementValue("visita"+numData+".dsVisita",'');
					setDisabled("visita"+numData+".idVisita", true);
					setDisabled("visita"+numData+".dsVisita", true);
				}
				setElementValue("dsObservacao"+numData,'');
				setDisabled("dsObservacao"+numData, true);
			}
	}
	
	
	//limpa e desabilita as datas de evento, caso for excluida a data de fechamento
	function limpaDesabilitaDtEventoByFechamento(flag){
		for(var i=8; i <= 12; i++) {
			setElementValue("dtEvento"+i,'');
			setDisabled("dtEvento"+i, flag);	
			
			if(document.getElementById("visita"+i+".idVisita")!= null){
				setElementValue("visita"+i+".idVisita",'');
				setElementValue("visita"+i+".dsVisita",'');
				setDisabled("visita"+i+".idVisita", true);
				setDisabled("visita"+i+".dsVisita", true);
			}
			setElementValue("dsObservacao"+i,'');
			setDisabled("dsObservacao"+i, true);
		}	
			
	}
	
	//habilita as datas de evento, caso a data de fechamento 
	function habilitaDtEventoByFechamento(){
		
		setDisabled("dtEvento8", true);
		setDisabled("dtEvento9", false);
		setDisabled("dtEvento10", true);
		setDisabled("dtEvento11", false);
		setDisabled("dtEvento12", false);
		
		if(getElementValue("dtEvento9")!= ''){
			setDisabled("dsObservacao9", false);
		}else{
			setDisabled("dsObservacao9", true);
		}
		if(getElementValue("dtEvento11")!= ''){
			setDisabled("visita11.idVisita", false);
			setDisabled("visita11.dsVisita", true);
			setDisabled("dsObservacao11", false);
		}else{
			setDisabled("visita11.idVisita", true);
			setDisabled("visita11.dsVisita", true);
			setDisabled("dsObservacao11", true);
		}	
		
	}
	
	function comparaDataPerda(numData) {
		var dtPerda = document.getElementById("dtPerda");
		result = isDate(dtPerda.value, dtPerda.mask);
		if(result == true){
			if(getElementValue("dtPerda")!= ""){
				getElement("tpMotivoPerda").required = true;
			}else{
				getElement("tpMotivoPerda").required = false;
			}
				
			var numComp = stringToNumber(numData);
			numComp = numComp - 1;
			
			document.getElementById("dtPerda").smallerThan = "dtAtual";
			
			if (!compareData(getElementValue("dtPerda"), getElementValue("dtAtual"), "JTDate", "yyyy-MM-dd", "yyyy-MM-dd")){
				alertI18nMessage("LMS-10059");
				getElement("tpMotivoPerda").required = false;
				setFocus(document.getElementById("dtEvento"+numData));
				return false;
			}
			
			for(var i=numComp; i > 0; i--) {
				var numString = i.toString();
				if(getElementValue("dtEvento"+numString)!= ""){
					if (!compareData(getElementValue("dtEvento"+numString), getElementValue("dtPerda"), "JTDate", "yyyy-MM-dd", "yyyy-MM-dd")){
							alertI18nMessage("LMS-01056");
							getElement("tpMotivoPerda").required = false;
							setFocus(document.getElementById("dtPerda"));
							return false;
							
					}
				}
			}
		}
		return result;
	}
	/***** SALVAR *****/
	function afterStoreEtapas_cb(data, error, errorKey) {
		store_cb(data,error);
		
		getElementValue("tpMotivoPerda").required = false;
		getElementValue("dtPerda").required = false;
		
		if(!error){
			setElementValue("idPipelineEtapa1", data.idPipelineEtapa1);
			setElementValue("idPipelineEtapa2", data.idPipelineEtapa2);
			setElementValue("idPipelineEtapa3", data.idPipelineEtapa3);
			setElementValue("idPipelineEtapa4", data.idPipelineEtapa4);
			setElementValue("idPipelineEtapa5", data.idPipelineEtapa5);
			setElementValue("idPipelineEtapa6", data.idPipelineEtapa6);
			setElementValue("idPipelineEtapa7", data.idPipelineEtapa7);
			setElementValue("idPipelineEtapa8", data.idPipelineEtapa8);
			setElementValue("idPipelineEtapa9", data.idPipelineEtapa9);
			setElementValue("idPipelineEtapa10", data.idPipelineEtapa10);
			setElementValue("idPipelineEtapa11", data.idPipelineEtapa11);
			setElementValue("idPipelineEtapa12", data.idPipelineEtapa12);
			if(data.tpSituacao != null){
				setElementValue("tpSituacao", data.tpSituacao);
				setDisabled(document, true);
				if(data.tpSituacao == 'F'){
					habilitaCamposPosDesabilitar();
			}
			}
			for(var i=1; i <=12; i++) {
				if(getElementValue("dtEvento"+i)!= ''){
					if(document.getElementById("visita"+i+".idVisita")!= null){
						setDisabled("visita"+i+".idVisita", proximaPreenchido(i));
						setDisabled("visita"+i+".dsVisita", proximaPreenchido(i));
					}
					setDisabled("dtEvento"+i, proximaPreenchido(i));
					setDisabled("dsObservacao"+i, proximaPreenchido(i));
			   }else{
			   		if(document.getElementById("visita"+i+".idVisita")!= null){
			   			setDisabled("visita"+i+".idVisita", false);
						setDisabled("visita"+i+".dsVisita", true);
					}
			   		setDisabled("dtEvento"+i, false);
					setDisabled("dsObservacao"+i, false);
			   }
			}
			habilitaAbaPropostasTabelas(data.enableAbaPropostasTabelas == 'true');
			setFocusOnFirstFocusableField();
		  } else {
			  if(errorKey == 'LMS-01211') {
				  requestFocusOnBlanckDsField();
			  }
		  }
		desabilitaEfetivacaoPrimeiroEmbarque();
	}
			
	function requestFocusOnBlanckDsField() {
		for(var i=1; i <=12; i++) {
			if(getElementValue("dtEvento"+i) != '' && !getElementValue("dsObservacao"+i)){
				setFocus(document.getElementById("dsObservacao" + i));
		  }
		}
	}
		
	function habilitaAbaPropostasTabelas(b){
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("propostasTabelas", !b);		
	}
	
	function habilitaCamposPosDesabilitar(){
		habilitaDtEventoByFechamento();
		setDisabled("storeButton", false);
	}

	function myOnShow() {
		inicializaTela();
		return false;
	}
	
	function inicializaTela(){
			var tabGroup = getTabGroup(this.document);	
			//pipelineCliente
			if(tabGroup.getTab("cad").getElementById("idPipelineCliente").value != "")
				setElementValue("idPipelineCliente", tabGroup.getTab("cad").getElementById("idPipelineCliente").value);
						
			//filial
			setElementValue("filial.idFilial", tabGroup.getTab("cad").getElementById("filial.idFilial").value);
			setElementValue("filial.sgFilial",tabGroup.getTab("cad").getElementById("filial.sgFilial").value);
			setElementValue("filial.pessoa.nmFantasia",tabGroup.getTab("cad").getElementById("filial.pessoa.nmFantasia").value);
			
			//regional
			setElementValue("siglaDescricao", tabGroup.getTab("cad").getElementById("siglaDescricao").value);
			
			//dtAtual
			setElementValue("dtAtual", tabGroup.getTab("cad").getElementById("dtAtual").value);
			
			//funcionario
			setElementValue("usuarioByIdUsuario.idUsuario",tabGroup.getTab("cad").getElementById("usuarioByIdUsuario.idUsuario").value);
			setElementValue("usuarioByIdUsuario.nrMatricula",tabGroup.getTab("cad").getElementById("usuarioByIdUsuario.nrMatricula").value);
			setElementValue("usuarioByIdUsuario.nmUsuario",tabGroup.getTab("cad").getElementById("usuarioByIdUsuario.nmUsuario").value);
		
			//cliente
			setElementValue("cliente.idCliente", tabGroup.getTab("cad").getElementById("cliente.idCliente").value);
			setElementValue("cliente.pessoa.nrIdentificacao",tabGroup.getTab("cad").getElementById("cliente.pessoa.nrIdentificacao").value);
			setElementValue("cliente.pessoa.nmPessoa",tabGroup.getTab("cad").getElementById("cliente.pessoa.nmPessoa").value);
		
			//tipoCliente
			setElementValue("cliente.tpCliente", tabGroup.getTab("cad").getElementById("cliente.tpCliente").value);
			
			getElementValue("tpMotivoPerda").required = false;
			getElementValue("dtPerda").required = false;
				
			if(getElementValue("consultou") == ''){
				limpaTodosCampos();
				var sdo = createServiceDataObject("lms.vendas.manterPipelineClienteAction.findPipelineEtapas", "findPipelineEtapas", {idPipelineCliente:getElementValue("idPipelineCliente")});
				xmit({serviceDataObjects:[sdo]});
			}
					
			desabilitaEfetivacaoPrimeiroEmbarque();
	}
		
	function desabilitaEfetivacaoPrimeiroEmbarque(){
		setDisabled("dtEvento8", true);
		setDisabled("dsObservacao8", true);
		setDisabled("dtEvento10", true);
		setDisabled("dsObservacao10", true);
	}
	
	function limpaCampos(){
		cleanButtonScript(this.document);
		getElementValue("tpMotivoPerda").required = "false";
		getElementValue("dtPerda").required = "false";
	}
	
	function myCallBack(data){
		setElementValue("consultou", "true");
		setElementValue("tpSituacao", data.tpSituacao);
		setElementValue("tpMotivoPerda", data.tpMotivoPerda);
		
		setElementValue("dtEvento1", data.dtEvento1);
		setElementValue("dtEvento2", data.dtEvento2);
		setElementValue("dtEvento3", data.dtEvento3);
		setElementValue("dtEvento4", data.dtEvento4);
		setElementValue("dtEvento5", data.dtEvento5);
		setElementValue("dtEvento6", data.dtEvento6);
		setElementValue("dtEvento7", data.dtEvento7);
		setElementValue("dtEvento8", data.dtEvento8);
		setElementValue("dtEvento9", data.dtEvento9);
		setElementValue("dtEvento10", data.dtEvento10);
		setElementValue("dtEvento11", data.dtEvento11);
		setElementValue("dtEvento12", data.dtEvento12);
		setElementValue("dtPerda", data.dtPerda);
		
		setElementValue("idPipelineEtapa1", data.idPipelineEtapa1);
		setElementValue("idPipelineEtapa2", data.idPipelineEtapa2);
		setElementValue("idPipelineEtapa3", data.idPipelineEtapa3);
		setElementValue("idPipelineEtapa4", data.idPipelineEtapa4);
		setElementValue("idPipelineEtapa5", data.idPipelineEtapa5);
		setElementValue("idPipelineEtapa6", data.idPipelineEtapa6);
		setElementValue("idPipelineEtapa7", data.idPipelineEtapa7);
		setElementValue("idPipelineEtapa8", data.idPipelineEtapa8);
		setElementValue("idPipelineEtapa9", data.idPipelineEtapa9);
		setElementValue("idPipelineEtapa10", data.idPipelineEtapa10);
		setElementValue("idPipelineEtapa11", data.idPipelineEtapa11);
		setElementValue("idPipelineEtapa12", data.idPipelineEtapa12);
		
		setElementValue("dsObservacao1", data.dsObservacao1);
		setElementValue("dsObservacao2", data.dsObservacao2);
		setElementValue("dsObservacao3", data.dsObservacao3);
		setElementValue("dsObservacao4", data.dsObservacao4);
		setElementValue("dsObservacao5", data.dsObservacao5);
		setElementValue("dsObservacao6", data.dsObservacao6);
		setElementValue("dsObservacao7", data.dsObservacao7);
		setElementValue("dsObservacao8", data.dsObservacao8);
		setElementValue("dsObservacao9", data.dsObservacao9);
		setElementValue("dsObservacao10", data.dsObservacao10);
		setElementValue("dsObservacao11", data.dsObservacao11);
		setElementValue("dsObservacao12", data.dsObservacao12);
		
		if(data.visita3 != undefined){
			setElementValue("visita3.idVisita", data.visita3.idVisita);
			setElementValue("visita3.dsVisita", data.visita3.dsVisita);
		}
		
		if(data.visita4 != undefined){
			setElementValue("visita4.idVisita", data.visita4.idVisita);
			setElementValue("visita4.dsVisita", data.visita4.dsVisita);
		}
		
		if(data.visita5 != undefined){
			setElementValue("visita5.idVisita", data.visita5.idVisita);
			setElementValue("visita5.dsVisita", data.visita5.dsVisita);
		}
		
		if(data.visita6 != undefined){
			setElementValue("visita6.idVisita", data.visita6.idVisita);
			setElementValue("visita6.dsVisita", data.visita6.dsVisita);
		}
		if(data.visita7 != undefined){
			setElementValue("visita7.idVisita", data.visita7.idVisita);
			setElementValue("visita7.dsVisita", data.visita7.dsVisita);
		}
		
		if(data.visita11 != undefined){
			setElementValue("visita11.idVisita", data.visita10.idVisita);
			setElementValue("visita11.dsVisita", data.visita10.dsVisita);
		}
	}
	
	function limpaTodosCampos(){
		setElementValue("tpSituacao", "");
		setElementValue("tpMotivoPerda", "");
		setElementValue("dtEvento1", "");
		setElementValue("dtEvento2", "");
		setElementValue("dtEvento3", "");
		setElementValue("dtEvento4", "");
		setElementValue("dtEvento5", "");
		setElementValue("dtEvento6", "");
		setElementValue("dtEvento7", "");
		setElementValue("dtEvento8", "");
		setElementValue("dtEvento9", "");
		setElementValue("dtEvento10", "");
		setElementValue("dtEvento11", "");
		setElementValue("dtEvento12", "");
		setElementValue("dtPerda", "");
		
		setElementValue("idPipelineEtapa1", "");
		setElementValue("idPipelineEtapa2", "");
		setElementValue("idPipelineEtapa3", "");
		setElementValue("idPipelineEtapa4", "");
		setElementValue("idPipelineEtapa5", "");
		setElementValue("idPipelineEtapa6", "");
		setElementValue("idPipelineEtapa7", "");
		setElementValue("idPipelineEtapa8", "");
		setElementValue("idPipelineEtapa9", "");
		setElementValue("idPipelineEtapa10","");
		setElementValue("idPipelineEtapa11","");
		setElementValue("idPipelineEtapa12","");
		
		setElementValue("dsObservacao1", "");
		setElementValue("dsObservacao2", "");
		setElementValue("dsObservacao3", "");
		setElementValue("dsObservacao4", "");
		setElementValue("dsObservacao5", "");
		setElementValue("dsObservacao6", "");
		setElementValue("dsObservacao7", "");
		setElementValue("dsObservacao8", "");
		setElementValue("dsObservacao9", "");
		setElementValue("dsObservacao10", "");
		setElementValue("dsObservacao11", "");
		setElementValue("dsObservacao12", "");
		
		setElementValue("visita3.idVisita", "");
		setElementValue("visita3.dsVisita", "");
		setElementValue("visita4.idVisita", "");
		setElementValue("visita4.dsVisita", "");
		setElementValue("visita5.idVisita","");
		setElementValue("visita5.dsVisita", "");
		setElementValue("visita6.idVisita", "");
		setElementValue("visita6.dsVisita", "");
		setElementValue("visita7.idVisita", "");
		setElementValue("visita7.dsVisita", "");
		setElementValue("visita11.idVisita", "");
		setElementValue("visita11.dsVisita", "");
	}
	
	function findPipelineEtapas_cb(data,error) {
		
		myCallBack(data);
				
		if(getElementValue("tpSituacao") == 'P' || getElementValue("tpSituacao") == 'F' || getElementValue("tpSituacao") == 'C'){	
			setDisabled(document, true);
			if(getElementValue("tpSituacao") == 'F')
				habilitaCamposPosDesabilitar();
				
		}else{
			setDisabled(document, false);
			
			desabilitaDsVisita();
			
			setDisabled("cliente.idCliente", true);
			setDisabled("cliente.tpCliente", true);
			setDisabled("filial.idFilial", true);
			setDisabled("filial.sgFilial", true);
			setDisabled("filial.pessoa.nmFantasia", true);
			setDisabled("usuarioByIdUsuario.idUsuario", true);
			setDisabled("siglaDescricao", true);
			setDisabled("cliente.pessoa.nmPessoa", true);
			setDisabled("usuarioByIdUsuario.nmUsuario", true);
			
			for(var i=1; i<=12; i++) {
				if(getElementValue("dtEvento"+i)!= ''){
					if(document.getElementById("visita"+i+".idVisita")!= null){
							setDisabled("visita"+i+".idVisita", proximaPreenchido(i));
							setDisabled("visita"+i+".dsVisita", proximaPreenchido(i));
					}
					setDisabled("dtEvento"+i, proximaPreenchido(i));
					setDisabled("dsObservacao"+i, proximaPreenchido(i));
			   }else{
			   		if(document.getElementById("visita"+i+".idVisita")!= null){
			   			setDisabled("visita"+i+".idVisita", false);
						setDisabled("visita"+i+".dsVisita", true);
					}
			   		setDisabled("dtEvento"+i, false);
					setDisabled("dsObservacao"+i, false);
			   }
			}
			
		}
		document.getElementById("tpMotivoPerda").required = false;
		document.getElementById("dtPerda").required = false;
		
		desabilitaEfetivacaoPrimeiroEmbarque();
	}
	
	function proximaPreenchido(index){
		if(index == 12) return false;
		var x = index + 1;
		var el = getElementValue("dtEvento"+x);
		if(el) {
			return true;
		} else {
			return false;
		}
	}
	
	
	function visitaPopup3(data){
		if (data != undefined){
			var dsVisita = data.filial.sgFilial +" "+ data.dtVisita + " "+  data.usuarioByIdUsuario.nmUsuario;
			setElementValue("visita3.idVisita", data.idVisita);
			setElementValue("visita3.dsVisita", dsVisita.toString());
		}
		
		return true;
	}
	
	
	function visitaPopup4(data){
	    if (data != undefined){
			var dsVisita = data.filial.sgFilial +" "+ data.dtVisita + " "+  data.usuarioByIdUsuario.nmUsuario;
			setElementValue("visita4.idVisita", data.idVisita);
			setElementValue("visita4.dsVisita", dsVisita);
		}
		return true;
	}
	function visitaPopup5(data){
		if (data != undefined){
			var dsVisita = data.filial.sgFilial +" "+ data.dtVisita + " "+  data.usuarioByIdUsuario.nmUsuario;
			setElementValue("visita5.idVisita", data.idVisita);
			setElementValue("visita5.dsVisita", dsVisita);
		}
		
		return true;
	}
	
	function visitaPopup6(data){
		if (data != undefined){
			var dsVisita = data.filial.sgFilial +" "+ data.dtVisita + " "+  data.usuarioByIdUsuario.nmUsuario;
			setElementValue("visita6.idVisita", data.idVisita);
			setElementValue("visita6.dsVisita", dsVisita);
		}
		
		return true;
	}
	function visitaPopup7(data){
		if (data != undefined){
			var dsVisita = data.filial.sgFilial +" "+ data.dtVisita + " "+  data.usuarioByIdUsuario.nmUsuario;
			setElementValue("visita7.idVisita", data.idVisita);
			setElementValue("visita7.dsVisita", dsVisita);
		}
		
		return true;
	}
	function visitaPopup11(data){
		if (data != undefined){
			var dsVisita = data.filial.sgFilial +" "+ data.dtVisita + " "+  data.usuarioByIdUsuario.nmUsuario;
			setElementValue("visita11.idVisita", data.idVisita);
			setElementValue("visita11.dsVisita", dsVisita);
		}
		
		return true;
	}
</script>