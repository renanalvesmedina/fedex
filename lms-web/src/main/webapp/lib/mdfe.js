function Mdfe() {
	
	this.urlGerarMdfe = "";
	this.gerarMdfeCallId = "";
	this.data = null;
	
	this.urVerificaAutorizacaoMdfe = "";
	this.verificaAutorizacaoMdfeCallId = "";
	
	this.urlEncerrarMdfesAutorizados = "";
	this.encerrarMdfesAutorizadosCallId = "";
	
	this.urlVerificaEncerramentoMdfe = "";
	this.verificaEncerramentoMdfeCallId = "";
	
	this.urlImprimirMdfe = "";
	
	this.idsManifestoEletronico = null;
	this.dhEmissao = null;
	this.imprimeMdfeAutorizado = null;
	this.nfMdfe = null;
	
	this.chaveMensagemFinal = "";

	/**
	 * gerarMdfe
	 */
	this.gerarMdfe = function(data, contingenciaConfirmada) {
		this.data = data;
		this.idControleCarga = getNestedBeanPropertyValue(data, "controleCarga.idControleCarga");
		if (contingenciaConfirmada) {
			this.data.contingenciaConfirmada = true;
		}
		var sdo = createServiceDataObject(this.urlGerarMdfe, this.gerarMdfeCallId, this.data);
	 	xmit({serviceDataObjects:[sdo]});
	};
	
	/**
	 * gerarMdfeCallback
	 */
	this.gerarMdfeCallback = function(data) {
		
		if (data.hasContingencia) {
			alert(data.confirm_key + " - " + data.confirm_message);
			this.gerarMdfe(this.data, true);
			return false;
			
		}
		
		if (data.encerrarMdfesAutorizados) {
			this.encerrarMdfesAutorizados();
			return false;
		}
		
		this.idsManifestoEletronico = data.idsManifestoEletronico;
		this.dhEmissao = data.dhEmissao;
		this.imprimeMdfeAutorizado = data.imprimeMdfeAutorizado;
		this.nfMdfe = data.nfMdfe;
		if (data.aguardarAutorizacaoMdfe) {
			this.verificaAutorizacaoMdfe();
			return false;
		}
		
		return true;
	};

	/**
	 * verificaAutorizacaoMdfe
	 */
	this.verificaAutorizacaoMdfe = function() {
		var data = new Array();
		data.idsManifestoEletronico = this.idsManifestoEletronico;
		data.dhEmissao = this.dhEmissao;
		this.showMessageMDFe();
		var sdo = createServiceDataObject(this.urVerificaAutorizacaoMdfe, this.verificaAutorizacaoMdfeCallId, data);
	 	xmit({serviceDataObjects:[sdo]});
	};
	
	/**
	 * verificaAutorizacaoMdfeCallback
	 */
	this.verificaAutorizacaoMdfeCallback = function(data) {
		if(data.limiteEspera) {
			/* Caso MDFe não tenha sido autorizada cria loop de chamadas intervaladas para não estourar timeout do xmit. */
			var self = this;
			setTimeout(function() {
					unblockUI();
					self.verificaAutorizacaoMdfe();
					return false;
				},
				data.limiteEspera*1000);
			return false;
			
		}
		/* Libera a tela e some com o loader que tinha sido setado por tempo ilimitado. */
		this.hideMessageMDFe();
		return true;
	};
	
	/**
	 * imprimirMDFe
	 */
	this.imprimirMDFe = function() {
		if (this.imprimeMdfeAutorizado) {
			//Imprime DAMDFE
			var parameters = new Object();
			parameters.idsManifestoEletronico = this.idsManifestoEletronico;
			var sdo = createServiceDataObject(this.urlImprimirMdfe, "openPdf", parameters);
			executeReportWindowed(sdo, 'pdf', "RELATORIO_MDFE");
			alert(this.chaveMensagemFinal + " - " + getI18nMessage(this.chaveMensagemFinal, this.nfMdfe,false));
		}
	};
	
	/**
	 * encerrarMdfesAutorizados
	 */
	this.encerrarMdfesAutorizados = function() {
		var data = new Object();
		setNestedBeanPropertyValue(data, "controleCarga.idControleCarga", this.idControleCarga);
		var sdo = createServiceDataObject(this.urlEncerrarMdfesAutorizados, this.encerrarMdfesAutorizadosCallId, data);
	 	xmit({serviceDataObjects:[sdo]});
	};
	
	/**
	 * verificaEncerramentoMdfe
	 */
	this.verificaEncerramentoMdfe = function(data) {
		this.showMessageMDFe();
		var sdo = createServiceDataObject(this.urlVerificaEncerramentoMdfe, this.verificaEncerramentoMdfeCallId, data);
	 	xmit({serviceDataObjects:[sdo]});
	};
	
	/**
	 * verificaEncerramentoMdfe_cb
	 */
	this.verificaEncerramentoMdfeCallback = function(data) {
		if(data.limiteEspera) {
			var self = this;
			setTimeout(function() {
					self.verificaEncerramentoMdfe(data);
					return false;
					},
				data.limiteEspera*1000);
			
			return false;
			
		}
		this.hideMessageMDFe();
		return true;
		
	};
	
	/* 	Exibe loader e bloqueia tela por tempo ilimitado. O mesmo só será removido na função 'hideMessageMDFe'. 
	Implementado dessa forma para garantir que tela não fique liberada nos intervalos das chamadas, e para que loader seja exibido durante toda a espera do processo de autorização da MDFe. */
	this.showMessageMDFe = function() {
		blockUI();
		var doc = getMessageDocument(null);
		var messageMDFe = doc.getElementById("messageMDFe.div");
		if(messageMDFe == null || messageMDFe == undefined){
			showSystemMessage('processando', null, true);
			var messageSign = doc.getElementById("message.div");
			if(messageSign != null && messageSign != undefined){
				var messageMDFe = messageSign;
				messageMDFe.id = 'messageMDFe.div';
				doc.body.appendChild(messageMDFe);
				
			}
		}
	};
		
	this.hideMessageMDFe = function() {
		unblockUI();
		var doc = getMessageDocument(null);
		var messageMDFe = doc.getElementById("messageMDFe.div");
		if(messageMDFe != null && messageMDFe != undefined){
			doc.body.removeChild(messageMDFe)
		}
	};


}