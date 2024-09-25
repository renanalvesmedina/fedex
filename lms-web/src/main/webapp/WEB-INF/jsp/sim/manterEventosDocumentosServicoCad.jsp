<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.manterEventosDocumentosServicoAction">
	<adsm:form action="/sim/manterEventosDocumentosServico" idProperty="idEvento">
		<adsm:textbox dataType="integer" property="cdEvento" labelWidth="20%" width="80%" required="true" label="evento" size="5" maxValue="999" minValue="0" maxLength="3"/>
		<adsm:hidden property="tpSituacaoDescricaoEvento" value="A"/>
		<adsm:lookup property="descricaoEvento" idProperty="idDescricaoEvento" required="true" criteriaProperty="cdDescricaoEvento" 
				service="lms.sim.manterEventosDocumentosServicoAction.findLookupDescricaoEvento" dataType="integer" label="descricaoEvento" size="3" 
				action="/sim/manterLocalizacoesCliente" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="1"
				exactMatch="true" maxLength="3" disabled="false">
			<adsm:propertyMapping criteriaProperty="tpSituacaoDescricaoEvento" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="descricaoEvento.dsDescricaoEvento" modelProperty="dsDescricaoEvento" />
			<adsm:textbox property="descricaoEvento.dsDescricaoEvento" dataType="text" size="60" disabled="true" />
		</adsm:lookup>
		<adsm:hidden property="tpSituacaoLocalizacaoMercadoria" value="A"/>
		<adsm:lookup property="localizacaoMercadoria" idProperty="idLocalizacaoMercadoria" cellStyle="vertical-align:bottom;" criteriaProperty="cdLocalizacaoMercadoria" 
				service="lms.sim.manterEventosDocumentosServicoAction.findLookupLocalizacaoMercadoria" dataType="integer" label="localizacaoMercadoria" size="3" 
				action="/sim/manterLocalizacoesMercadoria" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="1"
				exactMatch="true" maxLength="3" disabled="false">
			<adsm:propertyMapping criteriaProperty="tpSituacaoLocalizacaoMercadoria" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="localizacaoMercadoria.dsLocalizacaoMercadoria" modelProperty="dsLocalizacaoMercadoria" />
			<adsm:textbox property="localizacaoMercadoria.dsLocalizacaoMercadoria" dataType="text" size="60" disabled="true" />
		</adsm:lookup>
		<adsm:hidden property="tpSituacaoLocalEvento" value="A"/>
		<adsm:lookup property="localEvento" idProperty="idLocalEvento"  criteriaProperty="cdLocalEvento" 
				service="lms.sim.manterEventosDocumentosServicoAction.findLookupLocalEvento" dataType="integer" label="localEvento" size="3" 
				action="/sim/manterLocaisEventosDocumentosServico" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="1"
				exactMatch="true" maxLength="3" disabled="false">
			<adsm:propertyMapping criteriaProperty="tpSituacaoLocalEvento" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="localEvento.dsLocalEvento" modelProperty="dsLocalEvento" />
			<adsm:textbox property="localEvento.dsLocalEvento" dataType="text" size="60" disabled="true" />
		</adsm:lookup>
		<adsm:hidden property="tpSituacaoCancelaEvento" value="A"/>
		<adsm:lookup property="cancelaEvento" idProperty="idEvento"  criteriaProperty="cdEvento" 
				service="lms.sim.manterEventosDocumentosServicoAction.findLookupEvento" dataType="integer" label="retornarEvento" size="3" 
				action="/sim/manterEventosDocumentosServico" labelWidth="20%" width="80%"
				exactMatch="true" maxLength="3">
			<adsm:propertyMapping criteriaProperty="tpSituacaoCancelaEvento" modelProperty="tpSituacao"/>	
			<adsm:propertyMapping relatedProperty="cancelaEvento.dsEvento" modelProperty="dsEvento" />
			<adsm:textbox property="cancelaEvento.dsEvento" dataType="text" size="60" disabled="true" />
		</adsm:lookup>
		
		<adsm:combobox  property="tpEvento" label="tipoEvento" required="true" labelWidth="20%" width="30%" domain="DM_TIPO_EVENTO_DOCUMENTO_SERVICO"/>
		<adsm:checkbox property="blExibeCliente" label="exibeCliente"  />
		<adsm:checkbox property="blGeraParceiro" label="geraParceiro" labelWidth="20%" width="30%"  />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" labelWidth="15%" width="35%" />
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
