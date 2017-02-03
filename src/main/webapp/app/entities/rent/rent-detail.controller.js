(function() {
    'use strict';

    angular
        .module('carrentalApp')
        .controller('RentDetailController', RentDetailController);

    RentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Rent', 'Car', 'Customer', 'User'];

    function RentDetailController($scope, $rootScope, $stateParams, previousState, entity, Rent, Car, Customer, User) {
        var vm = this;

        vm.rent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('carrentalApp:rentUpdate', function(event, result) {
            vm.rent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
