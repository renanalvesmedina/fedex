<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacoesSimplificadasWeb">
		<adsm:lookup action="/vendas/manterDadosIdentificacao" dataType="text" property="codigoCliente" label="cliente" service="" required="true" labelWidth="18%" width="15%" size="6">
			<adsm:propertyMapping formProperty="nomeCliente" modelProperty="nomeCliente"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="nomeCliente" width="45%" size="20" disabled="true"/>

		<adsm:textbox dataType="text" label="notaFiscal" property="notaFiscal" labelWidth="18%" width="82%"/>

		<adsm:textbox label="documentoServico" labelWidth="18%" width="8%" size="6" maxLength="3" dataType="text" property="filialOrigem" />
        <adsm:textbox width="10%" size="10" dataType="text" property="numeroDocumento"/>
        <adsm:label key="hifen" width="2%" style="border:none;text-align:center"/>
        <adsm:lookup action="" dataType="text" property="numeroDocumentoCompl" service="" width="59%" size="5" style="width: 15px;"/>
			
		<adsm:textbox dataType="text" label="pedido" property="pedido" labelWidth="18%" width="82%"/>
        
		<adsm:buttonBar>
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
        </adsm:buttonBar>
	</adsm:form>
</adsm:window>

