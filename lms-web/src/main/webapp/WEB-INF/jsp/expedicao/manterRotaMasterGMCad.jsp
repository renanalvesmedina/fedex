<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Seta id da pessoa após inserir um registro.
	 * Desabilita tipo e número de identificação após inserir um registro.
	 */
	function loadRotaEmbarque_cb(data, exception){
		onDataLoad_cb(data, exception);
		setElementValue("idRotaEmbarque",data.idRotaEmbarque);
	} 
</script>

<adsm:window service="lms.gm.rotaEmbarqueService">
	<adsm:form action="/expedicao/manterRotaMasterGM" idProperty="idRotaEmbarque" onDataLoadCallBack="loadRotaEmbarque">
		<adsm:hidden property="idRotaEmbarque"/>
		<adsm:textbox 
			dataType="text" 
			property="siglaRota" 
			label="sigla" 
			maxLength="5" 
			size="15" 
			required="true"
		/>
		<adsm:textbox 
			dataType="text" 
			property="nomeRota" 
			label="nome" 
			maxLength="25" 
			size="30" 
			required="true"
		/>
		<adsm:textbox 
			label="horarioCorte" 
			property="horarioCorte" 
			dataType="JTTime" 
			labelWidth="21%" 
			width="29%" 
			required="true"
		/>
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
