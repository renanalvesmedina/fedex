<script type="text/javascript">
<!--
	function initWindow(eventObj){
		//alert(eventObj.name);
		if (eventObj.name== 'tab_click' || eventObj.name== 'newButton_click'){
			setElementValue("pcAliquotaInterna", "0,00");
			setElementValue("pcEmbuteInterno", "0,00");
			setElementValue("pcAliquotaInterestadual", "0,00");
			setElementValue("pcEmbuteInterestadual", "0,00");
			setElementValue("pcAliquotaDestNC", "0,00");
			setElementValue("pcEmbuteDestNC", "0,00");
		}
	}

//-->
</script>

<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
	service="lms.tributos.manterAliquotasICMSAereoAction"
>
	<adsm:form 
		action="/tributos/manterAliquotasICMSAereo" 
		idProperty="idAliquotaIcmsAereo"
	>
		<adsm:combobox
			service="lms.tributos.manterAliquotasICMSAereoAction.findUnidadeFederativa" 
			property="unidadeFederativa.idUnidadeFederativa"
			optionProperty="idUnidadeFederativa"
			optionLabelProperty="siglaDescricao"
			label="ufOrigem"
			boxWidth="120"
			required="true"
			onlyActiveValues="true"
		/>

		<adsm:textbox 
			dataType="JTDate" 
			property="dtInicioVigencia" 
			label="vigenciaInicial" 
			required="true" 
			labelWidth="17%" 
			width="33%"
		/>

		<adsm:section caption="informacoesContribuinte"/>
		
		<adsm:textbox 
			dataType="percent" 
			property="pcAliquotaInterna" 
			label="percentualAliquota" 
			size="6"
			minValue="0"
			maxValue="100"
			required="true"
		/>
		
		<adsm:textbox 
			dataType="percent" 
			property="pcEmbuteInterno" 
			label="percentualEmbutimento" 
			size="6" 
			labelWidth="17%" 
			width="33%"
			minValue="0"
			maxValue="100"
			required="true"
		/>
		
        <adsm:textarea 
	        property="obInterno" 
	        label="observacao"
	        required="true" 
	        width="85%" 
	        columns="84" 
	        rows="3" 
	        maxLength="500" 
        />

		<adsm:section caption="informacoesFreteInterestadual"/>
		
		<adsm:textbox 
			dataType="percent"
			property="pcAliquotaInterestadual" 
			label="percentualAliquota" 
			required="true"
			size="6" 
			minValue="0"
			maxValue="100"
			
		/>
		
		<adsm:textbox 
			dataType="percent"
			property="pcEmbuteInterestadual" 
			label="percentualEmbutimento" 
			required="true"
			size="6" 
			labelWidth="17%" 
			width="33%"
			minValue="0"
			maxValue="100"
			
		/>
        
        <adsm:textarea 
	        property="obInterestadual" 
	        label="observacao"
	        required="true" 
	        width="85%" 
	        columns="84" 
	        rows="3" 
	        maxLength="500" 
        />
        
   		<adsm:section caption="informacoesNaoContribuinte"/>
   		
   		<adsm:textbox 
			dataType="percent"
			property="pcAliquotaDestNC" 
			label="percentualAliquota" 
			required="true"
			size="6" 
			minValue="0"
			maxValue="100"
			
		/>
		
		<adsm:textbox 
			dataType="percent"
			property="pcEmbuteDestNC" 
			label="percentualEmbutimento" 
			required="true"
			size="6" 
			labelWidth="17%" 
			width="33%"
			minValue="0"
			maxValue="100"
			
		/>
        
        <adsm:textarea 
	        property="obDestNC" 
	        label="observacao"
	        required="true" 
	        width="85%" 
	        columns="84" 
	        rows="3" 
	        maxLength="500" 
        />

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>		
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(){
		newButtonScript();
		desabilitaTodosCampos(false);
		
	}
	
	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/	
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}else if ( val == false) {
			setElementValue("pcAliquotaInterna","0,00");
			setElementValue("pcEmbuteInterno","0,00");
			setElementValue("pcAliquotaInterestadual","0,00");
			setElementValue("pcEmbuteInterestadual","0,00");
			setElementValue("pcAliquotaDestNC","0,00");
			setElementValue("pcEmbuteDestNC","0,00");		
		}


		setDisabled("unidadeFederativa.idUnidadeFederativa",val);
		setDisabled("dtInicioVigencia",val);
		
		setDisabled("pcAliquotaInterna",val);
		setDisabled("pcEmbuteInterno",val);
		setDisabled("obInterno",val);		
		
		setDisabled("pcAliquotaInterestadual",val);		
		setDisabled("pcEmbuteInterestadual",val);		
		setDisabled("obInterestadual",val);
				
		setDisabled("pcAliquotaDestNC",val);		
		setDisabled("pcEmbuteDestNC",val);		
		setDisabled("obDestNC",val);	

		setFocusOnFirstFocusableField();				
		
	}	
	
	/**
		Chamado ao iniciar a tela
	*/
	function initWindow(eventObj){
			
		setDisabled('btnLimpar', false);
	
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			limpar();
		}
	
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton"  ){
			desabilitaTodosCampos();
			//todos os campos ficam desabilitados entao o foco vai para limpar
			setFocus(document.getElementById('btnLimpar'),true,true);
		}
		
	}
</script>