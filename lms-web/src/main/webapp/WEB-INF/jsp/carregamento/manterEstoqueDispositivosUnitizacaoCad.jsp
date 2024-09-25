<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.estoqueDispositivoQtdeService">
	<adsm:form action="/carregamento/manterEstoqueDispositivosUnitizacao" idProperty="idEstoqueDispositivoQtde">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="F" serializable="true"/>
		
		<adsm:lookup label="filial" dataType="text" size="3" maxLength="3" labelWidth="18%" width="82%" required="true"
					 property="filial"  exactMatch="true" 
					 service="lms.carregamento.estoqueDispositivoQtdeService.findLookupFiliaisPorUsuario" 
                     action="/municipios/manterFiliais"
                     idProperty="idFilial"
                     criteriaProperty="sgFilial">
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" size="30" maxLength="30" disabled="true"/>
        </adsm:lookup>
	
		<adsm:lookup label="empresaProprietaria" dataType="text" size="20" maxLength="20" labelWidth="18%" width="82%" required="true"
					 property="empresa"
					 service="lms.municipios.empresaService.findLookup" 
                     action="/municipios/manterEmpresas"  
                     idProperty="idEmpresa" 
                     criteriaProperty="pessoa.nrIdentificacao"
                     relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
            <adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
            <adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" serializable="false" size="30" maxLength="30" disabled="true"/>
        </adsm:lookup>

		<adsm:combobox label="tipoDispositivo" labelWidth="18%" width="82%"
					   property="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao"
					   service="lms.carregamento.tipoDispositivoUnitizacaoService.findTipoDispositivoByQuantidade"
					   optionProperty="idTipoDispositivoUnitizacao"
					   optionLabelProperty="dsTipoDispositivoUnitizacao"
					   onlyActiveValues="true"
					   required="true"
		/>
		<adsm:textbox property="qtEstoque" label="quantidade" dataType="integer" size="6" labelWidth="18%" width="82%" maxLength="6" required="true"/>

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton/>	
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript">

function initWindow(eventObj) {
	
	// Desabilita a combo quando vier da grid
	if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton" ) {
		setDisabled(document.getElementById("tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao"), true);
		setDisabled(document.getElementById("filial.idFilial"), true);
		setDisabled(document.getElementById("empresa.idEmpresa"), true);
		if (eventObj.name == "storeButton" ) {
			setFocusOnNewButton();
			return;
		}
	} 
	else {
		setDisabled("tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao", false);
		setDisabled(document.getElementById("filial.idFilial"), false);
		setDisabled(document.getElementById("empresa.idEmpresa"), false);		
		// se não veio da grid e do store, então deve setar na lookup de empresa como default a empresa mercurio
		findEmpresaDefault();
	}
	setFocusOnFirstFocusableField(this.document);
}

/**
 * Este méto irá popular a lookup de empresa com a empresa mercúrio
 * conforme especificação 05.01.02.06
 */     
function findEmpresaDefault() {
    var data = new Array();
    // Envia um map vazio, os dados necessários estão no método da service
    var sdo = createServiceDataObject("lms.carregamento.estoqueDispositivoQtdeService.findEmpresaDefault", "findEmpresaDefault",data);
    xmit({serviceDataObjects:[sdo]});
}
/**
 * CallBack da busca da empresa default
 */
function findEmpresaDefault_cb(data, error){
	if (error != undefined) {
		alert(error);
		return;
	}
	if (data.empresa != undefined) {
		setElementValue('empresa.idEmpresa', data.empresa.idEmpresa);
		setElementValue('empresa.pessoa.nrIdentificacao', data.empresa.pessoa.nrIdentificacao);
		setElementValue('empresa.pessoa.nmPessoa', data.empresa.pessoa.nmPessoa);
		document.getElementById("empresa.idEmpresa").reseted=false;
	}
}

/**
 * Verifica se o usuario tem acesso a mais de uma filial. Caso tenha acesso
 * a apenas uma, a lookup deve vir preenchida.
 */
 function initWindow(eventObj) {
	var criteria = new Array();
    var sdo = createServiceDataObject("lms.carregamento.estoqueDispositivoQtdeService.verificaAcessoFilial", "resultadoBusca", criteria);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoBusca_cb(data, error) {
	if(data.filial==undefined) {
		return false;
	} else {
		setElementValue('filial.idFilial', getNestedBeanPropertyValue(data,"filial.idFilial"));
		setElementValue('filial.sgFilial', getNestedBeanPropertyValue(data,"filial.sgFilial"));
		setElementValue('filial.pessoa.nmFantasia', getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
		
	}
}
</script>