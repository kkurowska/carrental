(function() {
    'use strict';
    angular
        .module('carrentalApp')
        .factory('Rent', Rent);

    Rent.$inject = ['$resource', 'DateUtils'];

    function Rent ($resource, DateUtils) {
        var resourceUrl =  'api/rents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.rent_date = DateUtils.convertLocalDateFromServer(data.rent_date);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.rent_date = DateUtils.convertLocalDateToServer(copy.rent_date);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.rent_date = DateUtils.convertLocalDateToServer(copy.rent_date);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
