<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.recepcaodescarga.descarregarVeiculoTrocarAction">
	<adsm:form idProperty="idIntegranteEqOperac" action="/recepcaoDescarga/descarregarVeiculo" height="150" 
	service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.findByIdIntegranteEqOperac" onDataLoadCallBack="enableFields" >

		<adsm:masterLink idProperty="idEquipeOperacao" showSaveAll="false">
			<adsm:masterLinkItem property="controleCargaConcatenado" label="controleCargas" itemWidth="50" />
			<adsm:masterLinkItem property="filialConcatenado" label="filial" itemWidth="50" />
			<adsm:masterLinkItem property="postoAvancadoConcatenado" label="postoAvancado" itemWidth="50" />
			<adsm:masterLinkItem property="equipe.dsEquipe" label="equipe" itemWidth="50" />
			<adsm:hidden property="idEquipe"/>
		</adsm:masterLink>

		<adsm:combobox property="tpIntegrante" domain="DM_INTEGRANTE_EQUIPE" 
					   onchange="return comboTpIntegranteOnChange(this.value)"
			  		   label="contratacao" width="85%" required="true" renderOptions="true"/>

		<adsm:hidden property="usuario.dsFuncao"/>
		<adsm:lookup dataType="integer" property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula" 
                     service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.findLookupUsuarioFuncionario" 
                     action="/configuracoes/consultarFuncionariosView" onchange="return funcionarioOnChange();"
                     label="funcionario" width="85%" disabled="true" size="16" maxLength="16" mask="000000000" criteriaSerializable="true">
            <adsm:propertyMapping criteriaProperty="idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>
			<adsm:propertyMapping relatedProperty="usuario.dsFuncao" modelProperty="dsFuncao"/>
            <adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:lookup property="pessoa" dataType="text" idProperty="idPessoa" 
			criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="nrIdentificacaoFormatado"
			service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.findLookupPessoa" action="/configuracoes/manterPessoas" 
			label="integrante" size="20" maxLength="20" width="85%" disabled="true" >
			<adsm:propertyMapping relatedProperty="pessoa.nmPessoa" modelProperty="nmPessoa" />	
			<adsm:propertyMapping relatedProperty="pessoa.tpIdentificacao" modelProperty="tpIdentificacao.value" />	
			
			<adsm:propertyMapping criteriaProperty="pessoa.tpPessoa" modelProperty="pessoa.tpPessoa" />		
			<adsm:propertyMapping criteriaProperty="pessoa.nmPessoa" modelProperty="nmPessoa" disable="false"/>	
			<adsm:propertyMapping criteriaProperty="pessoa.tpIdentificacao" modelProperty="pessoa.tpIdentificacao" disable="false"/>	
			<adsm:propertyMapping criteriaProperty="pessoa.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacao" disable="false"/>
			
			<adsm:textbox dataType="text" property="pessoa.nmPessoa" size="50" maxLength="50" disabled="true" />
		</adsm:lookup>

		<adsm:combobox property="cargoOperacional.idCargoOperacional" optionLabelProperty="dsCargo" optionProperty="idCargoOperacional"
			service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.findCargos" onlyActiveValues="true"
			label="cargo" width="85%" disabled="true"/>

		<adsm:lookup dataType="text" property="empresa" idProperty="idEmpresa" criteriaProperty="pessoa.nmPessoa"
			service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.findEmpresas" action="/municipios/manterEmpresas" 
			exactMatch="false" 	minLengthForAutoPopUpSearch="3" label="empresa" size="50"
			maxLength="50" width="85%" disabled="true">
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
		</adsm:lookup>

		<adsm:hidden property="pessoa.tpPessoa" value="F" />
		<adsm:hidden property="pessoa.tpIdentificacao" />
		<adsm:hidden property="tpSituacao" value="A" />
		
		<adsm:hidden property="idFilial"/>
		<adsm:hidden property="sgFilial"/>
		<adsm:hidden property="pessoa.nmFantasia"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarIntegrante" id="salvarIntegranteButton" service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.saveIntegranteEqOperac" />
			<adsm:button caption="limpar" id="novoIntegranteButton" onclick="novoIntegranteForm();"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="integranteEqOperac" idProperty="idIntegranteEqOperac" gridHeight="170" unique="true" rows="7" 
			   service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.findPaginatedIntegranteEqOperac" 
			   rowCountService="lms.recepcaodescarga.descarregarVeiculoTrocarAction.getRowCountIntegranteEqOperac"
			   detailFrameName="cad" autoSearch="false" onDataLoadCallBack="disableGridCheck">
		<adsm:gridColumn property="nmIntegranteEquipe" title="nome" width="25%" />
		<adsm:gridColumn property="usuario.nrMatricula" title="matricula" width="12%" align="right" />
		<adsm:gridColumn property="cargoOperacional.dsCargo" title="cargo" width="15%" /> 
		<adsm:gridColumn property="tpIntegrante" dataType="JTDateTimeZone" align="center" title="contratacao" width="15%" isDomain="true"/>
		<adsm:gridColumn property="pessoa.nrIdentificacaoFormatado" title="identificacao" width="15%" align="right" />
		<adsm:gridColumn property="empresa.pessoa.nmPessoa" title="empresa" width="18%" />
		<adsm:buttonBar>
			<adsm:removeButton id="excluirButton" caption="excluirIntegrante" service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.removeByIdsIntegranteEquipe"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	document.getElementById("idEquipe").masterLink = true;
	
	function initWindow(eventObj) {
		disableButtons();
		if(eventObj.name == "storeItemButton") {
			disableAllItems(true);
		} else  if (eventObj.name == "tab_click"){
			if (getElementValue("idEquipe")!="") {
				//Caso seja aberta do modo convencional...
				//document.getElementById("integranteEqOperac.chkSelectAll").disabled = false;
				integranteEqOperacGridDef.disabled=false;
				setDisabled("tpIntegrante", false);
				novoIntegranteForm();
			} else {
				//Caso seja aberta da grid...
				integranteEqOperacGridDef.disabled=true;
				setDisabled("tpIntegrante", true);
				disableAllItems(true);
				disableButtons(true);
			}
		}
		
	}
	
	/**
	 * Carrega o hidden de filial contido na aba de integrantes para ser utilizado como
	 * criteria.
	 */
	function loadFilialFromParent() {
		var parentWindow = dialogArguments.window.document;
			
		//carrega os objetos
		setElementValue("idFilial", parentWindow.getElementById("idFilial").value);
		setElementValue("sgFilial", parentWindow.getElementById("sgFilial").value);
		setElementValue("pessoa.nmFantasia", parentWindow.getElementById("pessoa.nmFantasia").value);
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
		
		if (getElementValue("idEquipe")=="") {
			disableButtons(true);
		}
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
	}
	
	/**
	 * Deixa a tela em seu estado inicial
	 */
	function novoIntegranteForm(){
		newScreen();
		disableAllItems(true);
	}
	
	/**
	 * Seta a mascara no campo de funcionario.
	 */
	function funcionarioOnChange(){
		format(document.getElementById("usuario.nrMatricula"));
		if (getElementValue("usuario.nrMatricula")=="000000000") {
			setElementValue("usuario.nrMatricula", "");
		}		
		return lookupChange({e:document.getElementById("usuario.idUsuario"),forceChange:true});
	}
	
	/**
	 * Verifica se estes registros estao sendo carregados apartir de uma equipe
	 * caso nao (idEquipe="") nao habilita o checkbox da grid.
	 */
	function disableGridCheck_cb(data, error) {
		if (getElementValue("idEquipe")=="") {
			integranteEqOperacGridDef.disabled=true;
		}
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
	 * Valida quais serao os campos que serao disabilitados ou nao
	 *
	 * @param value 'F' (Funcionario) ou 'T' (Terceiro)
	 */
	function checkDisableItems(value) {
	
		if (document.getElementById("tpIntegrante").disabled==true) {
			disableButtons(true);
			return false;
		}
	
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
	
	function retornoFuncionario_cb(data, error) {
		var r = usuario_funcionario_idChapa_exactMatch_cb(data);
		if (r == true) {
			setElementValue("usuario.nrMatricula", data[0].nrMatricula);
		}
		return r;
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
		var idEquipe = getElementValue("idEquipe");
		newButtonScript();
		setElementValue("idEquipe", idEquipe);
		
		if (comboValue!=undefined) setElementValue("tpIntegrante", comboValue);
		
		loadFilialFromParent();
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
	
	
</script>
