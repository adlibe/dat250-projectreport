port module Main exposing (..)

import Browser
import Browser.Navigation as Navigation
import Html exposing (Html, button, div, text, li, ul)
import Html.Attributes exposing (style)
import Html.Events exposing (onClick)
import Http exposing (request)
import Url.Builder as Url

import Vote exposing (..)
import Poll exposing (..)
import CmdAid exposing (..)
import ListAid exposing (..)
import List exposing (member)

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
port removeSession : () -> Cmd msg
port sendSession : String -> Cmd msg
port recciveSession : (String -> msg) -> Sub msg


-- MODEL

type alias Model 
    = {polls : List Poll, sessionID : Maybe String, votes : List Vote, comment : String}

type Msg
  = ReccivedSessionLocal String
  | AskSessionLocal
  | GotPolls (Result Http.Error (List Poll))
  | Reload
  | VoteOn Vote 
  | GotoMakePoll
  | GotoLogin
  | LogOut
 

-- HTTP

ownDiv : Html Msg -> Html Msg
ownDiv item = div [] [item]

pollsRequest : (Cmd Msg)
pollsRequest =
  Http.get
      { url = targetUrlPath "/polls"
      , expect = Http.expectJson GotPolls pollsDecoder
      }

voteRequest : Vote -> (Cmd Msg)
voteRequest v =
  Http.post {
     body = Http.jsonBody (encodeVote v),
     expect = Http.expectJson GotPolls pollsDecoder,
     url = targetUrlPath "/polls/vote"
  }


--- HTML

reloadButton : Html Msg
reloadButton = button [ onClick Reload ] [text "Reload"]

loginButton : Html Msg
loginButton = button [ onClick GotoLogin ] [text "Login"]

logoutButton : Html Msg
logoutButton = button [ onClick LogOut ] [text "Log Out"]

makePollButton : Html Msg 
makePollButton = button [ onClick GotoMakePoll ] [text "Create Poll"]

displayerUserInfo : Maybe String -> Html Msg
displayerUserInfo id =
    case id of 
        Nothing ->
            text "No Id"
        Just val ->
            text val

displayPolls : List Poll -> List Vote -> Html Msg
displayPolls polls votes =
    polls
    |> List.map (\ p -> li [] [text (p.title), displayOptions p.options votes])
    |> ul [style "list-style-type" "none"] 

displayOptions : List Option -> List Vote -> Html Msg
displayOptions options votes =
    options 
    |> List.map (\ o -> button [onClick (VoteOn {id = o.id})] [text o.caption,text" : ",text (String.fromInt o.votes)])
    |> ul []



-- MAIN

init : () -> (Model, Cmd Msg)
init _ = ({polls = [], sessionID = Nothing, votes = [], comment = ""}, Cmd.batch [requestSession (), pollsRequest])

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    ReccivedSessionLocal str ->
        {model | sessionID = Just str}
            |> noCmd

    AskSessionLocal -> 
        model
            |> withCmd (requestSession ())

    GotPolls res ->
        case res of 
            Ok p ->
                {model | polls = p}
                    |> (\m -> {m|comment = "Poll Reccived"})
                    |> noCmd
            Err _ ->
                {model | comment = "Incorrect poll info"}
                    |> noCmd

    Reload ->
        init () 

    VoteOn vote ->
        if model.sessionID == Nothing
            then 
                {model | comment = "You need to log in to vote "}
                    |> noCmd
            else
                if member vote model.votes 
                    then 
                        {model | comment = "Already Voted on this "}
                            |> noCmd
                    else
                        {model | votes = vote :: model.votes}
                            |> (\m -> {m | comment = "Sent Vote"})
                            |> withCmd (voteRequest vote)

    GotoMakePoll ->
        model 
            |> withCmd (Navigation.load (Url.relative ["MakePoll.html"] []))

    GotoLogin ->
        model 
            |> withCmd (Navigation.load (Url.relative ["Login.html"] []))

    LogOut ->
        model
            |> withCmd (Cmd.batch [removeSession (), Navigation.reload])




subscriptions : Model -> Sub Msg
subscriptions _ =
    recciveSession ReccivedSessionLocal


view : Model -> Html Msg
view model =
    div [] [
        ownDiv reloadButton,
        ownDiv loginButton,
        ownDiv logoutButton,
        ownDiv makePollButton,
        text "Session ID : ", 
        displayerUserInfo model.sessionID,
        div [] [text "Output: ", text model.comment],
        displayPolls model.polls model.votes
        ]
  
dlist : List Vote -> Html Msg
dlist l =
    l
    |> List.map (\v -> text (String.fromInt v.id))
    |> ul []
