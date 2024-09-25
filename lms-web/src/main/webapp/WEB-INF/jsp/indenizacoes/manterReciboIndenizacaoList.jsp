<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.manterReciboIndenizacaoAction" onPageLoad="pageLoad" onPageLoadCallBack="pageLoad">
	<adsm:form action="/indenizacoes/manterReciboIndenizacao"  height="169" idProperty="idReciboIndenizacao">

		<adsm:hidden property="restringeCancelados" />
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>

		<adsm:lookup label="filial" labelWidth="20%" width="80%"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
        
		<adsm:textbox label="numeroRIM" property="nrReciboIndenizacao" dataType="integer" width="80%" labelWidth="20%" mask="00000000"/>
		
        <adsm:lookup label="filialDebitada" labelWidth="20%" width="80%"
		             property="filialDebitada"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filialDebitada.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filialDebitada.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
        
   		<adsm:combobox label="tipoIndenizacao" property="tpIndenizacao" domain="DM_TIPO_INDENIZACAO" required="false" labelWidth="20%" width="80%" renderOptions="true"/>
        
		<adsm:lookup label="processoSinistro" 
					property="processoSinistro" 
					idProperty="idProcessoSinistro" 
					criteriaProperty="nrProcessoSinistro"
					disabled="false"
					picker="true" 	
					popupLabel="pesquisarProcessoSinistro"				
					dataType="text"	
					onPopupSetValue="onProcessoSinistroPopupSetValue"
					action="/seguros/manterProcessosSinistro" cmd="list"
					service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupProcessoSinistro"
					width="30%" labelWidth="20%">
					
			<adsm:propertyMapping relatedProperty="tipoSeguro.idTipoSeguro" modelProperty="processoSinistro.tipoSeguro.idTipoSeguro"/>		
		</adsm:lookup>

		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>		
		<adsm:lookup dataType="text" property="naoConformidade.filial"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilial" action="/municipios/manterFiliais" 
					 onchange="return sgFilialNaoConformidadeOnChangeHandler();"  onDataLoadCallBack="onFilialRNCDataLoadCallback"
					 popupLabel="pesquisarFilial"
					 label="naoConformidade" labelWidth="15%" width="35%" size="3" maxLength="3" picker="false" serializable="false">
		        <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
				<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:lookup dataType="integer" property="naoConformidade" idProperty="idNaoConformidade" criteriaProperty="nrNaoConformidade"
						 action="/rnc/manterNaoConformidade" service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupNaoConformidade" 
						 onDataLoadCallBack="onNaoConformidadeDataLoadCallback" onPopupSetValue="onNaoConformidadePopupSetValue" popupLabel="pesquisarNaoConformidade"
						 exactMatch="false" size="15" maxLength="8" required="false" mask="00000000" disabled="true"> 
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="naoConformidade.filial.sgFilial" blankFill="false"/>
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="naoConformidade.filial.sgFilial" />
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="naoConformidade.filial.idFilial"/>
			</adsm:lookup>
		</adsm:lookup>		
		
		
		<adsm:combobox property="tipoSeguro.idTipoSeguro" label="tipoSeguro" optionLabelProperty="sgTipo" optionProperty="idTipoSeguro" service="lms.indenizacoes.manterReciboIndenizacaoAction.findComboTipoSeguro" labelWidth="20%" width="30%" required="false"/>

		<adsm:combobox property="ocorrenciaNaoConformidade.motivoAberturaNc.idMotivoAberturaNc" 
				   label="motivoAbertura"  optionLabelProperty="dsMotivoAbertura" optionProperty="idMotivoAberturaNc" boxWidth="150" 
				   labelWidth="15%" width="35%" service="lms.indenizacoes.manterReciboIndenizacaoAction.findComboMotivoAbertura">
			<%--adsm:propertyMapping criteriaProperty="naoConformidade.idNaoConformidade" modelProperty="idNaoConformidade"/--%>
		</adsm:combobox>
	
		
		<adsm:combobox property="doctoServico.tpDocumentoServico" 
					   service="lms.indenizacoes.manterReciboIndenizacaoAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   label="documentoServico" labelWidth="20%" width="80%" serializable="true"
					   required="false"
					   onchange="return onTpDoctoServicoChange(this);">

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action=""
						 popupLabel="pesquisarFilial" 
						 size="3" maxLength="3" picker="false" disabled="true" serializable="true"
 						 onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						  }); "/>

			<adsm:lookup dataType="integer"
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 
						 mask="00000000"
						 popupLabel="pesquisarDocumentoServico"
						 onDataLoadCallBack="retornoDocumentoServico"
						 size="10" serializable="true" disabled="true" />

		</adsm:combobox>
						
		<adsm:lookup label="remetente" dataType="text" property="clienteRemetente" idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="20%" width="80%">
			<adsm:propertyMapping relatedProperty="clienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="clienteRemetente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>		

		<adsm:lookup label="destinatario" dataType="text" property="clienteDestinatario" idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="20%" width="80%">
			<adsm:propertyMapping relatedProperty="clienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="clienteDestinatario.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>				
  
		<adsm:range label="periodo" width="80%" labelWidth="20%">
			<adsm:textbox property="periodoInicial" dataType="JTDate" />
			<adsm:textbox property="periodoFinal" dataType="JTDate"/>
		</adsm:range>
		
		<adsm:combobox property="tpStatusIndenizacao" 
					   label="statusRecibo"
					   domain="DM_STATUS_INDENIZACAO"  
					   labelWidth="20%" 
					   width="80%" />
			
        <%-- ============================================================ --%>
		<adsm:section caption="beneficiario"/>
		<adsm:lookup label="filial" labelWidth="20%" width="80%"
		             property="filialBeneficiada"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filialBeneficiada.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filialBeneficiada.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="67" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup label="cliente" dataType="text" property="clienteBeneficiario" idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="20%" width="80%">
			<adsm:propertyMapping relatedProperty="clienteBeneficiario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="clienteBeneficiario.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>
		
		<adsm:lookup label="terceiro" dataType="text" property="beneficiarioTerceiro" idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="nrIdentificacaoFormatado"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas" 
					 exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="20%" width="80%" disabled="false">
			<adsm:propertyMapping relatedProperty="beneficiarioTerceiro.nmPessoa" modelProperty="nmPessoa"/>
			<adsm:textbox dataType="text" property="beneficiarioTerceiro.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>
		
        <%-- ============================================================ --%>		
		<adsm:section caption="favorecido"/>		
		<adsm:lookup label="filial" labelWidth="20%" width="80%"
		             property="filialFavorecida"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filialFavorecida.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filialFavorecida.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="67" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup label="cliente" dataType="text" property="clienteFavorecido" idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupCliente" action="/vendas/manterDadosIdentificacao"
					 exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="20%" width="80%">
			<adsm:propertyMapping relatedProperty="clienteFavorecido.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="clienteFavorecido.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>		

		<adsm:lookup label="terceiro" dataType="text" property="favorecidoTerceiro" idProperty="idPessoa" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="nrIdentificacaoFormatado"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas" 
					 exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="20%" width="80%" disabled="false">
			<adsm:propertyMapping relatedProperty="favorecidoTerceiro.nmPessoa" modelProperty="nmPessoa"/>
			<adsm:textbox dataType="text" property="favorecidoTerceiro.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>
		
        <%-- ============================================================ --%>		
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="reciboIndenizacao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="reciboIndenizacao" 
				idProperty="idReciboIndenizacao" 
				service="lms.indenizacoes.manterReciboIndenizacaoAction.findPaginatedReciboIndenizacao"
				rowCountService="lms.indenizacoes.manterReciboIndenizacaoAction.getRowCountReciboIndenizacao"
				selectionMode="none" 
				rows="20" 
				unique="true" 
				scrollBars="both" 
				onRowClick="rowClick"
				gridHeight="129">
		<adsm:gridColumnGroup separatorType="RNC">
			<adsm:gridColumn property="sgFilialReciboIndenizacao"     title="numeroRIM" dataType="text"    width="30"/>	
			<adsm:gridColumn property="nrReciboIndenizacao"           title=""          dataType="integer" width="80" mask="00000000"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="dtGeracao"                   title="dataGeracao"     dataType="JTDate" width="135" align="center"/>
		<adsm:gridColumn property="tpIndenizacao"               title="tipoIndenizacao" dataType="text"   width="140"/>
		<adsm:gridColumn property="nrIdentificacaoBeneficiario" title="beneficiario" dataType="text"      width="100"/>
		<adsm:gridColumn property="nmBeneficiario"              title="" dataType="text"      width="300"/>
		<adsm:gridColumn property="sgSimboloVlIndenizado"     title="valorIndenizado" width="70"  align="left"                     />
		<adsm:gridColumn property="vlIndenizado"              title=""                width="100" align="right" dataType="currency" />
		<adsm:gridColumn property="tpStatusIndenizacao" title="statusRecibo" dataType="text" width="130"/>

	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />	
	</adsm:buttonBar>
