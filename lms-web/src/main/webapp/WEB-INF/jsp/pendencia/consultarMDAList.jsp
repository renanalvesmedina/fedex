<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.pendencia.consultarMDAAction">
	<adsm:form action="/pendencia/consultarMDA" height="168">
	
		<!-- criterias da tela de 'Consultar localizacoes mercadorias' -->
		<adsm:hidden property="servico.tpModal"/>
		<adsm:hidden property="servico.tpAbrangencia"/>
		<adsm:hidden property="servico.tipoServico.idTipoServico"/>
		<adsm:hidden property="finalidade"/>
		<adsm:hidden property="tpDocumentoServico" serializable="false"/>
		<adsm:hidden property="idFilialDoctoSer" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		
	
		<adsm:hidden property="origem"/>
	
		<adsm:range label="periodo" width="85%" maxInterval="15">
			<adsm:textbox dataType="JTDate" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDate" property="dataFinal" picker="true"/>
		</adsm:range>

		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial"
					 criteriaProperty="sgFilial" 
				 	 service="lms.pendencia.consultarMDAAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
					 label="mda" dataType="text" size="5" maxLength="3" 
					 labelWidth="15%" width="85%" picker="false">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
					 
			<adsm:textbox property="nrDoctoServico" dataType="integer" mask="00000000"
						  size="10" maxLength="10"/>
		</adsm:lookup>
	
		<adsm:combobox property="tpRemetenteMda" label="tipoRemetente"
					   domain="DM_TIPO_REMETENTE_MDA" renderOptions="true"
					   labelWidth="15%" width="85%"
					   onchange="habilitaCamposRemetente()"/>

 		<adsm:lookup property="clienteByIdClienteRemetente" idProperty="idCliente" 
 					 criteriaProperty="pessoa.nrIdentificacao" 
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 service="lms.pendencia.consultarMDAAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao"
					 label="remetente" dataType="text"
					 size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true">
			<adsm:propertyMapping relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />

			<adsm:textbox dataType="text" property="clienteByIdClienteRemetente.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" serializable="false" />			  
		</adsm:lookup>

		<adsm:combobox property="tpDestinatarioMda" label="tipoDestinatario" 
					   domain="DM_TIPO_DESTINATARIO_MDA" 
					   labelWidth="15%" width="85%"
					   onchange="habilitaCamposDestinatario()"/>

		<adsm:combobox property="setor" 
					   service="lms.pendencia.consultarMDAAction.findSetor" 
					   optionProperty="idSetor" optionLabelProperty="dsSetor" 
					   onlyActiveValues="true" disabled="true"
 					   label="setorDestino" labelWidth="15%" width="85%"/>					   

		<adsm:lookup property="filialByIdFilialDestino" idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
				 	 service="lms.pendencia.consultarMDAAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" 
					 label="filialDestino" dataType="text" size="5" maxLength="3" 
					 labelWidth="15%" width="85%" disabled="true">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
					 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" 
						  size="30" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>

 		<adsm:lookup property="clienteByIdClienteDestinatario" idProperty="idCliente" 
 					 criteriaProperty="pessoa.nrIdentificacao" 
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 service="lms.pendencia.consultarMDAAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao" 
					 label="destinatario" dataType="text" disabled="true"
					 size="20" maxLength="20" labelWidth="15%" width="85%">
			<adsm:propertyMapping relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />

			<adsm:textbox dataType="text" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" serializable="false" />			  
		</adsm:lookup>	
		
		<adsm:lookup property="clienteByIdClienteConsignatario" idProperty="idCliente"					  
					 criteriaProperty="pessoa.nrIdentificacao"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.pendencia.consultarMDAAction.findLookupCliente"
					 action="/vendas/manterDadosIdentificacao" 
					 label="consignatario" dataType="text" size="20" maxLength="20" 
					 labelWidth="15%" width="85%">
			<adsm:propertyMapping relatedProperty="clienteByIdClienteConsignatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			
			<adsm:textbox dataType="text" property="clienteByIdClienteConsignatario.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>			

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="btnConsultar" onclick="executaConsulta(this.form)" />
			<adsm:button caption="limpar" id="btnLimpar" onclick="limpaForm()"/>
		</adsm:buttonBar>
		
		<script>
			var lms_17039 = '<adsm:label key="LMS-17039"/>';
		</script>
	</adsm:form>
	
	<adsm:grid property="mda" idProperty="idDoctoServico" selectionMode="none" gridHeight="132" 
			   scrollBars="both" unique="true" onRowClick="populaForm" detailFrameName="mda"
			   service="lms.pendencia.consultarMDAAction.findPaginatedMda"
			   rowCountService="lms.pendencia.consultarMDAAction.getRowCountMda">

        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="mda" property="filialByIdFilialOrigem.sgFilial" width="30" />
            <adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="filialDestino" property="filialByIdFilialDestino.sgFilial" width="80" />
		<adsm:gridColumn title="emissao" property="dhEmissao" dataType="JTDateTimeZone" align="center" width="120"/>
		<adsm:gridColumn title="remetente" property="clienteByIdClienteRemetente.pessoa.nmPessoa" dataType="text" width="200" />
		<adsm:gridColumn title="destinatario" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" dataType="text" width="200" />
		<adsm:gridColumn title="consignatario" property="clienteByIdClienteConsignatario.pessoa.nmPessoa" dataType="text" width="200" />
		<adsm:gridColumn title="localizacao" property="localizacaoMercadoria.dsLocalizacaoMercadoria" width="250" />
		<adsm:gridColumn title="status" property="tpStatusMda.description" width="120" />		
		<adsm:gridColumn title="dataEntrega" property="dataEntrega" dataType="JTDateTimeZone" width="120" align="center" />
	
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />	
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">

	function isLookup() {
		var url = new URL(parent.location.href);
		var mode = url.parameters["mode"];
		if ((mode!=undefined) && (mode=="lookup")) return true;
		return false;
	}
	
	if (isLookup()) {
		setDisabled('btnFechar',false);
		setDisabled('btnConsultar', false);
		setDisabled('btnLimpar', false);
	} else {
		setVisibility('btnFechar', false);
	}	
	
	/**
	 * Função chamada quando a tela é inicializada
	 */
	function initWindow(eventObj) {
		setDisabled('btnConsultar', false);
		setDisabled('btnLimpar', false);
		if(eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("mda", true);
			tabGroup.setDisabledTab("itens", true);			
		}		
	}
		
	/**
	 * Função q popula o form de MDA e desabilita as tabs.
	 */
	function populaForm(id) {
		var tabGroup = getTabGroup(this.document);
		if(tabGroup != null) {
			tabGroup.setDisabledTab("mda", false);
			tabGroup.setDisabledTab("itens", false);
		}
	}
	
	/**
	 * Função que habilita / desabilita o campo de cliente remetente.
	 */
	function habilitaCamposRemetente() {
		if(getElementValue("tpRemetenteMda") == "F") {
			resetValue("clienteByIdClienteRemetente.idCliente");
			setDisabled("clienteByIdClienteRemetente.idCliente", true);
		} else if(getElementValue("tpRemetenteMda") == "C") {			
			setDisabled("clienteByIdClienteRemetente.idCliente", false);
		} else {
			resetValue("clienteByIdClienteRemetente.idCliente");
			setDisabled("clienteByIdClienteRemetente.idCliente", true);
		}
	}	 
		
	/**
	 * Função que habilita / desabilita os campos de cliente ou filial destinatario.
	 */
	function habilitaCamposDestinatario() {
		if(getElementValue("tpDestinatarioMda") == "F") {
			setDisabled("filialByIdFilialDestino.idFilial", false);
			setDisabled("setor", false);
			resetValue("clienteByIdClienteDestinatario.idCliente");
			setDisabled("clienteByIdClienteDestinatario.idCliente", true);
		} else if(getElementValue("tpDestinatarioMda") == "C") {			
			setDisabled("clienteByIdClienteDestinatario.idCliente", false);
			resetValue("filialByIdFilialDestino.idFilial");
			setDisabled("filialByIdFilialDestino.idFilial", true);
			resetValue("setor");			
			setDisabled("setor", true);
		} else {
			resetValue("clienteByIdClienteDestinatario.idCliente");
			setDisabled("clienteByIdClienteDestinatario.idCliente", true);
			resetValue("filialByIdFilialDestino.idFilial");
			setDisabled("filialByIdFilialDestino.idFilial", true);
			resetValue("setor");			
			setDisabled("setor", true);
		}
	}
	
	/**
	 * Executa a validação customizada do form.
	 * É necessário informar no mínimo um período de datas e um tipo de remetente, ou um MDA.
	 */
	function executaConsulta(form){
		if ((getElementValue('filialByIdFilialOrigem.idFilial')!="") 
				&& (getElementValue('nrDoctoServico')!="")){
			findButtonScript('mda', form);
		} else {
			if ((getElementValue('dataInicial')=="")
					|| (getElementValue('dataFinal')=="")
					|| (getElementValue('tpRemetenteMda')=="")
					|| (getElementValue('tpDestinatarioMda')=="")){
				alert(lms_17039);
				return false;
			}
			findButtonScript('mda', form);
		}
	}
	
	function limpaForm(){
		cleanButtonScript(this.document);
		habilitaCamposRemetente();
		habilitaCamposDestinatario();
	}

</script>