## Url of Host Server REST API is defined in
[elm/src/HostUrl.elm](elm/src/HostUrl.elm)
Recompilation required for change
```
cd elm
elm make src/Main.elm       --output=site/main.js
elm make src/Login.elm      --output=site/login.js
elm make src/MakePoll.elm   --output=site/makePoll.js
```
Current URL is: http://localhost:5000

with paths
* /polls
* /polls/vote
* /login
* /register

Python flask server avaliable for testing 

⚠️Should be removed deleted before deployment⚠️