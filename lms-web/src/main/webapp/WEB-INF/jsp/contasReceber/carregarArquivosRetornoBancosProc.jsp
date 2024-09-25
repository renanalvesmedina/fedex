<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window>
	<adsm:form action="/contasReceber/carregarArquivosRetornoBancos">
     	<adsm:combobox service="" optionLabelProperty="cedente.nome" optionProperty="cedente.codigo" property="cedente.codigo" label="cedente" required="true" labelWidth="18%" width="32%"/>
   	    <adsm:textbox label="dataArquivo" dataType="JTDate" property="dataArquivo" disabled="true" labelWidth="18%" width="32%" />
   	    <adsm:textbox label="arquivo" property="arquivo" dataType="file" size="72" required="true" labelWidth="18%" width="82%" />

		<adsm:buttonBar>
      		<adsm:reportViewerButton caption="relatorioOcorrencias" reportName="contasReceber/emitirOcorrenciasBancos.jasper"/>
			<adsm:button caption="importarArquivo"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>