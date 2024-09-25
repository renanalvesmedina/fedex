<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<adsm:window service="lms.carregamento.manterGerarPreManifestoAction">
	<adsm:form idProperty="idDoctoServico" action="/carregamento/manterGerarPreManifesto"
			   service="lms.carregamento.manterGerarPreManifestoAction.findByIdDoctoServico">
		
		<adsm:hidden property="manifesto.idManifesto" />
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.sgFilial" />
		<adsm:hidden property="manifesto.nrPreManifesto" />
		
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.idFilial" />
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.sgFilial" />
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />		
		<adsm:hidden property="controleCarga.idControleCarga" />
		<adsm:hidden property="controleCarga.nrControleCarga" />
		<adsm:hidden property="origem" value="manterGerarPreManifesto"/>
	
		<adsm:masterLink idProperty="idManifesto" showSaveAll="true">			
			<adsm:masterLinkItem label="numeroPreManifesto" property="sgFilialNrPreManifesto" />
		</adsm:masterLink>
	</adsm:form>
	
	<adsm:grid idProperty="idPreManifestoDocumento" property="preManifestoDocumento" selectionMode="check" 
			   autoSearch="false" unique="true" scrollBars="both" gridHeight="285" rows="100"
			   service="lms.carregamento.manterGerarPreManifestoAction.findPaginatedPreManifestoDocumento"
			   rowCountService="lms.carregamento.manterGerarPreManifestoAction.getRowCountPreManifestoDocumento"
			   onRowClick="myOnRowClick" onDataLoadCallBack="myOnDataLoadCallBack">
		<adsm:editColumn field="hidden" property="idDoctoServico" dataType="text" title="hidden" width=""/>
		<adsm:gridColumn title="documentoServico" property="doctoServico.tpDocumentoServico" isDomain="true" width="30"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="doctoServico.filialByIdFilialOrigem.sgFilial" width="30" />
            <adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="destino" property="doctoServico.filialByIdFilialDestino.sgFilial" width="60" />
		<adsm:gridColumn title="volumes" property="doctoServico.qtVolumes" width="100" align="right" />
		<adsm:gridColumn title="peso" property="doctoServico.psReal" dataType="decimal" mask="#,###,##0.000" align="right" unit="kg" width="70"/>		
		
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valorMercadoria" property="doctoServico.moeda.sgMoeda" width="30" />		
			<adsm:gridColumn title="" property="doctoServico.moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="doctoServico.vlMercadoria" dataType="currency" mask="#,###,###,###,###,##0.00" width="80" align="right"/>	
					
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valorFrete" property="doctoServico.moeda2.sgMoeda" width="30" />		
			<adsm:gridColumn title="" property="doctoServico.moeda2.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="doctoServico.vlTotalDocServico" dataType="currency" mask="#,###,###,###,###,##0.00" width="80" align="right"/>				
		
		<adsm:gridColumn title="dpe" property="doctoServico.dtPrevEntrega" dataType="JTDate" width="100" align="center" />		
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="servico" property="doctoServico.servico.sgServico" width="30" />		
			<adsm:gridColumn title="" property="doctoServico.servico.tpModal" width="60" />
			<adsm:gridColumn title="" property="doctoServico.servico.tpAbrangencia" width="60" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="dataEmissao" property="doctoServico.dhEmissao" dataType="JTDateTimeZone" width="130" align="center"/>
		<adsm:gridColumn title="remetente" property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" width="180" />
		<adsm:gridColumn title="destinatario" property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" width="180" />
		<adsm:gridColumn title="localizacao" property="doctoServico.localizacaoMercadoria.dsLocalizacaoMercadoria" width="120" />
	
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="carregarVeiculo2" id="carregarVeiculo" action="carregamento/carregarVeiculo" cmd="main">
				<adsm:linkProperty src="controleCarga.filialByIdFilialOrigem.idFilial" target="controleCarga.filialByIdFilialOrigem.idFilial"/>
				<adsm:linkProperty src="controleCarga.filialByIdFilialOrigem.sgFilial" target="controleCarga.filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" target="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>
				<adsm:linkProperty src="controleCarga.idControleCarga" target="controleCarga.idControleCarga"/>
				<adsm:linkProperty src="controleCarga.nrControleCarga" target="controleCarga.nrControleCarga"/>
				<adsm:linkProperty src="origem" target="origem"/>
			</adsm:button>
			<adsm:button caption="adicionarDocumentoManual" id="adicionarDocumentoManual" onclick="popupAdicionarDocumentoManual();"/>			
			<adsm:button caption="adicionarDocumento" id="adicionarDocumento" onclick="popupAdicionarDocumento();"/>
			<adsm:button id="excluirButton" buttonType="removeButton" caption="excluirItem" onclick="excluirDocumento()" />
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">
	
	/**
	 * Função chamada quando a tela é carregada
	 */
	function initWindow(eventObj) {
		setDisabled("adicionarDocumentoManual", false);
		setDisabled("adicionarDocumento", false);
		
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		
		var idManifesto = tabDet.getFormProperty("idManifesto");
		var sgFilial = tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial");
		var nrPreManifesto = tabDet.getFormProperty("nrPreManifesto");
		setElementValue("manifesto.idManifesto", idManifesto);
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", sgFilial);
		setElementValue("manifesto.nrPreManifesto", nrPreManifesto);
			
		setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.idFilial"));
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial"));
		setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"));		
		setElementValue("controleCarga.idControleCarga", tabDet.getFormProperty("controleCarga.idControleCarga"));
		setElementValue("controleCarga.nrControleCarga", tabDet.getFormProperty("controleCarga.nrControleCarga"));
		
		if (tabDet.getFormProperty("solicitacaoRetirada.idSolicitacaoRetirada") != "") {
			preManifestoDocumentoGridDef.disabled = true;
		} else {
			preManifestoDocumentoGridDef.disabled = false;
		}
	}
	
	/**
	 * Função que retorna 'false' caso uma linha da grid tenha sido clicada.
	 */
	function myOnRowClick(id) {
		return false;
	}	
	
	/**
	 * Função chamada no retorno dos dados da grid.
	 */
	function myOnDataLoadCallBack_cb(data, error) {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		if (tabDet.getFormProperty("idManifesto") != "") {
			setDisabled("carregarVeiculo", false);
		} else {
			setDisabled("carregarVeiculo", true);
		}
	}
	
	/**
	 * Função que chama a pop-up de adicionar documentos manual.
	 */
	function popupAdicionarDocumentoManual() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");		
		if(tabDet.validate(tabDet.getElementById("idManifesto").form)) {
			showModalDialog('carregamento/manterGerarPreManifesto.do?cmd=man',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:680px;dialogHeight:147px;');
			return true;
		}
		return false;
	}
	
	/**
	 * Função que chama a pop-up de adicionar documentos.
	 */
	function popupAdicionarDocumento() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");		
		if(tabDet.validate(tabDet.getElementById("idManifesto").form)) {
			showModalDialog('carregamento/manterGerarPreManifestoAdicionarDocumentos.do?cmd=list',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:780px;dialogHeight:540px;');
			return true;
		}
		return false;
	}
	
	/**
	 * Função que retira os documentos da sessão e caso já exista um manifesto gravado, chama o store button.
	 */	 
	function excluirDocumento() {
		var mapCriteria = new Array();	   
		setNestedBeanPropertyValue(mapCriteria, "masterId", getElementValue("manifesto.idManifesto"));
		setNestedBeanPropertyValue(mapCriteria, "ids", preManifestoDocumentoGridDef.getSelectedIds().ids);
		var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.validateRemoveDocumentosPreManifesto", "validateRemoveDocumentosPreManifesto", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da validação da remoção de documentos.
	 */
	function validateRemoveDocumentosPreManifesto_cb(data, error) {
		if (!error) {
			preManifestoDocumentoGridDef.removeByIds("lms.carregamento.manterGerarPreManifestoAction.removeByIdsPreManifestoDocumento", "removeByIds");
		} else {
			alert(error);
		}		
	}
	
	/**
	 * Função de callback da exclusão do documento
	 */
	function removeByIds_cb(data, error) {
		if(!error) {			
			var tabGroup = getTabGroup(this.document);
			var tabDet = tabGroup.getTab("cad"); 
			if(getElementValue(tabDet.getElementById("idManifesto")) != "") {
				saveAllButtonScript("storeButton");
			}
			preManifestoDocumentoGridDef.executeSearch();	
		} else {
			alert(error);
		}
	}
	
</script>