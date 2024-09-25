<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script src="/lms/lib/lookup.js" type="text/javascript"></script>

<script type="text/javascript">
<!--
	//FUNÇÔES RELACIONADAS AO DOCTO SERVICO
	function changeTpDoctoServico(field) {
		resetValue('idDoctoServico');
		var flag = changeDocumentWidgetType(
					{documentTypeElement:field, 
					 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
					 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
					 parentQualifier:'doctoServico',
					 documentGroup:'DOCTOSERVICE',
					 actionService:'lms.entrega.registrarEntregasParceirosAction'}
				  );
		document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").service = "lms.entrega.registrarEntregasParceirosAction.findLookupFilial";
		var pmsFilial = document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").propertyMappings;
		pmsFilial[pmsFilial.length] = {modelProperty:"empresa.tpEmpresa", criteriaProperty:"empresa.tpEmpresa", inlineQuery:true};
		pmsFilial[pmsFilial.length] = {modelProperty:"tpAcesso", criteriaProperty:"tpAcesso", inlineQuery:true};

		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};
		pms[pms.length] = {modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", relatedProperty:"doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", blankFill:false};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.idCliente", criteriaProperty:"destinatario.idCliente", inlineQuery:true};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao", criteriaProperty:"destinatario.pessoa.nrIdentificacao", inlineQuery:false};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa", criteriaProperty:"destinatario.pessoa.nmPessoa", inlineQuery:false};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.idCliente", criteriaProperty:"remetente.idCliente", inlineQuery:true};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", criteriaProperty:"remetente.pessoa.nrIdentificacao", inlineQuery:false};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa", criteriaProperty:"remetente.pessoa.nmPessoa", inlineQuery:false};
		
		addElementChangeListener({e:document.getElementById("destinatario.idCliente"), changeListener:document.getElementById("doctoServico.idDoctoServico")});
		addElementChangeListener({e:document.getElementById("remetente.idCliente"), changeListener:document.getElementById("doctoServico.idDoctoServico")});

		return flag;
	}
	function changeFilialDoctoServico() {
		resetValue('idDoctoServico');
		return changeDocumentWidgetFilial(
					{filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"), 
					 documentNumberElement:document.getElementById("doctoServico.idDoctoServico")}
		);
	}
	//ADICIONAR CONTROLE CARGA APOS BUSCAR MEIO DE TRANSPORTE

	
	function onDataLoadSamples_cb(data) {
		var formElem = document.getElementById(mainForm);
		fillFormWithFormBeanData(formElem.tabIndex, data);
	}
	//SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.entrega.registrarEntregasParceirosAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	var idCliente = null;
	var nrIdentificacao = null;
	var nmPessoa = null;
	var idUsuario = null;	
	var blIrrestritoCliente = null;
	
	function dataSession_cb(data) {
		idCliente = getNestedBeanPropertyValue(data,"idCliente");
		nrIdentificacao = getNestedBeanPropertyValue(data,"pessoa.nrIdentificacao");
		nmPessoa = getNestedBeanPropertyValue(data,"pessoa.nmPessoa");
		idUsuario = getNestedBeanPropertyValue(data,"usuario.idUsuario");
		blIrrestritoCliente = getNestedBeanPropertyValue(data,"usuario.blIrrestritoCliente");
		setElementValue("usuario.idUsuario",idUsuario);
		setElementValue("usuario.blIrrestritoCliente",blIrrestritoCliente);

		setElementValue("proprietarioNrIdentificacao", getNestedBeanPropertyValue(data,"empresa.nrIdentificacao"));
		setElementValue("parceiro.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data,"empresa.nrIdentificacaoFormatado"));
		setElementValue("proprietario.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"empresa.nmEmpresa"));
		setElementValue("doctoServico.filialByIdFilialOrigem.tpOrdemDoc", getNestedBeanPropertyValue(data,"doctoServico.filialByIdFilialOrigem.tpOrdemDoc"));
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("doctoServico.idDoctoServico",true);
		}
	}

	function dataLoadVoid_cb(data,error) {
		if (error)
			alert(error);
	}
	function rowClick(id) {
		var data = ManifestoEntregaDocumentoGridDef.findById(id);

		var URL = "/entrega/registrarEntregasParceiros.do?cmd=det" +
					//"&idCliente=" + getNestedBeanPropertyValue(data, "doctoServico_clienteParceiro_idProprietario") +
					"&manifestoEntrega.filial.idFilial=" + getNestedBeanPropertyValue(data, "manifestoEntrega_filial_idFilial") +
					"&manifestoEntrega.filial.sgFilial=" + getNestedBeanPropertyValue(data, "manifestoEntrega_filial_sgFilial") +
					"&manifestoEntrega.idManifestoEntrega=" + getNestedBeanPropertyValue(data, "manifestoEntrega_idManifestoEntrega") +
					"&manifestoEntrega.nrManifestoEntrega=" + getNestedBeanPropertyValue(data, "manifestoEntrega_nrManifestoEntrega") +
					"&doctoServico.tpDocumentoServico=" + getNestedBeanPropertyValue(data, "doctoServico_tpDocumentoServico.value") +
					"&doctoServico.filialByIdFilialOrigem.idFilial=" + getNestedBeanPropertyValue(data, "doctoServico_filialOrigem_idFilial") +
					"&doctoServico.filialByIdFilialOrigem.sgFilial=" + getNestedBeanPropertyValue(data, "doctoServico_filialOrigem_sgFilial") +
					"&doctoServico.idDoctoServico=" + getNestedBeanPropertyValue(data, "doctoServico_idDoctoServico") +
					"&doctoServico.nrDoctoServico=" + getNestedBeanPropertyValue(data, "doctoServico_nrDoctoServico");
		showModalDialog(URL,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:220px;');
		return false;
	}
//-->
</script>
<adsm:window service="lms.entrega.registrarEntregasParceirosAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/entrega/registrarEntregasParceiros" height="110">

	<adsm:hidden property="usuario.idUsuario"/>
	<adsm:hidden property="usuario.blIrrestritoCliente"/>
	<adsm:hidden property="actionBaixas" value="lms.entrega.registrarEntregasParceirosAction"/>

<!-- lookup PROPRIETARIO -->
	<adsm:lookup 
 		service="lms.entrega.registrarEntregasParceirosAction.findLookupProprietario" 
 		dataType="text" 
 		property="parceiro" 
 		idProperty="idProprietario"
 		criteriaProperty="pessoa.nrIdentificacao" 
 		relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 		label="parceiro" size="30" maxLength="20"  
 		action="/contratacaoVeiculos/manterProprietarios" labelWidth="18%" width="80%" required="true" disabled="true">
        	<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
            <adsm:propertyMapping relatedProperty="proprietarioNrIdentificacao" modelProperty="pessoa.nrIdentificacao" />
            <adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="60" disabled="true" />
 	</adsm:lookup>
 	<adsm:hidden property="proprietarioNrIdentificacao" serializable="true"/>
 	<adsm:hidden property="tpAcessoFilial" serializable="true" value="A"/>

<!-- lookup MANIFESTO ENTREGA -->
	<adsm:lookup dataType="text" 
   				property="manifestoEntrega.filial" idProperty="idFilial" criteriaProperty="sgFilial" 
   				service="lms.entrega.registrarEntregasParceirosAction.findLookupFilial"
   				action="/municipios/manterFiliais" cmd="list" popupLabel="pesquisarFilial"
   				label="manifesto" size="3" maxLength="3" disabled="false"
   				labelWidth="18%" width="100%" exactMatch="true" picker="false">   				
   			<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
   			<adsm:propertyMapping criteriaProperty="tpAcessoFilial" modelProperty="tpAcesso" />
   			
   			<adsm:lookup dataType="integer" 
	   				property="manifestoEntrega"  idProperty="idManifestoEntrega" criteriaProperty="nrManifestoEntrega" 
	   				service="lms.entrega.registrarEntregasParceirosAction.findLookupManifestoEntrega" serializable="true"
	   				action="/entrega/consultarManifestosEntrega" cmd="lookup" popupLabel="pesquisarManifestoEntrega"
	   				size="7" maxLength="8" mask="00000000" exactMatch="true" disabled="false" onDataLoadCallBack="dataLoadManifestoEntrega">
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.idFilial" modelProperty="filial.idFilial" />
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.sgFilial" modelProperty="filial.sgFilial" />
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
   				<adsm:propertyMapping criteriaProperty="proprietarioNrIdentificacao" modelProperty="nrIdentificacao" />
   			</adsm:lookup>   			
   		<adsm:hidden property="manifestoEntrega.filial.pessoa.nmFantasia" serializable="false" />
   	</adsm:lookup>
   	
	<adsm:combobox
		property="doctoServico.tpDocumentoServico"
		label="documentoServico"
		labelWidth="18%"
		optionLabelProperty="description"
		service="lms.entrega.registrarEntregasParceirosAction.findTpDoctoServico"
		optionProperty="value" width="32%"
		onchange="return changeTpDoctoServico(this);"
	>
		<adsm:hidden property="empresa.tpEmpresa" value="M"/>
		<adsm:hidden property="tpAcesso" value="EM"/>
		<adsm:lookup
			dataType="text"
			property="doctoServico.filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial" 
			service=""
			disabled="true"
			action=""
			size="3"
			maxLength="3"
			picker="false"
			popupLabel="pesquisarDocumentoServico"
			onchange="return changeFilialDoctoServico();"
		>
			<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup
			dataType="integer"
			property="doctoServico"
			idProperty="idDoctoServico"
			criteriaProperty="nrDoctoServico" 
			service=""
			action=""
			size="12"
			maxLength="8"
			mask="00000000"
			serializable="true"
			disabled="true"
			popupLabel="pesquisarDocumentoServico"/>
		<adsm:hidden property="idDoctoServico"/>

	</adsm:combobox>

	<adsm:lookup
		label="notaFiscal"
		action="/expedicao/consultarNotaFiscalCliente"
		service="lms.entrega.registrarEntregasParceirosAction.findLookupConhecimento"
		dataType="integer"
		property="notaFiscalCliente"
		popupLabel="pesquisarNotaFiscal"
		idProperty="idDoctoServico"
		labelWidth="13%"
		width="37%"
		criteriaProperty="nrNotaFiscal"
		mask="00000000"
		maxLength="30"
		exactMatch="false"
		minLengthForAutoPopUpSearch="3"
	>
		<adsm:propertyMapping criteriaProperty="remetente.idCliente" modelProperty="remetente.idCliente" inlineQuery="true"/>
		<adsm:propertyMapping criteriaProperty="remetente.pessoa.nrIdentificacao" modelProperty="remetente.pessoa.nrIdentificacao" inlineQuery="false"/>
		<adsm:propertyMapping criteriaProperty="remetente.pessoa.nmPessoa" modelProperty="remetente.pessoa.nmPessoa" inlineQuery="false"/>

		<adsm:propertyMapping criteriaProperty="destinatario.idCliente" modelProperty="destinatario.idCliente" inlineQuery="false"/>
		<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nrIdentificacao" modelProperty="destinatario.pessoa.nrIdentificacao" inlineQuery="false"/>
		<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nmPessoa" modelProperty="destinatario.pessoa.nmPessoa" inlineQuery="false"/>

		<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="doctoServico.filialByIdFilialOrigem.empresa.tpEmpresa" inlineQuery="true"/>
		<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="doctoServico.filialByIdFilialOrigem.tpAcesso" inlineQuery="true"/>

		<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="filialOrigem.empresa.tpEmpresa" inlineQuery="true"/>
		<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="filialOrigem.tpAcesso" inlineQuery="true"/>

		<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="filialDestino.empresa.tpEmpresa" inlineQuery="true"/>
		<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="filialDestino.tpAcesso" inlineQuery="true"/>
	</adsm:lookup>

	<adsm:lookup
		dataType="text"
		property="remetente"
		idProperty="idCliente" 
		criteriaProperty="pessoa.nrIdentificacao"
		relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		service="lms.entrega.registrarEntregasParceirosAction.findLookupCliente"
		label="remetente"
		size="10"
		maxLength="20"
		labelWidth="18%"
		width="32%"
		action="/vendas/manterDadosIdentificacao" exactMatch="true"
	>
		<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
		<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="25" disabled="true" serializable="false" />
	</adsm:lookup>	

	<adsm:lookup
		dataType="text"
		property="destinatario"
		idProperty="idCliente"
		criteriaProperty="pessoa.nrIdentificacao"
		relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		service="lms.entrega.registrarEntregasParceirosAction.findLookupCliente" 
		label="destinatario"
		size="11"
		maxLength="20"
		labelWidth="13%"
		width="37%"
		action="/vendas/manterDadosIdentificacao" exactMatch="true"
	>
		<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
		<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="25" disabled="true" serializable="false" />
	</adsm:lookup>

 	<adsm:combobox  
 		property="doctoServico.filialByIdFilialOrigem.tpOrdemDoc" 
 		domain="DM_TP_ORDEM_DOC"  
 		label="ordemDocumentos" 
 		labelWidth="18%"  
 		width="32%"  
 		renderOptions="true"  
 	/>
 	
 	<adsm:textbox label="numeroPreAWB" property="idAwb" dataType="integer" width="37%" labelWidth="13%" maxLength="15"/>

		<adsm:buttonBar freeLayout="true">
		<adsm:findButton callbackProperty="ManifestoEntregaDocumento"/>
		<adsm:button caption="limpar" disabled="false" buttonType="resetButton" onclick="resetButtonFunction()"/>
	</adsm:buttonBar>
	<adsm:i18nLabels>
		<adsm:include key="LMS-09035"/>
		<adsm:include key="LMS-09045"/>
		<adsm:include key="LMS-09039"/>
		<adsm:include key="LMS-09038"/>
		<adsm:include key="LMS-09139"/>
	</adsm:i18nLabels>
	</adsm:form>

	<adsm:grid
		idProperty="idManifestoEntregaDocumento"
		property="ManifestoEntregaDocumento"
		onRowClick="rowClick"
		selectionMode="none"
		scrollBars="horizontal"
		rows="9"
		gridHeight="170"
	>
		<adsm:gridColumn title="documentoServico" property="doctoServico_tpDocumentoServico" width="100" isDomain="true"/>
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="" property="doctoServico_filialOrigem_sgFilial" width="50"/>
			<adsm:gridColumn title="" property="doctoServico_nrDoctoServico" dataType="integer" mask="00000000" width="100"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="60" title="identificacao" property="destinatario_pessoa_tpIdentificacao" isDomain="true" align="left"/>
		<adsm:gridColumn width="120" title="" property="destinatario_pessoa_nrIdentificacaoFormatado" align="right"/>
		<adsm:gridColumn width="150" title="destinatario" property="destinatario_pessoa_nmPessoa" align="left"/>
		<adsm:gridColumn width="90" title="dpe" property="doctoServico_dtPrevEntrega" dataType="JTDate" align="center"/>
		<adsm:gridColumn width="300" title="endereco" property="doctoServico_dsEnderecoEntregaReal" align="left"/>	
		<adsm:gridColumn width="200" title="ciaAerea" property="cia_aerea" align="left"/>
		<adsm:gridColumn width="90" title="preAwb" property="awb_id" align="left"/>
		<adsm:gridColumn width="80"
						 title="agendar" 
						 property="agendar" 
						 align="center"
						 image="/images/popup.gif"
						 link="javascript:findDadosAgendamentos" 
						 linkIdProperty="idManifestoEntregaDocumento" 
						 popupDimension="790,496" />
		
	</adsm:grid>
	<adsm:buttonBar>
	</adsm:buttonBar>
</adsm:window>
<script>

function dataLoadManifestoEntrega_cb(data,error) {
	if(data.length > 0) {
		var nrIdentificacao = getNestedBeanPropertyValue(data[0],"nrIdentificacaoFormatado");
		var nome = getNestedBeanPropertyValue(data[0],"manifesto.controleCarga.proprietario.pessoa.nmPessoa");
		setElementValue("proprietario.pessoa.nmPessoa",nome);
		setElementValue("parceiro.pessoa.nrIdentificacao",nrIdentificacao);
		setDisabled("proprietario.pessoa.nmPessoa",true);
		setFocus("doctoServico.tpDocumentoServico", true);
		return lookupExactMatch({e:document.getElementById("manifestoEntrega.idManifestoEntrega"), data:data});;
	}
	if(error!="" && error != null && error!="undefined"){
		alert(error);
	}
	return false;
}

function resetButtonFunction() {
	
	var nrIdentificacaoEmpresa = getElementValue('proprietarioNrIdentificacao');
	var nrIdentificacaoFormatadoEmpresa = getElementValue('parceiro.pessoa.nrIdentificacao');
	var nmPessoaPessoaProprietario = getElementValue('proprietario.pessoa.nmPessoa');
	
	resetValue(this.document);
	
	setElementValue("usuario.idUsuario",idUsuario);
	setElementValue("usuario.blIrrestritoCliente",blIrrestritoCliente);
	
	setElementValue("proprietarioNrIdentificacao", nrIdentificacaoEmpresa);
	setElementValue("parceiro.pessoa.nrIdentificacao", nrIdentificacaoFormatadoEmpresa);
	setElementValue("proprietario.pessoa.nmPessoa", nmPessoaPessoaProprietario);
}


// LMS - 4599
function findDadosAgendamentos(id){
	var manifestoEntrega = ManifestoEntregaDocumentoGridDef.findById(id);
	
	var data = new Object();
	data.idDoctoServico = getNestedBeanPropertyValue(manifestoEntrega, "doctoServico_idDoctoServico");
	data.idManifestoEntregaDocumento = getNestedBeanPropertyValue(manifestoEntrega, "idManifestoEntregaDocumento");
			
	var sdo = createServiceDataObject("lms.entrega.registrarEntregasParceirosAction.findDadosAgendamentoParceira", "openAgendamentos", data);
	xmit({serviceDataObjects:[sdo]});
	
	window.event.cancelBubble = true;
	
}

//  LMS - 4599 - Valida e envia os dados para a tela de manter agendamentos
function openAgendamentos_cb(data, errorKey) {	

	 var bl_pode_agendar = data.bl_pode_agendar;
 
	 if(bl_pode_agendar == "true"){
		 var URL = "entrega/manterAgendamentos.do?cmd=main" +
		 	 "&tipoEmpresaSessao="+ data.tipoEmpresaSessao +
	  	 "&idDoctoServico=" + data.idDoctoServico +
	  	 "&idManifestoEntregaDocumento=" + data.idManifestoEntregaDocumento;

		 	 showModalDialog(URL,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
	 }else{
		 alert("LMS-09139 -" + i18NLabel.getLabel("LMS-09139"));
	 }
	 
	 
}

</script>