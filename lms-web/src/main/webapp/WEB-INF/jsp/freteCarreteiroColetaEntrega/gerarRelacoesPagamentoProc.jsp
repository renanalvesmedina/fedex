<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/freteCarreteiroColetaEntrega/gerarRelacoesPagamento">
		<adsm:lookup service="" dataType="text" property="proprietario.id" criteriaProperty="proprietario.codigo" label="proprietario" size="10" maxLength="50" width="14%" labelWidth="20%" action="/contratacaoVeiculos/manterProprietarios" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeProprietario"/> 
        </adsm:lookup>
		<adsm:textbox dataType="text" property="nomeProprietario" size="30" maxLength="50" disabled="true" width="66%" />
		<adsm:textbox dataType="date" property="dataPagamento" label="dataPagamento" labelWidth="20%" width="80%" />	
		
		<adsm:section caption="regeracao" />
		<adsm:textbox dataType="integer" property="relacaoPagamento" label="relacaoPagamento" maxLength="10" labelWidth="20%" width="80%" />
		<adsm:buttonBar>
			<adsm:reportViewerButton caption="imprimirRelacao" reportName="/freteCarreteiroViagem/emitirRecibosEnviadosPagamento.jasper"/>
			<adsm:button caption="gerar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   