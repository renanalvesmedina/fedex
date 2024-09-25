<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>

	<adsm:form action="/tributos/manterArquivoFronteiraRapidaOrigem">

		<adsm:masterLink showSaveAll="false">
			<adsm:masterLinkItem property="manifestoViagem" label="manifestoViagem" />
		</adsm:masterLink>

	</adsm:form>

	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true">
		<adsm:gridColumn title="tipoDocumento" property="tipoDocumento"/>
		<adsm:gridColumn title="documentosServico" property="numeroDocumento" />
		<adsm:gridColumn title="dataEmissao" property="dataEmissao" />
		<adsm:gridColumn title="valor" property="valor" />
		<adsm:buttonBar>
			<adsm:button caption="excluirDocumentoGeracaoArquivo" boxWidth="245"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>