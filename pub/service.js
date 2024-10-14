app.service('myserv', function() {
          this.getServiceList = function () {
    return [];
}
this.getISEndpoint = function() { 
 return 'http://ZEUSPC.mshome.net:5555/';
}
this.getAPIList = function() { 
 return [];
}
this.getCreatedTime = function() { 
 return "21-03-2024 10:22:02 ICT";
}
this.getPackageInfo = function(){
 return{"packageName":"BankTransaction","createdDate":"13-03-2024 15:26:56 ICT","version":"1.0"};
}
});
