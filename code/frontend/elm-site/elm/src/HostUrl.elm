module HostUrl exposing (..)

targetUrl : String
targetUrl = "http://localhost:5000"

targetUrlPath : String -> String 
targetUrlPath path 
    = String.append targetUrl path