<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterTaxasSUFRAMAAction">
	<adsm:form action="/tributos/manterTaxasSUFRAMA" height="360" idProperty="idTaxaSuframa">	

		<adsm:range label="valorMercadoria" required="true" width="42%" labelWidth="14%">
			<adsm:textbox property="vlMercadoriaInicial" dataType="currency" size="23" minValue="0"/>
			<adsm:textbox property="vlMercadoriaFinal" dataType="currency" size="23"/>
		</adsm:range>

		<adsm:combobox label="indicadorCalculo" property="tpIndicadorCalculo" domain="DM_INDICADOR_CALCULO_SUFRAMA" required="true" width="28%"/>

		<adsm:textbox label="valorLiquido" property="vlLiquido" dataType="currency" required="true" width="42%" size="23" labelWidth="14%" minValue="0"/>

		<adsm:range label="vigencia" width="28%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>

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


		setDisabled("vlMercadoriaInicial",val);
		setDisabled("vlMercadoriaFinal",val);
		setDisabled("tpIndicadorCalculo",val);
		setDisabled("vlLiquido",val);
		setDisabled("dtVigenciaInicial",val);

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
