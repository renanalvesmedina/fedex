<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="selecionarNotasFiscais">
	<adsm:section caption="selecionarNotasFiscais" />
	<adsm:grid paramProperty="id" gridHeight="150" rows="9">
		<adsm:gridColumn property="notaFiscal" title="notaFiscal" align="right"/>
		<adsm:gridColumn property="dataEmissao" title="dataEmissao" align="center"/>
		<adsm:buttonBar>
			<adsm:button caption="selecionar" onclick="" />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
