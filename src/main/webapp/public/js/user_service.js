define(['angular'], function(angular) {

    var user = angular.module("userService", []);
    user.service('UserService', function(){
       this.userList = [];

        this.setUserList = function(users){
            this.userList = users;
        }

        this.getUserById = function(id){
            for(var i=0; i<this.userList.length; i++){
                var user = this.userList[i];
                if(id === user.userId.toString()){
                    return user;
                }
            }
            return null;
        }
    });
});