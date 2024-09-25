<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form action="/tributos/emitirValoresIVACompetencia">
		<adsm:range label="competencia">
			<adsm:textbox dataType="JTDate" property="competenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="competenciaFinal"/> 
		</adsm:range>
		<adsm:lookup action="/municipios/manterPaises" dataType="text" property="pais" service="" label="pais"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="tributos/emitirValoresIVACompetencia.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>