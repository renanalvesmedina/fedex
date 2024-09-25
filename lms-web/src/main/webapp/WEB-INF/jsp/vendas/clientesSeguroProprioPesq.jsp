<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.clientesSeguroProprioAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/vendas/clientesSeguroProprio">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-30050"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="tpAcesso" value="F"/>

	   	<adsm:combobox label="regional" 
	   	               property="regional.idRegional" 
	   	               optionLabelProperty="siglaDescricao" 
	   	               optionProperty="idRegional"
	   	               service="lms.vendas.clientesSeguroProprioAction.findComboRegional" 
	   	               boxWidth="250"
	   	               onchange="return myOnChangeComboRegional(this)"
	   	               onDataLoadCallBack="myRetornoComboRegional">
			<adsm:propertyMapping relatedProperty="regional.siglaDescricao" modelProperty="siglaDescricao"/>
	   	</adsm:combobox>
	   	<adsm:hidden property="regional.siglaDescricao"/>
	   	
        <adsm:lookup label="filial" 
                     property="filial" 
                     service="lms.vendas.clientesSeguroProprioAction.findLookupBySgFilial" 
                     action="/municipios/manterFiliais" 
                     idProperty="idFilial" 
                     criteriaProperty="sgFilial" 
                     dataType="text" 
                     size="3" 
                     labelWidth="15%" 
                     width="8%" 
                     maxLength="3"
                     onDataLoadCallBack="myRetornoLookupFilial"
                     afterPopupSetValue="setaSiglaNomeFilial">
            <adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" inlineQuery="true"/>         
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:propertyMapping relatedProperty="sgFilial" modelProperty="sgFilial" />
            <adsm:textbox dataType="text" width="27%" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
        <adsm:hidden property="sgFilial"/>
        
		<adsm:textbox dataType="JTDate" property="dtReferencia" label="dataReferencia" size="10" maxLength="10" width="35%" picker="true" required="true"/>

		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   required="true"
    				   defaultValue="pdf"
    				   width="35%"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:button caption="visualizar" id="btnVisualizar" buttonType="reportButton" disabled="false" onclick="verificacoes(this)"/>
			<adsm:button caption="limpar" id="btnLimpar" buttonType="resetButton" onclick="myLimpar(this);"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function myLimpar(elem){
		cleanButtonScript(elem.document);
		buscaDataAtual(false,true);		
		buscaFilialRegional(true,false);
	}
	
	function myOnPageLoadCallBack_cb(data,error){
		onPageLoad_cb(data,error);
		buscaDataAtual(false,true);		
		buscaFilialRegional(true,false);
	}
	
	function buscaDataAtual(realizaXmit, novosDados){
	
		if( novosDados == true ){	
			_serviceDataObjects = new Array();
		}
         
        var dados = new Array();         
        
        addServiceDataObject(createServiceDataObject("lms.vendas.clientesSeguroProprioAction.findDataAtual",
                                                      "setaDataAtual",
                                                      dados));

        if( realizaXmit == true ){
	        xmit(false);
	    }
        
	}
	
	function setaDataAtual_cb(data,error){
		if( error != undefined ){
			alert(error);			
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		setElementValue('dtReferencia',setFormat('dtReferencia',data._value));
		setFocusOnFirstFocusableField(document);
	}
	
	function buscaFilialRegional(realizaXmit, novosDados){
		if( novosDados == true ){	
			_serviceDataObjects = new Array();
		}
         
        var dados = new Array();         
         
        addServiceDataObject(createServiceDataObject("lms.vendas.clientesSeguroProprioAction.findFilialAndRegionalUsuario",
			                                         "setaFilialAndRegionalUsuario",
			                                         dados));
        if( realizaXmit == true ){
	        xmit(false);
	    }
	}
	
	function setaFilialAndRegionalUsuario_cb(data,error){
		if( error != undefined ){
			alert(error);			
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		fillFormWithFormBeanData(0, data);
		setaDescricaoRegional(getElement('regional.idRegional'));
		setaSiglaNomeFilial();
		setFocusOnFirstFocusableField(document);
	}
	
	function verificacoes(elem){
	
		var filial = getElementValue('filial.idFilial');
		var regional = getElementValue('regional.idRegional');
		
		if( filial == '' && regional == '' ){
			alert(i18NLabel.getLabel('LMS-30050'));
			setFocusOnFirstFocusableField(document);
			return false;
		}
	
		reportButtonScript('lms.vendas.clientesSeguroProprioAction', 'openPdf', elem.form);
	}	
	
	function setaDescricaoRegional(elem){
		if( elem.selectedIndex != 0 ){
			setElementValue('regional.siglaDescricao',elem.options[elem.selectedIndex].text);
		}
	}
	
	function myOnChangeComboRegional(elem){
		var retorno = comboboxChange({e:elem});
		setaDescricaoRegional(elem);
		return retorno;
	}
	
	function myRetornoComboRegional_cb(data, error){
		regional_idRegional_cb(data,error);
		setaDescricaoRegional(getElement('regional.idRegional'));
	}
	
	function myRetornoLookupFilial_cb(data,error){
		var retorno = filial_sgFilial_exactMatch_cb(data);
		setaSiglaNomeFilial();
		return retorno;
	}
	
	function setaSiglaNomeFilial(){
		if( getElementValue('filial.idFilial') != '' ){
			setElementValue('sgFilial',getElementValue('filial.sgFilial') + " - " + getElementValue('filial.pessoa.nmFantasia'));
		} else {
			resetValue('sgFilial');
		}
	}
	
</script>