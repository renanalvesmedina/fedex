<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.expedicao.consultarNotaFiscalClienteAction" onPageLoadCallBack="pageLoad" >
	<adsm:form action="/expedicao/consultarNotaFiscalCliente">

		<adsm:i18nLabels>
			<adsm:include key="LMS-09101"/>
			<adsm:include key="LMS-09102"/>
			<adsm:include key="periodoEmissao"/>
			<adsm:include key="documentoServico"/>
		</adsm:i18nLabels>

		<adsm:textbox
			dataType="integer" 
			property="nrNotaFiscal" 
			label="numero" 
			labelWidth="20%" 
			width="30%" 
			size="9" 
			maxLength="9" 
			required="true"
			mask="000000000"
		/>

		<adsm:range label="periodoEmissao" labelWidth="20%" width="30%" maxInterval="60">
			<adsm:textbox dataType="JTDate" property="periodoEmissaoInicial"/>
			<adsm:textbox dataType="JTDate" property="periodoEmissaoFinal"/>
		</adsm:range>

		<adsm:hidden property="filialOrigem.empresa.tpEmpresa" serializable="false"/>
		<adsm:hidden property="filialOrigem.tpAcesso" serializable="false"/>
		<adsm:lookup
			dataType="text"
			property="filialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.expedicao.consultarNotaFiscalClienteAction.findLookupFilial"
			label="filialOrigem" 
			size="3" 
			maxLength="3" 
			labelWidth="20%" 
			width="30%" 
			exactMatch="true" 
			action="/municipios/manterFiliais"
		>
			<adsm:propertyMapping criteriaProperty="filialOrigem.tpAcesso" modelProperty="tpAcesso"/>
			<adsm:propertyMapping criteriaProperty="filialOrigem.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>

			<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" size="25" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:hidden property="filialDestino.empresa.tpEmpresa" serializable="false"/>
		<adsm:hidden property="filialDestino.tpAcesso" serializable="false"/>
		<adsm:lookup
			dataType="text" 
			property="filialDestino"
			idProperty="idFilial" 
			criteriaProperty="sgFilial"
			service="lms.expedicao.consultarNotaFiscalClienteAction.findLookupFilial"
			label="filialDestino" 
			size="3" 
			maxLength="3" 
			labelWidth="20%"
			width="30%" 
			exactMatch="true"
			action="/municipios/manterFiliais"
		>
			<adsm:propertyMapping criteriaProperty="filialDestino.tpAcesso" modelProperty="tpAcesso"/>
			<adsm:propertyMapping criteriaProperty="filialDestino.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>

			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="sgFilialDestino" modelProperty="sgFilial" />
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="25" disabled="true" serializable="false" />
		</adsm:lookup>
		<adsm:hidden property="sgFilialDestino" serializable="true"/>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="remetente" 
			maxLength="20" 
			property="remetente" 
			service="lms.expedicao.consultarNotaFiscalClienteAction.findLookupRemetente" 
			size="20" 
			labelWidth="20%" 
			width="80%"
		>
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" disabled="true" property="remetente.pessoa.nmPessoa" serializable="false"	size="58"/>
		</adsm:lookup>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text"
			exactMatch="true"
			idProperty="idCliente"
			label="destinatario"
			maxLength="20"
			property="destinatario"
			service="lms.expedicao.consultarNotaFiscalClienteAction.findLookupDestinatario"
			size="20"
			labelWidth="20%"
			width="80%"
		>
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>	
			<adsm:textbox dataType="text" disabled="true" property="destinatario.pessoa.nmPessoa" serializable="false" size="58"/>
		</adsm:lookup>

		<!-- ---------------------------------------- -->
		<adsm:hidden property="idDoctoServico" serializable="true"/>
		<adsm:hidden property="dsTpDocumentoServico" serializable="true"/>
		<adsm:hidden property="sgFilialOrigemDoctoServico" serializable="true"/>
		<adsm:hidden property="nrDoctoServico" serializable="true"/>

		<adsm:combobox
			property="doctoServico.tpDocumentoServico"
			serializable="true"
			label="documentoServico"
			labelWidth="20%" width="30%"
			service="lms.expedicao.consultarNotaFiscalClienteAction.findTpDoctoServico"
			optionProperty="value"
			optionLabelProperty="description"
			onchange="return changeTpDoctoServico(this);"
		>
			<adsm:lookup
				dataType="text"
				serializable="true"
				property="doctoServico.filialByIdFilialOrigem"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				service=""
				popupLabel="pesquisarDocumentoServico"
				disabled="true"
				action="/municipios/manterFiliais"
				size="3"
				maxLength="3"
				picker="false"
				onchange="changeDoctoServicoFilialOrigem();"
			>
				<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
				<adsm:hidden property="doctoServico.filialByIdFilialOrigem.empresa.tpEmpresa" serializable="false"/>
				<adsm:hidden property="doctoServico.filialByIdFilialOrigem.tpAcesso" serializable="false"/>
			</adsm:lookup>
			<adsm:hidden property="blBloqueado" value="N"/>
			<adsm:lookup
				dataType="integer"
				property="doctoServico"
				popupLabel="pesquisarDocumentoServico"
				idProperty="idDoctoServico"
				criteriaProperty="nrDoctoServico"
				service=""
				action=""
				size="12"
				maxLength="8"
				mask="00000000"
				serializable="true"
				disabled="true"
			>
			</adsm:lookup>
		</adsm:combobox>		
		<!-- ---------------------------------------- -->		

		<adsm:buttonBar freeLayout="true">
			<adsm:button disabled="false" id="__buttonBar:0.findButton" caption="consultar" onclick="validatePopUp()"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid
		idProperty="idNotaFiscalConhecimento"
		property="gridNotaFiscalConhecimento"
		disableMarkAll="true"
		selectionMode="none"				
		rows="9" 
		gridHeight="180"
		service="lms.expedicao.consultarNotaFiscalClienteAction.findPaginatedNotaFiscalCliente"
		rowCountService="lms.expedicao.consultarNotaFiscalClienteAction.getRowCountNotaFiscalCliente"
		scrollBars="horizontal"
	>

		<adsm:gridColumn width="100" title="notaFiscal" property="nrNotaFiscal" dataType="integer" mask="000000"/>
		<adsm:gridColumn width="40" title="serie" property="dsSerie"/>
		<adsm:gridColumn width="100" dataType="JTDate" title="dataEmissao" property="dtEmissao"/>
		<adsm:gridColumn width="80" dataType="integer" title="volumes" property="qtVolumes"/>
		<adsm:gridColumn width="80" dataType="integer" title="peso" property="paMercadoria"/>
		<adsm:gridColumn width="80" dataType="currency" title="valor" property="vlTotal"/>

		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="40" isDomain="true"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialOrigem" width="120"/>
			<adsm:gridColumn title="" property="nrDoctoServico" width="0" align="right" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialDestino" property="sgFilialDestino" width="180" />
			<adsm:gridColumn title="" property="nmFantasiaDestino" width="0" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="identificacao" 
				property="tpIdentificacaoRemetente" isDomain="true"
				width="50" />
		<adsm:gridColumn title="" 
				property="nrIdentificacaoRemetente" 
				align="right" width="120"/>
		<adsm:gridColumn title="remetente" 
				property="remetente" 
				width="140" />
		<adsm:gridColumn title="identificacao" 
				property="tpIdentificacaoDestinatario" isDomain="true"
				width="50" />
		<adsm:gridColumn title="" 
				property="nrIdentificacaoDestinatario" 
				align="right" width="120"/>
		<adsm:gridColumn title="destinatario" 
				property="destinatario" 
				width="140" />
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:grid>

