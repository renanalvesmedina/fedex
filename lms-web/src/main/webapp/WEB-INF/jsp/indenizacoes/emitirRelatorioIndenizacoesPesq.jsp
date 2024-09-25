<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.emitirRelatorioIndenizacoesAction">
	<adsm:form action="/indenizacoes/emitirRelatorioIndenizacoes">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
	
		<adsm:range label="periodo" width="80%" labelWidth="20%" required="true" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="dtInicial" picker="true" onchange="carregaComboRegional()"/>
			<adsm:textbox dataType="JTDate" property="dtFinal" picker="true" onchange="carregaComboRegional()"/>
		</adsm:range>
		
		<adsm:hidden property="tpIndenizacaoHidden"/>
		<adsm:combobox property="tpIndenizacao" 
					   label="tipoIndenizacao" 
					   domain="DM_TIPO_INDENIZACAO" 
					   width="80%" renderOptions="true"
					   labelWidth="20%"
					   onchange="onChangeIndenizacao()"
					   />		
		
		<adsm:hidden property="dsMotivoAberturaHidden"/>			   
		<adsm:combobox label="motivoNaoConformidade" 
					   labelWidth="20%" 
					   width="80%"
					   property="motivoAberturaNc.idMotivoAberturaNc" 
					   optionProperty="idMotivoAberturaNc" 
					   optionLabelProperty="dsMotivoAbertura" 
					   service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findComboMotivoAberturaNc"
					   onchange="onChangeMotivoAberturaNc()"/>
					   		
		<adsm:hidden property="sgTipoSeguroHidden"/> 
		<adsm:combobox property="tipoSeguro.idTipoSeguro" 
					   optionLabelProperty="sgTipo" 
					   optionProperty="idTipoSeguro" 
					   service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findComboTipoSeguro"  
					   label="tipoSeguro" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeTipoSeguro()"/>
					   
		<adsm:hidden property="dsTipoSinistroHidden"/>
		<adsm:combobox property="tipoSinistro.idTipo" 
					   optionLabelProperty="dsTipo" 
					   optionProperty="idTipoSinistro" 
					   service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findComboTipoSinistro" 
					   label="tipoSinistro" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeTipoSinistro()"/>

		<adsm:hidden property="moedaHidden"/>
		<adsm:combobox property="moeda.idMoeda" 
					   service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findComboMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   label="valor" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeForSetHiddenValue(this, 'moedaHidden')">
	  		<adsm:range>
				<adsm:textbox dataType="currency" property="vlInicial" picker="false" />
				<adsm:textbox dataType="currency" property="vlFinal" picker="false" />
			</adsm:range>
		</adsm:combobox> 

		<adsm:lookup label="filial" labelWidth="20%" width="80%" 
             		 property="filial"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
   			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
          	<adsm:textbox dataType="text" 
          			      property="filial.pessoa.nmFantasia" 
          			      serializable="false" 
          			      size="50" 
          			      maxLength="50" 
          			     disabled="true"/>
        </adsm:lookup>
		
		<adsm:lookup label="filialDebitada" labelWidth="20%" width="80%" 
             		 property="filialDebitada"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>		             
      		<adsm:propertyMapping relatedProperty="filialDebitada.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
          	<adsm:textbox dataType="text" 
          			      property="filialDebitada.pessoa.nmFantasia" 
          			      serializable="false" 
          			      size="50" 
          			      maxLength="50" 
          			     disabled="true"/>
        </adsm:lookup>
		
		<adsm:hidden property="sgAndDsRegionalHidden"/>
		<adsm:combobox property="regional.idRegionalFilial" 
					   optionProperty="idRegional" 
					   optionLabelProperty="sgAndDsRegional"
					   service="" 
					   label="regional" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeRegional()"
					   autoLoad="false"/>
		
		<adsm:hidden property="tpModalHidden"/>
		<adsm:combobox label="modal" 
					   property="tpModal"  
					   domain="DM_MODAL" 
					   width="80%" renderOptions="true"
					   labelWidth="20%"
					   onchange="onChangeModal()"/>

		<adsm:hidden property="tpAbrangenciaHidden"/>
		<adsm:combobox property="tpAbrangencia" 
					   label="abrangencia" 
					   domain="DM_ABRANGENCIA" 
					   width="80%" renderOptions="true"
					   labelWidth="20%"
					   onchange="onChangeAbrangencia()"/>

		<adsm:hidden property="tpStatusIndenizacaoHidden"/>
		<adsm:combobox label="status" 
					   property="tpStatusIndenizacao"  
					   domain="DM_STATUS_INDENIZACAO" 
					   width="80%" renderOptions="true"
					   labelWidth="20%"
					   onchange="onChangeStatus()"/>		
		
		<adsm:lookup label="remetente"  
					 action="/vendas/manterDadosIdentificacao" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 dataType="text" 
					 exactMatch="true" 
					 idProperty="idCliente" 
					 maxLength="20" 
					 property="remetente" 
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findLookupCliente" 
					 size="20" 
					 labelWidth="20%" 
					 width="80%">
					<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa"  modelProperty="pessoa.nmPessoa"/>
					<adsm:textbox dataType="text" 
								  disabled="true" 
								  property="remetente.pessoa.nmPessoa" 
								  serializable="true"
								  size="50"/>
		</adsm:lookup>		
		
		<adsm:lookup label="destinatario"
					 action="/vendas/manterDadosIdentificacao" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 dataType="text" 
					 exactMatch="true" 
					 idProperty="idCliente" 
					 maxLength="20" 
					 property="destinatario" 
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findLookupCliente" 
					 size="20" 
					 labelWidth="20%" 
					 width="80%">
					<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
					<adsm:textbox dataType="text" 
								 disabled="true" 
								 property="destinatario.pessoa.nmPessoa" 
								 serializable="true"
								 size="50"/>
		</adsm:lookup>
		
		<adsm:lookup label="beneficiario" 
					 dataType="text" 
					 property="beneficiario" 
					 idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20"
					 maxLength="20" 
					 picker="true"
					 serializable="true" 
					 required="false" 
					 labelWidth="20%" 
					 width="80%">
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="beneficiario.nmPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="beneficiario.nmPessoa" disable="false" />
			
			<adsm:textbox dataType="text" 
						  property="beneficiario.nmPessoa" 
						  size="50" 
						  maxLength="50" 
						  disabled="true" 
						  serializable="true"/>
		</adsm:lookup>

		<adsm:lookup label="favorecido" 
					 dataType="text" 
					 property="favorecido" 
					 idProperty="idPessoa" 
					 picker="true"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20" 
					 maxLength="20" 
					 serializable="true" 
					 required="false" 
					 labelWidth="20%" 
					 width="80%">
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="favorecido.nmPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="favorecido.nmPessoa" disable="false" />
			
			<adsm:textbox dataType="text" 
						  property="favorecido.nmPessoa" 
						  size="50" 
						  maxLength="50" 
						  disabled="true" 
						  serializable="true"/>
		</adsm:lookup>
		
		<adsm:lookup label="devedor" 
					 dataType="text" 
					 property="devedor" 
					 idProperty="idPessoa" 
					 picker="true"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20" 
					 maxLength="20" 
					 serializable="true" 
					 required="false" 
					 labelWidth="20%" 
					 width="80%">
			
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="devedor.nmPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="devedor.nmPessoa" disable="false" />
			
			<adsm:textbox dataType="text" 
						  property="devedor.nmPessoa" 
						  size="50" 
						  maxLength="50" 
						  disabled="true" 
						  serializable="true"/>
		</adsm:lookup>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="20%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.indenizacoes.emitirRelatorioIndenizacoesAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript" >
