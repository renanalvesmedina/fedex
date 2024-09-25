<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.pesquisarConhecimentoAction" onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04425"/>
	</adsm:i18nLabels>

	<adsm:form action="/expedicao/pesquisarConhecimento">
		<adsm:hidden property="dvConhecimento" serializable="false"/>
		<adsm:hidden property="filialByIdFilialOrigem.empresa.tpEmpresa"/>
		<adsm:hidden property="blBloqueado"/>
		<adsm:hidden property="nrDoctoServico"/>
		<adsm:hidden property="tpConhecimento"/>
		<adsm:hidden property="clienteByIdClienteConsignatario.idCliente"/>

		<!-- criterias da tela de 'Consultar localizacoes mercadorias' -->
		<adsm:hidden property="servico.tpModal"/>
		<adsm:hidden property="servico.tpAbrangencia"/>
		<adsm:hidden property="servico.tipoServico.idTipoServico"/>
		<adsm:hidden property="finalidade"/>
		<adsm:hidden property="idFilialDoctoSer" serializable="false"/>
		
		<adsm:hidden property="idDoctoServico" serializable="false"/>
		
		<adsm:lookup
			service="lms.expedicao.pesquisarConhecimentoAction.findFilial"
			action="/municipios/manterFiliais"
			property="filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialOrigem"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="15%"
			width="48%">
			<adsm:propertyMapping 
				relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" 
				modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox
				dataType="text"
				property="filialByIdFilialOrigem.pessoa.nmFantasia"
				size="30"
				serializable="false"
				disabled="true"/>
		</adsm:lookup>
		<adsm:combobox
			label="tipoDocumento"
			labelWidth="15%"
			width="22%"
			boxWidth="73"
			property="tpDocumentoServico"
			domain="DM_TIPO_COD_SERV_CONHECIMENTO"/>

		<adsm:textbox
			property="nrConhecimento"
			label="numero"
			dataType="integer"
			size="8"
			maxLength="8"
			labelWidth="15%"
			width="48%"
			mask="00000000"/>
		<adsm:textbox
			property="nrNotaFiscal"
			label="notaFiscal"
			dataType="integer"
			size="10"
			maxLength="9"
			labelWidth="15%"
			width="22%"/>

		<adsm:lookup
			service="lms.expedicao.pesquisarConhecimentoAction.findFilial"
			action="/municipios/manterFiliais"
			property="filialByIdFilialDestino"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialDestino"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="15%"
			width="48%">
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
		 	<adsm:textbox
		 		dataType="text"
				property="filialByIdFilialDestino.pessoa.nmFantasia"
				size="30"
				serializable="false"
				disabled="true"/>
		</adsm:lookup>
		<adsm:combobox
			property="tpSituacaoConhecimento"
			label="situacao"
			optionLabelProperty="description"
			optionProperty="value"
			service="lms.expedicao.pesquisarConhecimentoAction.findTpSituacaoConhecimento"
			labelWidth="15%"
			width="22%"
			defaultValue="E"/>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.expedicao.pesquisarConhecimentoAction.findCliente"
			dataType="text"
			property="clienteByIdClienteRemetente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="remetente"
			size="20"
			maxLength="20"
			labelWidth="15%"
			width="48%">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteRemetente.pessoa.nmPessoa"
				size="30"
				maxLength="30"
				disabled="true"
				serializable="false"/>
		</adsm:lookup>
		
		<adsm:textbox
			property="nrDocumentoEletronico"
			label="numeroNfse"
			dataType="integer"
			size="10"
			maxLength="8"
			labelWidth="15%"
			width="22%"
			mask="00000000"/>

		<adsm:lookup action="/vendas/manterDadosIdentificacao"
			service="lms.expedicao.pesquisarConhecimentoAction.findCliente"
			dataType="text" property="clienteByIdClienteDestinatario"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="destinatario"
			size="20"
			maxLength="20"
			labelWidth="15%"
			width="85%">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteDestinatario.pessoa.nmPessoa"
				size="30"
				maxLength="30"
				disabled="true"
				serializable="false"/>
		</adsm:lookup>

		<adsm:range label="periodoEmissao" labelWidth="15%" width="48%" maxInterval="15">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" smallerThan="dtEmissaoFinal"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" biggerThan="dtEmissaoInicial"/>
		</adsm:range>
		<adsm:textbox
			property="vlTotalDocServico"
			label="valor"
			dataType="currency"
			mask="###,###,###,##0.00"
			size="10"
			maxLength="18"
			labelWidth="15%"
			width="22%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				caption="consultar" 
				id="__buttonBar:0.findButton" 
				disabled="false" 
				onclick="validateRestriction();"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		idProperty="idDoctoServico"
		property="conhecimento"
		gridHeight="200"
		service="lms.expedicao.pesquisarConhecimentoAction.findConhecimentoPaginated"
		rowCountService="lms.expedicao.pesquisarConhecimentoAction.getRowCountConhecimento"
		unique="true"
		rows="9"
		selectionMode="none">
		<adsm:gridColumn title="numero" property="nrDocumentoServico" align="center" width="85"/>
		<adsm:gridColumn title="dataHoraEmissao" dataType="JTDateTimeZone" property="dhEmissao" width="120"/>
		<adsm:gridColumn title="filialDestino" property="sgFilialDestino" width="78"/>
		<adsm:gridColumn title="valor" dataType="currency" property="vlTotalDocServico" width="80"/>
		<adsm:gridColumn title="situacao" property="tpSituacaoConhecimento" isDomain="true" width="60"/>
		<adsm:gridColumn title="remetente" property="clienteByIdClienteRemetente.pessoa.nmPessoa" width="165"/>
		<adsm:gridColumn title="destinatario" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" width="165"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript" src="../lib/expedicao.js"></script>
