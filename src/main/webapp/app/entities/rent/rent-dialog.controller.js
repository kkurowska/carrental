(function() {
    'use strict';

    angular
        .module('carrentalApp')
        .controller('RentDialogController', RentDialogController);

    RentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Rent', 'Car', 'Customer', 'User'];

    function RentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Rent, Car, Customer, User) {
        var vm = this;

        vm.rent = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.cars = Car.query();
        vm.customers = Customer.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.rent.id !== null) {
                Rent.update(vm.rent, onSaveSuccess, onSaveError);
            } else {
                Rent.save(vm.rent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('carrentalApp:rentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.rent_date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
