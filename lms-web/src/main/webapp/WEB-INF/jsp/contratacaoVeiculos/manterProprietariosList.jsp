<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript">
<!--
var idFilialLogado;
var sgFilialLogado;
var nmFilialLogado;

function pageLoad() { 
   
   onPageLoad();
   
   /* NÃO APAGAR O CODIGO, SERA DESCOMENTADO QUANDO SAIR A VERSAO 1.11 DA ARQUITETURA  */
   initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				    , tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			    , numberElement:document.getElementById("pessoa.nrIdentificacao") });
}

function pageLoad_cb(data, msg, msgKey) {

   onPageLoad_cb(data, msg, msgKey);
   
   //getFilialUsuario();
}
//-->
</script>
<adsm:window title="manterProprietarios" service="lms.contratacaoveiculos.manterProprietariosAction" onPageLoad="pageLoad" onPageLoadCallBack="pageLoad">
	<adsm:form action="/contratacaoVeiculos/manterProprietarios" idProperty="idProprietario">
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>
	 	
		<adsm:hidden property="dontFillFilial" />

        <adsm:lookup property="filial" idProperty="idFilial" required="false" criteriaProperty="sgFilial" maxLength="3"
        		service="lms.contratacaoveiculos.manterProprietariosAction.findLookupFilial" dataType="text" label="filial"
        		size="3" action="/municipios/manterFiliais" 
        		labelWidth="17%" width="33%" minLengthForAutoPopUpSearch="3" cellStyle="vertical-align:bottom;"
        		exactMatch="false" disabled="false">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
			<adsm:hidden property="filial.siglaNomeFilial"/>
		</adsm:lookup>
        
		<adsm:combobox property="tpProprietario" label="tipoProprietario"
				labelWidth="17%" domain="DM_TIPO_PROPRIETARIO" width="33%" />
        
		<adsm:combobox definition="TIPO_PESSOA.list" label="tipoPessoa" labelWidth="17%" width="33%" required="false"/>
		
		<adsm:complement label="identificacao" labelWidth="17%" width="33%">
	       	<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
	       	<adsm:textbox definition="IDENTIFICACAO_PESSOA" />		
	    </adsm:complement>
        <adsm:hidden property="pessoa.idPessoa" />
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="nome" maxLength="50" 
				      required="false" size="60" labelWidth="17%" width="75%"/>		
  
		<adsm:combobox property="tpPeriodoPagto" label="periodoPagamento" domain="DM_PERIODO_PAGAMENTO_PROPRIETARIO" labelWidth="17%" width="33%" cellStyle="vertical-align:bottom;"/>
		<adsm:combobox property="diaSemana" optionProperty="value" optionLabelProperty="description" label="diaPagamentoSemanal" service="lms.contratacaoveiculos.manterProprietariosAction.findDiasUteisPagamentoSemanal"  labelWidth="17%" width="33%" cellStyle="vertical-align:bottom;"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS_PESSOA" labelWidth="17%" width="33%" label="situacao"/>

		<adsm:range label="periodoAtualizacao" labelWidth="17%"  width="33%"> 
			<adsm:textbox dataType="JTDate" property="dtAtualizacaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtAtualizacaoFinal"/>
		</adsm:range>	

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="proprietario"/> 
			<adsm:resetButton/>
		</adsm:buttonBar>
		<script>
		var LMS_00049 = '<adsm:label key="LMS-00049"/>'; 
		</script>
	</adsm:form>
	
	<adsm:grid property="proprietario"  idProperty="idProprietario" gridHeight="153"
			service="lms.contratacaoveiculos.manterProprietariosAction.findPaginatedCustom"
			rowCountService="lms.contratacaoveiculos.manterProprietariosAction.getRowCountCustom"
			selectionMode="check" unique="true" rows="7" scrollBars="horizontal" >
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn width="80" title="filial" property="filial.sgFilial"/>
			<adsm:gridColumn width="60" title="" property="filial.pessoa.nmFantasia"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn width="60" title="identificacao" property="pessoa.tpIdentificacao" align="left" isDomain="true" />
		<adsm:gridColumn width="100" title="" property="pessoa.nrIdentificacaoFormatado" dataType="text" align="right" />		
		<adsm:gridColumn width="180" title="nome" property="pessoa.nmPessoa"/>
		
		<adsm:gridColumn width="60" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:gridColumn width="60" title="periodo" property="tpPeriodoPagto" isDomain="true"/>
		<adsm:gridColumn width="160" title="diaPagamentoSemanal" property="diaSemana" isDomain="true"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	
	// ############################################################
	// busca valores da filial do usuario logado
	// ############################################################
	function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterProprietariosAction.findFilialUsuarioLogado","getFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}
 
 	// ############################################################
	// callback de getFilialUsuario()
	// ############################################################
	function getFilialCallBack_cb(data,error) {

		if (error != undefined) {
			alert(error);
			return false;
		}
		
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			setaValoresFilial();
		}
	}
	
	function setaValoresFilial() {
		if (getElementValue("dontFillFilial") != "true") {
			setElementValue("filial.idFilial", idFilialLogado);
			setElementValue("filial.sgFilial", sgFilialLogado);
			setElementValue("filial.pessoa.nmFantasia", nmFilialLogado);
		}
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("pessoa.nrIdentificacao") != "" ||
					getElementValue("pessoa.nmPessoa") != "" ||
					(getElementValue("dtAtualizacaoInicial") != "" && getElementValue("dtAtualizacaoFinal") != "")) {
				return true;
			} else {
				alert(i18NLabel.getLabel("LMS-00013")
						+ document.getElementById("pessoa.nrIdentificacao").label + ', ' 
						+ document.getElementById("pessoa.nmPessoa").label + ', ' 
						+ document.getElementById("dtAtualizacaoInicial").label + ".");
			}
		}
		return false;
	}
	
	function validateLookupForm() {
      	return validateTab();
	}
	
	 
</script>