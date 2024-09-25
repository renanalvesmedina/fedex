<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="myPageLoad">
	<adsm:form action="/expedicao/emitirControleFormulariosCTRCCTR">

		<adsm:lookup label="filial" property="filial"
			service="lms.municipios.filialService.findLookup"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			criteriaSerializable="true"
			dataType="text"
			size="5"
			width="35%"
			maxLength="3">
			<adsm:hidden property="regional.siglaDescricao"/>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
		</adsm:lookup>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>

		<adsm:range label="periodoEmissao" labelWidth="15%" width="35%">
			<adsm:textbox dataType="JTDate" property="dataInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dataFinal" required="true"/>
		</adsm:range>

		<adsm:combobox
			property="ordenadoPor"
			label="ordenadoPor"
			domain="DM_ORDENACAO_EMISS_CONTR_FORM"
			width="65%"
			required="true"
		/>

		<adsm:buttonBar>
			<!-- expedicao/emitirControleFormulariosCTRCCTR.jasper -->
			<adsm:reportViewerButton service="lms.expedicao.emitirControleFormulariosCTRCCTRAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
<!--
	var idFilialUsuarioLogado;
	var sgFilialUsuarioLogado;
	var nmFilialUsuarioLogado;

	function initWindow(event) {
		if(event.name == "cleanButton_click"){
			preencheValoresDefault();
		}
	}

	function myPageLoad_cb(data, erro) {
		var data = new Array();
		onPageLoad_cb(data, erro);
		// Executa o metodo da service que retorna os dados da filial associado ao usuario
		var sdo = createServiceDataObject("lms.expedicao.emitirControleFormulariosCTRCCTRAction.filialFindByUser","filialUsuario",data);
		xmit({serviceDataObjects:[sdo]});
	}

	function filialUsuario_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			return false;
		}

		idFilialUsuarioLogado = getNestedBeanPropertyValue(data, "idFilial");
		sgFilialUsuarioLogado = getNestedBeanPropertyValue(data, "sgFilial");
		nmFilialUsuarioLogado = getNestedBeanPropertyValue(data, "nmFilial");

		preencheValoresDefault();
		setFocusOnFirstFocusableField();

		return true;
	}

	function preencheValoresDefault() {
		setElementValue("filial.idFilial",idFilialUsuarioLogado);
		setElementValue("filial.sgFilial",sgFilialUsuarioLogado);
		setElementValue("filial.pessoa.nmFantasia",nmFilialUsuarioLogado);
		setElementValue("ordenadoPor", "F");
	}

//-->
</script>
