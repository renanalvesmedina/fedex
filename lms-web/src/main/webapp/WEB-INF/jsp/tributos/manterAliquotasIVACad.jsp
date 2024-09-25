<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterAliquotasIVAAction">
	<adsm:form 
		action="/tributos/manterAliquotasIVA" 
		idProperty="idAliquotaIva">
		
		<adsm:lookup
			service="lms.tributos.manterAliquotasIVAAction.findLookupPais" 
			action="/municipios/manterPaises" 
			property="pais" 
			idProperty="idPais" 
			criteriaProperty="nmPais" 
			label="pais"
			minLengthForAutoPopUpSearch="3" 
			exactMatch="false"
			dataType="text" 
			size="30" 
			maxLength="60"
			required="true"
		>
			<adsm:propertyMapping 
				criteriaProperty="activeValue"
				modelProperty="tpSituacao"
			/>
		</adsm:lookup>
		
		<adsm:hidden property="activeValue" serializable="true" value="A"/>

		<adsm:range 
			label="vigencia" 
		>
	        <adsm:textbox 
	        	dataType="JTDate" 
	        	property="dtVigenciaInicial"
	        	smallerThan="dtVigenciaFinal"
				required="true"	        	
	        />
	    	<adsm:textbox 
	    		biggerThan="dtVigenciaInicial"
	    		dataType="JTDate" 
	    		property="dtVigenciaFinal"
	    	/>
        </adsm:range>

		<adsm:textbox 
			required="true"
			size="6" 
			dataType="percent" 
			property="pcAliquota" 
			label="percentualAliquota"
			minValue="0"
			maxValue="100"
		/>

		<adsm:buttonBar>
			<adsm:storeButton />
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
		var eventObj = {name:"newButton_click"};
		newButtonScript(document, true, eventObj);
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
		}


		setDisabled("pais.nmPais",val);
		setDisabled("pais.idPais",val);
		setDisabled("dtVigenciaInicial",val);
		setDisabled("pcAliquota",val);

		setFocusOnFirstFocusableField();				
	}	
	
	/**
		Chamado ao iniciar a tela
	*/
	function initWindow(eventObj){
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			limpar();
		}
	
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton"  ){
			desabilitaTodosCampos();
		}
		setDisabled('btnLimpar', false);
		
		
		if(eventObj.name == "storeButton"){
			setFocus('btnLimpar',true,true);
		} else {
			setFocusOnFirstFocusableField();
		}
		
	}
</script>