<script type="text/javascript">

	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);		
		getDadosFilial();
		
		/* LMS-4102 Habilitar este campo somente para documentos do tipo "NTE" ou "NSE". */
		var tpDocumentoServico = getElementValue('tpDocumentoServico');
		if('NTE' == tpDocumentoServico || 'NSE' == tpDocumentoServico){
			setDisabled("nrDocumentoEletronico", false);
		} else {
			setDisabled("nrDocumentoEletronico", true);
		}
		
	}

	var sgFilialRecebida = '';
	var nmFilialRecebida = '';
	
	function getDadosFilial() {
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		var idFilialOrigem = getElementValue('filialByIdFilialOrigem.idFilial');
		var nmFantasia = getElementValue('filialByIdFilialOrigem.pessoa.nmFantasia');
		if (isLookup && idFilialOrigem &&(!nmFantasia)) {
			document.getElementById('filialByIdFilialOrigem.sgFilial').masterLink='true';
			document.getElementById('filialByIdFilialOrigem.pessoa.nmFantasia').masterLink='true';			
			var sdo = createServiceDataObject("lms.expedicao.pesquisarConhecimentoAction.findFilialById", "getDadosFilial", {idFilialOrigem:idFilialOrigem});
	    	xmit({serviceDataObjects:[sdo]});				
		}	
	}
	
	function getDadosFilial_cb(data, error) {
		if (!error) {
			sgFilialRecebida = data.filial.sgFilial;
			nmFilialRecebida = data.filial.pessoa.nmFantasia;
			setElementValue('filialByIdFilialOrigem.sgFilial', data.filial.sgFilial);
			setElementValue('filialByIdFilialOrigem.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);
		}
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			setDisabled("__buttonBar:0.findButton", false);
		}
	}

  	function validateRestriction() {
		var dtEmissaoInicial = getElement("dtEmissaoInicial");
  		var dtEmissaoFinal = getElement("dtEmissaoFinal");
		var filialOrigem = getElement("filialByIdFilialOrigem.idFilial");
		var filialDestino = getElement("filialByIdFilialDestino.idFilial");
		var nrConhecimento = getElement("nrConhecimento");
		var nrNotaFiscal = getElement("nrNotaFiscal");
		var nrDocumentoEletronico = getElement("nrDocumentoEletronico");
		var clienteRemetente = getElement("clienteByIdClienteRemetente.idCliente");
		var clienteDestinatario = getElement("clienteByIdClienteDestinatario.idCliente");
		var tpSituacaoConhecimento = getElement("tpSituacaoConhecimento");
		var vlTotalDocServico = getElement("vlTotalDocServico");

		/* Combinacoes exigidas para Pesquisa: */
		var canExecute = (getElementValue(vlTotalDocServico) != "");
		
		canExecute =  (getElementValue(filialOrigem) != "" && getElementValue(nrDocumentoEletronico) != "");
		
		if (!canExecute) {
			/* Se Periodo eh Valido */
			if (getElementValue(dtEmissaoInicial) != "" && getElementValue(dtEmissaoFinal) != "") {
				canExecute = (getElementValue(filialOrigem) != "" 
							|| getElementValue(filialDestino) != ""
							|| getElementValue(clienteRemetente) != ""
							|| getElementValue(clienteDestinatario) != ""
							|| getElementValue(nrNotaFiscal) != ""
							|| getElementValue(tpSituacaoConhecimento) != "");
			} else {
				canExecute =  (getElementValue(filialOrigem) != "" && getElementValue(nrConhecimento) != "");
			}
		}
		/* Verifica se pode Executar a Pesquisa */
		if (!canExecute) {
			var params = new Array(filialOrigem.label, nrConhecimento.label
								  ,filialOrigem.label, dtEmissaoInicial.label
								  ,filialDestino.label, dtEmissaoInicial.label
								  ,clienteRemetente.label, dtEmissaoInicial.label
								  ,clienteDestinatario.label, dtEmissaoInicial.label
								  ,nrNotaFiscal.label, dtEmissaoInicial.label
								  ,tpSituacaoConhecimento.label, dtEmissaoInicial.label
								  ,filialOrigem.label, nrDocumentoEletronico.label
								  ,vlTotalDocServico.label);

			alertI18nMessage("LMS-04425", params, false);
			return false;
		}
		findButtonScript('conhecimento', document.forms[0]);
	}
</script>