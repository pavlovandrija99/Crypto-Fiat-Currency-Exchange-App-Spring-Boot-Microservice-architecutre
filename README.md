## Crypto-Fiat-Currency-Exchange-App-Spring-Boot-Microservice-architecutre

* Tecnhologies used for this project:
  - Spring Tool Suite
  - Java programming language
  - H2 in-memory database
  - Docker
  
* Following microservices are developed:

  1. Naming server - Microservice which uses Eureka Server. Every microservice will register into the Eureka Server and Eureka Server knows all the client
                     applications running on each port and IP address.
             
  2. Currency exchange - Microservice which comunicates with H2 database, that stores data about exchange rates for different currencies (RSD, EUR, USD, GBP,                              CHF). 
  
  3. Currency conversion - Microservice, represents an endpoint for all users requests for currency exchange. Currency conversion microservice identifies amount
                           of the currency that is requested to be exchanged, and checks if user that is sending a request, has the necessary currency amount on                                  his bank account. If that's the case, exchange of the assets is beginning. Currency conversion microservice iz communicating with bank                                  account, as well as currency exchange microservice.
                           
  4. Bank account - Microservice, connected with H2 in-memory database, that stores data about users bank account state, amount of assets on users bank accounts.
                    For every bank account binds unique email address, which servers as an identification mark.
                    
  5. Crypto exchange - Microservice which comunicates with H2 database, that stores data about exchange rates for different crypto currencies (ETH, BTC, LTC,                                  XRP).
  
  6. Crypto wallet - Microservice that is connected to H2 database, acquire data about amount of mentioned crypto currencies that users have on his account.
  
  7. Crypto conversion - Microservice represents an endpoint for all users requests for exchanging crypto currency assets, identifies amount
                         of the currency that is requested to be exchanged, and checks if user that is sending a request, has the necessary currency amount                                      on his crypto wallet account. If that's the case, exchange of the assets is beginning. Crypto conversion microservice iz                                                communicating with crypto wallet microservice, as well as crypto exchange microservice. 
                           
  8. API Gateway - Microservice, entry point of the application. All users request are directed to this microservice. API Gateway iz running on port 8765. 
 
                   
                   Examples of users request to the API Gateway: 
                   
                   1. localhost:8765/currency-conversion/from/XXX/to/YYY/quantity/QQQ/user/UUU
                    - Currency exchange request, QQQ stands for amount of XXX currency, that exchanges into YYY currency. UUU stands for email of the user that is sending request.
                   
                   2. localhost:8765/crypto-conversion/from/XXX/to/YYY/quantity/QQQ/user/UUU
                    - Crypto currency exhcangge request, QQQ stands for amount of XXX crypro currency, that exchanges into YYY crypto currency. UUU stands for the email of the user that is sending request.
                  
 * Feign Client (from Spring Cloud) is used for simplifying and making effective communication between microservices.
 * All microservices are connected to ZIPKIN tracing server, which tracks HTTP request thah are sent to any of microservices, and provides useable information about      request state/result. 
 * Every microservice is dockerized using Docker (docker-compose.yaml file).          
