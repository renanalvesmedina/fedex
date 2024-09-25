<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.manterEventosDocumentosServicoAction">
	<adsm:form action="/sim/manterEventosDocumentosServico" idProperty="idEvento" height="102">
		<adsm:textbox dataType="integer" property="cdEvento"  label="evento" labelWidth="20%" width="80%" maxValue="999" size="5" maxLength="3" minValue="0"/>
		<adsm:lookup property="descricaoEvento" idProperty="idDescricaoEvento" criteriaProperty="cdDescricaoEvento" 
				service="lms.sim.manterEventosDocumentosServicoAction.findLookupDescricaoEvento" dataType="integer" label="descricaoEvento" size="3" 
				action="/sim/manterLocalizacoesCliente" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="1"
				exactMatch="true" maxLength="3" disabled="false">
			<adsm:propertyMapping relatedProperty="descricaoEvento.dsDescricaoEvento" modelProperty="dsDescricaoEvento" />
			<adsm:textbox property="descricaoEvento.dsDescricaoEvento" dataType="text" size="60" disabled="true" />
		</adsm:lookup>
		<adsm:lookup property="localizacaoMercadoria" idProperty="idLocalizacaoMercadoria" cellStyle="vertical-align:bottom;" criteriaProperty="cdLocalizacaoMercadoria" 
				service="lms.sim.manterEventosDocumentosServicoAction.findLookupLocalizacaoMercadoria" dataType="integer" label="localizacaoMercadoria" size="3" 
				action="/sim/manterLocalizacoesMercadoria" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="1"
				exactMatch="true" maxLength="3" disabled="false">
			<adsm:propertyMapping relatedProperty="localizacaoMercadoria.dsLocalizacaoMercadoria" modelProperty="dsLocalizacaoMercadoria" />
			<adsm:textbox property="localizacaoMercadoria.dsLocalizacaoMercadoria" dataType="text" size="60" disabled="true" />
		</adsm:lookup>
		<adsm:lookup property="localEvento" idProperty="idLocalEvento" criteriaProperty="cdLocalEvento" 
				service="lms.sim.manterEventosDocumentosServicoAction.findLookupLocalEvento" dataType="integer" label="localEvento" size="3" 
				action="/sim/manterLocaisEventosDocumentosServico" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="1"
				exactMatch="true" maxLength="3" disabled="false">
			<adsm:propertyMapping relatedProperty="localEvento.dsLocalEvento" modelProperty="dsLocalEvento" />
			<adsm:textbox property="localEvento.dsLocalEvento" dataType="text" size="60" disabled="true" />
		</adsm:lookup>
		<adsm:lookup property="cancelaEvento" idProperty="idEvento"  criteriaProperty="cdEvento" 
				service="lms.sim.manterEventosDocumentosServicoAction.findLookupEvento" dataType="integer" label="retornarEvento" size="3" 
				action="/sim/manterEventosDocumentosServico" labelWidth="20%" width="80%"
				exactMatch="true" maxLength="3">
			<adsm:propertyMapping relatedProperty="cancelaEvento.dsEvento" modelProperty="dsEvento" />
			<adsm:textbox property="cancelaEvento.dsEvento" dataType="text" serializable="false" size="60" disabled="true" />
		</adsm:lookup>
		<adsm:combobox  property="tpEvento" label="tipoEvento" labelWidth="20%" width="30%" domain="DM_TIPO_EVENTO_DOCUMENTO_SERVICO"/>
		<adsm:combobox property="blExibeCliente" label="exibeCliente"  domain="DM_SIM_NAO"/>
		<adsm:combobox property="blGeraParceiro" label="geraParceiro" labelWidth="20%" width="30%"  domain="DM_SIM_NAO"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="15%" width="35%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="evento" />
			<adsm:resetButton />
		</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid idProperty="idEvento" property="evento" scrollBars="horizontal" defaultOrder="cdEvento" unique="true" 
			   gridHeight="182" rows="9" >
		<adsm:gridColumn property="cdEvento" title="evento" dataType="integer" width="50"/>
		<adsm:gridColumn property="descricaoEvento.descricaoEventoConcatenado" title="descricaoEvento" width="230"/>
		<adsm:gridColumn property="localizacaoMercadoria.localizacaoMercadoriaConcatenado" title="localizacaoMercadoria" width="230"/>
		<adsm:gridColumn property="localEvento.localEventoConcatenado" title="localEvento" width="150"/>
		<adsm:gridColumn property="tpEvento" title="tipoEvento" isDomain="true" width="100"/>
		<adsm:gridColumn property="blExibeCliente" title="exibeCliente" renderMode="image-check" width="80"/>
		<adsm:gridColumn property="blGeraParceiro" title="geraParceiro" renderMode="image-check" width="100"/>
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="60"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
    </adsm:grid>


</adsm:window>
