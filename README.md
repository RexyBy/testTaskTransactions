# Test task

## Database setup

You need the MySQL database to run this application. The simplest way to do that is run it into docker container:

```
docker run --name mysql -e MYSQL_ROOT_PASSWORD=mysecretpassword -e MYSQL_USER=appuser -e MYSQL_PASSWORD=mysecretpassword -e MYSQL_DATABASE=appdb -p 3305:3306 -d mysql:8.1
```

## Search

The search work in the following way: you must send the GET request to the following endpoint:

```
curl --location 'http://localhost:8080/api/transaction/search?filters=TIMESTAMP_TO%3A1694808000%2CTIMESTAMP_FROM%3A1694635200'
```

The filters param supports the following values:

| Filter          | Meaning                                                                                                   | Type     |
|-----------------|-----------------------------------------------------------------------------------------------------------|----------|
| ACTOR           | filter by actor                                                                                           | Multiple |
| TYPE            | filter by type                                                                                            | Multiple |
| TIMESTAMP_FROM  | filter transactions after this timestamp (inclusive)                                                      | Single   |
| TIMESTAMP_TO    | filter transactions before this timestamp (inclusive)                                                     | Single   |
| ANY_OTHER_VALUE | you may add any other value as key. In this key this data will be searched in additional transaction data | Multiple |

Any multiple filter can be mentioned several times to make "OR" search. For instance, the following request:

```
curl --location 'http://localhost:8080/api/transaction/search?filters=TYPE%3AjustType%2C%20TYPE%3AsuperType%2C%20ACTOR%3Aactor1'
```

will return all transactions that have type "justType" or "superType" and have actor "actor1".