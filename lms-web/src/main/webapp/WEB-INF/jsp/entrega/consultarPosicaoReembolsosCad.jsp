<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form service="lms.entrega.consultarPosicaoReembolsosAction.findByIdPosicaoReembolso" onDataLoadCallBack="posicaoReembolsoDataLoad" action="/entrega/consultarPosicaoReembolsos" height="390" idProperty="idReciboReembolso">

		<adsm:textbox
			label="filialOrigemReembolso"
			property="filialByIdFilialOrigem.sgFilial"
			dataType="text"
			size="3"
			disabled="true"
			labelWidth="20%"
			width="30%"
		>
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="30" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox
			label="filialDestinoReembolso"
			property="filialByIdFilialDestino.sgFilial"
			dataType="text"
			size="3"
			disabled="true"
			labelWidth="20%"
			width="30%"
		>
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" size="30" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="doctoServicoByIdDoctoServReembolsado.tpDocumentoServico"/>
		<adsm:hidden property="doctoServicoByIdDoctoServReembolsado.idDoctoServico"/>
		<adsm:textbox
			label="documentoServico"
			property="doctoServicoByIdDoctoServReembolsado.tpDocumentoServico.description"
			labelWidth="20%" width="30%"
			dataType="text"
			size="10"
			disabled="true"
			serializable="false"
		>
			<adsm:textbox size="3" dataType="text" property="doctoServicoByIdDoctoServReembolsado.filialOrigem.sgFilial" disabled="true" serializable="false"/>
			<adsm:textbox size="10" dataType="integer" mask="00000000" property="doctoServicoByIdDoctoServReembolsado.nrDoctoServico" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox
			label="reembolso"
			property="tpDocumentoServico.description"
			dataType="text"
			labelWidth="20%"
			width="30%"
			size="10"
			disabled="true"
			serializable="false"
		>
			<adsm:textbox size="3" dataType="text" property="filialByIdFilialOrigem2.sgFilial" disabled="true" serializable="false"/>
			<adsm:textbox size="10" dataType="integer" mask="00000000" property="nrDoctoServico" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox dataType="JTDateTimeZone" property="dhEmissao" picker="false" label="emissao" size="18" maxLength="50" disabled="true" labelWidth="20%" width="30%" />

		<adsm:textbox
			label="valor"
			property="sgMoeda"
			dataType="text"
			size="10"
			disabled="true"
			labelWidth="20%"
			width="30%"
		>
			<adsm:textbox dataType="currency" property="vlReembolso" size="18" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox
			label="remetente"
			property="doctoServicoByIdDoctoServReembolsado.clienteByIdClienteRemetente.pessoa.nrIdentificacao"
			dataType="text"
			maxLength="50"
			disabled="true"
			size="20"
			labelWidth="20%"
			width="80%"
		>
			<adsm:textbox dataType="text" property="doctoServicoByIdDoctoServReembolsado.clienteByIdClienteRemetente.pessoa.nmPessoa" size="30" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox
			dataType="text"
			property="doctoServicoByIdDoctoServReembolsado.clienteByIdClienteDestinatario.pessoa.nrIdentificacao"
			label="destinatario"
			maxLength="50"
			disabled="true"
			size="20"
			labelWidth="20%"
			width="80%"
		>
			<adsm:textbox dataType="text" property="doctoServicoByIdDoctoServReembolsado.clienteByIdClienteDestinatario.pessoa.nmPessoa" size="30" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="tpSituacaoRecibo.description" label="situacao" size="50" maxLength="50" disabled="true" labelWidth="20%" width="80%" />

		<adsm:section caption="rastreamento" />

		<adsm:grid
			idProperty="idEvento"
			property="evento"
			rows="11"
			unique="true"
			showPagging="false"
			autoSearch="false"
			onDataLoadCallBack="eventoDataLoad"
			service="lms.entrega.consultarPosicaoReembolsosAction.findGridEvento"
			onRowClick="gridRowClick"
			selectionMode="none"
		>
			<adsm:gridColumn width="390" title="evento" property="dsEvento" />
			<adsm:gridColumn title="data" property="dhEvento" dataType="JTDateTimeZone"/>

			<adsm:gridColumnGroup separatorType="MANIFESTO">
				<adsm:gridColumn width="100" title="numeroDocumento" property="sgFilial" dataType="text"/>
				<adsm:gridColumn width="100" title="" property="nrDocumento" dataType="integer" mask="00000000" />
			</adsm:gridColumnGroup>
		</adsm:grid>

		<adsm:section caption="cheques" />

		<adsm:grid
			idProperty="idCheque"
			property="cheque"
			rows="6"
			unique="true"
			showPagging="false"
			autoSearch="false"
			service="lms.entrega.consultarPosicaoReembolsosAction.findGridCheque"
			onRowClick="gridRowClick"
			selectionMode="none"
			onDataLoadCallBack="chequeDataLoad"
		>
			<adsm:gridColumn width="130" title="banco" property="nrBanco" dataType="integer"/>
			<adsm:gridColumn width="130" title="agencia" property="nrAgencia" dataType="integer"/>			
			<adsm:gridColumn width="130" title="cheque" property="nrCheque" dataType="integer"/>
			<adsm:gridColumn width="130" title="data" property="dtCheque" dataType="JTDate"/>
			
			<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="valor" property="sgMoeda" width="40" />
				<adsm:gridColumn title="" property="dsSimbolo" width="40" />
			</adsm:gridColumnGroup>
			<adsm:gridColumn title="" width="130" property="vlCheque" dataType="currency" />
		</adsm:grid>

		<adsm:buttonBar>
			<adsm:button id="informacoesDocumentoServico" caption="informacoesDocumentoServico" action="/sim/consultarLocalizacoesMercadorias" cmd="main" >		
				<adsm:linkProperty src="doctoServicoByIdDoctoServReembolsado.idDoctoServico" target="idDoctoServicoConsulta"/>		
				<%--adsm:linkProperty src="doctoServicoByIdDoctoServReembolsado.tpDocumentoServico" target="doctoServico.tpDocumentoServico"/>
				<adsm:linkProperty src="doctoServicoByIdDoctoServReembolsado.filialOrigem.sgFilial" target="doctoServico.filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="doctoServicoByIdDoctoServReembolsado.nrDoctoServico" target="doctoServico.nrDoctoServico"/>
				<adsm:linkProperty src="doctoServicoByIdDoctoServReembolsado.idDoctoServico" target="doctoServico.idDoctoServico"/>
				<adsm:linkProperty src="doctoServicoByIdDoctoServReembolsado.idDoctoServico" target="idDoctoServicoReembolsado"/--%>	
			</adsm:button>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function initWindow(evt) {
		if (evt.name = 'tab_click') {
			eventoGridDef.resetGrid();
			chequeGridDef.resetGrid();
		}
	}

	function posicaoReembolsoDataLoad_cb(data) {
		onDataLoad_cb(data);
		var gridData = new Object();
		gridData.idReciboReembolso = data.idReciboReembolso;
		eventoGridDef.executeSearch(gridData);
		
	}

	function eventoDataLoad_cb(data) {
		findCheques();
	}

	function findCheques(idReciboReembolso) {
		var gridData = new Object();
		gridData.idReciboReembolso = getElementValue("idReciboReembolso");
		chequeGridDef.executeSearch(gridData);
	}

	function chequeDataLoad_cb(data) {
		setFocus("informacoesDocumentoServico", false);
	}

	function gridRowClick() {
		return false;
	}
</script>
