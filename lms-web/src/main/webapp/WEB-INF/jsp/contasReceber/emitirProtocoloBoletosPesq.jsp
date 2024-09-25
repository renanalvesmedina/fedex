<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form action="/contasReceber/emitirProtocoloBoletos">

		<adsm:lookup label="filialFaturamento" service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" 
				size="5" maxLength="5" action="/municipios/manterFiliais" labelWidth="20%" width="80%"> 
			<adsm:propertyMapping modelProperty="nome" formProperty="filial"/> 
			<adsm:textbox property="filial" dataType="text" size="25" disabled="true" width="20%" required="true"/>
		</adsm:lookup>

		<adsm:range label="periodoEmissao" labelWidth="20%" width="80%" required="true">
			<adsm:textbox property="emissaoIni" dataType="JTDate"/>
			<adsm:textbox property="emissaoFim" dataType="JTDate"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:reportViewerButton caption="visualizar" reportName="/contasReceber/emitirProtocoloBoletos.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>