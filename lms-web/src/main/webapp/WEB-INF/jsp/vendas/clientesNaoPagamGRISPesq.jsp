<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.clientesNaoPagamGRISAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/vendas/clientesNaoPagamGRIS">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-30050"/>
		</adsm:i18nLabels>
		
	   	<adsm:hidden property="tpAcesso" value="F"/>

	   	<adsm:combobox label="regional" 
	   	               labelWidth="15%" 
	   	               width="35%" 
	   	               property="regional.idRegional" 
	   	               optionLabelProperty="siglaDescricao" 
	   	               optionProperty="idRegional"
	   	               onchange="return myOnChangeComboRegional(this)"
		   			   service="lms.vendas.clientesNaoPagamGRISAction.findComboRegional" 
		   			   boxWidth="250">
			<adsm:propertyMapping relatedProperty="regional.siglaDescricao" modelProperty="siglaDescricao"/>
	   	</adsm:combobox>
	   	<adsm:hidden property="regional.siglaDescricao"/>

		<adsm:lookup label="filial" 
					 property="filial" 		
					 service="lms.vendas.clientesNaoPagamGRISAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 dataType="text" 
					 size="3" 
					 labelWidth="10%" 
					 width="8%" 
					 maxLength="3"
					 onDataLoadCallBack="myRetornoLookupFilial"
                     afterPopupSetValue="setaSiglaNomeFilial">
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" inlineQuery="true"/>					 
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:propertyMapping formProperty="sgFilial" modelProperty="sgFilial" />
            <adsm:textbox dataType="text" width="27%" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="true"/>
        </adsm:lookup>
        <adsm:hidden property="sgFilial"/>

		<adsm:lookup property="usuario" 
	   				idProperty="idUsuario" 
	   				criteriaProperty="nrMatricula" 
                    dataType="text" 
                    label="promotor" 
                    size="16" 
	   				maxLength="16" 
                    labelWidth="15%" 
                    width="17%" 
                    service="lms.vendas.manterPromotoresClienteAction.findLookupFuncionarioPromotor" 
					action="/configuracoes/consultarFuncionarios"
					cmd="promotor">
               <adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" inlineQuery="true"/>
               <adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="16" labelWidth="15%" width="58%" disabled="true" serializable="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   required="true"
    				   labelWidth="15%"
    				   width="80%"
    				   defaultValue="pdf"
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
		buscaFilialRegional(true,true);
	}
	
	function myOnPageLoadCallBack_cb(data,error){
		onPageLoad_cb(data,error);
		buscaFilialRegional(true,true);
	}
	
	function buscaFilialRegional(realizaXmit, novosDados){
		if( novosDados == true ){	
			_serviceDataObjects = new Array();
		}
         
        var dados = new Array();         
         
        addServiceDataObject(createServiceDataObject("lms.vendas.clientesNaoPagamGRISAction.findFilialAndRegionalUsuario",
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
	
	function myOnChangeComboRegional(elem){
		var retorno = comboboxChange({e:elem});
		setaDescricaoRegional(elem);
		return retorno;
	}
	
	function setaDescricaoRegional(elem){
		if( elem.selectedIndex != 0 ){
			setElementValue('regional.siglaDescricao',elem.options[elem.selectedIndex].text);
		}
	}

	function verificacoes(elem){
	
		var filial = getElementValue('filial.idFilial');
		var regional = getElementValue('regional.idRegional');
		
		if( filial == '' && regional == '' ){
			alert(i18NLabel.getLabel('LMS-30050'));
			setFocusOnFirstFocusableField(document);
			return false;
		}
	
		reportButtonScript('lms.vendas.clientesNaoPagamGRISAction', 'openPdf', elem.form);
	}		
</script>