</adsm:window>

<script language="javascript" type="text/javascript">
	function initWindow(eventObj) {
		setDisabled("__buttonBar:0.findButton", false);
		writeDataSession();

		if (eventObj.name == "cleanButton_click") {
			setElementValue("doctoServico.idDoctoServico", "");
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", true);
		}
	}

	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.expedicao.consultarNotaFiscalClienteAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}

	var dataInicial = null;
	var dataFinal = null;

	function dataSession_cb(data) {
		dataInicial = getNestedBeanPropertyValue(data,"periodoEmissaoInicial");
		dataFinal = getNestedBeanPropertyValue(data,"periodoEmissaoFinal");
		writeDataSession();
	}

	function writeDataSession() {
		if (dataInicial != null &&
			dataFinal != null) {
			setElementValue("periodoEmissaoInicial", setFormat(document.getElementById("periodoEmissaoInicial"), dataInicial));
			setElementValue("periodoEmissaoFinal", setFormat(document.getElementById("periodoEmissaoFinal"), dataFinal));
		}

		setFocusOnFirstFocusableField(document);			
	}

	function validatePopUp() {
		if ( (getElementValue("periodoEmissaoInicial") == null || getElementValue("periodoEmissaoInicial") == "")
			&& (getElementValue("doctoServico.nrDoctoServico") == null || getElementValue("doctoServico.nrDoctoServico") == "")
		) {
			alert(i18NLabel.getLabel("LMS-09101"));
			return false;
		}

		if ( (getElementValue("periodoEmissaoInicial") != null || getElementValue("periodoEmissaoInicial") != "")
			&& ((getElementValue("nrNotaFiscal") == null || getElementValue("nrNotaFiscal") == "" )
			&& (getElementValue("filialOrigem.idFilial") == null || getElementValue("filialOrigem.idFilial") == "" )
			&& (getElementValue("filialDestino.idFilial") == null || getElementValue("filialDestino.idFilial") == "" )
			&& (getElementValue("remetente.idCliente") == null || getElementValue("remetente.idCliente") == "" )
			&& (getElementValue("destinatario.idCliente") == null || getElementValue("destinatario.idCliente") == "" )
			&& (getElementValue("doctoServico.idDoctoServico") == null || getElementValue("doctoServico.idDoctoServico") == "" ) )
		) {
			alert(i18NLabel.getLabel("LMS-09102"));
			return false;
		}

		findButtonScript('gridNotaFiscalConhecimento', Lazy);

		return true;
	}	

	function changeDoctoServicoFilialOrigem() {
		var r = changeDocumentWidgetFilial({
			filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
			documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
			}
		);
		return r;
	}

	function changeTpDoctoServico(field) {

		var flag = changeDocumentWidgetType({
			documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"), 
			filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
			documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
			documentGroup:'DOCTOSERVICE',
			parentQualifier:'doctoServico',
			actionService:'lms.expedicao.consultarNotaFiscalClienteAction'}
		);

		var pmsFilial = document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").propertyMappings;
		pmsFilial[pmsFilial.length] = {modelProperty:"empresa.tpEmpresa", criteriaProperty:"doctoServico.filialByIdFilialOrigem.empresa.tpEmpresa", inlineQuery:true};
		pmsFilial[pmsFilial.length] = {modelProperty:"tpAcesso", criteriaProperty:"doctoServico.filialByIdFilialOrigem.tpAcesso", inlineQuery:true};

		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};		

		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.idCliente", criteriaProperty:"remetente.idCliente"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", criteriaProperty:"remetente.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa", criteriaProperty:"remetente.pessoa.nmPessoa"};

		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.idFilial", criteriaProperty:"filialDestino.idFilial"};
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.sgFilial", criteriaProperty:"filialDestino.sgFilial"};
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia", criteriaProperty:"filialDestino.pessoa.nmFantasia"};

		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.idCliente", criteriaProperty:"destinatario.idCliente"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao", criteriaProperty:"destinatario.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa", criteriaProperty:"destinatario.pessoa.nmPessoa"};

		resetValue('idDoctoServico');

		if (field.value != '') {		
			changeDocumentWidgetFilial({
				filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
				documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
			});
		}

		setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", getElementValue("filialOrigem.idFilial"));
		setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", getElementValue("filialOrigem.sgFilial"));
		setElementValue("doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", getElementValue("filialOrigem.pessoa.nmFantasia"));

		if (getElementValue("filialOrigem.idFilial") != ''){
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", false);
		}

		return flag;
	}

</script>