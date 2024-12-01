This is the analytics component and an old version of the feed app, 
therefore not all the functionality is there like it should be.

To run this, make sure that you have both rabbitmq servers and mongodb servers running
on your computer. This can easily be done by using Docker images like this:
- docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
- docker run -d --name mongodb -p 27017:27017 mongo

After that you use ./gradlew bootRun in both the main application and also while inside the analytics component.
This should make everything run, and you can now POST polls by using apps like Bruno og Postman to 
http://localhost:8080/polls. These polls will then be automatically sent over to a mongodb database in a collection
called "analytics". To see the analytics, use "db.polls.find()", while inside mongosh and inside the 
analytics collection.