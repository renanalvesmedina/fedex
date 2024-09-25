<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<adsm:window service="lms.recepcaodescarga.iniciarDescargaAction">
	<adsm:form idProperty="idIntegranteEqOperac" action="/recepcaoDescarga/iniciarDescarga" height="150"
			   service="lms.recepcaodescarga.iniciarDescargaAction.findByIdIntegranteEqOperac" onDataLoadCallBack="enableFields">
	
		<adsm:hidden property="pessoa.tpPessoa" value="F" />
		<adsm:hidden property="pessoa.tpIdentificacao" />
		<adsm:hidden property="tpSituacao" value="A" />
		<adsm:hidden property="filialSessao.idFilial" />
		<adsm:hidden property="filialSessao.sgFilial" />
		<adsm:hidden property="filialSessao.pessoa.nmFantasia" />
	
		<adsm:masterLink idProperty="idEquipeOperacao" showSaveAll="false">
			<adsm:masterLinkItem label="controleCargas" property="controleCarga.nrControleCarga" itemWidth="50" />
			<adsm:masterLinkItem label="filial" property="sgFilial" itemWidth="50" />
			<adsm:masterLinkItem label="postoAvancado" property="sgPostoAvancado" itemWidth="50" />
			<adsm:masterLinkItem label="equipe" property="equipe.dsEquipe" itemWidth="50" />
			<adsm:hidden property="idEquipe"/>
		</adsm:masterLink>
		
		<adsm:combobox property="tpIntegrante" domain="DM_INTEGRANTE_EQUIPE" label="contratacao" renderOptions="true"
					   width="85%" onchange="return comboTpIntegranteOnChange(this.value)" required="true"/>
		
		<adsm:hidden property="usuario.dsFuncao"/>
		<adsm:lookup property="usuario" idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 action="/configuracoes/consultarFuncionariosView"
					 service="lms.recepcaodescarga.iniciarDescargaAction.findLookupUsuarioFuncionario" 
					 dataType="text" label="funcionario" size="16" 
					 maxLength="16" width="85%" exactMatch="true" disabled="true">
				<adsm:propertyMapping criteriaProperty="filialSessao.idFilial" modelProperty="filial.idFilial" disable="true"/>
				<adsm:propertyMapping criteriaProperty="filialSessao.sgFilial" modelProperty="filial.sgFilial" disable="true"/>
				<adsm:propertyMapping criteriaProperty="filialSessao.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" disable="true"/>				
				<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>
				<adsm:propertyMapping relatedProperty="usuario.dsFuncao" modelProperty="dsFuncao"/>
 				<adsm:propertyMapping modelProperty="nmUsuario" relatedProperty="usuario.nmUsuario"/>													  
			<adsm:textbox property="usuario.nmUsuario" dataType="text" 
						  size="60" maxLength="60" disabled="true"/>
		</adsm:lookup>	

		<adsm:lookup property="pessoa" dataType="text" idProperty="idPessoa" 
			criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="nrIdentificacaoFormatado"
			service="lms.recepcaodescarga.iniciarDescargaAction.findLookupPessoa" action="/configuracoes/manterPessoas" 
			label="integrante" size="20" maxLength="20" width="85%" disabled="true" >
			<adsm:propertyMapping relatedProperty="pessoa.nmPessoa" modelProperty="nmPessoa" />	
			<adsm:propertyMapping relatedProperty="pessoa.tpIdentificacao" modelProperty="tpIdentificacao.value" />	
			
			<adsm:propertyMapping criteriaProperty="pessoa.tpPessoa" modelProperty="pessoa.tpPessoa" />		
			<adsm:propertyMapping criteriaProperty="pessoa.nmPessoa" modelProperty="nmPessoa" disable="false"/>	
			<adsm:propertyMapping criteriaProperty="pessoa.tpIdentificacao" modelProperty="pessoa.tpIdentificacao" disable="false"/>	
			<adsm:propertyMapping criteriaProperty="pessoa.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacao" disable="false"/>
			
			<adsm:textbox dataType="text" property="pessoa.nmPessoa" size="50" maxLength="50" disabled="true" />
		</adsm:lookup>

		<adsm:combobox property="cargoOperacional.idCargoOperacional" 
					   optionLabelProperty="dsCargo" onlyActiveValues="true"
					   optionProperty="idCargoOperacional"
					   service="lms.recepcaodescarga.iniciarDescargaAction.findCargoOperacional"
					   label="cargo" width="85%" disabled="true"/>

		<adsm:lookup label="empresa" idProperty="idEmpresa" property="empresa" 
					 dataType="text" criteriaProperty="pessoa.nmPessoa"
					 action="/municipios/manterEmpresas" 
					 service="lms.recepcaodescarga.iniciarDescargaAction.findLookupEmpresa" 					 
					 exactMatch="false" minLengthForAutoPopUpSearch="3" size="50"
					 maxLength="50" width="85%" disabled="true">
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarIntegrante" id="salvarIntegranteButton" 
						 	  service="lms.recepcaodescarga.iniciarDescargaAction.saveIntegranteEqOperac"/>
			<adsm:button caption="limpar" id="novoIntegranteButton" onclick="novoIntegranteForm();"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="integranteEqOperac" idProperty="idIntegranteEqOperac" gridHeight="170" unique="true" 
			   rows="7" autoSearch="false" detailFrameName="integrantes" defaultOrder="cargoOperacional_.dsCargo:asc"
			   service="lms.recepcaodescarga.iniciarDescargaAction.findPaginatedIntegranteEqOperac" 
			   rowCountService="lms.recepcaodescarga.iniciarDescargaAction.getRowCountIntegranteEqOperac">
		<adsm:gridColumn property="nmIntegranteEquipe" title="nome" width="25%" />
		<adsm:gridColumn property="usuario.nrMatricula" title="matricula" width="12%" align="right" />
		<adsm:gridColumn property="cargoOperacional.dsCargo" title="cargo" width="15%" /> 
		<adsm:gridColumn property="tpIntegrante" dataType="text" title="contratacao" width="15%" isDomain="true"/>
		<adsm:gridColumn property="pessoa.nrIdentificacaoFormatado" title="cpf" width="15%" align="right" />
		<adsm:gridColumn property="empresa.pessoa.nmPessoa" title="empresa" width="18%" />
		<adsm:buttonBar>
			<adsm:removeButton id="excluirButton" caption="excluirIntegrante" service="lms.recepcaodescarga.iniciarDescargaAction.removeByIdsIntegranteEqOperac"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">

	// Pega parâmetros da tela pai.
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("recepcaoDescarga");

	/**
	 * Função chamada ao iniciar a página.
	 */
	function initWindow(eventObj) {
		disableButtons();
		setaDadosSessao();
		
		if(eventObj.name == "storeItemButton" || eventObj.name == "removeButton_grid") {
			disableAllItems(true);
		} else  if (eventObj.name == "tab_click") {
			novoIntegranteForm();
		}
	}
	
	/**
	 * Carrega os dados basicos da tela
	 */
	function setaDadosSessao() {
		setElementValue("filialSessao.idFilial", tabDet.getFormProperty("idFilial"));
		setElementValue("filialSessao.sgFilial", tabDet.getFormProperty("sgFilial"));
		setElementValue("filialSessao.pessoa.nmFantasia", tabDet.getFormProperty("nmFilial"));
	}	
		
	/**
	 * Verifica qual o valor no qual a combo de tpIntegrante esta setado
	 * Apartir disto habilita ou desabilita determinados campos.
	 *
	 * @param value valor atual da combo
	 */
	function comboTpIntegranteOnChange(value) {	
		newScreen(value);		
		checkDisableItems(value);
		
		return true;
	}
	
	/**
	 * Valida quais serao os campos que serao disabilitados ou nao
	 *
	 * @param value 'F' (Funcionario) ou 'T' (Terceiro)
	 */
	function checkDisableItems(value) {
		if (value=="F") {
			setDisabled("usuario.idUsuario", false);
			setDisabled("pessoa.idPessoa", true);
			setDisabled("cargoOperacional.idCargoOperacional", true);
			setDisabled("empresa.idEmpresa", true);
			
			document.getElementById("usuario.nrMatricula").required='true';
			document.getElementById("pessoa.pessoa.nrIdentificacao").required='false';
			document.getElementById("empresa.pessoa.nmPessoa").required='false';
			document.getElementById("cargoOperacional.idCargoOperacional").required='false';
		} else if (value=="T") {
			setDisabled("usuario.idUsuario", true);
			setDisabled("pessoa.idPessoa", false);
			setDisabled("cargoOperacional.idCargoOperacional", false);
			setDisabled("empresa.idEmpresa", false);
			
			document.getElementById("usuario.nrMatricula").required='false';
			document.getElementById("pessoa.pessoa.nrIdentificacao").required='true';
			document.getElementById("empresa.pessoa.nmPessoa").required='true';
			document.getElementById("cargoOperacional.idCargoOperacional").required='true';
		} else {
			disableAllItems(true);
		}
	}	

	/**
	 * Deixa a tela em seu estado inicial
	 */
	function novoIntegranteForm(){
		newScreen();
		disableAllItems(true);
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * Desabilita todos os 'possiveis' elementos da tela
	 *
	 * @param disable
	 */
	function disableAllItems(disable){
		setDisabled("usuario.idUsuario", disable);		
		setDisabled("pessoa.idPessoa", disable);
		setDisabled("cargoOperacional.idCargoOperacional", disable);
		setDisabled("empresa.idEmpresa", disable);
	}
	
	/**
	 * Limpa o form
	 * Recebe como parametro o valor da combo de 'tpIntegrante' caso 
	 * seja enviado um valor referente ao valor da combo (valor diferente de 'undefined') 
	 * a combo com fica com este valor setado.
	 *
	 * @param comboValue valor da combo
	 */
	function newScreen(comboValue) {
		newButtonScript();
		setaDadosSessao();
		if (comboValue!=undefined) setElementValue("tpIntegrante", comboValue);
	}
	
	/**
	 * Controla a propriedade de de habilitado ou desabilitado ou abilitado dos 
	 * botoes da tela
	 * 
	 * @param disable 
	 */
	function disableButtons(disable){
		setDisabled("novoIntegranteButton", disable);
		setDisabled("salvarIntegranteButton", disable);
		setDisabled("excluirButton", disable);
	}
	
	/**
	 * callback do form 
	 */
	function enableFields_cb(data, error) {
		checkDisableItems(data.tpIntegrante.value);
		onDataLoad_cb(data, error);
		if (data.tpIntegrante.value=="F") {
			setElementValue("usuario.nrMatricula", data.usuario.nrMatricula);
		}
	}	
		
</script>