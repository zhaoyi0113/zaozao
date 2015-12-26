define(['angular'], function(angular) {

    var user = angular.module("userServiceModule", []);
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

        this.updateUser = function(user){
            for(var i=0; i<this.userList.length; i++){
                var u = this.userList[i];
                if(u.userId === user.userId){
                    this.userList[i] = user;
                    break;
                }
            }
        }
    });
});