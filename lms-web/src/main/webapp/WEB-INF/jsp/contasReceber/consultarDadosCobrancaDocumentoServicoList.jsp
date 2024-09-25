<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:i18nLabels>
		<adsm:include key="LMS-36236"/>
	</adsm:i18nLabels>
	
	<adsm:form action="/contasReceber/consultarDadosCobrancaDocumentoServico">

		<adsm:hidden property="tpDocumentoHidden"/>
		<adsm:hidden property="doctoServico.idDoctoServicoHidden"/>
		
		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" 
					   width="80%"
					   labelWidth="20%" 
					   service="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="return onChangeTpDocumentoServico();"> 

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction.findLookupFilial"
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3" 
						 popupLabel="pesquisarFilial"
						 picker="false" 
						 exactMatch="true"
						 onDataLoadCallBack="filialOnChange"						 
						 onchange="return onChangeFilial();">										 
	            <adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
	            <adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia"/>
            </adsm:lookup>						 
			
			<adsm:lookup dataType="integer"
						 property="doctoServico"
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico"
						 service=""
						 action=""
						 popupLabel="pesquisarDocumentoServico"
						 size="10" 
						 maxLength="8" 
						 serializable="true" 
						 disabled="true" 
						 mask="00000000">
			</adsm:lookup>			 
		</adsm:combobox>
		
		<adsm:textbox 
					 dataType="JTDate" 
					 property="dataEmissao" 
					 label="emissao" 
					 labelWidth="20%" 
					 width="30%"/>
		
		<adsm:textbox
					 dataType="currency" 
					 property="valorDocumento" 
					 label="valorDocumento" 
					 labelWidth="20%" 
					 width="30%"/>
		
		<adsm:buttonBar freeLayout="true">
		
			<adsm:button onclick="return myFindButtonClick();" buttonType="findButton" caption="consultar" 
						/>
			
			<adsm:button buttonType="cleanButton" onclick="return myLimpar();" caption="limpar"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
			  property="doctoServico"
			  idProperty="idDoctoServico"
			  selectionMode="none" 
			  onRowClick="habilitaAba"
			  gridHeight="200" 
			  unique="true" 
			  rows="13" >	
			  
		<adsm:gridColumn width="30" title="documentoServico" property="tpDocumentoServico"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="30" title="" property="sgFilialOrigem" align="left"/>	
			<adsm:gridColumn width="70" title="" property="nrDocumentoServico" dataType="text" align="left"/>
		</adsm:gridColumnGroup>	  
	
		<adsm:gridColumn title="situacao" property="tpSituacao" dataType="text" width="309" isDomain="true"/>
		
		<adsm:gridColumn title="emissao" property="dhEmissao" dataType="JTDateTimeZone" align="center" />
		
		<adsm:gridColumn title="valor" property="siglaSimbolo" width="50" dataType="text"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">			
			<adsm:gridColumn title="" property="valorTotal" dataType="currency" width="100"/>
		</adsm:gridColumnGroup>
		
		<adsm:buttonBar>
		
	   	</adsm:buttonBar>
	   	
	</adsm:grid>
	
	
</adsm:window>




<script>
	function myFindButtonClick(){	
		if( validateConsultar() ){
			setElementValue("doctoServico.idDoctoServicoHidden", getElementValue("doctoServico.idDoctoServico"));
			findButtonScript('doctoServico', document.forms[0]);	
		}
	}

	function validateConsultar(){ 
		
		var retorno = true;
		var throwException = false;
		
		// Caso não seja informado o tipo de documento de serviço ou a filial do docto de serviço, lança a exception
		if( getElement("doctoServico.tpDocumentoServico").selectedIndex == 0 || getElementValue("doctoServico.filialByIdFilialOrigem.idFilial") == "" ){
			throwException = true;
		// Caso não seja informado o nrDoctoServico, data de emissão, valor do documento, lança a exception 
		}else if( getElementValue("doctoServico.nrDoctoServico") == "" && getElementValue("dataEmissao") == "" && getElementValue("valorDocumento") == "" ){
			throwException = true;
		}
		
		if(throwException){
			alert(i18NLabel.getLabel("LMS-36236"));
			retorno = false;
		}
		
		return retorno;
	}

	document.getElementById("doctoServico.nrDoctoServico").serializable = true;
var tabGroup = getTabGroup(document);

	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);
		disableDoctoServico();
	}
	
	function onChangeFilial(){
		disableDoctoServico();
		return changeDocumentWidgetFilial({
										 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
										 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')									 
										 });	
	}

function initWindow(eventObj){
	if(eventObj.name == "tab_click"){
		tabGroup.setDisabledTab("cad", true);
		tabGroup.setDisabledTab("devedores", true);
	}
}

function habilitaAba(){
	tabGroup.setDisabledTab("cad", false);
	tabGroup.setDisabledTab("devedores", false);
}

function filialOnChange_cb(data) {
   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   disableDoctoServico();
}

	
	function disableDoctoServico(){
		if (getElementValue("doctoServico.tpDocumentoServico") == "" || getElement("doctoServico.tpDocumentoServico").masterLink != undefined){
			resetValue('doctoServico.filialByIdFilialOrigem.idFilial');
			setDisabled('doctoServico.filialByIdFilialOrigem.idFilial', true);
			setDisabled('doctoServico.idDoctoServico', true);									
		} else {
			setDisabled('doctoServico.filialByIdFilialOrigem.idFilial', false);		
			if (getElementValue("doctoServico.filialByIdFilialOrigem.idFilial") == ""){
				habilitaLupa();
			} else {
				setDisabled('doctoServico.idDoctoServico', false);		
			}
		}


	}		

	
	/*
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("doctoServico.idDoctoServico", false);
		setDisabled("doctoServico.nrDoctoServico", true);
	}		
	
	function onChangeTpDocumentoServico() {
		var comboTpDocumentoServico = document.getElementById("doctoServico.tpDocumentoServico");
		setElementValue('tpDocumentoHidden', comboTpDocumentoServico.options[comboTpDocumentoServico.selectedIndex].text);
		var r = changeDocumentWidgetType({
									   documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"),
									   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
									   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
									   parentQualifier:'doctoServico',
									   documentGroup:'DOCTOSERVICE',
									   actionService:'lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction'
					   });		
					   
		disableDoctoServico();
		
		return r;					   
	}	
	
	function myLimpar(){		
		cleanButtonScript(this.document);
		disableDoctoServico();
	}

</script>