# tax-service
Tax service by Matej Rakar

This REST Service expects a JSON request body with information about traderId, playedAmount and odd in GET request URI and, based on recieved data, 
fetches information about a trader from database and calculates possibleReturnAmount, possibleReturnAmountBefTax, possibleReturnAmountAfterTax, taxRate and taxAmount.
It then returns a JSON containing above mentioned calculated variables.

To test this REST service, please follow the steps bellow:

1. Install xampp on your machine.
2. Start Apache and MySql services from xampp.
3. Visit http://localhost/phpmyadmin/ and create DB schema named tax_data.
4. Then go to Import tab and import file named tax_data.sql from my repository.
5. Install IntelliJ IDEA IDE
6. Connect it with GitHub and clone my repository to your machine.
7. Run application
8. You should now be able to test by sending a GET request from Postman, e.g. </br>
http&#58;//localhost:8080/taxservice/{"traderId": 1,"playedAmount": 5,"odd": 3.2}