</adsm:window>

<script><!--

	document.getElementById('restringeCancelados').masterLink='true';
	setDisabled('doctoServico.filialByIdFilialOrigem.idFilial', true);
	setDisabled('doctoServico.idDoctoServico', true);

	function getDadosFilial() {
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		var idFilial = getElementValue('filial.idFilial');
		var nmFantasia = getElementValue('filial.pessoa.nmFantasia');
		if (isLookup && idFilial &&(!nmFantasia)) {			
			document.getElementById('filial.sgFilial').masterLink='true';
			document.getElementById('filial.pessoa.nmFantasia').masterLink='true';
			var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.findFilialById", "getDadosFilial", {idFilial:idFilial});
	    	xmit({serviceDataObjects:[sdo]});				
		}	
	}
	
	function getDadosFilial_cb(data, error) {
		if (!error) {
			sgFilialRecebida = data.filial.sgFilial;
			nmFilialRecebida = data.filial.pessoa.nmFantasia;
			setElementValue('filial.sgFilial', data.filial.sgFilial);
			setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);
		}
	}
	
	function onTpDoctoServicoChange(elemento) {
   		var r = changeDocumentWidgetType({
 		   		documentTypeElement:elemento, 
		   		filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
		   		documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
		   		documentGroup:'SERVICE',
		   		actionService:'lms.indenizacoes.manterReciboIndenizacaoAction'
		   	});
		   	
	   if (getElementValue('doctoServico.tpDocumentoServico')=='CTR' || getElementValue('doctoServico.tpDocumentoServico')=='NFT') {
	       var pms = document.getElementById('doctoServico.idDoctoServico').propertyMappings;
	       pms[pms.length] = { modelProperty:"tpDocumentoServico", criteriaProperty:"doctoServico.tpDocumentoServico", inlineQuery:false };
       }
	   
	   return r;
	}
	
	function pageLoad() {
		var url = new URL(parent.location.href);
		var idProcessoWorkflow = url.parameters["idProcessoWorkflow"];

		if (idProcessoWorkflow!=null) {
			setDisabled(document, true);
			tabSetDisabled('pesq', true);
			tabSetDisabled('cad', false);
			getTabGroup(this.document).selectTab("cad", "tudoMenosNulo", true);
		} else {
			onPageLoad();
			showHideCloseButton();
		}
	}
	
	function pageLoad_cb(data, error) {
		onPageLoad_cb(data,error);
		getDadosFilial();
	}	
	function showHideCloseButton() {
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (isLookup) {
			setDisabled('btnFechar',false);
		} else {
			setVisibility('btnFechar', false);
		}	
	}
	
	function pessoaCallback_cb(data, error) {
		if (error != undefined){
			alert(error);
			cleanButtonScript(this.document);
	
			setElementValue("pessoa.tpIdentificacao","");
			setElementValue("pessoa.nrIdentificacao","");
			setElementValue("pessoa.idPessoa","");
			setElementValue("pessoa.nmPessoa","");
		}
	
	   	// Se Pessoa cadastrada
	   	if (data.idPessoa != undefined) {
		   	setElementValue("pessoa.tpIdentificacao",data.tpIdentificacao);
		   	setElementValue("pessoa.nrIdentificacao",data.nrIdentificacao);
			setElementValue("pessoa.idPessoa",data.idPessoa);
			setElementValue("pessoa.nmPessoa",data.nmPessoa);
		}
	}		
	
	function onProcessoSinistroPopupSetValue(data, error) {
		if (data!=undefined) {			
			if (data.idProcessoSinistro!=undefined) {
				var vet = new Array();				
				vet.idProcessoSinistro = data.idProcessoSinistro;
			    var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.findLookupProcessoSinistro", "processoSinistro_nrProcessoSinistro_exactMatch", vet);
			    xmit({serviceDataObjects:[sdo]});
			}
		}
	}	
	
	/****************************************************************************
 	 * Init Window
	 ****************************************************************************/
	function initWindow(e) {
	
		if (e.name=='cleanButton_click') { 
			setDisabled('doctoServico.filialByIdFilialOrigem.idFilial', true);
			setDisabled('doctoServico.idDoctoServico', true);
			disableNrNaoConformidade(true);						
		
		} else if (e.name=='gridRow_click') {
			tabSetDisabled('cad', false);
		}
	}
	
	function rowClick() {
		tabSetDisabled('cad', false);
		return true;
	}

	/****************************************************************************
 	 * Funções específicas da tag de RNC
	 ****************************************************************************/
	function sgFilialNaoConformidadeOnChangeHandler() {
		if (getElementValue("naoConformidade.filial.sgFilial")=="") {
			disableNrNaoConformidade(true);
			// limpaTodosDados();
		} else {
			disableNrNaoConformidade(false);
		}
		return lookupChange({e:document.forms[0].elements["naoConformidade.filial.idFilial"]});
	}
	
	function onNaoConformidadePopupSetValue(data){
		if (data.nrNaoConformidade!=undefined) {
			disableNrNaoConformidade(false);
		} else {
			disableNrNaoConformidade(true);
		}
	}
	
	function disableNrNaoConformidade(disable) {
		setDisabled("naoConformidade.idNaoConformidade", disable);
	}
	
	function onFilialRNCDataLoadCallback_cb(data, error) {
		if (data.length==0) {
			disableNrNaoConformidade(false);
		}
		return lookupExactMatch({e:document.getElementById("naoConformidade.filial.idFilial"), data:data});
	}
	
	function onNaoConformidadeDataLoadCallback_cb(data, error) {
		naoConformidade_nrNaoConformidade_exactMatch_cb(data);
		if (data[0]!=undefined) {
			document.getElementById("naoConformidade.filial.sgFilial").value=data[0].filial.sgFilial;
		}
	}
	
	
	/****************************************************************************
 	 * Funções específicas da tag de DocumentoServico
	 ****************************************************************************/

	function retornoDocumentoServico_cb(data, error) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
	}
	
	function onTabShow(fromTab) {
		tabSetDisabled('cad', true);
		tabSetDisabled('mda', true);
		tabSetDisabled('parcelas', true);
		tabSetDisabled('debitadas', true);			
		tabSetDisabled('documentos', true);
		tabSetDisabled('anexo', true);
		tabSetDisabled('eventos', true);		
	}
		
	function tabSetDisabled(tab, disable) {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup != null) {
			tabGroup.setDisabledTab(tab, disable);  
	    }          
	}
	
	function populaClienteFavorecido_cb(data, error){
		if (error){
			alert(error);
			return false;
		}
		if(data[0]){
			setElementValue("clienteFavorecido.idPessoa", data[0].idPessoa);
			setElementValue("clienteFavorecido.pessoa.nmPessoa", data[0].nmPessoa);
			setElementValue("clienteFavorecido.pessoa.nrIdentificacao", data[0].nrIdentificacaoFormatado);
		}
	}	
</script>