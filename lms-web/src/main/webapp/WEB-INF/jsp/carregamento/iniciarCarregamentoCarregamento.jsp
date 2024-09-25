<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<script type="text/javascript">

	/**
	 * Inicia objetos da tela
	 */
	function loadDataObjects() {	 
		onPageLoad();
		disableButtons(false);
	}
	
	/**
	 * Desabilita ou abilita os botoes desta aba
	 *
	 * @param disable 
	 */
	function disableButtons(disable) {
		document.getElementById("iniciar").disabled=disable;
		document.getElementById("fechar").disabled=disable; 
	}
</script>

<adsm:window service="lms.carregamento.iniciarCarregamentoAction" onPageLoad="loadDataObjects">
	<adsm:form action="/carregamento/iniciarCarregamento" idProperty="idEquipeOperacao">
	
		<adsm:section caption="iniciarCarregamento"/>
		
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.idFilial"/>
		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial" 
					 label="controleCargas" width="85%" size="3" maxLength="3" disabled="true">
			<adsm:textbox dataType="text" property="controleCarga.nrControleCarga" maxLength="8" size="8" disabled="true"/>
		</adsm:textbox>
		
		<adsm:hidden property="idFilial"/>
		<adsm:textbox dataType="text" property="sgFilial" label="filial" size="3" maxLength="3" width="85%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoa" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="sgFilialPostoAvancado" 
					  label="postoAvancado" maxLength="3" size="3" width="85%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoaPostoAvancado" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:lookup property="equipe" dataType="text" idProperty="idEquipe" criteriaProperty="dsEquipe"
					 action="/carregamento/manterEquipes" service="lms.carregamento.iniciarCarregamentoAction.findEquipes" 
					 onDataLoadCallBack="loadEquipes"  onPopupSetValue="loadEquipesPopUp" onchange="return equipeOnChange();" 
					 label="equipe" width="85%" maxLength="50" size="50" required="true" minLengthForAutoPopUpSearch="3" exactMatch="false">
			<adsm:propertyMapping modelProperty="idEquipe" relatedProperty="equipe.idEquipe"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
		</adsm:lookup>
					 
		<adsm:combobox property="box.idBox" optionProperty="idBox" optionLabelProperty="dsBox" 
					   service="lms.carregamento.iniciarCarregamentoAction.findBox" 
					   label="box" width="85%" required="true"/>
		
		<adsm:hidden property="controleCargaConcatenado" serializable="false"/>
		<adsm:hidden property="filialConcatenado" serializable="false"/>
		<adsm:hidden property="PostoAvancadoConcatenado" serializable="false"/>
		
		<adsm:buttonBar>
			<adsm:storeButton id="iniciar" caption="iniciarCarregamento" service="lms.carregamento.iniciarCarregamentoAction.storeIniciarCarregamento"/>
			<adsm:button id="fechar" caption="fechar" onclick="returnToParent()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	
	function initWindow(eventObj) {
		if (eventObj.name == "tab_load") {
			//Busca a tela pai
			var parentWindow = dialogArguments.window.document;
			
			//carrega os objetos
			setElementValue("controleCarga.idControleCarga", parentWindow.getElementById("controleCarga.idControleCarga").value);
			setElementValue("controleCarga.nrControleCarga", parentWindow.getElementById("controleCarga.nrControleCarga").value);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.idFilial").value);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value);
			
			setElementValue("idFilial", parentWindow.getElementById("idFilial").value);
			setElementValue("sgFilial", parentWindow.getElementById("sgFilial").value);
			setElementValue("nmPessoa", parentWindow.getElementById("nmPessoa").value);
			
			setElementValue("sgFilialPostoAvancado", parentWindow.getElementById("sgFilialPostoAvancado").value);
			setElementValue("nmPessoaPostoAvancado", parentWindow.getElementById("nmPessoaPostoAvancado").value);
			
			//Seta os hidens com seus valores concatenados
			setElementValue("controleCargaConcatenado", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value + " - " + parentWindow.getElementById("controleCarga.nrControleCarga").value);
			setElementValue("filialConcatenado", parentWindow.getElementById("sgFilial").value + " - " + parentWindow.getElementById("nmPessoa").value);
			
			if (getElementValue("nmPessoaPostoAvancado")!="") {
				setElementValue("postoAvancadoConcatenado", parentWindow.getElementById("sgFilialPostoAvancado").value + " - " + parentWindow.getElementById("nmPessoaPostoAvancado").value);
			} else {
				setElementValue("postoAvancadoConcatenado", parentWindow.getElementById("sgFilialPostoAvancado").value);
			}
			
		} else if(eventObj.name == "tab_click") {
			disableButtons(false);
			setIdEquipe(getElementValue("equipe.idEquipe"));
		} else if(eventObj.name == "storeButton") {
			returnToParent();
		}
	}
	
	//##################################
    // Comportamentos apartir de objetos
	//##################################
	
	function equipeOnChange() {
		if (getElementValue("equipe.dsEquipe")=="") {
			removeIntegrantes(0); 
			setDisableTab(true);
		} else {
			setDisableTab(false);
		}
		return equipe_dsEquipeOnChangeHandler();
	}
	
	
	/**
	 * Tratamento para o callback da lookup de equipes
	 *
	 * @param data
	 * @param error
	 */
	function loadEquipes_cb(data, error) {
		if (data[0]!=undefined) {
			setDisableTab(false);
			setElementValue("equipe.idEquipe", data[0].idEquipe);
			removeIntegrantes(data[0].idEquipe); 
			setIdEquipe(data[0].idEquipe);
		} else {	
			setDisableTab(true);
		}
		
		return lookupExactMatch({e:document.getElementById("equipe.idEquipe"), data:data, callBack:"loadEquipesLikeMatch"});
	}
	
	/**
	 * Tratamento para o callback LikeMatch da lookup de equipes
	 *
	 * @param data
	 * @param error
	 */
	function loadEquipesLikeMatch_cb(data, error) {
		if (data[0]!=undefined) {
			setDisableTab(false);
			setElementValue("equipe.idEquipe", data[0].idEquipe);
			removeIntegrantes(data[0].idEquipe); 
			setIdEquipe(data[0].idEquipe);
		} else {	
			setDisableTab(true);
		}
		
		return equipe_dsEquipe_likeEndMatch_cb(data);
	}
	
	
	/**
	 * Tratamento para o callback da lookup de equipes quando a mesma vem da popup
	 *
	 * @param data
	 * @param error
	 */
	function loadEquipesPopUp(data, error) {
		if (data.dsEquipe!=undefined) {
			setDisableTab(false);
			removeIntegrantes(data.idEquipe);
			setIdEquipe(data.idEquipe);
		}
	}
	
	/**
	 * Remove todos os filhos casoo id desta tela seja diferente da do filho
	 */
	function removeIntegrantes(idEquipe){
		var tabGroup = getTabGroup(this.document);
		var abaCarregamento = tabGroup.getTab("integrantes");
		
		if (abaCarregamento.getElementById("idEquipe").value!=idEquipe){
			var sdo = createServiceDataObject("lms.carregamento.iniciarCarregamentoAction.newMaster");
			xmit({serviceDataObjects:[sdo]});
		}	
		
	}
	
	/**
	 * Seta o id equipe existente na masterLinK 
	 */ 
	function setIdEquipe(idEquipe) {
	
		var tabGroup = getTabGroup(this.document);
		var abaCarregamento = tabGroup.getTab("integrantes");
		
		abaCarregamento.getElementById("idEquipe").value = idEquipe;
	}
	
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	
	/**
	 * Desabilita as abas de gerenciamento e riscos
	 *
	 * @param disableTab aba gerenciamento
	 */
	function setDisableTab(disableTab) {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup!=null) {
			if (disableTab!=null) tabGroup._tabsIndex[1].setDisabled(disableTab);
		}
	}
</script>