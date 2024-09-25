<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterTiposRegistroComplementoDocumentoServicoAction">
	<adsm:form action="/expedicao/manterTiposRegistroComplementoDocumentoServico" 
		idProperty="idTipoRegistroComplemento" newService="lms.expedicao.manterTiposRegistroComplementoDocumentoServicoAction.newMaster">
		<adsm:textbox maxLength="60" size="33" dataType="text" 
			property="dsTipoRegistroComplemento" label="descricao" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>