<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.emitirEntregasNaoRealizadasAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/entrega/emitirEntregasNaoRealizadas">
		
		<adsm:lookup label="filial" labelWidth="20%" width="80%"
		             property="rotaColetaEntrega.filial"
	        idProperty="idFilial" criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.entrega.emitirEntregasNaoRealizadasAction.findLookupFilial" 
	        dataType="text" size="3"
	        maxLength="3" exactMatch="true"
		             minLengthForAutoPopUpSearch="3"
		             required="true">
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping relatedProperty="sgFilial" modelProperty="sgFilial" />        	
        	<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.idFilial" modelProperty="idFilial"/>	
			<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" modelProperty="idFilial"/>
			<adsm:propertyMapping relatedProperty="doctoServicoOriginal.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="doctoServicoOriginal.filialByIdFilialOrigem.idFilial" modelProperty="idFilial"/>
			<adsm:propertyMapping relatedProperty="doctoServicoOriginal.idDoctoServico" blankFill="true"/>
			<adsm:propertyMapping relatedProperty="doctoServicoOriginal.nrDoctoServico" blankFill="true"/>
			<adsm:propertyMapping relatedProperty="manifestoEntrega.idManifestoEntrega" blankFill="true"/>
			<adsm:propertyMapping relatedProperty="manifestoEntrega.nrManifestoEntrega" blankFill="true"/>
			<adsm:propertyMapping relatedProperty="controleCarga.idControleCarga" blankFill="true"/>
			<adsm:propertyMapping relatedProperty="controleCarga.nrControleCarga" blankFill="true"/>
			
            <adsm:textbox dataType="text" 
            			  property="rotaColetaEntrega.filial.pessoa.nmFantasia" 
		  		serializable="true" size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
        <adsm:hidden property="sgFilial" serializable="true"/>

		<adsm:combobox property="doctoServicoOriginal.tpDocumentoServico" 
					   label="documentoServico" labelWidth="20%" width="77%" 
					   service="lms.entrega.emitirEntregasNaoRealizadasAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="var args = {
							   documentTypeElement:this, 
							   filialElement:document.getElementById('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'), 
							   documentNumberElement:document.getElementById('doctoServicoOriginal.idDoctoServico'), 
							   parentQualifier:'', 
							   documentGroup:'SERVICE',
							   actionService:'lms.entrega.emitirEntregasNaoRealizadasAction'
						   };
					   	   return changeDocumentWidgetType(args);">
		
		<adsm:lookup dataType="text"
			property="doctoServicoOriginal.filialByIdFilialOrigem"
			idProperty="idFilial"
			disabled="true" criteriaSerializable="true"
			criteriaProperty="sgFilial"
			action="/entrega/emitirEntregasNaoRealizadas"
			service="lms.entrega.emitirEntregasNaoRealizadasAction.findLookupFilial"
			onDataLoadCallBack="filialConhecimento"
			onchange="filialConhecimentoChange(); return changeDocumentWidgetFilial({
						   filialElement:document.getElementById('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('doctoServicoOriginal.idDoctoServico')
						   });"
			serializable="true" size="3" maxLength="3" picker="false">
			<adsm:lookup dataType="integer"
						 property="doctoServicoOriginal"
						 idProperty="idDoctoServico"
						 criteriaProperty="nrDoctoServico"
						 service="" 
						 action=""
						 popupLabel="pesquisarDocumentoServico"
						 size="12" maxLength="8" mask="00000000" disabled="true" 
						 onDataLoadCallBack="dataLoadDocto"
						 onPopupSetValue="popUpSetDocto">   
					<adsm:propertyMapping criteriaProperty="doctoServicoOriginal.filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial"/>
					<adsm:propertyMapping criteriaProperty="doctoServicoOriginal.filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial"/>
					<adsm:propertyMapping criteriaProperty="doctoServicoOriginal.tpDocumentoServico" modelProperty="tpDocumentoServico"/>
	 				<adsm:propertyMapping modelProperty="nrDoctoServico" relatedProperty="nrDoctoServico"/>
			</adsm:lookup>
		</adsm:lookup>
		</adsm:combobox>	
        <adsm:hidden property="nrDoctoServico" serializable="true"/>
		
        <adsm:lookup
			label="manifestoEntrega" property="manifestoEntrega.filial" idProperty="idFilial"
			criteriaProperty="sgFilial"	action="/municipios/manterFiliais"
			service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupFilial"
			disabled="true"	dataType="text"	picker="false" size="3"
			maxLength="3" labelWidth="20%" width="34%" criteriaSerializable="true"
			exactMatch="true" serializable="false">
			<adsm:lookup
				property="manifestoEntrega" idProperty="idManifestoEntrega"
				criteriaProperty="nrManifestoEntrega"
				service="lms.entrega.emitirEntregasNaoRealizadasAction.findLookupManifestoEntrega"
				action="/entrega/consultarManifestosEntrega" disabled="false"
				dataType="integer" size="20" maxLength="16"	mask="00000000" exactMatch="true"
				cmd="lookup" popupLabel="pesquisarManifestoEntrega"	serializable="true"
				onDataLoadCallBack="dataLoadManifesto" criteriaSerializable="true"
				onPopupSetValue="popUpSetManifesto">
				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>	
   				<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.idFilial" modelProperty="filial.idFilial"/>
			</adsm:lookup>
		</adsm:lookup>
        
        
		<adsm:lookup
			dataType="text"	property="controleCarga.filialByIdFilialOrigem"
			idProperty="idFilial" criteriaProperty="sgFilial" labelWidth="20%"
			service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupFilial"
			action="/municipios/manterFiliais" disabled="true" width="84%"
			popupLabel="pesquisarFilial" label="controleCargas" size="3"
			maxLength="3" picker="false" serializable="false" criteriaSerializable="true">
			<adsm:lookup
				dataType="integer" property="controleCarga"
				idProperty="idControleCarga" criteriaProperty="nrControleCarga" disabled="false"
				service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupControleCarga"
				action="/carregamento/manterControleCargas"
				cmd="list" popupLabel="pesquisarControleCarga"
				maxLength="8" size="8" mask="00000000"
				criteriaSerializable="true"
				onDataLoadCallBack="dataLoadControleCarga"
				onPopupSetValue="popUpSetControleCarga">
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" blankFill="false" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false"/>
			</adsm:lookup>
		</adsm:lookup>	

		<adsm:lookup property="rotaColetaEntrega" dataType="integer" 
   		             criteriaProperty="nrRota" 		             
		             idProperty="idRotaColetaEntrega"
		             label="rotaEntrega" labelWidth="20%" width="80%"
		             size="3" maxLength="3" 
		             minLengthForAutoPopUpSearch="7" exactMatch="true" 
		             action="/municipios/manterRotaColetaEntrega"
		             service="lms.entrega.emitirEntregasNaoRealizadasAction.findLookupRotaColetaEntrega">
		    <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" />
   			<adsm:propertyMapping relatedProperty="numeroRota" modelProperty="nrRota" />   
   			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>   
   			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>  
   			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false" />  
        	<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial"/>
        	<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial"/>
        	<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
            <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" />
		</adsm:lookup>
		<adsm:hidden property="numeroRota" serializable="true"/>
		 
	    <adsm:lookup label="remetente" 
			         property="cliente" 
        			 idProperty="idCliente"                      
        			 action="/vendas/manterDadosIdentificacao" 
                     criteriaProperty="pessoa.nrIdentificacao" 
                     relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
	        dataType="text" exactMatch="true" maxLength="20" 
                     service="lms.entrega.emitirEntregasNaoRealizadasAction.findLookupCliente" 
	        size="20" labelWidth="20%" width="80%">			
			<adsm:propertyMapping relatedProperty="nmRemetente" modelProperty="pessoa.nmPessoa"/>			
			<adsm:textbox dataType="text" 
		  		disabled="true" property="nmRemetente" 
		  		serializable="true"	size="30" maxLength="60"/>
		</adsm:lookup>		
		<adsm:range label="periodoFechamento" labelWidth="20%" width="80%" >
			<adsm:textbox dataType="JTDate" property="periodoInicial" required="true" /> 
			<adsm:textbox dataType="JTDate" property="periodoFinal" required="true" />
		</adsm:range>
        
        <adsm:combobox property="tpFormatoRelatorio" 
        	label="formatoRelatorio" domain="DM_FORMATO_RELATORIO"
        	required="true" labelWidth="20%" width="80%" defaultValue="pdf"/>
        
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.entrega.emitirEntregasNaoRealizadasAction"/>
			<adsm:button caption="limpar" buttonType="cleanButton" disabled="false" id="btnLimpar" onclick="myCleanButton(this)"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>


	/**
	*	Método sobrescrito para poder setar a filial do usuário após o click no botão Limpar
	*/
	function myCleanButton(elem){
		cleanButtonScript(elem.document);
		buscaFilialUsuario();		
	}

	function myOnPageLoadCallBack_cb(data,erro){
		onPageLoad_cb(data,erro);
		buscaFilialUsuario();
	}
	
	function buscaFilialUsuario(){
		var dados = new Array();
        var sdo = createServiceDataObject("lms.entrega.emitirEntregasNaoRealizadasAction.findFilialUsuario",
                                          "retornoFilialUsuario",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
	}
	
	function retornoFilialUsuario_cb(data,erro){
		if( erro != undefined ){
			alert(erro);
			setFocus(getElement('filial.sgFilial'));
			return false;
		}
		fillFormWithFormBeanData(0, data);
		setElementValue('sgFilial',data.rotaColetaEntrega.filial.sgFilial);
		setFocusOnFirstFocusableField(document);			
		
		setElementValue('manifestoEntrega.filial.sgFilial',data.rotaColetaEntrega.filial.sgFilial);
		setElementValue('manifestoEntrega.filial.idFilial',data.rotaColetaEntrega.filial.idFilial);
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial',data.rotaColetaEntrega.filial.sgFilial);
		setElementValue('controleCarga.filialByIdFilialOrigem.idFilial',data.rotaColetaEntrega.filial.idFilial);
		setElementValue('doctoServicoOriginal.filialByIdFilialOrigem.sgFilial',data.rotaColetaEntrega.filial.sgFilial);
		setElementValue('doctoServicoOriginal.filialByIdFilialOrigem.idFilial',data.rotaColetaEntrega.filial.idFilial);
	}
	
	function filialConhecimentoChange() {
		var sgFilial = getElementValue("doctoServicoOriginal.filialByIdFilialOrigem.sgFilial");
		if(sgFilial == "") {
			resetValue("doctoServicoOriginal.nrDoctoServico");
			resetValue("doctoServicoOriginal.filialByIdFilialOrigem.idFilial");
			resetValue("doctoServicoOriginal.filialByIdFilialOrigem.sgFilial");
		}
		doctoServicoOriginal_filialByIdFilialOrigem_sgFilialOnChangeHandler();
		return true;
	}

	function filialConhecimento_cb(data) {
		if(data == undefined || data.length == 0){
			resetValue("doctoServicoOriginal.nrDoctoServico");
		}
		return doctoServicoOriginal_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	}

	function dataLoadDocto_cb(data) {
		var r = doctoServicoOriginal_nrDoctoServico_exactMatch_cb(data);
		var idManifesto = getElementValue("manifestoEntrega.idManifestoEntrega");
		var idControleCarga = getElementValue("controleCarga.idControleCarga");
		if (data != undefined && data.length == 1){
			setElementValue('nrDoctoServico',data[0].nrDoctoServico);
			validateDoctoServicoManifestoControleCarga(getNestedBeanPropertyValue(data[0], "idDoctoServico"), idManifesto, idControleCarga);
		}
		return r;
	}
	function popUpSetDocto(data) {
		var idManifesto = getElementValue("manifestoEntrega.idManifestoEntrega");
		var idControleCarga = getElementValue("controleCarga.idControleCarga");
		setElementValue('nrDoctoServico',data.nrDoctoServico);
		validateDoctoServicoManifestoControleCarga(getNestedBeanPropertyValue(data, "idDoctoServico"), idManifesto, idControleCarga);
		return true;
	}
	
	function dataLoadManifesto_cb(data) {
		manifestoEntrega_nrManifestoEntrega_exactMatch_cb(data);
		var idDoctoServico = getElementValue("doctoServicoOriginal.idDoctoServico");
		var idControleCarga = getElementValue("controleCarga.idControleCarga");
		if (data != undefined && data.length == 1){
			setElementValue('manifestoEntrega.nrManifestoEntrega',data[0].nrManifestoEntrega);
			validateDoctoServicoManifestoControleCarga(idDoctoServico, getNestedBeanPropertyValue(data[0], "idManifestoEntrega"), idControleCarga);
		}
	}
	function popUpSetManifesto(data) {
		var idDoctoServico = getElementValue("doctoServicoOriginal.idDoctoServico");
		var idControleCarga = getElementValue("controleCarga.idControleCarga");
		setElementValue('manifestoEntrega.nrManifestoEntrega',data.nrManifestoEntrega);
		validateDoctoServicoManifestoControleCarga(idDoctoServico, data.idManifestoEntrega, idControleCarga);
		return true;
	}
	
	function dataLoadControleCarga_cb(data) {
		controleCarga_nrControleCarga_exactMatch_cb(data);
		var idDoctoServico = getElementValue("doctoServicoOriginal.idDoctoServico");
		var idManifesto = getElementValue("manifestoEntrega.idManifestoEntrega");
		if (data != undefined && data.length == 1){
			setElementValue('controleCarga.nrControleCarga',data[0].nrControleCarga);
			validateDoctoServicoManifestoControleCarga(idDoctoServico, idManifesto, getNestedBeanPropertyValue(data[0], "idControleCarga"));
		}
	}
	function popUpSetControleCarga(data) {
		var idDoctoServico = getElementValue("doctoServicoOriginal.idDoctoServico");
		var idManifesto = getElementValue("manifestoEntrega.idManifestoEntrega");
		setElementValue('controleCarga.nrControleCarga',data.nrControleCarga);
		validateDoctoServicoManifestoControleCarga(idDoctoServico, idManifesto, getNestedBeanPropertyValue(data, "idControleCarga"));
		return true;
	}
	
	
	function validateDoctoServicoManifestoControleCarga(idDoctoServico, idManifesto, idControleCarga) {
		var sdo = createServiceDataObject("lms.entrega.emitirEntregasNaoRealizadasAction.findDoctoServico","onDataLoadSamples",{idDoctoServico:idDoctoServico,idManifesto:idManifesto,idControleCarga:idControleCarga});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function onDataLoadSamples_cb(data,error){
		if(error != undefined) {
			alert(error);
			resetValue("doctoServicoOriginal.idDoctoServico");
			resetValue("manifestoEntrega.idManifestoEntrega");
			resetValue("controleCarga.idControleCarga");
			return;
		}
	}	
</script>