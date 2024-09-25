<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacoesSimplificadasWeb">
		<adsm:section caption="localizacao"/>
		<adsm:textbox dataType="text" property="entregaPrevista" label="entregaPrevista" labelWidth="21%" width="82%" disabled="true"/>
		<adsm:textbox dataType="text" property="entregaRealizada" label="entregaRealizada" labelWidth="21%" width="82%" disabled="true"/>
		<adsm:textbox dataType="text" property="motivoNaoEntrega" label="motivoNaoEntrega" labelWidth="21%" width="82%" disabled="true"/>
		<adsm:textbox dataType="text" property="localizacaoMercadoria" label="localizacaoMercadoria" labelWidth="21%" width="82%" disabled="true"/>
		
		<adsm:section caption="dadosDocumentoServico"/>
		<adsm:textbox dataType="text" property="numero" disabled="true" label="numero" labelWidth="21%" width="82%"/>
		
		<adsm:section caption="notasFiscaisDocumentoServico"/>
		<adsm:grid paramProperty="id" paramId="id" rows="8" showCheckbox="false" unique="false" gridHeight="185">
			<adsm:gridColumn property="notaFiscal" title="notaFiscal" align="left"/>
			<adsm:gridColumn property="dataEmissao" title="dataEmissao"/>
			<adsm:gridColumn property="volumes" title="volumes"/>
			<adsm:gridColumn property="peso" title="peso" unit="kg"/>
            <adsm:buttonBar>
				<adsm:button caption="voltar"/>
			</adsm:buttonBar>	
		</adsm:grid>
	</adsm:form>
</adsm:window>