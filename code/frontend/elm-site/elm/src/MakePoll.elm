port module MakePoll exposing (..)

import Browser
import Browser.Navigation as Navigation
import Url.Builder as Url

import Html exposing (Html, button, div, text)
import Html.Events exposing (onClick, onInput)
import Html exposing (..)
import Html.Attributes exposing (type_,placeholder,value,for,value,style)

import Http exposing (request,jsonBody)
import Json.Decode as D
import Json.Decode exposing (Decoder)
import Json.Encode as Encode

import Poll exposing (..)
import CmdAid exposing (..)
import ListAid exposing (..)

import HostUrl exposing (..)


-- MAIN

main =
  Browser.element
    { init = init
    , update = update
    , subscriptions = subscriptions
    , view = view
    }


-- PORTS

port requestSession : () -> Cmd msg
port sendSession : String -> Cmd msg
port recciveSession : (String -> msg) -> Sub msg


-- MODEL

type alias Model 
    = {title : String, options : List String, comment : String, sessionID : Maybe String}

type Msg
  = UpdateTitle String
  | UpdateOption Int String
  | AddOption 
  | RemoveOption Int
  | Submit
  | SubmissionResponse (Result Http.Error ())
  | GotoMainPage

-- HTTP

makePollRequest : String -> List String -> Cmd Msg
makePollRequest title options = 
        Http.post  
        { url = targetUrlPath "/polls",
          body = jsonBody (encodePollData title options),
          expect = Http.expectWhatever SubmissionResponse
        } 

-- DECODE | ENCODE

encodePollData : String -> List String -> Encode.Value
encodePollData title options =
    Encode.object 
            [  ("title", Encode.string title),
               ("options", Encode.list Encode.string options)
            ]


-- HTML

lineBreak : Html Msg
lineBreak = br [] []

addButton : Html Msg
addButton = Html.button [type_ "button", onClick AddOption] [text "Add Option"]

removeButton : Int -> Html Msg
removeButton i = Html.button [type_ "button", onClick (RemoveOption i)] [text "Remove"]

backButton : Html Msg
backButton = button [ onClick GotoMainPage ] [text "Back"]


displayOptions : List String -> Html Msg
displayOptions s =
    ul [style "list-style-type" "none"] (List.append (List.indexedMap optionHTML s) [addButton])

optionHTML : Int -> String -> Html Msg
optionHTML index str =
    li []
        [
            Html.input
                [ type_ "text"
                , value str
                , placeholder (String.concat ["Enter option: ", String.fromInt index])
                , onInput (UpdateOption index)
                ]
                [],
            removeButton index
        ]


-- MAIN

init : () -> (Model, Cmd Msg)
init _ = {title = "", options = ["","",""], comment = "", sessionID = Nothing}
            |> noCmd --requestSession ())

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    UpdateTitle str ->
        {model | title = str}
            |> noCmd
    
    UpdateOption int str ->
        {model | options = List.indexedMap (\i val -> if i == int then str else val) model.options }
            |> noCmd

    AddOption ->
        {model | options = List.concat [model.options, [""]]}
            |> noCmd 

    RemoveOption int ->
        {model | options = dropAtIndex int model.options}
            |> noCmd
     
    Submit -> 
        {model | comment = "Poll Sent - Response Pending"}
            |> withCmd (makePollRequest model.title model.options)

    SubmissionResponse res ->
        case res of
            Ok _ -> 
                {model | comment = "Poll was Submitted"}
                    |> noCmd
            Err _ ->
                {model | comment = "Submission Error"}
                    |> noCmd

    GotoMainPage ->
        model 
            |> withCmd (Navigation.load (Url.relative ["Index.html"] []))

    

 

subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none


view : Model -> Html Msg
view model =
    div []
            [   
                div [] [text model.comment],

                    div []
                        [ label [ for "title" ] [ text "Question:" ]
                        , input
                            [ type_ "text"
                            , placeholder "Enter prompt"
                            , value model.title
                            , onInput UpdateTitle
                            ]
                            []
                        ]
                        , displayOptions model.options
                    , button
                        [ type_ "button", onClick Submit]
                        [ text "Submit Poll" ]
                    , lineBreak
                    , lineBreak
                    , backButton

            ]
