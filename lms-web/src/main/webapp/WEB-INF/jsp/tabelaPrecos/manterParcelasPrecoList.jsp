<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.parcelaPrecoService">
	<adsm:form action="/tabelaPrecos/manterParcelasPreco">
		<adsm:combobox label="tipoParcela" property="tpParcelaPreco" domain="DM_TIPO_PARCELA" labelWidth="17%" width="33%"/>
		<adsm:combobox label="tipoDePrecificacao" property="tpPrecificacao" domain="DM_INDICADOR_CALCULO_PARCELA" labelWidth="17%" width="33%"/>

		<adsm:textbox label="parcela" property="nmParcelaPreco" dataType="text" labelWidth="17%" width="33%" maxLength="60" size="33"/>
		<adsm:textbox label="descricao" property="dsParcelaPreco" dataType="text" labelWidth="17%" width="33%" maxLength="60" size="33"/>

		<adsm:combobox label="indicadorCalculo" property="tpIndicadorCalculo" domain="DM_INDICADORES_CALCULO" labelWidth="17%" width="33%"/>
		<adsm:combobox label="incideICMS" property="blIncideIcms" domain="DM_SIM_NAO" labelWidth="17%" width="33%"/>

		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="17%" width="33%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="parcelaPreco"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="parcelaPreco" idProperty="idParcelaPreco" defaultOrder="nmParcelaPreco" gridHeight="200" unique="true" scrollBars="horizontal">
		<adsm:gridColumn title="parcela" property="nmParcelaPreco" width="200"/>
		<adsm:gridColumn title="descricao" property="dsParcelaPreco" width="200"/>
		<adsm:gridColumn title="tipoParcela" property="tpParcelaPreco" width="120" isDomain="true"/>
		<adsm:gridColumn title="tipoDePrecificacao" property="tpPrecificacao" width="130" isDomain="true"/>
		<adsm:gridColumn title="indicadorCalculo" property="tpIndicadorCalculo" width="180" isDomain="true"/>
		<adsm:gridColumn title="incideICMS" property="blIncideIcms" width="110" renderMode="image-check"/>
		<adsm:gridColumn title="embutirParcela" property="blEmbuteParcela" width="110" renderMode="image-check"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" width="90" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>