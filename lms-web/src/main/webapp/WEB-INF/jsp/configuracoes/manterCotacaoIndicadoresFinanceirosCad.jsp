<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.configuracoes.cotacaoIndicadorFinanceiroService">
	<adsm:form action="/configuracoes/manterCotacaoIndicadoresFinanceiros" idProperty="idCotacaoIndFinanceiro">
	
		<adsm:lookup service="lms.municipios.paisService.findLookup" 
					 action="/municipios/manterPaises"
					 property="indicadorFinanceiro.pais" 
					 label="pais"
					 idProperty="idPais"
					 criteriaProperty="nmPais"					 
					 dataType="text" 
					 maxLength="60"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 size="30"
					 onDataLoadCallBack="dataLoadCallBackPais"
					 onchange="return onChangePais()"
					 afterPopupSetValue="myAfterPopupSetValue"
					 required="true">						 
		</adsm:lookup>
		
		<adsm:hidden property="idIndicadorFinanceiroTmp" serializable="false"/>
		<adsm:hidden property="tpSituacaoAtivo" value="A"/>		
		
		<adsm:combobox property="indicadorFinanceiro.idIndicadorFinanceiro" 
		               label="indicadorFinanceiro" 
					   service="lms.configuracoes.indicadorFinanceiroService.findIndicadoresFinanceirosByPais" 
					   boxWidth="200"
					   optionLabelProperty="nmIndicadorFinanceiro" 
					   optionProperty="idIndicadorFinanceiro" 
					   labelWidth="17%" 
					   width="33%" 
					   required="true" 
					   autoLoad="false"					   
					   onDataLoadCallBack="setaIndicadorFinanceiro">
			<adsm:propertyMapping criteriaProperty="indicadorFinanceiro.pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="idIndicadorFinanceiroTmp" modelProperty="idIndicadorFinanceiro"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacao"/>
		</adsm:combobox>	   

		<adsm:textbox dataType="JTDate" property="dtCotacaoIndFinanceiro" label="data" required="true"/>		
		<adsm:textbox dataType="decimal" property="vlCotacaoIndFinanceiro" label="valor" required="true" minValue="0.0001" 
					  size="18" mask="##,###,###,###,##0.0000" labelWidth="17%" width="33%"/>
		
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
		newButtonScript(document,false,'newButton_click');
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


		setDisabled("indicadorFinanceiro.pais.nmPais",val);
		setDisabled("indicadorFinanceiro.pais.idPais",val);
		setDisabled("indicadorFinanceiro.idIndicadorFinanceiro",val);
		setDisabled("dtCotacaoIndFinanceiro",val);
		setDisabled("vlCotacaoIndFinanceiro",val);

		setFocusOnFirstFocusableField();				
	}	
	


	/**
	* Função utilizada para setar o indicador Financeiro correto quando esta 
	* tela é chamada pela tela de manter indicadores financeiros.
	* Não foi utilizada o mesmo método da tela de list, pois acarretava num erro de focus !
	*/
	function initWindow(eventObj) {	

		setDisabled('btnLimpar', false);

		if( eventObj.name == "tab_click" || eventObj.name == "gridRow_click"){	
							
			if(getElementValue("idIndicadorFinanceiroTmp") != null && getElementValue("idIndicadorFinanceiroTmp") != "" ){
				indicadorFinanceiro_pais_nmPaisOnChangeHandler();
				buscaIndicadoresFinanceiros();
			}
			
			notifyElementListeners({e:document.getElementById("indicadorFinanceiro.pais.idPais")});			
		}	
		
		//controle de habilitar e desabilitar os campos
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

	function myOnPageLoad_cb(data,erro){
		onPageLoad_cb(data,erro);
		indicadorFinanceiro_pais_nmPaisOnChangeHandler();
		if(getElementValue("idIndicadorFinanceiroTmp") != null && getElementValue("idIndicadorFinanceiroTmp") != "" ){
			setDisabled("indicadorFinanceiro.idIndicadorFinanceiro", true);
			document.getElementById("indicadorFinanceiro.idIndicadorFinanceiro").masterLink = "true";
		}
	}
	
	function setaIndicadorFinanceiro_cb(data){	

		indicadorFinanceiro_idIndicadorFinanceiro_cb(data);				
		
		if(getElementValue("idIndicadorFinanceiroTmp") != null && getElementValue("idIndicadorFinanceiroTmp") != "" ){
			setElementValue("indicadorFinanceiro.idIndicadorFinanceiro", getElementValue("idIndicadorFinanceiroTmp"));
			setDisabled("indicadorFinanceiro.idIndicadorFinanceiro", true);
			document.getElementById("indicadorFinanceiro.idIndicadorFinanceiro").masterLink = "true";			
		}		

	}
	
	function buscaIndicadoresFinanceiros(){
		var idPais = getElementValue("indicadorFinanceiro.pais.idPais");
		var tpSituacao = getElementValue('tpSituacaoAtivo');
		var idIndicadorFinanceiroTmp = getElementValue('idIndicadorFinanceiroTmp');
         
        var dados = new Array();         
        setNestedBeanPropertyValue(dados, "pais.idPais", idPais);
        setNestedBeanPropertyValue(dados, "tpSituacao", tpSituacao);
        setNestedBeanPropertyValue(dados, "idIndicadorFinanceiro", idIndicadorFinanceiroTmp);        
         
        var sdo = createServiceDataObject("lms.configuracoes.indicadorFinanceiroService.findIndicadoresFinanceirosByPais",
                                          "setaIndicadorFinanceiro",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
	}	
	
	function dataLoadCallBackPais_cb(data, error){
	
		var retorno = indicadorFinanceiro_pais_nmPais_exactMatch_cb(data)
	
		if( getElementValue('indicadorFinanceiro.pais.idPais') != '' ){
			buscaIndicadoresFinanceiros();
		}
		
		return retorno;
	}	
	
	function onChangePais(){
		var retorno = indicadorFinanceiro_pais_nmPaisOnChangeHandler();
		if( getElementValue('indicadorFinanceiro.pais.idPais') == '' ){
			resetValue('indicadorFinanceiro.idIndicadorFinanceiro');
		}
		return retorno;
	}
	
	function myAfterPopupSetValue(data, error){
		if( getElementValue('indicadorFinanceiro.pais.idPais') != '' ){
			buscaIndicadoresFinanceiros();
		}		
	}
	
</script>
