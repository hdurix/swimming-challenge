(function () {
    'use strict';

    angular.module('swimmingchallengeApp')
        .controller('AdministrationController', AdministrationController);

    function AdministrationController(User, Timeslot, AlertService) {

        var vm = this;

        vm.hasPay = hasPay;

        activate();

        function activate() {
            loadAll();
        }

        function loadAll() {
            vm.usersWithReserves = User.usersWithReserves();
        }

        function hasPay(user) {
            AlertService.success("L'utilisateur a bien payé ses " + (user.nbReserved * 9) + " €");
            Timeslot.hasPay({userId: user.id});
            vm.usersWithReserves = _.without(vm.usersWithReserves, user);
        }
    }
})();
