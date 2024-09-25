<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.cotacaoIndicadorFinanceiroService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterCotacaoIndicadoresFinanceiros">
	
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
					 onDataLoadCallBack="dataLoadCallBackPais"
					 onchange="return onChangePais()"
				     afterPopupSetValue="myAfterPopupSetValue"
					 size="30"/>
					 
		<adsm:hidden property="idIndicadorFinanceiroTmp" serializable="false"/>
					 
		<adsm:combobox property="indicadorFinanceiro.idIndicadorFinanceiro" label="indicadorFinanceiro" boxWidth="200"
					   service="lms.configuracoes.indicadorFinanceiroService.find"
					   optionLabelProperty="nmIndicadorFinanceiro" optionProperty="idIndicadorFinanceiro" autoLoad="false"					   
					   labelWidth="17%" width="33%" onDataLoadCallBack="setaIndicadorFinanceiro">
			<adsm:propertyMapping criteriaProperty="indicadorFinanceiro.pais.idPais" modelProperty="pais.idPais"/>			   
		</adsm:combobox>					   

		<adsm:range label="data">
			<adsm:textbox dataType="JTDate" property="dtCotacaoIndFinanceiroInicial"/>
			<adsm:textbox dataType="JTDate" property="dtCotacaoIndFinanceiroFinal"/> 
		</adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="cotacaoIndicadorFinanceiro"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid gridHeight="200" idProperty="idCotacaoIndFinanceiro" property="cotacaoIndicadorFinanceiro" defaultOrder="indicadorFinanceiro_pais_.nmPais, indicadorFinanceiro_.nmIndicadorFinanceiro, dtCotacaoIndFinanceiro" rows="13">
		<adsm:gridColumn width="40%" title="pais" property="indicadorFinanceiro.pais.nmPais"/>
		<adsm:gridColumn width="20%" title="indicadorFinanceiro" property="indicadorFinanceiro.nmIndicadorFinanceiro"/>
		<adsm:gridColumn width="20%" title="data" property="dtCotacaoIndFinanceiro" align="center" dataType="JTDate"/>
		<adsm:gridColumn width="25%" title="valor" property="vlCotacaoIndFinanceiro" align="right" dataType="currency" mask="##,###,###,###,##0.0000"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	/**
	* Função utilizada para ativar o change handler da lookup de pais
	* e com isso atualizar os dados da combobox de indicadores financeiros que é relacionanda a esta lookup
	*/	
	function myOnPageLoad_cb(data,erro){
	
		onPageLoad_cb(data,erro);
		
		indicadorFinanceiro_pais_nmPaisOnChangeHandler();
		
		if( getElementValue('indicadorFinanceiro.pais.idPais') != '' ){
			buscaIndicadoresFinanceiros();
		}
		
		notifyElementListeners({e:document.getElementById("indicadorFinanceiro.pais.idPais")});
	}
	
	/**
	* Função utilizada para setar o indicador financeiro correto (informacação vinda da tela de manter indicadores financeiros) 
	* após carregar os dados padrões da combobox.
	*/
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
		var idIndicadorFinanceiroTmp = getElementValue('idIndicadorFinanceiroTmp');
         
        var dados = new Array();         
        setNestedBeanPropertyValue(dados, "pais.idPais", idPais);
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