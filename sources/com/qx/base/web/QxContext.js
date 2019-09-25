

/**
 * QxContext : base class for requesting server
 */


const ctx = new QxContext();


//N2 environment
function QxContext(){
 
    // Acquire server URL
    this.serverURL = location.protocol+'//'+location.hostname+(location.port ? ':'+location.port: '');
    
}


