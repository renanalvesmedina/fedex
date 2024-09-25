<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="210">
		  <adsm:section caption="parcelasServicoAdicional"/>
			<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" unique="false" gridHeight="250" showPaging="false">
				<adsm:gridColumn width="55%" title="parcela" property="PARCELA"/>
				<adsm:gridColumn width="45%" title="valor" property="VALOR" align="right"/>
			</adsm:grid>
			<adsm:label key="branco" width="55%" style="border:none;"/>
			<adsm:textbox dataType="text" property="TOTAL" label="totalServicosAdicionais" size="20" disabled="true" labelWidth="25%" width="20%"/>			
	</adsm:form>   
</adsm:window>   