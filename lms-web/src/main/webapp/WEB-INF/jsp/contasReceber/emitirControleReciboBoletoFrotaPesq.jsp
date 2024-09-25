<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>

	<adsm:form action="/contasReceber/emitirControleReciboBoletoFrota">

		<adsm:textbox label="emissaoManifestoEntrega" labelWidth="30%" dataType="JTDate" property="dtEmissao" size="20" maxLength="20" required="true"/>

        <adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   labelWidth="30%" 
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirControleReciboBoletoFrotaAction" disabled="false"/>
			<adsm:resetButton/>	
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>