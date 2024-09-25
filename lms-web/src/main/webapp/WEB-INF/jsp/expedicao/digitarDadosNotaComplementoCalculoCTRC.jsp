<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/expedicao/digitarDadosNotaComplementoCalculoCTRC">

		<adsm:section caption="calculoCTRC" width="87"/>

		<adsm:label key="branco" width="1%"/>
		<adsm:ctrc property="conhecimentoComplementar" label="conhecimentoComplementar" labelWidth="26%" width="73%" />

		<adsm:label key="branco" width="1%"/>
		<adsm:ctrc property="romaneioComplementar" label="romaneioComplementar" labelWidth="26%" width="73%" hideDigit="true"/>

		<adsm:label key="branco" width="1%"/>
		<adsm:textarea columns="100" rows="2" maxLength="255" property="motivo" label="motivo" width="84%"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none"/>

		<adsm:section caption="calculoFrete" width="87"/>

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox dataType="text" size="40" property="parcela" label="parcela" maxLength="10" width="34%"/>
		<adsm:textbox dataType="text" size="10" property="valor" label="valorReais" maxLength="10" width="34%"/>

		<adsm:label key="espacoBranco" width="100%" style="border:none"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none"/>

		<adsm:label key="branco" width="50%"/>
		<adsm:textbox dataType="text" size="10" property="totalFrete" label="totalFreteReais" maxLength="10"/>

		<adsm:buttonBar>
			<adsm:button caption="cancelarTudo"/>
			<adsm:button caption="voltarDadosGerais"/>
			<adsm:button caption="gerarCTRC"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>