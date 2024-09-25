<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterFaturas"
		service="lms.contasreceber.manterFaturasAction.findByIdItemFatura"
		idProperty="idFatura">

		<adsm:combobox property="idMotivoOcorrencia" label="motivoBloqueio" labelWidth="15%" width="85%" required="true"
			service="lms.contasreceber.manterBloqueioFaturamentoAction.findMotivoOcorrenciaBloqueio"
			optionProperty="idMotivoOcorrencia" optionLabelProperty="dsMotivoOcorrencia" />
			
		<adsm:textbox label="previsaoTratativa" property="dtPrevisao" dataType="JTDate" size="10" maxLength="10" picker="true" labelWidth="15%" width="85%" required="true" />
		<adsm:textarea label="descricaoMotivo" property="dsBloqueio" maxLength="500" columns="65" rows="3" labelWidth="15%" width="85%"/>
		<adsm:hidden property="idFatura"/>
		<adsm:hidden property="origem"/>
		
		<adsm:buttonBar>
			<adsm:storeButton caption="salvar" id="btnSalvarBloqueio" service="lms.contasreceber.manterFaturasAction.storeBloqueioFaturamento" callbackProperty="salvarBloqueio" />
			<adsm:button caption="cancelar" onclick="self.close();"  disabled="false"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script>
	function salvarBloqueio_cb(data, error, erromsg) {
		if (error != undefined) {
			alert(error + '');
			return;
		}

		window.returnValue = {hasExecuted: true};
		window.close();
	}
	
	function myOnPageLoad_cb(d,e,c,x){
		var u = new URL(parent.location.href);
		setElementValue("idFatura", u.parameters["idFatura"]); 
		setElementValue("origem", u.parameters["origem"]); 
		onPageLoad_cb(d,e,c,x);
	}
</script>