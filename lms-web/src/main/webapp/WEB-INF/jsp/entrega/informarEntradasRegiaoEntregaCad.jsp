<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/entrega/informarEntradasRegiaoEntrega">
		<adsm:masterLink showSaveAll="false">
			<adsm:masterLinkItem property="empresa" label="empresa" itemWidth="50" />
			<adsm:masterLinkItem property="filial" label="filial" itemWidth="50" />
			<adsm:masterLinkItem property="controleCarga" label="controleCargas" itemWidth="50" />
			<adsm:masterLinkItem property="rotaEntrega" label="rotaEntrega" itemWidth="50" />
		</adsm:masterLink>

		<adsm:textbox label="documentoServico" labelWidth="20%" width="8%" size="6" maxLength="3" dataType="text" property="filialOrigem"  disabled="true" />
    	<adsm:textbox width="11%" size="10" dataType="text" property="numeroDocumento" disabled="true"/>
        <adsm:label key="hifen" width="1%" style="border:none;"/>
        <adsm:textbox dataType="text" property="numeroDocumentoCompl" width="10%" size="5" style="width: 15px;" disabled="true"/>

		<adsm:textbox dataType="text" property="tipo" label="tipo" size="20" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:textbox dataType="text" property="rotaEntregaOriginal" label="rotaEntregaOriginal" maxLength="20" size="20" labelWidth="20%" width="80%" disabled="true"/>

		<adsm:buttonBar freeLayout="true" />
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" gridHeight="200" showCheckbox="true" disableMarkAll="true">
		<adsm:gridColumn title="documentoServico" property="docServ" width="35%"/>
		<adsm:gridColumn title="tipo" property="tp" width="35%"/>
		<adsm:gridColumn title="rotaEntregaOriginal" property="rotaEO" width="30%" align="right"/>
		<adsm:buttonBar>
			<adsm:button caption="confirmarEntradaDocumentos" boxWidth="230"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>