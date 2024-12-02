module Poll exposing (..)

import Json.Decode as D
import Json.Decode exposing (Decoder)
import Json.Encode as Encode


type alias Poll = { title : String, options : List Option}

type alias Option = { caption : String, votes : Int, id : Int, voted : Bool}

-- Final poll type
{-
    Poll =
        title :     String
        option :    List Option
        startTime : Time
        endTime :   Time

    Option =
        caption :   String
        voters  :   List Users
-}

optionDecoder : Decoder Option
optionDecoder = D.map4 Option
    (D.field "question" D.string)
    (D.field "votes" D.int)
    (D.field "id" D.int)
    (D.succeed False)

pollDecoder : Decoder Poll
pollDecoder =  D.map2 Poll
    (D.field "title" D.string)
    (D.field "options" (D.list optionDecoder))

pollsDecoder : Decoder (List Poll)
pollsDecoder =
    D.list pollDecoder

{--
itemToJson : Poll -> Encode.Value
itemToJson i = 
        Encode.object 
            [  ("title",        Encode.string i.title),
               ("description",  Encode.string i.description)
            ]

-}