function carregaComboRegional() {
	var dtInicial = getElementValue("dtInicial");
	var dtFinal = getElementValue("dtFinal");
	
	if (dtInicial!="" && dtFinal!="") {
		setDisabled("regional.idRegionalFilial", false);
		var criteria = new Array();
	    setNestedBeanPropertyValue(criteria, "dtInicial", dtInicial);
	    setNestedBeanPropertyValue(criteria, "dtFinal", dtFinal);	
		
	    var sdo = createServiceDataObject("lms.indenizacoes.emitirRelatorioIndenizacoesAction.findComboRegional", "regional_idRegionalFilial", criteria);
	    xmit({serviceDataObjects:[sdo]});
	} else {
		setDisabled("regional.idRegionalFilial", true);
		var map = new Array();
		regional_idRegionalFilial_cb(map);
	}
}

function initWindow(){
	document.getElementById("filial.sgFilial").serializable=true;
	document.getElementById("filialDebitada.sgFilial").serializable=true;	
	setDisabled("regional.idRegionalFilial", true);
}

function onChangeForSetHiddenValue(combo, strHidden) {
	setElementValue(strHidden, combo.options[combo.selectedIndex].text);	
}

function onChangeMoeda() {
	var combo = document.getElementById("moeda.idMoeda");
	setElementValue('siglaSimboloHidden', combo.options[combo.selectedIndex].text);
}

function onChangeStatus() {
    var combo = document.getElementById("tpStatusIndenizacao");
    setElementValue('tpStatusIndenizacaoHidden', combo.options[combo.selectedIndex].text);
}
function onChangeRegional() {
    var combo = document.getElementById("regional.idRegionalFilial");
    setElementValue('sgAndDsRegionalHidden', combo.options[combo.selectedIndex].text);
}

function onChangeRegional() {
    var combo = document.getElementById("regional.idRegionalFilial");
    setElementValue('sgAndDsRegionalHidden', combo.options[combo.selectedIndex].text);
}

function onChangeModal() {
    var combo = document.getElementById("tpModal");
    setElementValue('tpModalHidden', combo.options[combo.selectedIndex].text);
}

function onChangeAbrangencia() {
    var combo = document.getElementById("tpAbrangencia");
    setElementValue('tpAbrangenciaHidden', combo.options[combo.selectedIndex].text);
}

function onChangeIndenizacao() {
    var combo = document.getElementById("tpIndenizacao");
    setElementValue('tpIndenizacaoHidden', combo.options[combo.selectedIndex].text);
}

function onChangeMotivoAberturaNc() {
    var combo = document.getElementById("motivoAberturaNc.idMotivoAberturaNc");
    setElementValue('dsMotivoAberturaHidden', combo.options[combo.selectedIndex].text);
}

function onChangeTipoSeguro() {
    var combo = document.getElementById("tipoSeguro.idTipoSeguro");
    setElementValue('sgTipoSeguroHidden', combo.options[combo.selectedIndex].text);
}

function onChangeTipoSinistro() {
    var combo = document.getElementById("tipoSinistro.idTipo");
    setElementValue('dsTipoSinistroHidden', combo.options[combo.selectedIndex].text);
}

</script>