<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/expedicao/demonstrativoCRTsEmitidos">
		<adsm:range label="periodoEmissaoCRT" required="true" labelWidth="20%" width="35%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal"/>
		</adsm:range>

		<adsm:textbox dataType="JTDate" property="dtReferenciaCotacao" label="dataReferenciaCotacao" labelWidth="25%" width="15%"/>

		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		<adsm:lookup
			label="filial"
			property="filial"
			service="lms.expedicao.demonstrativoCRTsEmitidosAction.findFilial"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			criteriaSerializable="true"
			dataType="text"
			size="5"
			labelWidth="20%"
			width="35%"
			maxLength="3">
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.expedicao.demonstrativoCRTsEmitidosAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>