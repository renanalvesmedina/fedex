<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script>
	function validateTab() {
		return validaValoresForm();
	}
	
	function validaValoresForm(){
		var emp = getElementValue("empresa.pessoa.nrIdentificacao");
		var dip = getElementValue("dataInicialPromocao");
		var dfp = getElementValue("dataFinalPromocao");

		if (emp=='' && dip=='' && dfp==''){
			alertI18nMessage("LMS-00055");
			return false;
		}
		return true;
	}
</script>

<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00055"/>
	</adsm:i18nLabels>

	<adsm:form action="/tabelaPrecos/relatorioTarifasPromocionaisAereo">
		<adsm:hidden property="empresa.tpEmpresa" value="C" serializable="false"/>
		<!-- adsm:hidden property="empresa.tpSituacao" value="A" serializable="false"/ -->
		<adsm:lookup label="ciaAerea" 
			property="empresa" 
			service="lms.tabelaprecos.relatorioTarifasPromocionaisAereoAction.findLookupEmpresa" 
			action="/municipios/manterEmpresas" 
			idProperty="idEmpresa" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			size="20" 
			maxLength="20" 
			labelWidth="10%" 
			width="20%">
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="tpEmpresa"/>
			<!-- adsm:propertyMapping criteriaProperty="empresa.tpSituacao" modelProperty="tpSituacao"/ -->
			<adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="30" maxLength="60" width="70%" disabled="true"/>
        </adsm:lookup>

		<adsm:range label="periodo" labelWidth="10%" width="90%">
			<adsm:textbox dataType="JTDate" property="dataInicialPromocao"/>
			<adsm:textbox dataType="JTDate" property="dataFinalPromocao"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:reportViewerButton id="reportViewer" disabled="false" service="lms.tabelaprecos.tarifaPromocionalAereoService"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function myOnPageLoad_cb(erro, msg) {
		onPageLoad_cb(erro, msg);
		setDisabled("reportViewer", false);
	}
</script>
