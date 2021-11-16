

import { ByteInflow } from '../bytes/ByteInflow.js';


class HeSystem {


	constructor() {

		// retrieve origin
		this.origin = window.location.origin;

		// build stylesheets map
		this.stylesheets = new Map();
	}


	import_CSS(name) {
		if (!this.stylesheets.has(name)) {
			document.head.innerHTML += `<link type="text/css" rel="stylesheet" href=${name}>`;
			this.stylesheets.set(name, true);
		}
	}



	sendRequest_GET(requestPath, requestType, responseCallback) {

		/**
				* Relies on browser cache for speeding things up
				*/
		let xhr = new XMLHttpRequest();

		// first line
		xhr.open("GET", this.origin + requestPath, true);
		xhr.responseType = requestType;

		// headers
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.setRequestHeader('Access-Control-Allow-Origin', "*");
		xhr.setRequestHeader('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS');
		xhr.setRequestHeader('Access-Control-Allow-Headers', 'Cookie, Content-Type, Authorization, Content-Length, X-Requested-With');
		xhr.setRequestHeader('Access-Control-Expose-Headers', 'Set-Cookie, X-Powered-By');

		let _this = this;
		// Hook the event that gets called as the request progresses
		xhr.onreadystatechange = function () {
			// If the request is "DONE" (completed or failed)
			if (xhr.readyState == 4) {
				// If we got HTTP status 200 (OK)
				if (xhr.status == 200) {
					responseCallback(xhr.responseText);
				}
			}
		};

		// fire request
		xhr.send(null);
	}


	/**
	
	*/
	sendRequest_POST(requestArrayBuffer, responseCallback) {

		// create request
		let request = new XMLHttpRequest();

		// setup XMLHttpRequest.open(method, url, async)
		request.open("POST", this.origin, true);

		// ask for array buffer type reponse
		request.responseType = "arraybuffer";

		// callback
		request.onreadystatechange = function () {
			if (this.readyState == 4 && this.status == 200) {
				let responseArrayBuffer = request.response; // Note: not oReq.responseText
				if (responseArrayBuffer) {
					let inflow = new ByteInflow(responseArrayBuffer);
					responseCallback(inflow);
				}
				else {
					console.log("[Helium/system] No response array buffer");
				}
			}
		};

		// fire
		request.send(requestArrayBuffer);
	}



	removeChildren(node) {
		/* An earlier edit to this answer used firstChild, 
		but this is updated to use lastChild as in computer-science, 
		in general, it's significantly faster to remove the last 
		element of a collection than it is to remove the first element 
		(depending on how the collection is implemented). */
		while (node.firstChild) {
			node.removeChild(node.lastChild);
		}
	}
}


/**

*/
export const system = new HeSystem();