<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.expedicao.manterCRTAction" onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04027"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/expedicao/manterCRT">

		<!-- criterias da tela de 'Consultar localizacoes mercadorias' -->
		<adsm:hidden property="servico.tpModal"/>
		<adsm:hidden property="servico.tpAbrangencia"/>
		<adsm:hidden property="servico.tipoServico.idTipoServico"/>
		<adsm:hidden property="finalidade"/>
		<adsm:hidden property="tpDocumentoServico" serializable="false"/>
		<adsm:hidden property="idFilialDoctoSer" serializable="false"/>
		<adsm:hidden property="sgPais"/>
		<adsm:hidden property="clienteByIdClienteConsignatario.idCliente"/>

		<adsm:lookup
			action="/municipios/manterFiliais"
			service="lms.expedicao.manterCRTAction.findFilialOrigem"
			dataType="text"
			property="filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialOrigem"
			size="3"
			maxLength="3"
			labelWidth="12%"
			width="35%">

		   	<adsm:propertyMapping
		   		modelProperty="pessoa.nmFantasia"
		   		relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>

            <adsm:textbox
            	dataType="text"
            	property="filialByIdFilialOrigem.pessoa.nmFantasia"
            	serializable="false"
            	size="30"
            	maxLength="60"
            	disabled="true"/>

        </adsm:lookup>

		<adsm:lookup
			action="/municipios/manterFiliais"
			service="lms.expedicao.manterCRTAction.findFilialDestino"
			dataType="text"
			property="filialByIdFilialDestino"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialDestino"
			size="3"
			maxLength="3"
			labelWidth="16%"
			width="37%">

		   	<adsm:propertyMapping
		   		modelProperty="pessoa.nmFantasia"
		   		formProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>

            <adsm:textbox
            	dataType="text"
            	property="filialByIdFilialDestino.pessoa.nmFantasia"
            	size="30"
            	serializable="false"
            	maxLength="45"
            	disabled="true"/>

        </adsm:lookup>

		<adsm:textbox
			property="nrCrt"
			dataType="integer"
			label="numeroCRT"
			size="6"
			serializable="true"
			maxLength="6"
			labelWidth="12%"
			width="35%"/>

		<adsm:range
			label="periodoEmissao"
			labelWidth="16%"
			width="37%">

	         <adsm:textbox
	         	dataType="JTDate"
	         	property="periodoEmissaoInicial"
	         	serializable="true"/>

	         <adsm:textbox
	         	dataType="JTDate"
	         	property="periodoEmissaoFinal"
	         	serializable="true"/>

        </adsm:range>

        <adsm:lookup
			label="remetente"
			property="clienteByIdClienteRemetente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.manterCRTAction.findRemetente"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			size="20"
			maxLength="20"
			labelWidth="12%"
			width="50%">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa" />

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteRemetente.pessoa.nmPessoa"
				size="35"
				maxLength="40"
				disabled="true" />
		</adsm:lookup>
		
		<adsm:lookup
			label="destinatario"
			property="clienteByIdClienteDestinatario"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.manterCRTAction.findDestinatario"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			size="20"
			maxLength="20"
			labelWidth="12%"
			width="50%">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa" />

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteDestinatario.pessoa.nmPessoa"
				size="35"
				maxLength="40"
				disabled="true" />
		</adsm:lookup>

		<adsm:buttonBar
			freeLayout="true">

			<adsm:findButton
				callbackProperty="ctoInternacional"/>
			<adsm:resetButton/>

		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid
		idProperty="idCtoInternacional"
		property="ctoInternacional"
		scrollBars="horizontal" 
		gridHeight="200"
		rowCountService="lms.expedicao.manterCRTAction.getRowCountCrt"
		unique="true">

		<adsm:gridColumn
			title="filialOrigem"
			property="filialByIdFilialOrigem.sgFilial"
			width="80" />

		<adsm:gridColumn
			title="numeroCRT"
			property="nrCrtFormatado"
			width="100" />

		<adsm:gridColumn
			title="filialDestino"
			property="filialByIdFilialDestino.sgFilial"
			width="80" />

		<adsm:gridColumn
			title="dataHoraEmissao"
			property="dhEmissao"
			dataType="JTDateTimeZone"
			width="120" />

		<adsm:gridColumn
			title="remetente"
			property="clienteByIdClienteRemetente.pessoa.nmPessoa"
			width="250" />

		<adsm:gridColumn
			title="destinatario"
			property="clienteByIdClienteDestinatario.pessoa.nmPessoa"
			width="250" />

		<adsm:gridColumn
			title="situacao"
			isDomain="true"
			property="tpSituacaoCrt"
			width="80" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>
<script type="text/javascript" src="../lib/expedicao.js"></script>
<script type="text/javascript">
	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);		
		getDadosFilial();
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
			var sdo = createServiceDataObject("lms.expedicao.manterCRTAction.findFilialById", "getDadosFilial", {idFilialOrigem:idFilialOrigem});
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

	function validateObligatorFields(){
		var periodoInicial = getElement('periodoEmissaoInicial');
		var periodoFinal = getElement('periodoEmissaoFinal');
		var filialOrigem = getElement('filialByIdFilialOrigem.idFilial')
		var filialDestino = getElement('filialByIdFilialDestino.idFilial')
		var nrCrt = getElement('nrCrt');
		var remetente = getElement('clienteByIdClienteRemetente.idCliente');
		var destinatario = getElement('clienteByIdClienteDestinatario.idCliente');

		if(getElementValue(nrCrt) != '' ||
		  (getElementValue(periodoInicial) != '' && getElementValue(periodoFinal) != '') &&
		  (getElementValue(filialOrigem) != '' ||
		   getElementValue(filialDestino) != '' ||
		   getElementValue(remetente) != '' ||
		   getElementValue(destinatario) != '')) {
			return true;
		}

		var params = [
			filialOrigem.label, periodoInicial.label
			,filialDestino.label, periodoInicial.label
			,nrCrt.label
			,remetente.label, periodoInicial.label
			,destinatario.label, periodoInicial.label];

		alertI18nMessage("LMS-04027", params, false);

		return false;
	}
	/************************************************************\
	*
	\************************************************************/
	function validateTab() {
		return (validateTabScript(document.forms) && validateObligatorFields());
	}
</script>